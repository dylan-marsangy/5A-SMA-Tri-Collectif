# 5A-SMA-Tri-Collectif
#### Dylan MARSANGY - Laura PHILIBERT

# Prérequis
- Java 8.
- Maven 4.0

# Application

## Comportement d'un agent
- A chaque tour, un agent perçoit son environnement et effectue un déplacement aléatoire (en évitant toutefois de rencontrer un autre agent).
- Si l'agent ne tient pas de bloc et que sa destination est occupée par un bloc, il tente de le prendre. S'il réussit, il le prend et se déplace sur la case anciennement occupée par le bloc. Sinon, il ne fait rien et reste à sa place.
- S'il tient un bloc et que sa destination est libre, il y bouge en tentant de déposer au passage le bloc qu'il tient sur sa case d'origine. S'il réussit, il le fait. Sinon, il garde son bloc sur lui.

## Statistiques calculées à l'exécution
A chaque exécution de l'algorithme, diverses statistiques sont calculées afin d'estimer la qualité des paramètres donnés en entrée.
Nous nous sommes principalement concentrés sur les voisinages des Blocs posés dans l'environnement à la fin de l'algorithme ainsi que sur les colonies formées.
Le détail des indicateurs calculés est disponible ci-dessous :
- Nombre de blocs A posés
- Nombre de blocs B posés
- Proportion de A voisins d'autres A
- Proportion de A voisins de B
- Proportion de B voisins d'autres B
- Nombre de colonies
- Taille moyenne d'une colonie (en nombre de blocs)
- Proportion de A par colonie
- Proportion de B par colonie

Ces indicateurs sont bien entendu extrêmement dépendants de la taille de voisinage choisie au départ.
Cette dernière peut être modifiée en changeant la valeur de la constante ```NEIGHBOURHOOD_SIZE``` de la classe ```SMAConstants```.

Les statistiques sont calculées sur plusieurs exécution de l'algorithme avec les mêmes paramètres. 
Cela permet de moyenner les résultats obtenus et de réduire le nombre de cas extrêmes pouvant survenir.
Le nombre d'exécution peut être modifié en changeant la valeur de la constante ```NB_RUN``` de la classe ```SMAConstants```.

L'ensemble des statistiques après le lancement de l'application est enfin sauvegardé dans un fichier Excel au format xlsx nommé ```demo.xlsx``` contenu dans le dossier ```extern/``` du projet.
Dans le cas où plusieurs tests sur les paramètres sont lancés (comme dans la classe ```ApplicationTest```), les résultats sont sauvegardés dans des feuilles de calcul différentes pour plus de lisibilité.
De plus, chaque tableau de résultat possède un entête rappelant les paramètres utilisés au lancement (Nombre de blocs A et B, nombre d'agents, etc.).

# Lancer l'application

## En mode release

### Depuis les sources
Exécutez la fonction `main` de la classe `SMApplicationV1` à la racine du package `org/polytech` dans le dossier des sources `src`.
Cette classe est pourvue de nombreux attributs servant à initialiser l'environnement et les agents (dimensions de la grille, nombre d'agents, constantes pour les formules de prise et dépôt de bloc, etc).
Modifiez-les à votre guise.

Si vous souhaitez modifier le nombre d'itérations de l'environnement ou la fréquence d'affichage de la grille pendant l'exécution de l'application, modifiez les attributs correspondants dans la classe `SMAConstants` également à la racine du package `org/polytech` dans le dossier `src`.

### Avec le JAR (conseillé)
Exécutez le jar de l'application en exécutant la commande `java -jar SMA-Tri-MARSANGY-PHILIBERT.java` dans un terminal de commande.

## En mode test
Le mode test permet de jouer avec les différents paramètres.

Pour ce faire, exécutez la classe `ApplicationTest` dans le package `org/polytech/` du dossier des tests `test`.
Cela va résulter en l'exécution de différents tests paramétrés écrits avec `JUnit 5`.

Chaque test paramétré ressemble à ceci :
```
@ParameterizedTest
@DisplayName("Run Application - Error (e)")
@ValueSource(doubles = {ERROR, 0.1, 0.5, 0.9})
public void runApplication_numberBlocksA(double error) {
    runSimulation(NUMBER_BLOCKS_A, NUMBER_BLOCKS_B, NUMBER_AGENTS,
                  GRID_ROWS, GRID_COLUMNS,
                  MEMORY_SIZE, SUCCESSIVE_MOVEMENTS, K_MINUS, K_PLUS, error);
}
```
Modifiez les valeurs présentes dans l'annotation `@ValueSource`. Un test sera exécuté pour chacune de ces valeurs sur le paramètre en question (ici, la probabilité d'erreur e).
Les autres paramètres ont une valeur par défaut attribuée qu'il est possible de modifier directement dans les attributs de ladite classe.

Il existe un de ces tests pour chacun des 10 paramètres. Have fun !