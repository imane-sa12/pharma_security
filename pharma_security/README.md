# TP2 - Sécurisation de gestion de pharmacie (Spring Boot + Spring Security + MySQL)

## Lancer
1. Démarrer MySQL (la base `pharma_security` est créée automatiquement).
2. Adapter `spring.datasource.password` dans `src/main/resources/application.properties`.
3. `mvn spring-boot:run` puis ouvrir http://localhost:8080

## Comptes de test
| Utilisateur | Mot de passe | Rôle |
|---|---|---|
| admin | admin123 | ADMIN |
| pharma | pharma123 | PHARMACIEN |
| caisse | caisse123 | CAISSIER |
| client | client123 | CLIENT |

## Droits
| URL | ADMIN | PHARMACIEN | CAISSIER | CLIENT |
|---|---|---|---|---|
| /admin/** | oui | non | non | non |
| /medicaments (lecture) | oui | oui | non | oui |
| /medicaments/stock/** | oui | oui | non | non |
| /medicaments/save, delete | oui | non | non | non |
| /ventes/** | oui | oui | oui | non |
