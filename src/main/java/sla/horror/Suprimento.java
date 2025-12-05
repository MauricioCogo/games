package sla.horror;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import lombok.Data;
import sla.api.FX_CG_2D_API;
import sla.api.FX_CG_2D_API.Estilo;
import sla.horror.Main.TipoSuprimento;

@Data
public class Suprimento {

    private double x, y;
    private int size = 20;
    private int life = 0;
    private int ammo = 0;
    private int flashlight = 0;
    private TipoSuprimento type;
    private boolean coletado = false;

    Image lifeImage = new Image(getClass().getResource("/imagens/horror/kitmedico.png").toExternalForm());
    Image supply = new Image(getClass().getResource("/imagens/horror/suprimentos.png").toExternalForm());

    public Suprimento(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void desenhar(FX_CG_2D_API api) {
        if (!coletado) {
            if (type == TipoSuprimento.VIDA) {
                api.imagem(lifeImage, x, y);
            }else{
                api.imagem(supply, x, y);
            }
        }
    }

    public Rectangle2D getBounds() {
        return new Rectangle2D(x, y, size, size);
    }

    public boolean isColetado() {
        return coletado;
    }

    public void coletar() {
        coletado = true;
    }
}
