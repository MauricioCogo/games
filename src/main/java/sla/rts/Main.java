package sla.rts;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import sla.api.FX_CG_2D_API;

public class Main extends FX_CG_2D_API {

    List<Reino> reinos;
    int qntReinos = 2;

    public Main(Stage stage) {
        super("RTS Maneiro", stage, 60, 1250, 720);
    }

    public static void main(String[] args) {
        App.iniciar(cena -> new Main(cena), args);
    }

    @Override
    public void acaoAoSair() {

    }

    @Override
    public void acaoAoIniciar() {
        reinos = new ArrayList<>();
        Reino r1 = new Reino(1000, 200, "Barbaros", Color.PURPLE, this);
        Reino r2 = new Reino(200, 300, "Elfos", Color.GREEN, this);
        reinos.add(r1);
        reinos.add(r2);

        r1.aoIniciar();
        r2.aoIniciar();


    }

    @Override
    public void atualizar() {

    }

    @Override
    public void desenhar() {
        empilhar();
        limparTela(Color.WHITE);

        for (Reino reino : reinos) {
            reino.desenhar();
        }
        
        desempilhar();
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

    @Override
    public void teclaPressionada(KeyEvent e) {

    }

    @Override
    public void teclaLiberada(KeyEvent e) {

    }

    @Override
    public void teclaDigitada(KeyEvent e) {

    }

}
