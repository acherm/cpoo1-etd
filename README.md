# CPOO1 : espace de travail étudiant (3INFO, INSA Rennes, 2026-2027)

Le code de départ des TP de **CPOO1**, Conception et Programmation Orientées
Objet (Java 21, Maven, JUnit 5, Mockito, JaCoCo, PIT). Récupération : bouton
**Code → Download ZIP** sur cette page, ou `git clone` si vous préférez.

| Dossier | Séance | Ce que c'est |
|---|---|---|
| `velo-etd/` | TP2 | squelette Maven, un paquet `velo` vide : `Velo`, `Guidon`, `Selle`, `Roue` à écrire, puis leurs tests |
| `tpa-test-etd/` | TP2 (Q12) | `MyPoint` et ses fautes volontaires, deux tests d'exemple : la javadoc est la spécification |
| `chessball-etd/` | TP3 à TP5 | le squelette du moteur **ChessBall** puis de son service : `Position`, `Coup`, les énumérations et `ResultatCoup` sont fournis, tout le reste est à écrire question après question |
| `suites-prof/` | TP2 Q11, TP3 Q10, TP4 Q5 | les suites de tests de l'encadrant·e, à copier dans `src/test/java2/` quand le sujet le demande (voir son README) |
| `extras/` | TP4 Q6–Q7 | `moteur/StrategieAdversaire.java` (l'interface de l'IA), `ui/Console.java` et `ui/Main.java` (le match IA contre IA en console) |

Les sujets (PDF) et les documents du client ChessBall sont sur Moodle.

## Prendre un squelette

```bash
git clone https://github.com/acherm/cpoo1-etd.git
cp -r cpoo1-etd/chessball-etd <dossier-de-votre-binome>
cd <dossier-de-votre-binome>/chessball-etd
mvn -q test
```

Aucun test au départ, c'est normal. Le rendu du module (TP5) est un dépôt
Git de votre binôme contenant le projet, vos modèles dans `docs/` et le
rapport : `git init` quand vous voulez, aucune forge imposée.

## Environnement

Java 21 (`java --version`), Maven, et un IDE : VS Code avec l'*Extension
Pack for Java* (ouvrir le dossier du projet, Maven est détecté) ou IntelliJ
IDEA (ouvrir `pom.xml` comme projet). Vérification : `mvn -q test` dans le
projet.

## Règle du cours

**IA interdite par défaut** en TD et TP de CPOO1, sauf les questions
marquées 🤖 des sujets.

Dépôt engendré depuis le matériel du cours (Mathieu Acher, INSA Rennes / IRISA) :
ne pas y proposer de modifications, signaler les erreurs à l'enseignant.
