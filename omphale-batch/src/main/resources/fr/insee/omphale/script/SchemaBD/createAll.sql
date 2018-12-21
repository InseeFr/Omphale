//parametres communs
//renseigner un répertoire pour rendre fonctionnelle cette instruction
define repertoire=

//traitement
start &&repertoire.\createTableOmphale.sql
start &&repertoire.\createPKOmphale.sql
start &&repertoire.\createFKOmphale.sql
start &&repertoire.\createIndexOmphale.sql
start &&repertoire.\createSEQOmphale.sql
start &&repertoire.\createViewOmphale.sql
start &&repertoire.\initDataOmphale.sql