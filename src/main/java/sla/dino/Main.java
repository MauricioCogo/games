package sla.dino;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import sla.api.FX_CG_2D_API;

public class Main extends FX_CG_2D_API {

    private Dino dino;
    private List<Obstaculo> ob;
    private double vel = -4;
    private Random r;
    private double segs;

    public Main(Stage stage) {
        super("Dino", stage, 60, 640, 480);
    }

    public static void main(String[] args) {
        App.iniciar(Main::new, args);
    }

    @Override
    public void acaoAoIniciar() {
        
        ob = new ArrayList<>();
        dino = new Dino(this);
        r = new Random();

        segs = r.nextInt(3);
        iniciarTimer("obs", segs, false, this::gerarObstaculo);
    }

    @Override
    public void atualizar() {
        if(vel>-10){
            vel-=0.001;
            System.out.println(vel);
        }

        dino.atualizar();
        

        for (Obstaculo o : ob) {
            o.atualizar();
            o.setVel(vel);
        }

        ob.removeIf(o -> o.getX() + o.getWidth() < 0);

        for (Obstaculo o : ob) {
            if (colisao(dino.getBounds(), o.getBounds())) {
                System.out.println("GAME OVER!");
            }
        }
    }

    @Override
    public void desenhar() {
        limparTela(Color.WHITE);
        dino.desenhar();
        for (Obstaculo o : ob) {
            o.desenhar();
        }
    }

    @Override
    public void acaoAoSair() {
    }

    @Override
    public void teclaPressionada(KeyEvent e) {
        dino.teclaPressionada(e);
    }

    @Override
    public void teclaLiberada(KeyEvent e) {
        dino.teclaLiberada(e);
    }

    //#region Eventos não usados
    @Override
    public void teclaDigitada(KeyEvent e) { }

    @Override
    public void cliqueDoMouse(MouseEvent e) { }

    @Override
    public void movimentoDoMouse(MouseEvent e) { }

    @Override
    public void movimentoDoMousePressionado(MouseEvent e) { }

    @Override
    public void mousePressionado(MouseEvent e) { }
    //#endregion

    // -----------------------------------------------------------------

    private void gerarObstaculo() {
        Obstaculo o = new Obstaculo(this);
        ob.add(o);
        segs = 1 + r.nextInt(3); // novo intervalo
        iniciarTimer("obs", segs, false, this::gerarObstaculo);
    }
}
