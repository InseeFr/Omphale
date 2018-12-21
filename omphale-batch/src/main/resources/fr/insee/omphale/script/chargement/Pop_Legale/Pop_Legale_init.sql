//define col1=ANNEE
//define col2=NOM

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