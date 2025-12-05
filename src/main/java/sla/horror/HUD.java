package sla.horror;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import lombok.Data;
import sla.api.FX_CG_2D_API;
import sla.api.FX_CG_2D_API.Estilo;

@Data
public class HUD {
    private double x;
    private double y;

    private int gap;

    private FX_CG_2D_API api;

    private int round;
    private int ammo;
    private int totalAmmo;
    private int supp;

    private int monsters;

    private String armaAtual;

    private Image coracao = new Image(getClass().getResource("/imagens/horror/coracao.png").toExternalForm());
    private Image pistola = new Image(getClass().getResource("/imagens/horror/pistola.png").toExternalForm());
    private Image escopeta = new Image(getClass().getResource("/imagens/horror/escopeta.png").toExternalForm());
    private Image rifle = new Image(getClass().getResource("/imagens/horror/rifle.png").toExternalForm());

    public HUD(int x, int y, int gap, FX_CG_2D_API api) {
        this.x = x;
        this.y = y;
        this.gap = gap;
        this.api = api;
    }

    public void desenhar() {
        api.texto("Round " + round, x, y, 40);
        api.texto("Quantidade de zumbis: " + monsters, x, y + gap, 40);
        api.texto(ammo + "/" + totalAmmo, x + 600, y, 40);

        switch (armaAtual) {
            case "pistola" -> {
                api.preenchimento(Color.YELLOW);
                api.retangulo(x + 490, y + 540, 80, 80, Estilo.PREENCHIDO);
            }
            case "escopeta" -> {
                api.preenchimento(Color.YELLOW);
                api.retangulo(x + 590, y + 540, 80, 80, Estilo.PREENCHIDO);
            }
            case "rifle" -> {
                api.preenchimento(Color.YELLOW);
                api.retangulo(x + 690, y + 540, 80, 80, Estilo.PREENCHIDO);
            }
            default -> throw new AssertionError();
        }

        api.preenchimento(Color.GRAY);
        api.retangulo(x + 500, y + 550, 60, 60, Estilo.PREENCHIDO);
        api.imagem(pistola, x + 515, y + 575);
        api.retangulo(x + 600, y + 550, 60, 60, Estilo.PREENCHIDO);
        api.imagem(escopeta, x + 600, y + 575);
        api.retangulo(x + 700, y + 550, 60, 60, Estilo.PREENCHIDO);
        api.imagem(rifle, x + 700, y + 575);

        if (supp == 1) {
            api.texto("Existe " + supp + " suprimento disponivel", x, y + (gap * 2), 30);
        } else if (supp > 1) {
            api.texto("Existem " + supp + " suprimentos disponiveis", x, y + (gap * 2), 30);
        }
    }

    public void miniMapa(char[][] grid) {

        double cellSize = 10;

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {

                char c = grid[i][j];

                if (c == '#') {
                    api.preenchimento(Color.GRAY);

                    double px = x + j * cellSize + 50;
                    double py = y + i * cellSize + 500;

                    api.retangulo(px, py, cellSize, cellSize, Estilo.PREENCHIDO);
                }
            }
        }
    }
}
