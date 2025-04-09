<template>
  <div class="fr-card fr-enlarge-link fr-card--horizontal fr-card--horizontal-tier">
    <div class="fr-card__body">
      <div class="fr-card__content">
        <h3 class="fr-card__title">
          <a :href="link">{{ title }}</a>
        </h3>
        <p class="fr-card__desc">{{ description }}</p>
        <div class="fr-card__start">
          <ul class="fr-tags-group">
            <li v-for="tag in tags" :key="tag">
              <p class="fr-tag">{{ tag }}</p>
            </li>
          </ul>
          <p v-if="startDetail" class="fr-card__detail fr-icon-warning-fill">{{ startDetail }}</p>
        </div>
        <div class="fr-card__end">
          <p v-if="endDetail" class="fr-card__detail fr-icon-warning-fill">{{ endDetail }}</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'DSFRCard',
  props: {
    title: {
      type: String,
      required: true
    },
    description: {
      type: String,
      required: true
    },
    link: {
      type: String,
      required: true
    },
    tags: {
      type: Array,
      default: () => []
    },
    startDetail: {
      type: String,
      default: ''
    },
    endDetail: {
      type: String,
      default: ''
    },
    imageSrc: {
      type: String,
      required: true
    },
    imageAlt: {
      type: String,
      default: ''
    }
  }
};
</script>

<style scoped>
/* Add any scoped styles if necessary */
</style>
