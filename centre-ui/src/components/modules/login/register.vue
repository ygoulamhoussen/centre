<script setup lang="ts">
import { useCaptcha } from '@/hooks/business/captcha'
import { useFormRules, useNaiveForm } from '@/hooks/common/form'
import { $t } from '@/locales'
import { useAuthStore } from '@/store/modules/auth'
import { computed, reactive, ref } from 'vue'

defineOptions({
  name: 'Register',
})

const { formRef, validate } = useNaiveForm()

interface FormModel {
  login: string
  email: string
  password: string
  confirmPassword: string
}

const model: FormModel = reactive({
  login: '',
  email: '',
  password: '',
  confirmPassword: '',
})

const rules = computed<Record<keyof FormModel, App.Global.FormRule[]>>(() => {
  const { formRules, createConfirmPwdRule } = useFormRules()

  return {
    login: [
      { required: true, message: 'Le nom d’utilisateur est obligatoire', trigger: 'blur' },
      { min: 3, max: 32, message: '3 à 32 caractères', trigger: 'blur' },
    ],
    email: formRules.email,
    password: formRules.pwd,
    confirmPassword: createConfirmPwdRule(model.password),
  }
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

const step = ref(1)
const code = ref('')

const registerLoading = ref(false)

const authStore = useAuthStore()

async function handleSubmit() {
  await validate()
  registerLoading.value = true
  try {
    // Construction du payload
    const payload = {
      userName: model.login,
      password: model.password,
      email: model.email,
      nom: capitalModel.nomExploitant,
      prenom: capitalModel.prenomExploitant,
      capitalIdentites: {
        ...capitalModel,
      },
    }
    // 1ère étape : envoi du code
    const response = await fetch(`${import.meta.env.VITE_SERVICE_BASE_URL}/auth/start-registration`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload),
    })
    if (!response.ok) {
      const error = await response.text()
      throw new Error(error)
    }
    window.$message?.success('Code envoyé à votre email')
    step.value = 2
  }
  catch (e: any) {
    window.$message?.error(e.message || 'Erreur lors de l\'inscription')
  }
  finally {
    registerLoading.value = false
  }
}

async function handleCodeValidation() {
  if (!code.value) {
    window.$message?.error('Veuillez saisir le code reçu par email')
    return
  }
  registerLoading.value = true
  try {
    const response = await fetch(`${import.meta.env.VITE_SERVICE_BASE_URL}/auth/confirm-registration`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ email: model.email, code: code.value }),
    })
    if (!response.ok) {
      const error = await response.text()
      throw new Error(error)
    }
    // Connexion automatique après validation
    try {
      await authStore.login(model.login, model.password)
      window.$message?.success($t('page.login.common.validateSuccess'))
      window.location.href = '/'
    } catch {
      window.$message?.error('Inscription validée, mais connexion impossible. Veuillez vous connecter manuellement.')
      goBackToLogin()
    }
  } catch (e: any) {
    window.$message?.error(e.message || 'Erreur lors de la validation du code')
  } finally {
    registerLoading.value = false
  }
}

function goBackToLogin() {
  window.location.href = '/login'
}
</script>

<template>
  <NForm
    ref="formRef"
    :model="model"
    :rules="rules"
    size="large"
    :show-label="false"
    @keyup.enter="step === 1 ? handleSubmit() : handleCodeValidation()"
  >
    <template v-if="step === 1">
      <NFormItem path="login">
        <NInput v-model:value="model.login" placeholder="Nom d'utilisateur" />
      </NFormItem>
      <NFormItem path="email">
        <NInput v-model:value="model.email" :placeholder="$t('page.login.common.emailPlaceholder')" />
      </NFormItem>
      <NFormItem path="password">
        <NInput
          v-model:value="model.password"
          type="password"
          show-password-on="click"
          :placeholder="$t('page.login.common.passwordPlaceholder')"
        />
      </NFormItem>
      <NFormItem path="confirmPassword">
        <NInput
          v-model:value="model.confirmPassword"
          type="password"
          show-password-on="click"
          :placeholder="$t('page.login.common.confirmPasswordPlaceholder')"
        />
      </NFormItem>
      <!-- Champs capital-identites (nom, prénom, etc.) à placer ici -->
      <NSpace vertical :size="18" class="w-full">
        <NButton type="primary" size="large" round block :loading="registerLoading" @click="handleSubmit">
          {{ $t('common.confirm') }}
        </NButton>
        <NButton type="primary" size="large" round block @click="goBackToLogin">
          {{ $t('page.login.common.back') }}
        </NButton>
      </NSpace>
    </template>
    <template v-else>
      <NFormItem>
        <NInput v-model:value="code" placeholder="Code reçu par email" />
      </NFormItem>
      <NSpace vertical :size="18" class="w-full">
        <NButton type="primary" size="large" round block :loading="registerLoading" @click="handleCodeValidation">
          Valider le code
        </NButton>
        <NButton type="primary" size="large" round block @click="goBackToLogin">
          {{ $t('page.login.common.back') }}
        </NButton>
      </NSpace>
    </template>
  </NForm>
</template>

<style scoped></style>
