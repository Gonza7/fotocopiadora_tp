import axios from "axios"; 
const API_URL = 'http://localhost:8080/api/compra';

export const listComprasService = () => axios.get(`${API_URL}`);
export const listComprasActiveService = () => axios.get(`${API_URL}/active`);
export const getCompraService = (id) => axios.get(`${API_URL}/${id}`);
export const createCompraService = (data) => axios.post(`${API_URL}`, data);
export const updateCompraService = (id, data) => axios.put(`${API_URL}/${id}`, data);
export const deleteCompraService = (id) => axios.put(`${API_URL}/delete/${id}`);
export const activateCompraService = (id) => axios.put(`${API_URL}/activate/${id}`);