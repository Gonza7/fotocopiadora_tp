<template>
  <v-container class="mt-8">
    <v-row justify="center" align="center" dense>
      <v-col
        cols="12"
        md="6"
        v-for="item in botones"
        :key="item.titulo"
      >
        <v-btn
          block
          large
          class="py-9 text-uppercase font-weight-bold"
          @click="$router.push(item.ruta)"
        >
          <v-icon start size="28" class="mr-2">{{ item.icono }}</v-icon>
          {{ item.titulo }}
        </v-btn>
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup>
const botones = [
  { titulo: 'Ventas', ruta: '/ventas', icono: 'mdi-cash-register' },
  { titulo: 'Compras', ruta: '/compras', icono: 'mdi-cart' },
  { titulo: 'Productos', ruta: '/productos', icono: 'mdi-package-variant' },
  { titulo: 'Usuarios', ruta: '/usuarios', icono: 'mdi-account' },
]
</script>
