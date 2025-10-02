package sla.teste;

import javafx.geometry.Rectangle2D;
import sla.api.FX_CG_2D_API;

public class Ataque {
    private double x, y;
    private int width, height;
    private int direction;
    private boolean colide = true;

    private FX_CG_2D_API api;

    public Ataque(double x, double y, int width, int height, int direction, FX_CG_2D_API api) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.direction = direction;
        this.api = api;
    }

    public void desenhar() {

    }

    public Rectangle2D getBounds() {
        double visualX = (direction == 1) ? x : x - width;
        return new Rectangle2D(visualX, y, width, height);
    }


    public void setColide(boolean c){
        this.colide = c;
    }

    public boolean getColide(){
        return colide;
    }

}
