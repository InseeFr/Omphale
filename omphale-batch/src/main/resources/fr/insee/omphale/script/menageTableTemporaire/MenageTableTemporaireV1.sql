begin

	for cur_rec in (select table_name from user_tables where table_name like 'ZP_%' or table_name like 'ZZ_%') loop
    execute immediate ('drop table '|| cur_rec.table_name);
  end loop;

	for cur_rec in (select view_name from user_views where view_name like 'ZP_%' or view_name like 'ZZ_%') loop
    execute immediate ('drop view '|| cur_rec.view_name);
  end loop;

end;