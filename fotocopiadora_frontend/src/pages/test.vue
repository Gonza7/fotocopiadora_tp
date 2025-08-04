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
    <v-data-table :headers="headers" :items="productosFiltrados" class="elevation-1" :search="search" :loading="loading"
      show-expand item-value="id" :sort-by="[{ key: 'id', order: 'asc' }]" :filter-keys="['nombre']"
      v-model:expanded="expanded">
      <template v-slot:top>
        <v-row class="ma-2" align="center" justify="space-between">
          <v-col cols="12" md="4">
            <v-text-field v-model="search" label="Buscar por nombre" dense clearable />
          </v-col>

          <v-col cols="12" md="4">
            <v-select v-model="filtroTipos" :items="tiposProducto" label="Filtrar por tipo" multiple chips dense
              clearable />
          </v-col>

          <v-col cols="12" md="4">
            <v-select v-model="filtroEstado" :items="['Activo', 'Inactivo']" label="Estado" dense clearable />
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
        <v-btn icon @click="
          item.softDelete ? activarProducto(item) : deleteProducto(item)
          " class="elevation-0">
          <v-icon :color="item.softDelete ? 'green' : 'red'">
            {{ item.softDelete ? "mdi-restore" : "mdi-delete" }}
          </v-icon>
        </v-btn>
      </template>
      <template #expanded-row="{ columns, item }">
        <tr>
          <td :colspan="columns.length">
            <v-card flat class="pa-4">
              <h3 class="text-h6 mb-4">Detalles del producto</h3>
              <v-row dense>
                <!-- Producto Venta -->
                <template v-if="item.tipoProducto === 'PRODUCTO_VENTA'">
                  <v-col cols="12" md="6">
                    <strong>Stock:</strong> {{ item.stock }}
                  </v-col>
                  <v-col cols="12" md="6">
                    <strong>Precio Unitario:</strong> ${{ item.precioUnitario.toFixed(2) }}
                  </v-col>
                </template>

                <!-- Insumo -->
                <template v-else-if="item.tipoProducto === 'INSUMO'">
                  <v-col cols="12" md="6">
                    <strong>Stock:</strong> {{ item.stock }}
                  </v-col>
                </template>

                <!-- Fotocopia -->
                <template v-else-if="item.tipoProducto === 'FOTOCOPIA'">
                  <v-col cols="12">
                    <strong class="mb-2 d-inline-block">Precios de fotocopia :</strong>
                    <v-table dense>
                      <thead>
                        <tr>
                          <th class="text-left px-1">Cantidad mínima</th>
                          <th class="text-left px-1">Precio Unitario</th>
                        </tr>
                      </thead>
                      <tbody>
                        <tr v-for="(precio, index) in item.preciosFotocopia" :key="index">
                          <td class="px-1 py-2">{{ precio.minimo }}</td>
                          <td class="px-1 py-2">${{ precio.precioUnitario.toFixed(2) }}</td>
                        </tr>
                      </tbody>
                    </v-table>
                  </v-col>

                </template>
              </v-row>
            </v-card>
          </td>
        </tr>
      </template>


    </v-data-table>

    <v-dialog v-model="dialog" max-width="600px">
      <v-card>
        <v-card-title>
          <span class="text-h6">{{ form.id ? "Editar" : "Nuevo" }} Producto</span>
        </v-card-title>

        <v-card-text>
          <v-form ref="formRef">
            <v-text-field v-model="form.nombre" label="Nombre" required
              :error-messages="errors.nombre ? [errors.nombre] : []" @input="errors.nombre = null" />

            <v-select v-model="form.tipo" :items="tipos" item-title="label" item-value="value" label="Tipo de Producto"
              required :disabled="form.id != null" :error-messages="errors.tipoProducto ? [errors.tipoProducto] : []"
              @input="errors.tipoProducto = null" />

            <v-text-field v-if="form.tipo === 'PRODUCTO_VENTA'" v-model.number="form.precioUnitario"
              label="Precio Unitario" type="number" min="0" required
              :error-messages="errors.precioUnitario ? [errors.precioUnitario] : []"
              @input="errors.precioUnitario = null" />

            <v-text-field v-if="form.tipo === 'PRODUCTO_VENTA' || form.tipo === 'INSUMO'" v-model.number="form.stock"
              label="Stock" type="number" min="0" :error-messages="errors.stock ? [errors.stock] : []"
              @input="errors.stock = null" />

            <!-- Lista de precios para Fotocopias -->
            <div v-if="form.tipo === 'FOTOCOPIA'">
              <div v-for="(precio, index) in form.listaPrecios" :key="index" class="d-flex gap-2 align-center">
                <v-text-field v-model.number="precio.minimo" label="Cantidad mínima" type="number" class="flex-grow-1"
                  :error-messages="errorsPrecios[index]?.minimo ? [errorsPrecios[index].minimo] : []"
                  @input="clearPrecioError(index, 'minimo')" />
                <v-text-field v-model.number="precio.precioUnitario" label="Precio unitario" type="number"
                  class="flex-grow-1"
                  :error-messages="errorsPrecios[index]?.precioUnitario ? [errorsPrecios[index].precioUnitario] : []"
                  @input="clearPrecioError(index, 'precioUnitario')" />
                <v-btn icon @click="form.listaPrecios.splice(index, 1)">
                  <v-icon>mdi-delete</v-icon>
                </v-btn>
              </div>
              <v-btn class="mt-2" @click="
                form.listaPrecios.push({
                  cantidadMinima: 0,
                  precioUnitario: 0,
                })
                " small>
                Agregar precio
              </v-btn>
            </div>
            <v-alert v-if="generalError" type="error" class="mt-4" variant="tonal" dismissible
              @input="generalError = ''">
              {{ generalError }}
            </v-alert>
          </v-form>
        </v-card-text>

        <v-card-actions>
          <v-spacer />
          <v-btn text @click="dialog = false">Cancelar</v-btn>
          <v-btn color="primary" @click="save">Guardar</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-container>
</template>

<script>
import {
  getProductoService,
  listProductosService,
  createProductoService,
  updateProductoService,
  deleteProductoService,
  activateProductoService,
} from "@/services/productoService";
export default {
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
        { title: "", key: "data-table-expand" }
      ],
      productos: [],
      tiposProducto: ["INSUMO", "PRODUCTO_VENTA", "FOTOCOPIA"],
      dialog: false,
      form: this.resetForm(),
      tipos: [
        { label: "Producto Venta", value: "PRODUCTO_VENTA" },
        { label: "Insumo", value: "INSUMO" },
        { label: "Fotocopia", value: "FOTOCOPIA" },
      ],
      filtroTipos: [],
      filtroEstado: null,
      expanded: [],
      errors: {},
      generalError: '',
      errorsPrecios: [],
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
      this.errors = {};
      this.errorsPrecios = [];
      this.generalError = '';

      if (producto) {
        const formCopy = JSON.parse(JSON.stringify(producto));
        formCopy.tipo = formCopy.tipoProducto;

        if (formCopy.tipo === "FOTOCOPIA") {
          formCopy.listaPrecios = formCopy.preciosFotocopia;
        }

        this.form = formCopy;
      } else {
        this.form = this.resetForm();
      }

      this.dialog = true;
    },
    async save() {
      this.errors = {};
      this.generalError = '';
      this.errorsPrecios = [];
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

        this.dialog = false;
        this.listProductos();
      } catch (error) {
        if (error.response && error.response.data) {
          const data = error.response.data;
          if (data.messages) {
            for (const key in data.messages) {
              if (key.startsWith("preciosFotocopia[")) {
                const match = key.match(/preciosFotocopia\[(\d+)\]\.(\w+)/);
                if (match) {
                  const index = parseInt(match[1]);
                  const field = match[2];
                  if (!this.errorsPrecios[index]) this.errorsPrecios[index] = {};
                  this.errorsPrecios[index][field] = data.messages[key];
                }
              } else {
                this.errors[key] = data.messages[key];
              }
            }
          }
          else if (data.message) {
            this.generalError = data.message;
          } else if (error.response.status === 400) {
            this.generalError = 'Hay errores en los datos enviados. Verificá que todos los campos requeridos estén completos.';
          } else {
            this.generalError = 'Ocurrió un error inesperado.';
          }
        } else {
          this.generalError = 'No se pudo conectar con el servidor.';
        }
      }
    },
    async deleteProducto(producto) {
      await deleteProductoService(producto.id);
      this.listProductos();
    },
    async activarProducto(producto) {
      await activateProductoService(producto.id);
      this.listProductos();
    },
    tipoLabel(tipo) {
      return tipo;
    },
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
    clearPrecioError(index, campo) {
      if (this.errorsPrecios[index]) {
        this.errorsPrecios[index][campo] = null;
      }
    }
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
    dialog(val) {
      if (!val) {
        // El formulario se cerró
        this.errors = {};
        this.errorsPrecios = [];
        this.generalError = '';
      }
    }
  }
};
</script>
