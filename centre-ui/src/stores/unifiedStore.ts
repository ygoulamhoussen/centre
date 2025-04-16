// unifiedStore.js
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
      dateAcquisition: '',
      dateLivraison: '',
      montantAcquisition: null,
      tantieme: null,
      fraisNotaire: null,
      fraisAgence: null
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
        dateAcquisition: '',
        dateLivraison: '',
        montantAcquisition: null,
        tantieme: null,
        fraisNotaire: null,
        fraisAgence: null
      }
    },
    updateProprieteField(field, value) {
      this.proprieteDTO[field] = value
    }
  },
  persist: false
})
