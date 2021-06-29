import java.util.*;
import java.lang.*;
import java.util.Scanner;

class main {

    static class FlujoMaximo {
        static int numVertices;
        int flujo_Camino;


        boolean bfs(int[][] grafoResidual, int s, int t, int[] nodoPadre) {

            boolean[] visitados = new boolean[numVertices];

            Queue<Integer> cola = new ArrayDeque<>();
            cola.add(s);
            visitados[s] = true;
            nodoPadre[s] = -1;

            while (!cola.isEmpty()) {
                int u = cola.poll(); //Retorna y saca el primero de la lista.

                for (int v = 0; v < numVertices; v++) {
                    if (!visitados[v] && grafoResidual[u][v] > 0) {
                        cola.add(v);
                        nodoPadre[v] = u;
                        visitados[v] = true;
                        flujo_Camino = Math.min(flujo_Camino, grafoResidual[u][v]);
                    }
                }
            }

            return visitados[t];
            
        }


        int edmondsKarp(int s, int t, int[][] grafoResidual) {
            int u, v;
            int[] nodoPadre = new int[numVertices];
            int flujoMaximo = 0;
            flujo_Camino = Integer.MAX_VALUE;
            while (bfs(grafoResidual, s, t, nodoPadre)) {

                for (v = t; v != s; v = nodoPadre[v]) {
                    u = nodoPadre[v];
                    grafoResidual[u][v] -= flujo_Camino;
                    grafoResidual[v][u] += flujo_Camino;
                }

                flujoMaximo += flujo_Camino;
            }

            return flujoMaximo;
        }


        public static void main(String[] args) {

            Scanner scanner = new Scanner(System.in);
            int arcos, s, t, u, v, c;
            numVertices = scanner.nextInt();
            arcos = scanner.nextInt();
            s = scanner.nextInt()-1;
            t = scanner.nextInt()-1;


            int [][] graforesidual = new int [numVertices][numVertices];

            for(int i = 0; i < arcos; i++){
                u = scanner.nextInt()-1;
                v = scanner.nextInt()-1;
                c = scanner.nextInt();
                graforesidual[u][v] = c;
            }

            scanner.close();

            FlujoMaximo m = new FlujoMaximo();

            System.out.println(m.edmondsKarp(s, t,graforesidual));
        }
    }

}