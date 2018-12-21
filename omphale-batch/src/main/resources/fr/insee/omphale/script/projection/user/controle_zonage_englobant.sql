// ce script
//		controler que le zonage de la projection de l utilisateur est bien englobe par le zonage de la projection englobante



//define prefix=zp
//define idep=
//define zonage_id=4159
//define zonage_englobant=4161



//table rapport
drop table &&prefix._&&idep._zonage_rapport;
create table &&prefix._&&idep._zonage_rapport
as select 0 detail_compl, 0 detail_multiple, 0 englobe_compl from dual
;

//communes du zonage detail
//recuperation des communes des zones non standards du zonage ou standards avec commune de zone 4, 5
drop table &&prefix._&&idep._zonage_detail;
create table &&prefix._&&idep._zonage_detail as select b.nom zone, a.commune
from commune_de_zone a, zone b where  a.zone=b.id_zone
and ( b.id_zone) in (select  zone from zone_de_zonage where  zonage=&&zonage_id.)
;

//ajout des communes des zones standards sans commune de zone 1,2,3
insert into &&prefix._&&idep._zonage_detail 
select distinct a.nom, b.id_commune from zone a, departement_impact c, commune b
where c.dept = b.id_dept and  c.zone = a.id_zone and a.standard=1 and a.type_zone_standard in (1,2,3)
and ( c.zone) in (select  zone from zone_de_zonage where  zonage=&&zonage_id.)
;

create index &&prefix._&&idep._zonage_detail_com on &&prefix._&&idep._zonage_detail (commune);

//communes du zonage englobant
//recuperation des communes des zones non standards du zonage ou standards avec commune de zone 4, 5
drop table &&prefix._&&idep._zonage_englobe;
create table &&prefix._&&idep._zonage_englobe as select b.id_zone zone, a.commune
from commune_de_zone a, zone b where  a.zone=b.id_zone
and ( b.id_zone) in (select  zone from zone_de_zonage where  zonage=&&zonage_englobant.)
;

//ajout des communes des zones standards sans commune de zone 1,2,3
insert into &&prefix._&&idep._zonage_englobe
select distinct a.id_zone, b.id_commune from zone a, departement_impact c, commune b
where c.dept = b.id_dept and  c.zone = a.id_zone and a.standard=1 and a.type_zone_standard in (1,2,3)
and ( c.zone) in (select  zone from zone_de_zonage where  zonage=&&zonage_englobant.)
;

create index &&prefix._&&idep._zonage_englobe_com on &&prefix._&&idep._zonage_englobe (commune);

//couple detail, englobe
drop table &&prefix._&&idep._zonage_det_eng;
create table &&prefix._&&idep._zonage_det_eng as
select distinct a.zone detail, nvl(b.zone, '__COMPL') englobe
from &&prefix._&&idep._zonage_detail a, &&prefix._&&idep._zonage_englobe b
where a.commune=b.commune(+);

//couple  englobe, detail
drop table &&prefix._&&idep._zonage_eng_det;
create table &&prefix._&&idep._zonage_eng_det as
select distinct a.zone englobe, nvl(b.zone, '__COMPL') detail
from &&prefix._&&idep._zonage_detail b, &&prefix._&&idep._zonage_englobe a
where a.commune=b.commune(+) ;

update &&prefix._&&idep._zonage_rapport
set detail_compl=(select count(*) from &&prefix._&&idep._zonage_det_eng where englobe='__COMPL');

update &&prefix._&&idep._zonage_rapport
set detail_multiple=(select max(nb) from (
select detail, count(*) nb from &&prefix._&&idep._zonage_det_eng group by detail
));

update &&prefix._&&idep._zonage_rapport
set englobe_compl=(select nvl(max(nb),0) from (
select englobe, count(*) nb from &&prefix._&&idep._zonage_eng_det 
where englobe in (select englobe from &&prefix._&&idep._zonage_eng_det where detail='__COMPL')
group by englobe
));

commit;


drop table &&prefix._&&idep._zonage_ctl;
create table &&prefix._&&idep._zonage_ctl (numero number);
create unique index &&prefix._&&idep._zonage_ctlk on &&prefix._&&idep._zonage_ctl(numero);
insert into &&prefix._&&idep._zonage_ctl	values (1);
insert into &&prefix._&&idep._zonage_ctl select 1 from &&prefix._&&idep._zonage_rapport where detail_compl != 0; 
insert into &&prefix._&&idep._zonage_ctl select 1 from &&prefix._&&idep._zonage_rapport where detail_multiple != 1;
insert into &&prefix._&&idep._zonage_ctl select 1 from &&prefix._&&idep._zonage_rapport where englobe_compl > 1;

commit;
