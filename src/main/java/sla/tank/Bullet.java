package sla.tank;

import javafx.geometry.Rectangle2D;
import javafx.scene.paint.Color;
import lombok.Data;
import sla.api.FX_CG_2D_API;
import sla.api.FX_CG_2D_API.AcaoTimer;
import sla.api.FX_CG_2D_API.Estilo;

@Data
public class Bullet implements Base {
    private double x, y;
    private double dx, dy;
    private double speed = 8;
    private int size = 6;
    private int remain = 5;

    private boolean die = false;
    private boolean colid = false;

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

        if (remain != 0) {
            if ((x >= api.larguraTela()) || (x < 0)) {
                dx *= -1;
                remain--;
            }
            if ((y >= api.alturaTela()) || (y < 0)) {
                dy *= -1;
                remain--;
            }
        } else {
            die = true;
        }

        if (!colid) {
            api.iniciarTimer("bullet", 0.2, false, new AcaoTimer() {

                @Override
                public void executar() {
                    colid = true;
                }

            });
        }
    }

    @Override
    public void desenhar(FX_CG_2D_API api) {
        api.preenchimento(Color.BLACK);
        api.circulo(x - size / 2, y - size / 2, size, size, Estilo.PREENCHIDO);
    }

    @Override
    public Rectangle2D getBounds() {
        return new Rectangle2D(x, y, size, size);
    }

    public boolean getDie() {
        return die;
    }

    public boolean getColid() {
        return colid;
    }
}
