package sla.tank;

import javafx.geometry.Rectangle2D;
import javafx.scene.paint.Color;
import sla.api.FX_CG_2D_API;
import sla.api.FX_CG_2D_API.Estilo;

public class Bullet implements Base {
    private double x, y;
    private double dx, dy;
    private double speed = 8;
    private int size = 6;

    public Bullet(double x, double y, double dx, double dy) {
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
    }

    @Override
    public void atualizar(FX_CG_2D_API api) {
        x += dx * speed;
        y += dy * speed;
    }

    @Override
    public void desenhar(FX_CG_2D_API api) {
        api.preenchimento(Color.BLACK);
        api.circulo(x - size/2, y - size/2, size, size, Estilo.PREENCHIDO);
    }

    @Override
    public Rectangle2D getBounds() {
        return new Rectangle2D(x, y, size, size);
    }
}
