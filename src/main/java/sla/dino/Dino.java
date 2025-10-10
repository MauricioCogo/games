package sla.dino;

import javafx.geometry.Rectangle2D;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import lombok.Data;
import sla.api.FX_CG_2D_API;
import sla.api.FX_CG_2D_API.Estilo;

@Data
public class Dino {
    double x, y, hspd = 0, vspd = 0;
    private int width, height;
    private int ground = 300;

    private boolean up, down;
    private boolean jumping;
    final double jumpHeight = -12, grav = 0.5;

    private FX_CG_2D_API api;

    public Dino(FX_CG_2D_API api) {
        this.api = api;
        x = 100;
        y = 400;
        width = 40;
        height = 60;
    }

    public void atualizar() {
        vspd += grav;
        x += hspd;
        y += vspd;

        // colisão com chão
        if (y >= ground) {
            y = ground;
            vspd = 0;
        }

        if (jumping && y >= ground) {
            vspd = jumpHeight;
            jumping = false;
        }

        if (down) {
            vspd += 5;
        }
    }

    public void desenhar() {
        api.preenchimento(Color.GREEN);
        if (down) {
            api.retangulo(x, y + height / 2, width, height / 2, Estilo.PREENCHIDO);
        } else {
            api.retangulo(x, y, width, height, Estilo.PREENCHIDO);
        }
    }

    public void teclaPressionada(KeyEvent e) {
        if (e.getCode() == KeyCode.UP && y >= ground) {
            jumping = true;
        }
        if (e.getCode() == KeyCode.DOWN) {
            down = true;
        }
    }

    public void teclaLiberada(KeyEvent e) {
        if (e.getCode() == KeyCode.UP) {
            up = false;
        }
        if (e.getCode() == KeyCode.DOWN) {
            down = false;
        }
    }

    public Rectangle2D getBounds(){
        if (down) {
            return new Rectangle2D(x, y + height / 2, width, height / 2);
        }else{
            return new Rectangle2D(x, y, width, height);
        }
    }
}
