# chessball-etd : squelette du moteur ChessBall (CPOO1, TP3 à TP5)

Projet Maven prêt (Java 21, JUnit 5, Mockito, JaCoCo, profil PIT). Il se
récupère au TP3 (« Code → Download ZIP » sur la page de l'espace de travail,
ou `git clone`) et vit jusqu'au rendu du TP5.

```bash
mvn -q test
```

Vert, aucun test au départ : c'est normal.

## Ce qui est fourni (à lire, pas à réécrire)

| Fichier (`src/main/java/chessball/moteur/`) | Rôle |
|---|---|
| `Position.java` | case du plateau : classe-valeur immuable, validation en construction, fabrique `Position.of("b2")` |
| `Direction.java` | les huit directions d'un tir |
| `Coup.java`, `Deplacement.java`, `Passe.java`, `Tir.java` | les trois coups d'un tour, une interface et ses classes |
| `Couleur.java` | `BLEUS`, `ROUGES`, `adverse()`, `rangeeAdverse()` |
| `TypePiece.java` | `DAME`, `TOUR`, `FOU`, `CAVALIER` |
| `Refus.java` | les motifs de refus d'un coup (`AUCUN` = légal) |
| `Statut.java` | les états du match : `ENGAGEMENT`, `EN_JEU`, `MI_TEMPS`, `TERMINE` |
| `ResultatCoup.java` | ce que `Partie.jouer` renvoie : `accepte()`, `refus()`, `but()` |

Tout le reste (`Equipe`, `Piece`, `Plateau`, `Case`, `Ballon`, `Motif`,
`Partie`, `Score`, l'IA, le service) est à écrire, question après question,
avec ses tests dans `src/test/java/chessball/…`. Les sujets nomment les
classes et les méthodes attendues : les suites de l'encadrant·e et le code
fourni compilent contre ces signatures.

## Ce qui se copie plus tard, depuis l'espace de travail

| Quand | Quoi | Où le mettre |
|---|---|---|
| TP3 Q10 | `suites-prof/chessball-tp3/` | `src/test/java2/` |
| TP4 Q6 | `extras/moteur/StrategieAdversaire.java` | `src/main/java/chessball/moteur/` |
| TP4 Q7 | `extras/ui/Console.java`, `extras/ui/Main.java` | `src/main/java/chessball/ui/` |
| TP4 Q5 | `suites-prof/chessball-tp4/` | `src/test/java2/` |

`src/test/java2` est déclaré comme sources de test dans le `pom.xml` :
`mvn test` l'exécute, quel que soit l'IDE.

## Commandes utiles

```bash
mvn -q test
mvn verify
mvn -q compile exec:java
mvn -Pmutation test-compile org.pitest:pitest-maven:mutationCoverage
```

Dans l'ordre : les tests ; les tests plus la règle JaCoCo (couverture totale
sur ce que vous écrivez, rapport dans `target/site/jacoco/index.html`) ; le
match IA contre IA du TP4 ; la mutation du TP5 (rapport dans
`target/pit-reports/index.html`).

## Rendu

Le rendu du module (TP5) est un dépôt Git contenant ce projet, vos modèles
dans `docs/` et le rapport `docs/rapport.md`. `git init` ici quand vous
voulez, aucune forge imposée.

## `docs/cahier-des-charges.md`

Le **cahier des charges du binôme** : un gabarit dont les sections 1 et 2
sont les deux documents du client, et dont tout le reste (réponses
officielles, glossaire, modèles, scénarios, traçabilité vers le code et les
tests) se remplit du TD2 au TP5. Les `.puml`, `.uvl` et leurs SVG se mettent
à côté. C'est ce cahier, complété, qui est rendu avec le code et le rapport
à la fin du module.
