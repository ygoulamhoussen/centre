import { defineStore } from 'pinia'

export const useUnifiedStore = defineStore('unified', {
  state: () => ({
    proprieteDTO: {
      typeBien: '',
      nom: '',
      adresse: '',
      complementAdresse: '',
      codePostal: '',
      ville: '',
      dateAcquisition: null,
      dateLivraison: null,
      montantAcquisition: null,
      tantieme: null,
      fraisNotaire: null,
      fraisAgence: null,
      utilisateurId: null,
      compositions: []
    }
  }),
  actions: {
    resetProprieteDTO() {
      this.proprieteDTO = {
        typeBien: '',
        nom: '',
        adresse: '',
        complementAdresse: '',
        codePostal: '',
        ville: '',
        dateAcquisition: null,
        dateLivraison: null,
        montantAcquisition: null,
        tantieme: null,
        fraisNotaire: null,
        fraisAgence: null,
        utilisateurId: null,
        compositions: []
      }
    },
    setTypeBien(type) {
      this.proprieteDTO.typeBien = type
    },
    setAdresse(adresse) {
      this.proprieteDTO = { ...this.proprieteDTO, ...adresse }
    },
    setAcquisition(acquisition) {
      this.proprieteDTO = { ...this.proprieteDTO, ...acquisition }
    },
    addComposition(composition) {
      this.proprieteDTO.compositions.push(composition)
    },
    removeComposition(index) {
      this.proprieteDTO.compositions.splice(index, 1)
    }
  },
  persist: true
})
