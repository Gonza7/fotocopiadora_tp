<template>
  <v-container fluid>
    <div class="d-flex flex-column flex-sm-row align-start align-sm-center justify-space-between mb-4">
      <h2 class="text-h4 mb-2 mb-sm-0">Usuarios</h2>
      <v-btn v-if="usuarioStore.isAdmin" color="primary" @click="abrirDialogo()">
        Nuevo Usuario
      </v-btn>
    </div>

    <v-card flat>
      <v-data-table
        :headers="headers"
        :items="usuarios"
        :loading="loading"
        :sort-by="[{ key: 'id', order: 'asc' }]"
        class="elevation-1"
        item-key="id"
        no-data-text="No hay usuarios registrados"
      >
        <template v-slot:item.acciones="{ item }">
          <div v-if="usuarioStore.isAdmin">
            <v-icon small class="mr-2" @click="abrirDialogo(item)">
              mdi-pencil
            </v-icon>
            <v-icon small color="red" @click="confirmarEliminar(item)">
              mdi-delete
            </v-icon>
          </div>
        </template>
      </v-data-table>

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
    </v-card>

    <UsuarioDialog
      :dialog="dialog"
      :usuarioAEditar="usuarioAEditar"
      @update:dialog="dialog = $event"
      @usuario-guardado="onUsuarioGuardado"
    />

    <v-dialog v-model="dialogEliminar" max-width="500px">
      <v-card>
        <v-card-title class="text-h5">Confirmar Eliminación</v-card-title>
        <v-card-text>
          ¿Estás seguro de que quieres eliminar al usuario
          <strong>{{ usuarioAEliminar?.nombre }}</strong
          >?
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="grey" text @click="dialogEliminar = false">Cancelar</v-btn>
          <v-btn color="red" text @click="eliminarUsuario">Eliminar</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-container>
</template>

<script>
import { useUsuarioStore } from '@/stores/usuarioStore';
import { listUsuariosService, deleteUsuarioService } from '@/services/usuarioService';
import UsuarioDialog from './UsuarioDialog.vue';

export default {
  components: {
    UsuarioDialog,
  },
  data() {
    return {
      usuarioStore: useUsuarioStore(),
      usuarios: [],
      loading: false,
      errorGeneral: '',
      dialog: false,
      usuarioAEditar: null,
      dialogEliminar: false,
      usuarioAEliminar: null,
    };
  },
  computed: {
    // La clave para eliminar la columna si no se cumplen las condiciones
    headers() {
      const baseHeaders = [
        { title: 'ID', key: 'id' },
        { title: 'Nombre', key: 'nombre' },
      ];

      // Añade la columna de acciones solo si el usuario es 'admin'
      if (this.usuarioStore.isAdmin) {
        baseHeaders.push({ title: 'Acciones', key: 'acciones', sortable: false });
      }

      return baseHeaders;
    },
  },
  created() {
    this.cargarUsuarios();
  },
  methods: {
    async cargarUsuarios() {
      this.loading = true;
      try {
        const response = await listUsuariosService();
        this.usuarios = response.data;
        this.errorGeneral = '';
      } catch (error) {
        console.error('Error al cargar usuarios:', error);
        this.errorGeneral = 'No se pudieron cargar los usuarios. Intenta de nuevo más tarde.';
      } finally {
        this.loading = false;
      }
    },
    abrirDialogo(usuario = null) {
      this.usuarioAEditar = usuario ? { ...usuario } : null;
      this.dialog = true;
    },
    onUsuarioGuardado() {
      this.cargarUsuarios();
      this.dialog = false;
    },
    confirmarEliminar(usuario) {
      this.usuarioAEliminar = usuario;
      this.dialogEliminar = true;
    },
    async eliminarUsuario() {
      this.dialogEliminar = false;
      try {
        await deleteUsuarioService(this.usuarioAEliminar.id);
        this.cargarUsuarios();
      } catch (error) {
        console.error('Error al eliminar usuario:', error);
        this.errorGeneral = 'No se pudo eliminar el usuario.';
      }
    },
  },
};
</script>