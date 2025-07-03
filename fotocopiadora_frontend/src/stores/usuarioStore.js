import { defineStore } from 'pinia'

export const useUsuarioStore = defineStore('usuario', {
  state: () => ({
    nombre: JSON.parse(localStorage.getItem('nombre')) || null,
  }),
  actions: {
    setNombre(data) {
      this.nombre = data;
      localStorage.setItem('nombre', JSON.stringify(data));
    },
    clear() {
      this.nombre = null,
      localStorage.removeItem('nombre')
    }
  },
})
