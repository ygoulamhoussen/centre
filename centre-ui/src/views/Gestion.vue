<template>
  <DsfrAccordion title="Gestion locative">

      <DsfrTiles :horizontal="false" :tiles="tiles" />
</DsfrAccordion>
    <DsfrAccordion title="Comptabilité">
      <DsfrTiles :horizontal="false" :tiles="tilesCompta" />
    </DsfrAccordion>

</template>

<script lang="ts">
import { defineComponent } from 'vue'
import svgTile from '@gouvfr/dsfr/dist/artwork/pictograms/document/contract.svg'
import svgTileAlt from '@gouvfr/dsfr/dist/artwork/pictograms/document/driving-licence.svg'

// Assurez-vous d'importer les composants accordion depuis DSFR
import { DsfrAccordion, DsfrTiles } from '@gouvminint/vue-dsfr'

export default defineComponent({
  name: 'Gestion',
  components: {
    DsfrAccordion,
    DsfrTiles
  },
  data() {
    return {
      tiles: [
        {
          title: "Ma formidable tuile 1",
          svgPath: svgTile,
          description: "Une tuile absolument formidable"
        },
        {
          title: "Ma formidable tuile 2",
          svgPath: svgTile,
          description: "Une tuile absolument formidable"
        },
        {
          title: "Ma formidable tuile 3",
          svgPath: svgTile,
          description: "Une tuile absolument formidable"
        },
        {
          title: "Ma formidable tuile 4",
          svgPath: svgTile,
          description: "Une tuile absolument formidable"
        }
      ],
      tilesCompta: [
        {
          title: "Ma formidable tuile 1",
          description: "Une tuile absolument formidable",
          svgPath: svgTileAlt,
        },
        {
          title: "Ma formidable tuile 2",
          svgPath: svgTileAlt,
          description: "Une tuile absolument formidable"
        },
        {
          title: "Ma formidable tuile 3",
          svgPath: svgTileAlt,
          description: "Une tuile absolument formidable"
        },
        {
          title: "Ma formidable tuile 4",
          svgPath: svgTileAlt,
          description: "Une tuile absolument formidable"
        }
      ]
    }
  }
})
</script>