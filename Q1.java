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

    private static void timeDfs(int[][] g, int n, boolean[] v, Map<Integer, Integer> t, int[] c) {
        v[n] = true;
        for (int adj : g[n])
            if (!v[adj]) timeDfs(g, adj, v, t, c);
        t.put(n, ++c[0]);
    }

    private static void getDeps(int[][] g, int n, Set<Integer> d) {
        for (int v : g[n])
            if (d.add(v)) getDeps(g, v, d);
    }

    private static int longestDfs(int[][] g, int n, int[] memo) {
        if (memo[n] != -1) return memo[n];
        int best = 0;
        for (int v : g[n])
            best = Math.max(best, longestDfs(g, v, memo));
        return memo[n] = best + 1;
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

    // retirer la dernière CFC (= celle contenant le plus petit élément)
    List<Integer> res = new ArrayList<>();

    for (int i = 0; i < scc.size() - 1; i++) {
        res.addAll(scc.get(i));
    }

    Collections.sort(res);
    return res;
}

    public static Map<Integer, Integer> getFinishingTimes(int[][] g) {
        Map<Integer, Integer> times = new HashMap<>();
        boolean[] vis = new boolean[g.length];
        int[] counter = {0};
        for (int i = 0; i < g.length; i++)
            if (!vis[i]) timeDfs(g, i, vis, times, counter);
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
        int[] memo = new int[g.length];
        Arrays.fill(memo, -1);
        int best = 0;
        for (int i = 0; i < g.length; i++)
            best = Math.max(best, longestDfs(g, i, memo));
        return best;
    }

    public static List<Integer> findAllSourceNodes(int[][] g) {
        int[] indegree = new int[g.length];
        for (int i = 0; i < g.length; i++)
            for (int v : g[i]) indegree[v]++;
        List<Integer> out = new ArrayList<>();
        for (int i = 0; i < g.length; i++)
            if (indegree[i] == 0) out.add(i);
        return out;
    }
}