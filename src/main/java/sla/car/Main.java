package sla.car;

import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import sla.api.FX_CG_2D_API;

public class Main extends FX_CG_2D_API{

    Carro c;

    public Main(Stage stage) {
        super("Carro teste", stage, 60, 640, 480);
    }


    public static void main(String[] args) {
        App.iniciar(cena -> new Main(cena), args);
    }

    @Override
    public void acaoAoSair() {
        
    }

    @Override
    public void acaoAoIniciar() {
        c = new Carro(this, 250, 250, 50, 90);
    }

    @Override
    public void teclaPressionada(KeyEvent e) {
        c.teclaPressionada(e);
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

    @Override
    public void atualizar() {
        c.desenhar(this);
    }

    @Override
    public void desenhar() {
        c.desenhar(this);
    }
    
}
