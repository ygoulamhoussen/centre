# Centre - Application de Gestion Immobilière

## Fonctionnalités

### Gestion des Quittances avec Écritures Comptables et Recettes Automatiques

L'application génère automatiquement une recette et une écriture comptable lors de la création d'une nouvelle quittance. Cette fonctionnalité permet de :

- **Création automatique de recette** : Une recette de type "QUITTANCE" est créée automatiquement lors de la création d'une quittance
- **Création automatique d'écriture comptable** : Une écriture comptable de type "RECETTE" est créée automatiquement et liée à la recette
- **Calcul automatique** : Le montant total inclut le loyer, les charges et la caution (si applicable)
- **Traçabilité complète** : La recette et l'écriture comptable sont liées à la propriété avec les informations du locataire
- **Gestion complète** : Possibilité de modifier et supprimer les écritures comptables

#### Détails techniques

**Recette automatique :**
- **Type** : QUITTANCE
- **Intitulé** : "Loyer - [Nom du locataire] - Période: [Date début] à [Date fin]"
- **Montant** : Somme du loyer + charges + caution (si incluse)
- **Date** : Date de début de la quittance
- **Commentaire** : "Recette générée automatiquement depuis la quittance #[ID]"

**Écriture comptable automatique :**
- **Type** : RECETTE
- **Date d'écriture** : Date de début de la quittance
- **Montant** : Même montant que la recette
- **Commentaire** : "Quittance de loyer - [Nom du locataire] - Période: [Date début] à [Date fin]"
- **Liaison** : Liée à la recette créée automatiquement

#### Utilisation

1. **Création automatique** : Lors de la création d'une quittance via le processus en 3 étapes, une recette et une écriture comptable sont générées automatiquement
2. **Création manuelle** : Dans la page de détail d'une quittance, un bouton "Écriture comptable" permet de créer manuellement l'écriture si elle n'existe pas
3. **Gestion des écritures** : Dans la section Comptabilité, vous pouvez :
   - Modifier les écritures comptables (date, montant, commentaire)
   - Supprimer les écritures comptables (supprime aussi la recette associée si elle existe)
4. **Visualisation** : Les recettes et écritures comptables sont visibles dans la section Comptabilité de l'application

#### Endpoints API

- `POST /api/createQuittance` - Crée une quittance et génère automatiquement la recette et l'écriture comptable
- `POST /api/ecritures-comptables/quittance/{quittanceId}` - Crée manuellement une écriture comptable pour une quittance
- `PUT /api/ecritures-comptables` - Modifie une écriture comptable existante
- `DELETE /api/ecritures-comptables/{id}` - Supprime une écriture comptable (et la recette associée si elle existe)

#### Fonctionnalités de gestion

**Modification d'écriture comptable :**
- Date d'écriture
- Montant
- Commentaire
- Type (CHARGE/RECETTE)
- Propriété associée
- Document associé

**Suppression d'écriture comptable :**
- Supprime l'écriture comptable
- Supprime automatiquement la recette associée si elle existe
- Confirmation requise avant suppression

Cette fonctionnalité assure une traçabilité complète des revenus locatifs dans la comptabilité de l'application avec une gestion complète des écritures comptables. 