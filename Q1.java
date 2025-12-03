import java.util.*;

public class Q1 {

    /* ==========================
       MÉTHODES PRIVÉES EN HAUT , MÉTHODES AUXILAIIRES
       ========================== */

    private static boolean dfs(int[][] g, int n, int[] s) {
        s[n] = 1;
        for (int v : g[n])
            if (s[v] == 1 || (s[v] == 0 && dfs(g, v, s)))
                return true;
        s[n] = 2;
        return false;
    }

    private static void findCycles(int[][] g, int n, int[] s, List<Integer> p, List<List<Integer>> c) {
        s[n] = 1;
        p.add(n);
        for (int v : g[n]) {
            if (s[v] == 1) {
                int idx = p.indexOf(v);
                if (idx != -1) c.add(new ArrayList<>(p.subList(idx, p.size())));
            } 
            else if (s[v] == 0)
                findCycles(g, v, s, p, c);
        }
        s[n] = 2;
        p.remove(p.size() - 1);
    }

    private static void normalize(List<Integer> c) {
        if (c.isEmpty()) return;
        int minIdx = 0;
        for (int i = 1; i < c.size(); i++)
            if (c.get(i) < c.get(minIdx)) minIdx = i;
        Collections.rotate(c, -minIdx);
    }

    private static void orderDfs(int[][] g, int n, boolean[] v, List<Integer> o) {
        v[n] = true;
        for (int adj : g[n])
            if (!v[adj]) orderDfs(g, adj, v, o);
        o.add(n);
    }

    private static int[][] reverse(int[][] g) {
        List<Integer>[] rev = new List[g.length];
        for (int i = 0; i < g.length; i++) rev[i] = new ArrayList<>();
        for (int i = 0; i < g.length; i++)
            for (int j : g[i]) rev[j].add(i);
        int[][] out = new int[g.length][];
        for (int i = 0; i < g.length; i++)
            out[i] = rev[i].stream().mapToInt(x -> x).toArray();
        return out;
    }

    private static void collect(int[][] g, int n, boolean[] v, List<Integer> comp) {
        v[n] = true;
        comp.add(n);
        for (int adj : g[n])
            if (!v[adj]) collect(g, adj, v, comp);
    }

    private static void reachDfs(int[][] g, int n, boolean[] v) {
        v[n] = true;
        for (int adj : g[n])
            if (!v[adj]) reachDfs(g, adj, v);
    }

    private static int[][] removeNode(int[][] g, int rm) {
        List<Integer>[] out = new List[g.length];
        for (int i = 0; i < g.length; i++) out[i] = new ArrayList<>();
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

    private static void timeDfs(int[][] g, int u, boolean[] vis,
                            Map<Integer, Integer> times, int[] time) {
    vis[u] = true;

    // temps à l'ENTRÉE du nœud
    time[0]++;

    // explorer les voisins
    for (int v : g[u]) {
        if (!vis[v]) {
            timeDfs(g, v, vis, times, time);
        }
    }

    // temps à la SORTIE du nœud
    time[0]++;
    times.put(u, time[0]);
}

    private static void getDeps(int[][] g, int n, Set<Integer> d) {
        for (int v : g[n])
            if (d.add(v)) getDeps(g, v, d);
    }

   private static int longestDfs(int[][] g, int node, boolean[] onPath) {
    if (onPath[node]) {
        // on a déjà ce nœud dans le chemin courant → éviter le cycle
        return 0;
    }

    onPath[node] = true;
    int best = 1; // au moins ce nœud lui-même

    for (int v : g[node]) {
        int candidate = 1 + longestDfs(g, v, onPath);
        if (candidate > best) best = candidate;
    }

    onPath[node] = false; // backtracking
    return best;
}

    /* ==========================
       MÉTHODES PUBLIQUES EN BAS
       ========================== */

    public static boolean hasCycle(int[][] g) {
        int[] state = new int[g.length];
        for (int i = 0; i < g.length; i++)
            if (state[i] == 0 && dfs(g, i, state))
                return true;
        return false;
    }

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

    public static List<List<Integer>> stronglyConnectedComponents(int[][] g) {
        int n = g.length;
        List<Integer> order = new ArrayList<>();
        boolean[] vis = new boolean[n];

        for (int i = 0; i < n; i++)
            if (!vis[i]) orderDfs(g, i, vis, order);

        int[][] rev = reverse(g);
        vis = new boolean[n];

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

        // tri : taille décroissante, puis plus petit élément croissant
        scc.sort((a, b) -> {
            int cmp = Integer.compare(b.size(), a.size());
            if (cmp != 0) return cmp;
            return Integer.compare(b.get(0), a.get(0));
        });

        return scc;
    }

    public static int countReachableNodes(int[][] g, int start) {
        boolean[] v = new boolean[g.length];
        reachDfs(g, start, v);
        int count = 0;
        for (boolean b : v) if (b) count++;
        return count;
    }

    public static List<Integer> findBridgeNodes(int[][] g) {
    List<List<Integer>> scc = stronglyConnectedComponents(g);

    // Si plusieurs composantes ont taille > 1 ---> aucun nœud pont
    int countBig = 0;
    for (List<Integer> comp : scc) {
        if (comp.size() > 1) countBig++;
    }
    if (countBig > 1) return new ArrayList<>();

    // Sinon : nœuds qui ne sont pas dans la dernière CFC
    List<Integer> last = scc.get(scc.size() - 1);
    Set<Integer> keep = new HashSet<>(last);

    List<Integer> res = new ArrayList<>();
    for (int i = 0; i < g.length; i++) {
        if (!keep.contains(i)) res.add(i);
    }

    Collections.sort(res);
    return res;
}

    public static Map<Integer, Integer> getFinishingTimes(int[][] g) {
    // LinkedHashMap pour garder les clés dans l'ordre d'insertion (0,1,2,3,…)
    Map<Integer, Integer> times = new LinkedHashMap<>();
    boolean[] vis = new boolean[g.length];
    int[] time = {0}; // compteur global

    // On fait le DFS dans l'ordre 0,1,2,3 comme dans l'énoncé
    for (int i = 0; i < g.length; i++) {
        if (!vis[i]) {
            timeDfs(g, i, vis, times, time);
        }
    }

    return times;
}



    public static boolean canInstallAll(int[][] g, List<Integer> broken) {
        if (hasCycle(g)) return false;
        Set<Integer> set = new HashSet<>(broken);
        for (int i = 0; i < g.length; i++)
            if (!set.contains(i))
                for (int d : g[i])
                    if (set.contains(d)) return false;
        return true;
    }

    public static List<Integer> findMinimalDependencySet(int[][] g, int target) {
    Set<Integer> deps = new HashSet<>();
    getDeps(g, target, deps);   // Récupère toutes les dépendances
    List<Integer> out = new ArrayList<>(deps);
    Collections.sort(out);
    return out;
}

    public static int longestDependencyChain(int[][] g) {
    int n = g.length;
    if (n == 0) return 0;

    boolean[] onPath = new boolean[n];
    int max = 0;

    for (int i = 0; i < n; i++) {
        int len = longestDfs(g, i, onPath);
        if (len > max) max = len;
    }
    return max;
}
   public static List<Integer> findAllSourceNodes(int[][] g) {
    int n = g.length;
    int[] indegree = new int[n];

    for (int i = 0; i < n; i++)
        for (int v : g[i])
            indegree[v]++;

    List<Integer> sources = new ArrayList<>();
    for (int i = 0; i < n; i++)
        if (indegree[i] == 0)
            sources.add(i);

    return sources;
}
}