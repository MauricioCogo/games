package sla.rts;

import javafx.geometry.Rectangle2D;

public abstract class Base {
    protected int x,y;
    protected int width,height;

    public abstract void desenhar();
    public abstract void atualizar();

    public abstract Rectangle2D getBounds();;
}
