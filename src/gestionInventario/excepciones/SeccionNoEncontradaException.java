package gestionInventario.excepciones;

/**
 * Excepción lanzada cuando se intenta operar sobre una sección que no existe en el inventario.
 * * @author Renato Espina
 * @version 1.0
 */
public class SeccionNoEncontradaException extends Exception {
	// Identificador único para el control de versiones durante la serialización.
	private static final long serialVersionUID = 1L;
	
	/**
	 * Construye una nueva excepción con el mensaje de detalle especificado.
	 * @param mensaje El mensaje de detalle.
	 */
    public SeccionNoEncontradaException(String mensaje) {
        super(mensaje);
    }
}