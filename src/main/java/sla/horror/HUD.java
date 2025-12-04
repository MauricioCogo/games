package sla.horror;

import sla.api.FX_CG_2D_API;

public class HUD {
    private int x;
    private int y;

    private int gap;

    private FX_CG_2D_API api;

    private int round;

    private int ammo;

    private boolean supp;


    public HUD(int x, int y, int gap, FX_CG_2D_API api){
        this.x = x;
        this.y = y;
        this.gap = gap;
        this.api = api;
    }

    public void desenhar(){
        api.texto("Round", x, y, gap);
    }
}
