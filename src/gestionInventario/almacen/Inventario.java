package gestionInventario.almacen;

import gestionInventario.utilidades.Consola;
import gestionInventario.utilidades.ExportadorExcel;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import gestionInventario.excepciones.*;

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

	public void eliminarSeccion(String nombre) {
		if(this.secciones.containsKey(nombre)) {
			secciones.remove(nombre);
		}
		
		else {
			System.out.println("LA SECCION NO EXISTE!!!");
		}
	}
	
	public void eliminarProducto(String Nombre) throws ProductoNoEncontradoException {
	    for(Secciones s: secciones.values()) {
	        Producto p=s.getProductos().get(Nombre);
	        if(p!=null) {
	            if(s.getProductos().remove(Nombre,p)){
	                System.out.println("Eliminado con exito!!!");
	                return;
	            }
	        }
	    }
	    throw new ProductoNoEncontradoException("No se encontró el producto " + Nombre + " en el sistema.");
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
	
	public void comprarProducto(String nombreProducto, int cantidad, String proveedor) {
	    System.out.println("¿A qué sección desea ingresar?");
	    for (String key : secciones.keySet()) {
	        System.out.println("- " + key);
	    }
	    String seccionBuscada = Consola.leerString(null);
	    Secciones seccion = secciones.get(seccionBuscada);
	    if (seccion == null) {
	        System.out.println("LA SECCIÓN BUSCADA NO EXISTE EN EL SISTEMA!!");
	        return;
	    }
	    seccion.comprarProducto(nombreProducto, cantidad, proveedor);
	}

	public void venderProducto(String nombreProducto, int cantidad) throws ProductoNoEncontradoException, StockInsuficienteException {
	    System.out.println("¿A qué sección desea ingresar?");
	    for (String key : secciones.keySet()) {
	        System.out.println("- " + key);
	    }
	    String seccionBuscada = Consola.leerString(null);
	    Secciones seccion = secciones.get(seccionBuscada);
	    if (seccion == null) {
	        throw new ProductoNoEncontradoException("La sección " + seccionBuscada + " no existe en el sistema.");
	    }
	    seccion.venderProducto(nombreProducto, cantidad);
	}
	
	public void filtrarProductos(int ventas) {
	    int sumatoriaTotal = 0;
	    boolean encontroAlMenosUno = false;

	    // Lista para guardar secciones que cumplen el criterio
	    List<Secciones> seccionesFiltradas = new ArrayList<>();

	    for (Secciones s : secciones.values()) {
	        int sumatoriaSec = 0;
	        boolean encontroEnSeccion = false;

	        System.out.println("\n-Sección: " + s.getNombre());

	        for (Producto p : s.getProductos().values()) {
	            if (p.getVentasTotales() >= ventas) {
	                encontroEnSeccion = true;
	                encontroAlMenosUno = true;
	                sumatoriaSec += p.getVentasTotales();
	                System.out.println("   - " + p.getNombre() + " tiene " + p.getVentasTotales() + " ventas.");
	            }
	        }

	        if (!encontroEnSeccion) {
	            System.out.println("   No existen productos con las ventas solicitadas en esta sección.");
	        } else {
	            sumatoriaTotal += sumatoriaSec;
	            seccionesFiltradas.add(s); // Guardamos la sección que tiene productos que cumplen el criterio
	            System.out.println("   Total de ventas en la sección: " + sumatoriaSec);
	        }
	    }

	    if (!encontroAlMenosUno) {
	        System.out.println("\nNo se encontraron productos que cumplan el criterio.");
	    } else {
	        System.out.println("\nHubo un total de " + sumatoriaTotal + " ventas en todas las secciones.");
	    }

	    String opcion = Consola.leerString("¿Desea generar un archivo reporte? (si/no): ");
	    if (opcion.equalsIgnoreCase("si")) {
	    	ExportadorExcel.generarReporte(seccionesFiltradas, ventas);
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
