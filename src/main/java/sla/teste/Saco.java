package sla.teste;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import sla.api.FX_CG_2D_API;
import sla.api.FX_CG_2D_API.Estilo;

public class Saco {

    FX_CG_2D_API api;
    private int x, y;
    private int life = 100;
    private double width, height;
    private Image spr = new Image(getClass().getResource("/imagens/pkm/saco.gif").toExternalForm());

    public Saco(int x, int y, FX_CG_2D_API api) {
        this.x = x;
        this.y = y;
        this.width = spr.getWidth();
        this.height = spr.getHeight();

        this.api = api;
    }

    public void desenhar() {
        if (life > 0) {
            api.imagem(spr, x, y);
            api.preenchimento(Color.RED);
            api.retangulo(x, y - 40, 100, 20, Estilo.PREENCHIDO);
            for (int i = life; i >= 0; i--) {
                api.preenchimento(Color.GREEN);
                api.retangulo(x, y - 40, i, 20, Estilo.PREENCHIDO);
            }
        }
    }

    public Rectangle2D getBounds() {
        return new Rectangle2D(x, y, width, height);
    }

    public void setLife(int life) {
        this.life = life;
    }

    public int getLife() {
        return life;
    }
}
