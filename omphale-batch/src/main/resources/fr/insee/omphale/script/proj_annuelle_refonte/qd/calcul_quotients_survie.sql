//              Algorithme de projection
//                  Calcul des quotients de survie : potentiellement modifiable



//calcul_quotients_survie.sql
//
//_quotient_surv <c- _quotient_decp, _pas
//_quotient_decp <i- _quotient_decp
//_quotient_decp <i- _quotient_decp
//_quotient_decp <i- _quotient_decp
//_quotient_decp <i- _quotient_decp
//_quotient_decp <i- _quotient_decp
//_quotient_surv2 <c- _quotient_decp, _pas
//_quotient_surv3 <c- _quotient_decp, _pas
//_quotient_surv4 <c- _quotient_decp, _pas
//_quotient_surv5 <c- _quotient_decp, _pas
//_quotient_surv <u- _quotient_surv2
//_quotient_surv <u- _quotient_surv3
//_quotient_surv <u- _quotient_surv4
//_quotient_surv <u- _quotient_surv


//parametres : 

//&gt. ;

//define annee_hor=2012
//define zonage_id=0
//define idep=
//define prefix=zp
//define annee_ref=2007
//define age_old=80
//define age_last=99
//define mere_inf=14
//define mere_sup=49
//define decal_annee=1
//define def_projection_id=3290
//define calage=0




//Traitements
//calcul des quotients survie quinquenaux pour chaque pas de projection

drop table &&prefix._&&idep._quotient_surv;
create table &&prefix._&&idep._quotient_surv
as select age, sexe, annee, zone, 1-qd qs from &&prefix._&&idep._quotient_decp
where annee in (select annee_pas from &&prefix._&&idep._pas)
;
create index &&prefix._&&idep._quotient_surv_cle on &&prefix._&&idep._quotient_surv(age, sexe, annee, zone);






