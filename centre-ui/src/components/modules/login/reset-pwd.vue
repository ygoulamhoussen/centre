<script setup lang="ts">
import { ref, reactive, computed } from 'vue'
import { useFormRules, useNaiveForm } from '@/hooks/common/form'
import { useRouterPush } from '@/hooks/common/router'
import { $t } from '@/locales'

defineOptions({
  name: 'ResetPwd',
})

const { toggleLoginModule } = useRouterPush()
const { formRef, validate } = useNaiveForm()

interface FormModel {
  userName: string
  code: string
  password: string
  confirmPassword: string
}

const model: FormModel = reactive({
  userName: '',
  code: '',
  password: '',
  confirmPassword: '',
})

const rules = computed(() => {
  const { formRules, createConfirmPwdRule } = useFormRules()
  return {
    userName: [{ required: true, message: $t('page.login.common.userNamePlaceholder'), trigger: 'blur' }],
    code: [{ required: true, message: $t('page.login.common.codePlaceholder'), trigger: 'blur' }],
    password: formRules.pwd,
    confirmPassword: createConfirmPwdRule(model.password),
  }
})

let emailForReset = ''

const sending = ref(false)
const sendSuccess = ref(false)
const resetLoading = ref(false)

async function sendResetCode() {
  if (!model.userName) {
    window.$message?.error($t('page.login.common.userNamePlaceholder'))
    return
  }
  sending.value = true
  sendSuccess.value = false
  try {
    // 1. Récupérer l'email associé au userName
    const resp = await fetch(`${import.meta.env.VITE_SERVICE_BASE_URL}/auth/getEmailByUserName`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ userName: model.userName }),
    })
    if (!resp.ok) throw new Error(await resp.text())
    const data = await resp.json()
    if (!data.email) throw new Error('Utilisateur non trouvé')
    emailForReset = data.email
    // 2. Envoyer le code à cet email
    const response = await fetch(`${import.meta.env.VITE_SERVICE_BASE_URL}/auth/send-reset-code`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ email: emailForReset }),
    })
    if (!response.ok) throw new Error(await response.text())
    sendSuccess.value = true
    window.$message?.success('Code envoyé par email')
  } catch (e: any) {
    window.$message?.error(e.message || 'Erreur lors de l’envoi du code')
  } finally {
    sending.value = false
  }
}

async function handleSubmit() {
  await validate()
  resetLoading.value = true
  try {
    if (!emailForReset) {
      window.$message?.error('Veuillez d’abord envoyer le code à votre email')
      return
    }
    const response = await fetch(`${import.meta.env.VITE_SERVICE_BASE_URL}/auth/reset-password`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        email: emailForReset,
        code: model.code,
        password: model.password,
      }),
    })
    if (!response.ok) throw new Error(await response.text())
    window.$message?.success($t('page.login.common.validateSuccess'))
    toggleLoginModule('pwd-login')
  } catch (e: any) {
    window.$message?.error(e.message || 'Erreur lors de la réinitialisation du mot de passe')
  } finally {
    resetLoading.value = false
  }
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
    <NFormItem path="userName">
      <NInput v-model:value="model.userName" :placeholder="$t('page.login.common.userNamePlaceholder')" />
      <NButton size="small" :loading="sending" @click="sendResetCode" class="ml-2">
        {{ $t('page.login.codeLogin.getCode') }}
      </NButton>
      <span v-if="sendSuccess" class="text-success text-12px ml-2">{{ $t('page.login.codeLogin.sendCodeSuccess') }}</span>
    </NFormItem>
    <NFormItem path="code">
      <NInput v-model:value="model.code" :placeholder="$t('page.login.common.codePlaceholder')" />
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
    <NSpace vertical :size="18" class="w-full">
      <NButton type="primary" size="large" round block :loading="resetLoading" @click="handleSubmit">
        {{ $t('page.login.common.confirm') }}
      </NButton>
      <NButton size="large" round block @click="toggleLoginModule('pwd-login')">
        {{ $t('page.login.common.back') }}
      </NButton>
    </NSpace>
  </NForm>
</template>

<style scoped></style>
