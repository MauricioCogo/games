package sla.horror.util;

import java.util.Arrays;

public class LabirintoPrim {
private int largura;
private int altura;
private final char PAREDE = '#';
private final char CAMINHO = ' ';

private char[][] grid;

public LabirintoPrim(int largura, int altura) {
    this.largura = largura;
    this.altura = altura;
    gerarContorno();
    imprimirComColchetes();
}

private void gerarContorno() {
    grid = new char[altura][largura];

    // Preenche tudo com caminho
    for (int y = 0; y < altura; y++) {
        Arrays.fill(grid[y], CAMINHO);
    }

    // Coloca paredes nas bordas
    for (int x = 0; x < largura; x++) {
        grid[0][x] = PAREDE;           // topo
        grid[altura - 1][x] = PAREDE;  // fundo
    }
    for (int y = 0; y < altura; y++) {
        grid[y][0] = PAREDE;           // esquerda
        grid[y][largura - 1] = PAREDE; // direita
    }
}

private void imprimirComColchetes() {
    for (int y = 0; y < altura; y++) {
        StringBuilder sb = new StringBuilder();
        for (int x = 0; x < largura; x++) {
            sb.append('[').append(grid[y][x]).append(']');
        }
        System.out.println(sb.toString());
    }
}

public char[][] getLabirinto() {
    return grid;
}
}
