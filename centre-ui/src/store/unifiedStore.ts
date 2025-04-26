// src/stores/unifiedStore.ts
import { defineStore } from 'pinia'

export const useUnifiedStore = defineStore('unified', {
  state: () => ({
    proprieteDTO: {
      id: '',
      nom: '',
      typeBien: '',
      adresse: '',
      complementAdresse: '',
      codePostal: '',
      ville: '',
      dateAcquisition: null,
      dateLivraison: null,
      montantAcquisition: '',
      tantieme: '',
      fraisNotaire: '',
      fraisAgence: '',
      compositions: []
    },
    locataireDTO: {
      id: '',
      utilisateurId: '',
      nom: '',
      telephone: '',
      email: '',
      adresse: '',
      complementAdresse: '',
      codePostal: '',
      ville: ''
    },
    locationDTO: {
      id: '',
      proprieteId: '',
      locataireId: '',
      dateDebut: '',
      dateFin: '',
      loyerMensuel: '',
      chargesMensuelles: '',
      depotGarantie: '',
      frequenceLoyer: '',
      jourEcheance: ''
    },
    quittanceDTO: {
      id: '',
      locationId: '',
      dateDebut: '',
      dateFin: '',
      dateEmission: '',
      montantLoyer: '',
      montantCharges: '',
      montantTotal: '',
      statut: ''
    },
    paiementDTO: {
      id: '',
      quittanceId: '',
      datePaiement: '',
      montant: '',
      moyenPaiement: '',
      reference: '',
      commentaire: '',
      estValide: false
    },
    creditDTO: {
      id: '',
      proprieteId: '',
      banque: '',
      montantEmprunte: '',
      dateDebut: '',
      dateFin: '',
      dureeMois: '',
      tauxInteretAnnuel: '',
      mensualite: '',
      assuranceMensuelle: '',
      fraisDossier: '',
      fraisGarantie: ''
    }
  }),

  actions: {
    updateProprieteField (field: string, value: any) {
      this.proprieteDTO[field] = value
    },
    resetProprieteDTO () {
      Object.assign(this.proprieteDTO, this.$state.proprieteDTO)
    },
    updateLocataireField (field: string, value: any) {
      this.locataireDTO[field] = value
    },
    resetLocataireDTO () {
      Object.assign(this.locataireDTO, this.$state.locataireDTO)
    },
    updateLocationField (field: string, value: any) {
      this.locationDTO[field] = value
    },
    resetLocationDTO () {
      Object.assign(this.locationDTO, this.$state.locationDTO)
    },
    updateQuittanceField (field: string, value: any) {
      this.quittanceDTO[field] = value
    },
    resetQuittanceDTO () {
      Object.assign(this.quittanceDTO, this.$state.quittanceDTO)
    },
    updatePaiementField (field: string, value: any) {
      this.paiementDTO[field] = value
    },
    resetPaiementDTO () {
      Object.assign(this.paiementDTO, this.$state.paiementDTO)
    },
    updateCreditField (field: string, value: any) {
      this.creditDTO[field] = value
    },
    resetCreditDTO () {
      Object.assign(this.creditDTO, this.$state.creditDTO)
    }
  }
})
