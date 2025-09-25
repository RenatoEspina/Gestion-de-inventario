package gestionInventario.excepciones;

/**
 * Excepción lanzada cuando una operación de compra excede el stock máximo
 * permitido para un ProductoPremium. Es una RuntimeException.
 *
 * @author Renato Espina
 * @version 1.1 (Convertida a RuntimeException)
 */
public class StockMaximoExcedidoException extends RuntimeException { // Este es el único cambio necesario
    private static final long serialVersionUID = 1L;

    public StockMaximoExcedidoException(String mensaje) {
        super(mensaje);
    }
}