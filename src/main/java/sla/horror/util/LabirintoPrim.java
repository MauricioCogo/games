package sla.horror.util;

import java.util.*;

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

        for (int x = 0; x < largura; x++) {
            grid[0][x] = PAREDE;
            grid[altura - 1][x] = PAREDE;
        }

        for (int y = 0; y < altura; y++) {
            grid[y][0] = PAREDE;
            grid[y][largura - 1] = PAREDE;
        }
    }

    private void adicionarFronteiras(int celulaX, int celulaY, List<Fronteira> fronteiras) {
        if (celulaY - 2 >= 1 && !visited[celulaY - 2][celulaX]) {
            fronteiras.add(new Fronteira(celulaX, celulaY - 1, celulaX, celulaY - 2));
        }
        if (celulaY + 2 < altura - 1 && !visited[celulaY + 2][celulaX]) {
            fronteiras.add(new Fronteira(celulaX, celulaY + 1, celulaX, celulaY + 2));
        }
        if (celulaX - 2 >= 1 && !visited[celulaY][celulaX - 2]) {
            fronteiras.add(new Fronteira(celulaX - 1, celulaY, celulaX - 2, celulaY));
        }
        if (celulaX + 2 < largura - 1 && !visited[celulaY][celulaX + 2]) {
            fronteiras.add(new Fronteira(celulaX + 1, celulaY, celulaX + 2, celulaY));
        }
    }

    private void imprimirComColchetes() {
        for (int y = 0; y < this.altura; y++) {
            StringBuilder sb = new StringBuilder();
            for (int x = 0; x < this.largura; x++) {
                sb.append('[').append(grid[y][x]).append(']');
            }
            System.out.println(sb.toString());
        }
    }

    public char[][] getLabirinto() {
        return this.grid;
    }
}
