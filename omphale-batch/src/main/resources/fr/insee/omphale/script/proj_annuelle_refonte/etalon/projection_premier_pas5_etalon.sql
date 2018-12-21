//parametres : 



//projection_premier_pas5_etalon.sql
//
//_pas_a_traiter <c- _pas
//_proj_pyr <c- _vue_pop_ref, _nb_pas
//_proj_pyr <i- _nb_pas
//_proj_pyr <i- _nb_pas
//_surv <c- _proj_pyr
//_surv <u- _emig_etrp
//_csv_deces <c- _surv
//_csv_deces <u- _quotient_surv
//_surv <u- _quotient_surv
//_emmflux <c- _quotient_emip, _surv
//_emmigf <c- _emmflux
//_immigf <c- _emmflux
//_immigf <d-
//_emmigf <d-
//_solde_mig_prep <c- _pas_a_traiter, _immigf, _emmigf
//_solde_mig_prep <i- _pas_a_traiter, _emmigf, _immigf
//_solde_mig_prep <u- _emig_etrp
//_solde_mig_prep <u- _immig_etrp
//_solde_mig <c- _solde_mig_prep
//_proj_prep <c- _surv, _pas_a_traiter
//_proj_prep <u- _immig_etrp
//_proj_prep <u- _immigf
//_proj_prep <u- _emmigf
//_proj_pyr <i- _proj_prep
//_proj_pyr_vieux <c- _proj_pyr, _pas_a_traiter
//_proj_pyr <d- _pas_a_traiter
//_proj_pyr <i- _proj_pyr_vieux
//_proj_pyr_vieux <d-
//_csv_naissances <c- _proj_pyr, _quotient_naip, _pas_a_traiter
//_proj_pyr_nai <c- _proj_pyr, _quotient_naip, _pas_a_traiter
//_proj_pyr <i- _proj_pyr_nai, _ratio_nai
//_proj_pyr <i- _proj_pyr_nai, _ratio_nai
//_proj_pyr_nai <d-
//_csv_emig <c- _emmigf
//_csv_immig <c- _immigf
//_pcsv_emmflux <c- _emmflux





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
//define annee_hor=2012
//define part_gars=0.512
//define generation=0




	//Traitements
	//creation table resultat
	drop table &&prefix._&&idep._pas_a_traiter;
	create table &&prefix._&&idep._pas_a_traiter as
	select * from &&prefix._&&idep._pas
	where annee_pas=(select min(b.annee_pas) from &&prefix._&&idep._pas b
	where b.a_faire=0);

--/*on récupère dans proj_pyr la population par age et sexe pour une annee de référence */

		drop table &&prefix._&&idep._proj_pyr;
		create table &&prefix._&&idep._proj_pyr as
		select a.age, a.sexe, c.annee_ref annee, a.zone, a.population
		from &&prefix._&&idep._vue_pop_ref a, &&prefix._&&idep._nb_pas c
		where 1 = 2
		;

	--/* on récupère les génération 0 et 1 */
		--/*on récupère dans proj_pyr la population par age et sexe pour la génération 0 de vue_pop_ref*/
			insert into &&prefix._&&idep._proj_pyr 
			select a.age, a.sexe, c.annee_ref annee, a.zone, a.population
			from &&prefix._&&idep._vue_pop_ref a, &&prefix._&&idep._nb_pas c
			where &&generation.=0
			;
		
		--/*on récupère dans proj_pyr la population par age et sexe pour la génération 1 de vue_generation*/
			insert into &&prefix._&&idep._proj_pyr 
			select a.age, a.sexe, c.annee_ref annee, a.zone, a.population
			from &&prefix._&&idep._vue_generation a, &&prefix._&&idep._nb_pas c
			where &&generation.=1
			;

	--/*on stocke les données de proj_pyr dans la table _surv */	
		drop table &&prefix._&&idep._surv;
		create table &&prefix._&&idep._surv as
		select * from &&prefix._&&idep._proj_pyr
		;

create index &&prefix._&&idep._proj_pyr_cle on &&prefix._&&idep._proj_pyr(age, sexe, annee, zone);
create index &&prefix._&&idep._surv_cle on &&prefix._&&idep._surv(age, sexe, annee, zone);



//				Calcul des non émigrants vers l’étranger
//non migrants vers etranger
	create index &&prefix._&&idep._emig_etrp_cle on &&prefix._&&idep._emig_etrp(age, sexe, annee, zone);
	create index &&prefix._&&idep._immig_etrp_cle on &&prefix._&&idep._immig_etrp(age, sexe, annee, zone);

--/*on supprime de la table _surv les émigrants de l'étranger de l'étranger */
	update &&prefix._&&idep._surv a set a.population=
	(select a.population-b.population from &&prefix._&&idep._emig_etrp b
	where b.age=(a.age+&&decal_annee.) and b.sexe=a.sexe and b.zone=a.zone and a.annee = b.annee)
	where (a.age+&&decal_annee., a.sexe, a.annee, a.zone) in (select age, sexe, annee, zone from &&prefix._&&idep._emig_etrp)
	;

drop table &&prefix._&&idep._csv_deces;
create table &&prefix._&&idep._csv_deces as
select * from &&prefix._&&idep._surv;
create index &&prefix._&&idep._csv_deces_k on &&prefix._&&idep._csv_deces(age, sexe, annee, zone);

update &&prefix._&&idep._csv_deces a set a.population
=(select a.population*(1-b.qs) from &&prefix._&&idep._quotient_surv b
where b.age=a.age and b.sexe=a.sexe and b.annee=a.annee and b.zone=a.zone)
where (a.age, a.sexe, a.annee, a.zone) in (select age, sexe, annee, zone from &&prefix._&&idep._quotient_surv)
;

--/* on supprime de la table _surv les décés*/
update &&prefix._&&idep._surv a set a.population
=(select a.population*b.qs from &&prefix._&&idep._quotient_surv b
where b.age=a.age and b.sexe=a.sexe and b.annee=a.annee and b.zone=a.zone)
where (a.age, a.sexe, a.annee, a.zone) in (select age, sexe, annee, zone from &&prefix._&&idep._quotient_surv)
;



drop table &&prefix._&&idep._emmflux;
create table &&prefix._&&idep._emmflux as select a.age, a.sexe, a.annee, a.origine , a.destination,
b.population*a.qe flux
from &&prefix._&&idep._quotient_emip a,  &&prefix._&&idep._surv b
where a.age=b.age and a.sexe=b.sexe and a.annee=b.annee and a.origine=b.zone
;


drop table &&prefix._&&idep._emmigf;
create table &&prefix._&&idep._emmigf as select age, sexe, annee, origine zone,
sum(flux) flux
from &&prefix._&&idep._emmflux
group by age, sexe, annee, origine
;

drop table &&prefix._&&idep._immigf;
create table &&prefix._&&idep._immigf as select age, sexe, annee, destination zone,
sum(flux) flux
from &&prefix._&&idep._emmflux
group by age, sexe, annee, destination
;


create index &&prefix._&&idep._emmigf_cle on &&prefix._&&idep._emmigf(age, sexe, annee, zone);
create index &&prefix._&&idep._immigf_cle on &&prefix._&&idep._immigf(age, sexe, annee, zone);

delete from &&prefix._&&idep._immigf where flux is null;
delete from &&prefix._&&idep._emmigf where flux is null;



//				Résultante des flux : potentiellement modifiable
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

//resultante des flux plus immigres de l etranger

--/* on récupère dans une table _pro_prep les données de _surv*/
drop table &&prefix._&&idep._proj_prep;
create table &&prefix._&&idep._proj_prep as
select * from &&prefix._&&idep._surv a, &&prefix._&&idep._pas_a_traiter c
where a.annee=c.annee_pas
;

--/* on ajoute de _proj_prep les immigrants de l'étranger*/
update &&prefix._&&idep._proj_prep a set a.population=
(select a.population+nvl(b.population,0) from &&prefix._&&idep._immig_etrp b
where b.age=(a.age+&&decal_annee.) and b.sexe=a.sexe and b.zone=a.zone and a.annee = b.annee)
where (a.age+&&decal_annee., a.sexe, a.annee, a.zone) in (select age, sexe, annee, zone from &&prefix._&&idep._immig_etrp)
;

--/** on ajoute les immigrants interne à _proj_prep/
update &&prefix._&&idep._proj_prep a set a.population=
(select a.population+nvl(b.flux,0) from &&prefix._&&idep._immigf b
where b.age=a.age and b.sexe=a.sexe and b.zone=a.zone and a.annee = b.annee)
where (a.age, a.sexe, a.annee, a.zone) in (select age, sexe, annee, zone from &&prefix._&&idep._immigf)
;

--/*on suprrime l'émigration interne*/
update &&prefix._&&idep._proj_prep a set a.population=
(select a.population-nvl(b.flux,0) from &&prefix._&&idep._emmigf b
where b.age=a.age and b.sexe=a.sexe and b.zone=a.zone and a.annee = b.annee)
where (a.age, a.sexe, a.annee, a.zone) in (select age, sexe, insère zone from &&prefix._&&idep._emmigf)
;
commit;

--/* on insère les données de proj_prep dans proj_pyr en décalant les âges et les années de 1 an*/
insert into &&prefix._&&idep._proj_pyr 
select distinct a.age+&&decal_annee. age, a.sexe, a.annee+&&decal_annee. annee, a.zone, a.population
from &&prefix._&&idep._proj_prep a
;


--/** on agrège dans proj_pyr les plus de 99 ans pour les années décalées de 1 an/
	--/** on récupère les vieux de proj_pyr pour les années décalées de 1 an dans proj_pyr_vieux/
	drop table &&prefix._&&idep._proj_pyr_vieux;
	create table &&prefix._&&idep._proj_pyr_vieux as
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
--/* résultats */
--/* les naissances par zone, sexe et âge (de la mère) (naissances_detaillees) */ 
-- /* 	pour l’ensemble des années charnières (labellisées année de début de pas) (CSV_NAISSANCES) ;*/
	drop table &&prefix._&&idep._csv_naissances;
	create table &&prefix._&&idep._csv_naissances
	as select (a.annee - &&decal_annee.) annee_pas, a.zone, a.age,
	sum(a.population*b.qf) naissances, 
	sum(a.population*b.qf)*&&part_gars. gars, 
	sum(a.population*b.qf)*(1-&&part_gars.) filles
	from &&prefix._&&idep._proj_pyr a, &&prefix._&&idep._quotient_naip b, &&prefix._&&idep._pas_a_traiter c
	where a.age=b.age and a.zone=b.zone and a.annee=b.annee and a.annee=c.annee_pas+&&decal_annee. and a.sexe=2
	group by a.annee, a.zone, a.age;

--/*on récupère les enfants de proj_pyr en appliquant le taux de qf de quotient_naip dans une table proj_pyr_nai*/
//ajout des enfants de 0 à 4 ans
	drop table &&prefix._&&idep._proj_pyr_nai;
	create table &&prefix._&&idep._proj_pyr_nai
	as select a.annee, a.zone, sum(a.population*b.qf) naissances
	from &&prefix._&&idep._proj_pyr a, &&prefix._&&idep._quotient_naip b, &&prefix._&&idep._pas_a_traiter c
	where a.age=b.age and a.zone=b.zone and a.annee=b.annee and a.annee=c.annee_pas+&&decal_annee. and a.sexe=2
	group by a.annee, a.zone;

--/* on insère les données pour les garçons de _proj_pyr_nai auquel on applique le ratio_nai avec part_gars dans _proj_pyr*/
	insert into &&prefix._&&idep._proj_pyr a
	select b.age, 1 sexe, a.annee, a.zone, a.naissances*b.ratio*&&part_gars.
	from &&prefix._&&idep._proj_pyr_nai a, &&prefix._&&idep._ratio_nai b
	where a.zone=b.zone
	;

--/* on insère les données pour les filles de _proj_pyr_nai auquel on applique le ratio_nai avec part_gars dans _proj_pyr*/
	insert into &&prefix._&&idep._proj_pyr a
	select b.age, 2 sexe, a.annee, a.zone, a.naissances*b.ratio*(1-&&part_gars.)
	from &&prefix._&&idep._proj_pyr_nai a, &&prefix._&&idep._ratio_nai b
	where a.zone=b.zone
	;

delete from &&prefix._&&idep._proj_pyr_nai;
commit;


//				Calcul des flux : potentiellement modifiable


//csv flux
drop table &&prefix._&&idep._csv_emig;
create table &&prefix._&&idep._csv_emig
as select * from &&prefix._&&idep._emmigf
where 1=2;
drop table &&prefix._&&idep._csv_immig;
create table &&prefix._&&idep._csv_immig
as select * from &&prefix._&&idep._immigf
where 1=2;
drop table &&prefix._&&idep._pcsv_emmflux;
create table &&prefix._&&idep._pcsv_emmflux
as select * from &&prefix._&&idep._emmflux
where 1=2
;
