<template>
  <v-card flat class="pa-4">
    <h3 class="text-h6 mb-4">Detalle de la Venta</h3>
    <v-table dense>
      <thead>
        <tr>
          <th class="text-left px-1">Producto</th>
          <th class="text-left px-1">Cantidad</th>
          <th class="text-left px-1">Precio Unitario</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(detalle, index) in venta.detallesVenta" :key="index">
          <td class="px-1 py-2">{{ detalle.nombreProducto }}</td>
          <td class="px-1 py-2">{{ detalle.cantidad }}</td>
          <td class="px-1 py-2">${{ detalle.precioUnitario.toFixed(2) }}</td>
        </tr>
      </tbody>
    </v-table>
  </v-card>
</template>

<script>
export default {
  props: {
    venta: {
      type: Object,
      required: true,
    },
  },
};
</script>