# Omphale  

L’évolution de la population d’un territoire dans le temps résulte de l’interaction entre trois composantes démographiques : les naissances, les décès et les migrations. Omphale 2017 propose,
comme Omphale 2010 précédemment, une modélisation de ces composantes à partir de l’observation récente des comportements démographiques. Omphale 2017 projette la population par sexe et âge détaillé sur un pas annuel jusqu’à l’horizon 2050. Il permet de réaliser des projections sur tout zonage respectant certaines conditions. Le modèle permet d’étudier la déformation dans le temps de la pyramide des âges. Le modèle permet également de réaliser des projections de ménages et d’actifs. L’outil permet sur le même modèle la réalisation de projections d’élèves ; celles-ci n’ont dans les faits jamais été proposées dans Omphale.

# Le code Omphale  

Omphale est écrit en Java, mais il comprend de nombreux fichiers SQL (ordonnés et lancés par le batch Java « traitement d'une projection ») qui composent ce qu’on appelle le moteur de projection.
Le code d'Omphale est composé d'une partie batch et d'une partie core (cette dernière est utilisée à la fois par la partie batch et la partie ihm non diffusée ici).  
La partie batch comprend à la fois Omphale 2010 (omphale) et Omphale 2017 (omphrefo). Pour passer de l'un à l'autre il suffit de renseigner le pas utilisé (quiquennal ou annuel) dans les properties  (omphale-batch/src/main/resources/assembly/parametre.properties). Toutefois les fichiers pdf en sortie d’une projection ont des libellés adaptés uniquement pour Omphale 2017 et son pas annuel.  
Il existe deux types de batch pour chaque version de Omphale, l'un de chargement du RP et de l'état civil dans la base de Omphale et l'autre de traitement d'une projection.  
Le lanceur de batch de Omphale 2010 se trouve à cet endroit :
omphale-batch/src/main/java/fr/insee/omphale/batch/Lanceur.java  
Celui du batch de Omphale 2017 se trouve à cet endroit : 
omphale-batch/src/main/java/fr/insee/omphrefo/batch/Lanceur.java  
L'argument à renseigner lors du lancement du batch est BATCH_TRAITEMENT_PROJECTION pour lancer un batch de traitement de projection ou BATCH_CHARGEMENT pour lancer un batch de chargement du RP et de l'état civil.

# Licence  

Omphale Copyright © 2010-2018, Institut national de la statistique et des études économiques This program is free software : you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program. If not, see https://www.gnu.org/licenses/. 
