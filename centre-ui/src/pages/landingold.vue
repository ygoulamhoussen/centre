<script setup lang="ts">
import { computed, ref } from 'vue'
import { Icon } from '@iconify/vue'
import { useAuthStore } from '@/store/modules/auth'

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

// Fonctionnalités clés pour LMNP
const features = [
  { icon: 'mdi:file-export', title: 'Export automatisé des données comptables', desc: 'Exportez vos données vers les formats fiscaux requis en un clic.' },
  { icon: 'mdi:link-variant', title: 'Intégration logiciels comptables', desc: 'Compatible avec les principaux logiciels comptables du marché.' },
  { icon: 'mdi:chart-line', title: 'Interface claire et indicateurs clés', desc: 'Suivez vos loyers, charges et rendement en temps réel.' },
  { icon: 'mdi:infinity', title: 'Propriétés illimitées', desc: 'Aucune limite sur le nombre de propriétés gérées.' },
  { icon: 'mdi:headset', title: 'Support dédié LMNP', desc: 'Assistance réactive spécialisée dans le LMNP.' },
]



// Statistiques sociales
const social = [
  { k: '+15h', v: 'économisées par déclaration' },
  { k: '0', v: 'erreur fiscale avec Zymo' },
  { k: '98%', v: 'de clients satisfaits' },
]

// Tarification simple
const pricing = {
  price: '199€',
  period: '/an TTC',
  features: [
    'Propriétés illimitées',
    'Export données comptables',
    'Intégration logiciels existants',
    'Support spécialisé LMNP',
    'Mises à jour incluses',
    'Conformité fiscale garantie'
  ]
}

// Témoignages clients
const testimonials = [
  {
    name: 'Marie L.',
    role: 'Propriétaire LMNP - 3 biens',
    text: 'Zymo m\'a fait gagner des heures sur ma déclaration fiscale. L\'export est parfait et le support très réactif.',
    avatar: 'ML'
  },
  {
    name: 'Thomas B.',
    role: 'Investisseur LMNP - 7 biens',
    text: 'Enfin un outil qui comprend les spécificités du LMNP ! Plus d\'erreurs dans mes déclarations.',
    avatar: 'TB'
  },
  {
    name: 'Sophie R.',
    role: 'Gérante LMNP - 2 biens',
    text: 'Interface simple et efficace. Je recommande à tous les loueurs en meublé.',
    avatar: 'SR'
  },
]

// FAQ spécialisée LMNP
const faq = [
  { q: 'Zymo fonctionne‑t‑il avec mon logiciel comptable actuel ?', a: 'Oui, Zymo s\'intègre avec les principaux logiciels comptables du marché.' },
  { q: 'L\'export respecte‑t‑il les exigences fiscales LMNP ?', a: 'Absolument. Nos exports sont conformes aux obligations fiscales du régime réel LMNP.' },
  { q: 'Combien de temps pour configurer Zymo ?', a: 'Moins de 30 minutes. Notre équipe vous accompagne dans la configuration initiale.' },
  { q: 'Mes données sont‑elles sécurisées ?', a: 'Vos données sont chiffrées et hébergées en Europe selon les standards RGPD.' },
]

const trial = ref({ email: '', units: 3, city: '' })

// Thème Tomappart - Couleurs Zymo
const themeOverrides = {
  common: {
    primaryColor: '#213B7E',      // Bleu foncé
    primaryColorHover: '#1a2f68',
    primaryColorPressed: '#142654',
    successColor: '#16a34a',
    warningColor: '#F7941D',      // Orange vif
    warningColorHover: '#e67c00',
    warningColorPressed: '#cc6a00',
    infoColor: '#06b6d4',
  },
  Button: {
    borderRadius: '8px',
    heightLarge: '48px',
    fontWeight: '600',
  },
  Card: {
    borderRadius: '12px',
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
            <n-tag type="success" round size="small" class="mb-2">Spécialisé LMNP au régime réel</n-tag>
            <h1 class="hero-title">
              Exportez vos données comptables <span class="primary">LMNP</span> en toute simplicité
            </h1>
            <p class="lead">
              Gagnez du temps et assurez votre conformité fiscale, sans effort.
            </p>
            <div class="row gap-xs mt-3" v-if="!isLogin">
              <n-button type="primary" size="large">Essayer gratuitement</n-button>
              <n-button type="warning" size="large">Demander une démo</n-button>
            </div>
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

      <!-- Problème et Solution -->
      <section class="section problem-solution">
        <div class="container">
          <div class="grid problem-solution-grid">
            <div class="problem">
              <n-icon size="32" class="warning mb-1"><Icon icon="mdi:alert-circle" /></n-icon>
              <h3>Les défis du LMNP au régime réel</h3>
              <p class="muted">
                Liasse fiscale complexe, saisie comptable fastidieuse, risques d'erreurs coûteuses... 
                Les obligations fiscales LMNP peuvent vite devenir un casse-tête.
              </p>
            </div>
            <div class="solution">
              <n-icon size="32" class="primary mb-1"><Icon icon="mdi:check-circle" /></n-icon>
              <h3>Zymo simplifie tout</h3>
              <p class="muted">
                Solution simple, rapide et sécurisée, spécialement conçue pour les loueurs en meublé non professionnels. 
                Export automatisé, conformité garantie.
              </p>
            </div>
          </div>
        </div>
      </section>

      <!-- Features -->
      <section id="features" class="section">
        <div class="container">
          <div class="center text">
            <h2>Fonctionnalités clés pour le <span class="primary">LMNP</span></h2>
            <p class="muted">Tout ce dont vous avez besoin pour gérer vos obligations fiscales en toute sérénité.</p>
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
            <h2>Un tarif simple et transparent</h2>
            <p class="muted">Pas de surprises, pas de frais cachés. Tout inclus pour votre activité LMNP.</p>
          </div>
          <div class="pricing-card mt-2">
            <n-card class="pricing-main" :bordered="true">
              <template #header>
                <div class="center">
                  <div class="price-large">
                    {{ pricing.price }}<span class="muted">{{ pricing.period }}</span>
                  </div>
                  <div class="muted">Propriétés illimitées</div>
                </div>
              </template>
              <n-space vertical size="small">
                <div class="row gap-xxs center" v-for="f in pricing.features" :key="f">
                  <n-icon size="16" class="primary"><Icon icon="mdi:check" /></n-icon>
                  <span class="sm">{{ f }}</span>
                </div>
              </n-space>
              <template #action>
                <n-button type="warning" size="large" block>S'abonner maintenant</n-button>
              </template>
            </n-card>
          </div>
        </div>
      </section>

      <!-- Testimonials -->
      <section class="section">
        <div class="container">
          <h2 class="center">Avis des utilisateurs</h2>
          <n-grid cols="1 m:3" x-gap="16" y-gap="16" class="mt-2">
            <n-gi v-for="t in testimonials" :key="t.name">
              <n-card>
                <div class="testimonial-header mb-1">
                  <div class="avatar">{{ t.avatar }}</div>
                  <div>
                    <div class="semi">{{ t.name }}</div>
                    <div class="xs muted">{{ t.role }}</div>
                  </div>
                </div>
                <p class="sm">{{ t.text }}</p>
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

      <!-- CTA Final -->
      <section class="section cta-final">
        <div class="container center">
          <h2>Simplifiez votre comptabilité LMNP dès aujourd'hui</h2>
          <p class="muted">Rejoignez les centaines de propriétaires qui nous font confiance pour leurs obligations fiscales.</p>
          <div class="row gap-xs mt-1 center">
            <n-button type="primary" size="large">Commencer maintenant</n-button>
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
.container {
  max-width: 1120px;
  margin: 0 auto;
  padding: 0 16px;
}
.row {
  display: flex;
  gap: 12px;
}
.row.center {
  align-items: center;
}
.row.between {
  justify-content: space-between;
}
.row.wrap {
  flex-wrap: wrap;
}
.grid {
  display: grid;
  gap: 16px;
}
.grid.two {
  grid-template-columns: repeat(2, minmax(0, 1fr));
}
.center {
  text-align: center;
}
.text {
  max-width: 720px;
  margin: 0 auto;
}

/* Header */
.header {
  position: sticky;
  top: 0;
  z-index: 50;
  backdrop-filter: blur(8px);
  background: linear-gradient(180deg, rgba(255, 255, 255, 0.85), rgba(255, 255, 255, 0.6));
  border-bottom: 1px solid rgba(15, 23, 42, 0.06);
}
.logo {
  height: 36px;
  width: 36px;
  display: grid;
  place-items: center;
  border-radius: 12px;
  background: rgba(33, 59, 126, 0.12);
}
.logo.small {
  height: 28px;
  width: 28px;
}
.brand-text .muted {
  margin-left: 8px;
  font-size: 12px;
  opacity: 0.65;
}
.nav {
  display: none;
  gap: 20px;
}
.nav a {
  color: inherit;
  opacity: 0.8;
  text-decoration: none;
}
.nav a:hover {
  color: #213B7E;
  opacity: 1;
}
@media (min-width: 768px) {
  .nav {
    display: flex;
  }
}

/* Hero */
.hero {
  padding: 64px 0;
  background:
    radial-gradient(1200px 600px at 20% 0%, rgba(33, 59, 126, 0.14), transparent 60%),
    radial-gradient(1200px 600px at 80% 10%, rgba(247, 148, 29, 0.14), transparent 60%),
    linear-gradient(180deg, #F5F5F5 0%, #fff 60%);
}
.hero-grid {
  grid-template-columns: 1fr;
  align-items: center;
}
@media (min-width: 900px) {
  .hero-grid {
    grid-template-columns: 1.1fr 0.9fr;
  }
}
.hero-title {
  font-weight: 800;
  font-size: clamp(28px, 6vw, 44px);
  line-height: 1.05;
}
.lead {
  margin-top: 12px;
  font-size: 18px;
  opacity: 0.8;
}
.primary {
  background: linear-gradient(45deg, #213B7E, #F7941D);
  -webkit-background-clip: text;
  background-clip: text;
  color: transparent;
}
.mockup {
  border-style: solid;
  border-color: rgba(33, 59, 126, 0.18);
  box-shadow: 0 10px 30px rgba(17, 24, 39, 0.08);
}
.chart {
  height: 120px;
  border-radius: 8px;
  background: repeating-linear-gradient(90deg, #eef2ff, #eef2ff 8px, #fff 8px, #fff 16px);
}
.kpi {
  font-weight: 700;
  font-size: 18px;
}
.pdf {
  height: 110px;
  border-radius: 8px;
  background: linear-gradient(135deg, #fafafa, #f1f5f9);
  border: 1px dashed rgba(0, 0, 0, 0.08);
}
.file .truncate {
  max-width: 70%;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

/* Sections */
.section {
  padding: 56px 0;
}

/* Section Problème/Solution */
.problem-solution {
  background: #F5F5F5;
}
.problem-solution-grid {
  grid-template-columns: 1fr;
  gap: 32px;
}
@media (min-width: 768px) {
  .problem-solution-grid {
    grid-template-columns: 1fr 1fr;
  }
}
.problem, .solution {
  padding: 24px;
  border-radius: 12px;
  background: white;
  text-align: center;
}
.problem h3, .solution h3 {
  margin: 12px 0 8px;
  color: #333333;
}

/* Pricing */
.pricing-card {
  max-width: 400px;
  margin: 0 auto;
}
.price-large {
  font-size: 32px;
  font-weight: 800;
  color: #213B7E;
}
.price-large .muted {
  font-size: 16px;
  margin-left: 4px;
}

/* Témoignages */
.testimonial-header {
  display: flex;
  align-items: center;
  gap: 12px;
}
.avatar {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  background: #213B7E;
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 600;
  font-size: 14px;
}

/* CTA Final */
.cta-final {
  background: linear-gradient(135deg, #213B7E, #1a2f68);
  color: white;
}
.cta-final h2, .cta-final p {
  color: white;
}

/* Proof */
.proof {
  background: #F5F5F5;
  padding: 40px 0;
  border-top: 1px solid rgba(0, 0, 0, 0.06);
  border-bottom: 1px solid rgba(0, 0, 0, 0.06);
}
.big {
  font-size: 30px;
  font-weight: 800;
  color: #213B7E;
}

/* Footer */
.footer {
  border-top: 1px solid rgba(0, 0, 0, 0.08);
  padding-top: 28px;
}
.footer-grid {
  grid-template-columns: repeat(1, minmax(0, 1fr));
}
@media (min-width: 900px) {
  .footer-grid {
    grid-template-columns: 2fr 1fr 1fr 1fr;
  }
}
.links {
  margin: 0;
  padding: 0;
  list-style: none;
  display: grid;
  gap: 8px;
}
.semi {
  font-weight: 600;
}
.subfooter {
  border-top: 1px solid rgba(0, 0, 0, 0.08);
  margin-top: 16px;
  font-size: 12px;
  opacity: 0.7;
}

/* Utils */
.mb-1 {
  margin-bottom: 8px;
}
.mb-2 {
  margin-bottom: 12px;
}
.mt-1 {
  margin-top: 8px;
}
.mt-2 {
  margin-top: 16px;
}
.mt-3 {
  margin-top: 20px;
}
.gap-xxs {
  gap: 6px;
}
.gap-xs {
  gap: 10px;
}
.gap-sm {
  gap: 14px;
}
.sm {
  font-size: 14px;
}
.xs {
  font-size: 12px;
}
.muted {
  opacity: 0.72;
}

/* Responsive helpers for mockup grid */
.md7 {
  grid-column: span 12;
}
.md5 {
  grid-column: span 12;
}
@media (min-width: 900px) {
  .md7 {
    grid-column: span 7;
  }
  .md5 {
    grid-column: span 5;
  }
}
</style>
