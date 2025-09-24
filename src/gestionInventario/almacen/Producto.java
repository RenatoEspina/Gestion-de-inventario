package gestionInventario.almacen;

import java.util.ArrayList;

public class Producto {
	private String nombre;
	private ArrayList<String> proveedores;
	private int stock;
	private int compras;
	private int ventas;
	
	public Producto(String nombre, String proveedor, int compra) {
		this.nombre=nombre;
		this.compras=compra;
		this.stock=compra;
		this.ventas=0;
		this.proveedores= new ArrayList<>();
		this.proveedores.add(proveedor);
	}
	
	public int getStock() {
	    return this.stock;
	}
	
	public String getNombre() {
		return this.nombre;
	}
	
	public ArrayList<String> getProveedores() {
	    return this.proveedores;
	}

	public int getComprasTotales() {
	    return this.compras;
	}

	public int getVentasTotales() {
	    return this.ventas;
	}

	public void agregarProveedor(String proveedor) {
	    if (!this.proveedores.contains(proveedor)) {
	        this.proveedores.add(proveedor);
	    }
	}

	public void ajustarComprasVentas(int comprasTotales, int ventasTotales) {
	    this.compras = comprasTotales;
	    this.ventas = ventasTotales;
	    this.stock = comprasTotales - ventasTotales;
	}
	
	public void compra(String proveedor, int compra) {
		if(!this.proveedores.contains(proveedor)) {
			this.proveedores.add(proveedor);
		}
		this.compras+=compra;
		this.stock+=compra;
	}
	
	public void venta(int venta) {
		this.ventas+=venta;
		this.stock-=venta;
	}
	
	public void informacion() {
		System.out.println("Informacion de " + nombre);
		System.out.println("Proveedores " + proveedores);
		System.out.println("Stock: " + stock);
		System.out.println("Compras: " + compras);
		System.out.println("Ventas: " + ventas);
	}
	
	@Override
	public String toString() {
	    return nombre;
	}	
}