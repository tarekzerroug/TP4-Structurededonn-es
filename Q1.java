import java.util.*;

public class Q1 {

    // algorithme de kosaraju est inspiré de stack overflow .
    
    
    /* ============================================================
       MÉTHODES PRIVÉES — OUTILS INTERNES
       (DFS, graph reverse, détection de cycles, etc.)
       ============================================================ */

    // DFS utilisé pour détecter l’existence d’un cycle
    private static boolean dfs(int[][] g, int n, int[] s) {
        s[n] = 1; // en cours de visite
        for (int v : g[n]) {
            // si on retombe sur un nœud "en cours" → cycle
            if (s[v] == 1 || (s[v] == 0 && dfs(g, v, s)))
                return true;
        }
        s[n] = 2; // complètement traité
        return false;
    }

    // DFS spécial pour récupérer tous les cycles simples rencontrés
    private static void findCycles(int[][] g, int n, int[] s, List<Integer> path, List<List<Integer>> out) {
        s[n] = 1;
        path.add(n);

        for (int v : g[n]) {
            if (s[v] == 1) {
                // on a trouvé un retour dans le chemin → cycle
                int idx = path.indexOf(v);
                if (idx != -1) out.add(new ArrayList<>(path.subList(idx, path.size())));
            } else if (s[v] == 0) {
                findCycles(g, v, s, path, out);
            }
        }

        s[n] = 2;
        path.remove(path.size() - 1);
    }

    // fait commencer le cycle par son plus petit élément (normalisation)
    private static void normalize(List<Integer> c) {
        if (c.isEmpty()) return;
        int minIdx = 0;
        for (int i = 1; i < c.size(); i++)
            if (c.get(i) < c.get(minIdx))
                minIdx = i;

        // rotation pour mettre l’élément min en premier
        Collections.rotate(c, -minIdx);
    }

    // DFS pour Kosaraju (premier passage)
    private static void orderDfs(int[][] g, int n, boolean[] vis, List<Integer> order) {
        vis[n] = true;
        for (int v : g[n])
            if (!vis[v]) orderDfs(g, v, vis, order);
        order.add(n); // on enregistre l’ordre de fin
    }

    // retourne un graphe orienté inversé
    private static int[][] reverse(int[][] g) {
        List<Integer>[] rev = new ArrayList[g.length];

        for (int i = 0; i < g.length; i++)
            rev[i] = new ArrayList<>();

        for (int i = 0; i < g.length; i++)
            for (int v : g[i])
                rev[v].add(i);

        int[][] out = new int[g.length][];
        for (int i = 0; i < g.length; i++)
            out[i] = rev[i].stream().mapToInt(x -> x).toArray();

        return out;
    }

    // DFS pour collecter une composante fortement connexe
    private static void collect(int[][] g, int n, boolean[] vis, List<Integer> comp) {
        vis[n] = true;
        comp.add(n);
        for (int v : g[n])
            if (!vis[v]) collect(g, v, vis, comp);
    }

    // DFS classique pour compter les nœuds atteignables
    private static void reachDfs(int[][] g, int n, boolean[] vis) {
        vis[n] = true;
        for (int v : g[n])
            if (!vis[v]) reachDfs(g, v, vis);
    }

    // enlève un nœud du graphe 
    private static int[][] removeNode(int[][] g, int rm) {
        List<Integer>[] out = new ArrayList[g.length];
        for (int i = 0; i < g.length; i++)
            out[i] = new ArrayList<>();

        for (int i = 0; i < g.length; i++) {
            if (i == rm) continue;
            for (int v : g[i])
                if (v != rm) out[i].add(v);
        }

        int[][] res = new int[g.length][];
        for (int i = 0; i < g.length; i++)
            res[i] = out[i].stream().mapToInt(x -> x).toArray();

        return res;
    }

    // DFS qui attribue un teps d’entrée et de sortie (finishing time)
    private static void timeDfs(int[][] g, int u, boolean[] vis,
                            Map<Integer, Integer> times, int[] timer) {

    vis[u] = true;

    // temps d'entrée
    timer[0]++;

    // explorer les voisins
    for (int v : g[u]) {
        if (!vis[v]) {
            timeDfs(g, v, vis, times, timer);
        }
    }

    // temps de sortie
    timer[0]++;
    times.put(u, timer[0]);   // on enregistre le temps de FIN du nœud
}
    // collecte toutes les dépendances d’un nœud
    private static void getDeps(int[][] g, int n, Set<Integer> deps) {
        for (int v : g[n])
            if (deps.add(v)) getDeps(g, v, deps);
    }

    // cherche la plus longue chaîne d’arêtes (sans re-visiter)
    private static int longestDfs(int[][] g, int node, boolean[] onPath) {
        if (onPath[node]) return 0; // on évite les cycles

        onPath[node] = true;
        int best = 1; // au minimum, la chaîne contient ce nœud

        for (int v : g[node]) {
            int cand = 1 + longestDfs(g, v, onPath);
            best = Math.max(best, cand);
        }

        onPath[node] = false;
        return best;
    }

    /* ============================================================
       MÉTHODES PUBLIQUES — CE QUI EST DEMANDÉ DANS LE TP
       ============================================================ */

    // Détection de cycle global
    public static boolean hasCycle(int[][] g) {
        int[] state = new int[g.length];
        for (int i = 0; i < g.length; i++)
            if (state[i] == 0 && dfs(g, i, state))
                return true;
        return false;
    }

    // Liste tous les cycles simples
    public static List<List<Integer>> findAllCycles(int[][] g) {
        List<List<Integer>> out = new ArrayList<>();
        int[] state = new int[g.length];
        List<Integer> path = new ArrayList<>();

        for (int i = 0; i < g.length; i++)
            if (state[i] == 0)
                findCycles(g, i, state, path, out);

        out.forEach(Q1::normalize);
        return out;
    }

    // Composantes Fortement Connexes (Kosaraju) , inspiré de stack overflow  .
    public static List<List<Integer>> stronglyConnectedComponents(int[][] g) {
        int n = g.length;
        List<Integer> order = new ArrayList<>();
        boolean[] vis = new boolean[n];

        // premier passage
        for (int i = 0; i < n; i++)
            if (!vis[i]) orderDfs(g, i, vis, order);

        // graphe inversé
        int[][] rev = reverse(g);
        Arrays.fill(vis, false);

        // second passage
        List<List<Integer>> scc = new ArrayList<>();
        for (int i = order.size() - 1; i >= 0; i--) {
            int node = order.get(i);
            if (!vis[node]) {
                List<Integer> comp = new ArrayList<>();
                collect(rev, node, vis, comp);
                Collections.sort(comp);
                scc.add(comp);
            }
        }

        // tri : d’abord par taille décroissante, ensuite par plus grand élément
        scc.sort((a, b) -> {
            int cmp = Integer.compare(b.size(), a.size());  // taille
            if (cmp != 0) return cmp;
            return Integer.compare(b.get(0), a.get(0));    
        });

        return scc;
    }

    // Nombre de nœuds atteignables
    public static int countReachableNodes(int[][] g, int start) {
        boolean[] vis = new boolean[g.length];
        reachDfs(g, start, vis);
        int count = 0;
        for (boolean b : vis) if (b) count++;
        return count;
    }

    // Identifie les nœuds qui ne sont pas dans la composante fotmenet connexe finale
    public static List<Integer> findBridgeNodes(int[][] g) {
        List<List<Integer>> scc = stronglyConnectedComponents(g);

        // si plusieurs composantes non triviales, alors aucun nœud pont
        int big = 0;
        for (List<Integer> c : scc)
            if (c.size() > 1) big++;

        if (big > 1) return new ArrayList<>();

        // on repère les noeuds ponts 
        List<Integer> last = scc.get(scc.size() - 1);
        Set<Integer> inside = new HashSet<>(last);

        List<Integer> out = new ArrayList<>();
        for (int i = 0; i < g.length; i++)
            if (!inside.contains(i))
                out.add(i);

        Collections.sort(out);
        return out;
    }

    // Temps de fin DFS
    public static Map<Integer, Integer> getFinishingTimes(int[][] g) {
    Map<Integer, Integer> times = new HashMap<>(); 
    boolean[] vis = new boolean[g.length];
    int[] timer = {0};

    // Toujours visiter les nœuds dans l'ordre croissant
    for (int i = 0; i < g.length; i++) {
        if (!vis[i]) {
            timeDfs(g, i, vis, times, timer);
        }
    }

    return times;
}

    // teste possibilité d’installation malgré nœuds cassés
    public static boolean canInstallAll(int[][] g, List<Integer> broken) {
        if (hasCycle(g)) return false;

        Set<Integer> set = new HashSet<>(broken);
        for (int i = 0; i < g.length; i++) {
            if (!set.contains(i)) {
                for (int d : g[i])
                    if (set.contains(d)) return false;
            }
        }
        return true;
    }

    // Toutes les dépendances minimales d’un package
    public static List<Integer> findMinimalDependencySet(int[][] g, int target) {
        Set<Integer> deps = new HashSet<>();
        getDeps(g, target, deps);
        List<Integer> out = new ArrayList<>(deps);
        Collections.sort(out);
        return out;
    }

    // Cherche la plus longue chaine possible dans un graphe orienté acyclique
    public static int longestDependencyChain(int[][] g) {
        int n = g.length;
        boolean[] onPath = new boolean[n];
        int max = 0;

        for (int i = 0; i < n; i++)
            max = Math.max(max, longestDfs(g, i, onPath));

        return max;
    }

    // nœuds sans prédécesseurs
    public static List<Integer> findAllSourceNodes(int[][] g) {
        int n = g.length;
        int[] indegree = new int[n];

        for (int i = 0; i < n; i++)
            for (int v : g[i])
                indegree[v]++;

        List<Integer> out = new ArrayList<>();
        for (int i = 0; i < n; i++)
            if (indegree[i] == 0)
                out.add(i);

        return out;
    }
}