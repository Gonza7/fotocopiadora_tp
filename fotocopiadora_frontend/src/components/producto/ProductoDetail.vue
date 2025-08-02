<template>
  <v-card flat class="pa-4">
    <h3 class="text-h6 mb-4">Detalles del producto</h3>
    <v-row dense>
      <template v-if="product.tipoProducto === 'PRODUCTO_VENTA'">
        <v-col cols="12" md="6">
          <strong>Stock:</strong> {{ product.stock }}
        </v-col>
        <v-col cols="12" md="6">
          <strong>Precio Unitario:</strong> ${{ product.precioUnitario.toFixed(2) }}
        </v-col>
      </template>

      <template v-else-if="product.tipoProducto === 'INSUMO'">
        <v-col cols="12" md="6">
          <strong>Stock:</strong> {{ product.stock }}
        </v-col>
      </template>

      <template v-else-if="product.tipoProducto === 'FOTOCOPIA'">
        <v-col cols="12">
          <strong class="mb-2 d-inline-block">Precios de fotocopia:</strong>
          <v-table dense>
            <thead>
              <tr>
                <th class="text-left px-1">Cantidad mínima</th>
                <th class="text-left px-1">Precio Unitario</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(precio, index) in product.preciosFotocopia" :key="index">
                <td class="px-1 py-2">{{ precio.minimo }}</td>
                <td class="px-1 py-2">${{ precio.precioUnitario.toFixed(2) }}</td>
              </tr>
            </tbody>
          </v-table>
        </v-col>
      </template>
    </v-row>
  </v-card>
</template>

<script>
export default {
  props: {
    product: {
      type: Object,
      required: true,
    },
  },
};
</script>