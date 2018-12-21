//define idep=
//define prefix=zp
//define annee_ref=2006
//define annee_hor=2031
//define age_last=99
//define idhyp=7
//define methode=CALET
//define tq=FR
//define annee_deb=2006
//define annee_fin=2041
//define sexe_deb=1
//define sexe_fin=2
//define age_deb=0
//define age_fin=99


drop table &&prefix._&&idep._pop_FRANCE;
create table &&prefix._&&idep._pop_FRANCE as select
a.age, a.sexe, a.annee, a.valeur population
from cb_hypothese a
where a.id_hypothese=&&idhyp.
and   a.annee >=&&annee_deb. and   a.annee <=&&annee_fin.  
and   a.sexe >=&&sexe_deb. and   a.sexe <=&&sexe_fin.
and   a.age >=&&age_deb. and   a.age <=&&age_fin.  
;


update &&prefix._&&idep._param_calage_etalon 
set calage_trouve=1, calage_france=1 where calage_demande=1
and exists (select 1 from &&prefix._&&idep._pop_FRANCE);

commit;

drop table &&prefix._&&idep._zone_hors;

create table &&prefix._&&idep._zone_hors
as select distinct 'DET_' || id_dept zone
from departement where 1=2
;