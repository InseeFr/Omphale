create index zz_temp_naissances_an on zz_temp_naissances (annee);
delete from cb_naissances where annee in (select distinct a.annee from zz_temp_naissances a);
insert into cb_naissances select * from zz_temp_naissances where zz_temp_naissances.commune in (select com.id_commune from commune com);
drop table zz_temp_naissances;
drop table zz_temp_naissances_ctl;

