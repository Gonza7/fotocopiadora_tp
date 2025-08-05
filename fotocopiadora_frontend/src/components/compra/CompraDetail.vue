<template>
  <v-card flat class="pa-4">
    <v-card-title class="text-h6">Detalle de Compra</v-card-title>
    <v-card-text>
      <v-row>
        <v-col cols="12" sm="6">
          <strong>Proveedor:</strong> {{ compra.proveedor }}
        </v-col>
        <v-col cols="12" sm="6">
          <strong>Fecha de Pago:</strong> {{ formatFecha(compra.fechaPago) }}
        </v-col>
        <v-col cols="12" sm="6">
          <strong>Monto Total:</strong> ${{ compra.monto.toFixed(2) }}
        </v-col>
        <v-col cols="12" sm="6">
          <strong>Estado Compra General:</strong>
          <v-chip :color="getColorEstadoCompra(compra.estadoCompra)" dark>
            {{ traducirEstadoCompra(compra.estadoCompra) }}
          </v-chip>
        </v-col>
      </v-row>

      <h4 class="mt-4 mb-2">Productos Comprados:</h4>
      <v-data-table
        :headers="detalleHeaders"
        :items="compra.detalleCompra"
        hide-default-footer
        disable-pagination
        class="elevation-1"
      >
        <template v-slot:item.estadoCompra="{ item }">
          <v-chip :color="getColorEstadoCompra(item.estadoCompra)" dark size="small">
            {{ traducirEstadoCompra(item.estadoCompra) }}
          </v-chip>
        </template>
        <template v-slot:item.acciones="{ item }">
          <v-btn
            v-if="item.estadoCompra === 'PENDIENTE'"
            variant="text"        color="success"
            size="small"
            @click="confirmarFinalizarDetalle(compra.id, item.idProducto, item.nombreProducto)"
            :loading="loadingDetalle === `${compra.id}-${item.idProducto}`"
            :disabled="loadingDetalle !== null && loadingDetalle !== `${compra.id}-${item.idProducto}`"
          >
            Finalizar
          </v-btn>
          <v-chip v-else color="info" size="small">
            Finalizado
          </v-chip>
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

      <v-dialog v-model="dialogConfirmacion" max-width="500">
        <v-card>
          <v-card-title class="text-h5">Confirmar Finalización</v-card-title>
          <v-card-text>
            ¿Estás seguro de que deseas finalizar el detalle del producto <strong>"{{ detalleAFinalizarNombre }}"</strong> de la compra #{{ detalleAFinalizarCompraId }}?
          </v-card-text>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="grey" text @click="dialogConfirmacion = false">Cancelar</v-btn>
            <v-btn color="success" text @click="ejecutarFinalizarDetalle">Confirmar</v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>

    </v-card-text>
  </v-card>
</template>

<script>
// Asegúrate de que esta ruta sea correcta para tu `compraService.js`
import { updateEstadoCompraService } from '@/services/compraService';

export default {
  props: {
    compra: {
      type: Object,
      required: true,
    },
  },
  emits: ['compra-actualizada'], // Emitimos un evento al padre cuando la compra se actualiza
  data() {
    return {
      detalleHeaders: [
        { title: "ID Producto", key: "idProducto" },
        { title: "Producto", key: "nombreProducto" },
        { title: "Cantidad", key: "cantidad" },
        { title: "Estado Detalle", key: "estadoCompra" },
        { title: "Acciones", key: "acciones", sortable: false }, // Nueva columna para los botones
      ],
      loadingDetalle: null, // Para manejar el estado de carga del botón específico
      errorGeneral: '', // Para mostrar errores generales

      // Propiedades para el diálogo de confirmación
      dialogConfirmacion: false,
      detalleAFinalizarCompraId: null,
      detalleAFinalizarProductoId: null,
      detalleAFinalizarNombre: '',
    };
  },
  methods: {
    formatFecha(fecha) {
      if (!fecha) return "";
      const options = { year: "numeric", month: "long", day: "numeric" };
      return new Date(fecha).toLocaleDateString("es-ES", options);
    },
    traducirEstadoCompra(estado) {
      const map = {
        PENDIENTE: "Pendiente",
        FINALIZADO: "Finalizada",
      };
      return map[estado] || estado;
    },
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
    // Abre el diálogo de confirmación
    confirmarFinalizarDetalle(idCompra, idProducto, nombreProducto) {
      this.detalleAFinalizarCompraId = idCompra;
      this.detalleAFinalizarProductoId = idProducto;
      this.detalleAFinalizarNombre = nombreProducto;
      this.dialogConfirmacion = true;
    },
    // Ejecuta la acción de finalizar después de la confirmación
    async ejecutarFinalizarDetalle() {
      this.dialogConfirmacion = false; // Cierra el diálogo de confirmación
      const idCompra = this.detalleAFinalizarCompraId;
      const idProducto = this.detalleAFinalizarProductoId;

      this.loadingDetalle = `${idCompra}-${idProducto}`; // Activa el loading del botón
      this.errorGeneral = ''; // Limpia errores anteriores

      try {
        // Llama al servicio de backend
        const updatedCompra = await updateEstadoCompraService(idCompra, idProducto);

        // Emitimos el evento al componente padre con la compra actualizada
        this.$emit('compra-actualizada', updatedCompra);

        // Actualización optimista de la UI para feedback inmediato:
        // Busca y actualiza el estado del detalle específico en la propiedad 'compra'
        const detalleActualizadoLocal = this.compra.detalleCompra.find(
          (d) => d.idProducto === idProducto
        );
        if (detalleActualizadoLocal) {
          detalleActualizadoLocal.estadoCompra = 'FINALIZADO'; // Cambia el estado visualmente
          // Si todos los detalles están finalizados, actualiza también el estado general de la compra
          const todosFinalizados = this.compra.detalleCompra.every(
            d => d.estadoCompra === 'FINALIZADO'
          );
          if (todosFinalizados) {
            this.compra.estadoCompra = 'FINALIZADO';
          }
        }

      } catch (error) {
        console.error("Error al finalizar el detalle de compra:", error);
        if (error.response && error.response.data && error.response.data.message) {
            this.errorGeneral = error.response.data.message;
        } else {
            this.errorGeneral = "Error al finalizar el detalle. Consulta la consola para más detalles.";
        }
      } finally {
        this.loadingDetalle = null; // Desactiva el loading del botón
        // Limpiar las propiedades del diálogo de confirmación
        this.detalleAFinalizarCompraId = null;
        this.detalleAFinalizarProductoId = null;
        this.detalleAFinalizarNombre = '';
      }
    },
  },
};
</script>

<style scoped>
/* Tus estilos si los tienes */
</style>