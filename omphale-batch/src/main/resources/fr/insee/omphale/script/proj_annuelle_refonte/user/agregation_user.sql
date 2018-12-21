// AGREGATION
// *************************************

//agregation_user.sql
// ces commentaires permettent de présenter la façon 
// 	dont chaque table utilisée dans ce script est créée, modifiée ou supprimée
// attention celle liste n est plus tout a fait a jour suite refonte
//_zonage_ctl <c-
//_zonage_ctl <i-
//_zonage_ctl <i- Zonage
//cb_population <d-
//user_population <d-
//_les_zones <c- zone_de_zonage, departement_impact, zone
//_les_zones <i- dept_de_groupet
//_les_zones <i- departement, region
//_les_zones <i- _les_zones
//_zonage <c- commune_de_zone, zone, zone_de_zonage
//_zonage <i- zone, departement_impact, commune, zone_de_zonage
//_agrege_pop <c- cb_pop_legale_init, _zonage
//_agrege_dec <c- cb_avg_dec, _zonage
//_agrege_nai <c- cb_avg_nai, _zonage
//_agrege_nai_pop <c- cb_avg_nai_pop, _zonage
//_zonage <i- dept_de_groupet
//_agrege_pep <c- cb_pop_legale_init, _zonage
//_agregation_flux <c- cb_flux_init, _zonage
//_agregation_flux_pop <c- cb_flux_init, _zonage
//_agrege_pop <d-
//_agrege_dec <d-
//_agrege_nai <d-
//_agrege_nai_pop <d-
//_agrege_flux <d-
//_agrege_flux_pop <d-
//_vue_pop_ref <c- _agrege_pop
//_vue_pop_ref_f <c- _agrege_pop
//_vue_pop_ref_r <c- _vue_pop_ref
//_vue_actifs <c- _agrege_pop
//_vue_actifs_f <c- _agrege_pop
//_vue_menages <c- _agrege_pop
//_vue_menages_f <c- _agrege_pop
//_vue_GENERATION <c- _agrege_pop
//_vue_GENERATION_f <c- _agrege_pop
//_vue_imm_etr <c- _agrege_pop
//_vue_imm_etr_f <c- _agrege_pop
//_vue_imm_etr_fm <c- _agrege_pep
//_vue_DEC <c- _agrege_dec
//_vue_DEC_F <c- _agrege_dec
//_vue_NAI <c- _agrege_nai
//_vue_NAI_F <c- _agrege_nai
//_vue_POP_NEE <c- _agrege_nai_pop
//_vue_POP_NEE_F <c- _agrege_nai_pop
//_vue_POP_NEE_R <c- _VUE_POP_NEE
//_param_calage_etalon




//parametres : 


//define zonage_id=65022
//define idep=
//define prefix=ZP
//define annee_ref=2012
//define def_projection_id=23145
//define calage=0
//define decal_annee=1






--/*DEBUT DES CONTROLES */
--/********************************************************************************************************************************************/
--	/* creation des contrôles et des index pour les tables temporaires zonages */


		drop table &&prefix._&&idep._zonage_ctl;
		create table &&prefix._&&idep._zonage_ctl (numero number);
		create unique index &&prefix._&&idep._zonage_ctlk on &&prefix._&&idep._zonage_ctl(numero);
		insert into &&prefix._&&idep._zonage_ctl values (1);
	
	--/* on contrôle que la table zonage ne contient pas de zonage utilisateur avec ETAT_VALIDATION différent de 9 */
		insert into &&prefix._&&idep._zonage_ctl select 1 from Zonage where ETAT_VALIDATION != 9 and id_zonage = &&zonage_id.;
		drop table &&prefix._&&idep._zonage_ctl;
--/********************************************************************************************************************************************/
--/*FIN  DES CONTROLES */

	
--/*DEBUT NETTOYAGE TABLE */
--/********************************************************************************************************************************************/
	--/* on nettoie les tables cb_population et user_population de toutes références de la projection en cours de l'utilisateur */
		delete from cb_population where projection=&&def_projection_id.;
		delete from user_population where projection=&&def_projection_id.;
--/********************************************************************************************************************************************/
--/*FIN NETTOYAGE TABLE */

		
		
--/*DEBUT CREATION TABLE TEMPORAIRE _LES_ZONES */
--/********************************************************************************************************************************************/

	--/*ETAPE 1*/
	--/* on créé une table temporaire les_zones dans laquelle on insère les zones et leurs départements impactés du zonage demandé par l'utilisateur* */
		drop table &&prefix._&&idep._les_zones;
		create table &&prefix._&&idep._les_zones
		as select distinct a.zone, 'ZONE' type, b.dept, c.nom, 'F' fm_dom_tom
		from zone_de_zonage a, departement_impact b, zone c
		where 
		a.zonage=&&zonage_id.
		and c.id_zone=a.zone
		and b.zone=a.zone
		;
	
	--/*ETAPE 2*/
	--/* on insère dans la table temporaire les_zones les références aux départements du groupe étalon correspondant au zonage demandé par l'utilisateur* */
		insert into &&prefix._&&idep._les_zones
		select null, 'DEPT', id_dept dept, '_DEP_' || id_dept, 'F' from departement
		where id_dept not in
		(select dept from dept_de_groupet where zonage=&&zonage_id.);
		commit;
	
	--/*ETAPE 3*/
	--/* on met à jour la colonne fm_dom_tom avec la valeur 'D' des départements dans la table temporaire les_zones  quand la région est une région des dom */
		update &&prefix._&&idep._les_zones set fm_dom_tom='D' 
		where dept in (select id_dept from departement 
		where id_region in 
		(select id_region from region where fm_dom_tom != 'F')
		)
		;
		commit;
	
	--/*ETAPE 4*/
	--/* on met à jour la colonne fm_dom_tom avec la valeur 'D' des zones quand la zone n'est pas nulle et la colonne fm_dom_tom est à 'D' */
		update &&prefix._&&idep._les_zones
		set fm_dom_tom='D' where zone is not null and zone in
		(select b.zone from &&prefix._&&idep._les_zones b where b.fm_dom_tom='D')
		;
		
		commit;
--/********************************************************************************************************************************************/
--/*FIN  CREATION TABLE TEMPORAIRE _LES_ZONES */

	
--/*DEBUT  CREATION TABLE TEMPORAIRE _ZONAGE */
--/********************************************************************************************************************************************/
	--/*ETAPE 1*/
    //communes du zonage
    //recuperation des communes des zones non standards du zonage ou standard avec commune de zone 4, 5
		drop table &&prefix._&&idep._zonage;
		create table &&prefix._&&idep._zonage as select b.nom zone, a.commune
		from commune_de_zone a, zone b where  a.zone=b.id_zone
		and ( b.id_zone) in (select  zone from zone_de_zonage where  zonage=&&zonage_id.)
		and exists(
		select 1 from zonage where etat_validation =9 and  id_zonage=&&zonage_id.
		)
		;
	
	--/*ETAPE 2*/
    //ajout des communes des zones standard sans commune de zone 1,2,3
		insert into &&prefix._&&idep._zonage 
		select distinct a.nom, b.id_commune from zone a, departement_impact c, commune b
		where c.dept = b.id_dept and  c.zone = a.id_zone and a.standard=1 and a.type_zone_standard in (1,2,3,6)
		and ( c.zone) in (select  zone from zone_de_zonage where  zonage=&&zonage_id.)
		and exists(
		select 1 from zonage where etat_validation =9 and   id_zonage=&&zonage_id.
		)
		;
    

      

--/********************************************************************************************************************************************/
--/*FIN  CREATION TABLE TEMPORAIRE _ZONAGE */

	
--/*DEBUT  DES AGREGATIONS **************************************************************************************************************/
--/********************************************************************************************************************************************/

        --/*Agregation de CB_POP_LEGALE_INIT pour le zonage de l'utilisateur  */
            --/****  Agrégation -  Population de référence */
             --/* population legale &&annee_ref. pour le zonage*/
            --/* agregation de la pop legale des communes du zonage + une zone complémentaire à la france entière plus une zone france entiere */ 
        
            drop table &&prefix._&&idep._agrege_pop;
            create table &&prefix._&&idep._agrege_pop as
            select age, sexe, type_pop, decode(grouping_id(zone), 0,zone,'_TOTAL') zone, sum(population) population
            from (
            select a.*, nvl(b.zone,'_COMPLEMENT') zone
            from cb_pop_legale_init a, &&prefix._&&idep._zonage b
            where a.commune=b.commune(+) and a.annee=&&annee_ref.
            )
            group by age, sexe, type_pop, cube(zone);


        --/*Agregation de CB_AVG_DEC pour zonage utilisateur*/
            --/**/
            --/***  Agrégation - Décès agregation des deces des communes du zonage + une zone complémentaire à la france entière plus une zone france entiere    */      
            
            drop table &&prefix._&&idep._agrege_dec;
            create table &&prefix._&&idep._agrege_dec as
                select 
                    age, 
                    sexe, 
                    decode(grouping_id(zone), 0,zone,'_TOTAL') zone, 
                    sum(deces) deces
                    from (
                            select 
                                a.*, 
                                nvl(b.zone,'_COMPLEMENT') zone
                                from 
                                    cb_avg_dec a, 
                                    &&prefix._&&idep._zonage b
                                where 
                                    a.commune=b.commune(+) 
                                    and a.annee=&&annee_ref.
            )
            group by age, sexe, cube(zone);



        --/*Agregation de CB_AVG_NAI pour zonage utilisateur*/
            --/****  Agrégation - Naissances pour les données france entière*/
            --/* agregation des naissances des communes du zonage + une zone complémentaire à la france entière plus une zone france entiere*/ 
        
            drop table &&prefix._&&idep._agrege_nai;
            
            create table &&prefix._&&idep._agrege_nai as
            select age, decode(grouping_id(zone), 0,zone,'_TOTAL') zone, sum(naissances) naissances
            from (
            select a.*, nvl(b.zone,'_COMPLEMENT') zone
            from cb_avg_nai a, &&prefix._&&idep._zonage b
            where a.commune=b.commune(+) and a.annee=&&annee_ref.
            )
            group by age, cube(zone);

        --/*Agregation de CB_AVG_NAI_POP pour zonage utilisateur*/
            --/***  Agregation -  naissances */
             --/* agregation des populatons nees des communes du zonage + une zone complémentaire à la france entière plus une zone france entiere  */ 
            drop table &&prefix._&&idep._agrege_nai_pop;
            
            create table &&prefix._&&idep._agrege_nai_pop as
            select -1 age, sexe, decode(grouping_id(zone), 0,zone,'_TOTAL') zone, sum(population) population
            from (
            select a.*, nvl(b.zone,'_COMPLEMENT') zone
            from cb_avg_nai_pop a, &&prefix._&&idep._zonage b
            where a.commune=b.commune(+) and a.annee=&&annee_ref.
            )
            group by sexe, cube(zone);
            

             --/* flux &&annee_ref. pour le zonage*/
             --/* ajout des departement non impactes*/ 
            insert into &&prefix._&&idep._zonage
            select '_DEP_'||id_dept zone, id_commune commune from commune where id_dept not in
            (select dept from dept_de_groupet where zonage=&&zonage_id.);
            
            
        --/*Agregation CB_POP_LEGALE_INIT pour immigrants */
            --/**** agregation - Immigrants de l’étranger (bruts) */
            --/* immig_etr france metro &&annee_ref. pour le zonage */
            --/* agregation de la pop legale des communes du zonage + une zone complémentaire à la france entière plus une zone france entiere */
            drop table &&prefix._&&idep._agrege_pep;
            
            create table &&prefix._&&idep._agrege_pep as
            select age, sexe, type_pop, decode(grouping_id(zone), 0,zone,'_TOTAL') zone, sum(population) population
            from (
            select a.*, nvl(b.zone,'_COMPLEMENT') zone
            from cb_pop_legale_init a, &&prefix._&&idep._zonage b
            where a.commune=b.commune(+) and a.annee=&&annee_ref.  and a.type_pop=5
            )
            group by age, sexe, type_pop, cube(zone);

        --/*Agregation CB_FLUX_INIT pour l'année de référence*/
            --/**** agrégation - Flux        */ 
            
            drop table &&prefix._&&idep._agregation_flux;           
            create table &&prefix._&&idep._agregation_flux as
                select 
                        age, 
                        sexe, zr, za, sum(flux) flu
            from (
             select age, sexe, flux, nvl(b.zone,'_COMPLEMENT') zr, nvl(c.zone,'_COMPLEMENT') za
             from cb_flux_init a, &&prefix._&&idep._zonage b, &&prefix._&&idep._zonage c
             where a.destination=b.commune(+) and a.origine=c.commune(+) and a.annee=&&annee_ref.
            )
            where zr not like '_DEP_%' or za not like '_DEP_%'
            group by   age, sexe, rollup (zr, za)
            having grouping_id(zr)=0 and grouping_id(za)=0
            ;
            

        --/*Agregation de CB_FLUX_INIT pour l'année de référence pour l'emmigration*/
            drop table &&prefix._&&idep._agregation_flux_pop;
            
            create table &&prefix._&&idep._agregation_flux_pop as
            select age, sexe, za origine , sum(flux) pop
            from (
             select age, sexe, flux, nvl(b.zone,'_COMPLEMENT') zr, nvl(c.zone,'_COMPLEMENT') za
             from cb_flux_init a, &&prefix._&&idep._zonage b, &&prefix._&&idep._zonage c
             where a.destination=b.commune(+) and a.origine=c.commune(+) and a.annee=&&annee_ref.
            )
            group by   age, sexe, za
            ;
            

--/********************************************************************************************************************************************/
--/*FIN  DES AGGREGATIONS **************************************************************************************************************/


--/*DEBUT NETTOYAGE COMPLEMENT ET INDEXATION **********************/
--/********************************************************************************************************************************************/
		delete from &&prefix._&&idep._agrege_pop where zone='_COMPLEMENT';
		delete from &&prefix._&&idep._agrege_dec where zone='_COMPLEMENT';
		delete from &&prefix._&&idep._agrege_nai where zone='_COMPLEMENT';
		delete from &&prefix._&&idep._agrege_nai_pop where zone='_COMPLEMENT';
		delete from &&prefix._&&idep._agregation_flux where zr='_COMPLEMENT' or za='_COMPLEMENT';
		delete from &&prefix._&&idep._agregation_flux_pop where origine='_COMPLEMENT';
		
		create index &&prefix._&&idep._agrege_pop_cle on &&prefix._&&idep._agrege_pop(age,sexe,zone);

--/********************************************************************************************************************************************/
--/*FIN  NETTOYAGE COMPLEMENT ET INDEXATION **********************/






--/*DEBUT CREATION DES VUES  **********************/
--/********************************************************************************************************************************************/

    --/************************/
    --/* agregation  ** population de référence france métropolitaine*/
    --/************************/
        --/*Population de référence France entière pour l'année de référence*/
            DROP VIEW &&prefix._&&idep._VUE_POP_REF;
            create view &&prefix._&&idep._vue_pop_ref as select age, sexe, zone,  population
            from &&prefix._&&idep._agrege_pop where type_pop=1 and zone not in ('_TOTAL');
            
            
        --/*Population de référence France entière pour l'année de référence*/          
            DROP VIEW &&prefix._&&idep._VUE_POP_REF_F;  
            create view &&prefix._&&idep._vue_pop_ref_f as select age, sexe, zone,  population
            from &&prefix._&&idep._agrege_pop where type_pop=1 and zone in ('_TOTAL');
            
        --/*Population de référence par zone de l'utilisateur et par age*/          
            DROP VIEW &&prefix._&&idep._VUE_POP_REF_R;  
            create view &&prefix._&&idep._vue_pop_ref_r as select age, zone, sum(population) population
            from &&prefix._&&idep._vue_pop_ref where age < &&decal_annee. group by zone, age;           
            
            
    --/************************/
    --/* agregation - pyramide par génération*/
    --/************************/
            DROP VIEW &&prefix._&&idep._VUE_GENERATION;
            create view &&prefix._&&idep._vue_GENERATION as select age, sexe, zone,  population
            from &&prefix._&&idep._agrege_pop where type_pop=4 and zone not in ('_TOTAL');

            DROP VIEW &&prefix._&&idep._VUE_GENERATION_F;
            create view &&prefix._&&idep._vue_GENERATION_f as select age, sexe, zone,  population
            from &&prefix._&&idep._agrege_pop where type_pop=4 and zone in ('_TOTAL');
            
            
            
    --/************************/
    --/* agregation - Actifs/Menages/Eleves*/
    --/************************/    
            --/*Actifs sur le zonage utilisateur*/
            DROP VIEW &&prefix._&&idep._VUE_ACTIFS;
            create view &&prefix._&&idep._vue_actifs as 
                select age, sexe, zone,  population
                    from &&prefix._&&idep._agrege_pop 
                        where type_pop=2 and zone not in ('_TOTAL')
            ;
            
            --/*Actifs france entière*/
            DROP VIEW &&prefix._&&idep._VUE_ACTIFS_F;
            create view &&prefix._&&idep._vue_actifs_f as select age, sexe, zone,  population
                from &&prefix._&&idep._agrege_pop where type_pop=2 and zone in ('_TOTAL');
                
            --/*Menages sur le zonage utilisateur*/
            DROP VIEW &&prefix._&&idep._VUE_MENAGES;
            create view &&prefix._&&idep._vue_menages as select age, sexe, zone,  population
                from &&prefix._&&idep._agrege_pop where type_pop=3 and zone not in ('_TOTAL');
            
            --/*Menages france entière*/
            DROP VIEW &&prefix._&&idep._VUE_MENAGES_F;
            create view &&prefix._&&idep._vue_menages_f as select age, sexe, zone,  population
                from &&prefix._&&idep._agrege_pop where type_pop=3 and zone in ('_TOTAL');
                
    --/************************/
    --/* agregation - Naissances*/
    --/************************/    

            --/*Naissances sur le zonage de l'utilisateur france entière*/
            DROP VIEW &&prefix._&&idep._VUE_NAI;
            create VIEW &&prefix._&&idep._VUE_NAI as select age, zone,  naissances
            from &&prefix._&&idep._agrege_nai where zone not in ('_TOTAL');
            
             
            --/*Naissances france entière (FM + DOM + TOM)*/
            DROP VIEW &&prefix._&&idep._VUE_NAI_F;
            create VIEW &&prefix._&&idep._VUE_NAI_F as select age, zone,  naissances
            from &&prefix._&&idep._agrege_nai where zone in ('_TOTAL');     

            --/*Nées sur le zonage de l'utilisateur france entière*/
            DROP VIEW &&prefix._&&idep._VUE_POP_NEE;
            create VIEW &&prefix._&&idep._VUE_POP_NEE as select age, sexe, zone,  population
            from &&prefix._&&idep._agrege_nai_pop where zone not in ('_TOTAL');

            --/*Nées sur le zonage de l'utilisateur france entière*/
            DROP VIEW &&prefix._&&idep._VUE_POP_NEE_F;
            create VIEW &&prefix._&&idep._VUE_POP_NEE_F as select age, sexe, zone,  population
            from &&prefix._&&idep._agrege_nai_pop where zone in ('_TOTAL');
            
            --/*Nées pour chaque zone de l'utilisteur*/         
            DROP VIEW &&prefix._&&idep._VUE_POP_NEE_R;
            create VIEW &&prefix._&&idep._VUE_POP_NEE_R as select zone, sum(population) naissances
            from &&prefix._&&idep._VUE_POP_NEE group by zone;           
            
            
    --/************************/
    --/* agregation - Deces*/
    --/************************/    
            
            --/*Deces sur le zonage de l'utilisateur france entière*/
            DROP VIEW &&prefix._&&idep._VUE_DEC;
            create VIEW &&prefix._&&idep._VUE_DEC as select age, sexe, zone,  deces
            from &&prefix._&&idep._agrege_dec where zone not in ('_TOTAL');
    
            --/*Deces france entière (FM + DOM + TOM)*/         
            DROP VIEW &&prefix._&&idep._VUE_DEC_F;
            create VIEW &&prefix._&&idep._VUE_DEC_F as select age, sexe, zone,  deces
            from &&prefix._&&idep._agrege_dec where zone in ('_TOTAL');         
                    
            
    --/************************/
    --/* agregation - Immigrants de l'étranger (bruts)  */
    --/************************/    
            drop view &&prefix._&&idep._VUE_IMM_ETR;            
            create view &&prefix._&&idep._vue_imm_etr as select age, sexe, zone,  population
            from &&prefix._&&idep._agrege_pop where type_pop=5 and zone not in ('_TOTAL');
            
        
            
            drop view &&prefix._&&idep._VUE_IMM_ETR_F;
            create view &&prefix._&&idep._vue_imm_etr_f as select age, sexe, zone,  population
            from &&prefix._&&idep._agrege_pop where type_pop=5 and zone in ('_TOTAL');
            
            drop view &&prefix._&&idep._vue_imm_etr_fm; 
            create view &&prefix._&&idep._vue_imm_etr_fm as select age, sexe, sum(population) population
            from &&prefix._&&idep._agrege_pep where zone in (select nom from &&prefix._&&idep._les_zones where fm_dom_tom = 'F')
            group by age, sexe
            ;           
            
            
            
        

        --/*preparation des operations de calage etalon pour ne pas planter les projections utilisateurs avec scenario standard mixte*/
                drop table &&prefix._&&idep._param_calage_etalon;
                create table &&prefix._&&idep._param_calage_etalon as select &&calage. calage_demande, 0 calage_trouve, 0 calage_france from dual
                ;
                
--/********************************************************************************************************************************************/
--/*FIN CREATION DES VUES  **********************/
