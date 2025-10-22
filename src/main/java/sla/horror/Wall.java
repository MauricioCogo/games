package sla.horror;

import javafx.geometry.Rectangle2D;
import javafx.scene.paint.Color;
import sla.api.FX_CG_2D_API;
import sla.api.FX_CG_2D_API.Estilo;

public class Wall {
    private int x,y;
    private int width, height;
    
    private FX_CG_2D_API api;
    public Wall(int x, int y, int size, FX_CG_2D_API api){
        this.x = x;
        this.y = y;
        this.width = size;
        this.height = size;
        this.api = api;
    }

    public void atualizar(){

    }

    public void desenhar(){
        api.preenchimento(Color.BLACK);
        api.retangulo(x,y,width,height,Estilo.PREENCHIDO);
    }

    public Rectangle2D getBounds(){
        return new Rectangle2D(x, y, width, height);
    }
}
