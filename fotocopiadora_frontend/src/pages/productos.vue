<template>
  <v-app>
    <v-main>
      <ProductoList />
    </v-main>
  </v-app>
</template>

<script>
import ProductoList from '@/components/producto/ProductoList.vue';

export default {
  name: 'App',
  components: {
    ProductoList,
  },
};
</script>