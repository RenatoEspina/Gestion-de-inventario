package gestionInventario.excepciones;

/**
 * Excepción lanzada cuando se intenta realizar una operación sobre un producto
 * que no existe en el inventario o sección especificada.
 * <p>
 * Esta excepción se utiliza en operaciones como:
 * </p>
 * <ul>
 *   <li>Eliminar un producto que no existe</li>
 *   <li>Vender un producto que no está registrado</li>
 *   <li>Buscar información de un producto inexistente</li>
 * </ul>
 * 
 * @author Renato Espina
 * @version 1.0
 * @see StockInsuficienteException
 */
public class ProductoNoEncontradoException extends Exception {
	// Identificador único para el control de versiones durante la serialización.
	private static final long serialVersionUID = 1L;

    /**
     * Constructor que crea una nueva excepción con un mensaje específico.
     * 
     * @param mensaje El mensaje descriptivo del error que incluye detalles
     *                sobre el producto que no fue encontrado
     */
    public ProductoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}