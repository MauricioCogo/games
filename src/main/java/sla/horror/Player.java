package sla.horror;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Rectangle2D;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import lombok.Data;
import sla.api.FX_CG_2D_API;

@Data
public class Player {
    private int x, y;
    private int width = 30, height = 30;
    private int spd = 3;

    private FX_CG_2D_API api;
    private List<Particle> ps;

    private boolean up, down, left, right, walk, sneak, run;

    private boolean reload, reloded = true, die = false;

    public Player(int x, int y, FX_CG_2D_API api) {
        this.x = x;
        this.y = y;
        this.api = api;
        ps = new ArrayList<>();

        api.iniciarTimer("walk", 0.5, true, () -> {
            walk = true;
        });
    }

    public void desenhar() {

    }

    public void atualizar(List<Wall> walls) {
        ps.removeIf(Particle::isColide);

        double hspd = 0;
        double vspd = 0;

        if ((up || down || left || right) && walk) {
            if (sneak) {
                createParticle(6, 1, Color.BLACK.brighter(), 2);
                spd = 1;
                walk = false;
            } else {
                createParticle(10, 4, Color.GRAY.darker(), 7);
                spd = 3;
                walk = false;
            }
        }

        if (run) {
            spd = 6;
        }

        if (up && !down)
            vspd = -spd;
        else if (down && !up)
            vspd = spd;

        if (left && !right)
            hspd = -spd;
        else if (right && !left)
            hspd = spd;

        double nextX = x + hspd;
        double nextY = y + vspd;

        Rectangle2D nextBounds = new Rectangle2D(nextX, nextY, width, height);

        boolean colide = false;
        for (Wall w : walls) {
            if (api.colisao(nextBounds, w.getBounds())) {
                colide = true;
                break;
            }
        }

        if (!colide) {
            x = (int) nextX;
            y = (int) nextY;
        }

        for (Particle p : ps) {
            p.atualizar(walls);
        }
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
        if (e.getCode() == KeyCode.SHIFT)
            sneak = true;
        if (e.getCode() == KeyCode.CONTROL)
            run = true;

        if (e.getCode() == KeyCode.SPACE && reloded) {
            createParticle(16, 7, Color.WHITE.darker(), 10);
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
        if (e.getCode() == KeyCode.SHIFT)
            sneak = false;
        if (e.getCode() == KeyCode.CONTROL)
            run = false;
    }

    public void createParticle(int total, int spd, Color cor, int timer) {
        for (int i = 0; i < total; i++) {
            double angulo = i * (2 * Math.PI / total);
            double dx = Math.cos(angulo);
            double dy = Math.sin(angulo);
            Particle p = new Particle((double) x, (double) y, dx, dy, spd, cor, 'p', timer, this.api);
            ps.add(p);
        }
    }

}
