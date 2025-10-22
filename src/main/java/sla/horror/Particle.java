package sla.horror;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.geometry.Rectangle2D;
import javafx.scene.paint.Color;
import lombok.Data;
import sla.api.FX_CG_2D_API;
import sla.api.FX_CG_2D_API.Estilo;

@Data
public class Particle {
    private double x, y;
    private double dx, dy;
    private double spd;
    private boolean colide = false, stop = false;
    private int width = 10, height = 10;
    private Color cor;
    private FX_CG_2D_API api;
    private Random r;
    private List<Monster> ms;
    private char name;

    // controla o tipo de comportamento
    private boolean tipoRebate;
    private boolean temporariamenteParada;
    private double tempoParada;

    public Particle(double x, double y, double dx, double dy, int spd, Color cor, char name, int timer,
            FX_CG_2D_API api) {
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
        this.spd = spd;
        this.api = api;
        this.cor = cor;
        this.name = name;

        r = new Random();
        ms = new ArrayList<>();

        // decide aleatoriamente se é do tipo "rebate até sumir" ou "para e volta"
        tipoRebate = r.nextBoolean();

        // tempo aleatório de parada (para tipo que para)
        tempoParada = 1 + r.nextDouble() * 4; // entre 1 e 5 segundos

        // se for tipo que para, programa um timer para liberar depois
        if (!tipoRebate) {
            api.iniciarTimer("parti" + r.nextInt(9999), tempoParada, false, () -> {
                temporariamenteParada = false;
            });
        }
    }

    public void atualizar(List<Wall> walls) {
        if (temporariamenteParada || stop)
            return;

        spd -= 0.01;
        if (spd <= 0) {
            colide = true;
            return;
        }

        x += dx * spd;
        y += dy * spd;

        for (Wall w : walls) {
            if (api.colisao(getBounds(), w.getBounds())) {
                if (tipoRebate) {
                    double angle = r.nextDouble() * 2 * Math.PI;
                    dx = Math.cos(angle);
                    dy = Math.sin(angle);
                    spd -= 0.2;
                } else {
                    // comportamento "para por um tempo"
                    dx = 0;
                    dy = 0;
                }
            }
        }

        for (Monster m : ms) {
            if (name != 'p')
                continue;

            if (api.colisao(getBounds(), m.getBounds())) {
                dx *= -1;
                dy *= -1;
                dx += (r.nextDouble() - 0.5) * 0.2;
                dy += (r.nextDouble() - 0.5) * 0.2;
                spd -= 0.5;
                cor = Color.RED;
            }
        }
    }

    public void desenhar() {
        api.preenchimento(cor);
        api.circulo(x, y, width, height, Estilo.PREENCHIDO);
    }

    public Rectangle2D getBounds() {
        return new Rectangle2D(x, y, width, height);
    }
}
