<template>
  <v-container class="fill-height d-flex justify-center align-center">
    <v-card width="400">
      <v-card-title class="text-h6">Crear Usuario</v-card-title>
      <v-card-text>
        <form @submit.prevent="login">
        <v-text-field label="Usuario" v-model="usuario.nombre" :error-messages="errors.nombre ? [errors.nombre] : []"/>
        <v-text-field label="Contraseña"v-model="usuario.contraseña" :error-messages="errors.contraseña ? [errors.contraseña] : []"/>
        <v-text-field label="Confirmar contraseña" v-model="confirmarContraseña" :error-messages="errors.confirmar"/>
        <v-btn type="submit" @click="createUsuario" block color="primary">Registrar</v-btn>
        <v-btn type="submit" @click="returnLogin" block color="secondary" class="mt-2">Volver</v-btn>
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
import { createUsuarioService } from '@/services/usuarioService'
import { ref } from 'vue'

export default{
  data: () => ({
    usuario: {
      nombre : "",
      contraseña : ""
    },
    errors: {},
    generalError: '',
    confirmarContraseña: ''
  }),
  methods: {
    async createUsuario() {
      this.errors = {};
      this.generalError = '';
      console.log(this.usuario);
      if (this.usuario.contraseña !== this.confirmarContraseña) {
        this.errors.confirmar = ["Las contraseñas no coinciden"];
        return;
      }
      try{
        const response = await createUsuarioService(this.usuario);
        this.$router.push('/');
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
    returnLogin() {
      this.$router.push('/');
    }
  }
}
</script>
