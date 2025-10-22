package sla.horror;

import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Rectangle2D;
import javafx.scene.paint.Color;
import lombok.Data;
import sla.api.FX_CG_2D_API;

@Data
public class Monster {
    private double x, y;
    private int width = 30, height = 30;
    private double spd = 4;

    private boolean chase;

    private FX_CG_2D_API api;
    private Player player;
    private List<Particle> ps;

    // direção aleatória quando não está perseguindo
    private double randDX = 0;
    private double randDY = 0;

    // A* related
    private char[][] grid; // mapa do labirinto ('#' parede, ' ' caminho)
    private double cellSize; // TAMANHO_CELULA
    private List<int[]> path = new ArrayList<>(); // lista de células (x,y)
    private int pathIndex = 0;
    private int pathCooldown = 15; // frames entre recálculos
    private int pathTimer = 0;

    public Monster(double x, double y, FX_CG_2D_API api, Player player, char[][] grid, double cellSize) {
        this.x = x;
        this.y = y;
        this.api = api;
        this.player = player;
        this.grid = grid;
        this.cellSize = cellSize;

        api.iniciarTimer("chase", 3, true, () -> chase = false);

        mudarDirecaoAleatoria();

        api.iniciarTimer("walk", 1, true, () -> {
            if (chase) {
                createParticle(6, 5, Color.RED, 4);
            } else {
                createParticle(3, 2, Color.PURPLE, 10);

            }
        });
    }

    private void mudarDirecaoAleatoria() {
        api.iniciarTimer("movAleatorio", 1 + Math.random(), true, () -> {
            if (!chase) { // só muda direção se não estiver perseguindo
                randDX = Math.random() * 2 - 1;
                randDY = Math.random() * 2 - 1;
                double dist = Math.sqrt(randDX * randDX + randDY * randDY);
                if (dist > 0) {
                    randDX /= dist;
                    randDY /= dist;
                }
            }
        });
    }

    public void atualizar(List<Wall> walls) {
        ps = player.getPs();

        // detecta partículas para ativar chase
        for (Particle p : ps) {
            if (api.colisao(p.getBounds(), getBounds()) && p.getName() == 'p' && !p.isStop()) {
                chase = true;
            }
        }

        double dx, dy;

        if (chase) {
            // usar A*
            pathTimer++;
            if (pathTimer >= pathCooldown) {
                recomputePathToPlayer();
                pathTimer = 0;
            }

            if (path != null && path.size() > 0 && pathIndex < path.size()) {
                // converte célula atual do caminho para coordenada mundo (centro da célula)
                int[] targetCell = path.get(pathIndex);
                double tx = targetCell[0] * cellSize + cellSize / 2.0 - width / 2.0;
                double ty = targetCell[1] * cellSize + cellSize / 2.0 - height / 2.0;

                dx = tx - x;
                dy = ty - y;

                double distToCell = Math.sqrt(dx*dx + dy*dy);
                if (distToCell < spd) {
                    // chegou o suficiente na célula -> avança para próximo
                    pathIndex++;
                }
            } else {
                // fallback: mover diretamente para jogador (caso não tenha path)
                dx = player.getX() - x;
                dy = player.getY() - y;
            }
        } else {
            dx = randDX;
            dy = randDY;
        }

        double dist = Math.sqrt(dx * dx + dy * dy);
        if (dist > 0) {
            dx /= dist;
            dy /= dist;

            double[] tryX = { dx * spd, dx * spd, 0 };
            double[] tryY = { dy * spd, 0, dy * spd };

            for (int i = 0; i < tryX.length; i++) {
                double nextX = x + tryX[i];
                double nextY = y + tryY[i];
                Rectangle2D nextBounds = new Rectangle2D(nextX, nextY, width, height);

                boolean colide = false;
                for (Wall w : walls) {
                    if (api.colisao(nextBounds, w.getBounds())) {
                        colide = true;
                        break;
                    }
                }

                if (!colide) {
                    x = nextX;
                    y = nextY;
                    break;
                }
            }
        }
    }

    // recalcula o caminho A* do monstro até o jogador (em coordenadas de célula)
    private void recomputePathToPlayer() {
        if (grid == null) return;

        int startGX = worldToGridX(x, cellSize);
        int startGY = worldToGridY(y, cellSize);

        int targetGX = worldToGridX(player.getX(), cellSize);
        int targetGY = worldToGridY(player.getY(), cellSize);

        // valida range
        if (!inGrid(startGX, startGY) || !inGrid(targetGX, targetGY)) {
            path = new ArrayList<>();
            pathIndex = 0;
            return;
        }

        List<int[]> newPath = AStar.findPath(grid, startGX, startGY, targetGX, targetGY);
        if (newPath != null && !newPath.isEmpty()) {
            // opcional: ignorar o primeiro nó se ele for a célula atual
            path = newPath;
            pathIndex = 0;
            // se o primeiro nó for exatamente a mesma célula onde está o monstro, pular
            if (pathIndex < path.size()) {
                int[] first = path.get(pathIndex);
                if (first[0] == startGX && first[1] == startGY && path.size() > 1) {
                    pathIndex = 1;
                }
            }
        } else {
            // sem caminho encontrado
            path = new ArrayList<>();
            pathIndex = 0;
        }
    }

    private int worldToGridX(double wx, double cellSize) {
        int gx = (int) ( (wx + width/2.0) / cellSize );
        return gx;
    }

    private int worldToGridY(double wy, double cellSize) {
        int gy = (int) ( (wy + height/2.0) / cellSize );
        return gy;
    }

    private boolean inGrid(int gx, int gy) {
        if (grid == null) return false;
        return gy >= 0 && gy < grid.length && gx >= 0 && gx < grid[0].length;
    }

    public void desenhar() {
        // você pode desenhar aqui um retângulo/ sprite
        // por enquanto deixo vazio (ou desenhe com api)
        // ex: api.drawRect(x, y, width, height, Color.DARKRED);
    }

    public Rectangle2D getBounds() {
        return new Rectangle2D(x, y, width, height);
    }

    public void createParticle(int total, int spd, Color cor, int timer) {
        for (int i = 0; i < total; i++) {
            double angulo = i * (2 * Math.PI / total);
            double dx = Math.cos(angulo);
            double dy = Math.sin(angulo);
            Particle p = new Particle((double) x, (double) y, dx, dy, spd, cor,'m',timer, this.api);
            ps.add(p);
        }
    }
}
