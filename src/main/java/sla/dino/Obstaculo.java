package sla.dino;

import java.util.Random;

import javafx.geometry.Rectangle2D;
import javafx.scene.paint.Color;
import lombok.Data;
import sla.api.FX_CG_2D_API;
import sla.api.FX_CG_2D_API.Estilo;

@Data
public class Obstaculo {
    private double x, y, hspd = 0, vspd = 0;
    private double vel = -4;
    private int width, height;
    private int ground = 300;

    private boolean air;
    private boolean die = false;

    private FX_CG_2D_API api;
    private Random r = new Random();

    public Obstaculo(FX_CG_2D_API api) {
        this.api = api;
        x = 600;

        air = r.nextBoolean();

        if (!air) {
            y = ground;
        } else {
            y = ground - 40;
        }

        width = 40;
        height = 60;
    }

    public void atualizar() {
        hspd = vel;
        x += hspd;
    }

    public void desenhar() {
        if (air) {
            api.preenchimento(Color.GRAY);
        } else {
            api.preenchimento(Color.YELLOW);
        }
        api.retangulo(x, y, width, height, Estilo.PREENCHIDO);
    }

    public Rectangle2D getBounds() {

        return new Rectangle2D(x, y, width, height);

    }
}
