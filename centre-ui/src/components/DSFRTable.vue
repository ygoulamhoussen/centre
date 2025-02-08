<template>
  <div>
    <div class="button-container">
      <button @click="openAddForm" class="fr-btn fr-btn--primary">Ajouter</button>
    </div>
    <div class="fr-table fr-table--bordered">
      <div class="fr-table__wrapper">
        <div class="fr-table__container">
          <div class="fr-table__content">
            <table>
              <caption>{{ title }}</caption>
              <thead>
                <tr>
                  <th v-for="(header, index) in headers" :key="index" scope="col">
                    {{ header }}
                  </th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="(row, rowIndex) in rows" :key="rowIndex">
                  <td v-for="(header, cellIndex) in headers" :key="cellIndex">
                    <span v-if="header !== 'Actions'">{{ row[header] }}</span>
                    <span v-else>
                      <button @click="openEditForm(row)" class="fr-btn fr-btn--secondary">Modifier</button>
                      <button @click="openDeleteForm(row.id)" class="fr-btn fr-btn--tertiary">Supprimer</button>
                    </span>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>

    <div v-if="showForm" class="form-container">
      <h2>{{ formTitle }}</h2>
      <form @submit.prevent="submitForm">
        <div v-for="(header, index) in headers" :key="index" v-if="header !== 'Actions'" class="form-group">
          <label :for="header">{{ header }}</label>
          <input v-if="header !== 'Description'" type="text" :id="header" v-model="form[header]" required />
          <textarea v-else :id="header" v-model="form[header]" required></textarea>
        </div>
        <div class="form-actions">
          <button type="submit" class="fr-btn fr-btn--primary">Enregistrer</button>
          <button type="button" @click="closeForm" class="fr-btn fr-btn--secondary">Annuler</button>
        </div>
      </form>
    </div>
  </div>
</template>

<script>
export default {
  name: 'DSFRTable',
  props: {
    title: {
      type: String,
      required: true
    },
    headers: {
      type: Array,
      required: true
    },
    rows: {
      type: Array,
      required: true
    }
  },
  data() {
    return {
      showForm: false,
      formTitle: '',
      form: {},
      currentRowIndex: null
    };
  },
  methods: {
    openAddForm() {
      this.formTitle = 'Ajouter';
      this.form = {};
      this.headers.forEach(header => {
        if (header !== 'Actions') {
          this.form[header] = '';
        }
      });
      this.showForm = true;
      this.$nextTick(() => {
        document.querySelector('input, textarea').focus();
        document.querySelector('input, textarea').scrollIntoView({ behavior: 'smooth', block: 'center' });
      });
    },
    openEditForm(row) {
      this.formTitle = 'Modifier';
      this.form = { ...row };
      this.showForm = true;
      this.$nextTick(() => {
        document.querySelector('input, textarea').focus();
        document.querySelector('input, textarea').scrollIntoView({ behavior: 'smooth', block: 'center' });
      });
    },
    openDeleteForm(id) {
      if (confirm('Êtes-vous sûr de vouloir supprimer cet élément ?')) {
        this.$emit('delete', id);
      }
    },
    closeForm() {
      this.showForm = false;
    },
    submitForm() {
      if (this.form.id === undefined) {
        this.$emit('add', this.form);
      } else {
        this.$emit('edit', this.form.id, this.form);
      }
      this.closeForm();
    }
  }
};
</script>

<style scoped>
.button-container {
  text-align: right;
  margin-bottom: 10px;
}

.form-group {
  margin-bottom: 15px;
}

.form-group label {
  display: block;
  margin-bottom: 5px;
}

.form-group input,
.form-group textarea {
  width: 100%;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 4px;
  box-sizing: border-box;
}

.form-actions {
  text-align: left;
}
</style>
