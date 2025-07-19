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
    const response = await fetch(`${import.meta.env.VITE_SERVICE_BASE_URL}/auth/register`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload),
    })
    if (!response.ok) {
      const error = await response.text()
      throw new Error(error)
    }
    // Inscription réussie, on tente la connexion automatique
    try {
      await authStore.login(model.login, model.password)
      window.$message?.success($t('page.login.common.validateSuccess'))
      window.location.href = '/'
    } catch {
      // Erreur de connexion automatique, on affiche un message mais on ne l'utilise pas
      window.$message?.error('Inscription réussie, mais connexion impossible. Veuillez vous connecter manuellement.')
      window.location.href = '/login'
    }
  }
  catch (e: any) {
    window.$message?.error(e.message || 'Erreur lors de l\'inscription')
  }
  finally {
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
    @keyup.enter="handleSubmit"
  >
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
      <NButton type="primary" size="large" round block @click="handleSubmit">
        {{ $t('common.confirm') }}
      </NButton>
      <NButton type="primary" size="large" round block @click="goBackToLogin">
        {{ $t('page.login.common.back') }}
      </NButton>
    </NSpace>
  </NForm>
</template>

<style scoped></style>
