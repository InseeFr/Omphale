//parametres : 

//define idep=
//define prefix=zp
//define annee_ref=2006
//define annee_hor=2030


//Traitements
//calcul des pas quinquenaux de projection

drop table &&prefix._&&idep._nb_pas;
create  table &&prefix._&&idep._nb_pas as
select &&annee_ref. annee_ref, &&annee_hor. annee_horizon, &&annee_ref. + &&decal_annee.*ceil((&&annee_hor. - &&annee_ref. )/&&decal_annee.) annee_hor,
ceil((&&annee_hor. - &&annee_ref. )/&&decal_annee.) nb_pas
from dual
;
drop table &&prefix._&&idep._pas;
create  table &&prefix._&&idep._pas as
select a.*, rownum pas_cur, annee_ref+&&decal_annee.*rownum-&&decal_annee. annee_pas, 0 a_faire from &&prefix._&&idep._nb_pas a, annees b
where rownum <= a.nb_pas
;
