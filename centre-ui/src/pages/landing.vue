<script setup lang="ts">
import { ref } from 'vue'
import { Icon } from '@iconify/vue'

definePage({
  meta: {
    constant: true,
    hideInMenu: true,
    layout: 'blank',
  },
})

// Fonctionnalités clés pour LMNP
const features = [
  { 
    icon: 'mdi:file-export', 
    title: 'Export automatisé des données comptables', 
    desc: 'Exportez vos données vers les formats fiscaux requis en un clic, sans erreur.' 
  },
  { 
    icon: 'mdi:link-variant', 
    title: 'Intégration logiciels comptables', 
    desc: 'Compatible avec les principaux logiciels comptables du marché.' 
  },
  { 
    icon: 'mdi:chart-line', 
    title: 'Interface claire et indicateurs clés', 
    desc: 'Suivez vos loyers, charges et rendement en temps réel avec des tableaux de bord intuitifs.' 
  },
  { 
    icon: 'mdi:infinity', 
    title: 'Propriétés illimitées', 
    desc: 'Aucune limite sur le nombre de propriétés gérées dans votre portefeuille.' 
  },
  { 
    icon: 'mdi:headset', 
    title: 'Support dédié LMNP', 
    desc: 'Assistance réactive spécialisée dans les particularités du LMNP.' 
  },
]

// Statistiques sociales
const socialProof = [
  { metric: '+15h', description: 'économisées par déclaration' },
  { metric: '0', description: 'erreur fiscale avec Zymo' },
  { metric: '98%', description: 'de clients satisfaits' },
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

const email = ref('')

function handleFreeTrial() {
  console.log('Essai gratuit demandé')
}

function handleDemo() {
  console.log('Démo demandée')
}

function handleSubscribe() {
  console.log('Abonnement demandé')
}

function handleFinalCta() {
  console.log('CTA final activé')
}
</script>

<template>
  <n-config-provider :theme-overrides="themeOverrides">
    <div class="landing-page">
      <!-- Header -->
      <header class="header">
        <div class="container">
          <div class="header-content">
            <div class="brand">
              <div class="logo">
                <n-icon size="24" color="#213B7E">
                  <Icon icon="mdi:calculator-variant" />
                </n-icon>
              </div>
              <div class="brand-text">
                <h1 class="brand-name">Zymo</h1>
                <span class="brand-tagline">LMNP simplifié</span>
              </div>
            </div>
            
            <nav class="navigation">
              <a href="#fonctionnalites" class="nav-link">Fonctionnalités</a>
              <a href="#tarifs" class="nav-link">Tarifs</a>
              <a href="#temoignages" class="nav-link">Témoignages</a>
              <a href="#contact" class="nav-link">Contact</a>
            </nav>
            
            <div class="header-actions">
              <n-button quaternary size="medium">Connexion</n-button>
              <n-button type="warning" size="medium">S'inscrire</n-button>
            </div>
          </div>
        </div>
      </header>

      <!-- Hero Section -->
      <section class="hero">
        <div class="container">
          <div class="hero-content">
            <div class="hero-text">
              <n-tag type="success" round size="small" class="hero-badge">
                Spécialisé LMNP au régime réel
              </n-tag>
              
              <h1 class="hero-title">
                Exportez vos données comptables <span class="gradient-text">LMNP</span> en toute simplicité
              </h1>
              
              <p class="hero-subtitle">
                Gagnez du temps et assurez votre conformité fiscale, sans effort.
              </p>
              
              <div class="hero-actions">
                <n-button type="primary" size="large" @click="handleFreeTrial">
                  Essayer gratuitement
                </n-button>
                <n-button type="warning" size="large" @click="handleDemo">
                  Demander une démo
                </n-button>
              </div>
              
              <div class="hero-trust">
                <n-icon size="16" color="#16a34a">
                  <Icon icon="mdi:shield-check" />
                </n-icon>
                <span>RGPD • Données hébergées en UE • Annulation à tout moment</span>
              </div>
            </div>
            
            <div class="hero-visual">
              <div class="dashboard-preview">
                <div class="dashboard-header">
                  <div class="dashboard-tabs">
                    <div class="tab active">Tableau de bord</div>
                    <div class="tab">Déclarations</div>
                    <div class="tab">Export</div>
                  </div>
                </div>
                <div class="dashboard-content">
                  <div class="metric-cards">
                    <div class="metric-card">
                      <div class="metric-label">Loyers annuels</div>
                      <div class="metric-value">25 800€</div>
                      <div class="metric-trend positive">+6.2%</div>
                    </div>
                    <div class="metric-card">
                      <div class="metric-label">Charges déductibles</div>
                      <div class="metric-value">8 450€</div>
                      <div class="metric-trend">-2.1%</div>
                    </div>
                  </div>
                  <div class="export-section">
                    <div class="export-button">
                      <n-icon size="16" color="#213B7E">
                        <Icon icon="mdi:download" />
                      </n-icon>
                      <span>Exporter vers DGFiP</span>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>

      <!-- Problème et Solution -->
      <section class="problem-solution">
        <div class="container">
          <div class="problem-solution-grid">
            <div class="problem-card">
              <div class="problem-icon">
                <n-icon size="32" color="#F7941D">
                  <Icon icon="mdi:alert-circle-outline" />
                </n-icon>
              </div>
              <h3>Les défis du LMNP au régime réel</h3>
              <p>
                Liasse fiscale complexe, saisie comptable fastidieuse, risques d'erreurs coûteuses... 
                Les obligations fiscales LMNP peuvent vite devenir un casse-tête pour les propriétaires.
              </p>
            </div>
            
            <div class="solution-card">
              <div class="solution-icon">
                <n-icon size="32" color="#213B7E">
                  <Icon icon="mdi:check-circle-outline" />
                </n-icon>
              </div>
              <h3>Zymo simplifie tout</h3>
              <p>
                Solution simple, rapide et sécurisée, spécialement conçue pour les loueurs en meublé non professionnels. 
                Export automatisé, conformité garantie, tranquillité d'esprit assurée.
              </p>
            </div>
          </div>
        </div>
      </section>

      <!-- Fonctionnalités -->
      <section id="fonctionnalites" class="features">
        <div class="container">
          <div class="section-header">
            <h2>Fonctionnalités clés pour le <span class="gradient-text">LMNP</span></h2>
            <p>Tout ce dont vous avez besoin pour gérer vos obligations fiscales en toute sérénité.</p>
          </div>
          
          <div class="features-grid">
            <div v-for="feature in features" :key="feature.title" class="feature-card">
              <div class="feature-icon">
                <n-icon size="24" color="#213B7E">
                  <Icon :icon="feature.icon" />
                </n-icon>
              </div>
              <h3>{{ feature.title }}</h3>
              <p>{{ feature.desc }}</p>
            </div>
          </div>
        </div>
      </section>

      <!-- Preuves sociales -->
      <section class="social-proof">
        <div class="container">
          <div class="social-proof-grid">
            <div v-for="proof in socialProof" :key="proof.metric" class="proof-card">
              <div class="proof-metric">{{ proof.metric }}</div>
              <div class="proof-description">{{ proof.description }}</div>
            </div>
          </div>
        </div>
      </section>

      <!-- Témoignages -->
      <section id="temoignages" class="testimonials">
        <div class="container">
          <div class="section-header">
            <h2>Avis des utilisateurs</h2>
            <p>Découvrez ce que pensent nos clients de Zymo</p>
          </div>
          
          <div class="testimonials-grid">
            <div v-for="testimonial in testimonials" :key="testimonial.name" class="testimonial-card">
              <div class="testimonial-content">
                <p>"{{ testimonial.text }}"</p>
              </div>
              <div class="testimonial-author">
                <div class="author-avatar">{{ testimonial.avatar }}</div>
                <div class="author-info">
                  <div class="author-name">{{ testimonial.name }}</div>
                  <div class="author-role">{{ testimonial.role }}</div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>

      <!-- Tarifs -->
      <section id="tarifs" class="pricing">
        <div class="container">
          <div class="section-header">
            <h2>Un tarif simple et transparent</h2>
            <p>Pas de surprises, pas de frais cachés. Tout inclus pour votre activité LMNP.</p>
          </div>
          
          <div class="pricing-card">
            <div class="pricing-header">
              <div class="price">{{ pricing.price }}<span class="period">{{ pricing.period }}</span></div>
              <div class="pricing-subtitle">Propriétés illimitées</div>
            </div>
            
            <div class="pricing-features">
              <div v-for="feature in pricing.features" :key="feature" class="pricing-feature">
                <n-icon size="16" color="#16a34a">
                  <Icon icon="mdi:check" />
                </n-icon>
                <span>{{ feature }}</span>
              </div>
            </div>
            
            <div class="pricing-action">
              <n-button type="warning" size="large" block @click="handleSubscribe">
                S'abonner maintenant
              </n-button>
            </div>
          </div>
        </div>
      </section>

      <!-- Appel à l'action final -->
      <section class="final-cta">
        <div class="container">
          <div class="cta-content">
            <h2>Simplifiez votre comptabilité LMNP dès aujourd'hui</h2>
            <p>Rejoignez les centaines de propriétaires qui nous font confiance pour leurs obligations fiscales.</p>
            <n-button type="primary" size="large" @click="handleFinalCta">
              Commencer maintenant
            </n-button>
          </div>
        </div>
      </section>

      <!-- Footer -->
      <footer class="footer">
        <div class="container">
          <div class="footer-content">
            <div class="footer-brand">
              <div class="brand">
                <div class="logo">
                  <n-icon size="20" color="#213B7E">
                    <Icon icon="mdi:calculator-variant" />
                  </n-icon>
                </div>
                <div class="brand-text">
                  <h3 class="brand-name">Zymo</h3>
                </div>
              </div>
              <p>Le copilote des propriétaires en LMNP.</p>
            </div>
            
            <div class="footer-links">
              <div class="link-group">
                <h4>Produit</h4>
                <ul>
                  <li><a href="#fonctionnalites">Fonctionnalités</a></li>
                  <li><a href="#tarifs">Tarifs</a></li>
                  <li><a href="#demo">Démo</a></li>
                </ul>
              </div>
              
              <div class="link-group">
                <h4>Ressources</h4>
                <ul>
                  <li><a href="#">Centre d'aide</a></li>
                  <li><a href="#">Documentation</a></li>
                  <li><a href="#">API</a></li>
                </ul>
              </div>
              
              <div class="link-group">
                <h4>Légal</h4>
                <ul>
                  <li><a href="#">Mentions légales</a></li>
                  <li><a href="#">Confidentialité</a></li>
                  <li><a href="#">Conditions d'utilisation</a></li>
                </ul>
              </div>
            </div>
          </div>
          
          <div class="footer-bottom">
            <div class="footer-bottom-content">
              <span>© {{ new Date().getFullYear() }} Zymo. Tous droits réservés.</span>
              <span>Fait avec ❤️ en France</span>
            </div>
          </div>
        </div>
      </footer>
    </div>
  </n-config-provider>
</template>

<style scoped>
/* Variables CSS */
:root {
  --primary-color: #213B7E;
  --accent-color: #F7941D;
  --background-light: #F5F5F5;
  --text-primary: #333333;
  --text-secondary: #666666;
  --white: #FFFFFF;
  --border-light: rgba(0, 0, 0, 0.08);
  --shadow-light: 0 4px 16px rgba(0, 0, 0, 0.08);
  --shadow-medium: 0 8px 32px rgba(0, 0, 0, 0.12);
}

/* Reset et base */
* {
  box-sizing: border-box;
}

.landing-page {
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', 'Roboto', 'Oxygen', 'Ubuntu', 'Cantarell', sans-serif;
  line-height: 1.6;
  color: var(--text-primary);
  background: var(--white);
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 24px;
}

/* Header */
.header {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 100;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(12px);
  border-bottom: 1px solid var(--border-light);
  padding: 16px 0;
}

.header-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 32px;
}

.brand {
  display: flex;
  align-items: center;
  gap: 12px;
}

.logo {
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(33, 59, 126, 0.1);
  border-radius: 8px;
}

.brand-name {
  font-size: 20px;
  font-weight: 700;
  color: var(--primary-color);
  margin: 0;
}

.brand-tagline {
  font-size: 12px;
  color: var(--text-secondary);
  margin-left: 8px;
}

.navigation {
  display: none;
  align-items: center;
  gap: 32px;
}

@media (min-width: 768px) {
  .navigation {
    display: flex;
  }
}

.nav-link {
  color: var(--text-secondary);
  text-decoration: none;
  font-weight: 500;
  transition: color 0.2s ease;
}

.nav-link:hover {
  color: var(--primary-color);
}

.header-actions {
  display: flex;
  align-items: center;
  gap: 12px;
}

/* Hero Section */
.hero {
  padding: 120px 0 80px;
  background: linear-gradient(135deg, var(--background-light) 0%, var(--white) 100%);
  position: relative;
  overflow: hidden;
}

.hero::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: 
    radial-gradient(circle at 20% 20%, rgba(33, 59, 126, 0.1) 0%, transparent 50%),
    radial-gradient(circle at 80% 80%, rgba(247, 148, 29, 0.1) 0%, transparent 50%);
  pointer-events: none;
}

.hero-content {
  display: grid;
  grid-template-columns: 1fr;
  gap: 48px;
  align-items: center;
  position: relative;
  z-index: 1;
}

@media (min-width: 768px) {
  .hero-content {
    grid-template-columns: 1.2fr 0.8fr;
  }
}

.hero-badge {
  margin-bottom: 16px;
}

.hero-title {
  font-size: clamp(32px, 5vw, 48px);
  font-weight: 800;
  line-height: 1.1;
  margin: 0 0 20px 0;
  color: var(--text-primary);
}

.gradient-text {
  background: linear-gradient(135deg, var(--primary-color), var(--accent-color));
  -webkit-background-clip: text;
  background-clip: text;
  color: transparent;
}

.hero-subtitle {
  font-size: 20px;
  color: var(--text-secondary);
  margin: 0 0 32px 0;
  max-width: 480px;
}

.hero-actions {
  display: flex;
  gap: 16px;
  margin-bottom: 20px;
  flex-wrap: wrap;
}

.hero-trust {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
  color: var(--text-secondary);
}

/* Dashboard Preview */
.dashboard-preview {
  background: var(--white);
  border-radius: 16px;
  box-shadow: var(--shadow-medium);
  border: 1px solid var(--border-light);
  overflow: hidden;
}

.dashboard-header {
  padding: 16px 20px;
  border-bottom: 1px solid var(--border-light);
  background: var(--background-light);
}

.dashboard-tabs {
  display: flex;
  gap: 20px;
}

.tab {
  padding: 8px 16px;
  border-radius: 6px;
  font-size: 14px;
  font-weight: 500;
  color: var(--text-secondary);
  cursor: pointer;
  transition: all 0.2s ease;
}

.tab.active {
  background: var(--primary-color);
  color: var(--white);
}

.dashboard-content {
  padding: 24px;
}

.metric-cards {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 16px;
  margin-bottom: 24px;
}

.metric-card {
  padding: 16px;
  background: var(--background-light);
  border-radius: 8px;
  border: 1px solid var(--border-light);
}

.metric-label {
  font-size: 12px;
  color: var(--text-secondary);
  margin-bottom: 4px;
}

.metric-value {
  font-size: 20px;
  font-weight: 700;
  color: var(--primary-color);
  margin-bottom: 4px;
}

.metric-trend {
  font-size: 12px;
  color: var(--text-secondary);
}

.metric-trend.positive {
  color: #16a34a;
}

.export-section {
  display: flex;
  justify-content: center;
}

.export-button {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 12px 24px;
  background: var(--primary-color);
  color: var(--white);
  border-radius: 8px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s ease;
}

.export-button:hover {
  background: #1a2f68;
}

/* Problème et Solution */
.problem-solution {
  padding: 80px 0;
  background: var(--background-light);
}

.problem-solution-grid {
  display: grid;
  grid-template-columns: 1fr;
  gap: 32px;
}

@media (min-width: 768px) {
  .problem-solution-grid {
    grid-template-columns: 1fr 1fr;
  }
}

.problem-card,
.solution-card {
  background: var(--white);
  padding: 32px;
  border-radius: 16px;
  box-shadow: var(--shadow-light);
  text-align: center;
  border: 1px solid var(--border-light);
}

.problem-icon,
.solution-icon {
  margin-bottom: 16px;
}

.problem-card h3,
.solution-card h3 {
  font-size: 20px;
  font-weight: 700;
  margin: 0 0 16px 0;
  color: var(--text-primary);
}

.problem-card p,
.solution-card p {
  color: var(--text-secondary);
  margin: 0;
  line-height: 1.6;
}

/* Sections communes */
.features,
.testimonials,
.pricing {
  padding: 80px 0;
}

.section-header {
  text-align: center;
  margin-bottom: 48px;
  max-width: 600px;
  margin-left: auto;
  margin-right: auto;
}

.section-header h2 {
  font-size: 32px;
  font-weight: 700;
  margin: 0 0 16px 0;
  color: var(--text-primary);
}

.section-header p {
  font-size: 18px;
  color: var(--text-secondary);
  margin: 0;
}

/* Fonctionnalités */
.features-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 24px;
}

.feature-card {
  background: var(--white);
  padding: 32px;
  border-radius: 16px;
  box-shadow: var(--shadow-light);
  border: 1px solid var(--border-light);
  text-align: center;
  transition: all 0.3s ease;
}

.feature-card:hover {
  transform: translateY(-4px);
  box-shadow: var(--shadow-medium);
}

.feature-icon {
  margin-bottom: 16px;
}

.feature-card h3 {
  font-size: 18px;
  font-weight: 700;
  margin: 0 0 12px 0;
  color: var(--text-primary);
}

.feature-card p {
  color: var(--text-secondary);
  margin: 0;
  line-height: 1.6;
}

/* Preuves sociales */
.social-proof {
  padding: 60px 0;
  background: var(--background-light);
  border-top: 1px solid var(--border-light);
  border-bottom: 1px solid var(--border-light);
}

.social-proof-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 32px;
}

.proof-card {
  text-align: center;
  background: var(--white);
  padding: 24px;
  border-radius: 12px;
  box-shadow: var(--shadow-light);
}

.proof-metric {
  font-size: 36px;
  font-weight: 800;
  color: var(--primary-color);
  margin-bottom: 8px;
}

.proof-description {
  font-size: 14px;
  color: var(--text-secondary);
}

/* Témoignages */
.testimonials-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 24px;
}

.testimonial-card {
  background: var(--white);
  padding: 32px;
  border-radius: 16px;
  box-shadow: var(--shadow-light);
  border: 1px solid var(--border-light);
}

.testimonial-content {
  margin-bottom: 20px;
}

.testimonial-content p {
  font-style: italic;
  color: var(--text-primary);
  margin: 0;
  line-height: 1.6;
}

.testimonial-author {
  display: flex;
  align-items: center;
  gap: 12px;
}

.author-avatar {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  background: var(--primary-color);
  color: var(--white);
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 600;
  font-size: 14px;
}

.author-name {
  font-weight: 600;
  color: var(--text-primary);
  margin-bottom: 2px;
}

.author-role {
  font-size: 14px;
  color: var(--text-secondary);
}

/* Tarifs */
.pricing {
  background: var(--background-light);
}

.pricing-card {
  max-width: 400px;
  margin: 0 auto;
  background: var(--white);
  border-radius: 20px;
  box-shadow: var(--shadow-medium);
  border: 1px solid var(--border-light);
  overflow: hidden;
}

.pricing-header {
  text-align: center;
  padding: 32px;
  background: var(--primary-color);
  color: var(--white);
}

.price {
  font-size: 48px;
  font-weight: 800;
  margin-bottom: 8px;
}

.period {
  font-size: 18px;
  font-weight: 400;
  opacity: 0.8;
}

.pricing-subtitle {
  font-size: 16px;
  opacity: 0.9;
}

.pricing-features {
  padding: 32px;
}

.pricing-feature {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 16px;
}

.pricing-feature:last-child {
  margin-bottom: 0;
}

.pricing-feature span {
  color: var(--text-primary);
  font-weight: 500;
}

.pricing-action {
  padding: 0 32px 32px;
}

/* CTA Final */
.final-cta {
  padding: 80px 0;
  background: linear-gradient(135deg, var(--primary-color), #1a2f68);
  color: var(--white);
  text-align: center;
}

.cta-content h2 {
  font-size: 32px;
  font-weight: 700;
  margin: 0 0 16px 0;
  color: var(--white);
}

.cta-content p {
  font-size: 18px;
  margin: 0 0 32px 0;
  opacity: 0.9;
  max-width: 600px;
  margin-left: auto;
  margin-right: auto;
}

/* Footer */
.footer {
  background: var(--white);
  border-top: 1px solid var(--border-light);
  padding: 48px 0 24px;
}

.footer-content {
  display: grid;
  grid-template-columns: 1fr;
  gap: 32px;
  margin-bottom: 32px;
}

@media (min-width: 768px) {
  .footer-content {
    grid-template-columns: 2fr 1fr 1fr 1fr;
  }
}

.footer-brand p {
  color: var(--text-secondary);
  margin: 16px 0 0 0;
}

.footer-links {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
  gap: 32px;
}

.link-group h4 {
  font-size: 16px;
  font-weight: 600;
  margin: 0 0 16px 0;
  color: var(--text-primary);
}

.link-group ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.link-group li {
  margin-bottom: 8px;
}

.link-group a {
  color: var(--text-secondary);
  text-decoration: none;
  transition: color 0.2s ease;
}

.link-group a:hover {
  color: var(--primary-color);
}

.footer-bottom {
  border-top: 1px solid var(--border-light);
  padding-top: 24px;
}

.footer-bottom-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 16px;
  font-size: 14px;
  color: var(--text-secondary);
}

/* Responsive */
@media (max-width: 768px) {
  .container {
    padding: 0 16px;
  }
  
  .hero {
    padding: 100px 0 60px;
  }
  
  .hero-actions {
    flex-direction: column;
    align-items: stretch;
  }
  
  .section-header h2 {
    font-size: 28px;
  }
  
  .footer-bottom-content {
    flex-direction: column;
    text-align: center;
  }
}
</style>
