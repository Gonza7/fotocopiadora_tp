import axios from "axios";
const API_URL = 'http://localhost:8080/api/producto';

export const listProductosService = () => axios.get(`${API_URL}`);
export const listProductosActiveService = () => axios.get(`${API_URL}/active`);
export const getProductoService = (id) => axios.get(`${API_URL}/${id}`);
export const createProductoService = (data) => axios.post(`${API_URL}`, data);
export const updateProductoService = (id, data) => axios.put(`${API_URL}/${id}`, data);
export const deleteProductoService = (id) => axios.put(`${API_URL}/delete/${id}`);
export const activateProductoService = (id) => axios.put(`${API_URL}/activate/${id}`);