<script lang="ts">
import { defineComponent } from 'vue'
import useToaster from '@/composables/use-toaster'
import svgContract from '@gouvfr/dsfr/dist/artwork/pictograms/document/contract.svg'
import svgDocument from '@gouvfr/dsfr/dist/artwork/pictograms/document/driving-licence.svg'

export default defineComponent({
  name: 'AppHome',
  data() {
    return {
      offers: [
        {
          id: 'tile-autonome',
          title: 'Autonome',
          description: 'À partir de 9,99€ /mois HT',
          details:
            'Locataires illimités, quittances illimitées, locations illimitées, archivage des documents, catégories personnalisées, accès aux immobilisations, accès au bilan comptable, génération de la liasse fiscale, envoi de la liasse fiscale au centre des impôts.',
          svgPath: svgContract,
          to: '#essai-gratuit',
        },
        {
          id: 'tile-supervise',
          title: 'Supervisé',
          description: 'À partir de 19,99€ /mois HT',
          details:
            'Locataires illimités, quittances illimitées, locations illimitées, archivage des documents, catégories personnalisées, accès aux immobilisations, accès au bilan comptable, attestation et révision par un expert-comptable, génération de la liasse fiscale, envoi de la liasse fiscale au centre des impôts.',
          svgPath: svgDocument,
          to: '#essai-gratuit',
        },
      ]
    }
  },
  mounted() {

  },
})
</script>

<template>
  
  <main>
    <!-- Section de présentation -->
    <section class="fr-container fr-mt-10w">
      <h1 class="fr-h1">La gestion locative et comptable simplifiée des investisseurs en SCI et en LMNP</h1>
      <p>
        Gérez vos biens en toute sérénité avec des fonctionnalités adaptées aux experts comme aux débutants.
      </p>
      <div class="fr-grid-row fr-grid-row--center">
        <DsfrButtonGroup inlineLayoutWhen="always">
          <DsfrButton variant="primary" href="#decouvrir">
            Découvrir Tikaz
          </DsfrButton>
          <DsfrButton variant="secondary" href="#video">
            Regarder la vidéo de présentation
          </DsfrButton>
        </DsfrButtonGroup>
      </div>
    </section>

    <!-- Section des partenaires -->
<!--     <section class="fr-container fr-mt-10w">
      <h2 class="fr-h3">Des partenaires de confiance</h2>
      <div class="fr-grid-row">
        <div class="fr-col-12 fr-col-md-3">
           <img src="@/assets/finances_publiques_logo.png" alt="Finances Publiques" class="fr-img--fluid" />
        </div>
        <div class="fr-col-12 fr-col-md-3">
          <img src="@/assets/atrium_logo.png" alt="Atrium" class="fr-img--fluid" />
        </div>
        <div class="fr-col-12 fr-col-md-3">
          <img src="@/assets/time2start_logo.png" alt="Time2Start" class="fr-img--fluid" />
        </div>
        <div class="fr-col-12 fr-col-md-3">
          <img src="@/assets/akoneo_logo.png" alt="Akoneo" class="fr-img--fluid" />
        </div>
      </div>
    </section> -->

    <!-- Section des offres -->
    <section class="fr-container fr-mt-10w">
      <h2 class="fr-h3">Nos offres</h2>
      <div class="fr-grid-row">
        <div
          v-for="offer in offers"
          :key="offer.id"
          class="fr-col-12 fr-col-md-6"
        >
          <DsfrTile
            :title="offer.title"
            :description="offer.description"
            :details="offer.details"
            :svg-path="offer.svgPath"
            :to="offer.to"
          />
        </div>
      </div>
    </section>

    <!-- Section application mobile -->
    <section class="fr-container fr-mt-10w">
      <h2 class="fr-h3">Une application mobile intuitive</h2>
      <p>
        Pour plus d'accessibilité, retrouvez toutes vos fonctionnalités à portée de main en téléchargeant l'application Tikaz disponible sur le Google Play et l'Apple Store.
      </p>
      <div class="fr-grid-row fr-grid-row--center">
        <DsfrButtonGroup inlineLayoutWhen="always">
          <DsfrButton variant="secondary" href="https://play.google.com/store/apps/details?id=fr.tomapp">
            Télécharger sur Google Play
          </DsfrButton>
          <DsfrButton variant="secondary" href="https://apps.apple.com/fr/app/tikaz/id6450775976">
            Télécharger sur l'App Store
          </DsfrButton>
        </DsfrButtonGroup>
      </div>
    </section>
  </main>
</template>

<style scoped>
/* Ajoutez ici les styles spécifiques à votre page d'accueil */
</style>
