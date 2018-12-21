create index zz_temp_pop_legale_an on zz_temp_pop_legale (annee, type_pop);
drop index cb_pop_legale_an;
create index cb_pop_legale_an on cb_pop_legale (annee, type_pop);
delete from cb_pop_legale where (annee, type_pop) in (select distinct a.annee, a.type_pop  from zz_temp_pop_legale a);
insert into cb_pop_legale select * from zz_temp_pop_legale where zz_temp_pop_legale.commune in (select com.id_commune from commune com);
drop table zz_temp_pop_legale;
drop table zz_temp_pop_legale_ctl;
drop index cb_pop_legale_an;