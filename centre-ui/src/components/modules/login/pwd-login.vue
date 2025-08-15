<script setup lang="ts">
import { useFormRules, useNaiveForm } from '@/hooks/common/form'
import { useRouterPush } from '@/hooks/common/router'
import { $t } from '@/locales'
import { useAuthStore } from '@/store/modules/auth'

defineOptions({
  name: 'PwdLogin',
})

const authStore = useAuthStore()
const { toggleLoginModule } = useRouterPush()
const { formRef, validate } = useNaiveForm()

interface FormModel {
  userName: string
  password: string
}

const model: FormModel = reactive({
  userName: '',
  password: '',
})

// Récupérer le paramètre username de l'URL et le préremplir
onMounted(() => {
  const urlParams = new URLSearchParams(window.location.search)
  const username = urlParams.get('username')
  if (username) {
    model.userName = decodeURIComponent(username)
  }
})

const rules = computed<Record<keyof FormModel, App.Global.FormRule[]>>(() => {
  // inside computed to make locale reactive, if not apply i18n, you can define it without computed
  const { formRules } = useFormRules()

  return {
    userName: formRules.userName,
    password: formRules.pwd,
  }
})

async function handleSubmit() {
  await validate()
  await authStore.login(model.userName, model.password)
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
      <NInput
        v-model:value="model.userName"
        :placeholder="$t('page.login.common.userNamePlaceholder')"
      />
    </NFormItem>
    <NFormItem path="password">
      <NInput
        v-model:value="model.password"
        type="password"
        show-password-on="click"
        :placeholder="$t('page.login.common.passwordPlaceholder')"
      />
    </NFormItem>
    <NSpace vertical :size="24">
      <div class="flex-y-center justify-between">
        <NCheckbox>{{ $t('page.login.pwdLogin.rememberMe') }}</NCheckbox>
        <NButton quaternary @click="toggleLoginModule('reset-pwd')">
          {{ $t('page.login.pwdLogin.forgetPassword') }}
        </NButton>
      </div>
      <NButton
        type="primary"
        size="large"
        round
        block
        :loading="authStore.loginLoading"
        @click="handleSubmit"
      >
        {{ $t('common.confirm') }}
      </NButton>
      <div class="flex-y-center justify-between gap-12px">
        <RouterLink to="/landing" class="flex-1">
          <NButton block quaternary>
            {{ $t('common.back') }}
          </NButton>
        </RouterLink>
      </div>
    </NSpace>
  </NForm>
</template>

<style scoped></style>
