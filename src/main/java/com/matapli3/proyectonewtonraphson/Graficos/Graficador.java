package com.matapli3.proyectonewtonraphson.Graficos;

import com.matapli3.proyectonewtonraphson.Logica.Iteracion;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Expression;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.geom.Ellipse2D;
import java.util.List;

public class Graficador {

    // Paleta acorde a la UI (Catppuccin Mocha)
    private static final Color FONDO = Color.decode("#1e1e2e");
    private static final Color FONDO_PANEL = Color.decode("#181825");
    private static final Color TEXTO = Color.decode("#cdd6f4");
    private static final Color REJILLA = Color.decode("#45475a");
    private static final Color CURVA = Color.decode("#89dceb");
    private static final Color PUNTOS = Color.decode("#f38ba8");
    private static final Color RAIZ = Color.decode("#a6e3a1");

    private Graficador() {
    } // clase de utilidad

    public static JFreeChart crearGrafica(String funcionTexto, List<Iteracion> iteraciones) {
        XYSeries serieFuncion = new XYSeries("f(x)");
        XYSeries serieIteraciones = new XYSeries("Iteraciones");

        // Rango en X a partir de las iteraciones (incluye xi y xiNext)
        double minX = iteraciones.get(0).xi;
        double maxX = iteraciones.get(0).xi;
        for (Iteracion it : iteraciones) {
            minX = Math.min(minX, Math.min(it.xi, it.xiNext));
            maxX = Math.max(maxX, Math.max(it.xi, it.xiNext));
        }
        // Margen adaptativo (en vez de un ±5 fijo)
        double span = maxX - minX;
        double margen = Math.max(span * 0.5, 1.0);
        minX -= margen;
        maxX += margen;

        // Curva de f(x): 400 muestras para que salga fluida
        Argument xArg = new Argument("x");
        Expression f = new Expression(funcionTexto, xArg);
        int muestras = 400;
        double paso = (maxX - minX) / muestras;
        for (int k = 0; k <= muestras; k++) {
            double x = minX + k * paso;
            xArg.setArgumentValue(x);
            double y = f.calculate();
            if (!Double.isNaN(y) && !Double.isInfinite(y)) {
                serieFuncion.add(x, y);
            }
        }

        // Punto de cada iteración sobre la curva: (xi, f(xi))
        for (Iteracion it : iteraciones) {
            xArg.setArgumentValue(it.xi);
            double y = f.calculate();
            if (!Double.isNaN(y) && !Double.isInfinite(y)) {
                serieIteraciones.add(it.xi, y);
            }
        }

        XYSeriesCollection datasetCurva = new XYSeriesCollection(serieFuncion);
        XYSeriesCollection datasetPuntos = new XYSeriesCollection(serieIteraciones);

        JFreeChart chart = ChartFactory.createXYLineChart(
                "Método de Newton-Raphson",
                "x", "f(x)",
                datasetCurva,
                PlotOrientation.VERTICAL,
                true, true, false);

        aplicarEstilo(chart, datasetPuntos, iteraciones);
        return chart;
    }

    private static void aplicarEstilo(JFreeChart chart, XYSeriesCollection datasetPuntos,
            List<Iteracion> iteraciones) {
        chart.setBackgroundPaint(FONDO);
        chart.getTitle().setPaint(TEXTO);
        chart.getTitle().setFont(new Font("SansSerif", Font.BOLD, 16));
        if (chart.getLegend() != null) {
            chart.getLegend().setBackgroundPaint(FONDO);
            chart.getLegend().setItemPaint(TEXTO);
        }

        XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(FONDO_PANEL);
        plot.setDomainGridlinePaint(REJILLA);
        plot.setRangeGridlinePaint(REJILLA);
        plot.setOutlinePaint(REJILLA);

        NumberAxis ejeX = (NumberAxis) plot.getDomainAxis();
        NumberAxis ejeY = (NumberAxis) plot.getRangeAxis();
        for (NumberAxis eje : new NumberAxis[]{ejeX, ejeY}) {
            eje.setLabelPaint(TEXTO);
            eje.setTickLabelPaint(TEXTO);
            eje.setAxisLinePaint(REJILLA);
            eje.setTickMarkPaint(REJILLA);
        }

        // Dataset 0: la curva (línea, sin formas)
        XYLineAndShapeRenderer rCurva = new XYLineAndShapeRenderer(true, false);
        rCurva.setSeriesPaint(0, CURVA);
        rCurva.setSeriesStroke(0, new BasicStroke(2.0f));
        plot.setRenderer(0, rCurva);

        // Dataset 1: los puntos de iteración (formas, sin línea)
        plot.setDataset(1, datasetPuntos);
        XYLineAndShapeRenderer rPuntos = new XYLineAndShapeRenderer(false, true);
        rPuntos.setSeriesPaint(0, PUNTOS);
        rPuntos.setSeriesShape(0, new Ellipse2D.Double(-4, -4, 8, 8));
        plot.setRenderer(1, rPuntos);

        // Línea horizontal en y = 0
        ValueMarker ejeCero = new ValueMarker(0.0);
        ejeCero.setPaint(TEXTO);
        ejeCero.setStroke(new BasicStroke(1.0f));
        plot.addRangeMarker(ejeCero);

        // Línea vertical punteada en la raíz aproximada
        double raiz = iteraciones.get(iteraciones.size() - 1).xiNext;
        ValueMarker markerRaiz = new ValueMarker(raiz);
        markerRaiz.setPaint(RAIZ);
        markerRaiz.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER,
                1.0f, new float[]{6.0f, 6.0f}, 0.0f));
        markerRaiz.setLabel("raíz ≈ " + String.format("%.5f", raiz));
        markerRaiz.setLabelPaint(RAIZ);
        plot.addDomainMarker(markerRaiz);
    }
}
