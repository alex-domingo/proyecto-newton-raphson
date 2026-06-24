/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.matapli3.proyectonewtonraphson;

import com.matapli3.proyectonewtonraphson.Logica.Iteracion;
import com.matapli3.proyectonewtonraphson.Logica.NewtonRaphson;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Ludvi
 */
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

        // Mostrar derivada
        System.out.println("La derivada calculada automáticamente es: " +
                NewtonRaphson.getDerivada(funcion));

        // Resolver
        List<Iteracion> pasos = NewtonRaphson.solve(funcion, x0, tol, maxIter);

        // Imprimir pasos
        for (Iteracion it : pasos) {
            System.out.println(it);
        }

        // Mostrar raíz aproximada
        if (NewtonRaphson.converge()) {
            System.out.printf("La raíz aproximada es: %.6f%n", pasos.get(pasos.size() - 1).xiNext);
        } else {
            System.out.println("No fue posible encontrar una raíz con el método de Newton-Raphson.");
        }
    }
}
