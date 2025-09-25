package gestionInventario.excepciones;

/**
 * Excepción lanzada al intentar vender un ProductoPerecible cuya fecha de vencimiento ya pasó.
 * Al ser una RuntimeException, no necesita ser declarada en la firma de los métodos.
 *
 * @author Renato Espina
 * @version 1.1 (Convertida a RuntimeException)
 */
public class ProductoVencidoException extends RuntimeException { // El cambio clave está aquí
    private static final long serialVersionUID = 1L;
    
    public ProductoVencidoException(String mensaje) {
        super(mensaje);
    }
}