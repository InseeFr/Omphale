//define col1=COMMUNE
//define col2=COMMUNE_LIEE

drop table zz_temp_couple_com_liee_ctl;
create table zz_temp_couple_com_liee_ctl (numero number);
create unique index zz_temp_couple_com_liee_ctlk on zz_temp_couple_com_liee_ctl(numero);
insert into zz_temp_couple_com_liee_ctl	values (1);
insert into zz_temp_couple_com_liee_ctl select 1 from dual where 'COMMUNE' != 'COMMUNE';
insert into zz_temp_couple_com_liee_ctl select 1 from dual where 'COMMUNE_LIEE' != 'COMMUNE_LIEE';


drop table zz_temp_couple_com_liee;
create table zz_temp_couple_com_liee as select * from couple_com_liee where 1 = 2;
