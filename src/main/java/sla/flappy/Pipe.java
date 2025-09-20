package sla.flappy;

import sla.api.FX_CG_2D_API;
import sla.api.FX_CG_2D_API.Estilo;
import javafx.geometry.Rectangle2D;
import javafx.scene.paint.Color;

public class Pipe {
    private double x;
    private double yTop;
    private double gap = 150;
    private double hspd = 3;

    private final int width = 50;

    private double middleY;
    private double middleX;

    private FX_CG_2D_API api;

    public Pipe(FX_CG_2D_API api, double startX) {
        this.api = api;
        this.x = startX;

        this.yTop = 50 + Math.random() * (api.alturaTela() - gap - 200);

        middleY = yTop + (gap / 2);

    }

    public void atualizar() {
        x -= hspd;
        middleX = x + width / 2.0;
    }

    public void desenhar() {
        api.preenchimento(Color.GREEN);
        api.retangulo(x, 0, width, yTop, Estilo.PREENCHIDO);
        api.retangulo(x, yTop + gap, width, api.alturaTela() - (yTop + gap), Estilo.PREENCHIDO);
    }

    public Rectangle2D getTopBounds() {
        return new Rectangle2D(x, 0, width, yTop);
    }

    public Rectangle2D getBottomBounds() {
        return new Rectangle2D(x, yTop + gap, width, api.alturaTela() - (yTop + gap));
    }

    public double getX() {
        return x;
    }

    public double getMiddleY() {
        return middleY;
    }

    public double getMiddleX() {
        return middleX;
    }

    public int getWidth() {
        return width;
    }
}
