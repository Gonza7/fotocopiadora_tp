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
          {{ item.softDelete ? "Inactivo" : "Activo" }}
        </v-chip>
      </template>

      <template #item.actions="{ item }">
        <v-icon small @click="openDialog(item)">mdi-pencil</v-icon>
        <v-btn
          icon
          @click="
            item.softDelete ? activarProducto(item) : deleteProducto(item)
          "
        >
          <v-icon :color="item.softDelete ? 'green' : 'red'">
            {{ item.softDelete ? "mdi-restore" : "mdi-delete" }}
          </v-icon>
        </v-btn>
      </template>
    </v-data-table>

    <v-dialog v-model="dialog" max-width="600px">
      <v-card>
        <v-card-title>
          <span class="text-h6"
            >{{ form.id ? "Editar" : "Nuevo" }} Producto</span
          >
        </v-card-title>

        <v-card-text>
          <v-form ref="formRef">
            <v-text-field v-model="form.nombre" label="Nombre" required />

            <v-select
              v-model="form.tipo"
              :items="tipos"
              item-title="label"
              item-value="value"
              label="Tipo de Producto"
              required
              :disabled="form.id != null"
            />

            <v-text-field
              v-if="form.tipo === 'PRODUCTO_VENTA'"
              v-model.number="form.precioUnitario"
              label="Precio Unitario"
              type="number"
              min="0"
              required
            />

            <v-text-field
              v-if="form.tipo === 'PRODUCTO_VENTA' || form.tipo === 'INSUMO'"
              v-model.number="form.stock"
              label="Stock"
              type="number"
              min="0"
            />

            <!-- Lista de precios para Fotocopias -->
            <div v-if="form.tipo === 'FOTOCOPIA'">
              <div
                v-for="(precio, index) in form.listaPrecios"
                :key="index"
                class="d-flex gap-2 align-center"
              >
                <v-text-field
                  v-model.number="precio.minimo"
                  label="Cantidad mínima"
                  type="number"
                  class="flex-grow-1"
                />
                <v-text-field
                  v-model.number="precio.precioUnitario"
                  label="Precio unitario"
                  type="number"
                  class="flex-grow-1"
                />
                <v-btn icon @click="form.listaPrecios.splice(index, 1)">
                  <v-icon>mdi-delete</v-icon>
                </v-btn>
              </div>
              <v-btn
                class="mt-2"
                @click="
                  form.listaPrecios.push({
                    cantidadMinima: 0,
                    precioUnitario: 0,
                  })
                "
                small
              >
                Agregar precio
              </v-btn>
            </div>

            <v-switch v-model="form.activo" label="Activo" />
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
        console.error("Error al guardar producto:", error);
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
};
</script>
