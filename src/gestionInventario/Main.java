package gestionInventario;

import gestionInventario.almacen.Inventario;
import gestionInventario.almacen.Producto;
import gestionInventario.almacen.Secciones;
import gestionInventario.almacen.subProductos.ProductoPerecible;
import gestionInventario.almacen.subProductos.ProductoPremium;
import gestionInventario.excepciones.ProductoNoEncontradoException;
import gestionInventario.excepciones.SeccionNoEncontradaException;
import gestionInventario.utilidades.*;

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
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.embed.swing.SwingNode;
import javax.swing.SwingUtilities; 
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

/**
 * Clase principal que inicia y controla la aplicación de gestión de inventario con interfaz gráfica JavaFX.
 * Proporciona una interfaz completa para gestionar secciones, productos y generar reportes.
 * 
 * @author Renato Espina
 * @version 2.3 (Lógica de UI Completa)
 */
public class Main extends Application {
	

    /** Constructor por defecto de la aplicación. */
    public Main() {}

	/** La instancia principal del inventario que contiene toda la lógica de negocio y los datos. */
	private Inventario almacen;

	/** El manejador de persistencia para cargar y guardar el estado del inventario. */
	private GestorPersistencia gestor;

	/** La ventana principal (Stage) de la aplicación JavaFX. */
	private Stage primaryStage;

	/** El botón de la interfaz para salir y guardar el inventario. */
	private Button btnSalir;

    /**
     * Método principal que inicia la aplicación JavaFX.
     * 
     * @param args Argumentos de línea de comandos
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Método de inicio de la aplicación JavaFX.
     * Configura la ventana principal, carga el inventario y muestra la interfaz.
     * 
     * @param primaryStage El escenario principal de la aplicación
     */
    @Override
    public void start(Stage primaryStage) {
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

    /**
     * Crea y configura el menú principal de la aplicación.
     * 
     * @return VBox con los elementos del menú principal
     */
    private VBox crearMenuPrincipal() {
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
    
    /**
     * Guarda el inventario actual en los archivos CSV y cierra la aplicación.
     * Muestra una alerta si ocurre un error durante el guardado.
     */
    private void salirYGuardar() {
        try {
            gestor.guardarInventario(almacen);
            mostrarAlerta(Alert.AlertType.INFORMATION, "Guardado", "Inventario guardado con éxito.", "¡Hasta luego!");
        } catch (IOException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error al Guardar", "No se pudo guardar el inventario.", e.getMessage());
        }
        primaryStage.close();
    }
    
    /**
	 * Muestra un diálogo de alerta genérico.
	 *
	 * @param tipo El tipo de alerta (ERROR, INFORMATION, WARNING, etc.)
	 * @param titulo El título de la ventana de alerta
	 * @param encabezado El texto principal de la alerta
	 * @param contenido Un texto descriptivo opcional
	 */
	private void mostrarAlerta(Alert.AlertType tipo, String titulo, String encabezado, String contenido) {
		Alert alerta = new Alert(tipo);
		alerta.setTitle(titulo);
		alerta.setHeaderText(encabezado);
		if (contenido != null) {
			alerta.setContentText(contenido);
		}
		alerta.showAndWait();
	}
    
    /**
     * Abre una nueva ventana modal para gestionar las secciones del inventario.
     * Permite agregar, renombrar y eliminar secciones.
     */
    private void abrirVentanaSecciones() {
        Stage ventana = new Stage();
        ventana.initModality(Modality.APPLICATION_MODAL);
        ventana.setTitle("Gestionar Secciones");
        ventana.setMinWidth(400);

        BorderPane layout = new BorderPane();
        layout.setPadding(new Insets(20));

        Label titulo = new Label("Secciones del Almacén");
        titulo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        layout.setTop(titulo);
        BorderPane.setAlignment(titulo, Pos.CENTER);

        ListView<Secciones> listaSecciones = new ListView<>();
        listaSecciones.setItems(almacen.getSeccionesAsObservableList());
        layout.setCenter(listaSecciones);
        BorderPane.setMargin(listaSecciones, new Insets(15, 0, 15, 0));

        Button btnAgregar = new Button("Agregar Sección");
        Button btnRenombrar = new Button("Renombrar Sección");
        Button btnEliminar = new Button("Eliminar Sección");
        btnAgregar.setMaxWidth(Double.MAX_VALUE);
        btnRenombrar.setMaxWidth(Double.MAX_VALUE);
        btnEliminar.setMaxWidth(Double.MAX_VALUE);
        
        HBox botonesLayout = new HBox(10, btnAgregar, btnRenombrar, btnEliminar);
        botonesLayout.setAlignment(Pos.CENTER);
        layout.setBottom(botonesLayout);

        btnAgregar.setOnAction(e -> {
            TextInputDialog dialogo = new TextInputDialog();
            dialogo.setTitle("Nueva Sección");
            dialogo.setHeaderText("Ingrese el nombre de la nueva sección:");
            dialogo.setContentText("Nombre:");

            Optional<String> resultado = dialogo.showAndWait();
            resultado.ifPresent(nombre -> {
                if (nombre.trim().isEmpty()) {
                    mostrarAlerta(Alert.AlertType.WARNING, "Inválido", "El nombre de la sección no puede estar vacío.", null);
                } else if (almacen.nuevaSeccion(nombre)) {
                    listaSecciones.refresh();
                    mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Sección '" + nombre + "' creada correctamente.", null);
                } else {
                    mostrarAlerta(Alert.AlertType.ERROR, "Error", "La sección '" + nombre + "' ya existe.", null);
                }
            });
        });

        btnRenombrar.setOnAction(e -> {
            Secciones seccionSeleccionada = listaSecciones.getSelectionModel().getSelectedItem();
            if (seccionSeleccionada == null) {
                mostrarAlerta(Alert.AlertType.WARNING, "Acción no válida", "Debe seleccionar una sección de la lista para renombrar.", null);
                return;
            }

            TextInputDialog dialogo = new TextInputDialog(seccionSeleccionada.getNombre());
            dialogo.setTitle("Renombrar Sección");
            dialogo.setHeaderText("Ingrese el nuevo nombre para la sección '" + seccionSeleccionada.getNombre() + "':");
            dialogo.setContentText("Nuevo nombre:");

            Optional<String> resultado = dialogo.showAndWait();
            resultado.ifPresent(nuevoNombre -> {
                if (nuevoNombre.trim().isEmpty()) {
                    mostrarAlerta(Alert.AlertType.WARNING, "Inválido", "El nombre no puede estar vacío.", null);
                    return;
                }
                if (nuevoNombre.equals(seccionSeleccionada.getNombre())) {
                    return;
                }

                if (almacen.renombrarSeccion(seccionSeleccionada.getNombre(), nuevoNombre)) {
                    listaSecciones.refresh();
                    mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Sección renombrada correctamente.", null);
                } else {
                    mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo renombrar la sección. El nuevo nombre '" + nuevoNombre + "' podría ya existir.", null);
                }
            });
        });

        btnEliminar.setOnAction(e -> {
            Secciones seccionSeleccionada = listaSecciones.getSelectionModel().getSelectedItem();
            if (seccionSeleccionada == null) {
                mostrarAlerta(Alert.AlertType.WARNING, "Acción no válida", "Debe seleccionar una sección de la lista.", null);
                return;
            }

            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION, "¿Seguro que desea eliminar la sección '" + seccionSeleccionada.getNombre() + "'? Se eliminarán todos sus productos.", ButtonType.YES, ButtonType.NO);
            confirmacion.setTitle("Confirmar Eliminación");
            confirmacion.setHeaderText(null);

            confirmacion.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    almacen.eliminarSeccion(seccionSeleccionada.getNombre());
                    listaSecciones.refresh();
                    mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Sección eliminada.", null);
                }
            });
        });

        Scene escena = new Scene(layout, 450, 500);
        ventana.setScene(escena);
        ventana.showAndWait();
    }

    /**
     * Abre una nueva ventana modal para gestionar los productos del inventario.
     * Permite agregar, eliminar, comprar y vender productos, así como generar reportes.
     */
    private void abrirVentanaProductos() {
        Stage ventana = new Stage();
        ventana.initModality(Modality.APPLICATION_MODAL);
        ventana.setTitle("Gestionar Productos");

        BorderPane layout = new BorderPane();
        layout.setPadding(new Insets(20));

        HBox topLayout = new HBox(10);
        topLayout.setAlignment(Pos.CENTER_LEFT);
        
        Label lblSeccion = new Label("Filtrar Sección:");

        ObservableList<Secciones> listaDeSecciones = almacen.getSeccionesAsObservableList();
        listaDeSecciones.add(0, null);

        ComboBox<Secciones> comboSecciones = new ComboBox<>(listaDeSecciones);

        comboSecciones.setCellFactory(param -> new ListCell<Secciones>() {
            @Override
            protected void updateItem(Secciones item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("Todas las secciones");
                } else {
                    setText(item.getNombre());
                }
            }
        });

        comboSecciones.setButtonCell(new ListCell<Secciones>() {
            @Override
            protected void updateItem(Secciones item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("Todas las secciones");
                } else {
                    setText(item.getNombre());
                }
            }
        });

        comboSecciones.getSelectionModel().selectFirst();

        TextField buscador = new TextField();
        buscador.setPromptText("Buscar por nombre...");
        HBox.setHgrow(buscador, Priority.ALWAYS);

        topLayout.getChildren().addAll(lblSeccion, comboSecciones, buscador);
        layout.setTop(topLayout);
        BorderPane.setMargin(topLayout, new Insets(0, 0, 15, 0));

        TableView<Producto> tablaProductos = new TableView<>();
        configurarTablaProductos(tablaProductos);
        layout.setCenter(tablaProductos);

        ObservableList<Producto> listaMaestra = FXCollections.observableArrayList();
        FilteredList<Producto> listaFiltrada = new FilteredList<>(listaMaestra, p -> true);
        tablaProductos.setItems(listaFiltrada);

        comboSecciones.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                listaMaestra.setAll(newVal.getProductosAsObservableList());
            } else {
                listaMaestra.setAll(almacen.getAllProductosAsObservableList());
            }
        });
        
        listaMaestra.setAll(almacen.getAllProductosAsObservableList());

        buscador.textProperty().addListener((obs, oldVal, newVal) -> {
            listaFiltrada.setPredicate(producto -> {
                if (newVal == null || newVal.isEmpty()) {
                    return true;
                }
                String filtroEnMinusculas = newVal.toLowerCase();
                return producto.getNombre().toLowerCase().contains(filtroEnMinusculas);
            });
        });

        VBox botonesLayout = new VBox(10);
        botonesLayout.setPadding(new Insets(0, 0, 0, 15));
        Button btnAgregar = new Button("Agregar Producto...");
        Button btnEliminar = new Button("Eliminar Producto");
        Button btnComprar = new Button("Comprar Stock...");
        Button btnVender = new Button("Vender Stock...");
        Button btnReporte = new Button("Opciones de filtro...");
        Button btnReporteTXT = new Button("Generar Reporte TXT");

        btnAgregar.setMaxWidth(Double.MAX_VALUE);
        btnEliminar.setMaxWidth(Double.MAX_VALUE);
        btnComprar.setMaxWidth(Double.MAX_VALUE);
        btnVender.setMaxWidth(Double.MAX_VALUE);
        btnReporte.setMaxWidth(Double.MAX_VALUE);
        btnReporteTXT.setMaxWidth(Double.MAX_VALUE);
        
        botonesLayout.getChildren().addAll(btnAgregar, btnEliminar, new Separator(), btnComprar, btnVender, new Separator(), btnReporte, btnReporteTXT);
        layout.setRight(botonesLayout);
        
        btnAgregar.setOnAction(e -> logicaAgregarProducto(comboSecciones.getValue(), tablaProductos));
        btnEliminar.setOnAction(e -> logicaEliminarProducto(tablaProductos.getSelectionModel().getSelectedItem(), tablaProductos));
        btnComprar.setOnAction(e -> logicaComprarProducto(tablaProductos.getSelectionModel().getSelectedItem(), tablaProductos));
        btnVender.setOnAction(e -> logicaVenderProducto(tablaProductos.getSelectionModel().getSelectedItem(), tablaProductos));
        btnReporte.setOnAction(e -> logicaOpcionesReporte());
        btnReporteTXT.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Guardar Reporte de Inventario Completo");
            fileChooser.setInitialFileName("Reporte_Inventario_" + LocalDate.now() + ".txt");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos de Texto", "*.txt"));

            File archivo = fileChooser.showSaveDialog(primaryStage);
            if (archivo != null) {
                try {
                    ExportadorTXT.generarReporteCompleto(almacen, archivo.getAbsolutePath());
                    mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Reporte TXT generado y guardado en:\n" + archivo.getAbsolutePath(), null);
                } catch (IOException ioe) {
                    mostrarAlerta(Alert.AlertType.ERROR, "Error al Guardar", "No se pudo guardar el archivo de texto.", ioe.getMessage());
                }
            }
        });

        Scene escena = new Scene(layout, 900, 600);
        ventana.setScene(escena);
        ventana.showAndWait();
    }
    
    /**
     * Configura las columnas de la tabla de productos.
     * 
     * @param tabla La tabla de productos a configurar
     */
    @SuppressWarnings("unchecked")
    private void configurarTablaProductos(TableView<Producto> tabla) {
        TableColumn<Producto, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colNombre.setPrefWidth(180);

        TableColumn<Producto, Integer> colStock = new TableColumn<>("Stock");
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        colStock.setPrefWidth(70);

        TableColumn<Producto, Integer> colCompras = new TableColumn<>("Compras Totales");
        colCompras.setCellValueFactory(new PropertyValueFactory<>("comprasTotales"));
        colCompras.setPrefWidth(120);

        TableColumn<Producto, Integer> colVentas = new TableColumn<>("Ventas Totales");
        colVentas.setCellValueFactory(new PropertyValueFactory<>("ventasTotales"));
        colVentas.setPrefWidth(120);

        TableColumn<Producto, String> colProveedores = new TableColumn<>("Proveedores");
        colProveedores.setCellValueFactory(cellData -> {
            String listaProveedores = String.join(", ", cellData.getValue().getProveedores());
            return new javafx.beans.property.SimpleStringProperty(listaProveedores);
        });
        colProveedores.setPrefWidth(200);

        tabla.getColumns().addAll(colNombre, colStock, colCompras, colVentas, colProveedores);
        tabla.setPlaceholder(new Label("No hay productos que mostrar o no se ha seleccionado una sección"));
    }

    /**
     * Maneja la lógica para el botón "Agregar Producto".
     * Muestra un diálogo personalizado para crear un nuevo producto y lo añade a la sección seleccionada.
     * 
     * @param seccionSeleccionada La sección donde se agregará el producto
     * @param tabla La tabla de productos que se actualizará
     */
    private void logicaAgregarProducto(Secciones seccionSeleccionada, TableView<Producto> tabla) {
        if (seccionSeleccionada == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Acción no válida", "Debe seleccionar una sección primero.", null);
            return;
        }

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
     * 
     * @param productoSeleccionado El producto a eliminar
     * @param tabla La tabla de productos que se actualizará
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
                    tabla.getItems().remove(productoSeleccionado);
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
     * 
     * @param producto El producto al que se le comprará stock
     * @param tabla La tabla de productos que se actualizará
     */
    private void logicaComprarProducto(Producto producto, TableView<Producto> tabla) {
        if (producto == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Acción no válida", "Debe seleccionar un producto.", null);
            return;
        }
        
        Secciones seccion = almacen.encontrarSeccionDeProducto(producto.getNombre());
        if (seccion == null) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error Crítico", "No se pudo encontrar la sección para este producto.", null);
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
     * 
     * @param producto El producto al que se le venderá stock
     * @param tabla La tabla de productos que se actualizará
     */
    private void logicaVenderProducto(Producto producto, TableView<Producto> tabla) {
        if (producto == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Acción no válida", "Debe seleccionar un producto.", null);
            return;
        }
        
        Secciones seccion = almacen.encontrarSeccionDeProducto(producto.getNombre());
        if (seccion == null) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error Crítico", "No se pudo encontrar la sección para este producto.", null);
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
            } catch (Exception e) {
                mostrarAlerta(Alert.AlertType.ERROR, "Error en la venta", "No se pudo realizar la venta.", e.getMessage());
            }
        });
    }

    /**
	 * Maneja la lógica para el botón "Opciones de filtro...".
	 * Pide un filtro, y en lugar de generar el reporte directamente,
	 * ahora abre una ventana con un gráfico de JFreeChart.
	 */
	private void logicaOpcionesReporte() {
		Dialog<Object[]> dialog = new Dialog<>();
		dialog.setTitle("Opciones de Reporte");
		dialog.setHeaderText("Seleccione el tipo de filtro para generar el gráfico y reporte");

		ButtonType generarButtonType = new ButtonType("Generar Gráfico", ButtonBar.ButtonData.OK_DONE); // Texto del botón cambiado
		dialog.getDialogPane().getButtonTypes().addAll(generarButtonType, ButtonType.CANCEL);

		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));

		ToggleGroup group = new ToggleGroup();
		RadioButton rbVentas = new RadioButton("Filtrar por ventas mínimas:");
		rbVentas.setToggleGroup(group);
		rbVentas.setSelected(true);

		RadioButton rbProveedor = new RadioButton("Filtrar por proveedor:");
		rbProveedor.setToggleGroup(group);

		TextField txtVentas = new TextField("0");
		TextField txtProveedor = new TextField();
		txtProveedor.setPromptText("Nombre del proveedor");
		txtProveedor.setDisable(true);

		group.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
			txtVentas.setDisable(newVal == rbProveedor);
			txtProveedor.setDisable(newVal == rbVentas);
		});

		grid.add(rbVentas, 0, 0);
		grid.add(txtVentas, 1, 0);
		grid.add(rbProveedor, 0, 1);
		grid.add(txtProveedor, 1, 1);

		dialog.getDialogPane().setContent(grid);

		dialog.setResultConverter(dialogButton -> {
			if (dialogButton == generarButtonType) {
				try {
					if (rbVentas.isSelected()) {
						int ventasMinimas = Integer.parseInt(txtVentas.getText());
						return new Object[]{"VENTAS", ventasMinimas};
					} else {
						String proveedor = txtProveedor.getText();
						if (proveedor.trim().isEmpty()) {
							throw new IllegalArgumentException("El nombre del proveedor no puede estar vacío.");
						}
						return new Object[]{"PROVEEDOR", proveedor};
					}
				} catch (Exception e) {
					mostrarAlerta(Alert.AlertType.ERROR, "Dato Inválido", "Por favor, revise los datos ingresados.", e.getMessage());
					return null;
				}
			}
			return null;
		});

		Optional<Object[]> resultado = dialog.showAndWait();
		resultado.ifPresent(filtro -> {
			String tipo = (String) filtro[0];
			List<Producto> productosFiltrados;

			String tituloGrafico; // NUEVO: para dar un título dinámico al gráfico

			if (tipo.equals("VENTAS")) {
				int valor = (int) filtro[1];
				productosFiltrados = almacen.filtrarProductosPorVentas(valor);
				tituloGrafico = "Productos con un mínimo de " + valor + " ventas";
			} else {
				String valor = (String) filtro[1];
				productosFiltrados = almacen.filtrarProductosPorProveedor(valor);
				tituloGrafico = "Productos del proveedor: " + valor;
			}

			if (productosFiltrados.isEmpty()) {
				mostrarAlerta(Alert.AlertType.INFORMATION, "Sin Resultados", "No se encontraron productos que cumplan con ese criterio.", null);
				return;
			}
			
			// NUEVO: En lugar de generar el Excel, llamamos al nuevo método que muestra el gráfico.
			mostrarVentanaGraficoYReporte(productosFiltrados, tipo, tituloGrafico);
		});
	}

	/**
	 * NUEVO: Este método crea y muestra una nueva ventana con el gráfico de JFreeChart
	 * y ofrece la opción de generar el reporte de Excel.
	 *
	 * @param productosFiltrados La lista de productos a visualizar y reportar.
	 * @param tipo               El tipo de filtro que se aplicó ("VENTAS" o "PROVEEDOR").
	 * @param tituloGrafico      El título para la ventana y el gráfico.
	 */
	private void mostrarVentanaGraficoYReporte(List<Producto> productosFiltrados, String tipo, String tituloGrafico) {
		Stage ventanaGrafico = new Stage();
		ventanaGrafico.initModality(Modality.APPLICATION_MODAL);
		ventanaGrafico.setTitle("Visualización de Reporte: " + tituloGrafico);

		// 1. Crear el gráfico usando nuestra clase de utilidad
		JFreeChart chart = GraficoUtilidades.crearGraficoDeBarrasVentas(productosFiltrados, tituloGrafico);
		ChartPanel chartPanel = new ChartPanel(chart);

		// 2. Integrar el panel de Swing (JFreeChart) en un nodo de JavaFX
		SwingNode swingNode = new SwingNode();
		SwingUtilities.invokeLater(() -> swingNode.setContent(chartPanel));

		// 3. Crear el botón para generar el reporte
		Button btnGenerarExcel = new Button("Generar Reporte Excel");
		btnGenerarExcel.setOnAction(e -> {
			// Esta es la lógica que antes estaba en logicaOpcionesReporte
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Guardar Reporte");
			fileChooser.setInitialFileName("Reporte_" + tipo + "_" + LocalDate.now() + ".xlsx");
			fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos de Excel", "*.xlsx"));

			File archivo = fileChooser.showSaveDialog(primaryStage);
			if (archivo != null) {
				try {
					if (tipo.equals("VENTAS")) {
						ExportadorExcel.generarReporteVentas(almacen, productosFiltrados, archivo.getAbsolutePath());
					} else {
						ExportadorExcel.generarReporteProveedor(productosFiltrados, archivo.getAbsolutePath());
					}
					mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Reporte generado y guardado.", null);
					ventanaGrafico.close(); // Cerramos la ventana del gráfico después de generar el reporte
				} catch (IOException ex) {
					mostrarAlerta(Alert.AlertType.ERROR, "Error al Guardar", "No se pudo guardar el archivo Excel.", ex.getMessage());
				}
			}
		});

		// 4. Montar la escena
		HBox layoutBotones = new HBox(btnGenerarExcel);
		layoutBotones.setAlignment(Pos.CENTER);
		layoutBotones.setPadding(new Insets(10));

		BorderPane layoutPrincipal = new BorderPane();
		layoutPrincipal.setCenter(swingNode); // El gráfico en el centro
		layoutPrincipal.setBottom(layoutBotones); // El botón abajo

		Scene scene = new Scene(layoutPrincipal, 800, 600);
		ventanaGrafico.setScene(scene);
		ventanaGrafico.showAndWait();
	}
}

/**
 * Clase auxiliar para crear un diálogo de formulario personalizado para agregar nuevos productos.
 * Este diálogo se adapta para pedir los datos necesarios según el tipo de producto seleccionado.
 */
class DialogoProducto {

	/** Constructor por defecto para el dialogo de creacion de productos. */
    public DialogoProducto() {}
	
    /**
     * Muestra el diálogo personalizado para agregar un nuevo producto.
     * 
     * @return Optional con el producto creado si se confirma, o empty si se cancela
     */
    public Optional<Producto> mostrarDialogo() {
        Dialog<Producto> dialog = new Dialog<>();
        dialog.setTitle("Agregar Nuevo Producto");
        dialog.setHeaderText("Complete los datos del nuevo producto");

        ButtonType crearButtonType = new ButtonType("Crear", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(crearButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nombreField = new TextField();
        nombreField.setPromptText("Nombre del producto");
        TextField proveedorField = new TextField();
        proveedorField.setPromptText("Proveedor inicial");
        TextField compraField = new TextField();
        compraField.setPromptText("Cantidad inicial");
        
        ComboBox<String> tipoCombo = new ComboBox<>(FXCollections.observableArrayList("Normal", "Perecible", "Premium"));
        tipoCombo.setValue("Normal");

        DatePicker fechaVencimientoPicker = new DatePicker();
        TextField stockMaxField = new TextField();
        
        Label fechaLabel = new Label("Fecha Vencimiento:");
        Label stockMaxLabel = new Label("Stock Máximo:");
        
        grid.add(new Label("Nombre:"), 0, 0); grid.add(nombreField, 1, 0);
        grid.add(new Label("Proveedor:"), 0, 1); grid.add(proveedorField, 1, 1);
        grid.add(new Label("Compra Inicial:"), 0, 2); grid.add(compraField, 1, 2);
        grid.add(new Label("Tipo:"), 0, 3); grid.add(tipoCombo, 1, 3);
        
        tipoCombo.valueProperty().addListener((obs, oldVal, newVal) -> {
            grid.getChildren().removeAll(fechaLabel, fechaVencimientoPicker, stockMaxLabel, stockMaxField);
            
            if (newVal.equals("Perecible")) {
                grid.add(fechaLabel, 0, 4);
                grid.add(fechaVencimientoPicker, 1, 4);
            } else if (newVal.equals("Premium")) {
                grid.add(stockMaxLabel, 0, 4);
                grid.add(stockMaxField, 1, 4);
            }
        });

        Node crearButton = dialog.getDialogPane().lookupButton(crearButtonType);
        crearButton.setDisable(true);
        
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
                        default:
                            return new Producto(nombre, proveedor, compra);
                    }
                } catch (Exception e) {
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