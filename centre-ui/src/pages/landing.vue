<script setup lang="ts">
import { Icon } from '@iconify/vue'
import { useAuthStore } from '@/store/modules/auth'
import { computed, ref } from 'vue'

definePage({
  meta: {
    constant: true,
    hideInMenu: true,
    layout: 'blank',
  },
})

const email = ref('')

const kpis = [
  { k: 'Biens', v: '3' },
  { k: 'Occupation', v: '96%' },
  { k: 'Impayés', v: '0' },
  { k: 'Loyers', v: '2 150€' },
]

const files = ['EDF_2025-02.pdf', 'Facture_peinture.jpg', 'Relevé_Banque_Mars.csv']

const partners = ['Finances Publiques', 'Atrium', 'Time2Start', 'Akoneo']

const features = [
  { icon: 'mdi:file-document-outline', title: 'Liasse fiscale 2033/2031', desc: 'Générez vos formulaires et exports FEC prêts à télétransmettre.' },
  { icon: 'mdi:trending-up', title: 'Suivi des loyers & impayés', desc: 'Encaissements, impayés, taux d\'occupation, alertes.' },
  { icon: 'mdi:tray-arrow-up', title: 'Import comptable intelligent', desc: 'Import banque & pièces, classification assistée.' },
  { icon: 'mdi:shield-check', title: 'Conformité & sécurité', desc: 'RGPD, sauvegardes chiffrées, piste d\'audit.' },
]

const steps = [
  { t: 'Connectez vos comptes et ajoutez vos biens', d: 'Banques, documents, locataires — consolidation automatique.' },
  { t: 'Suivez vos encaissements', d: 'Loyers, charges, impayés. Quittances en un clic.' },
  { t: 'Générez la liasse', d: 'Pré‑remplie avec vos données. Contrôles et export FEC inclus.' },
]

const social = [
  { k: '+200 h', v: 'économisées / an' },
  { k: '97%', v: 'clients satisfaits' },
  { k: '48h', v: 'pour être opérationnel' },
]

const plans = [
  {
    name: 'Autonome',
    price: '9,99€',
    period: '/mois HT',
    tagline: 'Gérez votre bien facilement',
    features: [
      'Locataires illimités',
      'Quittances illimitées',
      'Locations illimitées',
      'Archivage des documents',
      'Catégories personnalisées',
      'Accès au bilan comptable',
      'Génération de la liasse fiscale',
      'Envoi de la liasse fiscale',
    ],
    cta: 'Essai gratuit 30 jours',
    highlighted: true,
  },
  {
    name: 'Supervisé',
    price: '19,99€',
    period: '/mois HT',
    tagline: 'Accompagnement par un expert‑comptable',
    features: [
      'Tout le plan Autonome',
      'Révision par un expert‑comptable',
      'Attestations',
      'Support prioritaire',
    ],
    cta: 'Arrive prochainement',
    highlighted: false,
  },
]

const testimonials = [
  {
    name: 'Nathan',
    role: 'Gérant d\'une SCI',
    text:
      'Outil parfait pour gérer ses propriétés sans tracas, intuitif et complet pour la gestion locative et la comptabilité.',
  },
  {
    name: 'Adil',
    role: 'Propriétaire d\'un bien en LMNP',
    text:
      'Interface intuitive et comptabilité intégrée qui simplifie vraiment la gestion financière. Je recommande vivement.',
  },
  {
    name: 'Rywan',
    role: 'Propriétaire en SCI et LMNP',
    text:
      'Ils ajoutent des fonctionnalités rapidement, l\'outil reste simple et efficace pour gérer plusieurs statuts.',
  },
]

const faq = [
  { q: 'Zymo convient‑il si je déclare au réel ?', a: 'Oui. Conçu en priorité pour le LMNP au réel (2033/2031), avec contrôles et annexes.' },
  { q: 'Puis‑je importer mes relevés bancaires ?', a: 'Oui, via CSV/OFX et synchronisations, avec propositions automatiques (règles + IA).' },
  { q: 'Où sont hébergées mes données ?', a: 'En Union Européenne, chiffrées au repos et en transit. Sauvegardes quotidiennes.' },
  { q: 'Proposez‑vous une API ?', a: 'Oui sur les plans Entreprise : intégrations, SSO, webhooks sur demande.' },
]

const trial = ref({ email: '', units: 3, city: '' })

const themeOverrides = {
  common: {
    primaryColor: '#6d28d9',
    primaryColorHover: '#7c3aed',
    primaryColorPressed: '#5b21b6',
    successColor: '#16a34a',
    warningColor: '#f59e0b',
    infoColor: '#06b6d4',
  },
  Button: {
    borderRadius: '14px',
    heightLarge: '44px',
  },
  Card: {
    borderRadius: '16px',
  },
} as const

const authStore = useAuthStore()
const isLogin = computed(() => authStore.isLogin)
const routeHome = import.meta.env.VITE_ROUTE_HOME || '/home'

function logout() {
  authStore.resetStore()
}
</script>

<template>
  <n-config-provider :theme-overrides="themeOverrides">
    <n-layout class="min-h-screen">
      <!-- Header -->
      <n-layout-header bordered class="backdrop-blur header">
        <div class="container row between center">
          <div class="row center gap-sm brand">
            <div class="logo">
              <n-icon size="18">
                <Icon icon="mdi:sparkles" />
              </n-icon>
            </div>
            <div class="brand-text">
              <strong>Zymo</strong>
              <span class="muted">LMNP, sans friction</span>
            </div>
          </div>
          <nav class="nav">
            <a href="#features">Fonctionnalités</a>
            <a href="#how">Comment ça marche</a>
            <a href="#pricing">Tarifs</a>
            <a href="#faq">FAQ</a>
          </nav>
          <div class="row gap-xs">
            <template v-if="!isLogin">
              <RouterLink to="/login">
                <n-button quaternary>Connexion</n-button>
              </RouterLink>
              <RouterLink :to="{ name: '/login', params: { module: 'register' } }">
                <n-button type="warning">S'inscrire</n-button>
              </RouterLink>
            </template>
            <template v-else>
              <a :href="routeHome" target="_blank" rel="noopener noreferrer">
                <n-button type="primary">Aller au tableau de bord</n-button>
              </a>
              <n-button secondary @click="logout">Se déconnecter</n-button>
            </template>
          </div>
        </div>
      </n-layout-header>

      <!-- Hero -->
      <section class="hero">
        <div class="container grid hero-grid">
          <div>
            <n-tag type="success" round size="small" class="mb-2">Nouveau — Spécial LMNP au réel</n-tag>
            <h1 class="hero-title">
              La gestion locative et comptable simplifiée des investisseurs en <span class="primary">SCI</span> et en <span class="primary">LMNP</span>
            </h1>
            <p class="lead">
              Gérez vos biens en toute sérénité : quittances, locataires, transactions, immobilisations et liasse fiscale au même endroit.
            </p>
            <n-form v-if="!isLogin" class="row gap-xs mt-3" @submit.prevent>
              <n-input v-model:value="email" type="text" inputmode="email" placeholder="Votre e‑mail" class="flex-1" />
              <n-button type="primary" size="large">Demander un accès</n-button>
            </n-form>
            <div v-else class="row gap-xs mt-3">
              <a :href="routeHome" target="_blank" rel="noopener noreferrer">
                <n-button type="primary" size="large">Ouvrir mon espace</n-button>
              </a>
              <n-button secondary size="large" @click="logout">Se déconnecter</n-button>
            </div>
            <div class="muted sm mt-1 row center gap-xxs">
              <n-icon size="16"><Icon icon="mdi:shield-check" /></n-icon>
              RGPD • Données hébergées en UE • Annulation à tout moment
            </div>
          </div>

          <n-card size="small" class="mockup" :bordered="true">
            <n-grid cols="12" x-gap="12" y-gap="12">
              <n-gi :span="12" :offset="0" class="grid md7">
                <n-card size="small">
                  <div class="row between center mb-1">
                    <span class="sm"><strong>Loyers février</strong></span>
                    <n-tag type="success" size="small">+6,2%</n-tag>
                  </div>
                  <div class="chart" aria-hidden></div>
                </n-card>
                <div class="grid two mt-1">
                  <n-card size="small" v-for="kv in kpis" :key="kv.k">
                    <div class="muted xs">{{ kv.k }}</div>
                    <div class="kpi">{{ kv.v }}</div>
                  </n-card>
                </div>
              </n-gi>
              <n-gi :span="12" class="md5">
                <n-card size="small" class="mb-1">
                  <div class="row center gap-xxs mb-1">
                    <n-icon size="16"><Icon icon="mdi:office-building" /></n-icon>
                    <span class="sm"><strong>Quittance #Q-0245</strong></span>
                  </div>
                  <div class="pdf" aria-hidden></div>
                  <n-button block secondary class="mt-1">Télécharger le PDF</n-button>
                </n-card>
                <n-card size="small">
                  <div class="row center gap-xxs mb-1">
                    <n-icon size="16"><Icon icon="mdi:email-outline" /></n-icon>
                    <span class="sm"><strong>Pièces à classer</strong></span>
                  </div>
                  <n-space vertical size="small">
                    <div class="row between center file" v-for="f in files" :key="f">
                      <span class="truncate">{{ f }}</span>
                      <n-tag size="small" round>OCR</n-tag>
                    </div>
                  </n-space>
                </n-card>
              </n-gi>
            </n-grid>
          </n-card>
        </div>
      </section>

      <!-- Partners -->
      <section class="partners">
        <div class="container center">
          <div class="muted xs mb-2">Des partenaires de confiance</div>
          <div class="partners-row">
            <span class="partner" v-for="p in partners" :key="p">{{ p }}</span>
          </div>
        </div>
      </section>

      <!-- Features -->
      <section id="features" class="section">
        <div class="container">
          <div class="center text">
            <h2>Tout pour le <span class="primary">LMNP</span>, au même endroit</h2>
            <p class="muted">Gagnez des heures chaque mois grâce à des workflows pensés pour les propriétaires non professionnels.</p>
          </div>
          <n-grid cols="1 s:2 m:4" x-gap="16" y-gap="16" class="mt-2">
            <n-gi v-for="f in features" :key="f.title">
              <n-card :title="f.title" :bordered="true">
                <template #header-extra>
                  <n-icon size="20" class="primary"><Icon :icon="f.icon" /></n-icon>
                </template>
                <div class="muted">{{ f.desc }}</div>
              </n-card>
            </n-gi>
          </n-grid>
        </div>
      </section>

      <!-- How it works -->
      <section id="how" class="section">
        <div class="container grid how-grid">
          <div>
            <h2>Comment ça marche ?</h2>
            <ol class="steps">
              <li v-for="(s, i) in steps" :key="s.t" class="row gap-sm">
                <div class="bubble">{{ i + 1 }}</div>
                <div>
                  <div class="step-title">{{ s.t }}</div>
                  <div class="muted">{{ s.d }}</div>
                </div>
              </li>
            </ol>
            <div class="muted row center gap-xxs mt-1">
              <n-icon size="16"><Icon icon="mdi:shield-check" /></n-icon>
              Hébergement UE • Chiffrement au repos • Sauvegardes quotidiennes
            </div>
          </div>
          <n-card :bordered="true">
            <template #header>
              <strong>Essayez avec vos données</strong>
            </template>
            <n-space vertical>
              <n-input v-model:value="trial.email" placeholder="Adresse e‑mail" type="text" inputmode="email" />
              <n-input-number v-model:value="trial.units" placeholder="Nombre de biens" :min="0" />
              <n-input v-model:value="trial.city" placeholder="Ville (optionnel)" />
              <div class="row gap-xs">
                <n-button type="primary">Créer mon compte</n-button>
                <n-button secondary>Voir une démo</n-button>
              </div>
            </n-space>
          </n-card>
        </div>
      </section>

      <!-- Social proof -->
      <section class="proof">
        <div class="container">
          <n-grid cols="1 s:3" x-gap="16" y-gap="16">
            <n-gi v-for="sp in social" :key="sp.k">
              <n-card>
                <div class="center">
                  <div class="big">{{ sp.k }}</div>
                  <div class="muted xs">{{ sp.v }}</div>
                </div>
              </n-card>
            </n-gi>
          </n-grid>
        </div>
      </section>

      <!-- Pricing -->
      <section id="pricing" class="section">
        <div class="container">
          <div class="center text">
            <h2>Des tarifs simples et transparents</h2>
            <p class="muted">Sans engagement. Essai gratuit de 30 jours sur le plan Autonome.</p>
          </div>
          <n-grid cols="1 m:3" x-gap="16" y-gap="16" class="mt-2">
            <n-gi v-for="p in plans" :key="p.name">
              <n-card :class="[{ highlight: p.highlighted }]" :bordered="true">
                <template #header>
                  <div class="row between center">
                    <strong>{{ p.name }}</strong>
                    <div class="price">
                      {{ p.price }}<span class="muted">{{ p.period }}</span>
                    </div>
                  </div>
                  <div class="muted xs">{{ p.tagline }}</div>
                </template>
                <n-space vertical size="small">
                  <div class="row gap-xxs" v-for="f in p.features" :key="f">
                    <n-icon size="16" class="primary"><Icon icon="mdi:check" /></n-icon>
                    <span class="sm">{{ f }}</span>
                  </div>
                </n-space>
                <template #action>
                  <n-button type="primary" block>{{ p.cta }}</n-button>
                </template>
              </n-card>
            </n-gi>
          </n-grid>
        </div>
      </section>

      <!-- Testimonials -->
      <section class="section">
        <div class="container">
          <h2 class="center">Avis des utilisateurs</h2>
          <n-grid cols="1 m:3" x-gap="16" y-gap="16" class="mt-2">
            <n-gi v-for="t in testimonials" :key="t.name">
              <n-card :title="t.name">
                <div class="xs muted">{{ t.role }}</div>
                <p class="sm mt-1">{{ t.text }}</p>
              </n-card>
            </n-gi>
          </n-grid>
        </div>
      </section>

      <!-- FAQ -->
      <section id="faq" class="section">
        <div class="container">
          <h2 class="center">Questions fréquentes</h2>
          <n-grid cols="1 m:2" x-gap="16" y-gap="16" class="mt-2">
            <n-gi v-for="f in faq" :key="f.q">
              <n-card :title="f.q">
                <div class="muted">{{ f.a }}</div>
              </n-card>
            </n-gi>
          </n-grid>
        </div>
      </section>

      <!-- CTA -->
      <section class="section">
        <div class="container center">
          <h2>Prêt à simplifier votre LMNP ?</h2>
          <p class="muted">Créez un compte en 2 minutes et commencez votre essai gratuit.</p>
          <div class="row gap-xs mt-1 center">
            <RouterLink to="/login"><n-button type="primary" size="large">Commencer gratuitement</n-button></RouterLink>
            <RouterLink to="/pdf/html"><n-button secondary size="large">Voir un exemple de liasse</n-button></RouterLink>
          </div>
        </div>
      </section>

      <!-- Footer -->
      <footer class="footer">
        <div class="container grid footer-grid">
          <div>
            <div class="row center gap-xs mb-1">
              <div class="logo small">
                <n-icon size="16"><Icon icon="mdi:sparkles" /></n-icon>
              </div>
              <strong>Zymo</strong>
            </div>
            <div class="muted">Le copilote des propriétaires en LMNP.</div>
          </div>
          <div>
            <div class="semi mb-1">Produit</div>
            <ul class="links">
              <li><a href="#features">Fonctionnalités</a></li>
              <li><a href="#pricing">Tarifs</a></li>
              <li><a href="#faq">FAQ</a></li>
            </ul>
          </div>
          <div>
            <div class="semi mb-1">Ressources</div>
            <ul class="links">
              <li><a href="#">Centre d'aide</a></li>
              <li><a href="#">Statut & sécurité</a></li>
              <li><a href="#">API & intégrations</a></li>
            </ul>
          </div>
          <div>
            <div class="semi mb-1">Légal</div>
            <ul class="links">
              <li><a href="#">Mentions légales</a></li>
              <li><a href="#">Confidentialité</a></li>
              <li><a href="#">Conditions</a></li>
            </ul>
          </div>
        </div>
        <div class="subfooter">
          <div class="container row between center wrap">
            <span>© {{ new Date().getFullYear() }} Zymo. Tous droits réservés.</span>
            <span>Fait avec ❤️ en France</span>
          </div>
        </div>
      </footer>
    </n-layout>
  </n-config-provider>
</template>

<style scoped>
/* Layout helpers */
.container { max-width: 1120px; margin: 0 auto; padding: 0 16px; }
.row { display: flex; gap: 12px; }
.row.center { align-items: center; }
.row.between { justify-content: space-between; }
.row.wrap { flex-wrap: wrap; }
.grid { display: grid; gap: 16px; }
.grid.two { grid-template-columns: repeat(2, minmax(0,1fr)); }
.center { text-align: center; }
.text { max-width: 720px; margin: 0 auto; }

/* Header */
.header {
  position: sticky;
  top: 0;
  z-index: 50;
  backdrop-filter: blur(8px);
  background: linear-gradient(180deg, rgba(255,255,255,.85), rgba(255,255,255,.6));
  border-bottom: 1px solid rgba(15,23,42,.06);
}
.logo { height: 36px; width: 36px; display: grid; place-items: center; border-radius: 12px; background: rgba(79,70,229,0.12); }
.logo.small { height: 28px; width: 28px; }
.brand-text .muted { margin-left: 8px; font-size: 12px; opacity: .65; }
.nav { display: none; gap: 20px; }
.nav a { color: inherit; opacity: .8; text-decoration: none; }
.nav a:hover { color: #6d28d9; opacity: 1; }
@media (min-width: 768px) { .nav { display: flex; } }

/* Hero */
.hero {
  padding: 64px 0;
  background:
    radial-gradient(1200px 600px at 20% 0%, rgba(109,40,217,.14), transparent 60%),
    radial-gradient(1200px 600px at 80% 10%, rgba(6,182,212,.14), transparent 60%),
    linear-gradient(180deg,#f6f7ff 0%, #fff 60%);
}
.hero-grid { grid-template-columns: 1fr; align-items: center; }
@media (min-width: 900px) { .hero-grid { grid-template-columns: 1.1fr .9fr; } }
.hero-title { font-weight: 800; font-size: clamp(28px, 6vw, 44px); line-height: 1.05; }
.lead { margin-top: 12px; font-size: 18px; opacity: .8; }
.primary {
  background: linear-gradient(45deg, #6d28d9, #06b6d4);
  -webkit-background-clip: text;
  background-clip: text;
  color: transparent;
}
.mockup { border-style: solid; border-color: rgba(109,40,217,.18); box-shadow: 0 10px 30px rgba(17,24,39,.08); }
.chart { height: 120px; border-radius: 8px; background: repeating-linear-gradient(90deg, #eef2ff, #eef2ff 8px, #fff 8px, #fff 16px); }
.kpi { font-weight: 700; font-size: 18px; }
.pdf { height: 110px; border-radius: 8px; background: linear-gradient(135deg, #fafafa, #f1f5f9); border: 1px dashed rgba(0,0,0,.08); }
.file .truncate { max-width: 70%; white-space: nowrap; overflow: hidden; text-overflow: ellipsis; }

/* Sections */
.section { padding: 56px 0; }
.steps { margin: 16px 0 0; padding: 0; list-style: none; display: grid; gap: 14px; }
.bubble { height: 28px; width: 28px; border-radius: 999px; background: #6d28d9; color: #fff; display: grid; place-items: center; font-weight: 700; }
.step-title { font-weight: 600; }

/* Proof */
.proof { background: #f6f7fb; padding: 40px 0; border-top: 1px solid rgba(0,0,0,.06); border-bottom: 1px solid rgba(0,0,0,.06); }
.big { font-size: 30px; font-weight: 800; }

/* Pricing */
.price { font-weight: 800; font-size: 20px; }
.price .muted { margin-left: 4px; font-weight: 500; opacity: .6; }
.highlight { box-shadow: 0 0 0 2px #6d28d9 inset; border-radius: 16px; }

/* Footer */
.footer { border-top: 1px solid rgba(0,0,0,.08); padding-top: 28px; }
.footer-grid { grid-template-columns: repeat(1, minmax(0,1fr)); }
@media (min-width: 900px) { .footer-grid { grid-template-columns: 2fr 1fr 1fr 1fr; } }
.links { margin: 0; padding: 0; list-style: none; display: grid; gap: 8px; }
.semi { font-weight: 600; }
.subfooter { border-top: 1px solid rgba(0,0,0,.08); margin-top: 16px; font-size: 12px; opacity: .7; }

/* Utils */
.mb-1 { margin-bottom: 8px; }
.mb-2 { margin-bottom: 12px; }
.mt-1 { margin-top: 8px; }
.mt-2 { margin-top: 16px; }
.mt-3 { margin-top: 20px; }
.gap-xxs { gap: 6px; }
.gap-xs { gap: 10px; }
.gap-sm { gap: 14px; }
.sm { font-size: 14px; }
.xs { font-size: 12px; }
.muted { opacity: .72; }

/* Responsive helpers for mockup grid */
.md7 { grid-column: span 12; }
.md5 { grid-column: span 12; }
@media (min-width: 900px) {
  .md7 { grid-column: span 7; }
  .md5 { grid-column: span 5; }
}
</style>
