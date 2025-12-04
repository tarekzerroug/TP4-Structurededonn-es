/*
 * A T T E S T A T I O N   D ’ I N T É G R I T É   A C A D É M I Q U E
 *
 * [ ] Je certifie n’avoir utilisé aucun outil d’IA générative
 *     pour résoudre ce problème.
 *
 * [ oui] J’ai utilisé un ou plusieurs outils d’IA générative.
 *     Détails ci-dessous :
 *
 * Outil(s) utilisé(s) :
 * chat gpt 
 *
 * Raison(s) de l’utilisation :
 * écriture d'une partie de la documentaiton / commentaire , 
 * comprèhension du probleme 
 *
 * 
 *
 * Parties du code affectées :
 * 
 *Aucune
 * 
 */








import java.util.*;

public class Q3 {

    static final int MOUSE_WIN = 1;
    static final int CAT_WIN = 2;
    static final int DRAW = 0;

    static Map<String,Integer> memo;

    /* ============================================================
       CORE: evaluate() + solve()
       ============================================================ */

    public static int evaluate(int[][] g, int mouse, int cat, boolean mouseTurn) {
        memo = new HashMap<>();
        return solve(g, mouse, cat, mouseTurn ? 1 : 2, 0);
    }

    private static int solve(int[][] g, int m, int c, int turn, int depth) {
        // Limite anti-boucles
        if (depth > 4 * g.length * g.length)
            return DRAW;

        String key = m + "," + c + "," + turn;
        if (memo.containsKey(key))
            return memo.get(key);

        // États terminaux
        if (m == 0) return MOUSE_WIN;
        if (m == c) return CAT_WIN;

        int result;

        if (turn == 1) {  // souris
            result = CAT_WIN;  // pessimiste
            for (int nxt : g[m]) {
                int r = solve(g, nxt, c, 2, depth + 1);
                if (r == MOUSE_WIN) { result = MOUSE_WIN; break; }
                if (r == DRAW) result = DRAW;
            }
        } else {  // chat
            result = MOUSE_WIN;
            for (int nxt : g[c]) {
                if (nxt == 0) continue; // chat interdit au refuge
                int r = solve(g, m, nxt, 1, depth + 1);
                if (r == CAT_WIN) { result = CAT_WIN; break; }
                if (r == DRAW) result = DRAW;
            }
        }

        memo.put(key, result);
        return result;
    }

    /* ============================================================
       PARTIE PUBLIQUE (demandée dans le TP)
       ============================================================ */

    // 1. Résultat du jeu (souris commence au nœud 1, chat au nœud 2)
    public static int gameResult(int[][] graph) {
        return evaluate(graph, 1, 2, true);
    }

    // 2. Tous les mouvements initiaux gagnants de la souris
    public static List<Integer> getMouseWinningMoves(int[][] graph) {
        List<Integer> out = new ArrayList<>();
        for (int nxt : graph[1]) {
            int r = evaluate(graph, nxt, 2, false);
            if (r == MOUSE_WIN) out.add(nxt);
        }
        return out;
    }

    // 3. Nombre minimum de coups pour que player (=1 souris ou 2 chat) gagne
    public static int minMovesToWin(int[][] graph, int player) {
        int limit = 4 * graph.length * graph.length;

        for (int k = 1; k <= limit; k++) {
            memo = new HashMap<>();
            int r = solve(graph, 1, 2, 1, k);
            if (r == player)
                return k;
        }
        return -1;
    }

    // 4. Résultat d’un état arbitraire
    public static int gameResultFrom(int[][] graph, int mousePos, int catPos, boolean mouseTurn) {
        return evaluate(graph, mousePos, catPos, mouseTurn);
    }

    // 5. Toutes les positions (m, c, turn) menant à un match nul
    public static List<String> getAllDrawPositions(int[][] graph) {
        List<String> out = new ArrayList<>();
        for (int m = 1; m < graph.length; m++) {
            for (int c = 1; c < graph.length; c++) {
                if (c == 0) continue;
                for (int t = 0; t < 2; t++) {
                    int r = evaluate(graph, m, c, t == 1);
                    if (r == DRAW)
                        out.add(m + "," + c + "," + t);
                }
            }
        }
        return out;
    }

    // 6. Analyse complète : map (m,c,t) → résultat {0,1,2}
    public static Map<String, Integer> analyzeAllPositions(int[][] graph) {
        Map<String, Integer> out = new HashMap<>();
        for (int m = 1; m < graph.length; m++) {
            for (int c = 1; c < graph.length; c++) {
                if (c == 0) continue;
                for (int t = 0; t < 2; t++) {
                    int r = evaluate(graph, m, c, t == 1);
                    out.put(m + "," + c + "," + t, r);
                }
            }
        }
        return out;
    }

    // 7. Combien de positions sont gagnantes pour un joueur (1 ou 2)
    public static int countWinningPositions(int[][] graph, int player) {
        int cnt = 0;
        Map<String,Integer> mp = analyzeAllPositions(graph);
        for (int v : mp.values())
            if (v == player) cnt++;
        return cnt;
    }

    // 8. findSafeNodes : nœuds où la souris gagne contre TOUTES les positions du chat
    public static List<Integer> findSafeNodes(int[][] graph) {
    List<Integer> safe = new ArrayList<>();

    for (int m = 1; m < graph.length; m++) {

        boolean ok = true;

        for (int c = 1; c < graph.length; c++) {

            if (c == 0) continue;     // le chat ne peut pas être dans le refuge
            if (c == m) continue;     // position invalide : chat déjà sur la souris

            // Souris commence
            int r1 = evaluate(graph, m, c, true);

            // Chat commence
            int r2 = evaluate(graph, m, c, false);

            if (r1 != MOUSE_WIN || r2 != MOUSE_WIN) {
                ok = false;
                break;
            }
        }

        if (ok) safe.add(m);
    }

    return safe;
}
}