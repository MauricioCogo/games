package sla.rts;

import javafx.geometry.Rectangle2D;
import javafx.scene.paint.Color;
import sla.api.FX_CG_2D_API;
import sla.api.FX_CG_2D_API.Estilo;

public class Rebanho extends Base{
    private int quantidade;
    private Reino reino;
    private int x,y;

    private int width = 30, height = 30;

    Color cor;

    FX_CG_2D_API api;

    public Rebanho(int x, int y, FX_CG_2D_API api){
        this.x = x;
        this.y = y;
        this.api = api;

    }

    @Override
    public void desenhar() {
        api.empilhar();
        
        api.preenchimento(Color.BROWN);
        api.retangulo(x, y, width, height, Estilo.PREENCHIDO);

        api.desempilhar();
    }
    @Override
    public void atualizar() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'atualizar'");
    }
    @Override
    public Rectangle2D getBounds() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getBounds'");
    }
}
