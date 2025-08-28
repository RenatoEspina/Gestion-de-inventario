package gestionInventario.almacen;
import java.util.HashMap;
import java.util.Scanner;

public class Inventario {
	private HashMap<String,Secciones> secciones;
	
	public Inventario() {
		secciones = new HashMap<>();
	}
	
	public void nuevaSeccion(String nombre) {
		if(!secciones.containsKey(nombre)) {
			Secciones seccion= new Secciones(nombre);
			this.secciones.put(nombre, seccion);
		}
		
		else {
			System.out.println("LA SECCION YA EXISTE!!!");
		}
	}
	
	public void agregarProducto(Producto productos) {
		Scanner sc = new Scanner(System.in);
		System.out.println("Ah que seccion desea agregar el producto?");
		System.out.println(secciones);
		String seccionBuscada=sc.nextLine();
		Secciones seccion = secciones.get(seccionBuscada);
		if(seccion==null) {
			System.out.println("LA SECCION BUSCADA NO EXISTE EN EL SISTEMA!!");
			return;
		}
		seccion.agregarProducto(productos);
	}
	
	public void compraYVenta(String nombreProducto, String proveedor, int cantidad) {
		Scanner sc = new Scanner(System.in);
		System.out.println("Ah que seccion desea ingresar?");
		System.out.println(secciones);
		String seccionBuscada=sc.nextLine();
		Secciones seccion = secciones.get(seccionBuscada);
		if(seccion==null) {
			System.out.println("LA SECCION BUSCADA NO EXISTE EN EL SISTEMA!!");
			return;
		}
		System.out.println("desea ingresar una compra o una venta?");
		String opcion=sc.nextLine();
		if(opcion.equals("compra")) {
			seccion.CompraYventa(nombreProducto, true, proveedor, cantidad);
		}
		else {
			seccion.CompraYventa(nombreProducto, false, proveedor, cantidad);
		}
	}
	
}
