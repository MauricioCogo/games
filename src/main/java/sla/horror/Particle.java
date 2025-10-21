package sla.horror;

import java.util.List;

import javafx.geometry.Rectangle2D;
import javafx.scene.paint.Color;
import lombok.Data;
import sla.api.FX_CG_2D_API;
import sla.api.FX_CG_2D_API.Estilo;

@Data
public class Particle {
    private double x, y;
    private double dx, dy;

    private int spd;

    private boolean colide = false;
    private int width = 10, height = 10;

    private FX_CG_2D_API api;

    public Particle(double x, double y, double dx, double dy, int spd, FX_CG_2D_API api) {
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
        this.spd = spd;
        this.api = api;
    }

    public void atualizar(List<Wall> walls) {
        if (colide)
            return;

        double nextX = x + dx * spd;
        double nextY = y + dy * spd;

        Rectangle2D nextBoundsX = new Rectangle2D(nextX, y, width, height);
        Rectangle2D nextBoundsY = new Rectangle2D(x, nextY, width, height);

        boolean collided = false;

        for (Wall w : walls) {
            Rectangle2D wallBounds = w.getBounds();

            // Colisão no X
            if (api.colisao(nextBoundsX, wallBounds)) {
                if (dx > 0)
                    x = wallBounds.getMinX() - width; // encosta à esquerda
                else if (dx < 0)
                    x = wallBounds.getMaxX(); // encosta à direita
                dx = 0;
                collided = true;
            }

            // Colisão no Y
            if (api.colisao(nextBoundsY, wallBounds)) {
                if (dy > 0)
                    y = wallBounds.getMinY() - height; // encosta em cima
                else if (dy < 0)
                    y = wallBounds.getMaxY(); // encosta embaixo
                dy = 0;
                collided = true;
            }
        }

        if (!collided) {
            x = nextX;
            y = nextY;
        } else {
            colide = true;
        }
    }

    public void desenhar() {
        api.preenchimento(Color.GREEN);
        api.circulo(x, y, width, height, Estilo.PREENCHIDO);
    }

    public Rectangle2D getBounds() {
        return new Rectangle2D(x, y, width, height);
    }

}
