

delete from zz_temp_commune where id_commune in (select distinct a.id_commune from commune a);
insert into commune select * from zz_temp_commune;
drop table zz_temp_commune;
drop table zz_temp_commune_ctl;
commit;
