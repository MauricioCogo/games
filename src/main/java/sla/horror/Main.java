package sla.horror;

import java.net.URL;
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
import sla.horror.util.Bullet;
import sla.horror.util.LabirintoPrim;
import sla.horror.util.Wall;

public class Main extends FX_CG_2D_API {

    Player p;
    LabirintoPrim l;
    List<Wall> walls;
    List<Monster> monsters;
    List<Bullet> bs;
    List<Suprimento> suprimentos;
    HUD hud;

    private boolean noMenu = true; // se estamos no menu ou no jogo
    private Image menuFundo = new Image(getClass().getResource("/imagens/horror/capa.png").toExternalForm());
    private int opcaoSelecionada = 0; // 0 = Jogar, 1 = Créditos, 2 = Sair
    private String[] opcoesMenu = { "Jogar", "Créditos", "Sair" };

    double mouseTelaX = 0;
    double mouseTelaY = 0;

    private int round = 1;
    private int zumbisPorRound = 3;
    private boolean aguardandoRound = false;

    private int spawnMargin = 100;

    Image chao = new Image(getClass().getResource("/imagens/horror/tijolo.png").toExternalForm());
    Image zvermelho = new Image(getClass().getResource("/imagens/horror/olhosvermelhos.png").toExternalForm());
    Image zazul = new Image(getClass().getResource("/imagens/horror/olhosazuis.png").toExternalForm());
    Image zstealth = new Image(getClass().getResource("/imagens/horror/olhosstealth.png").toExternalForm());
    Image zamarelo = new Image(getClass().getResource("/imagens/horror/olhosamarelos.png").toExternalForm());
    Font frightmare = Font.loadFont(getClass().getResource("/font/Frightmare.ttf").toExternalForm(), 24);

    Random r;

    private final double TAMANHO_CELULA = 500;

    public Main(Stage stage) {
        super("SLA", stage, 60, 1280, 720);

        URL somMusica = Main.class.getResource("/sounds/musica.mp3");
        EfeitosSonoros.carregarSom("musica", somMusica);

        EfeitosSonoros.tocarSom("musica", false, true);
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
        bs = new ArrayList();
        suprimentos = new ArrayList<>();

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

        hud = new HUD(0, 0, 100, this);

        iniciarRound();

    }

    @Override
    public void atualizar() {
        System.out.println("Mouse x: " + mouseTelaX + " | Mouse y: " + mouseTelaY);

        if (!noMenu) {
            bs = p.getArmaAtual().getBullets();
            p.atualizar(walls);
            walls.forEach(Wall::atualizar);
            monsters.forEach(m -> m.atualizar(p));
            suprimentos.removeIf(Suprimento::isColetado);

            monsters.removeIf(Monster::isDie);
            bs.removeIf(Bullet::getDie);

            if (p.getLife() <= 0) {
                noMenu = true;
            }

            for (Monster m : monsters) {
                if (colisao(m.getBounds(), p.getBounds())) {
                    p.tomarDano();
                }
            }

            for (Bullet b : bs) {
                for (Monster m : monsters) {
                    if (colisao(m.getBounds(), b.getBounds())) {
                        if (Math.random() < 0.30) {
                            Suprimento sup = new Suprimento(m.getX(), m.getY());
                            suprimentos.add(sup);
                        }
                        m.setLife(m.getLife() - 1);
                        b.setRemain(b.getRemain() - 1);
                    }
                }
            }

            for (Bullet bullet : bs) {
                for (Wall w : walls) {
                    if (colisao(bullet.getBounds(), w.getBounds())) {
                        bullet.setDie(true);
                    }
                }
            }

            for (Suprimento s : suprimentos) {
                if (!s.isColetado() && colisao(s.getBounds(), p.getBounds())) {
                    if (p.getLife() <= 5) {
                        p.setLife(p.getLife() + 1);
                    }
                    if (p.getTam() >= 600) {
                        p.setTam(p.getTam() - 100);
                    }
                    if (p.getArmaAtual().getAmmoMagazine() <= p.getArmaAtual().getAmmoNumberTotal()) {
                        p.getArmaAtual().setAmmoMagazine(p.getArmaAtual().getAmmoNumberTotal());
                    }
                    s.coletar();
                }
            }

            if (monsters.isEmpty() && !aguardandoRound) {
                aguardandoRound = true;

                iniciarTimer("proximoRound", 2.0, false, () -> {
                    round++;
                    iniciarRound();
                });
            }

        }
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
            return;
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

        walls.forEach(Wall::desenhar);

        monsters.forEach(Monster::desenhar);
        p.atualizarMouse(mouseTelaX, mouseTelaY, camX, camY);
        suprimentos.forEach(s -> s.desenhar(this));
        p.desenhar();

        monsters.forEach(Monster::desenharOlhos);
        preenchimento(Color.WHITE);

        hud.setRound(round);
        hud.setSupp(suprimentos.size());
        hud.setAmmo(p.getArmaAtual().getAmmoMagazine());
        hud.setTotalAmmo(p.getArmaAtual().getAmmoNumberTotal());
        hud.setMonsters(monsters.size());
        hud.setArmaAtual(p.getArmaAtual().getNome());
        hud.setX(camX - 600);
        hud.setY(camY - 300);
        hud.desenhar();
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
            case 0 -> {
                spawnX = -spawnMargin;
                spawnY = r.nextInt(alturaMapa);
            }
            case 1 -> {
                spawnX = larguraMapa + spawnMargin;
                spawnY = r.nextInt(alturaMapa);
            }
            case 2 -> {
                spawnX = r.nextInt(larguraMapa);
                spawnY = -spawnMargin;
            }
            case 3 -> {
                spawnX = r.nextInt(larguraMapa);
                spawnY = alturaMapa + spawnMargin;
            }
        }

        TipoZumbi tipo = escolherTipoZumbi();

        int vida = 1;
        double velocidade = 1.5;
        int size = 30;
        Image cor = zvermelho;

        switch (tipo) {
            case FRACO:
                vida = 2;
                velocidade = 1.5;
                cor = zvermelho;
                size = 30;
                break;

            case RAPIDO:
                vida = 1;
                velocidade = 3.5;
                cor = zamarelo;
                size = 20;
                break;

            case TANQUE:
                vida = 5 + round;
                velocidade = 1.2;
                size = 60;
                cor = zazul;
                break;

            case STEALTH:
                vida = 2;
                velocidade = 1.5;
                cor = zstealth;
                break;
        }

        return new Monster(spawnX, spawnY, size, size, velocidade, this, cor, vida);
    }

    private void iniciarRound() {
        aguardandoRound = false;

        int quantidadeZumbis = round * zumbisPorRound;

        for (int i = 0; i < quantidadeZumbis; i++) {
            monsters.add(spawnZumbiFora());
        }
    }

    private void startGame() {
        noMenu = false;
        URL somUrl = Main.class.getResource("/sounds/start.mp3");
        EfeitosSonoros.carregarSom("start", somUrl);
        EfeitosSonoros.tocarSom("start", false, true);
        acaoAoIniciar();
    }

    private void mostrarCreditos() {
        System.out.println(
                "\n=====================================\n" +
                        "         🎮 C R É D I T O S 🎮        \n" +
                        "=====================================\n" +
                        "   Desenvolvido por:\n" +
                        "     • Carolini Bassan\n" +
                        "     • Djonathan Briesch\n" +
                        "     • Mauricio Cogo\n" +
                        "     • Rafael Tischler\n" +
                        "\n" +
                        "   Arte:\n" +
                        "     • Benhur Dona\n" +
                        "=====================================\n");
    }

    private void sairJogo() {
        System.exit(0);
    }

    public enum TipoZumbi {
        FRACO, RAPIDO, TANQUE, STEALTH;
    }

    private TipoZumbi escolherTipoZumbi() {
        int chance = r.nextInt(100);

        if (round >= 8 && chance < 20) {
            return TipoZumbi.TANQUE;
        }

        if (round >= 4 && chance < 40) {
            return TipoZumbi.RAPIDO;
        }

        if (round >= 5 && chance <30) {
            return TipoZumbi.STEALTH;
        }

        return TipoZumbi.FRACO;
    }
}
