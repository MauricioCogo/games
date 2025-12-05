package sla.horror;

import java.net.URL;
import java.util.List;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import lombok.Data;
import sla.api.FX_CG_2D_API;
import sla.api.FX_CG_2D_API.Estilo;
import sla.horror.util.Wall;

@Data
public class Player {
    private int x, y;
    private int width = 30, height = 30;
    private int spd = 6;
    private int life = 5;

    private double tam = 500;

    private Gun armaAtual;

    private Gun pistola;
    private Gun escopeta;
    private Gun rifle;

    private double mouseX, mouseY;

    private FX_CG_2D_API api;

    private boolean up, down, left, right, walk, sneak, run;

    private boolean reload, reloded = true, die = false;
    private boolean invencivel = false;

    private Image coracao = new Image(getClass().getResource("/imagens/horror/coracao.png").toExternalForm());
    private Image vancock = new Image(getClass().getResource("/imagens/horror/vancock.png").toExternalForm());

    public Player(int x, int y, FX_CG_2D_API api) {
        this.x = x;
        this.y = y;
        this.api = api;

        api.iniciarTimer("walk", 0.5, true, () -> {
            walk = true;
        });

        URL somPistola = getClass().getResource("/sounds/som-pistola.mp3");
        URL somEscopeta = getClass().getResource("/sounds/som-shotgun.mp3");
        URL somRifle = getClass().getResource("/sounds/som-rifle.mp3");

        pistola = new Gun(10,1, 10.0, 1, "pistola", somPistola);
        escopeta = new Gun(4, 6, 11.0, 5, "escopeta", somEscopeta);
        rifle = new Gun(6, 1, 20.0, 1000, "rifle", somRifle);

        armaAtual = pistola;
    }

    public void desenhar() {


        api.imagem(vancock, x, y);

        double cx = x + width / 2.0;
        double cy = y + height / 2.0;

        armaAtual.desenharArma(api, cx, cy, mouseX, mouseY);
        armaAtual.desenhar(api, cx,cy);
        double raio = 500;

        for (int i = 0; i < life; i++) {
            api.imagem(coracao, cx + (i * 20) - 50, cy - 50);
        }

        for (int ang = 0; ang < 360; ang += 18) {

            double rad = Math.toRadians(ang);

            double px = cx + Math.cos(rad) * raio;
            double py = cy + Math.sin(rad) * raio;
            api.preenchimento(Color.BLACK);
            api.circulo(px - tam / 2, py - tam / 2, tam, tam, Estilo.PREENCHIDO);
        }
    }

    public void atualizar(List<Wall> walls) {

        if (tam<=1000) {
            tam+=0.1;
        }

        double hspd = 0;
        double vspd = 0;

        if (run) {
            spd = 10;
        }else{
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

        armaAtual.atualizar(api);

    }

    public Rectangle2D getBounds() {
        return new Rectangle2D(x, y, width, height);
    }

    public void teclaPressionada(KeyEvent e) {
        if (e.getCode() == KeyCode.W)
            up = true;
        if (e.getCode() == KeyCode.S)
            down = true;
        if (e.getCode() == KeyCode.A)
            left = true;
        if (e.getCode() == KeyCode.D)
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
        if (e.getCode() == KeyCode.W)
            up = false;
        if (e.getCode() == KeyCode.S)
            down = false;
        if (e.getCode() == KeyCode.A)
            left = false;
        if (e.getCode() == KeyCode.D)
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

    public void tomarDano() {
        if (invencivel)
            return;

        life--;

        invencivel = true;

        // invencibilidade de 1.5 segundos
        api.iniciarTimer("invencivel", 0.5, false, () -> {
            invencivel = false;
        });
    }

}
