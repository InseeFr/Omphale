/*Creation des indexes pour accélèrer la lecture sur ZZ_TEMP_POP_LEGALE et CB_POP_LEGALE*/
create index zz_temp_pop_legale_an on ZZ_TEMP_POP_LEGALE (annee, type_pop);
drop index cb_pop_legale_an;
create index cb_pop_legale_an on CB_POP_LEGALE (annee, type_pop);

/*Suppression des données de ZZ_TEMP_POP_LEGALE déjà présente dans CB_POP_LEGALE avant insertion*/
delete from CB_POP_LEGALE 
	where 
		(annee, type_pop) in (select 
								distinct a.annee, a.type_pop  
								from ZZ_TEMP_POP_LEGALE a);

/*Insertion des données de ZZ_TEMP_POP_LEGALE dans CB_POP_LEGALE*/								
insert into CB_POP_LEGALE 
	select * 
		from ZZ_TEMP_POP_LEGALE 
		where 
			ZZ_TEMP_POP_LEGALE.commune in (select com.id_commune 
											from COMMUNE com);
											
/*Suppression des indexes et de la table temporaire*/
drop table ZZ_TEMP_POP_LEGALE;
drop table zz_temp_pop_legale_ctl;
drop index cb_pop_legale_an;