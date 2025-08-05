<template>
  <v-container fluid>
    <v-row class="justify-space-between align-center mb-2 mx-2">
      <v-col>
        <h1 class="text-h5">Compras</h1>
      </v-col>
      <v-col cols="auto">
        <v-btn color="primary" @click="abrirDialogo()">Registrar Compra</v-btn>
      </v-col>
    </v-row>

    <v-data-table
      :headers="headers"
      :items="comprasFiltradas"
      class="elevation-1"
      :search="search"
      :loading="loading"
      show-expand
      item-value="id"
      :sort-by="[{ key: 'id', order: 'asc' }]"
      :filter-keys="['proveedor']"
      v-model:expanded="expanded"
    >
      <template v-slot:top>
         
        <v-row class="ma-2" align="center" justify="space-between">
          <v-col cols="12" md="4">
            <v-text-field v-model="search" label="Buscar por proveedor" dense clearable />
          </v-col>
          <v-col cols="12" md="4">
            <v-select
              v-model="filtroEstadoCompra"
              :items="estadosCompra"
              label="Filtrar por Estado de Compra"
              clearable
              dense
              item-title="label"
              item-value="value"
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

      <template v-slot:item.fechaPago="{ item }">
        {{ formatFecha(item.fechaPago) }}
      </template>

      <template v-slot:item.monto="{ item }">
        ${{ item.monto.toFixed(2) }}
      </template>

      <template v-slot:item.estadoCompra="{ item }">
        <v-chip :color="getColorEstadoCompra(item.estadoCompra)" dark>
          {{ traducirEstadoCompra(item.estadoCompra) }}
        </v-chip>
      </template>

      <template v-slot:item.softDelete="{ item }">
        <v-chip :color="item.softDelete ? 'error' : 'success'" dark>
          {{ item.softDelete ? "Inactivo" : "Activo" }}
        </v-chip>
      </template>

      <template #item.actions="{ item }">
        <div class="d-flex flex-nowrap align-center">
          <v-icon small @click="abrirDialogo(item)">mdi-pencil</v-icon>
          <v-btn
            icon
            @click="
              item.softDelete ? activarCompra(item) : eliminarCompra(item)
            "
            class="elevation-0 ml-1"
            size="small"
          >
            <v-icon :color="item.softDelete ? 'green' : 'red'">
              {{ item.softDelete ? "mdi-restore" : "mdi-delete" }}
            </v-icon>
          </v-btn>
        </div>
      </template>

      <template #expanded-row="{ columns, item }">
        <tr>
          <td :colspan="columns.length">
            <CompraDetail
              :compra="item"
              @compra-actualizada="manejarActualizacionDeCompra"
            />
          </td>
        </tr>
      </template>
    </v-data-table>

    <CompraDialog
      :dialog="dialog"
      :compra-a-editar="compraAEditar"
      @update:dialog="dialog = $event"
      @compra-guardada="listarCompras"
    />
  </v-container>
</template>

<script>
import {
  listComprasService,
  deleteCompraService,
  activateCompraService,
} from "@/services/compraService";
import CompraDialog from "./CompraDialog.vue";
import CompraDetail from "./CompraDetail.vue";

export default {
  components: {
    CompraDialog,
    CompraDetail,
  },
  data() {
    return {
      loading: true,
      search: "",
      headers: [
        { title: "ID", key: "id", class: "d-none d-sm-table-cell" },
        { title: "Fecha de Pago", key: "fechaPago" },
        { title: "Proveedor", key: "proveedor" },
        { title: "Monto", key: "monto" },
        { title: "Estado Compra", key: "estadoCompra" },
        { title: "Estado", key: "softDelete", class: "d-none d-sm-table-cell" },
        { title: "Acciones", key: "actions", sortable: false, width: "100px" },
        { title: "", key: "data-table-expand", width: "50px" },
      ],
      compras: [],
      // *** CAMBIO AQUÍ: Actualizar estadosCompra ***
      estadosCompra: [
        { label: "Pendiente", value: "PENDIENTE" },
        { label: "Finalizada", value: "FINALIZADO" },
      ],
      dialog: false,
      compraAEditar: null,
      filtroEstadoCompra: null,
      filtroEstado: null,
      expanded: [],
    };
  },
  methods: {
    async listarCompras() {
      this.loading = true;
      try {
        const response = await listComprasService();
        this.compras = response.data.content;
      } catch (error) {
        console.error("Error al listar compras:", error);
      } finally {
        this.loading = false;
      }
    },
    formatFecha(fecha) {
      if (!fecha) return "";
      const options = { year: "numeric", month: "long", day: "numeric" };
      return new Date(fecha).toLocaleDateString("es-ES", options);
    },
    // *** CAMBIO AQUÍ: Actualizar traducción de estado de compra ***
    traducirEstadoCompra(estado) {
      const map = {
        PENDIENTE: "Pendiente",
        FINALIZADO: "Finalizada",
      };
      return map[estado] || estado;
    },
    // *** CAMBIO AQUÍ: Actualizar colores de estado de compra ***
    getColorEstadoCompra(estado) {
      switch (estado) {
        case "PENDIENTE":
          return "orange";
        case "FINALIZADO":
          return "green";
        default:
          return "grey";
      }
    },
    abrirDialogo(compra = null) {
      this.compraAEditar = compra
        ? {
            ...compra,
            detalleCompra: compra.detalleCompra.map((detalle) => ({
              idProducto: detalle.idProducto,
              nombreProducto: detalle.nombreProducto,
              cantidad: detalle.cantidad,
              estadoCompra: detalle.estadoCompra,
            })),
          }
        : null;

      if (!this.compraAEditar) {
        this.compraAEditar = {
          id: null,
          proveedor: "",
          detalleCompra: [],
          monto: null,
        };
      }
      this.dialog = true;
    },
    async eliminarCompra(compra) {
      try {
        await deleteCompraService(compra.id);
        this.listarCompras();
      } catch (error) {
        console.error("Error al eliminar compra:", error);
      }
    },
    async activarCompra(compra) {
      try {
        await activateCompraService(compra.id);
        this.listarCompras();
      } catch (error) {
        console.error("Error al activar compra:", error);
      }
    },
    manejarActualizacionDeCompra(compraActualizada) {
      // Encuentra la compra en tu lista principal y reemplázala con la versión actualizada
      const index = this.compras.findIndex(
        (c) => c.id === compraActualizada.id
      );
      if (index !== -1) {
        this.compras.splice(index, 1, compraActualizada);
      } else {
        // Si por alguna razón no se encuentra (ej. paginación), recarga la lista
        this.listarCompras();
      }
      // También podrías simplemente recargar la lista de compras si es más simple para tu caso:
      // this.cargarCompras();
    },
  },
  mounted() {
    this.listarCompras();
  },
  computed: {
    comprasFiltradas() {
      return this.compras.filter((c) => {
        const coincideEstadoCompra =
          !this.filtroEstadoCompra ||
          c.estadoCompra === this.filtroEstadoCompra;
        const coincideEstado =
          !this.filtroEstado ||
          (this.filtroEstado === "Activo" && !c.softDelete) ||
          (this.filtroEstado === "Inactivo" && c.softDelete);

        return coincideEstadoCompra && coincideEstado;
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

<style scoped>
/* Estilos para el ajuste responsivo de botones en la tabla */
@media (max-width: 767px) {
  .v-data-table .v-data-table__td {
    padding-left: 4px !important;
    padding-right: 4px !important;
  }
}
</style>
