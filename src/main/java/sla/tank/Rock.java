package sla.tank;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import sla.api.FX_CG_2D_API;

public class Rock implements Base {

    private int x, y;
    private int width, height;
    private Image spr;

    public Rock(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        spr = new Image(getClass().getResource("/imagens/tank/rock.gif").toExternalForm());
    }

    @Override
    public void desenhar(FX_CG_2D_API api) {
        api.imagem(spr, x, y);
    }

    @Override
    public void atualizar(FX_CG_2D_API api) {

    }

    @Override
    public Rectangle2D getBounds() {
        return new Rectangle2D(x, y, width, height);
    }

}
