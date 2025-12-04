package sla.horror;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import sla.api.FX_CG_2D_API;

public class Main extends FX_CG_2D_API {

    Player p;
    LabirintoPrim l;
    List<Wall> walls;
    List<Monster> monsters;

    private boolean noMenu = true; // se estamos no menu ou no jogo
    private Image menuFundo = new Image(getClass().getResource("/imagens/horror/capa.png").toExternalForm());
    private int opcaoSelecionada = 0; // 0 = Jogar, 1 = Créditos, 2 = Sair
    private String[] opcoesMenu = { "Jogar", "Créditos", "Sair" };

    double mouseTelaX = 0;
    double mouseTelaY = 0;

    private int round = 1;
    private int zumbisPorRound = 3;
    private int spawnMargin = 100;

    Image chao = new Image(getClass().getResource("/imagens/horror/tijolo.png").toExternalForm());
    Font frightmare = Font.loadFont(getClass().getResource("/font/Frightmare.ttf").toExternalForm(), 24);

    Random r;

    private final double TAMANHO_CELULA = 500;

    public Main(Stage stage) {
        super("SLA", stage, 60, 1280, 720);
    }

    public static void main(String[] args) {
        App.iniciar(cena -> new Main(cena), args);
    }

    @Override
    public void acaoAoIniciar() {
        int cols = (int) (2600 / TAMANHO_CELULA);
        int rows = (int) (2100 / TAMANHO_CELULA);
        l = new LabirintoPrim(cols, rows);

        walls = new ArrayList();
        monsters = new ArrayList();

        r = new Random();
        desenharLabirinto(l);

        p = new Player(r.nextInt(800), r.nextInt(600), this);
        boolean colidiu;
        do {
            colidiu = false;
            for (Wall w : walls) {
                if (colisao(p.getBounds(), w.getBounds())) {
                    p.setX(r.nextInt(800));
                    p.setY(r.nextInt(600));
                    colidiu = true;
                    break;
                }
            }
        } while (colidiu);

        iniciarTimer("rounds", 10, true, () -> {
            round++;
            int quantidadeZumbis = round * zumbisPorRound;
            for (int i = 0; i < quantidadeZumbis; i++) {
                Monster m = spawnZumbiFora();
                monsters.add(m);
            }
            System.out.println("Round " + round + " iniciado! Zumbis: " + quantidadeZumbis);
        });
    }

    @Override
    public void atualizar() {
        p.atualizar(walls);
        walls.forEach(Wall::atualizar);
        monsters.forEach(m -> m.atualizar(p));

    }

    @Override
    public void desenhar() {

        if (noMenu) {
            limparTela(Color.BLACK);
            imagem(menuFundo, 0, 0);

            for (int i = 0; i < opcoesMenu.length; i++) {
                if (i == opcaoSelecionada) {
                    preenchimento(Color.YELLOW);
                } else {
                    preenchimento(Color.WHITE);
                }
                texto(opcoesMenu[i], 200, 500 + i * 100, 100);
            }
            return; // interrompe o desenho do jogo enquanto estiver no menu
        }

        double camX = p.getX();
        double camY = p.getY();

        limparTela(Color.WHITE);

        empilhar();
        transladar(larguraTela() / 2 - camX, alturaTela() / 2 - camY);

        for (int i = 0; i < 2000; i += 32) {
            for (int j = 0; j < 2000; j += 32) {
                imagem(chao, i, j);
            }
        }

        walls.forEach(Wall::desenhar); // paredes por cima

        monsters.forEach(Monster::desenhar);
        p.atualizarMouse(mouseTelaX, mouseTelaY, camX, camY);
        texto("Round: " + round, camX, camY, 10);
        p.desenhar();
        monsters.forEach(Monster::desenharOlhos);
        preenchimento(Color.WHITE);

        desempilhar();
    }

    @Override
    public void acaoAoSair() {
    }

    @Override
    public void teclaPressionada(KeyEvent e) {
        if (noMenu) {
            switch (e.getCode()) {
                case UP -> opcaoSelecionada = (opcaoSelecionada + opcoesMenu.length - 1) % opcoesMenu.length;
                case DOWN -> opcaoSelecionada = (opcaoSelecionada + 1) % opcoesMenu.length;
                case ENTER -> {
                    switch (opcaoSelecionada) {
                        case 0 -> startGame();
                        case 1 -> mostrarCreditos();
                        case 2 -> sairJogo();
                    }
                }
            }
            return;
        }

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
        p.atirar(e);
    }

    @Override
    public void movimentoDoMouse(MouseEvent e) {
        mouseTelaX = e.getX();
        mouseTelaY = e.getY();
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
                double px = x * TAMANHO_CELULA;
                double py = y * TAMANHO_CELULA;
                char c = grid[y][x];

                if (c == '#') {
                    Wall w = new Wall((int) px, (int) py, (int) TAMANHO_CELULA, this);
                    walls.add(w);
                }
            }
        }
    }

    private Monster spawnZumbiFora() {
        int spawnX = 0, spawnY = 0;
        int larguraMapa = l.getLabirinto()[0].length * (int) TAMANHO_CELULA;
        int alturaMapa = l.getLabirinto().length * (int) TAMANHO_CELULA;

        switch (r.nextInt(4)) {
            case 0: // esquerda
                spawnX = -spawnMargin;
                spawnY = r.nextInt(alturaMapa);
                break;
            case 1: // direita
                spawnX = larguraMapa + spawnMargin;
                spawnY = r.nextInt(alturaMapa);
                break;
            case 2: // topo
                spawnX = r.nextInt(larguraMapa);
                spawnY = -spawnMargin;
                break;
            case 3: // fundo
                spawnX = r.nextInt(larguraMapa);
                spawnY = alturaMapa + spawnMargin;
                break;
        }
        return new Monster(spawnX, spawnY, 30, 30, 1, this);
    }

    private void startGame() {
        noMenu = false;
        acaoAoIniciar(); // inicializa o jogo
    }

    private void mostrarCreditos() {
        System.out.println("Créditos do jogo: Desenvolvido por você!");
        // Aqui você pode desenhar na tela de forma mais elaborada se quiser
    }

    private void sairJogo() {
        System.exit(0);
    }

}
