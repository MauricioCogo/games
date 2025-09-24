package sla.rts;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import sla.api.FX_CG_2D_API;
import sla.rts.recursos.Bosque;
import sla.rts.recursos.MinaFerro;
import sla.rts.recursos.Rebanho;

public class Main extends FX_CG_2D_API {
    private Random random;

    List<Reino> reinos;
    int qntReinos = 2;

    private List<MinaFerro> minas;
    private List<Rebanho> rebanhos;
    private List<Bosque> bosques;
    Reino r1;

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
        random = new Random();

        // Inicializa listas
        minas = new ArrayList<>();
        rebanhos = new ArrayList<>();
        bosques = new ArrayList<>();
        reinos = new ArrayList<>();

        // Cria recursos aleatórios dentro da tela
        for (int i = 0; i < 5; i++) {
            int x = random.nextInt(larguraTela() - 30);
            int y = random.nextInt(alturaTela() - 30);
            rebanhos.add(new Rebanho(x, y, this, random.nextInt(200) + 50));
        }

        for (int i = 0; i < 5; i++) {
            int x = random.nextInt(larguraTela() - 30);
            int y = random.nextInt(alturaTela() - 30);
            bosques.add(new Bosque(x, y, this, random.nextInt(200) + 50));
        }

        for (int i = 0; i < 5; i++) {
            int x = random.nextInt(larguraTela() - 30);
            int y = random.nextInt(alturaTela() - 30);
            minas.add(new MinaFerro(x, y, this, random.nextInt(200) + 50));
        }

        // Cria reinos
        r1 = new Reino(1000, 200, "Barbaros", Color.PURPLE, this);
        Reino r2 = new Reino(200, 300, "Elfos", Color.GREEN, this);
        reinos.add(r1);
        reinos.add(r2);

        // Inicializa tropas e recursos de cada reino
        r1.aoIniciar();
        r2.aoIniciar();
    }

    @Override
    public void atualizar() {
        reinos.forEach(Reino::atualizar);

        minas.forEach(MinaFerro::atualizar);
        rebanhos.forEach(Rebanho::atualizar);
        bosques.forEach(Bosque::atualizar);
    }

    @Override
    public void desenhar() {
        empilhar();
        limparTela(Color.WHITE);

        reinos.forEach(Reino::desenhar);

        minas.forEach(MinaFerro::desenhar);
        rebanhos.forEach(Rebanho::desenhar);
        bosques.forEach(Bosque::desenhar);

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
        if (e.getCode() == KeyCode.SPACE) {
            r1.cria_aldeao();
            
        }

    }

    @Override
    public void teclaLiberada(KeyEvent e) {

    }

    @Override
    public void teclaDigitada(KeyEvent e) {

    }

    private <T> void criar(int quantidade, List<T> lista, Supplier<T> factory) {
        for (int i = 0; i < quantidade; i++) {
            lista.add(factory.get());
        }
    }

}
