<script setup lang="ts">
import { useRouter } from 'vue-router'
import { CalculatorOutline, ScaleOutline, ConstructOutline, InformationCircleOutline, PersonOutline, PieChartOutline, WarningOutline } from '@vicons/ionicons5'

const router = useRouter()

definePage({
  meta: {
    title: 'Liasse Fiscale',
    icon: 'material-symbols:account-balance-outline',
    order: 19,
  },
})

</script>

<template>
  <div class="comptabilite-page">
    <n-card title="📊 Liasse fiscale LMNP" class="mb-4">
      <div class="text-gray-600 mb-6">
        <p>Accédez à tous les modules de la liasse fiscale LMNP :</p>
      </div>

      <n-grid :cols="7" :x-gap="16" :y-gap="16">
        <!-- Résultat Fiscal -->
        <n-grid-item>
          <n-card 
            title="📄 Formulaire 2033-B" 
            subtitle="Résultat fiscal LMNP"
            class="module-card"
            @click="router.push('/resultat-fiscal')"
          >
            <template #header-extra>
              <n-icon size="24" class="text-blue-500">
                <CalculatorOutline />
              </n-icon>
            </template>
            <div class="space-y-2">
              <p class="text-sm text-gray-600">Calcul du résultat fiscal de l'exercice</p>
              <ul class="text-xs text-gray-500 space-y-1">
                <li>• Recettes locatives</li>
                <li>• Charges déductibles</li>
                <li>• Résultat imposable</li>
              </ul>
            </div>
          </n-card>
        </n-grid-item>

        <!-- Bilan Simplifié -->
        <n-grid-item>
          <n-card 
            title="📄 Formulaire 2033-A" 
            subtitle="Bilan simplifié LMNP"
            class="module-card"
            @click="router.push('/bilan-simplifie')"
          >
            <template #header-extra>
              <n-icon size="24" class="text-green-500">
                <ScaleOutline />
              </n-icon>
            </template>
            <div class="space-y-2">
              <p class="text-sm text-gray-600">État du patrimoine à la clôture</p>
              <ul class="text-xs text-gray-500 space-y-1">
                <li>• Actif (immobilisations, trésorerie)</li>
                <li>• Passif (emprunts, capitaux)</li>
                <li>• Équilibre du bilan</li>
              </ul>
            </div>
          </n-card>
        </n-grid-item>

        <!-- Suivi Immobilisations -->
        <n-grid-item>
          <n-card 
            title="📄 Formulaire 2033-C" 
            subtitle="Suivi immobilisations"
            class="module-card"
            @click="router.push('/suivi-immobilisations')"
          >
            <template #header-extra>
              <n-icon size="24" class="text-purple-500">
                <ConstructOutline />
              </n-icon>
            </template>
            <div class="space-y-2">
              <p class="text-sm text-gray-600">Suivi des immobilisations et amortissements</p>
              <ul class="text-xs text-gray-500 space-y-1">
                <li>• Détail par immobilisation</li>
                <li>• Amortissements cumulés</li>
                <li>• Valeur nette comptable</li>
              </ul>
            </div>
          </n-card>
        </n-grid-item>

        <!-- Capital et Identités -->
        <n-grid-item>
          <n-card 
            title="📄 Formulaire 2033-D" 
            subtitle="Capital et identités"
            class="module-card"
            @click="router.push('/capital-identites')"
          >
            <template #header-extra>
              <n-icon size="24" class="text-orange-500">
                <PersonOutline />
              </n-icon>
            </template>
            <div class="space-y-2">
              <p class="text-sm text-gray-600">Renseignements sur l'exploitant</p>
              <ul class="text-xs text-gray-500 space-y-1">
                <li>• Identité de l'exploitant</li>
                <li>• Apports au capital</li>
                <li>• Qualité et parts</li>
              </ul>
            </div>
          </n-card>
        </n-grid-item>

        <!-- Répartition des Charges -->
        <n-grid-item>
          <n-card 
            title="📄 Formulaire 2033-E" 
            subtitle="Répartition des charges"
            class="module-card"
            @click="router.push('/repartition-charges')"
          >
            <template #header-extra>
              <n-icon size="24" class="text-red-500">
                <PieChartOutline />
              </n-icon>
            </template>
            <div class="space-y-2">
              <p class="text-sm text-gray-600">Ventilation des charges par nature</p>
              <ul class="text-xs text-gray-500 space-y-1">
                <li>• Achats et fournitures</li>
                <li>• Charges externes</li>
                <li>• Impôts et taxes</li>
                <li>• Amortissements</li>
              </ul>
            </div>
          </n-card>
        </n-grid-item>

        <!-- Produits et Charges Exceptionnels -->
        <n-grid-item>
          <n-card 
            title="📄 Formulaire 2033-F" 
            subtitle="Produits et charges exceptionnels"
            class="module-card"
            @click="router.push('/produits-charges-exceptionnels')"
          >
            <template #header-extra>
              <n-icon size="24" class="text-yellow-500">
                <WarningOutline />
              </n-icon>
            </template>
            <div class="space-y-2">
              <p class="text-sm text-gray-600">Opérations non courantes</p>
              <ul class="text-xs text-gray-500 space-y-1">
                <li>• Produits exceptionnels</li>
                <li>• Charges exceptionnelles</li>
                <li>• Plus/moins-values</li>
                <li>• Ventes de biens</li>
              </ul>
            </div>
          </n-card>
        </n-grid-item>

        <!-- Soldes Intermédiaires de Gestion -->
        <n-grid-item>
          <n-card 
            title="📄 Formulaire 2033-G" 
            subtitle="Soldes intermédiaires de gestion"
            class="module-card"
            @click="router.push('/soldes-intermediaires-gestion')"
          >
            <template #header-extra>
              <n-icon size="24" class="text-indigo-500">
                <CalculatorOutline />
              </n-icon>
            </template>
            <div class="space-y-2">
              <p class="text-sm text-gray-600">Analyse de la rentabilité</p>
              <ul class="text-xs text-gray-500 space-y-1">
                <li>• Production et consommation</li>
                <li>• Valeur ajoutée et EBE</li>
                <li>• Résultats par étapes</li>
                <li>• Calculs automatiques</li>
              </ul>
            </div>
          </n-card>
        </n-grid-item>
      </n-grid>

      <!-- Informations complémentaires -->
      <n-card title="ℹ️ Informations" size="small" class="mt-6">
        <n-alert type="info">
          <template #icon>
            <n-icon><InformationCircleOutline /></n-icon>
          </template>
          <div class="space-y-2">
            <p><strong>LMNP (Loueur Meublé Non Professionnel) :</strong></p>
            <ul class="list-disc list-inside space-y-1 text-sm">
              <li>Régime fiscal pour les propriétaires louant des biens meublés</li>
              <li>Obligation de tenir une comptabilité complète</li>
              <li>Déclaration annuelle avec les formulaires 2033-A, 2033-B, 2033-C, 2033-D, 2033-E, 2033-F et 2033-G</li>
              <li>Amortissement des immobilisations sur leur durée d'usage</li>
            </ul>
          </div>
        </n-alert>
      </n-card>
    </n-card>
  </div>
</template>

<style scoped>
.comptabilite-page {
  padding: 20px;
}

.module-card {
  cursor: pointer;
  transition: all 0.3s ease;
  height: 100%;
}

.module-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
}

.module-card .n-card-header {
  border-bottom: 1px solid #f0f0f0;
}

.module-card .n-card__content {
  padding: 16px;
}
</style> 