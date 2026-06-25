package com.matapli3.proyectonewtonraphson.Logica;

public class Iteracion {

    public final int numero;
    public final double xi;
    public final double fx;
    public final double fpx;
    public final double xiNext;
    public final double delta;

    public Iteracion(int numero, double xi, double fx, double fpx, double xiNext, double delta) {
        this.numero = numero;
        this.xi = xi;
        this.fx = fx;
        this.fpx = fpx;
        this.xiNext = xiNext;
        this.delta = delta;
    }

    @Override
    public String toString() {
        return String.format("Iteración %d: xi = %.6f, f(x) = %.6f, f'(x) = %.6f, xi+1 = %.6f, δ = %.6f",
                numero, xi, fx, fpx, xiNext, delta);
    }
}
