//Dans ce fichier sql, il faut remplacer idep_administrateur par l idep de l utilisateur
//et mdp_administrateur par le mot de passe de l administrateur

insert into type_pop values(1,'Legale');
insert into type_pop values(2,'Actifs');
insert into type_pop values(3,'Menages');
insert into type_pop values(4,'Generations');
insert into type_pop values(5,'Immigres');

insert into type_geo values(1,'France');
insert into type_geo values(2,'Regions');
insert into type_geo values(3,'Departements');
insert into type_geo values(4,'Zones utilisateurs');

insert into type_zone_standard values(0,'Non standard');
insert into type_zone_standard values(1,'Aire urbaine');
insert into type_zone_standard values(2,'Regions');
insert into type_zone_standard values(3,'Departements');
insert into type_zone_standard values(4,'Zones emploi');


insert into user_omphale values('idep_administrateur',1,'mdp_administrateur','Administrateur');
insert into user_omphale values('idep_LAMBDA',2,'mdp_lambda','Utilisateur');
insert into zonage values('1',3,'idep_administrateur','DEPARTEMENTS',2009,9,'zonage pour les projections etalons');
insert into zonage values('1',4,'idep_LAMBDA','TEST1',null,0,'zonage pour les tests');
insert into zone select
id_dept, 3, 'idep_administrateur', id_dept, 0,0, '_ETALON_' || id_dept from departement;
insert into zone_de_zonage
select type_geo, 1, id_zone from zone;

insert into departement_impact select
3, id_dept, id_dept from departement;

insert into zone values (1,4,'idep_LAMBDA','MM',0,0,'MM');
insert into zone values (2,4,'idep_LAMBDA','GN_BV',0,0,'GN_BV');
insert into zone_de_zonage values(4,1,1);
insert into zone_de_zonage values(4,1,2);

insert into zone select
seq_zone_geo4.nextval, 4, 'idep_administrateur', 'STANDARD_DEP_' || id_dept, 1,3, libelle from departement;

insert into departement_impact select
4, id_zone, substr(nom,14) id_dept from zone where standard=1 and type_zone_standard=3;


insert into zone select
seq_zone_geo4.nextval, 4, 'idep_administrateur', 'STANDARD_FRANCE', 1,1, 'France entiere' from dual;

insert into departement_impact select
4, id_zone, id_dept from zone a, departement b where standard=1 and type_zone_standard=1;

insert into zone select
seq_zone_geo4.nextval, 4, 'idep_administrateur', 'STANDARD_REG_' || id_region, 1,2, libelle from region;

insert into departement_impact select
4, id_zone, id_dept from zone a, departement b where standard=1 and type_zone_standard=2 and id_region=substr(a.nom,14);


update commune a set a.id_region = (select b.id_region from departement b where a.id_dept=b.id_dept);

INSERT INTO ANNEE SELECT 2005 + ROWNUM FROM DICT WHERE ROWNUM < 100;