create index zz_temp_deces_an on zz_temp_deces (annee);
delete from cb_deces where annee in (select distinct a.annee from zz_temp_deces a);
insert into cb_deces select * from zz_temp_deces where zz_temp_deces.commune in (select com.id_commune from commune com);
drop table zz_temp_deces;
drop table zz_temp_deces_ctl;

