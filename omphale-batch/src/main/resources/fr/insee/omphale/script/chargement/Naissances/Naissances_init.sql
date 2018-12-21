//define col1=ANNEE
//define col2=NOM

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