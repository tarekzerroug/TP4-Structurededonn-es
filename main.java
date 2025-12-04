import java.util.*;

public class main {

    public static void main(String[] args) {

        System.out.println("========== TESTS ADDITIONNELS Q1 ==========\n");

        /* ============================================================
           TEST 1 : DAG simple
           graphA = [
               [1,2],
               [3],
               [3],
               []
           ]
           Expected :
               hasCycle = false
               SCC = [[3],[1],[2],[0]]
               countReachable(0) = 4
               sources = [0]
         ============================================================ */
        int[][] graphA = {
            {1,2},
            {3},
            {3},
            {}
        };

        System.out.println("=== Test 1 : DAG simple ===");
        System.out.println("hasCycle(graphA) → " + Q1.hasCycle(graphA));      // expected: false
        System.out.println("SCC(graphA) → " + Q1.stronglyConnectedComponents(graphA)); // expected: [[3],[1],[2],[0]]
        System.out.println("countReachable(0) → " + Q1.countReachableNodes(graphA,0)); // expected: 4
        System.out.println("findBridgeNodes → " + Q1.findBridgeNodes(graphA));         // expected: [0,1,2] or []
        System.out.println("longestDependencyChain → " + Q1.longestDependencyChain(graphA)); // expected: 3 or 4 depending on def
        System.out.println("sources → " + Q1.findAllSourceNodes(graphA));              // expected: [0]
        System.out.println();


        /* ============================================================
           TEST 2 : Graphe avec cycle long
           graphB = [
               [1],
               [2],
               [3],
               [0]
           ]
           Expected :
               hasCycle = true
               SCC = [[0,1,2,3]]
               reachable(0) = 4
               sources = []   (car tous ont indegree > 0)
         ============================================================ */
        int[][] graphB = {
            {1},
            {2},
            {3},
            {0}
        };

        System.out.println("=== Test 2 : Cycle complet (0→1→2→3→0) ===");
        System.out.println("hasCycle → " + Q1.hasCycle(graphB));                        // expected: true
        System.out.println("SCC → " + Q1.stronglyConnectedComponents(graphB));          // expected: [[0,1,2,3]]
        System.out.println("countReachableNodes(0) → " + Q1.countReachableNodes(graphB,0)); // expected: 4
        System.out.println("findBridgeNodes → " + Q1.findBridgeNodes(graphB));          // expected: []
        System.out.println("findAllSourceNodes → " + Q1.findAllSourceNodes(graphB));    // expected: []
        System.out.println();


        /* ============================================================
           TEST 3 : Dengraph mixte DAG + cycle
           graphC = [
               [1],
               [2],
               [0],
               [1,4],
               []
           ]
           Expected :
               cycle : [0,1,2]
               SCC = [[0,1,2],[3],[4]]
               sources : [3]
         ============================================================ */
        int[][] graphC = {
            {1},
            {2},
            {0},
            {1,4},
            {}
        };

        System.out.println("=== Test 3 : Mixte cycle + DAG ===");
        System.out.println("hasCycle → " + Q1.hasCycle(graphC));                         // true
        System.out.println("cycles → " + Q1.findAllCycles(graphC));                      // expected: [0,1,2]
        System.out.println("SCC → " + Q1.stronglyConnectedComponents(graphC));           // expected: [[0,1,2],[3],[4]]
        System.out.println("findAllSourceNodes → " + Q1.findAllSourceNodes(graphC));     // expected: [3]
        System.out.println();


        /* ============================================================
           TEST 4 : Finishing times
           graphD = [
               [1],
               [],
               [3],
               []
           ]
           Expected DFS order from 0:
               finish times ≈ {1:2,0:3,3:6,2:7}
         ============================================================ */
        int[][] graphD = {
            {1},
            {},
            {3},
            {}
        };

        System.out.println("=== Test 4 : Finishing times ===");
        System.out.println("getFinishingTimes → " + Q1.getFinishingTimes(graphD));
        System.out.println();


        /* ============================================================
           TEST 5 : Minimal dependency set
           graphE = [
               [1],
               [2],
               []
           ]
           target = 0 → deps = [1,2]
         ============================================================ */
        int[][] graphE = {
            {1},
            {2},
            {}
        };

        System.out.println("=== Test 5 : Minimal dependencies ===");
        System.out.println("findMinimalDependencySet(graphE,0) → " + Q1.findMinimalDependencySet(graphE,0)); // expected: [1,2]
        System.out.println();


        System.out.println("========== TESTS ADDITIONNELS Q3 ==========\n");

        /* ============================================================
           TEST Q3-A : Souris gagne immédiatement
           graphF :
               0 = refuge
               1 → 0 (mouse wins instantly)
               2 → 1
         ============================================================ */
        int[][] graphF = {
            {},     // 0
            {0},    // 1
            {1}     // 2
        };

        System.out.println("=== Test Q3-A : Mouse immediate win ===");
        System.out.println("gameResult → " + Q3.gameResult(graphF));      // expected: 1
        System.out.println("mouseWinningMoves → " + Q3.getMouseWinningMoves(graphF)); // expected: [0]
        System.out.println("safeNodes → " + Q3.findSafeNodes(graphF));    // expected: [1]
        System.out.println();


        /* ============================================================
           TEST Q3-B : Chat gagne immédiatement
         ============================================================ */
        int[][] graphG = {
            {},        // 0
            {2},       // 1
            {1}        // 2
        };
        // Si souris =1, chat=2 → chat adjacent → cat wins

        System.out.println("=== Test Q3-B : Cat immediate win ===");
        System.out.println("gameResult → " + Q3.gameResult(graphG)); // expected: 2
        System.out.println("safeNodes → " + Q3.findSafeNodes(graphG)); // expected: []
        System.out.println();


        /* ============================================================
           TEST Q3-C : Draw infinite loop
           graphH :
               1 → 2
               2 → 1
               cat never catches mouse → DRAW
         ============================================================ */
        int[][] graphH = {
            {},        // 0
            {2},       // 1
            {1}        // 2
        };

        System.out.println("=== Test Q3-C : Draw expected ===");
        System.out.println("gameResult → " + Q3.gameResult(graphH)); // expected: 0
        System.out.println("getAllDrawPositions → " + Q3.getAllDrawPositions(graphH));
        System.out.println();


        System.out.println("=============== END ===============");
    }
}