<template>
    
  <nav role="navigation" class="fr-breadcrumb" aria-label="vous êtes ici :">
    <button class="fr-breadcrumb__button" aria-expanded="false" aria-controls="breadcrumb-1">Voir le fil d’Ariane</button>
    <div class="fr-collapse" id="breadcrumb-1">
      <ol class="fr-breadcrumb__list">
        <li v-for="(segment, index) in segments" :key="index">
          <a v-if="index < segments.length - 1" :href="segment.link" class="fr-breadcrumb__link">{{ segment.name }}</a>
          <a v-else class="fr-breadcrumb__link" aria-current="page">{{ segment.name }}</a>
        </li>
      </ol>
    </div>
  </nav>
</template>

<script>
export default {
  name: 'Breadcrumb',
  props: {
    segments: {
      type: Array,
      required: true,
      default: () => [
        { name: 'Accueil', link: '#/' },
        { name: 'Segment 1', link: '#/segment-1/' },
        { name: 'Segment 2', link: '#/segment-1/segment-2/' },
        { name: 'Segment 3', link: '#/segment-1/segment-2/segment-3/' },
        { name: 'Segment 4', link: '#/segment-1/segment-2/segment-3/segment-4/' },
        { name: 'Segment 5', link: '#/segment-1/segment-2/segment-3/segment-4/segment-5/' },
        { name: 'Page Actuelle', link: '' }
      ]
    }
  }
};
</script>

<style scoped>
/* Add any scoped styles if necessary */
</style>
