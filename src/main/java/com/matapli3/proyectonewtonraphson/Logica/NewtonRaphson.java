package com.matapli3.proyectonewtonraphson.Logica;

import org.mariuszgromada.math.mxparser.*;
import java.util.ArrayList;
import java.util.List;

public class NewtonRaphson {

    private static final double DERIVADA_MINIMA = 1e-12;

    static {
        // Confirmación de uso no comercial de mXparser (solo registro interno).
        License.iConfirmNonCommercialUse("matapli3");
    }

    private NewtonRaphson() {
    } // clase de utilidad, no se instancia

    public static Resultado solve(String funcion, double x0, double tol, int maxIter) {
        Argument xArg = new Argument("x", x0);
        Expression f = new Expression(funcion, xArg);
        Expression fDeriv = new Expression("der(" + funcion + ", x)", xArg);

        List<Iteracion> pasos = new ArrayList<>();

        // A3: validar que f(x) y su derivada se interpretan bien antes de iterar
        if (!f.checkSyntax() || !fDeriv.checkSyntax()) {
            return new Resultado(pasos, EstadoConvergencia.FUNCION_INVALIDA);
        }

        double xi = x0;

        for (int i = 1; i <= maxIter; i++) {
            xArg.setArgumentValue(xi);

            double fx = f.calculate();
            double fpx = fDeriv.calculate();

            // B6: cortar si algo explotó
            if (esInvalido(fx) || esInvalido(fpx)) {
                return new Resultado(pasos, EstadoConvergencia.VALOR_INVALIDO);
            }

            // B: derivada casi cero → el método no puede continuar
            if (Math.abs(fpx) < DERIVADA_MINIMA) {
                return new Resultado(pasos, EstadoConvergencia.DERIVADA_CERO);
            }

            double xiNext = xi - fx / fpx;
            if (esInvalido(xiNext)) {
                return new Resultado(pasos, EstadoConvergencia.VALOR_INVALIDO);
            }

            double delta = calcularDelta(xi, xiNext);
            pasos.add(new Iteracion(i, xi, fx, fpx, xiNext, delta));

            // Criterio 1: el paso entre iteraciones cae bajo la tolerancia (A1: marca convergencia)
            if (delta < tol) {
                return new Resultado(pasos, EstadoConvergencia.CONVERGIO_PASO);
            }

            // Criterio 2 (A2): el residuo se evalúa en el PUNTO NUEVO, no en xi
            xArg.setArgumentValue(xiNext);
            double fxNext = f.calculate();
            if (!esInvalido(fxNext) && Math.abs(fxNext) < tol) {
                return new Resultado(pasos, EstadoConvergencia.CONVERGIO_RESIDUO);
            }

            xi = xiNext;
        }

        return new Resultado(pasos, EstadoConvergencia.MAX_ITERACIONES);
    }

    /**
     * B5: error relativo |(xiNext - xi)/xiNext|, o absoluto si xiNext es 0.
     */
    private static double calcularDelta(double xi, double xiNext) {
        if (xiNext != 0.0) {
            return Math.abs((xiNext - xi) / xiNext);
        }
        return Math.abs(xiNext - xi);
    }

    private static boolean esInvalido(double v) {
        return Double.isNaN(v) || Double.isInfinite(v);
    }

    public static String getDerivada(String funcion) {
        Expression deriv = new Expression("der(" + funcion + ", x)");
        return deriv.getExpressionString();
    }
}
