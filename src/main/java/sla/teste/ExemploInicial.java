package sla.teste;

import sla.api.FX_CG_2D_API;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ExemploInicial extends FX_CG_2D_API {

    Player player;

    public ExemploInicial(Stage stage) {
        super("Teste Maneiro", stage, 60, 640, 480);
    }

    public static void main(String[] args) {
        App.iniciar(cena -> new ExemploInicial(cena), args);
    }

    @Override
    public void acaoAoIniciar() {
        player = new Player(larguraTela() / 2, alturaTela() / 2);
    }

    @Override
    public void atualizar() {
        player.atualizar(this);
        
    }

    @Override
    public void desenhar() {
        limparTela(Color.WHITE);
        retangulo(0, 0, larguraTela(), alturaTela(), Estilo.PREENCHIDO);

        player.desenhar(this);
    }

    @Override
    public void teclaPressionada(KeyEvent e) {
        player.teclaPressionada(e, this);
    }

    @Override
    public void teclaLiberada(KeyEvent e) {
        player.teclaLiberada(e);
    }

    @Override public void acaoAoSair() {}
    @Override public void teclaDigitada(KeyEvent e) {}
    @Override public void cliqueDoMouse(MouseEvent e) {}
    @Override public void movimentoDoMouse(MouseEvent e) {}
    @Override public void movimentoDoMousePressionado(MouseEvent e) {}
    @Override public void mousePressionado(MouseEvent e) {}
}
