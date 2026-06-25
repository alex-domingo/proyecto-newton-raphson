package com.matapli3.proyectonewtonraphson.Logica;

public enum EstadoConvergencia {
    CONVERGIO_PASO("Convergió: el cambio entre iteraciones es menor que la tolerancia."),
    CONVERGIO_RESIDUO("Convergió: |f(x)| es menor que la tolerancia."),
    MAX_ITERACIONES("No convergió: se agotó el número máximo de iteraciones."),
    DERIVADA_CERO("Detenido: la derivada se hizo prácticamente cero (tangente horizontal)."),
    VALOR_INVALIDO("Detenido: apareció un valor no numérico (NaN o infinito) durante el cálculo."),
    FUNCION_INVALIDA("Error: f(x) o su derivada no se pudieron interpretar. Revisa la sintaxis.");

    private final String mensaje;

    EstadoConvergencia(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getMensaje() {
        return mensaje;
    }

    public boolean esConvergencia() {
        return this == CONVERGIO_PASO || this == CONVERGIO_RESIDUO;
    }
}
