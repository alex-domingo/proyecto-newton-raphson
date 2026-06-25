package com.matapli3.proyectonewtonraphson.Graficos;

import com.matapli3.proyectonewtonraphson.Logica.Iteracion;
import org.jfree.data.xy.XYSeries;
import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Expression;
import javax.swing.JPanel;
import java.util.List;

public class Graficador
{

    public static void graficar(JPanel panelDestino, String funcionTexto, List<Iteracion> iteraciones)
    {
        XYSeries serieFuncion = new XYSeries("f(x)");
        XYSeries serieIteraciones = new XYSeries("Iteraciones");
        // Aqui se determina el rango de la gráfica por las iteraciones
        double minX = iteraciones.get(0).xi;
        double maxX = iteraciones.get(0).xi;
        for (Iteracion iteracion : iteraciones)
        {
            if (iteracion.xi < minX) minX = iteracion.xi;
            if (iteracion.xi > maxX) maxX = iteracion.xi;
        }
        // Margen para la grafica
        minX -= 5.0;
        maxX += 5.0;
        // Se Generan los puntos de la curva de la función
        Argument xArg = new Argument("x");
        Expression f = new Expression(funcionTexto, xArg);
        // Calcula 200 puntos para que la curva sea mas fluida
        double paso = (maxX - minX) / 200.0;
        for (double x = minX; x <= maxX; x += paso)
        {
            xArg.setArgumentValue(x);
            double y = f.calculate();
            if (!Double.isNaN(y))
            {
                serieFuncion.add(x, y);
            }
        }
   }
}