package sla.horror;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.image.Image;
import sla.api.FX_CG_2D_API;
import sla.api.FX_CG_2D_API.EfeitosSonoros;
import sla.api.FX_CG_2D_API.Estilo;

public class Gun {
    private int numProjectiles;
    private double bulletSpeed;
    private int bulletRemain;
    private Image sprite;
    private URL som;

    

    private List<Bullet> balas = new ArrayList<>();

    public Gun(int numProjectiles, double bulletSpeed, int bulletRemain, String nome, URL som) {
        this.numProjectiles = numProjectiles;
        this.bulletSpeed = bulletSpeed;
        this.bulletRemain = bulletRemain;
        this.sprite = new Image(getClass().getResource("/imagens/horror/"+nome).toExternalForm());
        this.som = som;
        
    }

    public void desenhar(FX_CG_2D_API api) {
        for (Bullet b : balas) b.desenhar(api);
    }

    public void desenharArma(FX_CG_2D_API api, double cx, double cy, double mouseX, double mouseY) {
        api.empilhar();
        double ang = Math.atan2(mouseY - cy, mouseX - cx);
        double angGraus = Math.toDegrees(ang);

        api.transladar(cx, cy);
        api.rotacionar(angGraus);
        api.transladar(-cx, -cy);

        api.imagem(sprite, cx, cy-5);
        api.desempilhar();
    }

    public void atualizar(FX_CG_2D_API api) {
        for (Bullet b : balas) b.atualizar(api);
    }

    public void atirar(MouseEvent e, double origemX, double origemY, double mouseX, double mouseY, FX_CG_2D_API api) {
        if (e.getButton() != MouseButton.PRIMARY) return;

        for (int i = 0; i < numProjectiles; i++) {
            double offsetAngle = 0;
            if (numProjectiles > 1) {
                offsetAngle = Math.toRadians(-10 + 20.0 * i / (numProjectiles - 1));
            }
            double dx = mouseX - origemX;
            double dy = mouseY - origemY;
            double len = Math.sqrt(dx*dx + dy*dy);
            if (len == 0) continue;
            dx /= len;
            dy /= len;

            double newDx = dx * Math.cos(offsetAngle) - dy * Math.sin(offsetAngle);
            double newDy = dx * Math.sin(offsetAngle) + dy * Math.cos(offsetAngle);

            EfeitosSonoros.carregarSom("tiro", som);
            EfeitosSonoros.tocarSom("tiro", false, false);

            balas.add(new Bullet(origemX, origemY, newDx, newDy, bulletRemain, bulletSpeed));
        }
    }

    public List<Bullet> getBullets(){
        return this.balas;
    }
}
