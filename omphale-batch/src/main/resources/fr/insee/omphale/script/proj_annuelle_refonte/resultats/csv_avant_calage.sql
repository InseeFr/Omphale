--/*Export des résultats : RG_120 -  résultats - typologie */



--/*Résumé des manipulations de tables dans ce script SQL à partir d'autres tables */
// attention cette liste n est plus tout a fait a jour
//csv_avant_calage.sql
//
//_csv_population_ncal <c- _proj_pyr
//_csv_survivant <c- _surv
//_csv_emig <u-
//_csv_immig <u-
//_csv_emig <d-
//_csv_immig <d-
//_csv_top_flux_init <c- _AGREGATION_FLUX 
//_csv_top_flux_init <d-
//_pcsv_top_flux_init <c- _csv_top_flux_init
//_pcsv_top_flux_init <i- _csv_top_flux_init
//_csv_top_flux <c- _pcsv_emmflux, _pcsv_top_flux_init
//_csv_immig_etr <c- _immig_etrp, _csv_survivant
//_csv_emig_etr <c- _emig_etrp, _csv_survivant
//_csv_qf <c- _quotient_naip
//_csv_qd <c- _quotient_decp
//_csv_qe <c- _quotient_emip, _csv_survivant, _pcsv_top_flux_init
//_csv_agrege_dec <c- _agrege_dec
//_cb_avg_nai_tmp <c- cb_naissances
//_csv_agrege_nai <c- _cb_avg_nai_tmp, _zonage
//_csv_agrege_nai <d-
//_csv_agrege_immig <c- _agregation_flux
//_csv_agrege_immig <d-
//_csv_agrege_emig <c- _agregation_flux
//_csv_agrege_emig <d-
//_csv_agrege_top_flux <c- _agregation_flux, _pcsv_top_flux_init
//_csv_survivant <d-
//_csv_ratio_nai_enf <c- _ratio_nai
//_csv_agrege_act <c- _vue_actifs
//_csv_agrege_act <d-
//_csv_agrege_men <c- _vue_menages
//_csv_agrege_men <d-
//_csv_qact <c- _quotient_actp
//_csv_qmen <c- _quotient_menp
//_csv_agrege_popref <c- _vue_pop_ref
//_csv_agrege_popref <d-
//_csv_agrege_immig_etr <c-_vue_imm_etr
//_csv_agrege_immig_etr <d-


--/* les populations détaillées par zone, sexe et âge, */
--/*  pour l’ensemble des années de projection et des zones d’étude (non calées) */
--/*  (CSV_POPULATION_NCAL, CSV_POPULATION_MEN, CSV_POPULATION_ACT) ; * */

drop table &&prefix._&&idep._csv_population_ncal;
create table &&prefix._&&idep._csv_population_ncal
as select * from &&prefix._&&idep._proj_pyr
where age >=0;


--/* si la projection est calée : les populations calées par zone, sexe et âge, */
--/*    pour l’ensemble des années de projection et des zones d’étude ; */
--/* VOIR le script SQL user/calage_user_fin.sql table _CSV_POPULATION_CAL*/



--/*résultats*/
--/*  les survivants par zone, sexe et âge */
--/* 		pour l’ensemble des années charnières (labellisées année de début de pas) */
--/* (CSV_SURVIVANT) ;  */

drop table &&prefix._&&idep._csv_survivant;
create table &&prefix._&&idep._csv_survivant
as select * from &&prefix._&&idep._surv
where age >=0;

ALTER TABLE &&prefix._&&idep._csv_survivant 
RENAME COLUMN annee TO annee_pas; 


create index &&prefix._&&idep._csv_population_ncalk on &&prefix._&&idep._csv_population_ncal(age, sexe, annee, zone);
create index &&prefix._&&idep._csv_survivantk on &&prefix._&&idep._csv_survivant(age, sexe, annee_pas, zone);



--/*résultats*/
--/* les flux par zone, sexe et âge pour toutes les années */
--/* * 			vers les zones d’échange privilégiées (fluxzone, zone d’échange.,sexe,age), */
--/* * 			et agrégés pour l’ensemble des zones d’échange (fluxzone,.,sexe,age) */
--/* * 			(émigrants hors de la zone) (labellisés année de début de pas) (CSV_EMIG);*/

ALTER TABLE &&prefix._&&idep._csv_emig 
RENAME COLUMN annee TO annee_pas;



--/*résultats*/
--/**/
--/* les flux par zone, sexe et âge pour toutes les années */
--/* 			en provenance des zones d’échange privilégiées (fluxzone d’échange, zone.,sexe,age), */
--/* 			et agrégés pour l’ensemble des zones d’échange (flux.,zone,sexe,age) (immigrants vers la zone)*/ 
--/* 			(labellisés année de début de pas) (CSV_IMMIG) ;*/
--/* */
ALTER TABLE &&prefix._&&idep._csv_immig 
RENAME COLUMN annee TO annee_pas;


--/* résultats*/
--/* */
--/* les décès détaillés par zone, sexe et âge, */
--/* 		pour l’ensemble des années de projection et des zones d’étude (deces_ad) */
--/*		(labellisés année de début de pas) (CSV_DECES) ;*/
--/* */
--/* */
alter table &&prefix._&&idep._csv_deces
RENAME COLUMN annee TO annee_pas;


--/* résultats*/
--/* */
--/* les flux par zone, sexe et âge pour toutes les années */
--/* 			vers les zones d’échange privilégiées (fluxzone, zone d’échange.,sexe,age), */
--/* 			et agrégés pour l’ensemble des zones d’échange (fluxzone,.,sexe,age) */
--/*			(émigrants hors de la zone) (labellisés année de début de pas) (CSV_EMIG);*/
--/* */
--/* */
update &&prefix._&&idep._csv_emig set age=age+1;


--/* résultats */
 --/**  */
--/** les flux par zone, sexe et âge pour toutes les années  */
--/* * 			en provenance des zones d’échange privilégiées (fluxzone d’échange, zone.,sexe,age),  */
--/* * 			et agrégés pour l’ensemble des zones d’échange (flux.,zone,sexe,age) (immigrants vers la zone)  */
--/* * 			(labellisés année de début de pas) (CSV_IMMIG) ; */
--/* * */
update &&prefix._&&idep._csv_immig set age=age+1;

create index &&prefix._&&idep._csv_emigcle on &&prefix._&&idep._csv_emig(age, sexe, annee_pas, zone);
create index &&prefix._&&idep._csv_immigcle on &&prefix._&&idep._csv_immig(age, sexe, annee_pas, zone);

create index &&prefix._&&idep._pcsv_emmfluxzn on &&prefix._&&idep._pcsv_emmflux(origine, destination);

--/*/*résultats*/
--/* * */
--/* *  les flux par zone, sexe et âge pour toutes les années */
--/* * 			vers les zones d’échange privilégiées (fluxzone, zone d’échange.,sexe,age), */
--/* * 			et agrégés pour l’ensemble des zones d’échange (fluxzone,.,sexe,age) */
--/* * 			(émigrants hors de la zone) (labellisés année de début de pas) (CSV_EMIG);*/
--/* * */
--/* * */
delete from &&prefix._&&idep._csv_emig where age < 1;

--/* résultats*/
--/* * */
 --/** les flux par zone, sexe et âge pour toutes les années */
--/* * 			en provenance des zones d’échange privilégiées (fluxzone d’échange, zone.,sexe,age), */
--/* * 			et agrégés pour l’ensemble des zones d’échange (flux.,zone,sexe,age) (immigrants vers la zone) */
 --/** 			(labellisés année de début de pas) (CSV_IMMIG) ;*/
 --/** */
delete from &&prefix._&&idep._csv_immig where age < 1;



drop table &&prefix._&&idep._csv_top_flux_init;      
create table &&prefix._&&idep._csv_top_flux_init
   as select
        zone_etude,
        zone_echange,
        flu_max flux
    from
        (select
            nvl(flux_sortant.zone_etude,
            flux_entr.zone_etude) zone_etude,
            nvl(flux_sortant.zone_echange,
            flux_entr.zone_echange) zone_echange,
            greatest(nvl(flux_sortant.flux_sortant,
            0),
            nvl(flux_entr.flux_entr,
            0)) flu_max,
            row_number() over(partition       
        by
            nvl(flux_sortant.zone_etude,
            flux_entr.zone_etude)       
        order by
            greatest(nvl(flux_sortant.flux_sortant,
            0),
            nvl(flux_entr.flux_entr,
            0)) desc) num    
        from
            (select
                zone_etude,
                zone_echange,
                flux_sortant    
            from
                (select
                    za zone_etude,
                    zr zone_echange,
                    sum(flu) flux_sortant,
                    row_number() over(partition          
                by
                    za          
                order by
                    sum(flu) desc) num       
                from
                    &&prefix._&&idep._AGREGATION_FLUX       
                where
                    za != zr       
                group by
                    za,
                    zr       
                order by
                    za,
                    sum(flu) desc)    
            where
                num <= &&zonage_nb_flux.
            ) flux_sortant    full 
        outer join
            (
                select
                    zone_etude,
                    zone_echange,
                    flux_entr    
                from
                    (select
                        za zone_echange,
                        zr zone_etude,
                        sum(flu) flux_entr,
                        row_number() over(partition          
                    by
                        zr          
                    order by
                        sum(flu) desc) num       
                    from
                        &&prefix._&&idep._AGREGATION_FLUX       
                    where
                        za != zr       
                    group by
                        za,
                        zr       
                    order by
                        zr,
                        sum(flu) desc)    
                where
                    num <= &&zonage_nb_flux.
                ) flux_entr    
                    on flux_sortant.zone_etude = flux_entr.zone_etude    
                    and flux_sortant.zone_echange = flux_entr.zone_echange    
            order by
                zone_etude,
                flu_max desc
        ) 
    where
        num <= &&zonage_nb_flux. 
    order by
        zone_etude,
        flu_max desc
;
create index &&prefix._&&idep._csv_top_flux_initz on &&prefix._&&idep._csv_top_flux_init(zone_etude, zone_echange);
//limitation des zones etude a celles du zonage avril 2011
delete from &&prefix._&&idep._csv_top_flux_init where zone_etude like '_DEP_%';
commit;
//
drop table &&prefix._&&idep._pcsv_top_flux_init;      
create table &&prefix._&&idep._pcsv_top_flux_init
as 
select
        zone_etude,
        zone_echange
from &&prefix._&&idep._csv_top_flux_init
where 1=2;

insert into &&prefix._&&idep._pcsv_top_flux_init a
select
        b.zone_etude,
        b.zone_echange
from &&prefix._&&idep._csv_top_flux_init b
union
select
        c.zone_echange,
        c.zone_etude
from &&prefix._&&idep._csv_top_flux_init c
;

create index &&prefix._&&idep._pcsv_top_flux_initz on &&prefix._&&idep._pcsv_top_flux_init(zone_etude, zone_echange);


--/*/* résultats*/
--/* * */
--/* * les flux vers et en provenance des zones d’échange « privilégiées » */ 
--/* * 		pour chaque zone du zonage et pour chaque pas de projection. */
--/* * 		Ces données sont à valeur sur cinq ans, ventilées selon la zone d’origine et */
--/* * 		la zone de destination, et exprimées dans l’âge en fin de pas de projection (CSV_TOP_FLUX).*/
--/* * */
--/* * */
--/* * */
drop table &&prefix._&&idep._csv_top_flux;      
create table &&prefix._&&idep._csv_top_flux
as 
select age+1 age, sexe, annee annee_pas, origine, destination, flux from &&prefix._&&idep._pcsv_emmflux
where (origine, destination) in (select zone_etude, zone_echange from &&prefix._&&idep._pcsv_top_flux_init)
;


--/*/**/
--/* * les immigrants de l’étranger (immig_etr) */
--/* * par zone, sexe et âge (labellisés année de début de pas) (CVS_IMMIG_ETR);*/
--/* * */
--/* * */
drop table &&prefix._&&idep._csv_immig_etr;
create table &&prefix._&&idep._csv_immig_etr
as select age, sexe, annee annee_pas, zone, population from &&prefix._&&idep._immig_etrp
where annee in (select annee_pas from &&prefix._&&idep._csv_survivant) and age >= 0
;


--/*/**/
--/**  les émigrants de l’étranger (emig_etr_ad) */
--/** par zone, sexe et âge (labellisés année de début de pas) (CSV_EMIG_ETR) ;*/
--/* * */
drop table &&prefix._&&idep._csv_emig_etr;
create table &&prefix._&&idep._csv_emig_etr
as select (age-1) age, sexe, annee annee_pas, zone, population from &&prefix._&&idep._emig_etrp
where annee in (select annee_pas from &&prefix._&&idep._csv_survivant) and age >=1
;



--/*/**/
--/* * les quotients de fécondité et de décès détaillés par zone, sexe et âge,*/
 --/** *pour l’ensemble des années de projection et des zones d’étude (CSV_QF, CSV_QD) ;   */
--/* * */
drop table &&prefix._&&idep._csv_qf;
create table &&prefix._&&idep._csv_qf
as select * from &&prefix._&&idep._quotient_naip
where zone != '_TOTAL' and age >=0
;


--/*/**/
--/* * les quotients de fécondité et de décès détaillés par zone, sexe et âge, */
 --/** *pour l’ensemble des années de projection et des zones d’étude (CSV_QF, CSV_QD) ;   */
 --/** */
drop table &&prefix._&&idep._csv_qd;
create table &&prefix._&&idep._csv_qd
as select * from &&prefix._&&idep._quotient_decp
where zone != '_TOTAL' and age >=0
;


--/* */
 --/* * les quotients d’émigration détaillés correspondant aux flux mentionnés ci-dessus, */
 --/* * pour chaque année charnière (labellisés année de début de pas) (CVS_QE) ;*/
  --/** */
drop table &&prefix._&&idep._csv_qe;
create table &&prefix._&&idep._csv_qe
as 
select age, sexe, annee annee_pas, origine, destination, qe from &&prefix._&&idep._quotient_emip
where origine != '_TOTAL' and destination != '_TOTAL'
and annee in (select c.annee_pas from &&prefix._&&idep._csv_survivant c)
and (origine, destination) in (select zone_etude, zone_echange from &&prefix._&&idep._pcsv_top_flux_init)
and age >=0
;


 --/*/**/
 --/* * les décès par zone, sexe et âge pour l’année de référence (CSV_AGREGE_DEC) ;*/
 --/* * */
 --/* * */*/
drop table &&prefix._&&idep._csv_agrege_dec;
create table &&prefix._&&idep._csv_agrege_dec
as select age, sexe, &&annee_ref. annee_ref, zone, deces from &&prefix._&&idep._agrege_dec
where zone != '_TOTAL'
and age >=0
;


 --/*/**/
 --/* * LC vérifie si il faut l'ajouter dans les spéc ou si déjà présents*/
 --/* * */
 --/* * */
drop table &&prefix._&&idep._cb_avg_nai_tmp;
create table &&prefix._&&idep._cb_avg_nai_tmp as select age, sexe, commune,  (sum(naissances))/5 population
from cb_naissances
where annee >= (&&annee_ref. -2) and annee <=(&&annee_ref. +2)
and age >=0
group by age, sexe,  commune
;


--/*/**/
--/* * les naissances par zone, sexe et âge (de la mère) */
--/** pour l’année de référence (CSV_AGREGE_NAI) ;*/
--/** */
drop table &&prefix._&&idep._csv_agrege_nai;
create table &&prefix._&&idep._csv_agrege_nai as
select * from (
select age, sexe, &&annee_ref. annee_ref, decode(grouping_id(zone), 0,zone,'_TOTAL') zone, sum(population) naissances
from (
select a.*, nvl(b.zone,'_COMPLEMENT') zone
from &&prefix._&&idep._cb_avg_nai_tmp a, &&prefix._&&idep._zonage b
where a.commune=b.commune(+)
)
group by age, sexe, cube(zone)
) where zone !='_TOTAL' and zone !='_COMPLEMENT'
;

delete from &&prefix._&&idep._csv_agrege_nai where zone like '_DEP_%';




--/*/**/
--/** les flux par zone, sexe et âge pour l’année du cycle de référence*/ 
--/**	en provenance des zones d’échange privilégiées (fluxzone d’échange, zone.,sexe,age), */
--/**	et agrégés pour l’ensemble des zones d’échange (flux.,zone,sexe,age) */
--/**	(immigrants vers la zone) (CSV_AGREGE_IMMIG);*/
 --/** */
 --/** */

drop table &&prefix._&&idep._csv_agrege_immig;
create table &&prefix._&&idep._csv_agrege_immig as
select age, sexe, &&annee_ref. annee_ref, zr zone , sum(flu) flux
from &&prefix._&&idep._agregation_flux
where age >=0 and zr != za
group by age, sexe, zr 
;

delete from &&prefix._&&idep._csv_agrege_immig where zone like '_DEP_%';


 --/* /* les flux par zone, sexe et âge pour l’année du cycle de référence */
 --/* 	vers les zones d’échange privilégiées (fluxzone, zone d’échange.,sexe,age), */
 --/* 	et agrégés pour l’ensemble des zones d’échange (fluxzone,.,sexe,age) */
 --/* 	(émigrants hors de la zone) (CSV_AGREGE_EMIG) ;*/
 --/*  */
 --/*  */
drop table &&prefix._&&idep._csv_agrege_emig;
create table &&prefix._&&idep._csv_agrege_emig as
select age, sexe, &&annee_ref. annee_ref, za zone , sum(flu) flux
from &&prefix._&&idep._agregation_flux
where age >=0 and zr != za
group by age, sexe, za
;

delete from &&prefix._&&idep._csv_agrege_emig where zone like '_DEP_%';


 --/* /**/
 --/*  * les flux vers et en provenance des zones d’échange « privilégiées »  */
 --/* *	pour chaque zone du zonage et pour l'année de référence. */
 --/* *	Ces données sont à valeur sur cinq ans, ventilées */
 --/* *	selon la zone d’origine et la zone de destination, */
 --/* *	et exprimées dans l’âge en fin de pas de projection (CSV_AGREGE_TOP_FLUX).*/
 --/*  * */
  --/* * */
  --/* * */
drop table &&prefix._&&idep._csv_agrege_top_flux;      
      create table &&prefix._&&idep._csv_agrege_top_flux
      as 
      select age, sexe, &&annee_ref. annee_ref, za origine, zr destination, flu flux from &&prefix._&&idep._agregation_flux
      where age >=0 and (za, zr) in (select zone_etude, zone_echange from &&prefix._&&idep._pcsv_top_flux_init)

      ;


//			Calcul des survivants 
delete from &&prefix._&&idep._csv_survivant where zone like '_DEP_%';


  --/*/* */
  --/* * les ratios « naissances - enfants » pour chaque zone et l’âge 0 an (ratio_nais_enf) (CSV_RATIO_NAI_ENF); */
  --/* *  */
  --/* *  */
  --/* *  */
  --/* * */
drop table &&prefix._&&idep._csv_ratio_nai_enf;      
      create table &&prefix._&&idep._csv_ratio_nai_enf
      as select * from &&prefix._&&idep._ratio_nai
      ;

   --/*/**/
   --/** la population de référence, les ménages et les actifs par zone, sexe et âge */
   --/**	pour l’année de référence (CSV_AGREGE_POPREF, CSV_AGREGE_MEN et CSV_AGREGE_ACT) ;*/
   --/* * */
   --/* * */

   --/*/*à modifier car ici prend en compte france métro */
   --/* * au final devra prendre en compte encore que france métro mais les données après chargement contiendront*/
    --/** france entière */
    --/** */
drop table &&prefix._&&idep._csv_agrege_popref;
create table &&prefix._&&idep._csv_agrege_popref
as select age, sexe, &&annee_ref. annee_ref, zone, population from &&prefix._&&idep._vue_pop_ref
where zone != '_TOTAL' and age >=0
;

delete from &&prefix._&&idep._csv_agrege_popref where zone like '_DEP_%';



    --/*/**/
    --/* * */
     --/** les immigrants de l’étranger (immig_etr) */
    --/**	par zone, sexe et âge (labellisés année de début de pas) */
    --/**	pour l’année de référence (CSV_AGREGE_IMMIG_ETR) ;*/
    --/* * */
    --/* * */
drop table &&prefix._&&idep._csv_agrege_immig_etr;
create table &&prefix._&&idep._csv_agrege_immig_etr
as select age, sexe, &&annee_ref. annee_ref, zone, population from &&prefix._&&idep._vue_imm_etr
where zone != '_TOTAL' and age >=0
;

delete from &&prefix._&&idep._csv_agrege_immig_etr where zone like '_DEP_%';

