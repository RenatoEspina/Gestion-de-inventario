package gestionInventario.almacen.subProductos;

import gestionInventario.almacen.Producto;

public class ProductoPremium extends Producto {
    private int stockMaximo;

    public ProductoPremium(String nombre, String proveedor, int compra, int stockMaximo) {
        super(nombre, proveedor, compra);
        this.stockMaximo = stockMaximo;
    }

    @Override
    public void compra(String proveedor, int cantidad) {
        if(getStock() + cantidad > stockMaximo) {
            System.out.println("No se puede comprar " + cantidad + " unidades. Límite máximo de stock: " + stockMaximo);
        } else {
            super.compra(proveedor, cantidad);
        }
    }

    public int getStockMaximo() {
        return stockMaximo;
    }

    public void setStockMaximo(int stockMaximo) {
        this.stockMaximo = stockMaximo;
    }

    @Override
    public void informacion() {
        super.informacion();
        System.out.println("Stock máximo permitido: " + stockMaximo);
    }
}