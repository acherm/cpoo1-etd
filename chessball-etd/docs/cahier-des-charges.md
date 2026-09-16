# ChessBall — cahier des charges du binôme

> CPOO1 — 3INFO, 2026-2027. **Gabarit à compléter** du TD2 au TP5 : c'est le
> document rendu à la fin du module, avec le code. Les sections 1 et 2
> reprennent les deux documents du client tels quels (ils ne disent pas
> tout, c'est voulu). Tout le reste est à vous : les réponses officielles
> obtenues en séance, le glossaire, les modèles, les scénarios, la
> traçabilité vers le code et les tests. Un diagramme se donne par son
> source (`.puml`, `.uvl`, dans ce dossier) **et** son image exportée
> (`.svg`), référencée ici par `![titre](fichier.svg)`.
>
> Remplacez chaque « *À compléter* » ; supprimez les consignes en italique
> quand la section est faite. Chaque décision du client porte un numéro
> (J1, J2… pour le jeu ; S1, S2… pour le service) : citez-les dans les
> modèles, les tests et le rapport.

| Section | Séance | Rempli ? |
|---|---|---|
| 3. Réponses officielles du client (jeu) | TD2 | |
| 4. Acteurs et périmètre | TD2 | |
| 5. Glossaire | TD2 | |
| 6. Modèle du domaine et diagrammes d'objets | TD2 (papier) → TP3 (studio) → TP4 (service) | |
| 7. Stories, critères, scénarios | TD2 → TP5 | |
| 8. Machine à états du match | TD2 | |
| 9. Réponses officielles du client (service) et cas d'utilisation | TP4 | |
| 10. Variantes : le feature model | TP4 | |
| 11. Points de variation dans le code | TP4–TP5 | |
| 12. Traçabilité modèle ↔ code ↔ tests | TP3–TP5 | |
| 13. Rapport de test | TP5 | |

## 1. Le jeu, tel que le client l'a écrit

*Copie conforme de `regles-chessball.pdf`. Ne pas corriger ce texte : les
corrections sont vos réponses officielles, en §3.*

ChessBall est un jeu de football qui se joue sur un plateau d'échecs de 8×8
cases (colonnes `a`–`h`, rangées `1`–`8`). Deux équipes s'affrontent, les
Bleus et les Rouges. Chaque équipe dispose de six pièces : une dame, deux
tours, deux fous et un cavalier. Les pièces se déplacent comme aux échecs :
la dame glisse en ligne ou en diagonale, les tours en ligne, les fous en
diagonale, le cavalier saute en « L ». Les pièces qui glissent ne peuvent pas
traverser une case occupée.

Avant le coup d'envoi, chaque équipe installe ses pièces sur ses deux
premières rangées, comme elle l'entend. Un unique ballon est placé au centre
du terrain pour l'engagement.

Les équipes jouent à tour de rôle. À son tour, une équipe effectue une seule
des actions suivantes : déplacer une de ses pièces selon son motif de
déplacement ; passer le ballon : la pièce porteuse du ballon l'envoie à une
pièce de son équipe située sur une ligne que la porteuse pourrait emprunter ;
tirer : la porteuse propulse le ballon en direction de la ligne de fond
adverse, le long d'une ligne qu'elle pourrait emprunter.

Une pièce qui atteint la case du ballon s'en empare et devient porteuse. Une
passe ou un tir peut être intercepté par une pièce adverse qui se trouve sur
le chemin du ballon.

Il y a but lorsque le ballon franchit la ligne de fond adverse. L'équipe qui
marque ajoute un point à son score, puis un nouvel engagement a lieu au
centre du terrain. Le match se joue en deux mi-temps de K tours chacune (la
valeur de K est à convenir avec l'organisateur du tournoi). À la fin du
match, l'équipe qui a marqué le plus de buts l'emporte.

## 2. Le service, tel que le client l'a écrit

*Copie conforme de `service-chessball.pdf` (remis au TP4).*

Le club ChessBall grandit et nous voulons un petit service en ligne pour
organiser la vie du club : pas une interface graphique pour l'instant, juste
le « cerveau » du service. Les joueurs et joueuses s'inscrivent avec un
pseudonyme. Une fois inscrit·e, on peut demander à jouer : le service apparie
alors deux personnes disponibles et crée un match. Nous tenons à ce que
l'appariement soit équitable : c'est plus intéressant quand les adversaires
sont de force comparable. À la fin d'un match, le service enregistre le
résultat (victoire, défaite, ou nul) et met à jour un classement « à la Elo,
comme aux échecs ». Tout le monde doit pouvoir consulter le classement du
club à tout moment. Il arrive que quelqu'un déclare forfait avant ou pendant
un match. Par ailleurs, un joueur ne devrait pas disputer trop de matchs à la
fois.

## 3. Réponses officielles du client (jeu) — TD2

*Une ligne par ambiguïté repérée : la question fermée que vous avez posée,
la réponse du client. Numérotez J1, J2… Au moins six. Les ambiguïtés que
vous n'avez pas posées mais dont une suite de tests de l'encadrant·e a
révélé la réponse (TP3, TP4) s'ajoutent ici, marquées « (suite prof) ».*

| N° | Question posée au client | Réponse officielle |
|---|---|---|
| J1 | *À compléter* | |
| J2 | | |

## 4. Acteurs et périmètre — TD2

**Acteurs.** *À compléter : qui interagit avec le jeu, avec le service ; y
a-t-il un arbitre humain ?*

**Hors périmètre pour ce semestre.** *À compléter : la liste explicite (IHM
graphique, réseau, persistance, IA « forte »…).*

## 5. Glossaire — TD2

*Une définition = une phrase, sans circularité. Les termes du glossaire sont
ceux du diagramme de classes (§6) et du code (§12) : même mot partout.*

| Terme | Définition |
|---|---|
| Plateau | *À compléter* |
| Case | |
| Pièce | |
| Motif de déplacement | |
| Porteuse | |
| Ballon | |
| Coup (déplacement, passe, tir) | |
| Interception | |
| Engagement | |
| Trait | |
| Tour (de jeu) | |
| Mi-temps | |
| But | |
| Score | |
| Match | |
| *(service, TP4)* Compte, Joueur, Appariement, Classement, Forfait | |

## 6. Modèle du domaine et diagrammes d'objets — TD2, TP3, TP4

**6.1 Le modèle du domaine** (`domaine.puml` / `domaine.svg`). *Concepts,
associations nommées, cardinalités dans les deux sens, compositions
justifiées, le ballon et sa contrainte `{xor}`. Étendu au service au TP4
(Compte, Joueur, Match, Appariement, Classement) : quelle association entre
le Match du service et la Partie du moteur ?*

![Modèle du domaine](domaine.svg)

**6.2 Décisions de modélisation.** *Où vit le ballon, et pourquoi (quelle
modélisation était fausse) ; ce qui est composition et ce qui ne l'est pas ;
le mot « tour » ; les termes du glossaire qui ne sont pas des classes, et
pourquoi.*

**6.3 Diagrammes d'objets** (`depart.puml`, `apres.puml`). *La position de
départ simplifiée, puis la position après un coup : ils instancient §6.1,
sinon c'est §6.1 qui est faux.*

![Position de départ](depart.svg)

## 7. Stories, critères, scénarios — TD2, TP5

**7.1 User stories.** *« En tant que… je veux… afin de… », au moins deux
acteurs.*

**7.2 Critères d'acceptation** *d'au moins une story.*

**7.3 Scénarios Gherkin.** *Nominal, refus, cas limite ; le scénario
manquant révélé par la machine à états (§8). Chaque scénario nomme le test
qui le joue (§12).*

```gherkin
Scénario: À compléter
  Étant donné …
  Quand …
  Alors …
```

**7.4 Contrat** *d'une opération (préconditions / postconditions) : ce que le
contrat dit que le scénario ne dit pas.*

## 8. Machine à états du match — TD2

*`etats-match.puml` / `.svg` : engagement, alternance, but, mi-temps, fin.
Événement et garde sur chaque transition ; la transition qu'aucun scénario ne
couvrait.*

![Machine à états du match](etats-match.svg)

## 9. Réponses officielles du client (service) et cas d'utilisation — TP4

| N° | Question posée au client | Réponse officielle |
|---|---|---|
| S1 | *À compléter (« force comparable » ? « trop de matchs » ? « à la Elo » ? forfait ? pseudo ?)* | |

**Cas d'utilisation du service.** *Le diagramme (`service-uc.puml`), et pour
« Demander un match » (rédigé au CM1) la liste de ses chemins, chacun avec le
test qui le joue (§12). Optionnel : un second cas d'utilisation au format du
cours (sept rubriques).*

## 10. Variantes : le feature model — TP4

*`chessball.uvl` / `.svg`, la contrainte du client, le nombre de
configurations valides (votre calcul, puis le compteur d'UVL Studio).*

![Feature model](chessball.svg)

## 11. Points de variation dans le code — TP4, TP5

*Quels points de variation du feature model existent déjà dans le code (K,
l'IA adverse, l'appariement, la fin de mi-temps…), sous quelle forme
(paramètre, interface + implémentations : le patron Stratégie,
`strategie.puml`), et lesquels n'y sont pas.*

## 12. Traçabilité modèle ↔ code ↔ tests — TP3 à TP5

*Une ligne par élément de modèle : la classe Java qui le réalise, le ou les
tests qui l'attestent. C'est cette table qui prouve que le code vient du
modèle.*

| Élément du modèle (§) | Classe(s) Java | Test(s) |
|---|---|---|
| Équipe *aligne* 6 pièces (§6.1) | *À compléter* | |
| Ballon posé XOR porté (§6.1) | | |
| Coup refusé ⇒ auto-transition (§8) | | |
| Scénario « … » (§7.3) | | |
| J7 passe (§3) | | |
| S1 appariement (§9) | | |

## 13. Rapport de test — TP5

*Voir `rapport.md` (plan imposé au TP5 : ce qui est implémenté, couverture
avant/après, score de mutation avant/après, doubles, ce que les tests n'ont
pas trouvé). Résumez ici en cinq lignes les chiffres finaux : couverture de
branches par paquet, score de mutation par paquet, nombre de tests.*
