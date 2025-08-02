<template>
  <v-dialog :model-value="dialog" @update:model-value="cerrarDialogo" max-width="800px">
    <v-card>
      <v-card-title>
        <span class="text-h6">{{ form.id ? "Editar" : "Registrar" }} Venta</span>
      </v-card-title>

      <v-card-text>
        <v-form ref="formRef">
          <v-select
            v-model="form.formaPago"
            :items="formasPago"
            label="Forma de Pago"
            required
            :error-messages="errors.formaPago ? [errors.formaPago] : []"
            @input="errors.formaPago = null"
          />

          <h3 class="mt-4 mb-2">Detalles de Venta</h3>
          <div v-for="(detalle, index) in form.detallesVenta" :key="index" class="d-flex align-center gap-2 mb-2">
            <v-select
              v-model="detalle.idProducto"
              :items="productosDisponibles"
              item-title="nombre"
              item-value="id"
              label="Producto"
              class="flex-grow-1"
              required
              :error-messages="errorsDetalle[index]?.idProducto ? [errorsDetalle[index].idProducto] : []"
              @update:model-value="limpiarErrorDetalle(index, 'idProducto')"
            />
            <v-text-field
              v-model.number="detalle.cantidad"
              label="Cantidad"
              type="number"
              min="1"
              class="flex-grow-0"
              style="width: 100px;"
              required
              :error-messages="errorsDetalle[index]?.cantidad ? [errorsDetalle[index].cantidad] : []"
              @input="limpiarErrorDetalle(index, 'cantidad')"
            />
            <v-btn icon @click="eliminarDetalle(index)">
              <v-icon>mdi-delete</v-icon>
            </v-btn>
          </div>
          <v-btn class="mt-2" @click="agregarDetalle" small>
            Agregar Producto
          </v-btn>

          <v-alert v-if="errorGeneral" type="error" class="mt-4" variant="tonal" dismissible
            @input="errorGeneral = ''">
            {{ errorGeneral }}
          </v-alert>
        </v-form>
      </v-card-text>

      <v-card-actions>
        <v-spacer />
        <v-btn text @click="cerrarDialogo">Cancelar</v-btn>
        <v-btn color="primary" @click="guardar">Guardar</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>
import { createVentaService, updateVentaService } from "@/services/ventaService"; // Asegúrate de tener tus servicios de ventas
import { listProductosService } from "@/services/productoService"; // Necesario para obtener los productos disponibles

export default {
  props: {
    dialog: {
      type: Boolean,
      required: true,
    },
    ventaAEditar: {
      type: Object,
      default: null,
    },
  },
  emits: ["update:dialog", "venta-guardada"],
  data() {
    return {
      form: this.resetearFormulario(),
      formasPago: [
        { label: "Efectivo", value: "EFECTIVO" },
        { label: "Transferencia", value: "TRANSFERENCIA" },
      ],
      productosDisponibles: [], // Aquí se cargarán los productos para el select
      errors: {},
      errorGeneral: "",
      errorsDetalle: [], // Errores para los detalles de venta
    };
  },
  watch: {
    ventaAEditar: {
      handler(newVal) {
        if (newVal) {
          this.form = { ...newVal };
        } else {
          this.form = this.resetearFormulario();
        }
        this.errors = {};
        this.errorsDetalle = [];
        this.errorGeneral = "";
      },
      immediate: true,
    },
    dialog(val) {
      if (val) {
        this.cargarProductos(); // Cargar productos solo cuando el diálogo se abre
      }
    }
  },
  methods: {
    resetearFormulario() {
      return {
        id: null,
        formaPago: "",
        detallesVenta: [],
      };
    },
    cerrarDialogo() {
      this.$emit("update:dialog", false);
    },
    async cargarProductos() {
      try {
        const response = await listProductosService();
        this.productosDisponibles = response.data.filter(p => !p.softDelete); // Solo productos activos
      } catch (error) {
        console.error("Error al cargar productos:", error);
        this.errorGeneral = "No se pudieron cargar los productos disponibles.";
      }
    },
    agregarDetalle() {
      this.form.detallesVenta.push({ idProducto: null, cantidad: 1 });
    },
    eliminarDetalle(index) {
      this.form.detallesVenta.splice(index, 1);
      if (this.errorsDetalle[index]) {
        this.errorsDetalle.splice(index, 1);
      }
    },
    limpiarErrorDetalle(index, campo) {
      if (this.errorsDetalle[index]) {
        this.errorsDetalle[index][campo] = null;
      }
    },
    async guardar() {
      this.errors = {};
      this.errorGeneral = "";
      this.errorsDetalle = [];
      try {
        const dataToSend = {
          ...this.form,
        };

        if (this.form.id) {
          await updateVentaService(this.form.id, dataToSend);
        } else {
          await createVentaService(dataToSend);
        }

        this.$emit("venta-guardada");
        this.cerrarDialogo();
      } catch (error) {
        if (error.response && error.response.data) {
          const data = error.response.data;
          if (data.messages) {
            for (const key in data.messages) {
              if (key.startsWith("detallesVenta[")) {
                const match = key.match(/detallesVenta\[(\d+)\]\.(\w+)/);
                if (match) {
                  const index = parseInt(match[1]);
                  const field = match[2];
                  if (!this.errorsDetalle[index]) this.errorsDetalle[index] = {};
                  this.errorsDetalle[index][field] = data.messages[key];
                }
              } else {
                this.errors[key] = data.messages[key];
              }
            }
          } else if (data.message) {
            this.errorGeneral = data.message;
          } else if (error.response.status === 400) {
            this.errorGeneral =
              "Hay errores en los datos enviados. Verificá que todos los campos requeridos estén completos y que los productos seleccionados sean válidos.";
          } else {
            this.errorGeneral = "Ocurrió un error inesperado.";
          }
        } else {
          this.errorGeneral = "No se pudo conectar con el servidor.";
        }
      }
    },
  },
};
</script>

<style scoped>
.gap-2 {
  gap: 8px; /* O el espacio que prefieras para simular gap */
}
</style>