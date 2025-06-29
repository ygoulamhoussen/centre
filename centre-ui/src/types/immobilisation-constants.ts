import { TypeImmobilisation, CategorieFiscale } from './immobilisation.d'

export const TYPE_IMMOBILISATION_LABELS: Record<TypeImmobilisation, string> = {
  [TypeImmobilisation.BIEN_IMMOBILIER]: 'Bien immobilier',
  [TypeImmobilisation.MOBILIER]: 'Mobilier',
  [TypeImmobilisation.TRAVAUX]: 'Travaux',
  [TypeImmobilisation.EQUIPEMENT]: 'Équipement',
  [TypeImmobilisation.AUTRE]: 'Autre'
};

export const CATEGORIE_FISCALE_LABELS: Record<CategorieFiscale, string> = {
  [CategorieFiscale.BATIMENT_50_ANS]: 'Bâtiment (50 ans)',
  [CategorieFiscale.BATIMENT_25_ANS]: 'Bâtiment (25 ans)',
  [CategorieFiscale.MOBILIER_10_ANS]: 'Mobilier (10 ans)',
  [CategorieFiscale.MOBILIER_5_ANS]: 'Mobilier (5 ans)',
  [CategorieFiscale.TRAVAUX_10_ANS]: 'Travaux (10 ans)',
  [CategorieFiscale.TRAVAUX_5_ANS]: 'Travaux (5 ans)',
  [CategorieFiscale.EQUIPEMENT_5_ANS]: 'Équipement (5 ans)',
  [CategorieFiscale.EQUIPEMENT_3_ANS]: 'Équipement (3 ans)'
};

export const CATEGORIE_FISCALE_DUREES: Record<CategorieFiscale, number> = {
  [CategorieFiscale.BATIMENT_50_ANS]: 50,
  [CategorieFiscale.BATIMENT_25_ANS]: 25,
  [CategorieFiscale.MOBILIER_10_ANS]: 10,
  [CategorieFiscale.MOBILIER_5_ANS]: 5,
  [CategorieFiscale.TRAVAUX_10_ANS]: 10,
  [CategorieFiscale.TRAVAUX_5_ANS]: 5,
  [CategorieFiscale.EQUIPEMENT_5_ANS]: 5,
  [CategorieFiscale.EQUIPEMENT_3_ANS]: 3
}; 