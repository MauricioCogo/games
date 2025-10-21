package sla.horror;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import sla.api.FX_CG_2D_API;

public class Main extends FX_CG_2D_API {

    Player p;
    LabirintoPrim l;
    List<Wall> walls;
    List<Particle> ps;

    private final double TAMANHO_CELULA = 200;

    public Main(Stage stage) {
        super("SLA", stage, 60, 1250, 720);
    }

    public static void main(String[] args) {
        App.iniciar(cena -> new Main(cena), args);
    }

    @Override
    public void acaoAoIniciar() {
        int cols = (int) (3750 / TAMANHO_CELULA);
        int rows = (int) (2160 / TAMANHO_CELULA);
        l = new LabirintoPrim(cols, rows);

        walls = new ArrayList();
        ps = new ArrayList();
        desenharLabirinto(l);

        p = new Player(larguraTela() / 2, alturaTela() / 2, this);
    }

    @Override
    public void atualizar() {
        p.atualizar();
        ps = p.getPs();
        ps.forEach(particle -> particle.atualizar(walls));
        walls.forEach(Wall::atualizar);

    }

    @Override
    public void desenhar() {
        double camX = p.getX();
        double camY = p.getY();

        limparTela(Color.BLACK);

        empilhar();
        transladar(larguraTela() / 2 - camX, alturaTela() / 2 - camY);

        walls.forEach(Wall::desenhar);
        ps.forEach(Particle::desenhar);
        p.desenhar();

        desempilhar();
    }

    @Override
    public void acaoAoSair() {
    }

    @Override
    public void teclaPressionada(KeyEvent e) {
        p.teclaPressionada(e);
    }

    @Override
    public void teclaLiberada(KeyEvent e) {
        p.teclaLiberada(e);
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

    public void desenharLabirinto(LabirintoPrim lab) {
        char[][] grid = lab.getLabirinto();

        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                char c = grid[y][x];
                double px = x * TAMANHO_CELULA;
                double py = y * TAMANHO_CELULA;

                if (c == '#') {
                    Wall w = new Wall((int) px, (int) py, (int) TAMANHO_CELULA, this);
                    walls.add(w);
                }
            }
        }
    }
}
