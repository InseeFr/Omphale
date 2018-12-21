//define col1=ID_COMMUNE
//define col2=ID_DEPT
//define col3=ID_REGION
//define col4=LIBELLE

drop table zz_temp_commune_ctl;
create table zz_temp_commune_ctl (numero number);
create unique index zz_temp_commune_ctlk on zz_temp_commune_ctl(numero);
insert into zz_temp_commune_ctl	values (1);
insert into zz_temp_commune_ctl select 1 from dual where 'ID_COMMUNE' != 'ID_COMMUNE';
insert into zz_temp_commune_ctl select 1 from dual where 'ID_DEPT' != 'ID_DEPT';
insert into zz_temp_commune_ctl select 1 from dual where 'ID_REGION' != 'ID_REGION';
insert into zz_temp_commune_ctl select 1 from dual where 'LIBELLE' != 'LIBELLE';

drop table zz_temp_commune;
create table zz_temp_commune as select * from commune where 1 = 2;
