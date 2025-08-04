<template>
  <v-app>
    <v-main>
      <CompraList />
    </v-main>
  </v-app>
</template>

<script>
import CompraList from '@/components/compra/CompraList.vue';

export default {
  name: 'App',
  components: {
    CompraList,
  },
};
</script>