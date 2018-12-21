//define col1=ANNEE
drop table zz_temp_cycles_ctl;
create table zz_temp_cycles_ctl (numero number);
create unique index zz_temp_cycles_ctlk on zz_temp_cycles_ctl(numero);
insert into zz_temp_cycles_ctl	values (1);
insert into zz_temp_cycles_ctl select 1 from dual where 'ANNEE' != 'ANNEE';
drop table zz_temp_cycles;
create table zz_temp_cycles (annee number(4));
