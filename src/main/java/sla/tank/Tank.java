package sla.tank;

import javafx.geometry.Rectangle2D;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import lombok.Data;
import lombok.EqualsAndHashCode;
import sla.api.FX_CG_2D_API;
import sla.api.FX_CG_2D_API.Estilo;

@Data
@EqualsAndHashCode(callSuper = false)
public class Tank implements Base {

    private int x, y;
    private int width, height;

    public Tank(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public void desenhar(FX_CG_2D_API api) {
        em
    }

    @Override
    public void atualizar(FX_CG_2D_API api) {
        
    }

    @Override
    public Rectangle2D getBounds() {
        return new Rectangle2D(x, y, width, height);
    }
}
