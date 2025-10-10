package sla.teste;

import sla.api.FX_CG_2D_API;
import sla.api.FX_CG_2D_API.AcaoTimer;
import sla.api.FX_CG_2D_API.Estilo;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import lombok.Data;

@Data
public class Player {
    double x, y, hspd = 0, vspd = 0;
    final double jumpHeight = -12, grav = 0.5;
    final int ground = 300;
    private int width = 40, height = 128;
    private boolean side;
    private double strong = 0;

    int direction = 1;
    boolean attackmode = false, left = false, right = false, jumping = false;
    boolean hand = false;

    Image sprite;
    Image idle, walk, attackidle, punch, punch2, jump;

    Healthbar health;
    Ataque ataqueAtual;
    FX_CG_2D_API api;

    public Player(int startX, int startY,String nome, boolean side, FX_CG_2D_API api) {
        x = startX;
        y = startY;
        idle = new Image(getClass().getResource("/imagens/pkm/idle.gif").toExternalForm());
        walk = new Image(getClass().getResource("/imagens/pkm/walk.gif").toExternalForm());
        attackidle = new Image(getClass().getResource("/imagens/pkm/attackidle.gif").toExternalForm());
        punch = new Image(getClass().getResource("/imagens/pkm/punch.gif").toExternalForm());
        punch2 = new Image(getClass().getResource("/imagens/pkm/punch2.gif").toExternalForm());
        jump = new Image(getClass().getResource("/imagens/pkm/jump.gif").toExternalForm());
        sprite = idle;
        this.side = side;
        this.api = api;
        health = new Healthbar(side, nome, 100, 0, api);
    }

    public void atualizar() {

        // gravidade
        vspd += grav;
        x += hspd;
        y += vspd;

        // colisão com chão
        if (y >= ground) {
            y = ground;
            vspd = 0;
        }

        // movimento horizontal
        if (left && !right) {
            hspd = -4;
            direction = -1;
            sprite = walk;
        } else if (right && !left) {
            hspd = 4;
            direction = 1;
            sprite = walk;
        } else {
            hspd = 0;
            if (!attackmode && y >= ground)
                sprite = idle;
        }

        // salto
        if (jumping && y >= ground) {
            vspd = jumpHeight;
            jumping = false;
            sprite = jump;
        }
        if (y < ground && vspd != 0)
            sprite = jump;

        if (strong <= 100 && strong > 0) {
            strong-=0.1;
        }
    }

    public void desenhar() {
        if (health.getLife() > 0) {
            api.empilhar();
            api.transladar(x, y);
            api.escalar(direction, 1);
            api.transladar(-x, -y);
            api.imagem(sprite, x - sprite.getWidth() / 2, y - sprite.getHeight() / 2);
            api.desempilhar();

            health.desenhar();
        }

        if (ataqueAtual != null)
            ataqueAtual.desenhar();
    }

    public void atacar(FX_CG_2D_API api) {
        if (y < ground || hspd != 0)
            return;

        double ataqueX = (direction == 1) ? x + 10 : x - 40;
        ataqueAtual = new Ataque(ataqueX, y - 10, 40, 10, direction, api);

        sprite = hand ? punch : punch2;
        hand = !hand;

        api.iniciarTimer("punch", 0.2, false, new AcaoTimer() {
            @Override
            public void executar() {
                ataqueAtual = null;
                sprite = attackidle;
            }
        });

        if (!attackmode) {
            attackmode = true;
            api.iniciarTimer("attack", 5, false, new AcaoTimer() {
                @Override
                public void executar() {
                    attackmode = false;
                    sprite = idle;
                }
            });
        }
    }

    public void teclaPressionada(KeyEvent e) {
        if (side) {
            if (e.getCode() == KeyCode.UP)
                jumping = true;
            if (e.getCode() == KeyCode.LEFT)
                left = true;
            if (e.getCode() == KeyCode.RIGHT)
                right = true;
            if (e.getCode() == KeyCode.K)
                atacar(api);
        } else {
            if (e.getCode() == KeyCode.W)
                jumping = true;
            if (e.getCode() == KeyCode.A)
                left = true;
            if (e.getCode() == KeyCode.D)
                right = true;
            if (e.getCode() == KeyCode.SPACE)
                atacar(api);
        }
    }

    public void teclaLiberada(KeyEvent e) {
        if (side) {
            if (e.getCode() == KeyCode.UP)
                jumping = false;
            if (e.getCode() == KeyCode.LEFT)
                left = false;
            if (e.getCode() == KeyCode.RIGHT)
                right = false;
            if (!left && !right && !attackmode && y >= ground)
                sprite = idle;
        } else {
            if (e.getCode() == KeyCode.W)
                jumping = false;
            if (e.getCode() == KeyCode.A)
                left = false;
            if (e.getCode() == KeyCode.D)
                right = false;
            if (!left && !right && !attackmode && y >= ground)
                sprite = idle;
        }
    }

    public Rectangle2D getBounds() {
        return new Rectangle2D(x - (width / 2), y - (height / 2), width, height);
    }
}
