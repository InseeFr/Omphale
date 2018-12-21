//projection_un_pas5_etalon.sql
//
//_pas_a_traiter <i- _pas
//_surv <i- _proj_pyr, _pas_a_traiter
//_surv <u- _emig_etrp, _pas_a_traiter
//_csv_deces <i- _surv, _pas_a_traiter
//_csv_deces <u- _quotient_surv, _pas_a_traiter
//_surv <u- _quotient_surv, _pas_a_traiter
//_emmflux <i- _quotient_emip, _surv, _pas_a_traiter
//_emmigf <i- _emmflux
//_immigf <i- _emmflux
//_solde_mig_prep <c- _pas_a_traiter, _immigf, _emmigf
//_solde_mig_prep <i- _pas_a_traiter, _emmigf, _immigf
//_solde_mig_prep <u- _emig_etrp
//_solde_mig_prep <u- _immig_etrp
//_solde_mig <c- _solde_mig_prep
//_proj_prep <c- _surv, _pas_a_traiter
//_proj_prep <u- _immig_etrp
//_proj_prep <u- _immigf
//_proj_prep <u- _emmigf
//_proj_pyr <i- proj_prep
//_proj_pyr_vieux <i- _proj_pyr, _pas_a_traiter
//_proj_pyr <d- _pas_a_traiter
//_proj_pyr <i- _proj_pyr_vieux
//_proj_pyr_vieux <d-
//_csv_naissances <i- _proj_pyr, _quotient_naip, _pas_a_traiter
//_proj_pyr_nai <i- _proj_pyr, _quotient_naip, _pas_a_traiter
//_proj_pyr <i- _proj_pyr_nai, _ratio_nai
//_proj_pyr <i- _proj_pyr_nai, _ratio_nai
//_proj_pyr_nai <d-


//&gt. ;
//parametres : 

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
//define part_gars=0.512
//define generation=0

//Traitements
//creation table resultat
insert into &&prefix._&&idep._pas_a_traiter 
select * from &&prefix._&&idep._pas
where annee_pas=(select min(b.annee_pas) from &&prefix._&&idep._pas b
where b.a_faire=0);

insert into &&prefix._&&idep._surv 
select a.* from &&prefix._&&idep._proj_pyr a
where a.annee=(select annee_pas from &&prefix._&&idep._pas_a_traiter)
;


//non migrants vers etranger

update &&prefix._&&idep._surv a set a.population=
(select a.population-b.population from &&prefix._&&idep._emig_etrp b
where b.age=(a.age+&&decal_annee.) and b.sexe=a.sexe and b.zone=a.zone and a.annee = b.annee)
where (a.age+&&decal_annee., a.sexe, a.annee, a.zone) in (select age, sexe, annee, zone from &&prefix._&&idep._emig_etrp)
and a.annee=(select annee_pas from &&prefix._&&idep._pas_a_traiter)
;


insert into &&prefix._&&idep._csv_deces 
select * from &&prefix._&&idep._surv where annee=(select annee_pas from &&prefix._&&idep._pas_a_traiter)
;


update &&prefix._&&idep._csv_deces a set a.population
=(select a.population*(1-b.qs) from &&prefix._&&idep._quotient_surv b
where b.age=a.age and b.sexe=a.sexe and b.annee=a.annee and b.zone=a.zone)
where (a.age, a.sexe, a.annee, a.zone) in (select age, sexe, annee, zone from &&prefix._&&idep._quotient_surv)
and a.annee=(select annee_pas from &&prefix._&&idep._pas_a_traiter)
;



update &&prefix._&&idep._surv a set a.population
=(select a.population*b.qs from &&prefix._&&idep._quotient_surv b
where b.age=a.age and b.sexe=a.sexe and b.annee=a.annee and b.zone=a.zone)
where (a.age, a.sexe, a.annee, a.zone) in (select age, sexe, annee, zone from &&prefix._&&idep._quotient_surv)
and a.annee=(select annee_pas from &&prefix._&&idep._pas_a_traiter)
;


insert into &&prefix._&&idep._emmflux select a.age, a.sexe, a.annee, a.origine , a.destination,
b.population*a.qe flux
from &&prefix._&&idep._quotient_emip a,  &&prefix._&&idep._surv b
where a.age=b.age and a.sexe=b.sexe and a.annee=b.annee and a.origine=b.zone
and a.annee=(select annee_pas from &&prefix._&&idep._pas_a_traiter)
;


insert into &&prefix._&&idep._emmigf select age, sexe, annee, origine zone,
sum(flux) flux
from &&prefix._&&idep._emmflux
group by age, sexe, annee, origine
;

insert into &&prefix._&&idep._immigf select age, sexe, annee, destination zone,
sum(flux) flux
from &&prefix._&&idep._emmflux
group by age, sexe, annee, destination
;


/////////////////////

drop table &&prefix._&&idep._solde_mig_prep;
create table &&prefix._&&idep._solde_mig_prep
as select a.age, a.sexe, a.annee, a.zone, a.flux-nvl(b.flux,0) flux
from &&prefix._&&idep._pas_a_traiter c, 
&&prefix._&&idep._immigf a, &&prefix._&&idep._emmigf b
where a.annee=c.annee_pas
and a.age=b.age(+) and a.annee=b.annee(+) and a.sexe=b.sexe(+) and a.zone=b.zone(+)
;
insert into &&prefix._&&idep._solde_mig_prep
select age, sexe, annee, zone, flux from(
select a.age, a.sexe, a.annee, a.zone, (-1 * a.flux) flux, b.flux immig
from &&prefix._&&idep._pas_a_traiter c, 
&&prefix._&&idep._emmigf a, &&prefix._&&idep._immigf b
where a.annee=c.annee_pas
and a.age=b.age(+) and a.annee=b.annee(+) and a.sexe=b.sexe(+) and a.zone=b.zone(+)
)
where immig is null
;

update &&prefix._&&idep._solde_mig_prep a set a.flux=
(select a.flux-b.population from &&prefix._&&idep._emig_etrp b
where b.age=(a.age+&&decal_annee.) and b.sexe=a.sexe and b.zone=a.zone and a.annee = b.annee)
where (a.age+&&decal_annee., a.sexe, a.annee, a.zone) in (select age, sexe, annee, zone from &&prefix._&&idep._emig_etrp)
;

update &&prefix._&&idep._solde_mig_prep a set a.flux=
(select a.flux+b.population from &&prefix._&&idep._immig_etrp b
where b.age=(a.age+&&decal_annee.) and b.sexe=a.sexe and b.zone=a.zone and a.annee = b.annee)
where (a.age+&&decal_annee., a.sexe, a.annee, a.zone) in (select age, sexe, annee, zone from &&prefix._&&idep._immig_etrp)
;

drop table &&prefix._&&idep._solde_mig;
create table &&prefix._&&idep._solde_mig as
select a.age+&&decal_annee. age, a.sexe, a.annee+&&decal_annee. annee, a.zone, a.flux
from &&prefix._&&idep._solde_mig_prep a
;

////////////////////


drop table &&prefix._&&idep._proj_prep;
create table &&prefix._&&idep._proj_prep as
select * from &&prefix._&&idep._surv a, &&prefix._&&idep._pas_a_traiter c
where a.annee=c.annee_pas
;

update &&prefix._&&idep._proj_prep a set a.population=
(select a.population+nvl(b.population,0) from &&prefix._&&idep._immig_etrp b
where b.age=(a.age+&&decal_annee.) and b.sexe=a.sexe and b.zone=a.zone and a.annee = b.annee)
where (a.age+&&decal_annee., a.sexe, a.annee, a.zone) in (select age, sexe, annee, zone from &&prefix._&&idep._immig_etrp)
;

update &&prefix._&&idep._proj_prep a set a.population=
(select a.population+nvl(b.flux,0) from &&prefix._&&idep._immigf b
where b.age=a.age and b.sexe=a.sexe and b.zone=a.zone and a.annee = b.annee)
where (a.age, a.sexe, a.annee, a.zone) in (select age, sexe, annee, zone from &&prefix._&&idep._immigf)
;

update &&prefix._&&idep._proj_prep a set a.population=
(select a.population-nvl(b.flux,0) from &&prefix._&&idep._emmigf b
where b.age=a.age and b.sexe=a.sexe and b.zone=a.zone and a.annee = b.annee)
where (a.age, a.sexe, a.annee, a.zone) in (select age, sexe, annee, zone from &&prefix._&&idep._emmigf)
;
commit;

insert into &&prefix._&&idep._proj_pyr 
select distinct a.age+&&decal_annee. age, a.sexe, a.annee+&&decal_annee. annee, a.zone, a.population
from &&prefix._&&idep._proj_prep a
;


insert into &&prefix._&&idep._proj_pyr_vieux
select &&age_last. age, sexe, annee, zone, sum(population) population
from &&prefix._&&idep._proj_pyr a, &&prefix._&&idep._pas_a_traiter c
where annee=c.annee_pas+&&decal_annee. and age >=&&age_last.
group by annee, sexe, zone
;
delete from &&prefix._&&idep._proj_pyr 
where age >=&&age_last. and annee=(select annee_pas+&&decal_annee. from &&prefix._&&idep._pas_a_traiter);
insert into &&prefix._&&idep._proj_pyr  select age, sexe,  annee, zone, population from &&prefix._&&idep._proj_pyr_vieux;
delete from &&prefix._&&idep._proj_pyr_vieux;

///////////////////fichier csv des naissances
insert into &&prefix._&&idep._csv_naissances
select (a.annee - &&decal_annee.) annee_pas, a.zone, a.age,
sum(a.population*b.qf) naissances, 
sum(a.population*b.qf)*&&part_gars. gars, 
sum(a.population*b.qf)*(1-&&part_gars.) filles
from &&prefix._&&idep._proj_pyr a, &&prefix._&&idep._quotient_naip b, &&prefix._&&idep._pas_a_traiter c
where a.age=b.age and a.zone=b.zone and a.annee=b.annee and a.annee=c.annee_pas+&&decal_annee. and a.sexe=2
group by a.annee, a.zone, a.age;

//ajout des enfants de 0 a 4 ans

insert into &&prefix._&&idep._proj_pyr_nai
select a.annee, a.zone, sum(a.population*b.qf) naissances
from &&prefix._&&idep._proj_pyr a, &&prefix._&&idep._quotient_naip b, &&prefix._&&idep._pas_a_traiter c
where a.age=b.age and a.zone=b.zone and a.annee=b.annee and a.annee=c.annee_pas+&&decal_annee. and a.sexe=2
group by a.annee, a.zone;

insert into &&prefix._&&idep._proj_pyr a
select b.age, 1 sexe, a.annee, a.zone, a.naissances*b.ratio*&&part_gars.
from &&prefix._&&idep._proj_pyr_nai a, &&prefix._&&idep._ratio_nai b
where a.zone=b.zone
;

insert into &&prefix._&&idep._proj_pyr a
select b.age, 2 sexe, a.annee, a.zone, a.naissances*b.ratio*(1-&&part_gars.)
from &&prefix._&&idep._proj_pyr_nai a, &&prefix._&&idep._ratio_nai b
where a.zone=b.zone
;

delete from &&prefix._&&idep._proj_pyr_nai;



commit;