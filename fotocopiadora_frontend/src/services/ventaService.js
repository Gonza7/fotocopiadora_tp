import axios from 'axios'

const API_URL = 'http://localhost:8080/api/ventas'

export const listVentasService = () => axios.get(`${API_URL}`)
export const listVentasActiveService = () => axios.get(`${API_URL}/active`)
export const getVentaService = (id) => axios.get(`${API_URL}/${id}`)
export const createVentaService = (data) => axios.post(`${API_URL}`, data)
export const updateVentaService = (id, data) => axios.put(`${API_URL}/${id}`, data)
export const deleteVentaService = (id) => axios.put(`${API_URL}/delete/${id}`)
export const activateVentaService = (id) => axios.put(`${API_URL}/activate/${id}`)

