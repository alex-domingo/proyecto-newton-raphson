package com.matapli3.proyectonewtonraphson;

import com.matapli3.proyectonewtonraphson.Logica.EstadoConvergencia;
import com.matapli3.proyectonewtonraphson.Logica.Iteracion;
import com.matapli3.proyectonewtonraphson.Logica.NewtonRaphson;
import com.matapli3.proyectonewtonraphson.Logica.Resultado;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import com.matapli3.proyectonewtonraphson.Graficos.Graficador;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.fx.ChartViewer;

public class PrimaryController {

    // --- Inputs ---
    @FXML
    private TextField txtFx;
    @FXML
    private TextField txtX0;
    @FXML
    private TextField txtIteraciones;
    @FXML
    private TextField txtTolerancia;

    // --- Resultados ---
    @FXML
    private TextField txtRaizAprox;
    @FXML
    private TextField txtConverge;

    // --- Panel gráfica ---
    @FXML
    private StackPane panelGrafica;

    // --- Tabla ---
    @FXML
    private TableView<Iteracion> tablaIteraciones;
    @FXML
    private TableColumn<Iteracion, Integer> colN;
    @FXML
    private TableColumn<Iteracion, Double> colXi;
    @FXML
    private TableColumn<Iteracion, Double> colFx;
    @FXML
    private TableColumn<Iteracion, Double> colFpx;
    @FXML
    private TableColumn<Iteracion, Double> colXi1;
    @FXML
    private TableColumn<Iteracion, Double> colDelta;

    @FXML
    public void initialize() {
        colN.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().numero).asObject());
        colXi.setCellValueFactory(d -> new SimpleDoubleProperty(d.getValue().xi).asObject());
        colFx.setCellValueFactory(d -> new SimpleDoubleProperty(d.getValue().fx).asObject());
        colFpx.setCellValueFactory(d -> new SimpleDoubleProperty(d.getValue().fpx).asObject());
        colXi1.setCellValueFactory(d -> new SimpleDoubleProperty(d.getValue().xiNext).asObject());
        colDelta.setCellValueFactory(d -> new SimpleDoubleProperty(d.getValue().delta).asObject());

        // #13: formato numérico de las columnas decimales
        configurarFormato(colXi);
        configurarFormato(colFx);
        configurarFormato(colFpx);
        configurarFormato(colXi1);
        configurarFormato(colDelta);

        // #14: resaltar la última fila (mejor aproximación / raíz)
        tablaIteraciones.setRowFactory(tv -> new TableRow<Iteracion>() {
            @Override
            protected void updateItem(Iteracion item, boolean empty) {
                super.updateItem(item, empty);
                getStyleClass().remove("fila-raiz");
                boolean esUltima = !empty && item != null
                        && getIndex() == tablaIteraciones.getItems().size() - 1;
                if (esUltima) {
                    getStyleClass().add("fila-raiz");
                }
            }
        });

        // #15: foco inicial en f(x)
        Platform.runLater(() -> txtFx.requestFocus());
    }

    @FXML
    private void calcular() {
        try {
            String funcion = txtFx.getText().trim();
            double x0 = Double.parseDouble(txtX0.getText().trim());
            int iteraciones = Integer.parseInt(txtIteraciones.getText().trim());
            double tolerancia = Double.parseDouble(txtTolerancia.getText().trim());

            if (funcion.isEmpty()) {
                mostrarError("Por favor ingresa la función f(x).");
                return;
            }
            if (iteraciones <= 0) {
                mostrarError("El número de iteraciones debe ser mayor que 0.");
                return;
            }
            if (tolerancia <= 0) {
                mostrarError("La tolerancia debe ser un número positivo.");
                return;
            }

            Resultado resultado = NewtonRaphson.solve(funcion, x0, tolerancia, iteraciones);

            if (resultado.getEstado() == EstadoConvergencia.FUNCION_INVALIDA) {
                mostrarError("No se pudo interpretar f(x). Revisa la sintaxis (usa 'x' como variable).");
                return;
            }
            if (!resultado.hayPasos()) {
                mostrarError("No se generaron iteraciones. Revisa los valores ingresados.");
                return;
            }

            // #14: mostrar la derivada calculada automáticamente
            ObservableList<Iteracion> datos = FXCollections.observableArrayList(resultado.getPasos());
            tablaIteraciones.setItems(datos);

            txtRaizAprox.setText(String.format("%.8f", resultado.getRaiz()));
            txtConverge.setText(resultado.convergio() ? "Sí ✓" : "No convergió");
            txtConverge.setTooltip(new Tooltip(resultado.getEstado().getMensaje()));

            // ================================================
            // ZONA PARA LA GRÁFICA
            //   funcion              → String con f(x)
            //   resultado.getPasos() → List<Iteracion>
            //   panelGrafica         → StackPane destino
            // ================================================
            // Gráfica
            JFreeChart chart = Graficador.crearGrafica(funcion, resultado.getPasos());
            ChartViewer viewer = new ChartViewer(chart);
            panelGrafica.getChildren().setAll(viewer);

        } catch (NumberFormatException e) {
            mostrarError("Ingresa valores numéricos válidos en x₀, iteraciones y tolerancia.");
        } catch (Exception e) {
            mostrarError("Error: " + e.getMessage());
        }
    }

    @FXML
    private void limpiar() {
        txtFx.clear();
        txtX0.clear();
        txtIteraciones.clear();
        txtTolerancia.clear();
        txtRaizAprox.clear();
        txtConverge.clear();
        txtConverge.setTooltip(null);
        tablaIteraciones.getItems().clear();
        txtFx.requestFocus();
        panelGrafica.getChildren().clear();
    }

    // #13: formatea cada celda decimal; usa notación científica para valores muy chicos/grandes
    private void configurarFormato(TableColumn<Iteracion, Double> columna) {
        columna.setCellFactory(c -> new TableCell<Iteracion, Double>() {
            @Override
            protected void updateItem(Double valor, boolean empty) {
                super.updateItem(valor, empty);
                setText((empty || valor == null) ? null : formatear(valor));
            }
        });
    }

    private static String formatear(double v) {
        if (Double.isNaN(v)) {
            return "NaN";
        }
        if (Double.isInfinite(v)) {
            return v > 0 ? "∞" : "-∞";
        }
        if (v == 0.0) {
            return "0.000000";
        }
        double abs = Math.abs(v);
        if (abs < 1e-4 || abs >= 1e7) {
            return String.format("%.4e", v);   // científica para magnitudes extremas
        }
        return String.format("%.6f", v);
    }

    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
