package sla.rts.recursos;

import sla.rts.TipoRecurso;

public interface RecursoFonte {
    double coletar(double quantidade);
    TipoRecurso getTipo();
    int getX();
    int getY();
    boolean estaVazio();
    String toString();
}
