package gestionInventario;

import gestionInventario.almacen.Inventario;
import gestionInventario.almacen.Producto;
import gestionInventario.almacen.Secciones;
import gestionInventario.almacen.subProductos.ProductoPerecible;
import gestionInventario.almacen.subProductos.ProductoPremium;
import gestionInventario.excepciones.ProductoNoEncontradoException;
import gestionInventario.excepciones.SeccionNoEncontradaException;
import gestionInventario.utilidades.ExportadorExcel;
import gestionInventario.utilidades.GestorPersistencia;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Clase principal que inicia y controla la aplicación de gestión de inventario con interfaz gráfica JavaFX.
 *
 * @author Renato Espina
 * @version 2.3 (Lógica de UI Completa)
 */
public class Main extends Application {

    private Inventario almacen;
    private GestorPersistencia gestor;
    private Stage primaryStage;
    private Button btnSalir;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // ... El método start() y el menú principal no cambian ...
        this.primaryStage = primaryStage;

        try {
            gestor = new GestorPersistencia("data/");
            almacen = gestor.cargarInventario();
        } catch (IOException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de Carga", "No se pudo cargar el inventario.", e.getMessage());
            almacen = new Inventario();
        }

        primaryStage.setTitle("Sistema de Gestión de Inventario");
        
        VBox root = crearMenuPrincipal();

        btnSalir.setOnAction(e -> salirYGuardar());
        
        primaryStage.setOnCloseRequest(event -> {
            event.consume(); 
            salirYGuardar();
        });

        Scene scene = new Scene(root, 450, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox crearMenuPrincipal() {
        // ... Sin cambios ...
        Label titleLabel = new Label("Sistema de Inventario");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #333;");

        Button btnSecciones = new Button("Gestionar Secciones");
        Button btnProductos = new Button("Gestionar Productos");
        btnSalir = new Button("Salir y Guardar");
        
        String buttonStyle = "-fx-font-size: 16px; -fx-padding: 12px 25px; -fx-background-color: #007bff; -fx-text-fill: white; -fx-background-radius: 5;";
        btnSecciones.setStyle(buttonStyle);
        btnProductos.setStyle(buttonStyle);
        btnSalir.setStyle("-fx-font-size: 16px; -fx-padding: 12px 25px; -fx-background-color: #dc3545; -fx-text-fill: white; -fx-background-radius: 5;");
        
        btnSecciones.setOnAction(e -> abrirVentanaSecciones());
        btnProductos.setOnAction(e -> abrirVentanaProductos());
        
        VBox root = new VBox(25);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #f4f4f9;");
        root.getChildren().addAll(titleLabel, btnSecciones, btnProductos, btnSalir);
        
        return root;
    }
    
    private void salirYGuardar() { /* ... Sin cambios ... */ }
    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String encabezado, String contenido) { /* ... Sin cambios ... */ }
    private void abrirVentanaSecciones() { /* ... Sin cambios ... */ }


    // --- GESTIÓN DE PRODUCTOS (MÉTODO PRINCIPAL) ---

    private void abrirVentanaProductos() {
        Stage ventana = new Stage();
        ventana.initModality(Modality.APPLICATION_MODAL);
        ventana.setTitle("Gestionar Productos");

        BorderPane layout = new BorderPane();
        layout.setPadding(new Insets(20));

        HBox topLayout = new HBox(10);
        topLayout.setAlignment(Pos.CENTER_LEFT);
        Label lblSeccion = new Label("Seleccionar Sección:");
        ComboBox<Secciones> comboSecciones = new ComboBox<>(almacen.getSeccionesAsObservableList());
        topLayout.getChildren().addAll(lblSeccion, comboSecciones);
        layout.setTop(topLayout);
        BorderPane.setMargin(topLayout, new Insets(0, 0, 15, 0));

        TableView<Producto> tablaProductos = new TableView<>();
        configurarTablaProductos(tablaProductos);
        layout.setCenter(tablaProductos);
        
        comboSecciones.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                tablaProductos.setItems(newVal.getProductosAsObservableList());
            } else {
                tablaProductos.setItems(FXCollections.observableArrayList());
            }
        });

        VBox botonesLayout = new VBox(10);
        botonesLayout.setPadding(new Insets(0, 0, 0, 15));
        Button btnAgregar = new Button("Agregar Producto...");
        Button btnEliminar = new Button("Eliminar Producto");
        Button btnComprar = new Button("Comprar Stock...");
        Button btnVender = new Button("Vender Stock...");
        Button btnReporte = new Button("Generar Reporte...");
        
        // ... (configuración de tamaño de botones)
        btnAgregar.setMaxWidth(Double.MAX_VALUE);
        btnEliminar.setMaxWidth(Double.MAX_VALUE);
        btnComprar.setMaxWidth(Double.MAX_VALUE);
        btnVender.setMaxWidth(Double.MAX_VALUE);
        btnReporte.setMaxWidth(Double.MAX_VALUE);
        
        botonesLayout.getChildren().addAll(btnAgregar, btnEliminar, new Separator(), btnComprar, btnVender, new Separator(), btnReporte);
        layout.setRight(botonesLayout);
        
        // --- LÓGICA DE EVENTOS PARA BOTONES ---
        btnAgregar.setOnAction(e -> logicaAgregarProducto(comboSecciones.getValue(), tablaProductos));
        btnEliminar.setOnAction(e -> logicaEliminarProducto(tablaProductos.getSelectionModel().getSelectedItem(), tablaProductos));
        btnComprar.setOnAction(e -> logicaComprarProducto(comboSecciones.getValue(), tablaProductos.getSelectionModel().getSelectedItem(), tablaProductos));
        btnVender.setOnAction(e -> logicaVenderProducto(comboSecciones.getValue(), tablaProductos.getSelectionModel().getSelectedItem(), tablaProductos));
        btnReporte.setOnAction(e -> logicaGenerarReporte());

        Scene escena = new Scene(layout, 900, 600);
        ventana.setScene(escena);
        ventana.showAndWait();
    }
    
    @SuppressWarnings("unchecked")
    private void configurarTablaProductos(TableView<Producto> tabla){
        // ... Sin cambios ...
        TableColumn<Producto, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colNombre.setPrefWidth(200);

        TableColumn<Producto, Integer> colStock = new TableColumn<>("Stock");
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));

        TableColumn<Producto, Integer> colCompras = new TableColumn<>("Compras Totales");
        colCompras.setCellValueFactory(new PropertyValueFactory<>("comprasTotales"));

        TableColumn<Producto, Integer> colVentas = new TableColumn<>("Ventas Totales");
        colVentas.setCellValueFactory(new PropertyValueFactory<>("ventasTotales"));

        tabla.getColumns().addAll(colNombre, colStock, colCompras, colVentas);
        tabla.setPlaceholder(new Label("Seleccione una sección para ver sus productos"));
    }

    // --- MÉTODOS AUXILIARES PARA LÓGICA DE BOTONES DE PRODUCTOS ---

    /**
     * Maneja la lógica para el botón "Agregar Producto".
     * Muestra un diálogo personalizado para crear un nuevo producto y lo añade a la sección seleccionada.
     */
    private void logicaAgregarProducto(Secciones seccionSeleccionada, TableView<Producto> tabla) {
        if (seccionSeleccionada == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Acción no válida", "Debe seleccionar una sección primero.", null);
            return;
        }

        // Crear y mostrar el diálogo personalizado
        DialogoProducto dialogo = new DialogoProducto();
        Optional<Producto> resultado = dialogo.mostrarDialogo();

        resultado.ifPresent(nuevoProducto -> {
            try {
                if (almacen.agregarProducto(seccionSeleccionada.getNombre(), nuevoProducto)) {
                    tabla.setItems(seccionSeleccionada.getProductosAsObservableList());
                    tabla.refresh();
                    mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Producto agregado correctamente.", null);
                } else {
                    mostrarAlerta(Alert.AlertType.ERROR, "Error", "El producto '" + nuevoProducto.getNombre() + "' ya existe en esta sección.", null);
                }
            } catch (SeccionNoEncontradaException e) {
                mostrarAlerta(Alert.AlertType.ERROR, "Error Crítico", "La sección seleccionada ya no existe.", e.getMessage());
            }
        });
    }

    /**
     * Maneja la lógica para el botón "Eliminar Producto".
     * Pide confirmación y elimina el producto seleccionado.
     */
    private void logicaEliminarProducto(Producto productoSeleccionado, TableView<Producto> tabla) {
        if (productoSeleccionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Acción no válida", "Debe seleccionar un producto de la tabla.", null);
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION, "¿Está seguro de que desea eliminar el producto '" + productoSeleccionado.getNombre() + "'?", ButtonType.YES, ButtonType.NO);
        confirmacion.setTitle("Confirmar Eliminación");
        confirmacion.setHeaderText(null);

        confirmacion.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                try {
                    almacen.eliminarProducto(productoSeleccionado.getNombre());
                    tabla.getItems().remove(productoSeleccionado); // Más eficiente que recargar todo
                    tabla.refresh();
                    mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Producto eliminado.", null);
                } catch (ProductoNoEncontradoException e) {
                    mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo eliminar el producto.", e.getMessage());
                }
            }
        });
    }

    /**
     * Maneja la lógica para el botón "Comprar Stock".
     * Pide cantidad y proveedor, y actualiza el stock del producto.
     */
    private void logicaComprarProducto(Secciones seccion, Producto producto, TableView<Producto> tabla) {
        if (producto == null || seccion == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Acción no válida", "Debe seleccionar una sección y un producto.", null);
            return;
        }

        TextInputDialog dialogo = new TextInputDialog("10");
        dialogo.setTitle("Comprar Stock");
        dialogo.setHeaderText("Comprar: " + producto.getNombre());
        dialogo.setContentText("Cantidad a comprar:");

        Optional<String> resultado = dialogo.showAndWait();
        resultado.ifPresent(cantidadStr -> {
            try {
                int cantidad = Integer.parseInt(cantidadStr);
                if (cantidad <= 0) {
                    mostrarAlerta(Alert.AlertType.WARNING, "Dato inválido", "La cantidad debe ser un número positivo.", null);
                    return;
                }
                
                // Pedir proveedor
                TextInputDialog provDialogo = new TextInputDialog();
                provDialogo.setTitle("Proveedor");
                provDialogo.setHeaderText("Ingrese el proveedor para esta compra:");
                provDialogo.setContentText("Proveedor:");
                String proveedor = provDialogo.showAndWait().orElse("No especificado");

                seccion.comprarProducto(producto.getNombre(), cantidad, proveedor);
                tabla.refresh();
                mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Compra registrada correctamente.", null);

            } catch (NumberFormatException nfe) {
                mostrarAlerta(Alert.AlertType.ERROR, "Error de formato", "Debe ingresar un número entero válido.", null);
            } catch (Exception e) {
                mostrarAlerta(Alert.AlertType.ERROR, "Error en la compra", "No se pudo realizar la compra.", e.getMessage());
            }
        });
    }

    /**
     * Maneja la lógica para el botón "Vender Stock".
     * Pide una cantidad y actualiza el stock del producto.
     */
    private void logicaVenderProducto(Secciones seccion, Producto producto, TableView<Producto> tabla) {
        if (producto == null || seccion == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Acción no válida", "Debe seleccionar una sección y un producto.", null);
            return;
        }

        TextInputDialog dialogo = new TextInputDialog("1");
        dialogo.setTitle("Vender Stock");
        dialogo.setHeaderText("Vender: " + producto.getNombre() + " (Stock actual: " + producto.getStock() + ")");
        dialogo.setContentText("Cantidad a vender:");

        Optional<String> resultado = dialogo.showAndWait();
        resultado.ifPresent(cantidadStr -> {
            try {
                int cantidad = Integer.parseInt(cantidadStr);
                if (cantidad <= 0) {
                    mostrarAlerta(Alert.AlertType.WARNING, "Dato inválido", "La cantidad debe ser un número positivo.", null);
                    return;
                }
                seccion.venderProducto(producto.getNombre(), cantidad);
                tabla.refresh();
                mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Venta registrada correctamente.", null);

            } catch (NumberFormatException nfe) {
                mostrarAlerta(Alert.AlertType.ERROR, "Error de formato", "Debe ingresar un número entero válido.", null);
            } catch (Exception e) { // Captura StockInsuficiente, ProductoVencido, etc.
                mostrarAlerta(Alert.AlertType.ERROR, "Error en la venta", "No se pudo realizar la venta.", e.getMessage());
            }
        });
    }

    /**
     * Maneja la lógica para el botón "Generar Reporte".
     * Pide un filtro, genera la lista y pide al usuario dónde guardar el archivo Excel.
     */
    private void logicaGenerarReporte() {
        TextInputDialog dialogo = new TextInputDialog("0");
        dialogo.setTitle("Generar Reporte de Ventas");
        dialogo.setHeaderText("Filtrar productos por ventas");
        dialogo.setContentText("Mostrar productos con ventas mayores o iguales a:");

        dialogo.showAndWait().ifPresent(ventasMinimasStr -> {
            try {
                int ventasMinimas = Integer.parseInt(ventasMinimasStr);
                List<Producto> productosFiltrados = almacen.filtrarProductosPorVentas(ventasMinimas);

                if (productosFiltrados.isEmpty()) {
                    mostrarAlerta(Alert.AlertType.INFORMATION, "Sin resultados", "No se encontraron productos que cumplan con ese criterio.", null);
                    return;
                }

                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Guardar Reporte");
                fileChooser.setInitialFileName("Reporte_Ventas_" + LocalDate.now() + ".xlsx");
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos de Excel", "*.xlsx"));
                
                File archivo = fileChooser.showSaveDialog(primaryStage);
                if (archivo != null) {
                    ExportadorExcel.generarReporte(almacen, productosFiltrados, archivo.getAbsolutePath());
                    mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Reporte generado y guardado en:\n" + archivo.getAbsolutePath(), null);
                }

            } catch (NumberFormatException nfe) {
                mostrarAlerta(Alert.AlertType.ERROR, "Error de formato", "Debe ingresar un número entero válido.", null);
            } catch (IOException ioe) {
                mostrarAlerta(Alert.AlertType.ERROR, "Error al guardar", "No se pudo guardar el archivo de Excel.", ioe.getMessage());
            }
        });
    }
}


/**
 * Clase auxiliar para crear un diálogo de formulario personalizado para agregar nuevos productos.
 * Este diálogo se adapta para pedir los datos necesarios según el tipo de producto seleccionado.
 */
class DialogoProducto {

    public Optional<Producto> mostrarDialogo() {
        Dialog<Producto> dialog = new Dialog<>();
        dialog.setTitle("Agregar Nuevo Producto");
        dialog.setHeaderText("Complete los datos del nuevo producto");

        // --- Configuración de Botones ---
        ButtonType crearButtonType = new ButtonType("Crear", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(crearButtonType, ButtonType.CANCEL);

        // --- Creación del Layout (GridPane) ---
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        // --- Creación de Controles del Formulario ---
        TextField nombreField = new TextField();
        nombreField.setPromptText("Nombre del producto");
        TextField proveedorField = new TextField();
        proveedorField.setPromptText("Proveedor inicial");
        TextField compraField = new TextField();
        compraField.setPromptText("Cantidad inicial");
        
        ComboBox<String> tipoCombo = new ComboBox<>(FXCollections.observableArrayList("Normal", "Perecible", "Premium"));
        tipoCombo.setValue("Normal");

        // Campos dinámicos
        DatePicker fechaVencimientoPicker = new DatePicker();
        TextField stockMaxField = new TextField();
        
        Label fechaLabel = new Label("Fecha Vencimiento:");
        Label stockMaxLabel = new Label("Stock Máximo:");
        
        // --- Añadir controles al GridPane ---
        grid.add(new Label("Nombre:"), 0, 0); grid.add(nombreField, 1, 0);
        grid.add(new Label("Proveedor:"), 0, 1); grid.add(proveedorField, 1, 1);
        grid.add(new Label("Compra Inicial:"), 0, 2); grid.add(compraField, 1, 2);
        grid.add(new Label("Tipo:"), 0, 3); grid.add(tipoCombo, 1, 3);
        
        // --- Lógica para mostrar/ocultar campos dinámicos ---
        tipoCombo.valueProperty().addListener((obs, oldVal, newVal) -> {
            // Limpiar campos dinámicos del grid
            grid.getChildren().removeAll(fechaLabel, fechaVencimientoPicker, stockMaxLabel, stockMaxField);
            
            if (newVal.equals("Perecible")) {
                grid.add(fechaLabel, 0, 4);
                grid.add(fechaVencimientoPicker, 1, 4);
            } else if (newVal.equals("Premium")) {
                grid.add(stockMaxLabel, 0, 4);
                grid.add(stockMaxField, 1, 4);
            }
        });

        // --- Habilitar/Deshabilitar botón "Crear" basado en la validez de los datos ---
        Node crearButton = dialog.getDialogPane().lookupButton(crearButtonType);
        crearButton.setDisable(true);
        
        // Listener para validar campos comunes
        Runnable validador = () -> {
            boolean invalido = nombreField.getText().trim().isEmpty() || 
                               proveedorField.getText().trim().isEmpty() ||
                               compraField.getText().trim().isEmpty();
            crearButton.setDisable(invalido);
        };
        nombreField.textProperty().addListener((obs, ov, nv) -> validador.run());
        proveedorField.textProperty().addListener((obs, ov, nv) -> validador.run());
        compraField.textProperty().addListener((obs, ov, nv) -> validador.run());


        dialog.getDialogPane().setContent(grid);

        // --- Convertir el resultado del diálogo en un objeto Producto ---
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == crearButtonType) {
                try {
                    String nombre = nombreField.getText();
                    String proveedor = proveedorField.getText();
                    int compra = Integer.parseInt(compraField.getText());

                    switch (tipoCombo.getValue()) {
                        case "Perecible":
                            LocalDate fecha = fechaVencimientoPicker.getValue();
                            if(fecha == null) throw new NullPointerException("La fecha de vencimiento es obligatoria.");
                            return new ProductoPerecible(nombre, proveedor, compra, fecha);
                        case "Premium":
                            int stockMax = Integer.parseInt(stockMaxField.getText());
                            return new ProductoPremium(nombre, proveedor, compra, stockMax);
                        default: // Normal
                            return new Producto(nombre, proveedor, compra);
                    }
                } catch (Exception e) {
                    // Si hay un error de formato o validación, se evita que el diálogo se cierre
                    // mostrando una alerta y devolviendo null.
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Error de Validación");
                    errorAlert.setHeaderText("Datos inválidos.");
                    errorAlert.setContentText("Por favor, revise los datos ingresados. Las cantidades deben ser números y todos los campos son obligatorios.\nError: " + e.getMessage());
                    errorAlert.showAndWait();
                    return null;
                }
            }
            return null;
        });

        return dialog.showAndWait();
    }
}