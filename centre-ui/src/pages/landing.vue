<script setup lang="ts">
import { ref, onMounted, onUnmounted, reactive } from 'vue'
import { 
  ArrowRight, 
  CheckCircle, 
  Star, 
  Calculator, 
  Users, 
  FileText, 
  Shield,
  Menu,
  X,
  Mail,
  Phone,
  MapPin,
  ChevronDown
} from 'lucide-vue-next'

definePage({
  meta: {
    constant: true,
    hideInMenu: true,
    layout: 'blank',
  },
})

const isMenuOpen = ref(false)
const openFAQ = ref<number | null>(null)
const isScrolled = ref(false)

// Formulaire d'inscription
const registerLoading = ref(false)
const acceptTerms = ref(false)

interface RegisterFormModel {
  login: string
  email: string
  password: string
  confirmPassword: string
  nom: string
  prenom: string
}

const registerModel: RegisterFormModel = reactive({
  login: '',
  email: '',
  password: '',
  confirmPassword: '',
  nom: '',
  prenom: '',
})

interface CapitalIdentitesModel {
  nomExploitant: string
  prenomExploitant: string
  adresseExploitant: string
  codePostalExploitant: string
  villeExploitant: string
  qualite: string
  apportsNumeraires: number | null
  apportsNature: number | null
  apportsIndustrie: number | null
  partsDetenues: string
}

const capitalModel: CapitalIdentitesModel = reactive({
  nomExploitant: '',
  prenomExploitant: '',
  adresseExploitant: '',
  codePostalExploitant: '',
  villeExploitant: '',
  qualite: 'Exploitant individuel',
  apportsNumeraires: null,
  apportsNature: null,
  apportsIndustrie: null,
  partsDetenues: 'N/A',
})

const handleScroll = () => {
  isScrolled.value = window.scrollY > 50
}

async function handleRegisterSubmit() {
  // Validation basique
  if (!registerModel.login || !registerModel.email || !registerModel.password || !registerModel.confirmPassword || !registerModel.nom || !registerModel.prenom) {
    window.$message?.error('Veuillez remplir tous les champs obligatoires')
    return
  }
  
  if (registerModel.password !== registerModel.confirmPassword) {
    window.$message?.error('Les mots de passe ne correspondent pas')
    return
  }
  
  if (registerModel.password.length < 6) {
    window.$message?.error('Le mot de passe doit contenir au moins 6 caractères')
    return
  }
  
  // Validation de l'email
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
  if (!emailRegex.test(registerModel.email)) {
    window.$message?.error('Veuillez saisir une adresse email valide')
    return
  }
  
  if (!acceptTerms.value) {
    window.$message?.error('Vous devez accepter les conditions d\'utilisation')
    return
  }

  registerLoading.value = true
  try {
    // Mise à jour du modèle capital avec les données du formulaire
    capitalModel.nomExploitant = registerModel.nom
    capitalModel.prenomExploitant = registerModel.prenom
    
    // Construction du payload
    const payload = {
      userName: registerModel.login,
      password: registerModel.password,
      email: registerModel.email,
      nom: registerModel.nom,
      prenom: registerModel.prenom,
      capitalIdentites: {
        ...capitalModel,
      },
    }
    
    // Inscription directe
    const response = await fetch(`${import.meta.env.VITE_SERVICE_BASE_URL}/auth/register`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload),
    })
    
    if (!response.ok) {
      const error = await response.text()
      throw new Error(error)
    }
    
    // Redirection vers la page de login avec l'identifiant prérempli
    window.$message?.success('Inscription réussie ! Vous pouvez maintenant vous connecter.')
    window.location.href = `/login?username=${encodeURIComponent(registerModel.login)}`
  }
  catch (e: any) {
    // Gestion spécifique des erreurs courantes
    if (e.message.includes('utilisateur_email_key') || e.message.includes('email') || e.message.includes('dupliqu')) {
      window.$message?.error('Cette adresse email est déjà utilisée. Veuillez utiliser une autre adresse ou vous connecter.')
    } else if (e.message.includes('utilisateur_user_name_key') || e.message.includes('username') || e.message.includes('login')) {
      window.$message?.error('Ce nom d\'utilisateur est déjà pris. Veuillez en choisir un autre.')
    } else if (e.message.includes('validation') || e.message.includes('invalide')) {
      window.$message?.error('Veuillez vérifier les informations saisies et réessayer.')
    } else {
      window.$message?.error(e.message || 'Erreur lors de l\'inscription. Veuillez réessayer.')
    }
  }
  finally {
    registerLoading.value = false
  }
}



onMounted(() => {
  window.addEventListener('scroll', handleScroll)
})

onUnmounted(() => {
  window.removeEventListener('scroll', handleScroll)
})

// Navigation items
const navItems = [
  { name: 'Accueil', href: '#hero' },
  { name: 'Fonctionnalités', href: '#features' },
  { name: 'Tarifs', href: '#pricing' },
  { name: 'FAQ', href: '#faq' },
  { name: 'Contact', href: '#contact' }
]

// FAQ data
const faqData = [
  {
    question: "Qu'est-ce que la LMNP et pourquoi ai-je besoin de Zymo ?",
    answer: "La Location Meublée Non Professionnelle (LMNP) est un statut fiscal qui vous permet de louer des biens meublés. Zymo simplifie toute la gestion comptable, fiscale et administrative de vos locations LMNP, vous garantissant la conformité avec l'administration fiscale."
  },
  {
    question: "Comment Zymo assure-t-il la conformité fiscale ?",
    answer: "Zymo est développé en conformité avec les dernières réglementations fiscales françaises. Notre logiciel génère automatiquement tous les documents requis par l'administration fiscale, incluant la liasse fiscale obligatoire pour le régime réel."
  },
  {
    question: "Puis-je essayer Zymo gratuitement ?",
    answer: "Oui ! Nous proposons un essai gratuit de 30 jours sans engagement. Vous pouvez tester toutes les fonctionnalités et voir comment Zymo simplifie votre gestion LMNP."
  },
  {
    question: "Que se passe-t-il si j'ai des questions ou besoin d'aide ?",
    answer: "Notre équipe support est disponible pour vous accompagner. Nous proposons également des guides détaillés et des webinaires pour vous aider à maîtriser votre fiscalité LMNP en toute sérénité."
  },
  {
    question: "Mes données sont-elles sécurisées ?",
    answer: "Absolument. Zymo utilise un chiffrement de niveau bancaire et est hébergé sur des serveurs sécurisés en France. Vos données personnelles et financières sont protégées selon les normes RGPD les plus strictes."
  }
]

const features = [
  {
    icon: Calculator,
    title: "Comptabilité automatisée",
    description: "Enregistrement automatique des revenus et charges. Calcul instantané de vos bénéfices et amortissements.",
    image: "https://images.unsplash.com/photo-1576272531110-2a342fe22342?crop=entropy&cs=srgb&fm=jpg&ixid=M3w3NTY2Nzh8MHwxfHNlYXJjaHwyfHxzb2Z0d2FyZSUyMGludGVyZmFjZXxlbnwwfHx8Ymx1ZXwxNzUzNTA4ODE1fDA&ixlib=rb-4.1.0&q=85"
  },
  {
    icon: Users,
    title: "Gestion des locataires",
    description: "Suivi des baux, des paiements et de la communication avec vos locataires en un seul endroit.",
    image: "https://images.unsplash.com/photo-1573496130103-a442a3754d0e?crop=entropy&cs=srgb&fm=jpg&ixid=M3w3NDQ2NDF8MHwxfHNlYXJjaHwxfHxidXNpbmVzcyUyMHByb2Zlc3Npb25hbHxlbnwwfHx8Ymx1ZXwxNzUzNDg5ODA1fDA&ixlib=rb-4.1.0&q=85"
  },
  {
    icon: FileText,
    title: "Documents centralisés",
    description: "Stockage sécurisé de tous vos documents : baux, factures, justificatifs. Tout à portée de clic.",
    image: "https://images.unsplash.com/photo-1664526937033-fe2c11f1be25?crop=entropy&cs=srgb&fm=jpg&ixid=M3w3NTY2Nzh8MHwxfHNlYXJjaHw0fHxzb2Z0d2FyZSUyMGludGVyZmFjZXxlbnwwfHx8Ymx1ZXwxNzUzNTA4ODE1fDA&ixlib=rb-4.1.0&q=85"
  },
  {
    icon: Shield,
    title: "Liasse fiscale automatique",
    description: "Génération automatique de votre liasse fiscale. Conformité garantie avec l'administration fiscale.",
    image: "https://images.unsplash.com/photo-1582647509870-c19b2ebb4a8f?crop=entropy&cs=srgb&fm=jpg&ixid=M3w3NDQ2MzR8MHwxfHNlYXJjaHwzfHxwcm9wZXJ0eSUyMG1hbmFnZW1lbnR8ZW58MHx8fGJsdWV8MTc1MzQ2MzEwOXww&ixlib=rb-4.1.0&q=85"
  }
]

const testimonials = [
  {
    name: "Marie Dubois",
    role: "Propriétaire de 3 appartements",
    content: "Zymo m'a fait gagner des heures chaque mois ! Plus besoin de me prendre la tête avec la comptabilité LMNP. Tout est automatisé et conforme.",
    rating: 5,
    image: "https://images.unsplash.com/photo-1573497490790-9053816a01d4?crop=entropy&cs=srgb&fm=jpg&ixid=M3w3NDQ2NDF8MHwxfHNlYXJjaHwyfHxidXNpbmVzcyUyMHByb2Zlc3Npb25hbHxlbnwwfHx8Ymx1ZXwxNzUzNDg5ODA1fDA&ixlib=rb-4.1.0&q=85"
  },
  {
    name: "Pierre Martin",
    role: "Investisseur immobilier",
    content: "Interface très intuitive et support réactif. J'ai pu générer ma première liasse fiscale en quelques clics. Parfait pour les non-experts !",
    rating: 5,
    image: "https://images.unsplash.com/photo-1544717297-fa95b6ee9643?crop=entropy&cs=srgb&fm=jpg&ixid=M3w3NDQ2NDF8MHwxfHNlYXJjaHwzfHxidXNpbmVzcyUyMHByb2Zlc3Npb25hbHxlbnwwfHx8Ymx1ZXwxNzUzNDg5ODA1fDA&ixlib=rb-4.1.0&q=85"
  },
  {
    name: "Sophie Laurent",
    role: "Propriétaire débutante",
    content: "Je ne connaissais rien à la fiscalité LMNP. Zymo m'a rassurée et guidée pas à pas. Maintenant je gère tout en toute sérénité !",
    rating: 5,
    image: "https://images.unsplash.com/photo-1590650624342-f527904ca1b3?crop=entropy&cs=srgb&fm=jpg&ixid=M3w3NDQ2NDJ8MHwxfHNlYXJjaHwxfHx0ZWFtJTIwbWVldGluZ3xlbnwwfHx8Ymx1ZXwxNzUzNTA4ODI2fDA&ixlib=rb-4.1.0&q=85"
  }
]

const pricingPlans = [
  {
    name: "Zymo Pro",
    price: "99",
    description: "Tarif unique annuel",
    features: [
      "Propriétés illimitées",
      "Comptabilité complète",
      "Gestion des locataires",
      "Génération liasse fiscale",
      "Support prioritaire",
      "Tableau de bord avancé",
      "API d'intégration",
      "Support téléphonique",
      "Formation personnalisée",
      "Conseiller fiscal dédié"
    ],
    recommended: true
  }
]
</script>

<template>
  <div class="min-h-screen bg-slate-900">
    <!-- Navigation -->
    <nav 
      :class="[
        'fixed top-0 w-full z-50 transition-all duration-300',
        isScrolled && !isMenuOpen
          ? 'md:bg-slate-900/95 md:backdrop-blur-md md:shadow-lg md:border-b md:border-slate-700/50 bg-transparent' 
          : isMenuOpen
          ? 'bg-slate-900/95 backdrop-blur-md shadow-lg border-b border-slate-700/50'
          : 'bg-transparent'
      ]"
    >
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="flex justify-between items-center py-4">
                                <div class="flex items-center">
             <a href="#hero" class="hover:scale-105 transition-transform cursor-pointer">
               <img 
                 src="/zymo-logo.svg" 
                 alt="Zymo Logo" 
                                   :class="[
                    'h-20 md:h-24 w-auto transition-opacity duration-300',
                    'md:opacity-100',
                    (isScrolled && !isMenuOpen) ? 'opacity-0 md:opacity-100' : isMenuOpen ? 'opacity-100' : 'opacity-0 md:opacity-100'
                  ]" 
               />
             </a>
           </div>
          
          <!-- Desktop Navigation -->
          <div class="hidden md:flex items-center space-x-8">
            <a
              v-for="item in navItems"
              :key="item.name"
              :href="item.href"
              class="text-white hover:text-blue-400 transition-colors duration-200 hover:-translate-y-0.5 transition-transform"
            >
              {{ item.name }}
            </a>
                                                   <a href="#contact" class="cta-button text-white px-6 py-2 rounded-lg font-semibold hover:scale-105 transition-transform">
                Essayer gratuitement
              </a>
             <a href="/login" class="border-2 border-white text-white px-6 py-2 rounded-lg font-semibold hover:bg-white hover:text-slate-900 transition-all duration-300">
               Se connecter
             </a>
          </div>

                     <!-- Mobile menu button -->
           <div class="md:hidden">
                           <button
                @click="isMenuOpen = !isMenuOpen"
                class="text-blue-400 hover:text-blue-300 hover:scale-95 transition-all duration-200 bg-transparent border-none p-2"
              >
               <X v-if="isMenuOpen" :size="24" />
               <Menu v-else :size="24" />
             </button>
           </div>
        </div>
      </div>

      <!-- Mobile Navigation -->
      <div
        v-if="isMenuOpen"
        class="md:hidden bg-slate-900/95 backdrop-blur-md shadow-lg border-b border-slate-700/50 transition-all duration-300"
      >
        <div class="px-4 py-2 space-y-2">
          <a
            v-for="item in navItems"
            :key="item.name"
            :href="item.href"
            class="block text-white hover:text-blue-400 py-2"
            @click="isMenuOpen = false"
          >
            {{ item.name }}
          </a>
                                           <a href="#contact" class="w-full cta-button text-white py-2 rounded-lg font-semibold mt-4 text-center block">
              Essayer gratuitement
            </a>
           <a href="/login" class="w-full border-2 border-white text-white py-2 rounded-lg font-semibold text-center block hover:bg-white hover:text-slate-900 transition-all duration-300 mt-2">
             Se connecter
           </a>
        </div>
      </div>
    </nav>

    <!-- Hero Section -->
    <section id="hero" class="hero-gradient min-h-screen flex items-center relative overflow-hidden">
      <div class="absolute inset-0 opacity-20">
        <img 
          src="https://images.unsplash.com/photo-1524292691042-82ed9c62673b?crop=entropy&cs=srgb&fm=jpg&ixid=M3w3NDQ2MzR8MHwxfHNlYXJjaHwxfHxwcm9wZXJ0eSUyMG1hbmFnZW1lbnR8ZW58MHx8fGJsdWV8MTc1MzQ2MzEwOXww&ixlib=rb-4.1.0&q=85" 
          alt="Property Management" 
          class="w-full h-full object-cover"
        />
        <div class="absolute inset-0 bg-gradient-to-r from-slate-900/90 to-slate-800/90"></div>
      </div>
      
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 relative z-10">
        <div class="grid lg:grid-cols-2 gap-12 items-center">
          <div class="animate-fade-in-left">
                         <h1 class="text-4xl md:text-6xl font-bold text-white leading-tight mb-6">
               Simplifiez votre  <span class="gradient-text"> fiscalité LMNP</span> en toute sérénité
             </h1>
            <p class="text-xl text-slate-300 mb-8 leading-relaxed">
              Zymo automatise votre comptabilité, gère vos locataires et génère votre liasse fiscale.
              Gagnez du temps et assurez-vous de la conformité fiscale de vos locations meublées.
            </p>
                         <div class="flex flex-col sm:flex-row gap-4">
                               <a href="#contact" class="cta-button text-white px-8 py-4 rounded-lg font-semibold text-lg flex items-center justify-center hover:scale-105 transition-transform">
                  Essayer gratuitement
                  <ArrowRight class="ml-2" :size="20" />
                </a>
               <button class="border-2 border-white text-white px-8 py-4 rounded-lg font-semibold text-lg bg-transparent hover:bg-white hover:text-slate-900 transition-all duration-300">
                 Voir la démonstration
               </button>
             </div>
            
            <!-- Trust indicators -->
            <div class="flex items-center mt-8 space-x-6">
              <div class="flex items-center">
                <CheckCircle class="text-green-400 mr-2" :size="20" />
                <span class="text-slate-300">Conforme DGFIP</span>
              </div>
              <div class="flex items-center">
                <CheckCircle class="text-green-400 mr-2" :size="20" />
                <span class="text-slate-300">30 jours gratuits</span>
              </div>
              <div class="flex items-center">
                <CheckCircle class="text-green-400 mr-2" :size="20" />
                <span class="text-slate-300">Support français</span>
              </div>
            </div>
          </div>
          
          <div class="relative animate-fade-in-right">
            <div class="glass-effect rounded-2xl p-8 shadow-2xl">
              <img 
                src="https://images.unsplash.com/photo-1659035260002-11d486d6e9f5?crop=entropy&cs=srgb&fm=jpg&ixid=M3w3NTY2Nzh8MHwxfHNlYXJjaHwxfHxzb2Z0d2FyZSUyMGludGVyZmFjZXxlbnwwfHx8Ymx1ZXwxNzUzNTA4ODE1fDA&ixlib=rb-4.1.0&q=85" 
                alt="Zymo Dashboard" 
                class="w-full rounded-lg"
              />
              <div class="absolute -bottom-4 -right-4 glass-effect rounded-xl p-4">
                <div class="flex items-center text-white">
                  <Star class="text-yellow-400 mr-1" :size="16" />
                  <span class="font-semibold">4.9/5</span>
                  <span class="text-sm text-slate-300 ml-2">+500 utilisateurs</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- Features Section -->
    <section id="features" class="py-20 bg-white">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="text-center mb-16 animate-fade-in-up">
          <h2 class="text-4xl md:text-5xl font-bold text-slate-900 mb-6">
            Tout ce dont vous avez besoin pour votre
            <span class="gradient-text">LMNP</span>
          </h2>
          <p class="text-xl text-slate-600 max-w-3xl mx-auto">
            Zymo centralise toute la gestion de vos locations meublées non professionnelles
            dans une interface simple et intuitive.
          </p>
        </div>

        <div class="grid md:grid-cols-2 lg:grid-cols-4 gap-8">
          <div
            v-for="(feature, index) in features"
            :key="index"
            class="feature-card bg-white rounded-2xl p-8 shadow-lg border border-slate-200 hover:scale-102 transition-transform animate-fade-in-up"
            :style="{ animationDelay: `${index * 0.2}s` }"
          >
            <div class="mb-6">
              <img 
                :src="feature.image" 
                :alt="feature.title" 
                class="w-full h-48 object-cover rounded-lg mb-4"
              />
              <component :is="feature.icon" class="text-blue-500" :size="40" />
            </div>
            <h3 class="text-xl font-bold text-slate-900 mb-4">{{ feature.title }}</h3>
            <p class="text-slate-600 leading-relaxed">{{ feature.description }}</p>
          </div>
        </div>
      </div>
    </section>

    <!-- Testimonials Section -->
    <section class="py-20 bg-slate-50">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="text-center mb-16 animate-fade-in-up">
          <h2 class="text-4xl md:text-5xl font-bold text-slate-900 mb-6">
            Ils nous font confiance
          </h2>
          <p class="text-xl text-slate-600">
            Découvrez comment Zymo a simplifié la gestion LMNP de nos utilisateurs
          </p>
        </div>

        <div class="grid md:grid-cols-3 gap-8">
          <div
            v-for="(testimonial, index) in testimonials"
            :key="index"
            class="bg-white rounded-2xl p-8 shadow-lg border border-slate-200 hover:scale-102 transition-transform animate-fade-in-up"
            :style="{ animationDelay: `${index * 0.2}s` }"
          >
            <div class="flex items-center mb-6">
              <img 
                :src="testimonial.image" 
                :alt="testimonial.name"
                class="w-16 h-16 rounded-full object-cover mr-4"
              />
              <div>
                <h4 class="font-bold text-slate-900">{{ testimonial.name }}</h4>
                <p class="text-slate-600 text-sm">{{ testimonial.role }}</p>
              </div>
            </div>
            <div class="flex mb-4">
              <Star
                v-for="i in testimonial.rating"
                :key="i"
                class="text-yellow-400 fill-current"
                :size="20"
              />
            </div>
            <p class="text-slate-700 italic leading-relaxed">"{{ testimonial.content }}"</p>
          </div>
        </div>
      </div>
    </section>

    <!-- Pricing Section -->
    <section id="pricing" class="py-20 bg-white">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                 <div class="text-center mb-16 animate-fade-in-up">
           <h2 class="text-4xl md:text-5xl font-bold text-slate-900 mb-6">
             Un tarif unique et transparent
           </h2>
           <p class="text-xl text-slate-600 max-w-3xl mx-auto">
             Une seule formule annuelle pour tous vos besoins LMNP.
             Inclut l'essai gratuit de 30 jours.
           </p>
         </div>

                 <div class="max-w-2xl mx-auto">
          <div
            v-for="(plan, index) in pricingPlans"
            :key="index"
            :class="[
              'rounded-2xl p-8 shadow-lg border-2 hover:scale-102 transition-transform animate-fade-in-up',
              plan.recommended 
                ? 'border-blue-500 bg-blue-50 relative' 
                : 'border-slate-200 bg-white'
            ]"
            :style="{ animationDelay: `${index * 0.1}s` }"
          >
            <div
              v-if="plan.recommended"
              class="absolute -top-4 left-1/2 transform -translate-x-1/2"
            >
              <span class="bg-blue-500 text-white px-6 py-2 rounded-full text-sm font-semibold">
                Recommandé
              </span>
            </div>
            
            <div class="text-center mb-8">
              <h3 class="text-2xl font-bold text-slate-900 mb-2">{{ plan.name }}</h3>
              <p class="text-slate-600 mb-4">{{ plan.description }}</p>
                             <div class="flex items-center justify-center">
                 <span class="text-4xl font-bold text-slate-900">{{ plan.price }}€</span>
                 <span class="text-slate-600 ml-2">/an</span>
               </div>
            </div>

            <ul class="space-y-4 mb-8">
              <li
                v-for="(feature, featureIndex) in plan.features"
                :key="featureIndex"
                class="flex items-center"
              >
                <CheckCircle class="text-green-500 mr-3 flex-shrink-0" :size="20" />
                <span class="text-slate-700">{{ feature }}</span>
              </li>
            </ul>

            <button
              :class="[
                'w-full py-4 rounded-lg font-semibold text-lg hover:scale-102 transition-transform',
                plan.recommended
                  ? 'cta-button text-white'
                  : 'border-2 border-slate-900 text-slate-900 hover:bg-slate-900 hover:text-white transition-all duration-300'
              ]"
            >
              Commencer l'essai gratuit
            </button>
          </div>
        </div>

        <div class="text-center mt-12 animate-fade-in-up">
          <p class="text-slate-600 mb-4">
            Tous les tarifs s'entendent hors taxes • Résiliation à tout moment
          </p>
          <p class="text-sm text-slate-500">
            💡 <strong>Astuce :</strong> Les frais d'abonnement Zymo sont déductibles de vos revenus LMNP !
          </p>
        </div>
      </div>
    </section>

    <!-- FAQ Section -->
    <section id="faq" class="py-20 bg-slate-50">
      <div class="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="text-center mb-16 animate-fade-in-up">
          <h2 class="text-4xl md:text-5xl font-bold text-slate-900 mb-6">
            Questions fréquentes
          </h2>
          <p class="text-xl text-slate-600">
            Tout ce que vous devez savoir sur Zymo et la fiscalité LMNP
          </p>
        </div>

        <div class="space-y-4">
          <div
            v-for="(faq, index) in faqData"
            :key="index"
            class="bg-white rounded-xl shadow-lg border border-slate-200 overflow-hidden animate-fade-in-up"
            :style="{ animationDelay: `${index * 0.1}s` }"
          >
            <button
              class="w-full p-6 text-left flex justify-between items-center hover:bg-slate-50 transition-colors duration-200"
              @click="openFAQ = openFAQ === index ? null : index"
            >
              <h3 class="text-lg font-semibold text-slate-900 pr-4">
                {{ faq.question }}
              </h3>
              <div
                :class="[
                  'transition-transform duration-300',
                  openFAQ === index ? 'rotate-180' : ''
                ]"
              >
                <ChevronDown class="text-slate-500" :size="24" />
              </div>
            </button>
            <div
              v-if="openFAQ === index"
              class="px-6 pb-6 text-slate-700 leading-relaxed transition-all duration-300"
            >
              {{ faq.answer }}
            </div>
          </div>
        </div>

        <div class="text-center mt-12 animate-fade-in-up">
          <p class="text-slate-600 mb-6">
            Vous avez d'autres questions ? Notre équipe est là pour vous aider.
          </p>
          <button class="cta-button text-white px-8 py-3 rounded-lg font-semibold hover:scale-105 transition-transform">
            Contactez notre support
          </button>
        </div>
      </div>
    </section>

    <!-- Contact Section -->
    <section id="contact" class="py-20 bg-white">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="text-center mb-16 animate-fade-in-up">
          <h2 class="text-4xl md:text-5xl font-bold text-slate-900 mb-6">
            Prêt à simplifier votre LMNP ?
          </h2>
          <p class="text-xl text-slate-600 max-w-3xl mx-auto">
            Rejoignez des centaines de propriétaires qui font confiance à Zymo
            pour leur gestion fiscale LMNP. Commencez dès aujourd'hui !
          </p>
        </div>

        <div class="grid lg:grid-cols-2 gap-12 items-center">
                       <div class="animate-fade-in-left">
                                         <div class="glass-effect rounded-2xl p-8 shadow-xl">
                 <h3 class="text-2xl font-bold text-slate-900 mb-6">
                   Créez votre compte gratuitement
                 </h3>
                 
                 <!-- Formulaire d'inscription -->
                 <div class="space-y-6">
                  <div class="grid md:grid-cols-2 gap-4">
                    <div>
                      <label class="block text-sm font-medium text-slate-700 mb-2">
                        Prénom
                      </label>
                      <input
                        v-model="registerModel.prenom"
                        type="text"
                        class="w-full px-4 py-3 rounded-lg border border-slate-300 focus:ring-2 focus:ring-blue-500 focus:border-blue-500 transition-colors duration-200"
                        placeholder="Votre prénom"
                        required
                      />
                    </div>
                    <div>
                      <label class="block text-sm font-medium text-slate-700 mb-2">
                        Nom
                      </label>
                      <input
                        v-model="registerModel.nom"
                        type="text"
                        class="w-full px-4 py-3 rounded-lg border border-slate-300 focus:ring-2 focus:ring-blue-500 focus:border-blue-500 transition-colors duration-200"
                        placeholder="Votre nom"
                        required
                      />
                    </div>
                  </div>
                  <div>
                    <label class="block text-sm font-medium text-slate-700 mb-2">
                      Nom d'utilisateur
                    </label>
                    <input
                      v-model="registerModel.login"
                      type="text"
                      class="w-full px-4 py-3 rounded-lg border border-slate-300 focus:ring-2 focus:ring-blue-500 focus:border-blue-500 transition-colors duration-200"
                      placeholder="Votre nom d'utilisateur"
                      required
                    />
                  </div>
                  <div>
                    <label class="block text-sm font-medium text-slate-700 mb-2">
                      Email professionnel
                    </label>
                    <input
                      v-model="registerModel.email"
                      type="email"
                      class="w-full px-4 py-3 rounded-lg border border-slate-300 focus:ring-2 focus:ring-blue-500 focus:border-blue-500 transition-colors duration-200"
                      placeholder="votre@email.com"
                      required
                    />
                  </div>
                  <div>
                    <label class="block text-sm font-medium text-slate-700 mb-2">
                      Mot de passe
                    </label>
                    <input
                      v-model="registerModel.password"
                      type="password"
                      class="w-full px-4 py-3 rounded-lg border border-slate-300 focus:ring-2 focus:ring-blue-500 focus:border-blue-500 transition-colors duration-200"
                      placeholder="Votre mot de passe"
                      required
                    />
                  </div>
                  <div>
                    <label class="block text-sm font-medium text-slate-700 mb-2">
                      Confirmer le mot de passe
                    </label>
                    <input
                      v-model="registerModel.confirmPassword"
                      type="password"
                      class="w-full px-4 py-3 rounded-lg border border-slate-300 focus:ring-2 focus:ring-blue-500 focus:border-blue-500 transition-colors duration-200"
                      placeholder="Confirmez votre mot de passe"
                      required
                    />
                  </div>
                                     <div class="flex items-start">
                     <input
                       v-model="acceptTerms"
                       type="checkbox"
                       id="terms"
                       class="mt-1 mr-3 rounded border-slate-300 text-blue-600 focus:ring-blue-500"
                       required
                     />
                     <label for="terms" class="text-sm text-slate-600">
                       J'accepte les <a href="#" class="text-blue-600 hover:underline">conditions d'utilisation</a> et la <a href="#" class="text-blue-600 hover:underline">politique de confidentialité</a>
                     </label>
                   </div>
                                     <button
                     @click="handleRegisterSubmit"
                     :disabled="registerLoading || !acceptTerms"
                     class="w-full cta-button text-white py-4 rounded-lg font-semibold text-lg hover:scale-102 transition-transform disabled:opacity-50 disabled:cursor-not-allowed"
                   >
                     <span v-if="registerLoading">Envoi en cours...</span>
                     <span v-else>Créer mon compte gratuitement</span>
                   </button>
                                     <p class="text-sm text-slate-600 text-center">
                     Déjà un compte ? <a href="/login" class="text-blue-600 hover:underline font-semibold">Se connecter</a>
                   </p>
                 </div>
              </div>
          </div>

          <div class="space-y-8 animate-fade-in-right">
            <div class="text-center lg:text-left">
              <h3 class="text-2xl font-bold text-slate-900 mb-6">
                Contactez-nous directement
              </h3>
              
              <div class="space-y-6">
                <div class="flex items-center justify-center lg:justify-start">
                  <div class="bg-blue-100 p-3 rounded-full mr-4">
                    <Mail class="text-blue-600" :size="24" />
                  </div>
                  <div>
                    <p class="font-semibold text-slate-900">Email</p>
                    <p class="text-slate-600">contact@zymo.fr</p>
                  </div>
                </div>
                
                <div class="flex items-center justify-center lg:justify-start">
                  <div class="bg-green-100 p-3 rounded-full mr-4">
                    <Phone class="text-green-600" :size="24" />
                  </div>
                  <div>
                    <p class="font-semibold text-slate-900">Téléphone</p>
                    <p class="text-slate-600">01 23 45 67 89</p>
                    <p class="text-sm text-slate-500">Lun-Ven 9h-18h</p>
                  </div>
                </div>
                
                <div class="flex items-center justify-center lg:justify-start">
                  <div class="bg-purple-100 p-3 rounded-full mr-4">
                    <MapPin class="text-purple-600" :size="24" />
                  </div>
                  <div>
                    <p class="font-semibold text-slate-900">Adresse</p>
                    <p class="text-slate-600">123 Avenue des Champs-Élysées</p>
                    <p class="text-slate-600">75008 Paris, France</p>
                  </div>
                </div>
              </div>

              <div class="mt-8 p-6 bg-green-50 rounded-xl border border-green-200">
                <div class="flex items-center justify-center lg:justify-start mb-4">
                  <CheckCircle class="text-green-500 mr-2" :size="24" />
                  <span class="font-semibold text-green-800">Support français garanti</span>
                </div>
                <p class="text-green-700 text-sm">
                  Notre équipe support basée en France vous répond en moins de 4 heures
                  ouvrées. Expertise fiscale LMNP incluse.
                </p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- Footer -->
    <footer class="bg-slate-900 text-white py-16">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="grid md:grid-cols-4 gap-8 mb-8">
                     <div>
                                        <div class="mb-4">
               <a href="#hero" class="hover:scale-105 transition-transform cursor-pointer">
                 <img src="/zymo-logo.svg" alt="Zymo Logo" class="h-24 md:h-28 w-auto" />
               </a>
             </div>
            <p class="text-slate-400 mb-6">
              La solution de référence pour simplifier votre fiscalité LMNP
              et gérer vos locations meublées en toute sérénité.
            </p>
            <div class="flex space-x-4">
              <!-- Social media links would go here -->
            </div>
          </div>
          
          <div>
            <h4 class="font-semibold mb-4">Produit</h4>
            <ul class="space-y-2 text-slate-400">
              <li><a href="#features" class="hover:text-white transition-colors duration-200">Fonctionnalités</a></li>
              <li><a href="#pricing" class="hover:text-white transition-colors duration-200">Tarifs</a></li>
              <li><a href="#" class="hover:text-white transition-colors duration-200">Sécurité</a></li>
              <li><a href="#" class="hover:text-white transition-colors duration-200">Intégrations</a></li>
            </ul>
          </div>
          
          <div>
            <h4 class="font-semibold mb-4">Ressources</h4>
            <ul class="space-y-2 text-slate-400">
              <li><a href="#" class="hover:text-white transition-colors duration-200">Guide LMNP</a></li>
              <li><a href="#" class="hover:text-white transition-colors duration-200">Centre d'aide</a></li>
              <li><a href="#" class="hover:text-white transition-colors duration-200">Blog</a></li>
              <li><a href="#" class="hover:text-white transition-colors duration-200">Webinaires</a></li>
            </ul>
          </div>
          
          <div>
            <h4 class="font-semibold mb-4">Entreprise</h4>
            <ul class="space-y-2 text-slate-400">
              <li><a href="#" class="hover:text-white transition-colors duration-200">À propos</a></li>
              <li><a href="#contact" class="hover:text-white transition-colors duration-200">Contact</a></li>
              <li><a href="#" class="hover:text-white transition-colors duration-200">Carrières</a></li>
              <li><a href="#" class="hover:text-white transition-colors duration-200">Presse</a></li>
            </ul>
          </div>
        </div>
        
        <div class="border-t border-slate-800 pt-8 flex flex-col md:flex-row justify-between items-center">
          <p class="text-slate-400 text-sm">
            © 2025 Zymo. Tous droits réservés.
          </p>
          <div class="flex space-x-6 mt-4 md:mt-0">
            <a href="#" class="text-slate-400 hover:text-white text-sm transition-colors duration-200">
              Mentions légales
            </a>
            <a href="#" class="text-slate-400 hover:text-white text-sm transition-colors duration-200">
              Politique de confidentialité
            </a>
            <a href="#" class="text-slate-400 hover:text-white text-sm transition-colors duration-200">
              CGU
            </a>
          </div>
        </div>
      </div>
    </footer>

         
  </div>
</template>

<style scoped>
:root {
  --color-primary: #213B7E;
  --color-accent: #F7941D;
  --color-bg: #F5F5F5;
  --color-text: #333333;
  --color-muted: #666666;
  --color-white: #FFFFFF;
}

/* Variables CSS globales pour s'assurer qu'elles sont disponibles */
:global(:root) {
  --color-primary: #213B7E;
  --color-accent: #F7941D;
}

.gradient-text {
  background: linear-gradient(135deg, var(--color-primary), #4a90e2);
  -webkit-background-clip: text;
  background-clip: text;
  color: transparent;
}

.cta-button {
  background: var(--color-primary) !important;
  border: none;
}

.cta-button:hover {
  background: #1a2f68 !important;
}

.glass-effect {
  background: rgba(255, 255, 255, 0.06);
  backdrop-filter: blur(12px);
  border: 1px solid rgba(255, 255, 255, 0.1);
}

.hero-gradient {
  background: linear-gradient(135deg, #0f172a 0%, #1e293b 50%, #334155 100%);
}

.feature-card {
  transition: all 0.3s ease;
}

.feature-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
}

/* Animations */
@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes fadeInLeft {
  from {
    opacity: 0;
    transform: translateX(-50px);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}

@keyframes fadeInRight {
  from {
    opacity: 0;
    transform: translateX(50px);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}

@keyframes fadeInScale {
  from {
    opacity: 0;
    transform: scale(0);
  }
  to {
    opacity: 1;
    transform: scale(1);
  }
}

.animate-fade-in-up {
  animation: fadeInUp 0.6s ease-out forwards;
}

.animate-fade-in-left {
  animation: fadeInLeft 0.8s ease-out forwards;
}

.animate-fade-in-right {
  animation: fadeInRight 0.8s ease-out 0.2s forwards;
}

.animate-fade-in-scale {
  animation: fadeInScale 0.5s ease-out forwards;
  opacity: 0;
}

/* Responsive utilities */
@media (max-width: 768px) {
  .hero-gradient {
    min-height: 100vh;
  }
  
  .text-4xl {
    font-size: 2rem;
  }
  
  .md\:text-6xl {
    font-size: 2.5rem;
  }
}

.hover\:scale-102:hover {
  transform: scale(1.02);
}

.hover\:scale-105:hover {
  transform: scale(1.05);
}

.hover\:scale-95:hover {
  transform: scale(0.95);
}

.transition-transform {
  transition: transform 0.2s ease;
}
</style>
