package sla.horror;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class LabirintoPrim {
    private int largura;
    private int altura;
    private final char PAREDE = '#';
    private final char CAMINHO = ' ';

    private char[][] grid;
    private boolean[][] visited;
    private Random random = new Random();

    public List<Fronteira> passos = new ArrayList<>();

    public class Fronteira {
        public int paredeX, paredeY;
        public int celulaX, celulaY;

        Fronteira(int px, int py, int cx, int cy) {
            paredeX = px;
            paredeY = py;
            celulaX = cx;
            celulaY = cy;
        }
    }

    public LabirintoPrim(int largura, int altura) {
        this.largura = largura;
        this.altura = altura;
        gerarLabirinto();
        imprimirComColchetes();
    }

    private void gerarLabirinto() {

        grid = new char[this.altura][this.largura];

        for (int y = 0; y < this.altura; y++) {
            Arrays.fill(grid[y], PAREDE);
        }

        int celulasHorizontal = this.largura / 2;
        int celulasVertical = this.altura / 2;
        visited = new boolean[this.altura][this.largura];

        int startcelulaX = random.nextInt(celulasHorizontal) * 2 + 1;
        int startcelulaY = random.nextInt(celulasVertical) * 2 + 1;

        grid[startcelulaY][startcelulaX] = CAMINHO;
        visited[startcelulaY][startcelulaX] = true;

        List<Fronteira> fronteiras = new ArrayList<>();
        adicionarFronteiras(startcelulaX, startcelulaY, fronteiras);

        while (!fronteiras.isEmpty()) {
            int idx = random.nextInt(fronteiras.size());
            Fronteira f = fronteiras.remove(idx);

            if (!visited[f.celulaY][f.celulaX]) {
                grid[f.paredeY][f.paredeX] = CAMINHO;
                grid[f.celulaY][f.celulaX] = CAMINHO;
                visited[f.celulaY][f.celulaX] = true;
                passos.add(f);
                adicionarFronteiras(f.celulaX, f.celulaY, fronteiras);
            }
        }
    }

    private void adicionarFronteiras(int celulaX, int celulaY, List<Fronteira> fronteiras) {
        if (celulaY - 2 > 0 && grid[celulaY - 2][celulaX] == PAREDE) {
            fronteiras.add(new Fronteira(celulaX, celulaY - 1, celulaX, celulaY - 2));
        }
        if (celulaY + 2 < this.altura - 1 && grid[celulaY + 2][celulaX] == PAREDE) {
            fronteiras.add(new Fronteira(celulaX, celulaY + 1, celulaX, celulaY + 2));
        }
        if (celulaX - 2 > 0 && grid[celulaY][celulaX - 2] == PAREDE) {
            fronteiras.add(new Fronteira(celulaX - 1, celulaY, celulaX - 2, celulaY));
        }
        if (celulaX + 2 < this.largura - 1 && grid[celulaY][celulaX + 2] == PAREDE) {
            fronteiras.add(new Fronteira(celulaX + 1, celulaY, celulaX + 2, celulaY));
        }
    }

    private void imprimirComColchetes() {
        for (int y = 0; y < this.altura; y++) {
            StringBuilder sb = new StringBuilder();
            for (int x = 0; x < this.largura; x++) {
                sb.append('[').append(grid[y][x]).append(']');
            }
        }
    }

    public char[][] getLabirinto() {
        return this.grid;
    }
}
