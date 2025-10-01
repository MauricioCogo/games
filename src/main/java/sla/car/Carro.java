package sla.car;

import static java.lang.Math.*;

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

    private int left = 3300, right = 3300; // raio de cada circulo
    private int wheelAngle = 3300;
    private int var = 100;
    private boolean circleL, circleR;

    private double angle, directionAngle;
    private int spd;

    public Carro(FX_CG_2D_API api, int x, int y, int width, int height) {
        this.api = api;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void desenhar(FX_CG_2D_API api) {
        System.out.println("angulo das rodas: " + wheelAngle + " esquerda: " + left + " direita: " + right);
        api.preenchimento(Color.GREEN);
        api.retangulo(x, y, width, height, Estilo.PREENCHIDO);
        
        if (circleR) {
            api.circulo(x + width, y - (right / 2), right, right, Estilo.LINHAS);
        }
        if (circleL) {
            api.circulo(x - left, y - (left / 2), left, left, Estilo.LINHAS);
        }
    }

    public void atualizar(FX_CG_2D_API api) {
        circleR = wheelAngle >= 100 && wheelAngle <= 3200;
        circleL = wheelAngle >= 3400 && wheelAngle <= 6500;

        directionAngle = toRadians((wheelAngle - 3300) / 100.0);

        angle += (spd / (double) width) * tan(directionAngle);

        x += (int) (spd * sin(angle));
        y += (int) (spd * cos(angle));

        System.out.println("angle: " + angle + " dir: " + directionAngle);
    }

    public void teclaPressionada(KeyEvent e) {
        if (e.getCode() == KeyCode.LEFT && left >= 200) {
            wheelAngle += var;
            left -= var;
            right += var;

        }
        if (e.getCode() == KeyCode.RIGHT && right >= 200) {
            wheelAngle -= var;
            left += var;
            right -= var;
        }

        if (e.getCode() == KeyCode.UP && spd <= 40) {
            spd = -10;
        }
        if (e.getCode() == KeyCode.DOWN && spd <= 40) {
            spd = 4;
        }
    }

    public void teclaLiberada(KeyEvent e) {
        if (e.getCode() == KeyCode.UP) {
            spd = 0;
        }
        if (e.getCode() == KeyCode.DOWN) {
            spd = 0;
        }
    }
}
