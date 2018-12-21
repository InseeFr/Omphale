create index zz_temp_flux_an on zz_temp_flux (annee);
drop index cb_flux_an;
create index cb_flux_an on cb_flux (annee);
delete from cb_flux where annee in (select distinct a.annee from zz_temp_flux a);
insert into cb_flux select * from zz_temp_flux where zz_temp_flux.origine in (select com.id_commune from commune com)
 															and
 													  zz_temp_flux.destination in (select com.id_commune from commune com);
drop table zz_temp_flux;
drop table zz_temp_flux_ctl;
drop index cb_flux_an;
commit;