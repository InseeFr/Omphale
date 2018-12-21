drop table &&prefix._&&idep._pas_annuel;
create table &&prefix._&&idep._pas_annuel as select annee_pas from &&prefix._&&idep._pas_a_traiter
;
drop table &&prefix._&&idep._pop_aux;
create table &&prefix._&&idep._pop_aux as 
select age, sexe, annee, zone, population
from &&prefix._&&idep._proj_pyr
where annee=(select annee_pas from &&prefix._&&idep._pas_annuel)
;
create index &&prefix._&&idep._pop_aux_cle on &&prefix._&&idep._pop_aux(age, sexe, annee, zone);

