//define col1=ANNEE
//define col2=NOM

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