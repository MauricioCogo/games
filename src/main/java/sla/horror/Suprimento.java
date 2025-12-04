package sla.horror;

import javafx.geometry.Rectangle2D;
import javafx.scene.paint.Color;
import sla.api.FX_CG_2D_API;
import sla.api.FX_CG_2D_API.Estilo;

public class Suprimento {

    private double x, y;
    private int size = 20;
    private boolean coletado = false;

    public Suprimento(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void desenhar(FX_CG_2D_API api) {
        if (!coletado) {
            api.preenchimento(Color.RED);
            api.retangulo(x,y,size,size, Estilo.PREENCHIDO);
        }
    }

    public Rectangle2D getBounds() {
        return new Rectangle2D(x, y, size, size);
    }

    public boolean isColetado() {
        return coletado;
    }

    public void coletar() {
        coletado = true;
    }
}
