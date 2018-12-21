drop table &&prefix._&&idep._pop_aux_nai;
create table &&prefix._&&idep._pop_aux_nai
as select a.annee, a.zone, sum(a.population*b.qf*(1+d.moy/2)) naissances
from &&prefix._&&idep._pop_aux a, &&prefix._&&idep._quotient_naip b, &&prefix._&&idep._pas_annuel c,
&&prefix._&&idep._qm_moy d
where 
a.age=b.age and a.zone=b.zone and a.annee=b.annee 
and a.age=d.age and a.sexe=d.sexe and a.zone=d.zone and d.annee=(select annee_pas+5 from &&prefix._&&idep._pas_a_traiter)

and a.annee=c.annee_pas and a.sexe=2
group by a.annee, a.zone
;
insert into &&prefix._&&idep._pop_aux
select -1, 1, annee, zone, naissances*&&part_gars.
from &&prefix._&&idep._pop_aux_nai
; 
insert into &&prefix._&&idep._pop_aux
select -1, 2, annee, zone, naissances*(1-&&part_gars.)
from &&prefix._&&idep._pop_aux_nai
; 

insert into &&prefix._&&idep._pop_aux
select a.age+1, a.sexe, a.annee+1, a.zone, a.population*(1+d.moy)*(1-b.qd)
from &&prefix._&&idep._pop_aux a, &&prefix._&&idep._quotient_decp b, &&prefix._&&idep._pas_annuel c,
&&prefix._&&idep._qm_moy d
where 
a.age=b.age(+) and a.zone=b.zone(+) and a.annee=b.annee(+) and a.sexe=b.sexe(+) 
and a.age=d.age(+) and a.sexe=d.sexe(+) and a.zone=d.zone(+) and d.annee=(select annee_pas+5 from &&prefix._&&idep._pas_a_traiter)
and a.annee=c.annee_pas
;
//regroupement des vieux
drop table &&prefix._&&idep._pop_aux_vieux;
create table &&prefix._&&idep._pop_aux_vieux as select
99 age, sexe, annee, zone, sum(population) population
from &&prefix._&&idep._pop_aux
where age >=&&age_last.
and annee=(select annee_pas+1 from &&prefix._&&idep._pas_annuel)
group by annee, sexe, zone
;
delete from &&prefix._&&idep._pop_aux
where age >=&&age_last.
and annee=(select annee_pas+1 from &&prefix._&&idep._pas_annuel)
;
insert into &&prefix._&&idep._pop_aux
select * from &&prefix._&&idep._pop_aux_vieux
;

commit;
update &&prefix._&&idep._pas_annuel set annee_pas=annee_pas+1
;
commit;
