package gestionInventario.almacen;

import java.util.HashMap;
import gestionInventario.utilidades.Consola;

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
		productos.put(producto.getNombre(),producto);
	}
	
	public String getNombre() {
		return this.nombre;
	}
	
	public HashMap<String, Producto> getProductos() {
	    return this.productos;
	}
	
	public void eliminarProducto(String nombre) {
	    if (productos.remove(nombre) == null) {
	        System.out.println("No existe el producto para eliminar.");
	    } else {
	        System.out.println("Producto eliminado.");
	    }
	}
	
	public void agregarProducto(Producto producto) {
		if(!productos.containsKey(producto.getNombre())){
			productos.put(producto.getNombre(),producto);
		}
		else {
			System.out.println("EL PRODUCTO YA EXISTE!!!");
		}
	}
	
	public void compraYVenta(String nombreProducto, boolean compraOVenta) {
	    Producto producto = productos.get(nombreProducto);
	    if (producto == null) {
	        System.out.println("El producto " + nombreProducto + " no existe en la sección " + nombre);
	        return;
	    }
	    if (compraOVenta) {
	        // true compra
	    	int cantidad=Consola.leerEntero("Ingrese cantidad: ");
	    	String proveedor=Consola.leerString("Ingrese proveedor: ");
	        producto.compra(proveedor, cantidad);
	        System.out.println("Compra realizada: " + cantidad + " unidades de " + nombreProducto);
	    } 
	    else {
	        // false vende
	    	int cantidad=Consola.leerEntero("Ingrese cantidad:");
	        if (cantidad > producto.getStock()) {
	            System.out.println("No hay suficiente stock para vender " + cantidad + " unidades de " + nombreProducto);
	        }   
	        else {
	            producto.venta(cantidad);
	            System.out.println("Venta realizada: " + cantidad + " unidades de " + nombreProducto);
	        }
	    }
	}
	
	public void informacionProducto(String nombre) {
		Producto buscado = productos.get(nombre);
		if (buscado == null) {
		    System.out.println("El producto no existe en esta sección");
		    return;
		}
		buscado.informacion();

	}
	
	public void listarProductos() {
	    if (productos.isEmpty()) {
	        System.out.println("No hay productos en esta sección.");
	        return;
	    }
	    for (String key : productos.keySet()) {
		    System.out.println("- " + key);
		}
	}
	
	@Override
	public String toString() {
	    return nombre;
	}
}
