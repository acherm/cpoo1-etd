# Suites de tests de l'encadrant·e (CPOO1)

Chaque dossier est une suite JUnit 5 à copier **dans `src/test/java2/` du
projet concerné**, dossier que les `pom.xml` des squelettes déclarent déjà
comme sources de test : `mvn test` l'exécute ensuite, quel que soit l'IDE.

| Dossier | Projet | Quand | Copier dans |
|---|---|---|---|
| `velo/` | `velo-etd` | TP2 Q11 | `src/test/java2/velo/` |
| `chessball-tp3/` | `chessball-etd` | TP3 Q10 | `src/test/java2/chessball/` |
| `chessball-tp4/` | `chessball-etd` | TP4 Q5 | `src/test/java2/chessball/` |

Une suite **compile contre les signatures du sujet** (noms de classes, de
méthodes, types de retour) : si `mvn test` ne compile pas, c'est une
signature qui diffère, pas un test qui échoue. Elle sert d'oracle en fin de
séance : un test rouge est un cas que vos propres tests avaient manqué.
Vous pouvez la lire avant, ce n'est pas interdit ; c'est moins instructif.
