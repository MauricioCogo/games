package sla.horror;

import java.util.List;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import lombok.Data;
import sla.api.FX_CG_2D_API;
import sla.api.FX_CG_2D_API.Estilo;

@Data
public class Monster {
    private int x, y;
    private int width = 30, height = 30;
    private double spd;
    private int life;

    private Image spr;

    private FX_CG_2D_API api;

    private boolean die = false;

    public Monster(int x, int y, int width, int height, double spd, FX_CG_2D_API api, Image spr, int life) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.spd = spd;
        this.api = api;
        this.spr = spr;
        this.life = life;
    }

    public void atualizar(Player p, List<Monster> monsters) {
        if (life <= 0) {
            die = true;
        }

        moverPara(p.getX(), p.getY());
        evitarEmpurro(monsters);
    }

    public void desenhar() {
        api.preenchimento(Color.DARKGREEN);
        api.circulo(x, y, width, height, Estilo.PREENCHIDO);
    }

    public void moverPara(int destinoX, int destinoY) {
        double dx = destinoX - x;
        double dy = destinoY - y;

        double dist = Math.sqrt(dx * dx + dy * dy);

        if (dist > 0) {

            double normX = dx / dist;
            double normY = dy / dist;

            double ruidoX = (Math.random() - 0.5) * 0.4;
            double ruidoY = (Math.random() - 0.5) * 0.4;

            double velocidade = spd * Math.min(dist / 80, 1.0);

            x += (normX + ruidoX) * velocidade;
            y += (normY + ruidoY) * velocidade;
        }
    }

    public void desenharOlhos() {
        api.imagem(spr, x, y);
    }

    public Rectangle2D getBounds() {
        return new Rectangle2D(x, y, width, height);
    }

    public void evitarEmpurro(List<Monster> todos) {
        for (Monster m : todos) {
            if (m == this)
                continue;

            if (this.getBounds().intersects(m.getBounds())) {
                double dx = this.x - m.x;
                double dy = this.y - m.y;

                double dist = Math.sqrt(dx * dx + dy * dy);
                if (dist == 0)
                    dist = 1;

                this.x += dx / dist * 2;
                this.y += dy / dist * 2;
            }
        }
    }
}
