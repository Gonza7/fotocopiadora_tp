<template>
  <v-app>
    <v-main>
      <UsuarioList />
    </v-main>
  </v-app>
</template>

<script>
import UsuarioList from '@/components/usuario/UsuarioList.vue';

export default {
  name: 'App',
  components: {
    UsuarioList,
  },
};
</script>