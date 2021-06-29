
import static java.lang.Math.min;

import java.util.*;

class EdmonKarp {

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


            public boolean esResidual() {
                return capacidad == 0;
            }

            public long remainingCapacity() {
                return capacidad - flujo;
            }

            public void aumentado(long flujoCamino) {
                flujo += flujoCamino;
                residual.flujo -= flujoCamino;
            }
        }

         static class FlujoMaximo {
        
            static final long INF = Long.MAX_VALUE / 2;
            
            int numVertices, s, t;
            
            private int nodoVisitado = 1;
            private final int[] visitados;
            
            protected boolean termino;
            
            protected List<Arco>[] grafo;

            public FlujoMaximo(int numVertices, int s, int t, LinkedList<Arco>[] grafo) {
                this.numVertices = numVertices;
                this.s = s;
                this.t = t;
                visitados = new int[numVertices];
                this.grafo = grafo;
            }

            public void visita(int i) {
                visitados[i] = nodoVisitado;
            }
            
            public boolean visitado(int i) {
                return visitados[i] == nodoVisitado;
            }
            
            public void marcarComoNoVisitado() {
                nodoVisitado++;
            }
            
            public long flujoMaximo() {
                long flujo = -1;
                long flujoMaximo = 0;
                
                while (flujo != 0){
                    marcarComoNoVisitado();
                    flujo = bfs();
                    flujoMaximo += flujo;
                }
                
                return flujoMaximo;
            }

            private long bfs() {
                Queue<Integer> cola = new ArrayDeque<>(numVertices);
                visita(s);
                //cola.offer(s);
                cola.add(s);
                
                Arco[] anterior = new Arco[numVertices];
                while (!cola.isEmpty()) {
                    int vertice = cola.poll();
                    if (vertice == t) break;

                    for (Arco Arco : grafo[vertice]) {
                        long cap = Arco.remainingCapacity();
                        if (cap > 0 && !visitado(Arco.v)) {
                            visita(Arco.v);
                            anterior[Arco.v] = Arco;
                            cola.offer(Arco.v);
                        }
                    }
                }
                
                if (anterior[t] == null) //No encontramos un camino desde s a t.
                    return 0;
                
                long flujoCamino = Long.MAX_VALUE;
                for (Arco Arco = anterior[t]; Arco != null; Arco = anterior[Arco.u])
                    flujoCamino = min(flujoCamino, Arco.remainingCapacity());

                for (Arco Arco = anterior[t]; Arco != null; Arco = anterior[Arco.u])
                    Arco.aumentado(flujoCamino);

                return flujoCamino;
            }


            public static void main(String[] args) {

                Scanner scanner = new Scanner(System.in);
                int arcos, s, t, u, v, c;
                int numVertices = scanner.nextInt();
                arcos = scanner.nextInt();
                s = scanner.nextInt()-1;
                t = scanner.nextInt()-1;

                LinkedList<Arco>[] grafo = new LinkedList[numVertices + 1];
                for (int i = 1; i < grafo.length; i++)
                    grafo[i] = new LinkedList<>();

                Arco arco,residuo;


                for(int i = 0; i < arcos; i++){
                    u = scanner.nextInt()-1;
                    v = scanner.nextInt()-1;
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
}

