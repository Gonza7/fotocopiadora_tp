<template>
  <v-container fluid>
    <v-row class="justify-space-between align-center mb-2 mx-2">
      <v-col>
        <h1 class="text-h5">Productos</h1>
      </v-col>
      <v-col cols="auto">
        <v-btn color="primary" @click="openDialog()">Agregar Producto</v-btn>
      </v-col>
    </v-row>

    <v-data-table
      :headers="headers"
      :items="productosFiltrados"
      class="elevation-1"
      :search="search"
      :loading="loading"
      show-expand
      item-value="id"
      :sort-by="[{ key: 'id', order: 'asc' }]"
      :filter-keys="['nombre']"
      v-model:expanded="expanded"
    >
      <template v-slot:top>
        <v-row class="ma-2" align="center" justify="space-between">
          <v-col cols="12" md="4">
            <v-text-field v-model="search" label="Buscar por nombre" dense clearable />
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
          {{ item.softDelete ? "Inactivo" : "Activo" }}
        </v-chip>
      </template>

      <template #item.actions="{ item }">
        <v-icon small @click="openDialog(item)">mdi-pencil</v-icon>
        <v-btn
          icon
          @click="item.softDelete ? activarProducto(item) : deleteProducto(item)"
          class="elevation-0"
        >
          <v-icon :color="item.softDelete ? 'green' : 'red'">
            {{ item.softDelete ? "mdi-restore" : "mdi-delete" }}
          </v-icon>
        </v-btn>
      </template>

      <template #expanded-row="{ columns, item }">
        <tr>
          <td :colspan="columns.length">
            <ProductoDetail :product="item" />
          </td>
        </tr>
      </template>
    </v-data-table>

    <ProductoDialog
      :dialog="dialog"
      :product-to-edit="productToEdit"
      @update:dialog="dialog = $event"
      @product-saved="listProductos"
    />
  </v-container>
</template>

<script>
import {
  listProductosService,
  deleteProductoService,
  activateProductoService,
} from "@/services/productoService";
import ProductoDialog from "./ProductoDialog.vue";
import ProductoDetail from "./ProductoDetail.vue";

export default {
  components: {
    ProductoDialog,
    ProductoDetail,
  },
  data() {
    return {
      loading: true,
      search: "",
      headers: [
        { title: "ID", key: "id" },
        { title: "Nombre", key: "nombre" },
        { title: "Tipo", key: "tipoProducto" },
        { title: "Estado", key: "softDelete" },
        { title: "Acciones", key: "actions", sortable: false },
        { title: "", key: "data-table-expand" },
      ],
      productos: [],
      tiposProducto: ["INSUMO", "PRODUCTO_VENTA", "FOTOCOPIA"],
      dialog: false,
      productToEdit: null, // Guardará el producto a editar
      filtroTipos: [],
      filtroEstado: null,
      expanded: [],
    };
  },
  methods: {
    async listProductos() {
      this.loading = true;
      try {
        const response = await listProductosService();
        this.productos = response.data;
      } catch (error) {
        console.error("Error al listar productos:", error);
      } finally {
        this.loading = false;
      }
    },
    traducirTipo(tipo) {
      const map = {
        INSUMO: "Insumo",
        PRODUCTO_VENTA: "Producto Venta",
        FOTOCOPIA: "Fotocopia",
      };
      return map[tipo] || tipo;
    },
    openDialog(producto = null) {
      this.productToEdit = producto ? { ...producto, tipo: producto.tipoProducto } : null;
      if (this.productToEdit && this.productToEdit.tipo === "FOTOCOPIA") {
        this.productToEdit.listaPrecios = [...this.productToEdit.preciosFotocopia];
      } else if (!this.productToEdit) {
        // Resetear el formulario al abrirlo para un nuevo producto
        this.productToEdit = {
          id: null,
          nombre: "",
          tipo: "",
          precioUnitario: null,
          stock: null,
          listaPrecios: [],
        };
      }
      this.dialog = true;
    },
    async deleteProducto(producto) {
      try {
        await deleteProductoService(producto.id);
        this.listProductos();
      } catch (error) {
        console.error("Error al eliminar producto:", error);
        // Aquí podrías mostrar una notificación al usuario
      }
    },
    async activarProducto(producto) {
      try {
        await activateProductoService(producto.id);
        this.listProductos();
      } catch (error) {
        console.error("Error al activar producto:", error);
        // Aquí podrías mostrar una notificación al usuario
      }
    },
  },
  mounted() {
    this.listProductos();
  },
  computed: {
    productosFiltrados() {
      return this.productos.filter((p) => {
        const coincideTipo =
          this.filtroTipos.length === 0 ||
          this.filtroTipos.includes(p.tipoProducto);
        const coincideEstado =
          !this.filtroEstado ||
          (this.filtroEstado === "Activo" && !p.softDelete) ||
          (this.filtroEstado === "Inactivo" && p.softDelete);

        return coincideTipo && coincideEstado;
      });
    },
  },
  watch: {
    expanded(val) {
      if (val.length > 1) {
        this.expanded = [val[val.length - 1]];
      }
    },
  },
};
</script>