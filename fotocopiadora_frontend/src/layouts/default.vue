<template>
  <v-app>
    <Navbar v-if="showNav"/>
    <v-main>
      <router-view />
    </v-main>
    <AppFooter />
  </v-app>
  
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'
const route = useRoute()

  const showNav = computed(() => {
  return !['/', '/signup'].includes(route.path)
})
</script>
