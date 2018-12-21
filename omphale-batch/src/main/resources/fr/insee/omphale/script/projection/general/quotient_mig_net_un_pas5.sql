//quotients de migration nette
//pour calcul population des annees intermediaires


insert into &&prefix._&&idep._csv_anomalies select c.annee_pas, b.nombre , 'cases negatives mises a zero' from
(
 select count(*) nombre from &&prefix._&&idep._proj_pyr where population < 0 
) b, &&prefix._&&idep._pas_a_traiter c
where b.nombre >0
;

update &&prefix._&&idep._proj_pyr set population=0 where population <0
;

commit;


create table &&prefix._&&idep._qm_brut as select 
a.age-2 age, a.sexe, a.annee, a.zone, 
decode((nvl(a.population,0) - nvl(b.flux,0)), 0, 0,
decode(nvl(b.flux,0),0,0,(power(( b.flux/(a.population-b.flux)+1),0.2)-1))
) qmb
from &&prefix._&&idep._proj_pyr a, &&prefix._&&idep._solde_mig b, &&prefix._&&idep._pas_a_traiter c
where a.annee=(c.annee_pas+5)
and a.annee=b.annee(+) and a.age=b.age(+) and a.sexe=b.sexe(+) and a.zone=b.zone(+)
and a.age >5 
;


insert into &&prefix._&&idep._qm_brut
select -1 age, 1 sexe, c.annee_pas+5 annee, a.zone, b.ratio - 1 
from &&prefix._&&idep._pas_a_traiter c, &&prefix._&&idep._ratio_nai b,
(select distinct zone from &&prefix._&&idep._proj_pyr) a
where a.zone=b.zone and b.age=0
;
insert into &&prefix._&&idep._qm_brut
select -1 age, 2 sexe, c.annee_pas+5 annee, a.zone, b.ratio - 1 
from &&prefix._&&idep._pas_a_traiter c, &&prefix._&&idep._ratio_nai b,
(select distinct zone from &&prefix._&&idep._proj_pyr) a
where a.zone=b.zone and b.age=0
;

insert into &&prefix._&&idep._qm_brut
select d.age, 1 sexe, c.annee_pas+5 annee, a.zone, b.ratio/d.ratio - 1 
from &&prefix._&&idep._pas_a_traiter c, &&prefix._&&idep._ratio_nai b,&&prefix._&&idep._ratio_nai d,
(select distinct zone from &&prefix._&&idep._proj_pyr) a
where a.zone=b.zone and b.age=(d.age+1) and nvl(d.ratio,0) != 0 and a.zone=d.zone
and d.age >=0 and d.age <=3
;

insert into &&prefix._&&idep._qm_brut
select d.age, 2 sexe, c.annee_pas+5 annee, a.zone, b.ratio/d.ratio - 1 
from &&prefix._&&idep._pas_a_traiter c, &&prefix._&&idep._ratio_nai b,&&prefix._&&idep._ratio_nai d,
(select distinct zone from &&prefix._&&idep._proj_pyr) a
where a.zone=b.zone and b.age=(d.age+1) and nvl(d.ratio,0) != 0 and a.zone=d.zone
and d.age >=0 and d.age <=3
;

insert into &&prefix._&&idep._qm_brut
select 98, 1 sexe, c.annee_pas+5 annee, a.zone, 0 
from &&prefix._&&idep._pas_a_traiter c,
(select distinct zone from &&prefix._&&idep._proj_pyr) a
;
insert into &&prefix._&&idep._qm_brut
select 99, 1 sexe, c.annee_pas+5 annee, a.zone, 0 
from &&prefix._&&idep._pas_a_traiter c,
(select distinct zone from &&prefix._&&idep._proj_pyr) a
;

insert into &&prefix._&&idep._qm_brut
select 98, 2 sexe, c.annee_pas+5 annee, a.zone, 0 
from &&prefix._&&idep._pas_a_traiter c,
(select distinct zone from &&prefix._&&idep._proj_pyr) a
;
insert into &&prefix._&&idep._qm_brut
select 99, 2 sexe, c.annee_pas+5 annee, a.zone, 0 
from &&prefix._&&idep._pas_a_traiter c,
(select distinct zone from &&prefix._&&idep._proj_pyr) a
;



drop table &&prefix._&&idep._qm_med;
create table &&prefix._&&idep._qm_med as select 
age, sexe, annee, zone, qmb prev, qmb med, qmb next
from &&prefix._&&idep._qm_brut
;

create index &&prefix._&&idep._qm_med_cle on &&prefix._&&idep._qm_med(age, sexe, annee, zone);
create index &&prefix._&&idep._qm_brut_cle on &&prefix._&&idep._qm_brut(age, sexe, annee, zone);


update &&prefix._&&idep._qm_med a set 
a.prev = (select b.qmb from &&prefix._&&idep._qm_brut b where 
a.sexe=b.sexe and a.annee=b.annee and a.zone=b.zone
and b.age=a.age-1)
where a.age >=0 and a.age <=98
;

update &&prefix._&&idep._qm_med a set 
a.next = (select b.qmb from &&prefix._&&idep._qm_brut b where 
a.sexe=b.sexe and a.annee=b.annee and a.zone=b.zone
and b.age=a.age+1)
where a.age >=0 and a.age <=98
;

update &&prefix._&&idep._qm_med  set 
prev = med where prev is null
;
update &&prefix._&&idep._qm_med  set 
next = med where next is null
;
//tri a,b : permute si a>b
update &&prefix._&&idep._qm_med  set 
prev=med, med=prev where prev > med
;
//tri b,c : permute si b>c
update &&prefix._&&idep._qm_med  set 
next=med, med=next where med > next
;
//tri a,b : permute si a>b
update &&prefix._&&idep._qm_med  set 
prev=med, med=prev where prev > med
;

drop table &&prefix._&&idep._qm_moy;
create table &&prefix._&&idep._qm_moy as select 
age, sexe, annee, zone, med prev, med moy, med next
from &&prefix._&&idep._qm_med
;

create index &&prefix._&&idep._qm_moy_cle on &&prefix._&&idep._qm_moy(age, sexe, annee, zone);

update &&prefix._&&idep._qm_moy a set 
a.prev = (select b.med from &&prefix._&&idep._qm_med b where 
a.sexe=b.sexe and a.annee=b.annee and a.zone=b.zone
and b.age=a.age-1)
where a.age >=0 and a.age <=98
;

update &&prefix._&&idep._qm_moy a set 
a.next = (select b.med from &&prefix._&&idep._qm_med b where 
a.sexe=b.sexe and a.annee=b.annee and a.zone=b.zone
and b.age=a.age+1)
where a.age >=0 and a.age <=98
;

update &&prefix._&&idep._qm_moy  set 
prev = 0 where prev is null
;
update &&prefix._&&idep._qm_moy  set 
next = 0 where next is null
;

update &&prefix._&&idep._qm_moy  set 
moy = power(((1+prev)*(1+moy)*(1+moy)*(1+next)), 0.25) -1
;

delete from &&prefix._&&idep._qm_moy where age >=0 and age <=3
;

insert into &&prefix._&&idep._qm_moy  select 
age, sexe, annee, zone, qmb prev, qmb moy, qmb next
from &&prefix._&&idep._qm_brut where  age >=0 and age <=3
;

commit;



