package sla.flappy;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import sla.api.FX_CG_2D_API;

public class Pipe {
    private double x;
    private double yTop;
    private double gap = 120;
    private double hspd = 3;

    private final int width = 50;

    private double middleY;
    private double middleX;

    private FX_CG_2D_API api;

    private Image inicio, tubo; 

    public Pipe(FX_CG_2D_API api, double startX) {
        this.api = api;
        this.x = startX;

        this.yTop = 50 + Math.random() * (api.alturaTela() - gap - 100);

        middleY = yTop + (gap / 2);

        tubo = new Image(getClass().getResource("/imagens/bird/cano_corpo.gif").toExternalForm());
        inicio = new Image(getClass().getResource("/imagens/bird/cano_ponta.gif").toExternalForm());

    }

    public void atualizar() {
        x -= hspd;
        middleX = x + width / 2.0;
    }

    public void desenhar() {
        double y = 0;

        while (y + tubo.getHeight() < yTop) {
            api.imagem(tubo, x, y);
            y += tubo.getHeight();
        }

        // Desenha a ponta do cano sobre o último tubo
        api.imagem(inicio, x, yTop - inicio.getHeight());

        // Desenha corpo do cano inferior: repete o tubo até o final da tela
        y = yTop + gap + inicio.getHeight();
        while (y < api.alturaTela()) {
            api.imagem(tubo, x, y);
            y += tubo.getHeight();
        }

        // Desenha a ponta do cano inferior
        api.imagem(inicio, x, yTop + gap);
    }

    public Rectangle2D getTopBounds() {
        return new Rectangle2D(x, 0, width, yTop);
    }

    public Rectangle2D getBottomBounds() {
        return new Rectangle2D(x, yTop + gap, width, api.alturaTela() - (yTop + gap));
    }

    public double getX() {
        return x;
    }

    public double getMiddleY() {
        return middleY;
    }

    public double getMiddleX() {
        return middleX;
    }

    public int getWidth() {
        return width;
    }
}
