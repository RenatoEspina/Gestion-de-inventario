package gestionInventario.excepciones;

public class ProductoNoEncontradoException extends Exception {
    private static final long serialVersionUID = 1L;

    public ProductoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}

