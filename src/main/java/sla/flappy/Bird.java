package sla.flappy;

import java.util.Random;

import sla.api.FX_CG_2D_API;
import sla.api.FX_CG_2D_API.Estilo;
import javafx.geometry.Rectangle2D;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;

public class Bird {

    private double x, y;
    private double vspd = 0;
    private final double grav = 0.8;
    private final double jumpHeight = -12;
    private double pipeX = 0, pipeY = 0; // inicia em 0, não em NaN
    private Color cor;

    private final int width = 30, height = 30;

    private double peso1, peso2, peso3, peso4;
    private double saida1, saida2, saida3, saida4, soma1, soma3;

    private FX_CG_2D_API api;
    Random rand = new Random();

    private boolean die = false;

    public Bird(FX_CG_2D_API api, double startX, double startY) {
        this.api = api;
        this.x = startX - 200;
        this.y = startY;

        this.cor = Color.color(rand.nextDouble(), rand.nextDouble(), rand.nextDouble());

    }

    public void atualizar() {
        vspd += grav;
        y += vspd;

        if (y > api.alturaTela() - height) {
            y = api.alturaTela() - height;
            vspd = 0;
        }

        if (y < 0) {
            y = 0;
            vspd = 0;
        }

        // IA simples
        saida1 = calc(peso1, pipeX);
        saida2 = calc(peso2, pipeY);
        soma1 = saida1 + saida2;

        saida3 = calc(peso3, soma1);
        saida4 = calc(peso4, soma1);

        soma3 = saida3 + saida4;

        // condicional de pulo
        if (soma3 > 0) { // threshold ajustável
            vspd = jumpHeight;
        }
    }

    public void desenhar() {
        api.preenchimento(cor);
        api.retangulo(x, y, width, height, Estilo.PREENCHIDO);
    }

    public void teclaPressionada(KeyEvent e) {
    }

    public Rectangle2D getBounds() {
        return new Rectangle2D(x, y, width, height);
    }

    public boolean getDie() {
        return this.die;
    }

    public void setDie(boolean die) {
        this.die = die;
    }

    public void setPipe(double pipe_x, double pipe_y) {
        pipeX = pipe_x - x;
        pipeY = pipe_y - y;
    }

    public double calc(double peso, double var) {
        double saida = var * peso; 
        return saida;
    }

    public void setPesos(double peso12, double peso22, double peso32, double peso42) {
        this.peso1 = peso12;
        this.peso2 = peso22;
        this.peso3 = peso32;
        this.peso4 = peso42;
    }

    public double getPeso1() {
        return peso1;
    }

    public double getPeso2() {
        return peso2;
    }

    public double getPeso3() {
        return peso3;
    }

    public double getPeso4() {
        return peso4;
    }

}
