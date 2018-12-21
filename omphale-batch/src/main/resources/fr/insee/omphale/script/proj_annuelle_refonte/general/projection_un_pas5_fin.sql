

//csv flux
insert into &&prefix._&&idep._csv_emig
select * from &&prefix._&&idep._emmigf
;
insert into &&prefix._&&idep._csv_immig
select * from &&prefix._&&idep._immigf
;
insert into &&prefix._&&idep._pcsv_emmflux
select * from &&prefix._&&idep._emmflux
;

update &&prefix._&&idep._pas set a_faire=1 where annee_pas = (select annee_pas from &&prefix._&&idep._pas_a_traiter);
delete from &&prefix._&&idep._pas_a_traiter;
commit;



truncate table &&prefix._&&idep._immigf;
truncate table &&prefix._&&idep._emmigf;
truncate table &&prefix._&&idep._emmflux;
