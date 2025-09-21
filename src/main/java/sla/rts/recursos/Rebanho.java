package sla.rts.recursos;

import javafx.geometry.Rectangle2D;
import javafx.scene.paint.Color;
import sla.api.FX_CG_2D_API;
import sla.api.FX_CG_2D_API.Estilo;
import sla.rts.Base;
import sla.rts.Reino;
import sla.rts.TipoRecurso;

public class Rebanho extends Base implements RecursoFonte {
    private int quantidadeDispon = 100;
    private Reino reino;
    private int x, y;

    private int width = 30, height = 30;

    Color cor;

    FX_CG_2D_API api;

    public Rebanho(int x, int y, FX_CG_2D_API api, int qntdade) {
        this.x = x;
        this.y = y;
        this.api = api;
        this.quantidadeDispon = qntdade;
    }

    public Rebanho(int x, int y, FX_CG_2D_API api, Reino reino) {
        this.x = x;
        this.y = y;
        this.api = api;
        this.reino = reino;
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
    }

    @Override
    public Rectangle2D getBounds() {
        return new Rectangle2D(x, y, width, height);
    }

    @Override
    public double coletar(double quantidade) {
        double coletado = Math.min(quantidade, quantidadeDispon);
        quantidadeDispon -= coletado;
        return coletado;
    }

    @Override
    public TipoRecurso getTipo() {
        return TipoRecurso.COMIDA;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    public boolean estaVazio() {
        return quantidadeDispon <= 0;
    }
    
    public String toString(){
        return "X: "+x +"\n Y: "+y+"\n capacidade: "+quantidadeDispon;
    }

}
