package sla.tank;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import sla.api.FX_CG_2D_API;

public class Main extends FX_CG_2D_API {

    private Tank tank;
    private List<Moita> moitas;
    private List<Rock> pedras;
    Random r;

    public Main(Stage stage) {
        super("Tank Offline", stage, 60, 640, 480);
    }

    public static void main(String[] args) {
        App.iniciar(cena -> new Main(cena), args);
    }

    @Override
    public void acaoAoIniciar() {
        tank = new Tank(alturaTela() / 2, larguraTela() / 2, 40, 40);
        r = new Random();

        moitas = new ArrayList<>();
        pedras = new ArrayList<>();
        int qntPedra = r.nextInt(6) + 1;
        int qntMoita = r.nextInt(6) + 1;

        for (int i = 0; i < qntMoita; i++) {
            int x = r.nextInt(larguraTela());
            int y = r.nextInt(alturaTela());
            Moita m = new Moita(x, y, 50, 50);
            moitas.add(m);
        }
        for (int i = 0; i < qntPedra; i++) {
            int x = r.nextInt(larguraTela());
            int y = r.nextInt(alturaTela());
            Rock rock = new Rock(x, y, 50, 50);
            pedras.add(rock);
        }
    }

    @Override
    public void atualizar() {
        if (!tank.getDie()) {
            tank.atualizar(this);
        }

        for (Moita moita : moitas) {
            moita.atualizar(this);
        }
        for (Rock pedra : pedras) {
            pedra.atualizar(this);
        }

        for (Bullet b : tank.getBl()) {
            if (b.getColid() && colisao(b.getBounds(), tank.getBounds())) {
                tank.setDie(true);
            }

            for (Rock pedra : pedras) {
                if (b.getColid() && colisao(b.getBounds(), pedra.getBounds())) {
                    b.setDie(true);

                }
            }

        }

        tank.getBl().removeIf(Bullet::getDie);
    }

    @Override
    public void desenhar() {
        limparTela(Color.GREEN.brighter());

        if (!tank.getDie()) {
            tank.desenhar(this);
        }

        boolean estaEscondido = false;

        for (Moita moita : moitas) {
            moita.desenhar(this);

            if (colisao(tank.getBounds(), moita.getBounds())) {
                estaEscondido = true;
            }
        }

        tank.setHidden(estaEscondido);

        for (Rock pedra : pedras) {
            pedra.desenhar(this);
        }
    }

    @Override
    public void acaoAoSair() {

    }

    @Override
    public void teclaPressionada(KeyEvent e) {
        tank.teclaPressionada(e, this);
    }

    @Override
    public void teclaLiberada(KeyEvent e) {
        tank.teclaLiberada(e);
    }

    // #region
    @Override
    public void teclaDigitada(KeyEvent e) {

    }

    @Override
    public void cliqueDoMouse(MouseEvent e) {
        tank.shoot(e, this);
    }

    @Override
    public void movimentoDoMouse(MouseEvent e) {
        tank.atualizarMouse(e.getX(), e.getY());
    }

    @Override
    public void movimentoDoMousePressionado(MouseEvent e) {

    }

    @Override
    public void mousePressionado(MouseEvent e) {

    }

    // #endregion
}
