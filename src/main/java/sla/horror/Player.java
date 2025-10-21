package sla.horror;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Rectangle2D;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import lombok.Data;
import sla.api.FX_CG_2D_API;
import sla.api.FX_CG_2D_API.Estilo;

@Data
public class Player {
    private int x, y;
    private int width = 30, height = 30;
    private int spd = 3;

    private FX_CG_2D_API api;
    private List<Particle> ps;

    private boolean up, down, left, right;

    private boolean reload, reloded = true, die = false;

    public Player(int x, int y, FX_CG_2D_API api) {
        this.x = x;
        this.y = y;
        this.api = api;
        ps = new ArrayList<>();
    }

    public void desenhar() {
        api.preenchimento(Color.WHITE);
        api.retangulo(x, y, width, height, Estilo.PREENCHIDO);
    }

    public void atualizar() {

        ps.removeIf(Particle::isColide);

        double hspd = 0;
        double vspd = 0;

        if (up && !down) {
            vspd = -spd;

        } else if (down && !up) {
            vspd = spd;

        }

        if (left && !right) {
            hspd = -spd;

        } else if (right && !left) {
            hspd = spd;
        }

        x += hspd;
        y += vspd;
    }

    public Rectangle2D getBounds() {
        return new Rectangle2D(x, y, width, height);
    }

    public void teclaPressionada(KeyEvent e) {
        if (e.getCode() == KeyCode.UP)
            up = true;
        if (e.getCode() == KeyCode.DOWN)
            down = true;
        if (e.getCode() == KeyCode.LEFT)
            left = true;
        if (e.getCode() == KeyCode.RIGHT)
            right = true;

        if (e.getCode() == KeyCode.SPACE && reloded) {
            createParticle();
            reloded = false;
            api.iniciarTimer("reload_player", 1.0, false, () -> reloded = true);
        }
    }

    public void teclaLiberada(KeyEvent e) {
        if (e.getCode() == KeyCode.UP)
            up = false;
        if (e.getCode() == KeyCode.DOWN)
            down = false;
        if (e.getCode() == KeyCode.LEFT)
            left = false;
        if (e.getCode() == KeyCode.RIGHT)
            right = false;
    }

    public void createParticle() {
        int total = 32; // ou 8, depende do efeito que você quer
        for (int i = 0; i < total; i++) {
            double angulo = i * (2 * Math.PI / total);
            double dx = Math.cos(angulo);
            double dy = Math.sin(angulo);
            Particle p = new Particle((double)x, (double)y, dx, dy, 5, this.api);
            ps.add(p);
        }
    }

}
