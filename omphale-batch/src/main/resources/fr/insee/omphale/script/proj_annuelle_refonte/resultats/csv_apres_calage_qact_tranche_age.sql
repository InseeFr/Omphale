  --/*/**/
  --/** la population de référence, les ménages et les actifs par zone, sexe et âge */
  --/** pour l’année de référence (CSV_AGREGE_POPREF, CSV_AGREGE_MEN et CSV_AGREGE_ACT) ;*/
  --/* * */
  --/* * */
drop table &&prefix._&&idep._csv_agrege_act;
create table &&prefix._&&idep._csv_agrege_act
as select age, sexe, &&annee_ref. annee_ref, zone, population actifs from &&prefix._&&idep._vue_actifs
where zone != '_TOTAL' and age >=0
;

delete from &&prefix._&&idep._csv_agrege_act where zone like '_DEP_%';


  --/*/**/
  --/** la population de référence, les ménages et les actifs par zone, sexe et âge */
  --/** pour l’année de référence (CSV_AGREGE_POPREF, CSV_AGREGE_MEN et CSV_AGREGE_ACT) ;*/
  --/* * */
   --/** */
drop table &&prefix._&&idep._csv_agrege_men;
create table &&prefix._&&idep._csv_agrege_men
as select age, sexe, &&annee_ref. annee_ref, zone, population menages from &&prefix._&&idep._vue_menages
where zone != '_TOTAL' and age >=0
;

delete from &&prefix._&&idep._csv_agrege_men where zone like '_DEP_%';


   --/*/* les taux d’activité et les taux de chefs de ménages par zone, sexe et âge, */
   --/* * *pour l’ensemble année de projection et des zones d’étude (CSV_QACT, CSV_QMEN) ;*/
   --/* * */
drop table &&prefix._&&idep._csv_qmen;
create table &&prefix._&&idep._csv_qmen
as select * from &&prefix._&&idep._quotient_menp
where zone != '_TOTAL' and age >=0
;
   
--/*/* les taux d’activité et les taux de chefs de ménages par zone, sexe et âge, */
   --/* * *pour l’ensemble année de projection et des zones d’étude (CSV_QACT, CSV_QMEN) ;*/
    --/** pour les méthodes sur les actifs (main, sur, para ou log)*/
drop table &&prefix._&&idep._csv_qact;
create table &&prefix._&&idep._csv_qact
as select a.SEXE, a.ANNEE, a.ZONE, a.QA, a.AGEQ from &&prefix._&&idep._p_ta a
where zone != '_TOTAL';