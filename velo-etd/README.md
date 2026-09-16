# velo-etd : squelette du TP2 (CPOO1)

Projet Maven prêt (Java 21, JUnit 5, JaCoCo avec règle de couverture à
100 % sur `mvn verify`). Un seul paquet, `velo`, vide : `Velo`, `Guidon`,
`Selle`, `Roue` s'écrivent dans `src/main/java/velo/`, vos tests dans
`src/test/java/velo/`.

```bash
mvn -q test
```

Vert, aucun test au départ : c'est normal.

La suite de tests de l'encadrant·e (TP2 Q11) est dans `suites-prof/velo/`
de l'espace de travail : à copier dans `src/test/java2/velo/`, dossier déjà
déclaré comme sources de test dans le `pom.xml`.
