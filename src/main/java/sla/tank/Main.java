package sla.tank;

import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import sla.api.FX_CG_2D_API;

public class Main extends FX_CG_2D_API {

    private Tank tank;
    private Moita moita;

    public Main(Stage stage) {
        super("Tank Offline", stage, 60, 640, 480);
    }

    public static void main(String[] args) {
        App.iniciar(cena -> new Main(cena), args);
    }

    @Override
    public void acaoAoIniciar() {
        tank = new Tank(alturaTela() / 2, larguraTela() / 2, 40, 40);
        moita = new Moita(100, 100, 50, 50);
    }

    @Override
    public void atualizar() {
        tank.atualizar(this);
    }

    @Override
    public void desenhar() {
        limparTela(Color.WHITE);

        tank.desenhar(this);
        moita.desenhar(this);
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
        tank.shoot(e,this);
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
