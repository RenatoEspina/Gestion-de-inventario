package gestionInventario.excepciones;

/**
 * Excepción lanzada cuando una operación de compra excede el stock máximo
 * permitido para un ProductoPremium. Es una RuntimeException.
 *
 * @author Renato Espina
 * @version 1.1 (Convertida a RuntimeException)
 */
public class StockMaximoExcedidoException extends RuntimeException {
	// Identificador único para el control de versiones durante la serialización.
    private static final long serialVersionUID = 1L;

    /**
	 * Construye una nueva excepción con el mensaje de detalle especificado.
	 * @param mensaje El mensaje de detalle.
	 */
    public StockMaximoExcedidoException(String mensaje) {
        super(mensaje);
    }
}