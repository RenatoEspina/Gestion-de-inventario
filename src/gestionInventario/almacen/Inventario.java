package gestionInventario.almacen;

import gestionInventario.utilidades.Consola;
import java.util.HashMap;

public class Inventario {
	private HashMap<String,Secciones> secciones;
	
	public Inventario() {
		this.secciones = new HashMap<>();
	}
	
	public HashMap<String, Secciones> getSecciones() {
	    return this.secciones;
	}
	
	public void nuevaSeccion(String nombre) {
		if(!this.secciones.containsKey(nombre)) {
			Secciones seccion= new Secciones(nombre);
			this.secciones.put(nombre, seccion);
		}
		
		else {
			System.out.println("LA SECCION YA EXISTE!!!");
		}
	}

	public void eliminarProducto(String Nombre) {
		for(Secciones s: secciones.values()) {
			System.out.println("Buscando...");
			Producto p=s.getProductos().get(Nombre);
			if(p!=null) {
				Consola.limpiarPantalla();
				System.out.println("El producto se encontro en la seccion " + s.getNombre());
				String respuesta= Consola.leerString("Desea eliminarlo? (si/no): ");
				if(respuesta.equals("si")) {
					if(s.getProductos().remove(Nombre, p)){
						System.out.print("Eliminado con exito!!!");
						return ;
					}
					System.out.print("La eliminacion fallo");
					return ;
				}
			}
		}
		System.out.print("No se encontro el producto en sistema");
		return;
	}
	
	public void agregarProducto(Producto productos) {
		System.out.println("Ah que seccion desea agregar el producto?");
		for (String key : secciones.keySet()) {
		    System.out.println("- " + key);
		}
		String seccionBuscada=Consola.leerString(null);
		Secciones seccion = this.secciones.get(seccionBuscada);
		if(seccion==null) {
			System.out.println("LA SECCION BUSCADA NO EXISTE EN EL SISTEMA!!");
			return;
		}
		seccion.agregarProducto(productos);
	}
	
	public void agregarProducto(String seccionNombre, Producto producto) {
	    Secciones seccion = this.secciones.get(seccionNombre);
	    if (seccion == null) {
	        System.out.println("LA SECCIÓN " + seccionNombre + " NO EXISTE EN EL SISTEMA!!");
	        return;
	    }
	    seccion.agregarProducto(producto);
	}
	
	public void compraYVenta() {
		System.out.println("Ah que seccion desea ingresar?");
		for (String key : secciones.keySet()) {
		    System.out.println("- " + key);
		}
		String seccionBuscada = Consola.leerString(null);
		Secciones seccion = secciones.get(seccionBuscada);
		if(seccion==null) {
			System.out.println("LA SECCION BUSCADA NO EXISTE EN EL SISTEMA!!");
			return;
		}
        String nombreProducto = Consola.leerString("Ingrese nombre del producto: ");
		String opcion = Consola.leerString("desea ingresar una compra o una venta?");
		if(opcion.equals("compra")) {
			seccion.compraYVenta(nombreProducto, true);
		}
		else {
			seccion.compraYVenta(nombreProducto, false);
		}
	}
	
	public void informacionProducto() {
		System.out.println("Ah que seccion desea ingresar?");
		for (String key : secciones.keySet()) {
		    System.out.println("- " + key);
		}
		String seccionBuscada=Consola.leerString(null);
		Secciones seccion = secciones.get(seccionBuscada);
		if(seccion==null) {
			System.out.println("LA SECCION BUSCADA NO EXISTE EN EL SISTEMA!!");
			return;
		}
		Consola.limpiarPantalla();
		System.out.println("Seccion ingresada:" + seccionBuscada);
		seccion.listarProductos();
        String nombreProducto = Consola.leerString("Ingrese nombre del producto: ");
        seccion.informacionProducto(nombreProducto);
	}
	
}
