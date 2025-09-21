package sla.rts;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

import javafx.geometry.Rectangle2D;
import javafx.scene.paint.Color;
import lombok.Data;
import lombok.EqualsAndHashCode;
import sla.api.FX_CG_2D_API;
import sla.api.FX_CG_2D_API.Estilo;
import sla.rts.recursos.Bosque;
import sla.rts.recursos.MinaFerro;
import sla.rts.recursos.Rebanho;
import sla.rts.tropas.Aldeao;
import sla.rts.tropas.Soldado;

@Data
@EqualsAndHashCode(callSuper = false)
public class Reino extends Base {
    FX_CG_2D_API api;
    Random random = new Random();

    private String nome;

    private double comida;
    private double madeira;
    private double ferro;

    private int x, y, raio = 100;
    private int width = 40, height = 40;
    private Color cor;

    private List<Aldeao> aldeoes = new ArrayList<>();
    private List<Soldado> soldados = new ArrayList<>();
    private List<MinaFerro> minas = new ArrayList<>();
    private List<Rebanho> rebanhos = new ArrayList<>();
    private List<Bosque> bosques = new ArrayList<>();

    public Reino(int x, int y, String nome, Color cor, FX_CG_2D_API api) {
        this.x = x;
        this.y = y;
        this.nome = nome;
        this.cor = cor;
        this.api = api;

    }

    public void aoIniciar() {

        criar(2, soldados, () -> new Soldado(
                x + random.nextInt(raio * 2 + 20) - raio,
                y + random.nextInt(raio * 2 + 20) - raio,
                this, api, cor.darker()));

        criar(2, minas, () -> new MinaFerro(
                x + random.nextInt(raio * 2 + 100) - raio,
                y + random.nextInt(raio * 2 + 100) - raio,
                api, this));

        criar(2, rebanhos, () -> new Rebanho(
                x + random.nextInt(raio * 2 + 100) - raio,
                y + random.nextInt(raio * 2 + 100) - raio,
                api, this));

        criar(2, bosques, () -> new Bosque(
                x + random.nextInt(raio * 2 + 100) - raio,
                y + random.nextInt(raio * 2 + 100) - raio,
                api, this));

        Aldeao ac = new Aldeao(x + random.nextInt(raio * 2 + 20) - raio, y + random.nextInt(raio * 2 + 20) - raio, this, api, cor.brighter(), rebanhos.get(0));
        Aldeao am = new Aldeao(x + random.nextInt(raio * 2 + 20) - raio, y + random.nextInt(raio * 2 + 20) - raio, this, api, cor.brighter(), bosques.get(0));
        Aldeao af = new Aldeao(x + random.nextInt(raio * 2 + 20) - raio, y + random.nextInt(raio * 2 + 20) - raio, this, api, cor.brighter(), minas.get(0));
        aldeoes.add(ac);
        aldeoes.add(am);
        aldeoes.add(af);
    }

    @Override
    public void desenhar() {
        api.empilhar();

        api.preenchimento(cor);
        api.retangulo(x, y, width, height, Estilo.PREENCHIDO);
        aldeoes.forEach(Aldeao::desenhar);
        soldados.forEach(Soldado::desenhar);
        minas.forEach(MinaFerro::desenhar);
        rebanhos.forEach(Rebanho::desenhar);
        bosques.forEach(Bosque::desenhar);

        api.texto("Comida: " + comida, x, y + 20, 10);
        api.texto("Ferro: " + ferro, x, y + 30, 10);
        api.texto("Madeira: " + madeira, x, y + 40, 10);

        api.desempilhar();
    }

    @Override
    public void atualizar() {
        aldeoes.forEach(Aldeao::atualizar);
        minas.forEach(MinaFerro::atualizar);
        rebanhos.forEach(Rebanho::atualizar);
        bosques.forEach(Bosque::atualizar);



        rebanhos.removeIf(Rebanho::estaVazio);
        minas.removeIf(MinaFerro::estaVazio);
        bosques.removeIf(Bosque::estaVazio);
    }

    public void adicionarRecurso(TipoRecurso tipo, double valor) {
        switch (tipo) {
            case COMIDA -> comida += valor;
            case MADEIRA -> madeira += valor;
            case FERRO -> ferro += valor;
        }
    }

    @Override
    public Rectangle2D getBounds() {
        return new Rectangle2D(x, y, width, height);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    private <T> void criar(int quantidade, List<T> lista, Supplier<T> factory) {
        for (int i = 0; i < quantidade; i++) {
            lista.add(factory.get());
        }
    }

}
