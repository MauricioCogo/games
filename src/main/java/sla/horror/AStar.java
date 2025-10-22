package sla.horror;

import java.util.*;

public class AStar {

    public static class Node {
        public final int x, y;
        public double g; // custo do inicio até aqui
        public double h; // heurística até o alvo
        public Node parent;

        public Node(int x, int y) { this.x = x; this.y = y; }

        public double f() { return g + h; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Node)) return false;
            Node n = (Node) o;
            return x == n.x && y == n.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

    // Heurística Manhattan (por grid 4-direções)
    private static double heuristic(int x1, int y1, int x2, int y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }

    // Retorna lista de pontos (int[2] = {x,y}) do inicio até o destino inclusive.
    // grid: '#' parede, ' ' caminho
    public static List<int[]> findPath(char[][] grid, int startX, int startY, int targetX, int targetY) {
        List<int[]> empty = Collections.emptyList();
        int h = grid.length;
        if (h == 0) return empty;
        int w = grid[0].length;
        if (!inBounds(startX, startY, w, h) || !inBounds(targetX, targetY, w, h)) return empty;
        if (grid[targetY][targetX] == '#') return empty; // destino inacessível
        if (grid[startY][startX] == '#') return empty;

        Node start = new Node(startX, startY);
        start.g = 0;
        start.h = heuristic(startX, startY, targetX, targetY);

        PriorityQueue<Node> open = new PriorityQueue<>(Comparator.comparingDouble(Node::f));
        Map<String, Node> allNodes = new HashMap<>();
        String key = key(startX, startY);
        allNodes.put(key, start);
        open.add(start);

        Set<String> closed = new HashSet<>();

        int[][] dirs = {{0,-1},{0,1},{-1,0},{1,0}}; // N S W E

        while (!open.isEmpty()) {
            Node current = open.poll();
            if (current.x == targetX && current.y == targetY) {
                return reconstructPath(current);
            }

            closed.add(key(current.x, current.y));

            for (int[] d : dirs) {
                int nx = current.x + d[0];
                int ny = current.y + d[1];
                if (!inBounds(nx, ny, w, h)) continue;
                if (grid[ny][nx] == '#') continue;

                String nkey = key(nx, ny);
                if (closed.contains(nkey)) continue;

                double tentativeG = current.g + 1; // custo uniforme = 1 por passo

                Node neighbor = allNodes.get(nkey);
                if (neighbor == null) {
                    neighbor = new Node(nx, ny);
                    neighbor.g = tentativeG;
                    neighbor.h = heuristic(nx, ny, targetX, targetY);
                    neighbor.parent = current;
                    allNodes.put(nkey, neighbor);
                    open.add(neighbor);
                } else if (tentativeG < neighbor.g) {
                    // melhor caminho
                    neighbor.g = tentativeG;
                    neighbor.parent = current;
                    // reinsert to update priority (simpler: remove+add)
                    open.remove(neighbor);
                    open.add(neighbor);
                }
            }
        }

        return empty; // sem caminho
    }

    private static List<int[]> reconstructPath(Node node) {
        LinkedList<int[]> path = new LinkedList<>();
        Node cur = node;
        while (cur != null) {
            path.addFirst(new int[]{cur.x, cur.y});
            cur = cur.parent;
        }
        return path;
    }

    private static boolean inBounds(int x, int y, int w, int h) {
        return x >= 0 && x < w && y >= 0 && y < h;
    }

    private static String key(int x, int y) { return x + "," + y; }
}
