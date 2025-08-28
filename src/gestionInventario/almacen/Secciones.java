package gestionInventario.almacen;
import java.util.HashMap;

public class Secciones {
	private String nombre;
	private HashMap<String,Producto> productos;
	
	public Secciones(String nombre) {
		this.nombre=nombre;
		this.productos= new HashMap<>();
	}
	
	public Secciones(String nombre, Producto producto) {
		this.nombre=nombre;
		this.productos= new HashMap<>();
		productos.put(producto.nombre(),producto);
	}
	
	public String nombre() {
		return this.nombre;
	}
	
	public void agregarProducto(Producto producto) {
		if(!productos.containsKey(producto.nombre())){
			productos.put(producto.nombre(),producto);
		}
		
		else {
			System.out.println("EL PRODUCTO YA EXISTE!!!");
		}
	}
	
	public void CompraYventa(String nombreProducto, boolean compraOVenta, String proveedor, int cantidad) {
	    Producto producto = productos.get(nombreProducto);
	    if (producto == null) {
	        System.out.println("El producto " + nombreProducto + " no existe en la sección " + nombre);
	        return;
	    }
	    if (compraOVenta) {
	        // true compra
	        producto.compra(proveedor, cantidad);
	        System.out.println("Compra realizada: " + cantidad + " unidades de " + nombreProducto);
	    } 
	    else {
	        // false vende
	        if (cantidad > producto.getStock()) {
	            System.out.println("No hay suficiente stock para vender " + cantidad + " unidades de " + nombreProducto);
	        }   
	        else {
	            producto.venta(cantidad);
	            System.out.println("Venta realizada: " + cantidad + " unidades de " + nombreProducto);
	        }
	    }
	}
	
}
