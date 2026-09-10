# CPOO1 : espace de travail étudiant (3INFO, INSA Rennes, 2026-2027)

Le code de départ des TP de **CPOO1**, Conception et Programmation Orientées
Objet. Trois projets Maven indépendants (Java 21, JUnit 5, Mockito, JaCoCo) :

| Dossier | Séances | Ce que c'est |
|---|---|---|
| `chessball-etd/` | TP3 à TP6 | le squelette du moteur **ChessBall**, puis de son service de matchs : `Position`, `Direction` et `Coup` sont fournis, tout le reste est à écrire question après question |
| `tpa-uml-java-etd/` | piste académique, TP « du diagramme de classes au code » | un paquet Java par question, `pom.xml` prêt |
| `tpa-test-etd/` | piste académique, TP « test logiciel » | `MyPoint` et ses fautes volontaires, deux tests d'exemple |

Les sujets (PDF) et les documents du client ChessBall sont sur Moodle. Les
suites de tests de l'encadrant·e (`src/test/java2`) ne sont **pas** ici : elles
sont distribuées en séance.

## Prendre le squelette du TP3

Le dépôt du binôme a été créé au TP2 sur la forge GitLab de l'INSA. Le
squelette se copie **à sa racine** :

```bash
git clone https://github.com/acherm/cpoo1-etd.git        # une fois, n'importe où
cd <votre-depot-gitlab>
cp -r ../cpoo1-etd/chessball-etd/. .                      # pom.xml, src/, .gitlab-ci.yml
mvn -q test                                               # compile ; aucun test au départ, c'est normal
git add -A && git commit -m "Importe le squelette Maven (#N)" && git push
```

(Ou « Code → Download ZIP » sur cette page, puis copier le contenu de
`chessball-etd/` de la même façon.)

## Environnement

Java 21 (`java --version`), Maven, Git, et un IDE : VS Code avec l'*Extension
Pack for Java* (ouvrir le dossier du projet, Maven est détecté) ou IntelliJ IDEA
(ouvrir `pom.xml` comme projet). Vérification : `mvn -q test` dans le projet.

## Règles du jeu

- **IA interdite par défaut** en TD et TP de CPOO1, sauf les questions marquées 🤖 des sujets.
- Un commit par question, message à l'impératif, issue référencée (`#N`).
- `.gitlab-ci.yml` est fourni dans `chessball-etd/` : activer la CI sur la forge est le bonus du TP3.

Dépôt engendré depuis le matériel du cours (Mathieu Acher, INSA Rennes / IRISA) :
ne pas y proposer de modifications, signaler les erreurs à l'enseignant.
