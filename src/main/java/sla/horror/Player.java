package sla.horror;

import java.util.List;

import javafx.geometry.Rectangle2D;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import lombok.Data;
import sla.api.FX_CG_2D_API;
import sla.api.FX_CG_2D_API.Estilo;

@Data
public class Player {
    private int x, y;
    private int width = 30, height = 30;
    private int spd = 6;
    private int life = 5;

    private Gun armaAtual;

    private Gun pistola;
    private Gun escopeta;
    private Gun rifle;

    private double mouseX, mouseY;

    private FX_CG_2D_API api;

    private boolean up, down, left, right, walk, sneak, run;

    private boolean reload, reloded = true, die = false;

    public Player(int x, int y, FX_CG_2D_API api) {
        this.x = x;
        this.y = y;
        this.api = api;

        api.iniciarTimer("walk", 0.5, true, () -> {
            walk = true;
        });

        pistola = new Gun(1, 10.0, 1, "pistola.png");
        escopeta = new Gun(6, 11.0, 1, "escopeta.png");
        rifle = new Gun(1, 20.0, 3, "rifle.png");
        
        armaAtual = pistola;
    }

    public void desenhar() {

        api.preenchimento(Color.color(1.0, 0.80, 0.74));

        api.retangulo(x, y, width, height, Estilo.PREENCHIDO);

        double cx = x + width / 2.0;
        double cy = y + height / 2.0;

        armaAtual.desenharArma(api, cx, cy, mouseX, mouseY);
        armaAtual.desenhar(api);
        double raio = 500;

        double tam = 600;

        api.preenchimento(Color.BLACK);

        for (int ang = 0; ang < 360; ang += 18) {

            double rad = Math.toRadians(ang);

            double px = cx + Math.cos(rad) * raio;
            double py = cy + Math.sin(rad) * raio;

            api.circulo(px - tam / 2, py - tam / 2, tam, tam, Estilo.PREENCHIDO);
        }
    }

    public void atualizar(List<Wall> walls) {

        double hspd = 0;
        double vspd = 0;

        if (run) {
            spd = 10;
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

        armaAtual.atualizar(api);

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

        if (e.getCode() == KeyCode.DIGIT1)
            armaAtual = pistola;
        if (e.getCode() == KeyCode.DIGIT2)
            armaAtual = escopeta;
        if (e.getCode() == KeyCode.DIGIT3)
            armaAtual = rifle;

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

    public void atirar(MouseEvent e) {
        double cx = x + width / 2.0;
        double cy = y + height / 2.0;
        armaAtual.atirar(e, cx, cy, mouseX, mouseY, api);
    }

    public void atualizarMouse(double mouseTelaX, double mouseTelaY, double camX, double camY) {
        this.mouseX = mouseTelaX + camX - api.larguraTela() / 2;
        this.mouseY = mouseTelaY + camY - api.alturaTela() / 2;
    }

}
