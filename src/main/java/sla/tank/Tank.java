package sla.tank;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Rectangle2D;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import lombok.Data;
import lombok.EqualsAndHashCode;
import sla.api.FX_CG_2D_API;
import sla.api.FX_CG_2D_API.AcaoTimer;
import sla.api.FX_CG_2D_API.Estilo;

@Data
@EqualsAndHashCode(callSuper = false)
public class Tank implements Base {

    private int x, y;
    private int middleX, middleY;
    private double mouseX, mouseY;
    private int width, height;
    private double spd = 4;

    List<Bullet> bl = new ArrayList<>();

    boolean left = false, right = false, up = false, down = false, reload = false;

    public Tank(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public void desenhar(FX_CG_2D_API api) {
        api.empilhar();

        api.preenchimento(Color.GREEN);
        api.retangulo(x, y, width, height, Estilo.PREENCHIDO);

        for (Bullet bullet : bl) {
            bullet.desenhar(api);
        }

        int raio = 20;
        api.preenchimento(Color.DARKGREEN);
        api.circulo(middleX - raio, middleY - raio, raio * 2, raio * 2, Estilo.PREENCHIDO);

        double angulo = Math.atan2(mouseY - middleY, mouseX - middleX);
        angulo = Math.toDegrees(angulo);

        api.transladar(middleX, middleY);
        api.rotacionar(angulo);
        api.transladar(-middleX, -middleY);

        int canhaoLarg = 40;
        int canhaoAlt = 10;
        api.retangulo(middleX, middleY - canhaoAlt / 2, canhaoLarg, canhaoAlt, Estilo.PREENCHIDO);

        if (reload) {
            api.texto("Recarregando...", x, y + 50, 20);
        }

        api.desempilhar();
    }

    @Override
    public void atualizar(FX_CG_2D_API api) {

        double hspd = 0;
        double vspd = 0;

        if (up && !down) {
            vspd = -spd;
        } else if (down && !up) {
            vspd = spd;
        }

        if (left && !right) {
            hspd = -spd;
        } else if (right && !left) {
            hspd = spd;
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

            // cria o tiro
            double startX = middleX;
            double startY = middleY;
            double dx = e.getX() - middleX;
            double dy = e.getY() - middleY;
            double length = Math.sqrt(dx * dx + dy * dy);
            dx /= length;
            dy /= length;
            Bullet bullet = new Bullet(startX, startY, dx, dy);
            bl.add(bullet);

            // inicia o timer para recarregar
            api.iniciarTimer("reload", 3, false, new FX_CG_2D_API.AcaoTimer() {
                @Override
                public void executar() {
                    reload = false;
                    System.out.println("Recarregado!");
                }
            });
        }
    }
}
