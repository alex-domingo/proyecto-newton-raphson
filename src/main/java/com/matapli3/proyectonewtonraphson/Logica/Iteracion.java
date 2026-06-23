package com.matapli3.proyectonewtonraphson.Logica;

public class Iteracion {
    public int numero;
    public double xi;
    public double fx;
    public double fpx;
    public double xiNext;
    public double delta;

    public Iteracion(int numero, double xi, double fx, double fpx, double xiNext) {
        this.numero = numero;
        this.xi = xi;
        this.fx = fx;
        this.fpx = fpx;
        this.xiNext = xiNext;
        this.delta = Math.abs(xiNext - xi);
    }

    @Override
    public String toString() {
        return String.format("Iteración %d: xi = %.6f, f(x) = %.6f, f'(x) = %.6f, xi+1 = %.6f, δ = %.6f",
                numero, xi, fx, fpx, xiNext, delta);
    }
}