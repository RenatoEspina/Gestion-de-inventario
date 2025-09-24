package gestionInventario.almacen.subProductos;

import gestionInventario.almacen.Producto;
import java.time.LocalDate;

public class ProductoPerecible extends Producto {
    private LocalDate fechaVencimiento;

    public ProductoPerecible(String nombre, String proveedor, int compra, LocalDate fechaVencimiento) {
        super(nombre, proveedor, compra);
        this.fechaVencimiento = fechaVencimiento;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    @Override
    public void venta(int cantidad) {
        if (LocalDate.now().isAfter(fechaVencimiento)) {
            System.out.println("No se puede vender " + getNombre() + " porque está vencido desde " + fechaVencimiento);
        } else {
            super.venta(cantidad);
        }
    }

    @Override
    public void informacion() {
        super.informacion();
        System.out.println("Fecha de vencimiento: " + fechaVencimiento);
    }
}