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
  getters: {
    // Getter para verificar si el nombre del usuario es 'admin'
    // Se recomienda usar 'ADMIN' en mayúsculas para evitar errores de case-sensitive
    isAdmin: (state) => state.nombre && state.nombre.toUpperCase() === 'ADMIN',
    isAuthenticated: (state) => !!state.nombre,
  }
})
