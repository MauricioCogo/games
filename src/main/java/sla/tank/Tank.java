package sla.tank;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import lombok.Data;
import lombok.EqualsAndHashCode;
import sla.api.FX_CG_2D_API;

@Data
@EqualsAndHashCode(callSuper = false)
public class Tank implements Base {

    private int x, y;
    private int middleX, middleY;
    private double mouseX, mouseY;
    private int canhaoLarg = 40;
    private int canhaoAlt = 10;
    private int raio = 20;

    private int width, height;
    private double spd = 4;

    private boolean hidden = false;
    private boolean die = false;

    private Image base, cano, belt, belt90, spr;

    List<Bullet> bl = new ArrayList<>();

    boolean left = false, right = false, up = false, down = false, reload = false;

    public Tank(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        base = new Image(getClass().getResource("/imagens/tank/b.gif").toExternalForm());
        cano = new Image(getClass().getResource("/imagens/tank/cano.gif").toExternalForm());
        belt = new Image(getClass().getResource("/imagens/tank/belt.gif").toExternalForm());
        belt90 = new Image(getClass().getResource("/imagens/tank/belt90.gif").toExternalForm());
        spr = belt;
    }

    @Override
    public void desenhar(FX_CG_2D_API api) {
        api.empilhar();

        for (Bullet bullet : bl) {
            bullet.desenhar(api);
        }

        if (!hidden) {

            if (reload) {
                api.texto("Recarregando...", x - (width/2), y + 70, 20);
            }

            api.imagem(spr, x, y);

            double angulo = Math.atan2(mouseY - middleY, mouseX - middleX);
            angulo = Math.toDegrees(angulo);

            api.transladar(middleX, middleY);
            api.rotacionar(angulo);
            api.transladar(-middleX, -middleY);

            api.imagem(base, middleX - raio, middleY - raio);
            api.imagem(cano, middleX, middleY - canhaoAlt / 2);

        }

        api.desempilhar();
    }

    @Override
    public void atualizar(FX_CG_2D_API api) {

        bl.removeIf(Bullet::getDie);

        double hspd = 0;
        double vspd = 0;

        if (up && !down) {
            vspd = -spd;
            width = 40;
            height = 60;
            spr = belt;

        } else if (down && !up) {
            vspd = spd;
            width = 40;
            height = 60;
            spr = belt;

        }

        if (left && !right) {
            hspd = -spd;
            width = 60;
            height = 40;
            spr = belt90;

        } else if (right && !left) {
            hspd = spd;
            width = 60;
            height = 40;
            spr = belt90;

        }

        x += hspd;
        y += vspd;
        middleX = x + (width / 2);
        middleY = y + (height / 2);
        for (Bullet bullet : bl) {
            bullet.atualizar(api);
        }
    }

    @Override
    public Rectangle2D getBounds() {
        return new Rectangle2D(x, y, width, height);
    }

    public void teclaPressionada(KeyEvent e, FX_CG_2D_API api) {
        if (e.getCode() == KeyCode.UP)
            up = true;
        if (e.getCode() == KeyCode.DOWN)
            down = true;
        if (e.getCode() == KeyCode.LEFT)
            left = true;
        if (e.getCode() == KeyCode.RIGHT)
            right = true;
    }

    public void teclaLiberada(KeyEvent e) {
        if (e.getCode() == KeyCode.UP)
            up = false;
        if (e.getCode() == KeyCode.DOWN)
            down = false;
        if (e.getCode() == KeyCode.LEFT)
            left = false;
        if (e.getCode() == KeyCode.RIGHT)
            right = false;
    }

    void atualizarMouse(double mx, double my) {
        this.mouseX = mx;
        this.mouseY = my;
    }

    public void shoot(MouseEvent e, FX_CG_2D_API api) {
        if (e.getButton() == MouseButton.PRIMARY && !reload) {
            reload = true;

            double startX = middleX;
            double startY = middleY;
            double dx = e.getX() - middleX;
            double dy = e.getY() - middleY;
            double length = Math.sqrt(dx * dx + dy * dy);
            dx /= length;
            dy /= length;
            Bullet bullet = new Bullet(startX, startY, dx, dy);
            bl.add(bullet);

            api.iniciarTimer("reload", 2, false, new FX_CG_2D_API.AcaoTimer() {
                @Override
                public void executar() {
                    reload = false;
                    System.out.println("Recarregado!");
                }
            });
        }
    }

    public boolean getDie() {
        return die;
    }

    public boolean isReloading(){
        return reload;
    }
}
