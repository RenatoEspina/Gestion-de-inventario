package gestionInventario.almacen;
import java.util.HashMap;
import java.util.Scanner;

public class Inventario {
	private String nombreEmpresa;
	private HashMap<String,Secciones> secciones;
	
	public Inventario(String nombreEmpresa) {
		this.nombreEmpresa=nombreEmpresa;
		this.secciones = new HashMap<>();
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
	
	public void nuevaSeccion(String nombre, Producto producto) {
	    if(!this.secciones.containsKey(nombre)) {
	        Secciones seccion = new Secciones(nombre, producto);
	        this.secciones.put(nombre, seccion);
	    } else {
	        System.out.println("La sección ya existe!");
	    }
	}

	public void agregarProducto(Producto productos) {
		Scanner sc = new Scanner(System.in);
		System.out.println("Ah que seccion desea agregar el producto?");
		System.out.println(secciones);
		String seccionBuscada=sc.nextLine();
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

	public HashMap<String, Secciones> getSecciones() {
	    return this.secciones;
	}
	
	public void compraYVenta() {
		Scanner sc = new Scanner(System.in);
		System.out.println("Ah que seccion desea ingresar?");
		System.out.println(secciones);
		String seccionBuscada=sc.nextLine();
		Secciones seccion = secciones.get(seccionBuscada);
		if(seccion==null) {
			System.out.println("LA SECCION BUSCADA NO EXISTE EN EL SISTEMA!!");
			return;
		}
		System.out.print("Ingrese nombre del producto: ");
        String nombreProducto = sc.nextLine();
        System.out.print("Ingrese proveedor: ");
        String proveedor = sc.nextLine();
		System.out.println("desea ingresar una compra o una venta?");
		String opcion=sc.nextLine();
		if(opcion.equals("compra")) {
			seccion.compraYVenta(nombreProducto, true, proveedor);
		}
		else {
			seccion.compraYVenta(nombreProducto, false, proveedor);
		}
	}
	
	public void informacionProducto() {
		Scanner sc = new Scanner(System.in);
		System.out.println("Ah que seccion desea ingresar?");
		System.out.println(secciones);
		String seccionBuscada=sc.nextLine();
		Secciones seccion = secciones.get(seccionBuscada);
		if(seccion==null) {
			System.out.println("LA SECCION BUSCADA NO EXISTE EN EL SISTEMA!!");
			return;
		}
		System.out.print("Ingrese nombre del producto: ");
        String nombreProducto = sc.nextLine();
        seccion.informacionProducto(nombreProducto);
	}
	
}
