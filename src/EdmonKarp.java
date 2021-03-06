import static java.lang.Math.min;

import java.util.*;

public class EdmonKarp {

    public static class Arco {
        int u;
        int v;
        long capacidad;
        int flujo;
        Arco residual;

        public Arco(int u,int v,long cap){
            this.u = u;
            this.v = v;
            this.capacidad = cap;
        }

        public long capacidadRestante() {
            return capacidad - flujo;
        }

        public void aumentado(long flujoCamino) {
            flujo += flujoCamino;
            residual.flujo -= flujoCamino;
        }
    }

    private static class FlujoMaximo {
        int numVertices, s, t;
        private int nodoVisitado = 1;
        private final int[] visitados;
        protected List<Arco>[] grafo;

        public FlujoMaximo(int numVertices, int s, int t, LinkedList<Arco>[] grafo) {
            this.numVertices = numVertices;
            this.s = s;
            this.t = t;
            visitados = new int[numVertices+1];
            this.grafo = grafo;
        }

        public long flujoMaximo() {
            long flujoCamino = -1;
            long flujoMaximo = 0;

            while (flujoCamino != 0){
                nodoVisitado++;
                flujoCamino = bfs();
                flujoMaximo += flujoCamino;
            }

            return flujoMaximo;
        }

        private long bfs() {
            Queue<Integer> cola = new ArrayDeque<>(numVertices);
            visitados[s] = nodoVisitado;
            cola.offer(s);

            Arco[] anterior = new Arco[numVertices+1];
            long cap;
            while (!cola.isEmpty()) {
                int vertice = cola.poll();
                if (vertice == t)
                    break;

                for (Arco arco : grafo[vertice]) {
                    cap = arco.capacidadRestante();
                    if (cap > 0 && (visitados[arco.v] != nodoVisitado)) {
                        visitados[arco.v] = nodoVisitado;
                        anterior[arco.v] = arco;
                        cola.offer(arco.v);
                    }
                }
            }

            if (anterior[t] == null) //No encontramos un camino desde s a t.
                return 0;

            long flujoCamino = hallarFlujoMinimo(anterior, t);
            recorridoAumentado(anterior, t, flujoCamino);

            return flujoCamino;
        }

        private static long hallarFlujoMinimo(Arco[] anterior, int t){
            long flujoCamino = Long.MAX_VALUE;
            for (Arco arco = anterior[t]; arco != null; arco = anterior[arco.u])
                flujoCamino = min(flujoCamino, arco.capacidadRestante());
            return flujoCamino;
        }

        private static void recorridoAumentado(Arco[] anterior, int t, long flujoCamino){
            for (Arco arco = anterior[t]; arco != null; arco = anterior[arco.u])
                arco.aumentado(flujoCamino);
        }
    }



    @SuppressWarnings("unchecked")
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        int arcos, s, t, u, v, c;
        int numVertices = scanner.nextInt();
        arcos = scanner.nextInt();
        s = scanner.nextInt();
        t = scanner.nextInt();

        LinkedList<Arco>[] grafo = new LinkedList[numVertices + 1];
        for (int i = 1; i < grafo.length; i++)
            grafo[i] = new LinkedList<>();

        Arco arco, residuo;

        for(int i = 0; i < arcos; i++){
            u = scanner.nextInt();
            v = scanner.nextInt();
            c = scanner.nextInt();
            arco = new Arco(u,v,c);
            residuo = new Arco(v,u,0);
            arco.residual = residuo;
            residuo.residual = arco;
            grafo[u].add(arco);
            grafo[v].add(residuo);
        }

        scanner.close();

        FlujoMaximo edmondKarp = new FlujoMaximo(numVertices, s, t, grafo);

        System.out.println(edmondKarp.flujoMaximo());

    }

}
