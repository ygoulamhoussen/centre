import React, { useState, useEffect } from 'react';
import { motion, AnimatePresence } from 'framer-motion';
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
  ChevronDown,
  ChevronUp
} from 'lucide-react';

const LandingPage = () => {
  const [isMenuOpen, setIsMenuOpen] = useState(false);
  const [openFAQ, setOpenFAQ] = useState(null);
  const [isScrolled, setIsScrolled] = useState(false);

  useEffect(() => {
    const handleScroll = () => {
      setIsScrolled(window.scrollY > 50);
    };
    window.addEventListener('scroll', handleScroll);
    return () => window.removeEventListener('scroll', handleScroll);
  }, []);

  // Animation variants
  const fadeInUp = {
    initial: { opacity: 0, y: 30 },
    animate: { opacity: 1, y: 0 },
    transition: { duration: 0.6 }
  };

  const staggerContainer = {
    animate: {
      transition: {
        staggerChildren: 0.2
      }
    }
  };

  // Navigation items
  const navItems = [
    { name: 'Accueil', href: '#hero' },
    { name: 'Fonctionnalités', href: '#features' },
    { name: 'Tarifs', href: '#pricing' },
    { name: 'FAQ', href: '#faq' },
    { name: 'Contact', href: '#contact' }
  ];

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
  ];

  return (
    <div className="min-h-screen bg-slate-900">
      {/* Navigation */}
      <motion.nav 
        className={`fixed top-0 w-full z-50 transition-all duration-300 ${
          isScrolled ? 'bg-slate-900/95 backdrop-blur-md shadow-lg border-b border-slate-700/50' : 'bg-transparent'
        }`}
        initial={{ opacity: 0, y: -50 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.6 }}
      >
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between items-center py-4">
            <div className="flex items-center">
              <motion.div 
                className="text-2xl font-bold gradient-text"
                whileHover={{ scale: 1.05 }}
              >
                Zymo
              </motion.div>
            </div>
            
            {/* Desktop Navigation */}
            <div className="hidden md:flex items-center space-x-8">
              {navItems.map((item) => (
                <motion.a
                  key={item.name}
                  href={item.href}
                  className="text-white hover:text-blue-400 transition-colors duration-200"
                  whileHover={{ y: -2 }}
                >
                  {item.name}
                </motion.a>
              ))}
              <motion.button
                className="cta-button text-white px-6 py-2 rounded-lg font-semibold"
                whileHover={{ scale: 1.05 }}
                whileTap={{ scale: 0.95 }}
              >
                Essayer gratuitement
              </motion.button>
            </div>

            {/* Mobile menu button */}
            <div className="md:hidden">
              <motion.button
                onClick={() => setIsMenuOpen(!isMenuOpen)}
                className="text-white"
                whileTap={{ scale: 0.95 }}
              >
                {isMenuOpen ? <X size={24} /> : <Menu size={24} />}
              </motion.button>
            </div>
          </div>
        </div>

        {/* Mobile Navigation */}
        <AnimatePresence>
          {isMenuOpen && (
            <motion.div
              className="md:hidden glass-effect"
              initial={{ opacity: 0, height: 0 }}
              animate={{ opacity: 1, height: 'auto' }}
              exit={{ opacity: 0, height: 0 }}
            >
              <div className="px-4 py-2 space-y-2">
                {navItems.map((item) => (
                  <a
                    key={item.name}
                    href={item.href}
                    className="block text-white hover:text-blue-400 py-2"
                    onClick={() => setIsMenuOpen(false)}
                  >
                    {item.name}
                  </a>
                ))}
                <button className="w-full cta-button text-white py-2 rounded-lg font-semibold mt-4">
                  Essayer gratuitement
                </button>
              </div>
            </motion.div>
          )}
        </AnimatePresence>
      </motion.nav>

      {/* Hero Section */}
      <section id="hero" className="hero-gradient min-h-screen flex items-center relative overflow-hidden">
        <div className="absolute inset-0 opacity-20">
          <img 
            src="https://images.unsplash.com/photo-1524292691042-82ed9c62673b?crop=entropy&cs=srgb&fm=jpg&ixid=M3w3NDQ2MzR8MHwxfHNlYXJjaHwxfHxwcm9wZXJ0eSUyMG1hbmFnZW1lbnR8ZW58MHx8fGJsdWV8MTc1MzQ2MzEwOXww&ixlib=rb-4.1.0&q=85" 
            alt="Property Management" 
            className="w-full h-full object-cover"
          />
          <div className="absolute inset-0 bg-gradient-to-r from-slate-900/90 to-slate-800/90"></div>
        </div>
        
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 relative z-10">
          <div className="grid lg:grid-cols-2 gap-12 items-center">
            <motion.div
              initial={{ opacity: 0, x: -50 }}
              animate={{ opacity: 1, x: 0 }}
              transition={{ duration: 0.8 }}
            >
              <h1 className="text-4xl md:text-6xl font-bold text-white leading-tight mb-6">
                Simplifiez votre{' '}
                <span className="gradient-text">fiscalité LMNP</span>{' '}
                en toute sérénité
              </h1>
              <p className="text-xl text-slate-300 mb-8 leading-relaxed">
                Zymo automatise votre comptabilité, gère vos locataires et génère votre liasse fiscale. 
                Gagnez du temps et assurez-vous de la conformité fiscale de vos locations meublées.
              </p>
              <div className="flex flex-col sm:flex-row gap-4">
                <motion.button
                  className="cta-button text-white px-8 py-4 rounded-lg font-semibold text-lg flex items-center justify-center"
                  whileHover={{ scale: 1.05 }}
                  whileTap={{ scale: 0.95 }}
                >
                  Essayer gratuitement
                  <ArrowRight className="ml-2" size={20} />
                </motion.button>
                <motion.button
                  className="border-2 border-white text-white px-8 py-4 rounded-lg font-semibold text-lg hover:bg-white hover:text-slate-900 transition-all duration-300"
                  whileHover={{ scale: 1.05 }}
                  whileTap={{ scale: 0.95 }}
                >
                  Voir la démonstration
                </motion.button>
              </div>
              
              {/* Trust indicators */}
              <div className="flex items-center mt-8 space-x-6">
                <div className="flex items-center">
                  <CheckCircle className="text-green-400 mr-2" size={20} />
                  <span className="text-slate-300">Conforme DGFIP</span>
                </div>
                <div className="flex items-center">
                  <CheckCircle className="text-green-400 mr-2" size={20} />
                  <span className="text-slate-300">30 jours gratuits</span>
                </div>
                <div className="flex items-center">
                  <CheckCircle className="text-green-400 mr-2" size={20} />
                  <span className="text-slate-300">Support français</span>
                </div>
              </div>
            </motion.div>
            
            <motion.div
              initial={{ opacity: 0, x: 50 }}
              animate={{ opacity: 1, x: 0 }}
              transition={{ duration: 0.8, delay: 0.2 }}
              className="relative"
            >
              <div className="glass-effect rounded-2xl p-8 shadow-2xl">
                <img 
                  src="https://images.unsplash.com/photo-1659035260002-11d486d6e9f5?crop=entropy&cs=srgb&fm=jpg&ixid=M3w3NTY2Nzh8MHwxfHNlYXJjaHwxfHxzb2Z0d2FyZSUyMGludGVyZmFjZXxlbnwwfHx8Ymx1ZXwxNzUzNTA4ODE1fDA&ixlib=rb-4.1.0&q=85" 
                  alt="Zymo Dashboard" 
                  className="w-full rounded-lg"
                />
                <div className="absolute -bottom-4 -right-4 glass-effect rounded-xl p-4">
                  <div className="flex items-center text-white">
                    <Star className="text-yellow-400 mr-1" size={16} />
                    <span className="font-semibold">4.9/5</span>
                    <span className="text-sm text-slate-300 ml-2">+500 utilisateurs</span>
                  </div>
                </div>
              </div>
            </motion.div>
          </div>
        </div>
      </section>

      {/* Features Section */}
      <section id="features" className="py-20 bg-white">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <motion.div
            className="text-center mb-16"
            initial={{ opacity: 0, y: 30 }}
            whileInView={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.6 }}
            viewport={{ once: true }}
          >
            <h2 className="text-4xl md:text-5xl font-bold text-slate-900 mb-6">
              Tout ce dont vous avez besoin pour votre{' '}
              <span className="gradient-text">LMNP</span>
            </h2>
            <p className="text-xl text-slate-600 max-w-3xl mx-auto">
              Zymo centralise toute la gestion de vos locations meublées non professionnelles 
              dans une interface simple et intuitive.
            </p>
          </motion.div>

          <motion.div
            className="grid md:grid-cols-2 lg:grid-cols-4 gap-8"
            variants={staggerContainer}
            initial="initial"
            whileInView="animate"
            viewport={{ once: true }}
          >
            {[
              {
                icon: <Calculator className="text-blue-500" size={40} />,
                title: "Comptabilité automatisée",
                description: "Enregistrement automatique des revenus et charges. Calcul instantané de vos bénéfices et amortissements.",
                image: "https://images.unsplash.com/photo-1576272531110-2a342fe22342?crop=entropy&cs=srgb&fm=jpg&ixid=M3w3NTY2Nzh8MHwxfHNlYXJjaHwyfHxzb2Z0d2FyZSUyMGludGVyZmFjZXxlbnwwfHx8Ymx1ZXwxNzUzNTA4ODE1fDA&ixlib=rb-4.1.0&q=85"
              },
              {
                icon: <Users className="text-green-500" size={40} />,
                title: "Gestion des locataires",
                description: "Suivi des baux, des paiements et de la communication avec vos locataires en un seul endroit.",
                image: "https://images.unsplash.com/photo-1573496130103-a442a3754d0e?crop=entropy&cs=srgb&fm=jpg&ixid=M3w3NDQ2NDF8MHwxfHNlYXJjaHwxfHxidXNpbmVzcyUyMHByb2Zlc3Npb25hbHxlbnwwfHx8Ymx1ZXwxNzUzNDg5ODA1fDA&ixlib=rb-4.1.0&q=85"
              },
              {
                icon: <FileText className="text-purple-500" size={40} />,
                title: "Documents centralisés",
                description: "Stockage sécurisé de tous vos documents : baux, factures, justificatifs. Tout à portée de clic.",
                image: "https://images.unsplash.com/photo-1664526937033-fe2c11f1be25?crop=entropy&cs=srgb&fm=jpg&ixid=M3w3NTY2Nzh8MHwxfHNlYXJjaHw0fHxzb2Z0d2FyZSUyMGludGVyZmFjZXxlbnwwfHx8Ymx1ZXwxNzUzNTA4ODE1fDA&ixlib=rb-4.1.0&q=85"
              },
              {
                icon: <Shield className="text-red-500" size={40} />,
                title: "Liasse fiscale automatique",
                description: "Génération automatique de votre liasse fiscale. Conformité garantie avec l'administration fiscale.",
                image: "https://images.unsplash.com/photo-1582647509870-c19b2ebb4a8f?crop=entropy&cs=srgb&fm=jpg&ixid=M3w3NDQ2MzR8MHwxfHNlYXJjaHwzfHxwcm9wZXJ0eSUyMG1hbmFnZW1lbnR8ZW58MHx8fGJsdWV8MTc1MzQ2MzEwOXww&ixlib=rb-4.1.0&q=85"
              }
            ].map((feature, index) => (
              <motion.div
                key={index}
                className="feature-card bg-white rounded-2xl p-8 shadow-lg border border-slate-200"
                variants={fadeInUp}
                whileHover={{ scale: 1.02 }}
              >
                <div className="mb-6">
                  <img 
                    src={feature.image} 
                    alt={feature.title} 
                    className="w-full h-48 object-cover rounded-lg mb-4"
                  />
                  {feature.icon}
                </div>
                <h3 className="text-xl font-bold text-slate-900 mb-4">{feature.title}</h3>
                <p className="text-slate-600 leading-relaxed">{feature.description}</p>
              </motion.div>
            ))}
          </motion.div>
        </div>
      </section>

      {/* Testimonials Section */}
      <section className="py-20 bg-slate-50">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <motion.div
            className="text-center mb-16"
            initial={{ opacity: 0, y: 30 }}
            whileInView={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.6 }}
            viewport={{ once: true }}
          >
            <h2 className="text-4xl md:text-5xl font-bold text-slate-900 mb-6">
              Ils nous font confiance
            </h2>
            <p className="text-xl text-slate-600">
              Découvrez comment Zymo a simplifié la gestion LMNP de nos utilisateurs
            </p>
          </motion.div>

          <motion.div
            className="grid md:grid-cols-3 gap-8"
            variants={staggerContainer}
            initial="initial"
            whileInView="animate"
            viewport={{ once: true }}
          >
            {[
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
            ].map((testimonial, index) => (
              <motion.div
                key={index}
                className="bg-white rounded-2xl p-8 shadow-lg border border-slate-200"
                variants={fadeInUp}
                whileHover={{ scale: 1.02 }}
              >
                <div className="flex items-center mb-6">
                  <img 
                    src={testimonial.image} 
                    alt={testimonial.name}
                    className="w-16 h-16 rounded-full object-cover mr-4"
                  />
                  <div>
                    <h4 className="font-bold text-slate-900">{testimonial.name}</h4>
                    <p className="text-slate-600 text-sm">{testimonial.role}</p>
                  </div>
                </div>
                <div className="flex mb-4">
                  {[...Array(testimonial.rating)].map((_, i) => (
                    <Star key={i} className="text-yellow-400 fill-current" size={20} />
                  ))}
                </div>
                <p className="text-slate-700 italic leading-relaxed">"{testimonial.content}"</p>
              </motion.div>
            ))}
          </motion.div>
        </div>
      </section>

      {/* Pricing Section */}
      <section id="pricing" className="py-20 bg-white">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <motion.div
            className="text-center mb-16"
            initial={{ opacity: 0, y: 30 }}
            whileInView={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.6 }}
            viewport={{ once: true }}
          >
            <h2 className="text-4xl md:text-5xl font-bold text-slate-900 mb-6">
              Un tarif simple et transparent
            </h2>
            <p className="text-xl text-slate-600 max-w-3xl mx-auto">
              Choisissez l'offre qui correspond à vos besoins. 
              Toutes nos formules incluent l'essai gratuit de 30 jours.
            </p>
          </motion.div>

          <div className="grid md:grid-cols-3 gap-8 max-w-5xl mx-auto">
            {[
              {
                name: "Starter",
                price: "19",
                description: "Parfait pour débuter",
                features: [
                  "1 bien en location",
                  "Comptabilité de base",
                  "Génération liasse fiscale",
                  "Support par email"
                ],
                recommended: false
              },
              {
                name: "Professional",
                price: "39",
                description: "Le plus populaire",
                features: [
                  "Jusqu'à 5 biens",
                  "Comptabilité complète",
                  "Gestion des locataires",
                  "Stockage documents illimité",
                  "Support prioritaire",
                  "Tableau de bord avancé"
                ],
                recommended: true
              },
              {
                name: "Expert",
                price: "79",
                description: "Pour les gros portefeuilles",
                features: [
                  "Biens illimités",
                  "Toutes les fonctionnalités",
                  "API d'intégration",
                  "Support téléphonique",
                  "Formation personnalisée",
                  "Conseiller fiscal dédié"
                ],
                recommended: false
              }
            ].map((plan, index) => (
              <motion.div
                key={index}
                className={`rounded-2xl p-8 shadow-lg border-2 ${
                  plan.recommended 
                    ? 'border-blue-500 bg-blue-50 relative' 
                    : 'border-slate-200 bg-white'
                }`}
                initial={{ opacity: 0, y: 30 }}
                whileInView={{ opacity: 1, y: 0 }}
                transition={{ duration: 0.6, delay: index * 0.1 }}
                viewport={{ once: true }}
                whileHover={{ scale: 1.02 }}
              >
                {plan.recommended && (
                  <div className="absolute -top-4 left-1/2 transform -translate-x-1/2">
                    <span className="bg-blue-500 text-white px-6 py-2 rounded-full text-sm font-semibold">
                      Recommandé
                    </span>
                  </div>
                )}
                
                <div className="text-center mb-8">
                  <h3 className="text-2xl font-bold text-slate-900 mb-2">{plan.name}</h3>
                  <p className="text-slate-600 mb-4">{plan.description}</p>
                  <div className="flex items-center justify-center">
                    <span className="text-4xl font-bold text-slate-900">{plan.price}€</span>
                    <span className="text-slate-600 ml-2">/mois</span>
                  </div>
                </div>

                <ul className="space-y-4 mb-8">
                  {plan.features.map((feature, featureIndex) => (
                    <li key={featureIndex} className="flex items-center">
                      <CheckCircle className="text-green-500 mr-3 flex-shrink-0" size={20} />
                      <span className="text-slate-700">{feature}</span>
                    </li>
                  ))}
                </ul>

                <motion.button
                  className={`w-full py-4 rounded-lg font-semibold text-lg ${
                    plan.recommended
                      ? 'cta-button text-white'
                      : 'border-2 border-slate-900 text-slate-900 hover:bg-slate-900 hover:text-white transition-all duration-300'
                  }`}
                  whileHover={{ scale: 1.02 }}
                  whileTap={{ scale: 0.98 }}
                >
                  Commencer l'essai gratuit
                </motion.button>
              </motion.div>
            ))}
          </div>

          <motion.div
            className="text-center mt-12"
            initial={{ opacity: 0, y: 30 }}
            whileInView={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.6 }}
            viewport={{ once: true }}
          >
            <p className="text-slate-600 mb-4">
              Tous les tarifs s'entendent hors taxes • Résiliation à tout moment
            </p>
            <p className="text-sm text-slate-500">
              💡 <strong>Astuce :</strong> Les frais d'abonnement Zymo sont déductibles de vos revenus LMNP !
            </p>
          </motion.div>
        </div>
      </section>

      {/* FAQ Section */}
      <section id="faq" className="py-20 bg-slate-50">
        <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8">
          <motion.div
            className="text-center mb-16"
            initial={{ opacity: 0, y: 30 }}
            whileInView={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.6 }}
            viewport={{ once: true }}
          >
            <h2 className="text-4xl md:text-5xl font-bold text-slate-900 mb-6">
              Questions fréquentes
            </h2>
            <p className="text-xl text-slate-600">
              Tout ce que vous devez savoir sur Zymo et la fiscalité LMNP
            </p>
          </motion.div>

          <motion.div
            className="space-y-4"
            variants={staggerContainer}
            initial="initial"
            whileInView="animate"
            viewport={{ once: true }}
          >
            {faqData.map((faq, index) => (
              <motion.div
                key={index}
                className="bg-white rounded-xl shadow-lg border border-slate-200 overflow-hidden"
                variants={fadeInUp}
              >
                <motion.button
                  className="w-full p-6 text-left flex justify-between items-center hover:bg-slate-50 transition-colors duration-200"
                  onClick={() => setOpenFAQ(openFAQ === index ? null : index)}
                  whileHover={{ backgroundColor: "#f8fafc" }}
                >
                  <h3 className="text-lg font-semibold text-slate-900 pr-4">
                    {faq.question}
                  </h3>
                  <motion.div
                    animate={{ rotate: openFAQ === index ? 180 : 0 }}
                    transition={{ duration: 0.3 }}
                  >
                    <ChevronDown className="text-slate-500" size={24} />
                  </motion.div>
                </motion.button>
                <AnimatePresence>
                  {openFAQ === index && (
                    <motion.div
                      initial={{ opacity: 0, height: 0 }}
                      animate={{ opacity: 1, height: 'auto' }}
                      exit={{ opacity: 0, height: 0 }}
                      transition={{ duration: 0.3 }}
                    >
                      <div className="px-6 pb-6 text-slate-700 leading-relaxed">
                        {faq.answer}
                      </div>
                    </motion.div>
                  )}
                </AnimatePresence>
              </motion.div>
            ))}
          </motion.div>

          <motion.div
            className="text-center mt-12"
            initial={{ opacity: 0, y: 30 }}
            whileInView={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.6 }}
            viewport={{ once: true }}
          >
            <p className="text-slate-600 mb-6">
              Vous avez d'autres questions ? Notre équipe est là pour vous aider.
            </p>
            <motion.button
              className="cta-button text-white px-8 py-3 rounded-lg font-semibold"
              whileHover={{ scale: 1.05 }}
              whileTap={{ scale: 0.95 }}
            >
              Contactez notre support
            </motion.button>
          </motion.div>
        </div>
      </section>

      {/* Contact Section */}
      <section id="contact" className="py-20 bg-white">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <motion.div
            className="text-center mb-16"
            initial={{ opacity: 0, y: 30 }}
            whileInView={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.6 }}
            viewport={{ once: true }}
          >
            <h2 className="text-4xl md:text-5xl font-bold text-slate-900 mb-6">
              Prêt à simplifier votre LMNP ?
            </h2>
            <p className="text-xl text-slate-600 max-w-3xl mx-auto">
              Rejoignez des centaines de propriétaires qui font confiance à Zymo 
              pour leur gestion fiscale LMNP. Commencez dès aujourd'hui !
            </p>
          </motion.div>

          <div className="grid lg:grid-cols-2 gap-12 items-center">
            <motion.div
              initial={{ opacity: 0, x: -50 }}
              whileInView={{ opacity: 1, x: 0 }}
              transition={{ duration: 0.8 }}
              viewport={{ once: true }}
            >
              <div className="glass-effect rounded-2xl p-8 shadow-xl">
                <h3 className="text-2xl font-bold text-slate-900 mb-6">
                  Demandez une démonstration personnalisée
                </h3>
                <form className="space-y-6">
                  <div className="grid md:grid-cols-2 gap-4">
                    <div>
                      <label className="block text-sm font-medium text-slate-700 mb-2">
                        Prénom
                      </label>
                      <input
                        type="text"
                        className="w-full px-4 py-3 rounded-lg border border-slate-300 focus:ring-2 focus:ring-blue-500 focus:border-blue-500 transition-colors duration-200"
                        placeholder="Votre prénom"
                      />
                    </div>
                    <div>
                      <label className="block text-sm font-medium text-slate-700 mb-2">
                        Nom
                      </label>
                      <input
                        type="text"
                        className="w-full px-4 py-3 rounded-lg border border-slate-300 focus:ring-2 focus:ring-blue-500 focus:border-blue-500 transition-colors duration-200"
                        placeholder="Votre nom"
                      />
                    </div>
                  </div>
                  <div>
                    <label className="block text-sm font-medium text-slate-700 mb-2">
                      Email professionnel
                    </label>
                    <input
                      type="email"
                      className="w-full px-4 py-3 rounded-lg border border-slate-300 focus:ring-2 focus:ring-blue-500 focus:border-blue-500 transition-colors duration-200"
                      placeholder="votre@email.com"
                    />
                  </div>
                  <div>
                    <label className="block text-sm font-medium text-slate-700 mb-2">
                      Nombre de biens LMNP
                    </label>
                    <select className="w-full px-4 py-3 rounded-lg border border-slate-300 focus:ring-2 focus:ring-blue-500 focus:border-blue-500 transition-colors duration-200">
                      <option>1 bien</option>
                      <option>2-3 biens</option>
                      <option>4-5 biens</option>
                      <option>Plus de 5 biens</option>
                    </select>
                  </div>
                  <div>
                    <label className="block text-sm font-medium text-slate-700 mb-2">
                      Message (optionnel)
                    </label>
                    <textarea
                      rows="4"
                      className="w-full px-4 py-3 rounded-lg border border-slate-300 focus:ring-2 focus:ring-blue-500 focus:border-blue-500 transition-colors duration-200"
                      placeholder="Dites-nous en plus sur vos besoins..."
                    ></textarea>
                  </div>
                  <motion.button
                    type="submit"
                    className="w-full cta-button text-white py-4 rounded-lg font-semibold text-lg"
                    whileHover={{ scale: 1.02 }}
                    whileTap={{ scale: 0.98 }}
                  >
                    Demander ma démonstration
                  </motion.button>
                </form>
              </div>
            </motion.div>

            <motion.div
              initial={{ opacity: 0, x: 50 }}
              whileInView={{ opacity: 1, x: 0 }}
              transition={{ duration: 0.8 }}
              viewport={{ once: true }}
              className="space-y-8"
            >
              <div className="text-center lg:text-left">
                <h3 className="text-2xl font-bold text-slate-900 mb-6">
                  Ou contactez-nous directement
                </h3>
                
                <div className="space-y-6">
                  <div className="flex items-center justify-center lg:justify-start">
                    <div className="bg-blue-100 p-3 rounded-full mr-4">
                      <Mail className="text-blue-600" size={24} />
                    </div>
                    <div>
                      <p className="font-semibold text-slate-900">Email</p>
                      <p className="text-slate-600">contact@zymo.fr</p>
                    </div>
                  </div>
                  
                  <div className="flex items-center justify-center lg:justify-start">
                    <div className="bg-green-100 p-3 rounded-full mr-4">
                      <Phone className="text-green-600" size={24} />
                    </div>
                    <div>
                      <p className="font-semibold text-slate-900">Téléphone</p>
                      <p className="text-slate-600">01 23 45 67 89</p>
                      <p className="text-sm text-slate-500">Lun-Ven 9h-18h</p>
                    </div>
                  </div>
                  
                  <div className="flex items-center justify-center lg:justify-start">
                    <div className="bg-purple-100 p-3 rounded-full mr-4">
                      <MapPin className="text-purple-600" size={24} />
                    </div>
                    <div>
                      <p className="font-semibold text-slate-900">Adresse</p>
                      <p className="text-slate-600">123 Avenue des Champs-Élysées</p>
                      <p className="text-slate-600">75008 Paris, France</p>
                    </div>
                  </div>
                </div>

                <div className="mt-8 p-6 bg-green-50 rounded-xl border border-green-200">
                  <div className="flex items-center justify-center lg:justify-start mb-4">
                    <CheckCircle className="text-green-500 mr-2" size={24} />
                    <span className="font-semibold text-green-800">Support français garanti</span>
                  </div>
                  <p className="text-green-700 text-sm">
                    Notre équipe support basée en France vous répond en moins de 4 heures 
                    ouvrées. Expertise fiscale LMNP incluse.
                  </p>
                </div>
              </div>
            </motion.div>
          </div>
        </div>
      </section>

      {/* Footer */}
      <footer className="bg-slate-900 text-white py-16">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="grid md:grid-cols-4 gap-8 mb-8">
            <div>
              <div className="text-2xl font-bold gradient-text mb-4">Zymo</div>
              <p className="text-slate-400 mb-6">
                La solution de référence pour simplifier votre fiscalité LMNP 
                et gérer vos locations meublées en toute sérénité.
              </p>
              <div className="flex space-x-4">
                {/* Social media links would go here */}
              </div>
            </div>
            
            <div>
              <h4 className="font-semibold mb-4">Produit</h4>
              <ul className="space-y-2 text-slate-400">
                <li><a href="#features" className="hover:text-white transition-colors duration-200">Fonctionnalités</a></li>
                <li><a href="#pricing" className="hover:text-white transition-colors duration-200">Tarifs</a></li>
                <li><a href="#" className="hover:text-white transition-colors duration-200">Sécurité</a></li>
                <li><a href="#" className="hover:text-white transition-colors duration-200">Intégrations</a></li>
              </ul>
            </div>
            
            <div>
              <h4 className="font-semibold mb-4">Ressources</h4>
              <ul className="space-y-2 text-slate-400">
                <li><a href="#" className="hover:text-white transition-colors duration-200">Guide LMNP</a></li>
                <li><a href="#" className="hover:text-white transition-colors duration-200">Centre d'aide</a></li>
                <li><a href="#" className="hover:text-white transition-colors duration-200">Blog</a></li>
                <li><a href="#" className="hover:text-white transition-colors duration-200">Webinaires</a></li>
              </ul>
            </div>
            
            <div>
              <h4 className="font-semibold mb-4">Entreprise</h4>
              <ul className="space-y-2 text-slate-400">
                <li><a href="#" className="hover:text-white transition-colors duration-200">À propos</a></li>
                <li><a href="#contact" className="hover:text-white transition-colors duration-200">Contact</a></li>
                <li><a href="#" className="hover:text-white transition-colors duration-200">Carrières</a></li>
                <li><a href="#" className="hover:text-white transition-colors duration-200">Presse</a></li>
              </ul>
            </div>
          </div>
          
          <div className="border-t border-slate-800 pt-8 flex flex-col md:flex-row justify-between items-center">
            <p className="text-slate-400 text-sm">
              © 2025 Zymo. Tous droits réservés.
            </p>
            <div className="flex space-x-6 mt-4 md:mt-0">
              <a href="#" className="text-slate-400 hover:text-white text-sm transition-colors duration-200">
                Mentions légales
              </a>
              <a href="#" className="text-slate-400 hover:text-white text-sm transition-colors duration-200">
                Politique de confidentialité
              </a>
              <a href="#" className="text-slate-400 hover:text-white text-sm transition-colors duration-200">
                CGU
              </a>
            </div>
          </div>
        </div>
      </footer>

      {/* Floating CTA Button */}
      <motion.div
        className="fixed bottom-8 right-8 z-40"
        initial={{ opacity: 0, scale: 0 }}
        animate={{ opacity: 1, scale: 1 }}
        transition={{ delay: 2, duration: 0.5 }}
      >
        <motion.button
          className="cta-button text-white px-6 py-3 rounded-full font-semibold shadow-2xl flex items-center"
          whileHover={{ scale: 1.05 }}
          whileTap={{ scale: 0.95 }}
        >
          <span className="mr-2">Créer mon compte</span>
          <ArrowRight size={20} />
        </motion.button>
      </motion.div>
    </div>
  );
};

export default LandingPage;