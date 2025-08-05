export function handleBackendError(error, { setFieldErrors, setListaErrores, setGeneralError }) {
  if (!error.response) {
    setGeneralError('Error de conexión con el servidor.');
    return;
  }

  const data = error.response.data;

  // Validaciones por campos (por ejemplo: nombre, stock, etc.)
  if (data.messages && typeof data.messages === 'object') {
    setFieldErrors(data.messages);
  }

  // Validaciones de lista (como precios de fotocopias)
  else if (Array.isArray(data.errors)) {
    const listaErrores = data.errors.map(e => {
      const err = {};
      if (e.minimo) err.minimo = e.minimo;
      if (e.precioUnitario) err.precioUnitario = e.precioUnitario;
      return err;
    });
    setListaErrores(listaErrores);
  }

  // Mensaje general (por ejemplo: BusinessException)
  else if (data.message) {
    setGeneralError(data.message);
  }

  // Fallback
  else {
    setGeneralError('Ocurrió un error inesperado.');
  }
}
