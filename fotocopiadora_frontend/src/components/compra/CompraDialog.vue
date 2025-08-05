<template>
  <v-dialog :model-value="dialog" @update:model-value="cerrarDialogo" max-width="800px">
    <v-card>
      <v-card-title>
        <span class="text-h6">{{ form.id ? "Editar" : "Registrar" }} Compra</span>
      </v-card-title>

      <v-card-text>
        <v-form ref="formRef">
          <v-text-field v-model="form.proveedor" label="Proveedor" required
            :error-messages="errors.proveedor ? [errors.proveedor] : []" @input="errors.proveedor = null" />
          <v-text-field v-model.number="form.monto" label="Monto total" type="text" inputmode="numeric" min="1" required
            :error-messages="errors.monto ? [errors.monto] : []" @input="errors.monto = null" />
          <h3 class="mt-4 mb-2">Detalles de Compra</h3>
          <div v-for="(detalle, index) in form.detalleCompra" :key="index" class="d-flex align-center gap-2 mb-2">
            <v-autocomplete v-model="detalle.idProducto" :items="productosDisponibles" item-title="nombre"
              item-value="id" label="Producto" placeholder="Escribe para buscar un producto" class="flex-grow-1"
              required :error-messages="errorsDetalle[index]?.idProducto
                ? [errorsDetalle[index].idProducto]
                : []
                " @update:model-value="limpiarErrorDetalle(index, 'idProducto')" return-object :filter="customFilter"
              @change="onProductoSeleccionado(index)" />
            <v-text-field v-model.number="detalle.cantidad" label="Cantidad" type="text" inputmode="numeric"
              class="flex-grow-1" style="width: 100px" required :error-messages="errorsDetalle[index]?.cantidad
                ? [errorsDetalle[index].cantidad]
                : []
                " @input="limpiarErrorDetalle(index, 'cantidad')" />

            <v-btn icon @click="eliminarDetalle(index)">
              <v-icon>mdi-delete</v-icon>
            </v-btn>
          </div>
          <v-btn class="mt-2" @click="agregarDetalle" small>
            Agregar Producto
          </v-btn>

          <v-alert v-if="errorGeneral" type="error" class="mt-4" variant="tonal" dismissible @input="errorGeneral = ''">
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
import {
  createCompraService,
  updateCompraService,
} from "@/services/compraService";
import { listProductosService } from "@/services/productoService";

export default {
  props: {
    dialog: {
      type: Boolean,
      required: true,
    },
    compraAEditar: {
      type: Object,
      default: null,
    },
  },
  emits: ["update:dialog", "compra-guardada"],
  data() {
    return {
      form: this.resetearFormulario(),
      productosDisponibles: [],
      errors: {},
      errorGeneral: "",
      errorsDetalle: [],
    };
  },
  watch: {
    dialog: {
      handler(val) {
        if (val) {
          // Cargar productos primero
          this.cargarProductos().then(() => {
            // Una vez que los productos estén cargados, configurar el formulario
            if (this.compraAEditar) {
              const compra = JSON.parse(JSON.stringify(this.compraAEditar));
              // Mapear idProducto de vuelta a objetos completos para el v-autocomplete
              compra.detalleCompra = compra.detalleCompra.map((detalle) => {
                const producto = this.productosDisponibles.find(
                  (p) => p.id === detalle.idProducto
                );
                return {
                  ...detalle,
                  idProducto: producto || null, // Asignar el objeto completo o null
                };
              });
              this.form = compra;
            } else {
              this.form = this.resetearFormulario();
            }
            // Limpiar errores al abrir o resetear el diálogo
            this.errors = {};
            this.errorsDetalle = [];
            this.errorGeneral = "";
          });
        }
      },
      immediate: true,
    },
  },
  methods: {
    resetearFormulario() {
      return {
        id: null,
        proveedor: "",
        detalleCompra: [],
        monto: null,
      };
    },
    cerrarDialogo() {
      this.$emit("update:dialog", false);
    },
    async cargarProductos() {
      try {
        const response = await listProductosService();
        this.productosDisponibles = response.data.filter(
          (p) => !p.softDelete && p.tipoProducto !== "FOTOCOPIA"
        ); // Solo productos activos
      } catch (error) {
        console.error("Error al cargar productos:", error);
        this.errorGeneral = "No se pudieron cargar los productos disponibles.";
      }
    },
    agregarDetalle() {
      this.form.detalleCompra.push({ idProducto: null, cantidad: null, nombreProducto: '' });
      this.errorGeneral = ""; // limpiamos error de "Debe agregar al menos un producto"
    }
    ,
    eliminarDetalle(index) {
      this.form.detalleCompra.splice(index, 1);
      if (this.errorsDetalle[index]) {
        this.errorsDetalle.splice(index, 1);
      }
    },
    limpiarErrorDetalle(index, campo) {
      if (this.errorsDetalle[index]) {
        this.errorsDetalle[index][campo] = null;
      }
    },
    customFilter(item, queryText, itemText) {
      const nombre = item?.nombre?.toLowerCase() || "";
      const query = queryText.toLowerCase();
      return nombre.includes(query);
    },
    onProductoSeleccionado(index) {
      // Cuando return-object es true, idProducto es el objeto completo
      const selectedProduct = this.form.detalleCompra[index].idProducto;
      if (selectedProduct && selectedProduct.id) {
        // Asignamos el ID para el envío al backend y el nombre para visualización si es necesario
        this.form.detalleCompra[index].idProducto = selectedProduct.id;
        this.form.detalleCompra[index].nombreProducto = selectedProduct.nombre;
      } else {
        // Si la selección se borra o es inválida
        this.form.detalleCompra[index].idProducto = null;
        this.form.detalleCompra[index].nombreProducto = '';
      }
    },
    async guardar() {
      this.errors = {};
      this.errorGeneral = "";
      this.errorsDetalle = [];


      // Validar que haya al menos un detalle
      if (this.form.detalleCompra.length === 0) {
        this.errorGeneral = "Debe agregar al menos un producto a la compra.";
        return;
      }

      // Validar detalles individuales
      const erroresDetalle = this.form.detalleCompra.map((detalle, i) => {
        const error = {};
        if (!detalle.idProducto) error.idProducto = "Debe seleccionar un producto.";
        if (!detalle.cantidad || detalle.cantidad <= 0)
          error.cantidad = "La cantidad debe ser mayor a 0.";
        return error;
      });

      const hayErrores = erroresDetalle.some(error => Object.keys(error).length > 0);
      if (hayErrores) {
        this.errorsDetalle = erroresDetalle;
        return;
      }

      try {
        const dataToSend = {
          proveedor: this.form.proveedor,
          detalleCompra: this.form.detalleCompra.map((det) => ({
            idProducto: typeof det.idProducto === "object" ? det.idProducto.id : det.idProducto,
            cantidad: Number(det.cantidad),
          })),
          monto: this.form.monto,
        };

        if (this.form.id) {
          await updateCompraService(this.form.id, dataToSend);
        } else {
          await createCompraService(dataToSend);
        }

        this.$emit("compra-guardada");
        this.cerrarDialogo();
      } catch (error) {
        // manejo de errores como ya lo tenías
        if (error.response && error.response.data) {
          const data = error.response.data;
          if (data.messages) {
            for (const key in data.messages) {
              if (key.startsWith("detalleCompra[")) {
                const match = key.match(/detalleCompra\[(\d+)\]\.(\w+)/);
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
              "Hay errores en los datos enviados. Verificá los campos requeridos.";
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
  gap: 8px;
  /* O el espacio que prefieras para simular gap */
}
</style>