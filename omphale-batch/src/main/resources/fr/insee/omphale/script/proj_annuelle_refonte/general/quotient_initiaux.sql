//		Calcul des données pour l année de référence
// **************************************************************************



//quotients_initiaux.sql
//
//_quotient_dec <c- _vue_pop_ref, _VUE_DEC
//_quotient_dec_f <c- _vue_pop_ref_f, _VUE_DEC_f
//_quotient_emi <c- _agregation_flux_pop, _agregation_flux
//_quotient_dec_vieux <c- _quotient_dec
//_quotient_dec_vieux <u- _quotient_dec_f
//_quotient_dec_vieux <u-
//_quotient_dec_vieux <i- _quotient_dec_f, _quotient_dec, _quotient_dec_vieux
//_quotient_dec_vieux <u-
//_quotient_dec_reg <c- _quotient_dec_vieux
//_quotient_dec_vieux <u- _quotient_dec_reg
//_quotient_dec <d- _quotient_dec_vieux
//_quotient_dec <i- _quotient_dec_vieux
//_quotient_dec_nes <c- _agrege_nai_pop, _agrege_dec
//_quotient_dec <i- _quotient_dec_nes
//_quotient_nai <c- _vue_pop_ref, _VUE_NAI
//_ratio_nai <c- _vue_pop_ref_r, _vue_pop_nee_r
//_quotient_act <c- vue_pop_ref, _vue_actifs
//_quotient_men <c- _vue_pop_ref, _vue_menages


//parametres : 

//&gt. ;
//define annee_hor=2012
//define zonage_id=0
//define idep=
//define prefix=zp
//define annee_ref=2007
//define age_old=80
//define age_last=99
//define mere_inf=14
//define mere_sup=49
//define decal_annee=1
//define def_projection_id=3290
//define calage=0





//			Quotients de décès
// *********************************************************


		//calcul quotients deces
		create index &&prefix._&&idep._agrege_dec_cle on &&prefix._&&idep._agrege_dec(age,sexe,zone);
		drop table &&prefix._&&idep._quotient_dec;
		create table &&prefix._&&idep._quotient_dec as select distinct a.age, a.sexe, a.zone, decode(nvl(population,0), 0, 0, nvl(deces,0)/population) qd
		from &&prefix._&&idep._vue_pop_ref a, &&prefix._&&idep._VUE_DEC b where a.age=b.age(+) and a.sexe=b.sexe(+) and a.zone=b.zone(+)
		;
 
// 			Quotients de décès France
// *********************************************************

		drop table &&prefix._&&idep._quotient_dec_f;
		create table &&prefix._&&idep._quotient_dec_F as select distinct a.age, a.sexe, a.zone, decode(nvl(population,0), 0, 0, nvl(deces,0)/population) qd
		from &&prefix._&&idep._vue_pop_ref_f a, &&prefix._&&idep._VUE_DEC_f b where a.age=b.age(+) and a.sexe=b.sexe(+) and a.zone=b.zone(+)
		;

		
// potentiellement modifiable
//					Quotients d émigration
// *********************************************************
				
		//calcul quotients emigration
		create index &&prefix._&&idep._agregation_flux_cle on &&prefix._&&idep._agregation_flux(age, sexe, za);
		create index &&prefix._&&idep._agreg_flux_pop_cle on &&prefix._&&idep._agregation_flux_pop(age, sexe, origine);
		drop table &&prefix._&&idep._quotient_emi;
		create table &&prefix._&&idep._quotient_emi as select distinct (a.age - &&decal_annee.) age, a.sexe, a.origine, b.zr destination, decode(nvl(pop,0), 0, 0, nvl(flu,0)/pop) qe
		from &&prefix._&&idep._agregation_flux_pop a, &&prefix._&&idep._agregation_flux b where a.age=b.age(+) and a.sexe=b.sexe(+) and a.origine=b.za(+)
		and a.origine != b.zr and a.age >=&&decal_annee.
		;

		

//			Quotients de décès
//							régression linéaire du vecteur qd_lth sur les ages (calcul intermédiaire)
// *********************************************************************************************************
			
		
			//regression qd pour +$$age_old.
			
			drop table &&prefix._&&idep._quotient_dec_vieux;
			create table &&prefix._&&idep._quotient_dec_vieux as select * from &&prefix._&&idep._quotient_dec
			where age >=&&age_old.;
			
			update &&prefix._&&idep._quotient_dec_vieux a
			set a.qd=(select b.qd from &&prefix._&&idep._quotient_dec_f b
			where  b.age = a.age and b.sexe=a.sexe)
			where a.qd=0;
			
			update &&prefix._&&idep._quotient_dec_vieux a
			set a.qd=0 where a.qd is null;


//			Quotients de décès		
//							Pour les 80 ans et plus :
// ************************************************************
	//	surcharge éventuelle par les quotients de décès France (calcul intermédiaire) 			
			
			
			insert into &&prefix._&&idep._quotient_dec_vieux 
			select a.age, a.sexe, b.zone, a.qd from &&prefix._&&idep._quotient_dec_f a, 
			(select distinct zone from &&prefix._&&idep._quotient_dec) b
			where a.age >=&&age_old. and (a.age, a.sexe, b.zone)
			not in (select age, sexe, zone from &&prefix._&&idep._quotient_dec_vieux)
			;


	//	passage au logarithme (calcul intermédiaire)
			update &&prefix._&&idep._quotient_dec_vieux
			set qd=ln(qd);


	//	régression linéaire du vecteur qd_lth sur les ages (calcul intermédiaire)

			drop table &&prefix._&&idep._quotient_dec_reg;
			create table &&prefix._&&idep._quotient_dec_reg
			as select sexe, zone, regr_slope(qd, age) a, regr_intercept(qd, age) b
			from &&prefix._&&idep._quotient_dec_vieux
			group by sexe, zone;

	//	passage à l exponentiel (résultat final)

			update &&prefix._&&idep._quotient_dec_vieux a
			set a.qd=(select exp(b.a*a.age+b.b) from &&prefix._&&idep._quotient_dec_reg b
			where b.zone=a.zone and b.sexe=a.sexe)
			;
			commit;
			
			delete from &&prefix._&&idep._quotient_dec
			where (age, sexe, zone) in (select age, sexe, zone from &&prefix._&&idep._quotient_dec_vieux);
			
			insert into &&prefix._&&idep._quotient_dec select * from &&prefix._&&idep._quotient_dec_vieux;
			drop table &&prefix._&&idep._quotient_dec_reg;
			drop table &&prefix._&&idep._quotient_dec_vieux;


//			Quotients de décès		
							// 	Pour l age fictif - moins un an -
// ************************************************************************

		//on insere la mortalite infantile (age=-1)
		drop table &&prefix._&&idep._quotient_dec_nes;
		create table &&prefix._&&idep._quotient_dec_nes as select
		a.age, a.sexe, a.zone,  decode(nvl(population,0), 0, 0, nvl(deces,0)/population) qd 
		from &&prefix._&&idep._agrege_nai_pop a,  &&prefix._&&idep._agrege_dec b where a.age=b.age(+) and a.sexe=b.sexe(+) and a.zone=b.zone(+)
		;
		insert into &&prefix._&&idep._quotient_dec select * from &&prefix._&&idep._quotient_dec_nes;
		drop table &&prefix._&&idep._quotient_dec_nes;
		
		
		create unique index &&prefix._&&idep._quotient_dec_cle on &&prefix._&&idep._quotient_dec(age,sexe,zone);


// potentiellement modifiable
//				Quotients de fécondité
// *********************************************************


		//calcul quotients fecondite simplifie
		create index &&prefix._&&idep._agrege_nai_cle on &&prefix._&&idep._agrege_nai(age,zone);
		drop table &&prefix._&&idep._quotient_nai;
		create table &&prefix._&&idep._quotient_nai as select distinct a.age, a.zone, decode(nvl(population,0), 0, 0, nvl(naissances,0)/population) qf
		from &&prefix._&&idep._vue_pop_ref a, &&prefix._&&idep._VUE_NAI b where a.age=b.age(+) and a.sexe=2 and a.age >= &&mere_inf. and a.age <= &&mere_sup. and a.zone=b.zone(+)
		
		;
		create unique index  &&prefix._&&idep._quotient_nai_cle   on  &&prefix._&&idep._quotient_nai (age, zone);

		
// potentiellement modifiable
//				Ratios naissances - enfants
// *********************************************************
		
		//calcul des ratios naissances-enfants
		
		drop table &&prefix._&&idep._ratio_nai;
		create table &&prefix._&&idep._ratio_nai as select a.zone, a.age , decode(nvl(b.naissances,0), 0, 0, nvl(a.population,0)/b.naissances) ratio 
		from &&prefix._&&idep._vue_pop_ref_r a, &&prefix._&&idep._vue_pop_nee_r b
		where a.zone=b.zone(+)
		;

		
		
//				Taux actifs / ménages / éléves
// *********************************************************
			//calcul taux actifs et menage
			
			drop table &&prefix._&&idep._quotient_act;
			create table &&prefix._&&idep._quotient_act as select a.age, a.sexe, a.zone, decode(nvl(a.population,0),0,0,nvl(b.population,0)/a.population) qa
			from &&prefix._&&idep._vue_pop_ref a, &&prefix._&&idep._vue_actifs b
			where a.age=b.age and a.sexe=b.sexe and a.zone=b.zone
			;
			
			create unique index  &&prefix._&&idep._quotient_act_cle   on  &&prefix._&&idep._quotient_act (age, sexe, zone);
			
			drop table &&prefix._&&idep._quotient_men;
			create table &&prefix._&&idep._quotient_men as select a.age, a.sexe, a.zone, decode(nvl(a.population,0),0,0,nvl(b.population,0)/a.population) qm
			from &&prefix._&&idep._vue_pop_ref a, &&prefix._&&idep._vue_menages b
			where a.age=b.age and a.sexe=b.sexe and a.zone=b.zone
			;
			
			create unique index  &&prefix._&&idep._quotient_men_cle   on  &&prefix._&&idep._quotient_men (age, sexe, zone);
			
			
			
			
