//define col1=ANNEE
//define col2=NOM

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