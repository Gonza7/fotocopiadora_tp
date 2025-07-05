<template>
  <v-container fluid>
  <v-row class="justify-space-between align-center mb-2 mx-2">
      <v-col>
        <h1 class="text-h5">Productos</h1>
      </v-col>
      <v-col cols="auto">
        <v-btn color="primary" @click="abrirDialogo">Agregar Producto</v-btn>
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

    
  </v-data-table>
  </v-container>
</template>

<script>
  import { getProductoService, listProductosService } from '@/services/productoService';
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