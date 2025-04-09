<template>
  <DsfrHeader
    service-title="Tomappart"
    service-description="Gestion locative et comptable simplifiée"
    :navigation="navigation"
  />
</template>

<script>
import { DsfrHeader } from '@gouvfr/dsfr';

export default {
  components: {
    DsfrHeader,
  },
  data() {
    return {
      navigation: [
        { text: 'Accueil', to: '/' },
        { text: 'Fonctionnalités', to: '/fonctionnalites' },
        { text: 'Tarifs', to: '/tarifs' },
        { text: 'Contact', to: '/contact' },
        { text: 'Connexion', to: '/connexion' },
      ],
    };
  },
};
</script>

<style scoped>
/* Ajoutez ici les styles spécifiques si nécessaire */
</style>
