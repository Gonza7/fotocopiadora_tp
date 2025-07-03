<template>
  <v-container class="fill-height d-flex justify-center align-center">
    <v-card width="400">
      <v-card-title class="text-h6">Iniciar Sesión</v-card-title>
      <v-card-text>
        <form @submit.prevent="login">
        <v-text-field label="Usuario" v-model="usuario.nombre" :error-messages="errors.nombre ? [errors.nombre] : []"/>
        <v-text-field label="Contraseña" :type="showPassword ? 'text' : 'password'" v-model="usuario.contraseña" :error-messages="errors.contraseña ? [errors.contraseña] : []" :append-inner-icon="showPassword ? 'mdi-eye-off' : 'mdi-eye'"
          @click:append-inner="showPassword = !showPassword"/>
        <v-btn @click="login" type="submit" block color="primary">Entrar</v-btn>
        <v-btn @click="signup" block color="secondary" class="mt-2">Crear Usuario</v-btn>
        <v-alert
          v-if="generalError"
          type="error"
          class="mt-4"
          variant="tonal"
          dismissible
          @input="generalError = ''"
        >
          {{ generalError }}
        </v-alert>
        </form>
      </v-card-text>
    </v-card>
  </v-container>
</template>

<script>
import { useRouter } from 'vue-router'
import axios from 'axios'
import { ref } from 'vue'
import { useUsuarioStore } from '@/stores/usuarioStore'
const usuarioStore = useUsuarioStore()
export default{
  data: () => ({
    usuario: {
      nombre : "",
      contraseña : ""
    },
    errors: {},
    generalError: '',
    showPassword: false
  }),
  methods: {
    async login() {
      this.errors = {};
      this.generalError = '';
      console.log(this.usuario);
      try{
        const response = await axios.post("http://localhost:8080/api/usuario/login",this.usuario);
        usuarioStore.setNombre(response.data.nombre)
        this.$router.push('/home');
      } catch (error) {
        if (error.response && error.response.data) {
          const data = error.response.data

          if (data.messages) {
            // Errores de validación
            this.errors = data.messages
          } else if (data.message) {
            // Otro tipo de error
            this.generalError = data.message
          } else {
            this.generalError = 'Ocurrió un error inesperado.'
          }
        } else {
          this.generalError = 'No se pudo conectar con el servidor.'
        }
      }
    },
    signup (){
      this.$router.push('/signup');
    }
  }
}
</script>
