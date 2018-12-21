--//Ajout d'un type_pop 6 legale_anterieure

insert into type_pop values (6, 'legale_anterieure');

--// création des Tables cb_avg_dec_ant et cb_avg_nai_ant

create table cb_avg_dec_ant as select * from cb_avg_dec;
truncate table cb_avg_dec_ant;
commit;

create table cb_avg_nai_ant as select * from cb_avg_nai;
truncate table cb_avg_nai_ant;
commit;