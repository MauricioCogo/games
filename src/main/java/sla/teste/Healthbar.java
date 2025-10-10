package sla.teste;

import javafx.scene.paint.Color;
import lombok.Data;
import sla.api.FX_CG_2D_API;
import sla.api.FX_CG_2D_API.Estilo;

@Data
public class Healthbar {
    private boolean side;
    private String nome;
    private int life;
    private int lifeMax;
    private int strong;
    private int margin;

    private FX_CG_2D_API api;

    public Healthbar(boolean side, String nome, int life, int strong, FX_CG_2D_API api) {
        this.side = side;
        this.nome = nome;
        this.life = life;
        this.lifeMax = life;
        this.strong = strong;
        this.api = api;
        this.margin = api.larguraTela();

        timer();

    }

    public void timer() {
        api.iniciarTimer("strong", 0.5, true, () -> {
            if (strong > 0 && strong < 100)
                strong -= 3;
        });
    }

    public void atualizar() {

    }

    public void desenhar() {
        if (side) {
            api.preenchimento(Color.GRAY);
            api.retangulo(0, 0, 250, 50, Estilo.PREENCHIDO);

            api.texto(nome, 20, 20, 15);

            api.preenchimento(Color.RED);
            api.retangulo(20, 30, lifeMax, 20, Estilo.PREENCHIDO);

            for (int i = life; i > 0; i--) {
                api.preenchimento(Color.GREEN);
                api.retangulo(20, 30, i, 20, Estilo.PREENCHIDO);
            }

            for (int i = 0; i < strong; i++) {
                api.preenchimento(Color.YELLOW);
                api.retangulo(20, 40, i, 20, Estilo.PREENCHIDO);
            }

        } else {
            api.preenchimento(Color.GRAY);
            api.retangulo(api.larguraTela() - 250, 0, 250, 50, Estilo.PREENCHIDO);

            api.texto(nome, margin - 230, 20, 15);

            api.preenchimento(Color.RED);
            api.retangulo(margin - 230, 30, lifeMax, 20, Estilo.PREENCHIDO);

            for (int i = life; i > 0; i--) {
                api.preenchimento(Color.GREEN);
                api.retangulo(margin - 230, 30, i, 20, Estilo.PREENCHIDO);
            }

        }
    }

}
