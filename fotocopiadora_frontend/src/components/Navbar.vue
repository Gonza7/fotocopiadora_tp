<template>
    <v-app-bar app dark>
      <v-app-bar-nav-icon @click="drawer = !drawer" />
      <v-toolbar-title>Fotocopiadora</v-toolbar-title>

      <v-spacer></v-spacer>

      <!-- AVATAR Y MENU USUARIO -->
      <v-menu bottom left>
        <template #activator="{ props }">
          <v-btn v-bind="props" icon>
            <v-avatar color="profile">
              <span class="white--text">{{ inicialUsuario }}</span>
            </v-avatar>
          </v-btn>
        </template>
        <v-list>
          <v-list-item :class="userItemClass">
            <v-list-item-title class="font-weight-bold">
                {{ usuario }}
            </v-list-item-title>
          </v-list-item>
          <v-list-item @click="toggleTheme" prepend-icon="mdi-theme-light-dark">
            <v-list-item-title>
                {{ isDark ? 'Modo claro' : 'Modo oscuro' }}
            </v-list-item-title>
          </v-list-item>
          <v-list-item @click="logout" prepend-icon="mdi-logout">
            <v-list-item-title>Cerrar sesión</v-list-item-title>
          </v-list-item>
        </v-list>
      </v-menu>
    </v-app-bar>

    <!-- SIDEBAR -->
    <v-navigation-drawer app v-model="drawer" temporary>
      <v-list dense nav>
        <v-list-item
          v-for="item in menuItems"
          :key="item.title"
          :to="item.to"
          router
        >
          <v-list-item-icon>
            <v-icon>{{ item.icon }}</v-icon>
          </v-list-item-icon>
          <v-list-item-title>{{ item.title }}</v-list-item-title>
        </v-list-item>
      </v-list>
    </v-navigation-drawer>

</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useUsuarioStore } from '@/stores/usuarioStore'
import { useTheme } from 'vuetify'

const router = useRouter()
const drawer = ref(false)

const store = useUsuarioStore()

// Nombre del usuario desde el store
const usuario = computed(() => store.nombre || 'Usuario')

// Inicial (primera letra del nombre)
const inicialUsuario = computed(() =>
  store.nombre ? store.nombre.charAt(0).toUpperCase() : '?'
)

// Cerrar sesión
const logout = () => {
  store.clear()
  router.push('/')
}

// Menú lateral
const menuItems = [
  { title: 'Inicio', icon: 'mdi-home', to: '/home' },
  { title: 'Ventas', icon: 'mdi-cash-register', to: '/ventas' },
  { title: 'Compras', icon: 'mdi-cart', to: '/compras' },
  { title: 'Productos', icon: 'mdi-package-variant', to: '/productos' },
  { title: 'Usuarios', icon: 'mdi-account', to: '/usuarios'},
]

const theme = useTheme()

const userItemClass = computed(() =>
  theme.global.current.value.dark ? 'bg-grey-darken-3' : 'bg-grey-lighten-3'
)

const toggleTheme = () => {
  theme.global.name.value = theme.global.current.value.dark ? 'light' : 'dark'
}

const isDark = computed(() => theme.global.current.value.dark)
</script>