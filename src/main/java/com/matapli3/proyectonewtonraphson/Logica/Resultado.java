package com.matapli3.proyectonewtonraphson.Logica;

import java.util.Collections;
import java.util.List;

public class Resultado {

    private final List<Iteracion> pasos;
    private final EstadoConvergencia estado;

    public Resultado(List<Iteracion> pasos, EstadoConvergencia estado) {
        this.pasos = pasos;
        this.estado = estado;
    }

    public List<Iteracion> getPasos() {
        return Collections.unmodifiableList(pasos);
    }

    public EstadoConvergencia getEstado() {
        return estado;
    }

    public boolean convergio() {
        return estado.esConvergencia();
    }

    public boolean hayPasos() {
        return !pasos.isEmpty();
    }

    /**
     * Última aproximación calculada, o NaN si no hay pasos.
     */
    public double getRaiz() {
        if (pasos.isEmpty()) {
            return Double.NaN;
        }
        return pasos.get(pasos.size() - 1).xiNext;
    }
}
