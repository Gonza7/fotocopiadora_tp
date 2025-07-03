import axios from "axios";

const API_URL = 'http://localhost:8080/api/usuario'

export const createUsuarioService = (data) => axios.post(`${API_URL}`, data);
export const updateUsuarioService = (id, data) => axios.put(`${API_URL}/${id}`, data);
export const deleteUsuarioService = (id) => axios.delete(`${API_URL}/${id}`);
export const listUsuariosService = () => axios.get(`${API_URL}`);
export const getUsuarioService = (id) => axios.get(`${API_URL}/${id}`);
export const loginService = (data) => axios.post(`${API_URL}/login`, data);