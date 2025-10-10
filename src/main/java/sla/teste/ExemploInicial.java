package sla.teste;

import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import sla.api.FX_CG_2D_API;

public class ExemploInicial extends FX_CG_2D_API {

    Player player;
    Player player2;

    public ExemploInicial(Stage stage) {
        super("Teste Maneiro", stage, 60, 640, 480);
    }

    public static void main(String[] args) {
        App.iniciar(cena -> new ExemploInicial(cena), args);
    }

    @Override
    public void acaoAoIniciar() {
        player = new Player((larguraTela() / 2) - 100, alturaTela() / 2, "Mauricio",true, this);
        player2 = new Player((larguraTela() / 2) + 100, alturaTela() / 2, "Benhur", false, this);

    }

    @Override
    public void atualizar() {
        player.atualizar();
        player2.atualizar();
        if (player.ataqueAtual != null && colisao(player.ataqueAtual.getBounds(), player2.getBounds()) && player.ataqueAtual.getColide()) {
            player2.health.setLife(player2.health.getLife() - 5);
            player.ataqueAtual.setColide(false);
            player.health.setStrong(player.health.getStrong()+10);
            System.out.println(player.health.getStrong());
        }
        if (player2.ataqueAtual != null && colisao(player2.ataqueAtual.getBounds(), player.getBounds())
                && player2.ataqueAtual.getColide()) {
            player.health.setLife(player.health.getLife() - 5);
            player2.ataqueAtual.setColide(false);
        }
    }

    @Override
    public void desenhar() {
        limparTela(Color.WHITE);
        retangulo(0, 0, larguraTela(), alturaTela(), Estilo.PREENCHIDO);
        player.desenhar();
        player2.desenhar();
    }

    @Override
    public void teclaPressionada(KeyEvent e) {
        player.teclaPressionada(e);
        player2.teclaPressionada(e);
    }

    @Override
    public void teclaLiberada(KeyEvent e) {
        player.teclaLiberada(e);
        player2.teclaLiberada(e);
    }

    @Override
    public void acaoAoSair() {
    }

    @Override
    public void teclaDigitada(KeyEvent e) {
    }

    @Override
    public void cliqueDoMouse(javafx.scene.input.MouseEvent e) {
    }

    @Override
    public void movimentoDoMouse(javafx.scene.input.MouseEvent e) {
    }

    @Override
    public void movimentoDoMousePressionado(javafx.scene.input.MouseEvent e) {
    }

    @Override
    public void mousePressionado(javafx.scene.input.MouseEvent e) {
    }
}
