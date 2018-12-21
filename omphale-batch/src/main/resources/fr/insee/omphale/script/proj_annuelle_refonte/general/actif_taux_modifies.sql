//Calcul d’une population calée projetée en tranches d’âge quinquennales PopOmphQ à partir de la population issue de la projection Pop Omphale (calée projetée) par zone sexe et âge
DROP TABLE &&prefix._&&idep._POPOMPHQ;    
CREATE TABLE &&prefix._&&idep._POPOMPHQ AS SELECT a.ANNEE, a.SEXE, a.ZONE, SUM(a.POPULATION) pop, TA.AGEQ, TA.MIN, TA.MAX 
FROM &&prefix._&&idep._PROJ_PYR a INNER JOIN AGEQ TA ON a.AGE 
BETWEEN TA.MIN AND TA.MAX GROUP BY TA.AGEQ, a.ANNEE, a.SEXE, a.ZONE, TA.MIN, TA.MAX;     


//Vérification et correction du taux calculé pour les tranches d âges de 50 ans et plus

//Calcul des poids des populations pour les tranches d age compris entre 50 ans et 65 ans
//Ex : Poids50-54 = (Pop 50-54)/(Pop 50-54 + Pop 55-59) 
 
DROP VIEW &&prefix._&&idep._Poids;
CREATE VIEW &&prefix._&&idep._Poids AS SELECT  a.POP/(a.POP + b.POP) poids,
a.ANNEE, a.SEXE, a.ZONE, a.AGEQ, a.MIN, a.MAX 
FROM &&prefix._&&idep._POPOMPHQ a INNER 
JOIN &&prefix._&&idep._POPOMPHQ b On (b.MIN = A.MIN+5)  AND a.ANNEE=b.ANNEE AND a.SEXE=b.SEXE AND a.ZONE=b.ZONE AND a.MIN>=50;  


//ajout du poids à la table &&prefix._&&idep._p_ta dans une nouvelle vue
DROP TABLE &&prefix._&&idep._p_ta_pds;
CREATE TABLE &&prefix._&&idep._p_ta_pds AS SELECT a.ANNEE, a.SEXE, a.QA, a.ZONE, a.AGEQ, a.MAX, a.MIN, b.POIDS FROM &&prefix._&&idep._p_ta a INNER JOIN &&prefix._&&idep._Poids b 
On a.ANNEE = b.ANNEE AND a.SEXE=b.SEXE AND a.ZONE=b.ZONE 
AND a.AGEQ = b.AGEQ AND a.MIN=b.MIN AND a.MAX=b.MAX;


//Calcul des écarts de taux et application du correctif
//Ex : si (Qact 60-64 - Qact 55-59) > 0 alors
//  Qact 55-59-corrigé = Qact 55-59 + (Qact 60-64 - Qact 55-59) * Poids55-59
//  Qact 60-64-corrigé = Qact 60-64 - (Qact 60-64 - Qact 55-59) * (1-Poids55-59)
// si (Qact 60-64 - Qact 55-59) <= 0 alors on ne modifie pas les Qact.

create unique index &&prefix._&&idep._p_ta_pds_cle on &&prefix._&&idep._p_ta_pds (MIN, ZONE, SEXE, ANNEE);
create unique index &&prefix._&&idep._p_ta_cle on &&prefix._&&idep._p_ta (MIN, ZONE, SEXE, ANNEE);


//si (Qact 55-59 - Qact 50-54) > 0 alors    Qact 50-54-corrigé = Qact 50-54 + (Qact 55-59 - Qact 50-54) * Poids50-54
UPDATE &&prefix._&&idep._p_ta a SET a.QA = (
    CASE
        WHEN ( SIGN((SELECT b.QA FROM &&prefix._&&idep._p_ta_pds b WHERE b.MIN=55 AND b.ZONE=a.ZONE AND b.SEXE=a.SEXE AND b.ANNEE=a.ANNEE)
                                             - a.QA)>0) 
                        THEN a.QA + ((SELECT b.QA FROM &&prefix._&&idep._p_ta_pds b WHERE b.MIN=55 AND b.ZONE=a.ZONE AND b.SEXE=a.SEXE AND b.ANNEE=a.ANNEE)
                                                      - a.QA)*(SELECT b.POIDS FROM &&prefix._&&idep._p_ta_pds b WHERE b.MIN=50 AND b.ZONE=a.ZONE AND b.SEXE=a.SEXE AND b.ANNEE=a.ANNEE)
        ELSE a.QA
    END
    )
    WHERE (a.MIN=50); 

//si (Qact 55-59 - Qact 50-54) > 0 alors Qact 55-59-corrigé = Qact 55-59 - (Qact 55-59 - Qact 50-54) * (1-Poids 50-54)
UPDATE &&prefix._&&idep._p_ta a SET a.QA = (
    CASE
        WHEN ( SIGN(a.QA - (SELECT b.QA FROM &&prefix._&&idep._p_ta_pds b WHERE b.MIN=50 AND b.ZONE=a.ZONE AND b.SEXE=a.SEXE AND b.ANNEE=a.ANNEE))>0) 
                        THEN a.QA - (a.QA - (SELECT b.QA FROM &&prefix._&&idep._p_ta_pds b WHERE b.MIN=50 AND b.ZONE=a.ZONE AND b.SEXE=a.SEXE AND b.ANNEE=a.ANNEE)
                                                      )*(1-(SELECT b.POIDS FROM &&prefix._&&idep._p_ta_pds b WHERE b.MIN=50 AND b.ZONE=a.ZONE AND b.SEXE=a.SEXE AND b.ANNEE=a.ANNEE))
        ELSE a.QA
    END
    )
    WHERE (a.MIN=55);  

//Mise a jour de la table &&prefix._&&idep._p_ta_pds avec les valeurs corrigées
UPDATE &&prefix._&&idep._p_ta_pds a SET a.QA = 
        (SELECT b.QA FROM &&prefix._&&idep._p_ta b WHERE b.MIN = a.MIN AND b.ANNEE=a.ANNEE AND b.SEXE=a.SEXE AND b.ZONE = a.ZONE)    
        WHERE a.MIN = 50;
UPDATE &&prefix._&&idep._p_ta_pds a SET a.QA = 
        (SELECT b.QA FROM &&prefix._&&idep._p_ta b WHERE b.MIN = a.MIN AND b.ANNEE=a.ANNEE AND b.SEXE=a.SEXE AND b.ZONE = a.ZONE)    
        WHERE a.MIN = 55;



//si (Qact 60-64 - Qact 55-59) > 0 alors    Qact 55-59-corrigé = Qact 55-59corrigé + (Qact 60-64 - Qact 55-59corrigé) * Poids 55-59
UPDATE &&prefix._&&idep._p_ta a SET a.QA = (
    CASE
        WHEN ( SIGN((SELECT b.QA FROM &&prefix._&&idep._p_ta_pds b WHERE b.MIN=60 AND b.ZONE=a.ZONE AND b.SEXE=a.SEXE AND b.ANNEE=a.ANNEE)
                                             - a.QA)>0) 
                        THEN a.QA + ((SELECT b.QA FROM &&prefix._&&idep._p_ta_pds b WHERE b.MIN=60 AND b.ZONE=a.ZONE AND b.SEXE=a.SEXE AND b.ANNEE=a.ANNEE)
                              - a.QA)*(SELECT b.POIDS FROM &&prefix._&&idep._p_ta_pds b WHERE b.MIN=55 AND b.ZONE=a.ZONE AND b.SEXE=a.SEXE AND b.ANNEE=a.ANNEE)
        ELSE a.QA
    END
    )
    WHERE (a.MIN=55); 
    
//si (Qact 60-64 - Qact 55-59) > 0 alors      Qact 60-64-corrigé = Qact 60-64 - (Qact 60-64 - Qact 55-59corrigé) * (1-Poids 55-59)
UPDATE &&prefix._&&idep._p_ta a SET a.QA = (
    CASE
        WHEN ( SIGN(a.QA - (SELECT b.QA FROM &&prefix._&&idep._p_ta_pds b WHERE b.MIN=55 AND b.ZONE=a.ZONE AND b.SEXE=a.SEXE AND b.ANNEE=a.ANNEE))>0) 
                        THEN a.QA - (a.QA - (SELECT b.QA FROM &&prefix._&&idep._p_ta_pds b WHERE b.MIN=55 AND b.ZONE=a.ZONE AND b.SEXE=a.SEXE AND b.ANNEE=a.ANNEE)
                                                      )*(1-(SELECT b.POIDS FROM &&prefix._&&idep._p_ta_pds b WHERE b.MIN=55 AND b.ZONE=a.ZONE AND b.SEXE=a.SEXE AND b.ANNEE=a.ANNEE))
        ELSE a.QA
    END
    )
    WHERE (a.MIN=60); 
    
//Mise a jour de la table &&prefix._&&idep._p_ta_pds avec les valeurs corrigées
UPDATE &&prefix._&&idep._p_ta_pds a SET a.QA = 
        (SELECT b.QA FROM &&prefix._&&idep._p_ta b WHERE b.MIN = a.MIN AND b.ANNEE=a.ANNEE AND b.SEXE=a.SEXE AND b.ZONE = a.ZONE)    
        WHERE a.MIN = 55;
UPDATE &&prefix._&&idep._p_ta_pds a SET a.QA = 
        (SELECT b.QA FROM &&prefix._&&idep._p_ta b WHERE b.MIN = a.MIN AND b.ANNEE=a.ANNEE AND b.SEXE=a.SEXE AND b.ZONE = a.ZONE)    
        WHERE a.MIN = 60;
    
    
//si (Qact 65-69 - Qact 60-64) > 0 alors        Qact 60-64-corrigé = Qact 60-64corrigé + (Qact 65-69 - Qact 60-64corrigé) * Poids 60-64
UPDATE &&prefix._&&idep._p_ta a SET a.QA = (
    CASE
        WHEN ( SIGN((SELECT b.QA FROM &&prefix._&&idep._p_ta_pds b WHERE b.MIN=65 AND b.ZONE=a.ZONE AND b.SEXE=a.SEXE AND b.ANNEE=a.ANNEE)
                                             - a.QA)>0) 
                        THEN a.QA + ((SELECT b.QA FROM &&prefix._&&idep._p_ta_pds b WHERE b.MIN=65 AND b.ZONE=a.ZONE AND b.SEXE=a.SEXE AND b.ANNEE=a.ANNEE)
                              - a.QA)*(SELECT b.POIDS FROM &&prefix._&&idep._p_ta_pds b WHERE b.MIN=60 AND b.ZONE=a.ZONE AND b.SEXE=a.SEXE AND b.ANNEE=a.ANNEE)
        ELSE a.QA
    END
    )
    WHERE (a.MIN=60); 
    
//si (Qact 65-69 - Qact 60-64) > 0 alors      Qact 65-69-corrigé = Qact 65-69 - (Qact 65-69 - Qact 60-64corrigé) * (1-Poids 60-64)
UPDATE &&prefix._&&idep._p_ta a SET a.QA = (
    CASE
        WHEN ( SIGN(a.QA - (SELECT b.QA FROM &&prefix._&&idep._p_ta_pds b WHERE b.MIN=60 AND b.ZONE=a.ZONE AND b.SEXE=a.SEXE AND b.ANNEE=a.ANNEE))>0) 
                        THEN a.QA - (a.QA - (SELECT b.QA FROM &&prefix._&&idep._p_ta_pds b WHERE b.MIN=60 AND b.ZONE=a.ZONE AND b.SEXE=a.SEXE AND b.ANNEE=a.ANNEE)
                                                      )*(1-(SELECT b.POIDS FROM &&prefix._&&idep._p_ta_pds b WHERE b.MIN=60 AND b.ZONE=a.ZONE AND b.SEXE=a.SEXE AND b.ANNEE=a.ANNEE))
        ELSE a.QA
    END
    )
    WHERE a.MIN=65; 


//Mise a jour de la table &&prefix._&&idep._p_ta_pds avec les valeurs corrigées
UPDATE &&prefix._&&idep._p_ta_pds a SET a.QA = 
        (SELECT b.QA FROM &&prefix._&&idep._p_ta b WHERE b.MIN = a.MIN AND b.ANNEE=a.ANNEE AND b.SEXE=a.SEXE AND b.ZONE = a.ZONE)    
        WHERE a.MIN = 60;
UPDATE &&prefix._&&idep._p_ta_pds a SET a.QA = 
        (SELECT b.QA FROM &&prefix._&&idep._p_ta b WHERE b.MIN = a.MIN AND b.ANNEE=a.ANNEE AND b.SEXE=a.SEXE AND b.ZONE = a.ZONE)    
        WHERE a.MIN = 65; 


//si (Qact 70+ - Qact 65-69) > 0 alors      Qact 65-69-corrigé = Qact 65-69corrigé + (Qact 70-+ - Qact 65-69corrigé) * Poids 65-69
UPDATE &&prefix._&&idep._p_ta a SET a.QA = (
    CASE
        WHEN ( SIGN((SELECT b.QA FROM &&prefix._&&idep._p_ta b WHERE b.MIN=70 AND b.ZONE=a.ZONE AND b.SEXE=a.SEXE AND b.ANNEE=a.ANNEE)
                                             - a.QA)>0) 
                        THEN a.QA + ((SELECT b.QA FROM &&prefix._&&idep._p_ta b WHERE b.MIN=70 AND b.ZONE=a.ZONE AND b.SEXE=a.SEXE AND b.ANNEE=a.ANNEE)
                             - a.QA)*(SELECT b.POIDS FROM &&prefix._&&idep._p_ta_pds b WHERE b.MIN=65 AND b.ZONE=a.ZONE AND b.SEXE=a.SEXE AND b.ANNEE=a.ANNEE)
        ELSE a.QA
    END
    )
    WHERE a.MIN=65; 
    
//si (Qact 70+ - Qact 65-69) > 0 alors    Qact 70-+-corrigé = Qact 70-+ - (Qact 70-+ - Qact 65-69corrigé) * (1-Poids 65-69)    
UPDATE &&prefix._&&idep._p_ta a SET a.QA = (
    CASE
        WHEN ( SIGN(a.QA - (SELECT b.QA FROM &&prefix._&&idep._p_ta_pds b WHERE b.MIN=65 AND b.ZONE=a.ZONE AND b.SEXE=a.SEXE AND b.ANNEE=a.ANNEE))>0) 
                        THEN a.QA - (a.QA - (SELECT b.QA FROM &&prefix._&&idep._p_ta_pds b WHERE b.MIN=65 AND b.ZONE=a.ZONE AND b.SEXE=a.SEXE AND b.ANNEE=a.ANNEE)
                                                      )*(1-(SELECT b.POIDS FROM &&prefix._&&idep._p_ta_pds b WHERE b.MIN=65 AND b.ZONE=a.ZONE AND b.SEXE=a.SEXE AND b.ANNEE=a.ANNEE))
        ELSE a.QA
    END
    )
    WHERE  a.MIN=70; 