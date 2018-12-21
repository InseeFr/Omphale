drop table &&prefix._&&idep._csv_population_ncal;
create table &&prefix._&&idep._csv_population_ncal
as select * from &&prefix._&&idep._proj_pyr
where age >=0;

drop table &&prefix._&&idep._csv_survivant;
create table &&prefix._&&idep._csv_survivant
as select * from &&prefix._&&idep._surv
where age >=0;

ALTER TABLE &&prefix._&&idep._csv_survivant 
RENAME COLUMN annee TO annee_pas; 


create index &&prefix._&&idep._csv_population_ncalk on &&prefix._&&idep._csv_population_ncal(age, sexe, annee, zone);
create index &&prefix._&&idep._csv_survivantk on &&prefix._&&idep._csv_survivant(age, sexe, annee_pas, zone);

ALTER TABLE &&prefix._&&idep._csv_emig 
RENAME COLUMN annee TO annee_pas;

ALTER TABLE &&prefix._&&idep._csv_immig 
RENAME COLUMN annee TO annee_pas;

alter table &&prefix._&&idep._csv_deces
RENAME COLUMN annee TO annee_pas;

update &&prefix._&&idep._csv_emig set age=age+5;
update &&prefix._&&idep._csv_immig set age=age+5;

create index &&prefix._&&idep._csv_emigcle on &&prefix._&&idep._csv_emig(age, sexe, annee_pas, zone);
create index &&prefix._&&idep._csv_immigcle on &&prefix._&&idep._csv_immig(age, sexe, annee_pas, zone);

create index &&prefix._&&idep._pcsv_emmfluxzn on &&prefix._&&idep._pcsv_emmflux(origine, destination);
delete from &&prefix._&&idep._csv_emig where age < 5;
delete from &&prefix._&&idep._csv_immig where age < 5;

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
//limitation des zones etude à celles du zonage avril 2011
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
drop table &&prefix._&&idep._csv_top_flux;      
      create table &&prefix._&&idep._csv_top_flux
      as 

      select age+5 age, sexe, annee annee_pas, origine, destination, flux from &&prefix._&&idep._pcsv_emmflux
      where (origine, destination) in (select zone_etude, zone_echange from &&prefix._&&idep._pcsv_top_flux_init)
      ;



drop table &&prefix._&&idep._csv_immig_etr;
create table &&prefix._&&idep._csv_immig_etr
as select age, sexe, annee annee_pas, zone, population from &&prefix._&&idep._immig_etrp
where annee in (select annee_pas from &&prefix._&&idep._csv_survivant) and age >= 0
;

drop table &&prefix._&&idep._csv_emig_etr;
create table &&prefix._&&idep._csv_emig_etr
as select (age-5) age, sexe, annee annee_pas, zone, population from &&prefix._&&idep._emig_etrp
where annee in (select annee_pas from &&prefix._&&idep._csv_survivant) and age >=5
;


drop table &&prefix._&&idep._csv_qf;
create table &&prefix._&&idep._csv_qf
as select * from &&prefix._&&idep._quotient_naip
where zone != '_TOTAL' and age >=0
;

drop table &&prefix._&&idep._csv_qd;
create table &&prefix._&&idep._csv_qd
as select * from &&prefix._&&idep._quotient_decp
where zone != '_TOTAL' and age >=0
;

drop table &&prefix._&&idep._csv_qe;
create table &&prefix._&&idep._csv_qe
as 
select age, sexe, annee annee_pas, origine, destination, qe from &&prefix._&&idep._quotient_emip
where origine != '_TOTAL' and destination != '_TOTAL'
and annee in (select c.annee_pas from &&prefix._&&idep._csv_survivant c)
and (origine, destination) in (select zone_etude, zone_echange from &&prefix._&&idep._pcsv_top_flux_init)
and age >=0
;

drop table &&prefix._&&idep._csv_agrege_dec;
create table &&prefix._&&idep._csv_agrege_dec
as select age, sexe, &&annee_ref. annee_ref, zone, deces from &&prefix._&&idep._agrege_dec
where zone != '_TOTAL'
and age >=0
;

drop table &&prefix._&&idep._cb_avg_nai_tmp;
create table &&prefix._&&idep._cb_avg_nai_tmp as select age, sexe, commune,  (sum(naissances))/5 population
from cb_naissances
where annee >= (&&annee_ref. -2) and annee <=(&&annee_ref. +2)
and age >=0
group by age, sexe,  commune
;

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

drop table &&prefix._&&idep._csv_agrege_immig;
create table &&prefix._&&idep._csv_agrege_immig as
select age, sexe, &&annee_ref. annee_ref, zr zone , sum(flu) flux
from &&prefix._&&idep._agregation_flux
where age >=0 and zr != za
group by age, sexe, zr 
;

delete from &&prefix._&&idep._csv_agrege_immig where zone like '_DEP_%';

drop table &&prefix._&&idep._csv_agrege_emig;
create table &&prefix._&&idep._csv_agrege_emig as
select age, sexe, &&annee_ref. annee_ref, za zone , sum(flu) flux
from &&prefix._&&idep._agregation_flux
where age >=0 and zr != za
group by age, sexe, za
;

delete from &&prefix._&&idep._csv_agrege_emig where zone like '_DEP_%';

drop table &&prefix._&&idep._csv_agrege_top_flux;      
      create table &&prefix._&&idep._csv_agrege_top_flux
      as 
      select age, sexe, &&annee_ref. annee_ref, za origine, zr destination, flu flux from &&prefix._&&idep._agregation_flux
      where age >=0 and (za, zr) in (select zone_etude, zone_echange from &&prefix._&&idep._pcsv_top_flux_init)

      ;

delete from &&prefix._&&idep._csv_survivant where zone like '_DEP_%';

drop table &&prefix._&&idep._csv_ratio_nai_enf;      
      create table &&prefix._&&idep._csv_ratio_nai_enf
      as select * from &&prefix._&&idep._ratio_nai
      ;

drop table &&prefix._&&idep._csv_agrege_act;
create table &&prefix._&&idep._csv_agrege_act
as select age, sexe, &&annee_ref. annee_ref, zone, population actifs from &&prefix._&&idep._vue_actifs
where zone != '_TOTAL' and age >=0
;

delete from &&prefix._&&idep._csv_agrege_act where zone like '_DEP_%';

drop table &&prefix._&&idep._csv_agrege_men;
create table &&prefix._&&idep._csv_agrege_men
as select age, sexe, &&annee_ref. annee_ref, zone, population menages from &&prefix._&&idep._vue_menages
where zone != '_TOTAL' and age >=0
;

delete from &&prefix._&&idep._csv_agrege_men where zone like '_DEP_%';

drop table &&prefix._&&idep._csv_qact;
create table &&prefix._&&idep._csv_qact
as select * from &&prefix._&&idep._quotient_actp
where zone != '_TOTAL' and age >=0
;

drop table &&prefix._&&idep._csv_qmen;
create table &&prefix._&&idep._csv_qmen
as select * from &&prefix._&&idep._quotient_menp
where zone != '_TOTAL' and age >=0
;

drop table &&prefix._&&idep._csv_agrege_popref;
create table &&prefix._&&idep._csv_agrege_popref
as select age, sexe, &&annee_ref. annee_ref, zone, population from &&prefix._&&idep._vue_pop_ref
where zone != '_TOTAL' and age >=0
;

delete from &&prefix._&&idep._csv_agrege_popref where zone like '_DEP_%';

drop table &&prefix._&&idep._csv_agrege_immig_etr;
create table &&prefix._&&idep._csv_agrege_immig_etr
as select age, sexe, &&annee_ref. annee_ref, zone, population from &&prefix._&&idep._vue_imm_etr
where zone != '_TOTAL' and age >=0
;

delete from &&prefix._&&idep._csv_agrege_immig_etr where zone like '_DEP_%';

