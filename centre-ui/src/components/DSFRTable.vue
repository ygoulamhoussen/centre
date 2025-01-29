<template>
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
                <td v-for="(cell, cellIndex) in row" :key="cellIndex" v-html="cell">
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
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
  }
};
</script>

<style scoped>
/* Add any scoped styles if necessary */
</style>
