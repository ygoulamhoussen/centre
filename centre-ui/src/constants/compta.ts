// Types de charges déductibles LMNP au régime réel
export const naturesCharges = [
  // 🏠 1. Charges de propriété
  {
    label: 'Assurance PNO / loyers',
    value: 'ASSURANCE_PNO',
    compte: '616000',
    description: 'Assurance habitation propriétaire non occupant',
    categorie: 'PROPRIETE',
  },
  {
    label: 'Entretien et réparations',
    value: 'ENTRETIEN_REPARATION',
    compte: '615000',
    description: 'Plomberie, peinture, électricité, etc.',
    categorie: 'PROPRIETE',
  },
  {
    label: 'Charges de copropriété',
    value: 'COPROPRIETE',
    compte: '614000',
    description: 'Quote-part propriétaire (hors travaux non déductibles)',
    categorie: 'PROPRIETE',
  },
  {
    label: 'Taxe foncière',
    value: 'TAXE_FONCIERE',
    compte: '635100',
    description: 'Impôt local déductible sauf TEOM récupérable',
    categorie: 'PROPRIETE',
  },

  // 🧾 2. Charges de gestion
  {
    label: 'Honoraires de gestion locative',
    value: 'GESTION_LOCATIVE',
    compte: '622000',
    description: 'Agence, conciergerie, gestion',
    categorie: 'GESTION',
  },
  {
    label: 'Frais d\'expert-comptable',
    value: 'EXPERT_COMPTABLE',
    compte: '622600',
    description: 'Si recours à un pro',
    categorie: 'GESTION',
  },
  {
    label: 'Abonnement logiciel LMNP',
    value: 'LOGICIEL_LMNP',
    compte: '623100',
    description: 'Si payé pour la gestion locative/comptable',
    categorie: 'GESTION',
  },
  {
    label: 'Frais postaux, téléphone',
    value: 'FRAIS_COMMUNICATION',
    compte: '626000',
    description: 'Liés à l\'activité de location',
    categorie: 'GESTION',
  },
  {
    label: 'Fournitures de bureau',
    value: 'FOURNITURES',
    compte: '606300',
    description: 'Papier, classeurs, etc.',
    categorie: 'GESTION',
  },

  // 💸 3. Charges financières
  {
    label: 'Intérêts d\'emprunt',
    value: 'INTERETS_EMPRUNT',
    compte: '661100',
    description: 'Seule la part intérêt est déductible',
    categorie: 'FINANCIER',
  },
  {
    label: 'Assurance emprunteur',
    value: 'ASSURANCE_EMPRUNTEUR',
    compte: '616100',
    description: 'Si distincte du crédit',
    categorie: 'FINANCIER',
  },
  {
    label: 'Frais de dossier ou garantie',
    value: 'FRAIS_DOSSIER',
    compte: '627000',
    description: 'Frais de mise en place du crédit',
    categorie: 'FINANCIER',
  },

  // 🧾 4. Autres charges spécifiques
  {
    label: 'Publicité / annonces',
    value: 'PUBLICITE',
    compte: '623000',
    description: 'Diffusion sur Leboncoin, SeLoger…',
    categorie: 'AUTRES',
  },
  {
    label: 'Frais de déplacement',
    value: 'DEPLACEMENT',
    compte: '625100',
    description: 'Si visites, assemblée copro, travaux',
    categorie: 'AUTRES',
  },
  {
    label: 'Cotisation CFE',
    value: 'CFE',
    compte: '635800',
    description: 'Impôt local obligatoire en LMNP réel',
    categorie: 'AUTRES',
  },

  // 🔧 5. Charges d'exploitation
  {
    label: 'Électricité',
    value: 'ELECTRICITE',
    compte: '606100',
    description: 'Consommation électrique',
    categorie: 'EXPLOITATION',
  },
  {
    label: 'Eau',
    value: 'EAU',
    compte: '606200',
    description: 'Consommation d\'eau',
    categorie: 'EXPLOITATION',
  },
  {
    label: 'Chauffage',
    value: 'CHAUFFAGE',
    compte: '606400',
    description: 'Consommation de chauffage',
    categorie: 'EXPLOITATION',
  },

  // 📋 6. Autres (pour flexibilité)
  {
    label: 'Autres charges',
    value: 'AUTRES',
    compte: '606800',
    description: 'Autres charges non classées',
    categorie: 'AUTRES',
  },
]

// Catégories de charges pour le regroupement
export const categoriesCharges = [
  { value: 'PROPRIETE', label: '🏠 Charges de propriété' },
  { value: 'GESTION', label: '🧾 Charges de gestion' },
  { value: 'FINANCIER', label: '💸 Charges financières' },
  { value: 'EXPLOITATION', label: '🔧 Charges d\'exploitation' },
  { value: 'AUTRES', label: '📋 Autres charges' },
]

// Types de recettes LMNP
export const typesRecettes = [
  { label: 'Loyer encaissé', value: 'LOYER' },
  { label: 'Remboursement de charges', value: 'REMBOURSEMENT_CHARGES' },
  { label: 'Régularisation de charges', value: 'REGULARISATION_CHARGES' },
  { label: 'Dépôt de garantie encaissé', value: 'DEPOT_GARANTIE' },
  { label: 'Aide / Subvention', value: 'AIDE_SUBVENTION' },
  { label: 'Autre produit exceptionnel', value: 'AUTRE' },
]

// Fonction utilitaire pour obtenir les charges par catégorie
export function getChargesByCategorie(categorie: string) {
  return naturesCharges.filter(charge => charge.categorie === categorie)
}

// Fonction utilitaire pour obtenir une charge par sa valeur
export function getChargeByValue(value: string) {
  return naturesCharges.find(charge => charge.value === value)
}

// Fonction utilitaire pour obtenir le compte comptable d'une charge
export function getCompteCharge(value: string): string {
  const charge = getChargeByValue(value)
  return charge?.compte || '606800' // Compte par défaut si non trouvé
}