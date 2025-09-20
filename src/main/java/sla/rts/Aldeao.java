package sla.rts;

import javafx.geometry.Rectangle2D;
import javafx.scene.paint.Color;
import sla.api.FX_CG_2D_API;
import sla.api.FX_CG_2D_API.Estilo;

public class Aldeao extends Base{
    FX_CG_2D_API api;

    private int vida;
    private int capacidade;
    private Color cor;
    private int hspd, vpsd, spd = 3;

    private int x, y;
    private int width = 20, height = 20;

    private Reino reino;

    public Aldeao(FX_CG_2D_API api) {
        this.api = api;
    }

    public Aldeao(int x, int y, Reino reino, FX_CG_2D_API api,Color cor) {
        this.x = x;
        this.y = y;
        this.reino = reino;
        this.api = api;
        this.cor = cor;

    }

    @Override
    public void desenhar() {
        api.empilhar();

        api.preenchimento(cor);
        api.retangulo(x, y, width, height, Estilo.PREENCHIDO);


        api.desempilhar();

    }

    @Override
    public void atualizar() {

    }

    @Override
    public Rectangle2D getBounds() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getBounds'");
    }

}
