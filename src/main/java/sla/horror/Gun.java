package sla.horror;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import lombok.Data;
import javafx.scene.image.Image;
import sla.api.FX_CG_2D_API;
import sla.api.FX_CG_2D_API.EfeitosSonoros;
import sla.horror.util.Bullet;

@Data
public class Gun {

    private int numProjectiles;
    private int ammoMagazine;
    private int ammoNumberTotal;
    private int bulletLife;
    private double bulletSpeed;
    private Image sprite;
    private URL som;

    private String nome;

    private boolean noBullets = false;

    private List<Bullet> balas = new ArrayList<>();

    public Gun(int bulletNumberMax, int numProjectiles, double bulletSpeed, int bulletLife, String nome, URL som) {
        this.numProjectiles = numProjectiles;
        this.bulletSpeed = bulletSpeed;
        this.ammoMagazine = bulletNumberMax;
        this.ammoNumberTotal = bulletNumberMax;
        this.bulletLife = bulletLife;
        this.sprite = new Image(getClass().getResource("/imagens/horror/" + nome + ".png").toExternalForm());
        this.nome = nome;
        this.som = som;

    }

    public void desenhar(FX_CG_2D_API api, double cx, double cy) {
        for (Bullet b : balas)
            b.desenhar(api);
    }

    public void desenharArma(FX_CG_2D_API api, double cx, double cy, double mouseX, double mouseY) {

        System.out.println(ammoMagazine + " municao");

        api.empilhar();
        double ang = Math.atan2(mouseY - cy, mouseX - cx);
        double angGraus = Math.toDegrees(ang);

        api.transladar(cx, cy);
        api.rotacionar(angGraus);
        api.transladar(-cx, -cy);

        api.imagem(sprite, cx, cy - 5);
        api.desempilhar();
    }

    public void atualizar(FX_CG_2D_API api) {
        noBullets = ammoMagazine <= 0;

        for (Bullet b : balas)
            b.atualizar(api);
    }

    public void atirar(MouseEvent e, double origemX, double origemY, double mouseX, double mouseY, FX_CG_2D_API api) {
        if (ammoMagazine > 0) {
            if (e.getButton() != MouseButton.PRIMARY)
                return;

            ammoMagazine--;
            

            for (int i = 0; i < numProjectiles; i++) {
                double offsetAngle = 0;
                if (numProjectiles > 1) {
                    offsetAngle = Math.toRadians(-10 + 20.0 * i / (numProjectiles - 1));
                }
                double dx = mouseX - origemX;
                double dy = mouseY - origemY;
                double len = Math.sqrt(dx * dx + dy * dy);
                if (len == 0)
                    continue;
                dx /= len;
                dy /= len;

                double newDx = dx * Math.cos(offsetAngle) - dy * Math.sin(offsetAngle);
                double newDy = dx * Math.sin(offsetAngle) + dy * Math.cos(offsetAngle);

                EfeitosSonoros.carregarSom("tiro", som);
                EfeitosSonoros.tocarSom("tiro", false, false);

                balas.add(new Bullet(origemX, origemY, newDx, newDy, bulletLife, bulletSpeed));
            }
        }
    }

    public List<Bullet> getBullets() {
        return this.balas;
    }
}
