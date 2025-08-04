<template>
  <v-container fluid>
    <v-row class="justify-space-between align-center mb-2 mx-2">
      <v-col>
        <h1 class="text-h5">Ventas</h1>
      </v-col>
      <v-col cols="auto">
        <v-btn color="primary" @click="abrirDialogo()">Registrar Venta</v-btn>
      </v-col>
    </v-row>

    <v-data-table
      :headers="headers"
      :items="ventasFiltradas"
      class="elevation-1"
      :search="search"
      :loading="loading"
      show-expand
      item-value="id"
      :sort-by="[{ key: 'id', order: 'asc' }]"
      v-model:expanded="expanded"
    >
      <template v-slot:top>
        <v-row class="ma-2" align="center" justify="space-between">
          <v-col cols="12" md="6">
            <v-select
              v-model="filtroFormaPago"
              :items="formasPago"
              item-title="title"
              item-value="value"
              label="Filtrar por forma de pago"
              clearable
              dense
            />
          </v-col>

          <v-col cols="12" md="6">
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

      <template v-slot:item.formaPago="{ item }">
        {{ traducirFormaPago(item.formaPago) }}
      </template>

      <template v-slot:item.fechaPago="{ item }">
        {{ formatFecha(item.fechaPago) }}
      </template>

      <template v-slot:item.precioTotal="{ item }">
        ${{ item.precioTotal.toFixed(2) }}
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
            @click="item.softDelete ? activarVenta(item) : eliminarVenta(item)"
            class="elevation-0 ml-1"
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
            <VentaDetail :venta="item" />
          </td>
        </tr>
      </template>
    </v-data-table>

    <VentaDialog
      :dialog="dialog"
      :venta-a-editar="ventaAEditar"
      @update:dialog="dialog = $event"
      @venta-guardada="listarVentas"
    />
  </v-container>
</template>

<script>
import {
  listVentasService,
  deleteVentaService,
  activateVentaService,
} from "@/services/ventaService"; // Asegúrate de tener tus servicios de ventas
import VentaDialog from "./VentaDialog.vue";
import VentaDetail from "./VentaDetail.vue";

export default {
  components: {
    VentaDialog,
    VentaDetail,
  },
  data() {
    return {
      loading: true,
      search: "",
      headers: [
        { title: "ID", key: "id" },
        { title: "Forma de Pago", key: "formaPago" },
        { title: "Fecha de Pago", key: "fechaPago" },
        { title: "Precio Total", key: "precioTotal" },
        { title: "Estado", key: "softDelete" },
        { title: "Acciones", key: "actions", sortable: false },
        { title: "", key: "data-table-expand" },
      ],
      ventas: [],
      formasPago: [
        { title: "Efectivo", value: "EFECTIVO" },
        { title: "Transferencia", value: "TRANSFERENCIA" },
      ],
      dialog: false,
      ventaAEditar: null, // Guardará la venta a editar
      filtroFormaPago: null,
      filtroEstado: null,
      expanded: [],
    };
  },
  methods: {
    async listarVentas() {
      this.loading = true;
      try {
        const response = await listVentasService();
        this.ventas = response.data.content;
      } catch (error) {
        console.error("Error al listar ventas:", error);
      } finally {
        this.loading = false;
      }
    },
    traducirFormaPago(formaPago) {
      const map = {
        EFECTIVO: "Efectivo",
        TRANSFERENCIA: "Transferencia",
      };
      return map[formaPago] || formaPago;
    },
    formatFecha(fecha) {
      if (!fecha) return "";
      const options = { year: "numeric", month: "long", day: "numeric" };
      return new Date(fecha).toLocaleDateString("es-ES", options);
    },
    abrirDialogo(venta = null) {
      // Para la edición, necesitamos el id de los productos en detalleVenta para el formulario
      this.ventaAEditar = venta
        ? {
            ...venta,
            detallesVenta: venta.detallesVenta.map((detalle) => ({
              idProducto: detalle.idProducto,
              nombreProducto: detalle.nombreProducto,
              cantidad: detalle.cantidad,
            })),
          }
        : null;

      if (!this.ventaAEditar) {
        // Resetear el formulario al abrirlo para una nueva venta
        this.ventaAEditar = {
          id: null,
          formaPago: "",
          detallesVenta: [],
        };
      }
      this.dialog = true;
    },
    async eliminarVenta(venta) {
      try {
        await deleteVentaService(venta.id);
        this.listarVentas();
      } catch (error) {
        console.error("Error al eliminar venta:", error);
      }
    },
    async activarVenta(venta) {
      try {
        await activateVentaService(venta.id);
        this.listarVentas();
      } catch (error) {
        console.error("Error al activar venta:", error);
      }
    },
  },
  mounted() {
    this.listarVentas();
  },
  computed: {
    ventasFiltradas() {
      return this.ventas.filter((v) => {
        const coincideFormaPago =
          !this.filtroFormaPago || v.formaPago === this.filtroFormaPago;
        const coincideEstado =
          !this.filtroEstado ||
          (this.filtroEstado === "Activo" && !v.softDelete) ||
          (this.filtroEstado === "Inactivo" && v.softDelete);

        return coincideFormaPago && coincideEstado;
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
