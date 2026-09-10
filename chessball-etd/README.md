# chessball-etd : squelette du moteur ChessBall (CPOO1, TP3 à TP6)

Projet Maven prêt (Java 21, JUnit 5, Mockito, JaCoCo). Il se dépose dans le
dépôt Git du binôme créé au TP2 (voir TP3 Q1).

```bash
mvn -q test          # compile ; aucun test au départ, c'est normal
```

## Ce qui est fourni (à lire, pas à réécrire)

| Fichier (`src/main/java/chessball/moteur/`) | Rôle |
|---|---|
| `Position.java` | case du plateau : `record`, validation en construction, fabrique `Position.of("b2")` |
| `Direction.java` | les huit directions d'un tir |
| `Coup.java`, `Deplacement.java`, `Passe.java`, `Tir.java` | les trois coups d'un tour, interface `sealed` et ses `record` |

Tout le reste (`Equipe`, `Piece`, `Plateau`, `Case`, `Ballon`, les motifs de
déplacement, la partie) est à écrire, question après question, avec ses tests
dans `src/test/java/chessball/moteur/`.

## `src/test/java2`

Dossier réservé à la **suite de tests de l'encadrant·e** (TP3 Q9) : le
`pom.xml` le déclare comme sources de test, `mvn test` l'exécute une fois
l'archive dézippée dedans.

## Rituels

Un commit par question, message à l'impératif, issue référencée (`#N`).
`.gitlab-ci.yml` est fourni : activer la CI sur la forge est le bonus du TP3.
