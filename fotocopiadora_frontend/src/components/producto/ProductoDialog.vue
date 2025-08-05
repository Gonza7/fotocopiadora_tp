<template>
  <v-dialog :model-value="dialog" @update:model-value="closeDialog" max-width="600px">
    <v-card>
      <v-card-title>
        <span class="text-h6">{{ form.id ? "Editar" : "Nuevo" }} Producto</span>
      </v-card-title>

      <v-card-text>
        <v-form ref="formRef">
          <!-- Nombre -->
          <v-text-field v-model="form.nombre" label="Nombre" required
            :error-messages="errors.nombre ? [errors.nombre] : []" @input="errors.nombre = null" />

          <!-- Tipo de Producto -->
          <v-select v-model="form.tipo" :items="tipos" item-title="label" item-value="value" label="Tipo de Producto"
            required :disabled="form.id != null" :error-messages="errors.tipo ? [errors.tipo] : []"
            @update:model-value="handleTipoChange" />

          <!-- Precio Unitario (solo para PRODUCTO_VENTA) -->
          <v-text-field v-if="form.tipo === 'PRODUCTO_VENTA'" v-model.number="form.precioUnitario"
            label="Precio Unitario" type="text" inputmode="numeric" min="0" required
            :error-messages="errors.precioUnitario ? [errors.precioUnitario] : []"
            @input="errors.precioUnitario = null" />

          <!-- Stock (PRODUCTO_VENTA o INSUMO) -->
          <v-text-field v-if="form.tipo === 'PRODUCTO_VENTA' || form.tipo === 'INSUMO'" v-model.number="form.stock"
            label="Stock" type="text" inputmode="numeric" min="0" :error-messages="errors.stock ? [errors.stock] : []"
            @input="errors.stock = null" />

          <!-- Lista de Precios (solo FOTOCOPIA) -->
          <div v-if="form.tipo === 'FOTOCOPIA'">
            <div v-for="(precio, index) in form.listaPrecios" :key="index" class="d-flex gap-2 align-center mb-2">
              <!-- Cantidad mínima -->
              <v-text-field v-model.number="precio.minimo" label="Cantidad mínima" type="text" inputmode="numeric"
                class="flex-grow-1" :error-messages="errorsPrecios[index]?.minimo ? [errorsPrecios[index].minimo] : []"
                @input="clearPrecioError(index, 'minimo')" />

              <!-- Precio unitario -->
              <v-text-field v-model.number="precio.precioUnitario" label="Precio unitario" type="text"
                inputmode="numeric" class="flex-grow-1"
                :error-messages="errorsPrecios[index]?.precioUnitario ? [errorsPrecios[index].precioUnitario] : []"
                @input="clearPrecioError(index, 'precioUnitario')" />

              <!-- Botón eliminar -->
              <v-btn icon @click="removePrecio(index)">
                <v-icon>mdi-delete</v-icon>
              </v-btn>
            </div>

            <!-- Botón agregar precio -->
            <v-btn class="mt-2" @click="addPrecio" small>
              Agregar precio
            </v-btn>
          </div>

          <!-- Alerta de error general -->
          <v-alert v-if="generalError" type="error" class="mt-4" variant="tonal" dismissible @input="generalError = ''">
            {{ generalError }}
          </v-alert>
        </v-form>
      </v-card-text>

      <v-card-actions>
        <v-spacer />
        <v-btn text @click="closeDialog">Cancelar</v-btn>
        <v-btn color="primary" @click="save">Guardar</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>


<script>
import { createProductoService, updateProductoService } from "@/services/productoService";
import { handleBackendError } from "@/utils/ErrorHandler.js";

export default {
  props: {
    dialog: {
      type: Boolean,
      required: true,
    },
    productToEdit: {
      type: Object,
      default: null,
    },
  },
  emits: ["update:dialog", "product-saved"],
  data() {
    return {
      form: this.resetForm(),
      tipos: [
        { label: "Producto Venta", value: "PRODUCTO_VENTA" },
        { label: "Insumo", value: "INSUMO" },
        { label: "Fotocopia", value: "FOTOCOPIA" },
      ],
      errors: {},
      generalError: "",
      errorsPrecios: [],
    };
  },
  watch: {
    productToEdit: {
      handler(newVal) {
        if (newVal) {
          this.form = { ...newVal };
        } else {
          this.form = this.resetForm();
        }
        this.errors = {};
        this.errorsPrecios = [];
        this.generalError = "";
      },
      immediate: true,
    },
  },
  methods: {
    resetForm() {
      return {
        id: null,
        nombre: "",
        tipo: "",
        precioUnitario: null,
        stock: null,
        listaPrecios: [],
      };
    },
    closeDialog() {
      this.$emit("update:dialog", false);
    },
    handleTipoChange() {
      if (this.form.tipo !== 'PRODUCTO_VENTA') {
        this.form.precioUnitario = null;
      }
      if (this.form.tipo !== 'PRODUCTO_VENTA' && this.form.tipo !== 'INSUMO') {
        this.form.stock = null;
      }
      if (this.form.tipo !== 'FOTOCOPIA') {
        this.form.listaPrecios = [];
      }
    },
    addPrecio() {
      this.form.listaPrecios.push({ minimo: null, precioUnitario: null });
    },
    removePrecio(index) {
      this.form.listaPrecios.splice(index, 1);
      if (this.errorsPrecios[index]) {
        this.errorsPrecios.splice(index, 1);
      }
    },
    clearPrecioError(index, campo) {
      if (this.errorsPrecios[index]) {
        this.errorsPrecios[index][campo] = null;
      }
    },
    async save() {
      this.errors = {};
      this.generalError = "";
      this.errorsPrecios = [];

      if (!this.form.tipo) {
        this.errors.tipo = "Debe seleccionar un tipo de producto.";
        return;
      }
      
      if (this.form.tipo === "FOTOCOPIA" && this.form.listaPrecios.length === 0) {
        this.generalError = "Debe agregar al menos un precio.";
        return;
      }

      if (this.form.tipo === "FOTOCOPIA") {
        const errores = this.form.listaPrecios.map((precio) => {
          const error = {};
          if (precio.minimo == null || precio.minimo < 0) {
            error.minimo = "La cantidad mínima debe ser mayor o igual a 0.";
          }
          if (precio.precioUnitario == null || precio.precioUnitario <= 0) {
            error.precioUnitario = "El precio debe ser mayor a 0.";
          }
          return error;
        });

        const hayErrores = errores.some((e) => Object.keys(e).length > 0);
        if (hayErrores) {
          this.errorsPrecios = errores;
          return;
        }
      }

      try {
        const dataToSend = {
          ...this.form,
          tipoProducto: this.form.tipo,
        };

        if (this.form.tipo === "FOTOCOPIA") {
          dataToSend.preciosFotocopia = this.form.listaPrecios;
          delete dataToSend.listaPrecios;
        }

        if (this.form.id) {
          await updateProductoService(this.form.id, dataToSend);
        } else {
          await createProductoService(dataToSend);
        }

        this.$emit("product-saved");
        this.closeDialog();
      } catch (error) {
        handleBackendError(error, {
          setFieldErrors: (errs) => { this.errors = errs; },
          setListaErrores: (errs) => { this.errorsPrecios = errs; },
          setGeneralError: (msg) => { this.generalError = msg; },
        });
      }
    }
  },
};
</script>


<style scoped>
.gap-2 {
  gap: 8px;
  /* O el espacio que prefieras para simular gap */
}
</style>