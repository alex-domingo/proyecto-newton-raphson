package com.matapli3.proyectonewtonraphson;

import com.matapli3.proyectonewtonraphson.Logica.Iteracion;
import com.matapli3.proyectonewtonraphson.Logica.NewtonRaphson;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import java.util.List;

public class PrimaryController {

    // --- Inputs ---
    @FXML private TextField txtFx;
    @FXML private TextField txtX0;
    @FXML private TextField txtIteraciones;
    @FXML private TextField txtTolerancia;

    // --- Resultados ---
    @FXML private TextField txtRaizAprox;
    @FXML private TextField txtConverge;

    // --- Panel gráfica () ---
    @FXML private StackPane panelGrafica;

    // --- Tabla ---
    @FXML private TableView<Iteracion> tablaIteraciones;
    @FXML private TableColumn<Iteracion, Integer> colN;
    @FXML private TableColumn<Iteracion, Double>  colXi;
    @FXML private TableColumn<Iteracion, Double>  colFx;
    @FXML private TableColumn<Iteracion, Double>  colFpx;
    @FXML private TableColumn<Iteracion, Double>  colXi1;
    @FXML private TableColumn<Iteracion, Double>  colDelta;

    @FXML
    public void initialize() {
        colN.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().numero).asObject());
        colXi.setCellValueFactory(d -> new SimpleDoubleProperty(d.getValue().xi).asObject());
        colFx.setCellValueFactory(d -> new SimpleDoubleProperty(d.getValue().fx).asObject());
        colFpx.setCellValueFactory(d -> new SimpleDoubleProperty(d.getValue().fpx).asObject());
        colXi1.setCellValueFactory(d -> new SimpleDoubleProperty(d.getValue().xiNext).asObject());
        colDelta.setCellValueFactory(d -> new SimpleDoubleProperty(d.getValue().delta).asObject());
    }

    @FXML
    private void calcular() {
        try {
            String funcion     = txtFx.getText().trim();
            double x0          = Double.parseDouble(txtX0.getText().trim());
            int    iteraciones = Integer.parseInt(txtIteraciones.getText().trim());
            double tolerancia  = Double.parseDouble(txtTolerancia.getText().trim());

            if (funcion.isEmpty()) {
                mostrarError("Por favor ingresa la función f(x).");
                return;
            }

            // Llamada directa a la lógica matematica 
            List<Iteracion> pasos = NewtonRaphson.solve(funcion, x0, tolerancia, iteraciones);

            if (pasos.isEmpty()) {
                mostrarError("No se generaron iteraciones. Revisa los valores ingresados.");
                return;
            }

            // Llenar tabla 
            ObservableList<Iteracion> datos = FXCollections.observableArrayList(pasos);
            tablaIteraciones.setItems(datos);

            // Mostrar resultado
            Iteracion ultima = pasos.get(pasos.size() - 1);
            txtRaizAprox.setText(String.format("%.8f", ultima.xiNext));
            txtConverge.setText(NewtonRaphson.converge() ? "Sí ✓" : "No convergió");

            // ================================================
            // ZONA PARA LA GRÁFICA - Aquí puedes conectaer la logica de la grafica
            // ================================================
            // Tienes disponibles:
            //   funcion  → String con la función que ingresó el usuario
            //   pasos    → List<Iteracion> con todos los datos de la tabla
            //   panelGrafica → StackPane donde insertas tu gráfica
            //

        } catch (NumberFormatException e) {
            mostrarError("Por favor ingresa valores numéricos válidos en x₀, iteraciones y tolerancia.");
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
        tablaIteraciones.getItems().clear();
    }

    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
