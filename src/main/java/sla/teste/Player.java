package sla.teste;

import sla.api.FX_CG_2D_API;
import sla.api.FX_CG_2D_API.AcaoTimer;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class Player {
    double x, y;
    double hspd, vspd = 0;
    double jumpHeight = -12, grav = 0.5, teste = 2;

    int ground = 300;
    int direction = 1;

    boolean attackmode = false, left = false, right = false, jumping = false;
    boolean hand = false;

    Image sprite;
    Image idle, walk, attackidle, punch, jump, punch2;

    public Player(int startX, int startY) {
        x = startX;
        y = startY;

        idle = new Image(getClass().getResource("/imagens/pkm/idle.gif").toExternalForm());
        walk = new Image(getClass().getResource("/imagens/pkm/walk.gif").toExternalForm());
        attackidle = new Image(getClass().getResource("/imagens/pkm/attackidle.gif").toExternalForm());
        punch = new Image(getClass().getResource("/imagens/pkm/punch.gif").toExternalForm());
        punch2 = new Image(getClass().getResource("/imagens/pkm/punch2.gif").toExternalForm());
        jump = new Image(getClass().getResource("/imagens/pkm/jump.gif").toExternalForm());

        sprite = idle;
    }

    public void atualizar(FX_CG_2D_API api) {
        // gravidade e velocidade horizontal
        vspd += grav;
        x += hspd;

        // ve se foi pra direita e esquerda
        if (left && !right) {
            hspd = -4;
            direction = -1;
            sprite = walk;
            api.pararTimer("attack");
        } else {
            if (right && !left) {
                hspd = 4;
                direction = 1;
                sprite = walk;
                api.pararTimer("attack");

            } else {
                hspd = 0;
            }
        }

        // ve se pode pular e pula
        if (jumping && y >= ground) {
            vspd = jumpHeight;
            jumping = false;
        }

        //modifica a posição vertical
        y += vspd;

        // muda o sprite se n ta no chão
        if (y < ground) {
            sprite = jump;
        }

        // faz a colisão com o chão
        if (y >= ground) {
            y = ground;
            vspd = 0;
            if (!attackmode && hspd == 0) {
                sprite = idle;
            }
        }

    }

    public void desenhar(FX_CG_2D_API api) {
        api.empilhar();
        api.transladar(x, y);
        api.escalar(direction, 1);
        api.transladar(-x, -y);
        api.imagem(sprite, x - sprite.getWidth() / 2, y - sprite.getHeight() / 2);
        api.desempilhar();
    }

    public void teclaPressionada(KeyEvent e, FX_CG_2D_API api) {
        if (e.getCode() == KeyCode.UP) {
            jumping = true;
            attackmode = false;
        }

        if (e.getCode() == KeyCode.LEFT) {
            left = true;
            attackmode = false;
        }
        if (e.getCode() == KeyCode.RIGHT) {
            right = true;
            attackmode = false;
        }

        if (e.getCode() == KeyCode.SPACE) {

            if (hand) {
                sprite = punch;
                hand = false;
            } else {
                sprite = punch2;
                hand = true;
            }

            api.iniciarTimer("punch", 0.2, false, new AcaoTimer() {
                @Override
                public void executar() {
                    if (attackmode) {
                        sprite = attackidle;
                    }
                }
            });

            if (!attackmode) {
                attackmode = true;
                api.iniciarTimer("attack", 5, false, new AcaoTimer() {
                    @Override
                    public void executar() {
                        attackmode = false;
                        sprite = idle;
                        System.out.println("entrou em modo de atauq");
                    }
                });
            }
        }
    }

    public void teclaLiberada(KeyEvent e) {
        if (e.getCode() == KeyCode.SPACE) {
            jumping = false;
        }
        if (e.getCode() == KeyCode.LEFT) {
            left = false;
            sprite = idle;
        }
        if (e.getCode() == KeyCode.RIGHT) {
            right = false;
            sprite = idle;
        }
    }
}
