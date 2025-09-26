package gestionInventario.excepciones;

/**
 * Excepción lanzada al intentar vender un ProductoPerecible cuya fecha de vencimiento ya pasó.
 * Al ser una RuntimeException, no necesita ser declarada en la firma de los métodos.
 *
 * @author Renato Espina
 * @version 1.1 (Convertida a RuntimeException)
 */
public class ProductoVencidoException extends RuntimeException { 
	// Identificador único para el control de versiones durante la serialización.
    private static final long serialVersionUID = 1L;
    
    /**
     * Construye una nueva excepción con el mensaje de detalle especificado.
     * @param mensaje El mensaje de detalle.
     */
    public ProductoVencidoException(String mensaje) {
        super(mensaje);
    }
}