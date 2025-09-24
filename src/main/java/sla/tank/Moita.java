package sla.tank;

import javafx.geometry.Rectangle2D;
import javafx.scene.paint.Color;
import sla.api.FX_CG_2D_API;
import sla.api.FX_CG_2D_API.Estilo;

public class Moita implements Base {

    private int x, y;
    private int width, height;

    public Moita(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public void desenhar(FX_CG_2D_API api) {
        api.preenchimento(Color.DARKGREEN);
        api.circulo(x, y, width, height, Estilo.PREENCHIDO);
    }

    @Override
    public void atualizar(FX_CG_2D_API api) {

    }

    @Override
    public Rectangle2D getBounds() {
        return new Rectangle2D(x, y, width, height);
    }

}
