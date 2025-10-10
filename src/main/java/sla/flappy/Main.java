package sla.flappy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import sla.api.FX_CG_2D_API;
import javafx.application.Platform;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends FX_CG_2D_API {

    List<Bird> bs;
    List<Pipe> p;
    Random rand = new Random();
    int geracao = 0, num_b, timer, timerA;

    public Main(Stage stage) {
        super("Flappy Bird Simples", stage, 60, 640, 480);
    }

    public static void main(String[] args) {
        App.iniciar(cena -> new Main(cena), args);
    }

    @Override
    public void acaoAoSair() {
    }

    @Override
    public void acaoAoIniciar() {
        p = new ArrayList<>();
        bs = new ArrayList<>();

        Platform.runLater(() -> cria(0, 0, 0, 0));

        iniciarTimer("pipe", 1.3, true, new AcaoTimer() {
            @Override
            public void executar() {
                Pipe cano = new Pipe(Main.this, larguraTela());
                p.add(cano);
                System.out.println("add cano");
            }
        });
    }

    @Override
    public void atualizar() {
        timer++;
        boolean todos = true;
        Bird ultimo = null;
        for (Bird b : bs) {
            if (b != null && !b.getDie()) {
                todos = false;
                ultimo = b;
                b.atualizar();

                Pipe alvo = null;
                for (Pipe pipe : p) {
                    if (pipe.getX() + pipe.getWidth() > b.getBounds().getMinX()) {
                        alvo = pipe;
                        break;
                    }
                }

                if (alvo != null) {
                    b.setPipe(alvo.getMiddleX(), alvo.getMiddleY());

                    if ((colisao(b.getBounds(), alvo.getBottomBounds()))
                            || (colisao(b.getBounds(), alvo.getTopBounds()))) {
                        b.setDie(true);
                        num_b--;
                        EfeitosSonoros.tocarSom("taco", true, true);
                        System.out.println("colidiu");
                    }
                }
            }
        }

        for (Pipe pipe : p) {
            pipe.atualizar();
        }

        p.removeIf(pipe -> pipe.getX() + pipe.getWidth() < 0);

        if (todos) {
            p.clear();
            bs.clear();
            timerA = timer;
            timer = 0;
            geracao++;

            if (ultimo != null) {
                double p1 = ultimo.getPeso1();
                double p2 = ultimo.getPeso2();
                double p3 = ultimo.getPeso3();
                double p4 = ultimo.getPeso4();

                cria(p1, p2, p3, p4);

            } else {
                cria(0, 0, 0, 0);
            }

            System.out.println("acabou");
        }
    }

    @Override
    public void desenhar() {
        limparTela(Color.WHITE);

        empilhar();
        for (Pipe pipe : p) {
            pipe.desenhar();
        }

        // desenha apenas pássaros vivos
        for (Bird b : bs) {
            if (b != null && !b.getDie()) {
                b.desenhar();
            }
        }
        texto("Geração: " + geracao, 20, 20, 20);
        texto("passaros: " + num_b, 20, 40, 20);
        texto("tempo: " + timer, 20, 60, 20);
        texto("tempo anterior: " + timerA, 20, 80, 20);
        desempilhar();
    }

    @Override
    public void teclaPressionada(KeyEvent e) {
    }

    @Override
    public void teclaLiberada(KeyEvent e) {
    }

    @Override
    public void teclaDigitada(KeyEvent e) {
    }

    @Override
    public void cliqueDoMouse(MouseEvent e) {
    }

    @Override
    public void movimentoDoMouse(MouseEvent e) {
    }

    @Override
    public void movimentoDoMousePressionado(MouseEvent e) {
    }

    @Override
    public void mousePressionado(MouseEvent e) {
    }

    public void cria(double peso11, double peso22, double peso33, double peso44) {
        for (int i = 0; i < 700; i++) {
            double peso1, peso2, peso3, peso4;

            if (peso11 == 0) {
                peso1 = -1000 + rand.nextDouble() * 2000;
                peso2 = -1000 + rand.nextDouble() * 2000;
                peso3 = -1000 + rand.nextDouble() * 2000;
                peso4 = -1000 + rand.nextDouble() * 2000;
            } else {
                peso1 = mutar(peso11);
                peso2 = mutar(peso22);
                peso3 = mutar(peso33);
                peso4 = mutar(peso44);
            }

            Bird b = new Bird(this, this.larguraTela() / 2 + rand.nextDouble() * 120, this.alturaTela() / 2);
            num_b++;

            b.setPesos(peso1, peso2, peso3, peso4);
            bs.add(b);
        }
    }

    private double mutar(double peso) {
        if (rand.nextDouble() < 0.01) {
            peso += -100 + rand.nextDouble() * 100;
        }
        if (peso > 1000)
            peso = 1000;
        if (peso < -1000)
            peso = -1000;

        return peso;
    }
}
