<template>
  <v-dialog :model-value="dialog" @update:model-value="cerrarDialogo" max-width="500px">
    <v-card>
      <v-card-title>
        <span class="text-h5">{{ form.id ? 'Editar' : 'Nuevo' }} Usuario</span>
      </v-card-title>
      <v-card-text>
        <v-form ref="formRef">
          <v-text-field
            v-model="form.nombre"
            label="Nombre"
            required
            :error-messages="errors.nombre ? [errors.nombre] : []"
            @input="errors.nombre = null"
          ></v-text-field>
          <v-text-field
            v-model="form.contraseña"
            label="Contraseña"
            type="password"
            required
            :error-messages="errors.contraseña ? [errors.contraseña] : []"
            @input="errors.contraseña = null"
          ></v-text-field>
        </v-form>
        <v-alert
          v-if="errorGeneral"
          type="error"
          class="mt-4"
          variant="tonal"
          dismissible
          @input="errorGeneral = ''"
        >
          {{ errorGeneral }}
        </v-alert>
      </v-card-text>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn color="grey" text @click="cerrarDialogo">Cancelar</v-btn>
        <v-btn color="primary" text @click="guardar">Guardar</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>
import { createUsuarioService, updateUsuarioService } from '@/services/usuarioService';

export default {
  props: {
    dialog: {
      type: Boolean,
      required: true,
    },
    usuarioAEditar: {
      type: Object,
      default: null,
    },
  },
  emits: ['update:dialog', 'usuario-guardado'],
  data() {
    return {
      form: this.resetearFormulario(),
      errors: {},
      errorGeneral: '',
    };
  },
  watch: {
    dialog(val) {
      if (val && this.usuarioAEditar) {
        this.form = { ...this.usuarioAEditar, contraseña: '' };
      } else if (val) {
        this.form = this.resetearFormulario();
      }
      this.errors = {};
      this.errorGeneral = '';
    },
  },
  methods: {
    resetearFormulario() {
      return {
        id: null,
        nombre: '',
        contraseña: '',
      };
    },
    cerrarDialogo() {
      this.$emit('update:dialog', false);
    },
    async guardar() {
      this.errors = {};
      this.errorGeneral = '';
      try {
        if (this.form.id) {
          await updateUsuarioService(this.form.id, this.form);
        } else {
          await createUsuarioService(this.form);
        }
        this.$emit('usuario-guardado');
        this.cerrarDialogo();
      } catch (error) {
        if (error.response && error.response.data) {
          const data = error.response.data;
          if (data.messages) {
            this.errors = data.messages;
          } else if (data.message) {
            this.errorGeneral = data.message;
          }
        } else {
          this.errorGeneral = 'Ocurrió un error inesperado al procesar el usuario.';
        }
      }
    },
  },
};
</script>