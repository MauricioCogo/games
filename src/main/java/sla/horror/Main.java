package sla.horror;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import sla.api.FX_CG_2D_API;
import sla.horror.util.Bullet;
import sla.horror.util.LabirintoPrim;
import sla.horror.util.Wall;

public class Main extends FX_CG_2D_API {

    Player p;
    LabirintoPrim l;
    HUD hud;
    Random r;

    List<Wall> walls;
    List<Monster> monsters;
    List<Bullet> bs;
    List<Suprimento> suprimentos;

    private boolean noMenu = true;
    private boolean aguardandoRound = false;

    private final String[] opcoesMenu = { "Jogar", "Créditos", "Sair" };

    double mouseTelaX = 0;
    double mouseTelaY = 0;
    private final double TAMANHO_CELULA = 200;

    private int opcaoSelecionada = 0; // 0 = Jogar, 1 = Créditos, 2 = Sair
    private int round = 1;
    private int zumbisPorRound = 3;
    private int spawnMargin = 100;

    private final Image menuFundo = new Image(getClass().getResource("/imagens/horror/capa.png").toExternalForm());
    private final Image chao = new Image(getClass().getResource("/imagens/horror/tijolo.png").toExternalForm());
    private final Image zvermelho = new Image(
            getClass().getResource("/imagens/horror/olhosvermelhos.png").toExternalForm());
    private final Image zazul = new Image(getClass().getResource("/imagens/horror/olhosazuis.png").toExternalForm());
    private final Image zstealth = new Image(
            getClass().getResource("/imagens/horror/olhosstealth.png").toExternalForm());
    private final Image zamarelo = new Image(
            getClass().getResource("/imagens/horror/olhosamarelos.png").toExternalForm());

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

        desenharChao(l);
        desenharLabirinto(l);

        p = new Player(r.nextInt(800), r.nextInt(600), this);
        hud = new HUD(0, 0, 100, this);

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

        iniciarRound();
    }

    @Override
    public void atualizar() {

        if (!noMenu) {
            bs = p.getArmaAtual().getBullets();
            p.atualizar(walls);
            monsters.forEach(m -> m.atualizar(p, monsters));
            suprimentos.removeIf(Suprimento::isColetado);
            monsters.removeIf(Monster::isDie);
            bs.removeIf(Bullet::getDie);

            // verifica se o player tomou dano
            for (Monster m : monsters) {
                if (colisao(m.getBounds(), p.getBounds())) {
                    p.tomarDano();
                }
            }

            // verifica se o monstro tomou dano
            for (Bullet b : bs) {
                for (Monster m : monsters) {
                    if (colisao(m.getBounds(), b.getBounds())) {
                        if (Math.random() < 0.30) {
                            Suprimento sup = spawnRecurso(m.getX(), m.getY());
                            suprimentos.add(sup);
                        }
                        m.setLife(m.getLife() - 1);
                        b.setRemain(b.getRemain() - 1);
                    }
                }
            }

            // verifica se a bala bateu na parede
            for (Bullet bullet : bs) {
                for (Wall w : walls) {
                    if (colisao(bullet.getBounds(), w.getBounds())) {
                        bullet.setDie(true);
                    }
                }
            }

            // verifica se o suprimento foi coletado
            for (Suprimento s : suprimentos) {
                if (!s.isColetado() && colisao(s.getBounds(), p.getBounds())) {

                    if (s.getType() == TipoSuprimento.RECURSOS) {
                        if (p.getTam() >= 600) {
                            p.setTam(p.getTam() - 100);
                        }
                        if (p.getArmaAtual().getAmmoMagazine() <= p.getArmaAtual().getAmmoNumberTotal()) {
                            p.getArmaAtual().setAmmoMagazine(p.getArmaAtual().getAmmoNumberTotal());
                        }
                    } else {
                        if (p.getLife() <= 5) {
                            p.setLife(p.getLife() + 1);
                        }
                    }

                    s.coletar();
                }
            }

            // verifica se existe monstro
            if (monsters.isEmpty() && !aguardandoRound) {
                aguardandoRound = true;

                iniciarTimer("proximoRound", 2.0, false, () -> {
                    round++;
                    iniciarRound();
                });
            }

            // verifica se o player morreu
            if (p.getLife() <= 0) {
                round = 0;
                noMenu = true;
                monsters.clear();
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

        limparTela(Color.WHITE);

        double camX = p.getX();
        double camY = p.getY();

        empilhar();

        transladar(larguraTela() / 2 - camX, alturaTela() / 2 - camY);

        walls.forEach(Wall::desenhar);

        monsters.forEach(Monster::desenhar);
        p.atualizarMouse(mouseTelaX, mouseTelaY, camX, camY);
        suprimentos.forEach(s -> s.desenhar(this));
        p.desenhar();
        monsters.forEach(Monster::desenharOlhos);

        preenchimento(Color.DARKGRAY.darker());

        hud.setRound(round);
        hud.setSupp(suprimentos.size());
        hud.setAmmo(p.getArmaAtual().getAmmoMagazine());
        hud.setTotalAmmo(p.getArmaAtual().getAmmoNumberTotal());
        hud.setMonsters(monsters.size());
        hud.setArmaAtual(p.getArmaAtual().getNome());
        hud.setX(camX - 600);
        hud.setY(camY - 300);
        hud.miniMapa(l.getLabirinto());
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
                default -> throw new IllegalArgumentException("Unexpected value: " + e.getCode());
            }
            return;
        }

        p.teclaPressionada(e);
    }

    // #region
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

    // #endregion

    private void desenharChao(LabirintoPrim lab) {
        char[][] grid = lab.getLabirinto();

        double tileW = chao.getWidth();
        double tileH = chao.getHeight();

        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {

                if (grid[y][x] == ' ') {

                    double px = x * TAMANHO_CELULA;
                    double py = y * TAMANHO_CELULA;

                    // Preenche a célula inteira repetindo o tile
                    for (double yy = 0; yy < TAMANHO_CELULA; yy += tileH) {
                        for (double xx = 0; xx < TAMANHO_CELULA; xx += tileW) {
                            imagem(chao, px + xx, py + yy);
                        }
                    }
                }
            }
        }
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
            case FRACO -> {
                vida = 2;
                velocidade = 1.5;
                cor = zvermelho;
                size = 30;
            }

            case RAPIDO -> {
                vida = 1;
                velocidade = 3.5;
                cor = zamarelo;
                size = 20;
            }

            case TANQUE -> {
                vida = 5 + round;
                velocidade = 1.2;
                size = 60;
                cor = zazul;
            }

            case STEALTH -> {
                vida = 2;
                velocidade = 1.5;
                cor = zstealth;
            }
        }

        return new Monster(spawnX, spawnY, size, size, velocidade, this, cor, vida);
    }

    private TipoZumbi escolherTipoZumbi() {
        int chance = r.nextInt(100);

        if (round >= 8 && chance < 20) {
            return TipoZumbi.TANQUE;
        }

        if (round >= 4 && chance < 40) {
            return TipoZumbi.RAPIDO;
        }

        if (round >= 5 && chance < 70) {
            return TipoZumbi.STEALTH;
        }

        return TipoZumbi.FRACO;
    }

    public Suprimento spawnRecurso(int x, int y) {
        int chance = r.nextInt(100);

        Suprimento sup = new Suprimento(x, y);
        if (chance > 50) {
            sup.setType(TipoSuprimento.VIDA);
        } else {
            sup.setType(TipoSuprimento.RECURSOS);
        }

        return sup;

    }

    public enum TipoZumbi {
        FRACO, RAPIDO, TANQUE, STEALTH;
    }

    public enum TipoSuprimento {
        VIDA, RECURSOS;
    }
}
