package sla.rts.tropas;

import java.util.List;

import javafx.geometry.Rectangle2D;
import javafx.scene.paint.Color;
import sla.api.FX_CG_2D_API;
import sla.api.FX_CG_2D_API.Estilo;
import sla.rts.Base;
import sla.rts.Reino;
import sla.rts.TipoRecurso;
import sla.rts.recursos.RecursoFonte;

public class Aldeao extends Base {
    FX_CG_2D_API api;

    private int vida;
    private int capacidade = 10;
    private int carregando = 0;
    private Color cor;
    private int hspd, vpsd, spd = 3;
    private TipoRecurso tipoCarregado;
    private RecursoFonte destino;

    private int x, y;
    private int width = 20, height = 20;

    private Reino reino;

    public Aldeao(FX_CG_2D_API api) {
        this.api = api;
    }

    public Aldeao(int x, int y, Reino reino, FX_CG_2D_API api, Color cor, RecursoFonte destino) {
        this.x = x;
        this.y = y;
        this.reino = reino;
        this.api = api;
        this.cor = cor;
        this.destino = destino;
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
        if (destino == null)
            return; // não tem recurso

        if (carregando == 0) {
            moverPara(destino.getX(), destino.getY());
            if (estaNoDestino(destino.getX(), destino.getY())) {
                carregando = (int) destino.coletar(capacidade);
                tipoCarregado = destino.getTipo();
            }
        } else {
            // carregando, indo para o reino
            moverPara(reino.getX(), reino.getY());
            if (estaNoDestino(reino.getX(), reino.getY())) {
                reino.adicionarRecurso(tipoCarregado, carregando);
                carregando = 0;

                // recurso acabou? pega o próximo
                if (destino.estaVazio()) {
                    RecursoFonte proximo = proximoRecurso(tipoCarregado, destino);
                    atribuirRecurso(proximo);
                }
            }
        }
    }

    public void atribuirRecurso(RecursoFonte fonte) {
        this.destino = fonte;
    }

    private void moverPara(int destinoX, int destinoY) {
        if (x < destinoX)
            x += spd;
        if (x > destinoX)
            x -= spd;
        if (y < destinoY)
            y += spd;
        if (y > destinoY)
            y -= spd;
    }

    private boolean estaNoDestino(int dx, int dy) {
        return Math.abs(x - dx) < 5 && Math.abs(y - dy) < 5;
    }

    @Override
    public Rectangle2D getBounds() {
        return new Rectangle2D(x, y, width, height);
    }

    private RecursoFonte proximoRecurso(TipoRecurso tipo, RecursoFonte atual) {

        List<? extends RecursoFonte> lista = switch (tipo) {
            case COMIDA -> reino.getRebanhos();
            case MADEIRA -> reino.getBosques();
            case FERRO -> reino.getMinas();
        };

        if (lista.isEmpty())
            return null;

        int idx = lista.indexOf(atual);
        return lista.get((idx + 1) % lista.size());

    }

}
