package sla.car;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import lombok.Data;
import sla.api.FX_CG_2D_API;
import sla.api.FX_CG_2D_API.Estilo;

@Data
public class Carro {
    FX_CG_2D_API api;
    private int x, y;
    private int width, height;

    private int left = 10, right = 0;

    public Carro(FX_CG_2D_API api, int x, int y, int width, int height) {
        this.api = api;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void desenhar(FX_CG_2D_API api) {
        api.preenchimento(Color.GREEN);
        api.retangulo(x, y, width, height, Estilo.PREENCHIDO);
        api.preenchimento(Color.RED);
        api.retangulo(x + left, y + 40, 10, 10, Estilo.PREENCHIDO);
        api.preenchimento(Color.BLUE);
        api.retangulo(x + right, y + 40, 10, 10, Estilo.PREENCHIDO);
    }

    public void atualizar(FX_CG_2D_API api) {

    }

    public void teclaPressionada(KeyEvent e) {
        if (e.getCode() == KeyCode.LEFT) {
            left+=5;
            right-=5;
        }
        if (e.getCode() == KeyCode.RIGHT) {
            right+=5;
            left-=5;
        }
    }
}
