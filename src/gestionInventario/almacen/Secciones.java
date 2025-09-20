package gestionInventario.almacen;
import java.util.HashMap;
import java.util.Scanner;

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
	
	public void agregarProducto(Producto producto) {
		if(!productos.containsKey(producto.getNombre())){
			productos.put(producto.getNombre(),producto);
		}
		else {
			System.out.println("EL PRODUCTO YA EXISTE!!!");
		}
	}
	
	public void agregarProducto(String nombre, String proveedor, int cantidad) {
	    Producto producto = new Producto(nombre, proveedor, cantidad);
	    agregarProducto(producto);
	}
	
	public void compraYVenta(String nombreProducto, boolean compraOVenta, String proveedor) {
		Scanner sc= new Scanner(System.in);
	    Producto producto = productos.get(nombreProducto);
	    if (producto == null) {
	        System.out.println("El producto " + nombreProducto + " no existe en la sección " + nombre);
	        return;
	    }
	    if (compraOVenta) {
	        // true compra
	    	System.out.println("Ingrese cantidad:");
	    	int cantidad=sc.nextInt();
	    	sc.nextLine();
	        producto.compra(proveedor, cantidad);
	        System.out.println("Compra realizada: " + cantidad + " unidades de " + nombreProducto);
	    } 
	    else {
	        // false vende
	    	System.out.println("Ingrese cantidad:");
	    	int cantidad=sc.nextInt();
	    	sc.nextLine();
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
		buscado.informacion();
	}
	
	@Override
	public String toString() {
	    return" Productos: " + productos.keySet();
	}
}
