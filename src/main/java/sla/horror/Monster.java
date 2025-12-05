package sla.horror;

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

    public void atualizar(Player p) {
        if(life <= 0){
            die = true;
        }

        moverPara(p.getX(), p.getY());

        if(die){
            
        }
    }

    public void desenhar() {
        api.preenchimento(Color.DARKGREEN);
        api.circulo(x, y, width, height, Estilo.PREENCHIDO);
    }

    public void moverPara(int destinoX, int destinoY) {
        if (x < destinoX)
            x += spd;
        if (x > destinoX)
            x -= spd;
        if (y < destinoY)
            y += spd;
        if (y > destinoY)
            y -= spd;
    }

    public void desenharOlhos() {
        api.imagem(spr, x, y);

    }

    public Rectangle2D getBounds() {
        return new Rectangle2D(x, y, width, height);
    }
}
