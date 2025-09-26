package gestionInventario.excepciones;

/**
 * Excepción lanzada cuando se intenta realizar una operación de venta o retiro
 * de productos pero no hay suficiente stock disponible.
 * <p>
 * Esta excepción previene operaciones inválidas que podrían resultar en
 * valores de stock negativos. Se utiliza específicamente en operaciones de venta
 * donde la cantidad solicitada excede el stock actual del producto.
 * </p>
 * 
 * @author Renato Espina
 * @version 1.0
 * @see ProductoNoEncontradoException
 */
public class StockInsuficienteException extends Exception {
	// Identificador único para el control de versiones durante la serialización.
    private static final long serialVersionUID = 1L;

    /**
     * Constructor que crea una nueva excepción con un mensaje específico.
     * 
     * @param mensaje El mensaje descriptivo del error que incluye detalles
     *                sobre el producto, la cantidad solicitada y el stock disponible
     */
    public StockInsuficienteException(String mensaje) {
        super(mensaje);
    }
}