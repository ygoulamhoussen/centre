import { defineStore } from 'pinia'

export const useUnifiedStore = defineStore('unified', {
  state: () => ({
    selectedProprieteId: null as string | null,
    locations: [] as any[],

    proprieteDTO: {
      id: null as string | null,
      nom: null as string | null,
      typeBien: null as string | null,
      adresse: null as string | null,
      complementAdresse: null as string | null,
      codePostal: null as string | null,
      ville: null as string | null,
      dateAcquisition: null as string | null,
      dateLivraison: null as string | null,
      montantAcquisition: null as number | null,
      tantieme: null as string | null,
      fraisNotaire: null as number | null,
      compositions: [] as any[],
      fraisGarantie: null as string | null,
    },
    locataireDTO: {
      id: null as string | null,
      utilisateurId: null as string | null,
      nom: null as string | null,
      telephone: null as string | null,
      email: null as string | null,
      adresse: null as string | null,
      complementAdresse: null as string | null,
      codePostal: null as string | null,
      ville: null as string | null,
    },
    locationDTO: {
      id: '',
      proprieteId: '',
      proprieteNom: '',
      locataireId: '',
      locataireNom: '',
      dateDebut: null as string | null,
      dateFin: null as string | null,
      loyerMensuel: '0',
      chargesMensuelles: '0',
      depotGarantie: '0',
      frequenceLoyer: 'Mensuel',
      jourEcheance: '1',
    },
    quittanceDTO: {
      id: null as string | null,
      locationId: null as string | null,
      dateDebut: null as string | null,
      dateFin: null as string | null,
      dateEmission: null as string | null,
      dateEcheance: null as string | null,
      montantLoyer: '0' as string | null,
      montantCharges: '0' as string | null,
      montantTotal: '0' as string | null,
      statut: 'PAYEE' as string | null,
      inclureCaution: false,
      depotGarantie: '0' as string | null,
      selectedMonth: null as number | null,
      selectedYear: null as number | null,
      selectedQuarter: null as number | null,
    },
    paiementDTO: {
      id: null as string | null,
      quittanceId: null as string | null,
      datePaiement: null as string | null,
      montant: null as string | null,
      moyenPaiement: null as string | null,
      reference: null as string | null,
      commentaire: null as string | null,
      estValide: false as boolean,
    },
    creditDTO: {
      id: null as string | null,
      proprieteId: null as string | null,
      banque: null as string | null,
      montantEmprunte: null as string | null,
      dateDebut: null as string | null,
      dateFin: null as string | null,
      dureeMois: null as string | null,
      tauxInteretAnnuel: null as string | null,
      mensualite: null as string | null,
      assuranceMensuelle: null as string | null,
      fraisDossier: null as string | null,
      fraisGarantie: null as string | null,
    },
  }),

  actions: {
    setLocations(locations: any[]) {
      this.locations = locations
    },
    setSelectedProprieteId(id: string | null) {
      this.selectedProprieteId = id
    },
    updateProprieteField(field: string, value: any) {
      (this.proprieteDTO as any)[field] = value
    },
    resetProprieteDTO() {
      this.proprieteDTO = {
        id: null,
        nom: null,
        typeBien: null,
        adresse: null,
        complementAdresse: null,
        codePostal: null,
        ville: null,
        dateAcquisition: null,
        dateLivraison: null,
        montantAcquisition: null,
        tantieme: null,
        fraisNotaire: null,
        compositions: [],
        fraisGarantie: null,
      }
    },
    updateLocataireField(field: string, value: any) {
      (this.locataireDTO as any)[field] = value
    },
    resetLocataireDTO() {
      Object.assign(this.locataireDTO, this.$state.locataireDTO)
    },
    updateLocationField(field: string, value: any) {
      (this.locationDTO as any)[field] = value
    },
    resetLocationDTO() {
      Object.assign(this.locationDTO, this.$state.locationDTO)
    },
    updateQuittanceField(field: string, value: any) {
      (this.quittanceDTO as any)[field] = value
    },
    resetQuittanceDTO() {
      this.quittanceDTO = {
        id: null,
        locationId: null,
        dateDebut: null,
        dateFin: null,
        dateEmission: null,
        dateEcheance: null,
        montantLoyer: '0',
        montantCharges: '0',
        montantTotal: '0',
        statut: 'IMPAYEE',
        inclureCaution: false,
        depotGarantie: '0',
        selectedMonth: null,
        selectedYear: null,
        selectedQuarter: null,
      }
    },
    updatePaiementField(field: string, value: any) {
      (this.paiementDTO as any)[field] = value
    },
    resetPaiementDTO() {
      Object.assign(this.paiementDTO, this.$state.paiementDTO)
    },
    updateCreditField(field: string, value: any) {
      (this.creditDTO as any)[field] = value
    },
    resetCreditDTO() {
      Object.assign(this.creditDTO, this.$state.creditDTO)
    },
    async getAmortissement(proprieteId: string, categorie: string = '') {
      let url = `${import.meta.env.VITE_SERVICE_BASE_URL}/api/genererAmortissement/${proprieteId}`
      if (categorie) {
        url += `?categorie=${encodeURIComponent(categorie)}`
      }
      const res = await fetch(url, {
        credentials: 'include',
      })
      if (!res.ok) {
        throw new Error('Erreur lors de la génération du plan')
      }
      return res.json()
    },
  },
})
