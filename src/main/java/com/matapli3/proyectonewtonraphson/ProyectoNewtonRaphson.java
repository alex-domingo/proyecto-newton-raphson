package com.matapli3.proyectonewtonraphson;

import com.matapli3.proyectonewtonraphson.Logica.Iteracion;
import com.matapli3.proyectonewtonraphson.Logica.NewtonRaphson;
import com.matapli3.proyectonewtonraphson.Logica.Resultado;
import java.util.Scanner;

public class ProyectoNewtonRaphson {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Método de Newton-Raphson");
        System.out.print("Ingrese la función f(x): ");
        String funcion = sc.nextLine();

        System.out.print("Ingrese valor inicial x0: ");
        double x0 = sc.nextDouble();

        System.out.print("Ingrese tolerancia: ");
        double tol = sc.nextDouble();

        System.out.print("Ingrese número máximo de iteraciones: ");
        int maxIter = sc.nextInt();

        System.out.println("La derivada calculada automáticamente es: "
                + NewtonRaphson.getDerivada(funcion));

        Resultado resultado = NewtonRaphson.solve(funcion, x0, tol, maxIter);

        for (Iteracion it : resultado.getPasos()) {
            System.out.println(it);
        }

        System.out.println(resultado.getEstado().getMensaje());
        if (resultado.convergio()) {
            System.out.printf("La raíz aproximada es: %.6f%n", resultado.getRaiz());
        }
    }
}
