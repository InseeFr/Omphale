//parametres : 
//idep utilisateur (&&idep.)
//borne_inf (&&mere_inf.) et borne_sup(&&mere_sup.) des qf
//annee_reference (&&cycle.)
//age de regroupement(&&age_last. et plus)
//age de decalage flux (decal &&decal.)
//ancol : nb annees collecte pour un cycle : 5
//ancolq pour les tests, on divise par 4 au lieu de 5 car il manque une annee de donnees pour deces et naissances

//define idep=

//define age_last=99
//define mere_inf=14
//define mere_sup=49
//define ancol=5
//define ancolq=5

//controles


--/* On vérifie qu'il n'y a pas les données avec les années de la table zz_temp_cycles dans la table cb_pop_legale*/ 
 --/*  pour le type_pop 1 (Legale) */
insert into zz_temp_cycles_ctl select 1 from dual where 
exists (
select 1 from zz_temp_cycles a 
where a.annee not in (select b.annee from cb_pop_legale b where b.type_pop=1)
);

--/* On vérifie qu'il n'y a pas les données avec les années de la table zz_temp_cycles dans la table cb_pop_legale */
 --/* pour le type_pop 5 (Immigres) */
insert into zz_temp_cycles_ctl select 1 from dual where 
exists (
select 1 from zz_temp_cycles a 
where a.annee not in (select b.annee from cb_pop_legale b where b.type_pop=5)
);

--/* On vérifie qu'il n'y a pas les données avec les années de la table zz_temp_cycles dans la table cb_pop_legale */
--/* pour le type_pop 5 (Immigres) */
insert into zz_temp_cycles_ctl select 1 from dual where 
exists (
select 1 from zz_temp_cycles a 
where a.annee not in (select b.annee from cb_pop_legale b where b.type_pop=6)
);


--/* On vérifie qu'il n'y a pas les données avec les années de la table zz_temp_cycles dans la table cb_flux  */
insert into zz_temp_cycles_ctl select 1 from dual where 
exists (
select 1 from zz_temp_cycles a 
where a.annee not in (select b.annee from cb_flux b )
);

--/* On vérifie qu'il n'y a pas les données avec les années (N- 2)de la table zz_temp_cycles dans la table cb_deces */
insert into zz_temp_cycles_ctl select 1 from dual where 
exists (
select 1 from zz_temp_cycles a 
where (a.annee-2) not in (select b.annee from cb_deces b )
);

--/* On vérifie qu'il n'y a pas les données avec les années (N-1) de la table zz_temp_cycles dans la table cb_deces */
insert into zz_temp_cycles_ctl select 1 from dual where 
exists (
select 1 from zz_temp_cycles a 
where (a.annee-1) not in (select b.annee from cb_deces b )
);

--/* On vérifie qu'il n'y a pas les données avec les années (N) de la table zz_temp_cycles dans la table cb_deces  */
insert into zz_temp_cycles_ctl select 1 from dual where 
exists (
select 1 from zz_temp_cycles a 
where a.annee not in (select b.annee from cb_deces b )
);

--/* On vérifie qu'il n'y a pas les données avec les années (N+1) de la table zz_temp_cycles dans la table cb_deces*/
insert into zz_temp_cycles_ctl select 1 from dual where 
exists (
select 1 from zz_temp_cycles a 
where (a.annee+1) not in (select b.annee from cb_deces b )
);


--/* On vérifie qu'il n'y a pas les données avec les années (N+2) de la table zz_temp_cycles dans la table cb_deces  */
insert into zz_temp_cycles_ctl select 1 from dual where 
exists (
select 1 from zz_temp_cycles a 
where (a.annee+2) not in (select b.annee from cb_deces b )
);


--/* On vérifie qu'il n'y a pas les données avec les années (N-2) de la table zz_temp_cycles dans la table cb_naissances*/
insert into zz_temp_cycles_ctl select 1 from dual where 
exists (
select 1 from zz_temp_cycles a 
where (a.annee-2) not in (select b.annee from cb_naissances b )
);

--/* On vérifie qu'il n'y a pas les données avec les années (N-1) de la table zz_temp_cycles dans la table cb_naissances */
insert into zz_temp_cycles_ctl select 1 from dual where 
exists (
select 1 from zz_temp_cycles a 
where (a.annee-1) not in (select b.annee from cb_naissances b )
);

--/* On vérifie qu'il n'y a pas les données avec les années (N) de la table zz_temp_cycles dans la table cb_naissances */
insert into zz_temp_cycles_ctl select 1 from dual where 
exists (
select 1 from zz_temp_cycles a 
where a.annee not in (select b.annee from cb_naissances b )
);

--/* On vérifie qu'il n'y a pas les données avec les années (N+1) de la table zz_temp_cycles dans la table cb_naissances  */
insert into zz_temp_cycles_ctl select 1 from dual where 
exists (
select 1 from zz_temp_cycles a 
where (a.annee+1) not in (select b.annee from cb_naissances b )
);

--/* On vérifie qu'il n'y a pas les données avec les années (N+2) de la table zz_temp_cycles dans la table cb_naissances */
insert into zz_temp_cycles_ctl select 1 from dual where 
exists (
select 1 from zz_temp_cycles a 
where (a.annee+2) not in (select b.annee from cb_naissances b )
);

//suppression des cycles existants
--/*On supprime toutes les données dans les tables init et de moyenne avg*/
truncate table cb_pop_legale_init drop storage;
truncate table cb_flux_init drop storage;
truncate table cb_avg_dec drop storage;
truncate table cb_avg_nai drop storage;
truncate table cb_avg_nai_pop drop storage;


truncate table cb_avg_dec_ant drop storage;
truncate table cb_avg_nai_ant drop storage;

--/*************************************************///


//creation des nouveaux cycles


//population legale &&cycle. pour pyramide initiale

--/* on insère les données de cb_pop_legale dans cb_pop_legale_init  pour les années définies dans zz_temp_cycles et pour l'age inférieur à age_last (99)  */
insert into cb_pop_legale_init 
select age, sexe, commune, annee, type_pop, population from cb_pop_legale 
where annee in (select annee from zz_temp_cycles) and age < &&age_last.
;

--/* on crée une table temporaire zz_temp_pop_legale_vieux pour récupérer la population des vieux supérieurà 99 pour toutes les années disponibles dans zz_temp_cycles*/
drop table zz_temp_pop_legale_vieux;
create table zz_temp_pop_legale_vieux as
select annee, &&age_last. age, sexe, type_pop, commune, sum(population) population
from cb_pop_legale 
where annee in (select annee from zz_temp_cycles) and age >=&&age_last.
group by annee, sexe, type_pop, commune
;

--/* on insère ces données les vieux supérieur à 99 dans la table cb_pop_legale_init*/
insert into cb_pop_legale_init select age, sexe, commune, annee, type_pop, population from zz_temp_pop_legale_vieux;
drop table zz_temp_pop_legale_vieux;


		
		
//flux &&cycle.

insert into cb_flux_init 
select age, sexe, destination, origine, annee, flux  from cb_flux 
where annee in (select annee from zz_temp_cycles) and age < &&age_last.
;
drop table zz_temp_flux_init_vieux;
create table zz_temp_flux_init_vieux as
select annee, &&age_last. age, sexe, destination, origine, sum(flux) flux
from cb_flux 
where annee in (select annee from zz_temp_cycles) and age >= &&age_last.
group by annee, sexe, destination, origine
;
insert into cb_flux_init
select age, sexe, destination, origine, annee, flux from zz_temp_flux_init_vieux
;
drop table zz_temp_flux_init_vieux;




//deces cycle &&cycle.

insert into cb_avg_dec select a.age, a.sexe, a.commune, b.annee, (sum(a.deces))/&&ancolq. deces
from cb_deces a, zz_temp_cycles b
where a.annee >= (b.annee-2) and a.annee <=(b.annee+2)
group by a.age, a.sexe, a.commune, b.annee
;


drop table zz_temp_avg_dec_vieux;
create table zz_temp_avg_dec_vieux as select annee, &&age_last. age, sexe, commune, sum(deces) deces
from cb_avg_dec
where age >=&&age_last. 
group by annee,  sexe, commune
;

delete from cb_avg_dec where age >=&&age_last. ;
insert into cb_avg_dec select age, sexe, commune, annee, deces from zz_temp_avg_dec_vieux;
drop table zz_temp_avg_dec_vieux;

--/* mise à jour table cb_avg_dec_ant*/

insert into cb_avg_dec_ant select a.age, a.sexe, a.commune, b.annee, (sum(a.deces))/&&ancolq. deces
from cb_deces a, zz_temp_cycles b
where a.annee >= (b.annee-3) and a.annee <=(b.annee+1)
group by a.age, a.sexe, a.commune, b.annee
;

drop table zz_temp_avg_dec_ant_vieux;
create table zz_temp_avg_dec_ant_vieux as select annee, &&age_last. age, sexe, commune, sum(deces) deces
from cb_avg_dec_ant
where age >=&&age_last. 
group by annee,  sexe, commune
;

delete from cb_avg_dec_ant where age >=&&age_last. ;
insert into cb_avg_dec_ant select age, sexe, commune, annee, deces from zz_temp_avg_dec_ant_vieux;
drop table zz_temp_avg_dec_ant_vieux;



//naissance pour calcul des quotients de fecondite
//naissances cycle &&cycle.
//on additionne les sexes
 // on groupe les ages extremes de la plage de fecondite and age>=&&mere_inf. and age <=&&mere_sup.

insert into cb_avg_nai select a.age, a.commune, b.annee, (sum(a.naissances))/&&ancolq. naissances
from cb_naissances a, zz_temp_cycles b
where a.annee >= (b.annee-2) and a.annee <=(b.annee+2)
group by a.age,  a.commune, b.annee
;



drop table zz_temp_avg_nai_vieux;
create table zz_temp_avg_nai_vieux as select &&mere_sup. age,  commune, annee, sum(naissances) naissances
from cb_avg_nai
where age >=&&mere_sup.  
group by annee,  commune
;

delete from cb_avg_nai where age >=&&mere_sup. ;
insert into cb_avg_nai select age, commune, annee, naissances from zz_temp_avg_nai_vieux;
drop table zz_temp_avg_nai_vieux;

create table zz_temp_avg_nai_vieux as select &&mere_inf. age,  commune, annee, sum(naissances) naissances
from cb_avg_nai
where age <=&&mere_inf. 
group by annee,  commune
;

delete from cb_avg_nai where age <=&&mere_inf.  ;
insert into cb_avg_nai select age, commune, annee, naissances from zz_temp_avg_nai_vieux;
drop table zz_temp_avg_nai_vieux;


--/* mise à jour table cb_avg_nai_ant*/

insert into cb_avg_nai_ant select a.age, a.commune, b.annee, (sum(a.naissances))/&&ancolq. naissances
from cb_naissances a, zz_temp_cycles b
where a.annee >= (b.annee-3) and a.annee <=(b.annee+1)
group by a.age,  a.commune, b.annee
;



drop table zz_temp_avg_nai_ant_vieux;
create table zz_temp_avg_nai_ant_vieux as select &&mere_sup. age,  commune, annee, sum(naissances) naissances
from cb_avg_nai_ant
where age >=&&mere_sup.  
group by annee,  commune
;

delete from cb_avg_nai_ant where age >=&&mere_sup. ;
insert into cb_avg_nai_ant select age, commune, annee, naissances from zz_temp_avg_nai_ant_vieux;
drop table zz_temp_avg_nai_ant_vieux;

create table zz_temp_avg_nai_ant_vieux as select &&mere_inf. age,  commune, annee, sum(naissances) naissances
from cb_avg_nai_ant
where age <=&&mere_inf. 
group by annee,  commune
;

delete from cb_avg_nai_ant where age <=&&mere_inf.  ;
insert into cb_avg_nai_ant select age, commune, annee, naissances from zz_temp_avg_nai_ant_vieux;
drop table zz_temp_avg_nai_ant_vieux;


//naissance pour calcul des population age -1
//population nee cycle &&cycle.
//on additionne les ages de la mere

insert into cb_avg_nai_pop select a.sexe, a.commune,b.annee, (sum(naissances))/&&ancolq. population
from cb_naissances a, zz_temp_cycles b
where a.annee >= (b.annee-2) and a.annee <=(b.annee+2)
group by a.sexe,  a.commune, b.annee
;
commit;
--/*************************************************/

drop table zz_temp_cycles;
drop table zz_temp_cycles_ctl;

