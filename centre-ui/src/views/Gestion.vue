<template>
  <DsfrAccordionsGroup v-model="activeAccordion">
    <DsfrAccordion id="accordion-gestion" :title="titleGestion">
      <DsfrTiles :horizontal="false" :tiles="tiles" />
    </DsfrAccordion>
    <DsfrAccordion id="accordion-compta" :title="titleCompta">
      <DsfrTiles :horizontal="false" :tiles="tilesCompta" />
    </DsfrAccordion>
  </DsfrAccordionsGroup>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { DsfrAccordion, DsfrAccordionsGroup, DsfrTiles } from '@gouvminint/vue-dsfr'
import svgGestion from '@gouvfr/dsfr/dist/artwork/pictograms/document/driving-licence.svg'
import svgCompta from '@gouvfr/dsfr/dist/artwork/pictograms/document/driving-licence.svg'

export default defineComponent({
  name: 'Gestion',
  components: {
    DsfrAccordion,
    DsfrAccordionsGroup,
    DsfrTiles
  },
  data() {
    return {
      activeAccordion: 0,
      titleGestion: 'Gestion locative',
      titleCompta: 'Comptabilité',
      tiles: [
        {
          to :"ProprieteView",
          title: "Propriétés",
          svgPath: svgGestion,
          description: "Ajoutez et gérez vos biens immobiliers"
        },
        {
          title: "Locataires",
          svgPath: svgGestion,
          description: "Suivez les informations de vos locataires"
        },
        {
          title: "Locations",
          svgPath: svgGestion,
          description: "Gérez les baux, loyers et échéances"
        },
        {
          title: "Quittances",
          svgPath: svgGestion,
          description: "Créez et suivez les quittances de loyer"
        }
      ],
      tilesCompta: [
        {
          title: "Immobilisations",
          svgPath: svgCompta,
          description: "Décomposez et amortissez vos biens"
        },
        {
          title: "Crédits immobiliers",
          svgPath: svgCompta,
          description: "Suivez vos emprunts et échéanciers"
        },
        {
          title: "Comptabilité",
          svgPath: svgCompta,
          description: "Journal, écritures et clôture de l’exercice"
        },
        {
          title: "Documents",
          svgPath: svgCompta,
          description: "Stockez et classez vos justificatifs"
        }
      ]
    }
  }
})
</script>