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

@Data
@EqualsAndHashCode(callSuper = false)
public class Reino extends Base {
    FX_CG_2D_API api;
    Random random = new Random();

    private String nome;

    private int comida;
    private int madeira;
    private int ferro;

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
        criar(5, aldeoes, () -> new Aldeao(
                x + random.nextInt(raio * 2) - raio,
                y + random.nextInt(raio * 2) - raio,
                this, api, cor.brighter()));

        criar(2, soldados, () -> new Soldado(
                x + random.nextInt(raio * 2) - raio,
                y + random.nextInt(raio * 2) - raio,
                this, api, cor.darker()));

        criar(2, minas, () -> new MinaFerro(
                x + random.nextInt(raio * 2 + 100) - raio,
                y + random.nextInt(raio * 2 + 100) - raio,
                api));

        criar(2, rebanhos, () -> new Rebanho(
                x + random.nextInt(raio * 2 + 100) - raio,
                y + random.nextInt(raio * 2 + 100) - raio,
                api));

        criar(2, bosques, () -> new Bosque(
                x + random.nextInt(raio * 2 + 100) - raio,
                y + random.nextInt(raio * 2 + 100) - raio,
                api));
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

    private <T> void criar(int quantidade, List<T> lista, Supplier<T> factory) {
        for (int i = 0; i < quantidade; i++) {
            lista.add(factory.get());
        }
    }

}
