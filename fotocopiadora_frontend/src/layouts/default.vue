<template>
  <Navbar v-if="showNav"/>
  <v-main>
    <router-view />
  </v-main>

  <AppFooter />
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'
const route = useRoute()

  const showNav = computed(() => {
  // Oculta navbar/sidebar en login y signup, por ejemplo
  return !['/', '/signup'].includes(route.path)
})
</script>
