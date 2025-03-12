<template>
  <div class="modal-overlay">
    <div class="modal-content">
      <h2>{{ title }}</h2>
      <form @submit.prevent="onSubmit">
        <div class="form-group">
          <label for="nom">Nom</label>
          <input type="text" id="nom" v-model="localForm.nom" required />
        </div>
        <div class="form-group">
          <label for="email">Email</label>
          <input type="email" id="email" v-model="localForm.email" required />
        </div>
        <div class="form-actions">
          <button type="submit" class="fr-btn fr-btn--primary">Enregistrer</button>
          <button type="button" @click="$emit('cancel')" class="fr-btn fr-btn--secondary">Annuler</button>
        </div>
      </form>
    </div>
  </div>
</template>

<script>
export default {
  name: 'FormateurFormModal',
  props: {
    title: { type: String, required: true },
    form: { type: Object, required: true }
  },
  data() {
    return {
      localForm: { ...this.form }
    };
  },
  methods: {
    onSubmit(e) {
      // Ignore the native event and emit only the copied form data
      this.$emit('submit', { ...this.localForm });
    }
  },
  watch: {
    form: {
      handler(newVal) {
        this.localForm = { ...newVal };
      },
      deep: true,
      immediate: true
    }
  }
};
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
}
.modal-content {
  background: #fff;
  padding: 20px;
  border-radius: 4px;
  width: 90%;
  max-width: 500px;
}
.form-group {
  margin-bottom: 15px;
}
.form-group label {
  display: block;
  margin-bottom: 5px;
}
.form-group input {
  width: 100%;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 4px;
  box-sizing: border-box;
}
.form-actions {
  display: flex;
  gap: 10px;
  margin-top: 15px;
}
</style>
