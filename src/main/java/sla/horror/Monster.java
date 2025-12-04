package sla.horror;

import javafx.geometry.Rectangle2D;
import javafx.scene.paint.Color;
import lombok.Data;
import sla.api.FX_CG_2D_API;
import sla.api.FX_CG_2D_API.Estilo;

@Data
public class Monster {
    private int x, y;
    private int width = 30, height = 30;
    private int spd;

    private FX_CG_2D_API api;

    private boolean die = false;

    public Monster(int x, int y, int width, int height, int spd, FX_CG_2D_API api) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.spd = spd;
        this.api = api;
    }

    public void atualizar(Player p) {
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
        double cx = x + width / 2.0;
        double cy = y + height / 2.0;

        double separacao = width * 0.25;
        double olhoY = cy - height * 0.15;

        api.preenchimento(Color.RED);

        api.circulo(cx + separacao - 2.5, olhoY, 5, 5, Estilo.PREENCHIDO);

        api.circulo(cx - separacao - 2.5, olhoY, 5, 5, Estilo.PREENCHIDO);
    }

    public Rectangle2D getBounds() {
        return new Rectangle2D(x, y, width, height);
    }
}
