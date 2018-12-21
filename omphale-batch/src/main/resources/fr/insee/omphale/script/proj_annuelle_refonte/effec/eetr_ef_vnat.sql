//Évolution des quotients	
//	Évolution des quotients d’émigration
//  	Ventilation du national : potentiellement modifiable



//eetr_ef_vnat.sql
//
//_cube <c- cb_hypothese
//_cube <u- _nb_pas
//_h_emig_etr <c- cb_hypothese, _cube, _vue_imm_etr, _vue_imm_etr_f
//_emig_etrp <d- _cube
//_emig_etrp <i- _h_emig_etj


//parametres : 



//define idep=
//define prefix=ZP
//define annee_ref=2012
//define annee_hor=2017
//define idhyp=518
//define methode=ventil
//define tq=eetr
//define annee_deb=2012
//define annee_fin=2017
//define sexe_deb=1
//define sexe_fin=1
//define age_deb=20
//define age_fin=23

//define zone_locale=AIX
//define zone_destination=%

//Traitements
//ventilation du national cube hypothese




--/* generation de la table cube */
	//projection des immigrants de l etranger

		drop table &&prefix._&&idep._cube;
		create  table &&prefix._&&idep._cube as
			select 
				decode(sign(min(annee)-&&annee_deb.),
				1, 
				min(annee), &&annee_deb.)  annee_deb,
		 		decode(sign(max(annee)-&&annee_fin.),
		 		-1,
		 		max(annee), &&annee_fin.)  annee_fin 
		 		from 
		 			cb_hypothese 
		 		where id_hypothese=&&idhyp.
		;
		
		
		update &&prefix._&&idep._cube 
		set 
			annee_deb = decode(sign(annee_deb-&&annee_ref.),1, annee_deb, &&annee_ref.),
			annee_fin = (
							select 
								decode(sign(annee_fin-annee_hor),-1, annee_fin, annee_hor) 
								from 
								&&prefix._&&idep._nb_pas
						)
		;
		commit;
	

	--/* calcul de l'emigration ventilée nationale */
		drop table &&prefix._&&idep._h_emig_etr;
		create table 
			&&prefix._&&idep._h_emig_etr 
			as 
				select distinct
					a.age, 
					a.sexe, 
					a.annee, 
					b.zone, 
					a.valeur*b.population/d.population population
				from 
					cb_hypothese a, &&prefix._&&idep._cube c,
					(select zone, sum(population) population from &&prefix._&&idep._vue_imm_etr group by zone) b,
					(select sum(population) population from &&prefix._&&idep._vue_imm_etr_f) d
				where 
					a.id_hypothese=&&idhyp.
					and a.sexe >=&&sexe_deb. 
					and a.sexe <=&&sexe_fin. 
					and a.age >=&&age_deb. 
					and a.age <=&&age_fin.
					and a.annee >= c.annee_deb 
					and a.annee <= c.annee_fin
			;


			--/* Menage sur d'autres tables temporaires  */
				delete from &&prefix._&&idep._emig_etrp a
				where 
				a.zone like '&&zone_locale.'
				and
				a.annee >= (select annee_deb from &&prefix._&&idep._cube)
				and
				a.annee <= (select annee_fin from &&prefix._&&idep._cube)
				and
				a.sexe >=&&sexe_deb. 
				and
				a.sexe <=&&sexe_fin. 
				and 
				a.age >=&&age_deb. 
				and a.age <=&&age_fin.
				;
				
				commit;
				
				insert into &&prefix._&&idep._emig_etrp
				select * from &&prefix._&&idep._h_emig_etr
				where 
				zone like '&&zone_locale.'
				;
				
				commit;
				drop table &&prefix._&&idep._h_emig_etr;