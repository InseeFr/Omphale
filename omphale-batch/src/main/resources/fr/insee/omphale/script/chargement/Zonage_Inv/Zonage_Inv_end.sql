

//invalidation des zonages
update zonage a set a.etat_validation=0 where a.id_zonage in (select b.zonage from zone_de_zonage b 
 where b.zone in (select c.id_zone from zone c where c.type_zone_standard not in (1,2,3)) )
;
commit;
