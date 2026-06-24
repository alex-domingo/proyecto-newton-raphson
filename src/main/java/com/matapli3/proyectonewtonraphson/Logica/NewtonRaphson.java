package com.matapli3.proyectonewtonraphson.Logica;

import com.sun.jdi.connect.Connector;
import org.mariuszgromada.math.mxparser.*;
import java.util.ArrayList;
import java.util.List;

public class NewtonRaphson {
    private static boolean converge = false;

    public static List<Iteracion> solve(String funcion, double x0, double tol, int maxIter) {
        converge = false;

        Argument xArg = new Argument("x", x0);

        Expression f = new Expression(funcion, xArg);
        Expression fDeriv = new Expression("der(" + funcion + ", x)", xArg);

        List<Iteracion> pasos = new ArrayList<>();

        double xi = x0;

        for (int i = 1; i <= maxIter; i++) {

            xArg.setArgumentValue(xi);

            double fx = f.calculate();

            double fpx = fDeriv.calculate();

            if (Math.abs(fpx) < 1e-12) {
                System.out.println("La derivada es muy pequeña. Iteración detenida.");
                break;
            }
            double xiNext = xi - fx / fpx;

            pasos.add(new Iteracion(i, xi, fx, fpx, xiNext));

            if (Math.abs((xiNext - xi)/xiNext) < tol)
                break;

            if (Math.abs(fx) < tol) {
                converge = true;
                break;
            }
            xi = xiNext;
        }
        return pasos;
    }

    public static String getDerivada(String funcion) {
        Expression deriv = new Expression("der(" + funcion + ", x)");
        return deriv.getExpressionString();
    }

    public static boolean converge() {
        return converge;
    }
}