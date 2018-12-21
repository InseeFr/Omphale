/**********************************************************************************************/
/* Commune script init *************************************************************************/
/**********************************************************************************************/

drop table zz_temp_couple_com_liee_ctl;
create table zz_temp_couple_com_liee_ctl (numero number);
create unique index zz_temp_couple_com_liee_ctlk on zz_temp_couple_com_liee_ctl(numero);
insert into zz_temp_couple_com_liee_ctl	values (1);
insert into zz_temp_couple_com_liee_ctl select 1 from dual where 'COMMUNE' != 'COMMUNE';
insert into zz_temp_couple_com_liee_ctl select 1 from dual where 'COMMUNE_LIEE' != 'COMMUNE_LIEE';


drop table zz_temp_commune;
create table zz_temp_commune as select * from commune where 1 = 2;

/**********************************************************************************************/
/* deces script init *************************************************************************/
/**********************************************************************************************/

drop table zz_temp_deces_ctl;
create table zz_temp_deces_ctl (numero number);
create unique index zz_temp_deces_ctlk on zz_temp_deces_ctl(numero);
insert into zz_temp_deces_ctl	values (1);
insert into zz_temp_deces_ctl select 1 from dual where 'AGE' != 'AGE';
insert into zz_temp_deces_ctl select 1 from dual where 'ANNEE' != 'ANNEE';
insert into zz_temp_deces_ctl select 1 from dual where 'COMMUNE' != 'COMMUNE';
insert into zz_temp_deces_ctl select 1 from dual where 'DECES' != 'DECES';
insert into zz_temp_deces_ctl select 1 from dual where 'SEXE' != 'SEXE';
drop table zz_temp_deces;
create table zz_temp_deces as select * from cb_deces where 1 = 2;

/**********************************************************************************************/
/* flux script init *************************************************************************/
/**********************************************************************************************/

drop table zz_temp_flux_ctl;
create table zz_temp_flux_ctl (numero number);
create unique index zz_temp_flux_ctlk on zz_temp_flux_ctl(numero);
insert into zz_temp_flux_ctl	values (1);
insert into zz_temp_flux_ctl select 1 from dual where 'AGE' != 'AGE';
insert into zz_temp_flux_ctl select 1 from dual where 'ANNEE' != 'ANNEE';
insert into zz_temp_flux_ctl select 1 from dual where 'DESTINATION' != 'DESTINATION';
insert into zz_temp_flux_ctl select 1 from dual where 'FLUX' != 'FLUX';
insert into zz_temp_flux_ctl select 1 from dual where 'ORIGINE' != 'ORIGINE';
insert into zz_temp_flux_ctl select 1 from dual where 'SEXE' != 'SEXE';
drop table zz_temp_flux;
create table zz_temp_flux as select * from cb_flux where 1 = 2;

/**********************************************************************************************/
/* naissances script init *************************************************************************/
/**********************************************************************************************/

drop table zz_temp_naissances_ctl;
create table zz_temp_naissances_ctl (numero number);
create unique index zz_temp_naissances_ctlk on zz_temp_naissances_ctl(numero);
insert into zz_temp_naissances_ctl	values (1);
insert into zz_temp_naissances_ctl select 1 from dual where 'AGE' != 'AGE';
insert into zz_temp_naissances_ctl select 1 from dual where 'ANNEE' != 'ANNEE';
insert into zz_temp_naissances_ctl select 1 from dual where 'COMMUNE' != 'COMMUNE';
insert into zz_temp_naissances_ctl select 1 from dual where 'NAISSANCES' != 'NAISSANCES';
insert into zz_temp_naissances_ctl select 1 from dual where 'SEXE' != 'SEXE';
drop table zz_temp_naissances;
create table zz_temp_naissances as select * from cb_naissances where 1 = 2;

/**********************************************************************************************/
/* pop_legale script init *************************************************************************/
/**********************************************************************************************/

drop table zz_temp_pop_legale_ctl;
create table zz_temp_pop_legale_ctl (numero number);
create unique index zz_temp_pop_legale_ctlk on zz_temp_pop_legale_ctl(numero);
insert into zz_temp_pop_legale_ctl	values (1);
insert into zz_temp_pop_legale_ctl select 1 from dual where 'AGE' != 'AGE';
insert into zz_temp_pop_legale_ctl select 1 from dual where 'ANNEE' != 'ANNEE';
insert into zz_temp_pop_legale_ctl select 1 from dual where 'COMMUNE' != 'COMMUNE';
insert into zz_temp_pop_legale_ctl select 1 from dual where 'POPULATION' != 'POPULATION';
insert into zz_temp_pop_legale_ctl select 1 from dual where 'SEXE' != 'SEXE';
insert into zz_temp_pop_legale_ctl select 1 from dual where 'TYPE_POP' != 'TYPE_POP';
drop table zz_temp_pop_legale;
create table zz_temp_pop_legale as select * from cb_pop_legale where 1 = 2;

/**********************************************************************************************/
/* cycles script init *************************************************************************/
/**********************************************************************************************/

drop table zz_temp_cycles_ctl;
create table zz_temp_cycles_ctl (numero number);
create unique index zz_temp_cycles_ctlk on zz_temp_cycles_ctl(numero);
insert into zz_temp_cycles_ctl	values (1);
insert into zz_temp_cycles_ctl select 1 from dual where 'ANNEE' != 'ANNEE';
drop table zz_temp_cycles;
create table zz_temp_cycles (annee number(4));

/**********************************************************************************************/
/* couple_com_liee script init *************************************************************************/
/**********************************************************************************************/
drop table zz_temp_couple_com_liee_ctl;
create table zz_temp_couple_com_liee_ctl (numero number);
create unique index zz_temp_couple_com_liee_ctlk on zz_temp_couple_com_liee_ctl(numero);
insert into zz_temp_couple_com_liee_ctl	values (1);
insert into zz_temp_couple_com_liee_ctl select 1 from dual where 'COMMUNE' != 'COMMUNE';
insert into zz_temp_couple_com_liee_ctl select 1 from dual where 'COMMUNE_LIEE' != 'COMMUNE_LIEE';


drop table zz_temp_couple_com_liee;
create table zz_temp_couple_com_liee as select * from couple_com_liee where 1 = 2;