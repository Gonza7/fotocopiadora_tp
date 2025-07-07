<template>
  <v-container fluid>
  <v-row class="justify-space-between align-center mb-2 mx-2">
      <v-col>
        <h1 class="text-h5">Productos</h1>
      </v-col>
      <v-col cols="auto">
        <v-btn color="primary" @click="openDialog">Agregar Producto</v-btn>
      </v-col>
    </v-row>
  <v-data-table
    :headers="headers"
    :items="productos"
    class="elevation-1"
    :search="search"
    :loading="loading"
    :sort-by="[{ key: 'id', order: 'asc' }]"
    :filter-keys="['nombre']"
  >
   <template v-slot:top>
      <v-row class="ma-2" align="center" justify="space-between">
        <v-col cols="12" md="4">
          <v-text-field
            v-model="search"
            label="Buscar por nombre"
            dense
            clearable
          />
        </v-col>

        <v-col cols="12" md="4">
          <v-select
            v-model="filtroTipos"
            :items="tiposProducto"
            label="Filtrar por tipo"
            multiple
            chips
            dense
            clearable
          />
        </v-col>

        <v-col cols="12" md="4">
          <v-select
            v-model="filtroEstado"
            :items="['Activo', 'Inactivo']"
            label="Estado"
            dense
            clearable
          />
        </v-col>
      </v-row>
    </template>

    <template v-slot:item.tipoProducto="{ item }">
      {{ traducirTipo(item.tipoProducto) }}
    </template>

    <template v-slot:item.softDelete="{ item }">
      <v-chip :color="item.softDelete ? 'error' : 'success'" dark>
        {{ item.softDelete ? 'Inactivo' : 'Activo' }}
      </v-chip>
    </template>

    <template v-slot:item.acciones="{ item }">
      <v-btn icon @click="openDetalles(item)"><v-icon>mdi-eye</v-icon></v-btn>
      <v-btn icon @click="updateProducto(item)"><v-icon>mdi-pencil</v-icon></v-btn>
      <v-btn icon @click="deleteProducto(item)"><v-icon color="red">mdi-delete</v-icon></v-btn>
    </template>
  </v-data-table>

  </v-container>
</template>

<script>
  import { getProductoService, listProductosService, deleteProductoService, activateProductoService } from '@/services/productoService';
  import FormProducto from '@/components/Producto/FormProducto.vue';
  import DetalleProducto from '@/components/Producto/DetalleProducto.vue';
  export default{
    data() {
      return {
        loading: true,
        search: '',
        headers: [
          { title: 'ID', key: 'id' },
          { title: 'Nombre', key: 'nombre' },
          { title: 'Tipo', key: 'tipoProducto' },
          { title: 'Estado', key: 'softDelete' }
        ],
        productos: [],
        tiposProducto: ['INSUMO', 'PRODUCTO_VENTA', 'FOTOCOPIA'],
        filtroTipos: [],
        filtroEstado: null,
        dialog: false,
        producto: null,
        dialogDetalle: false,
        productoDetalle: null,
        isUpdate: false
      }
    },
    computed: {
      productosFiltrados() {
        return this.productos.filter(p => {
          const tipoCoincide = !this.filtroTipos.length || this.filtroTipos.includes(p.tipoProducto);
          const estadoCoincide = this.filtroEstado === null || (this.filtroEstado === 'Activo' && !p.softDelete) || (this.filtroEstado === 'Inactivo' && p.softDelete);
          return tipoCoincide && estadoCoincide;
        });
      }
    },
    methods: {
      async listProductos() {
        this.loading = true;
        try {
          const response = await listProductosService();
          this.productos = response.data;
        } catch (error) {
          console.error('Error al listar productos:', error);
        } finally {
          this.loading = false;
        }
      },
      openDialog() {
        this.producto = null;
        this.isUpdate = false;
        this.dialog = true;
      },
      closeDialog() {
        this.dialog = false;
      },
      updateProducto(producto) {
        this.producto = { ...producto };
        this.isUpdate = true;
        this.dialog = true;
      },
      async saveProducto(){
        await this.listProductos();
      },
      async deleteProducto(producto) {
        await deleteProductoService(producto.id);
        await this.listProductos();
      },
      openDetalles(producto) {
        this.productoDetalle = producto;
        this.dialogDetalle = true;
      },
      traducirTipo(tipo) {
        const map = {
          INSUMO: 'Insumo',
          PRODUCTO_VENTA: 'Producto Venta',
          FOTOCOPIA: 'Fotocopia'
        };
        return map[tipo] || tipo;
      }
    },
    mounted() {
      this.listProductos();
    }
  }
</script>