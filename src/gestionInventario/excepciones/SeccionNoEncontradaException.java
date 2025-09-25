package gestionInventario.excepciones;

/**
 * Excepción lanzada cuando se intenta operar sobre una sección que no existe en el inventario.
 * * @author Renato Espina
 * @version 1.0
 */
public class SeccionNoEncontradaException extends Exception {
	private static final long serialVersionUID = 1L;
    public SeccionNoEncontradaException(String mensaje) {
        super(mensaje);
    }
}