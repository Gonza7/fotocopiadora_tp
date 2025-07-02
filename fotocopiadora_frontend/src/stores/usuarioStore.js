import { defineStore } from 'pinia'

export const useUsuarioStore = defineStore('usuario', {
  state: () => ({
    nombre: '' // Este valor se cargará al iniciar sesión
  }),
  actions: {
    setNombre(nombre) {
      this.nombre = nombre
    },
    clear() {
      this.nombre = ''
    }
  },
})
