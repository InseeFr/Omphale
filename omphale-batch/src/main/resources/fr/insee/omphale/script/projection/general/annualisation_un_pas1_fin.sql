drop table &&prefix._&&idep._pop_ecart;
create table &&prefix._&&idep._pop_ecart as select
a.age, a.sexe, a.annee, a.zone, a.population-nvl(b.population,0) ecart
from &&prefix._&&idep._pop_aux b, &&prefix._&&idep._proj_pyr a
where 
a.annee=b.annee(+) and a.sexe=b.sexe(+) and a.zone=b.zone(+) and a.age=b.age(+)
and a.annee=(select annee_pas from &&prefix._&&idep._pas_annuel)
;

drop table &&prefix._&&idep._pop_aux2;
create table &&prefix._&&idep._pop_aux2 as select * from &&prefix._&&idep._pop_aux
;

create index &&prefix._&&idep._pop_aux2_cle on &&prefix._&&idep._pop_aux2(age, sexe, zone, annee);
create index &&prefix._&&idep._pop_ecart_cle on &&prefix._&&idep._pop_ecart(age, sexe, zone);

update &&prefix._&&idep._pop_aux2 a
set a.population=(select a.population + (a.annee-c.annee_pas)*b.ecart/5
from &&prefix._&&idep._pop_ecart b, &&prefix._&&idep._pas_a_traiter c
where a.age=b.age and a.sexe=b.sexe and a.zone=b.zone)
where (a.age, a.sexe, a.zone) in
(select d.age, d.sexe, d.zone from &&prefix._&&idep._pop_ecart d)
;

insert into &&prefix._&&idep._proj_pyr
select b.* from &&prefix._&&idep._pop_aux2 b, &&prefix._&&idep._pas_a_traiter c
where b.annee > c.annee_pas and b.annee < (c.annee_pas+5)
;

commit;
