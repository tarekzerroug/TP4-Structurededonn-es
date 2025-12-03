import java.util.Arrays;
import java.util.List;

public class main {
    public static void main(String[] args) {

        // Graphe demandé
        int[][] graph = {
            {},     // 0 → 1
            {},     // 1 → 2
            {0},     // 2 → 3
            {0,1},     // 3 → 4
            {2,3}       // 4 → personne
        };

        System.out.println("hasCycle(graph) = " + Q1.hasCycle(graph));

        System.out.println("stronglyConnectedComponents(graph) = " +
            Q1.stronglyConnectedComponents(graph));

        System.out.println("countReachableNodes(graph, 4) = " +
            Q1.countReachableNodes(graph, 4));

        System.out.println("findBridgeNodes(graph) = " +
            Q1.findBridgeNodes(graph));

        System.out.println("longestDependencyChain(graph) = " +
            Q1.longestDependencyChain(graph));

        System.out.println("findMinimalDependencySet(graph, 4) = " +
            Q1.findMinimalDependencySet(graph, 4));

        System.out.println("findAllSourceNodes(graph) = " +
            Q1.findAllSourceNodes(graph));
   
        int[][] graph2 = {
            {1},    // 0 → 1
            {2},    // 1 → 2
            {0},    // 2 → 0
            {1, 2}  // 3 → 1 et 2
        };

        System.out.println("------------------------------" );
        System.out.println("===== Exemple 2 : Graphe avec cycle =====");

        System.out.println("hasCycle(graph) -> " + Q1.hasCycle(graph2));

        System.out.println("findAllCycles(graph) -> " + Q1.findAllCycles(graph2));

        System.out.println("stronglyConnectedComponents(graph) -> " + Q1.stronglyConnectedComponents(graph2));

        System.out.println("countReachableNodes(graph, 3) -> " + Q1.countReachableNodes(graph2, 3));

        System.out.println("getFinishingTimes(graph) -> " + Q1.getFinishingTimes(graph2));

        List<Integer> broken = Arrays.asList(1);
        System.out.println("canInstallAll(graph, [1]) -> " + Q1.canInstallAll(graph2, broken));

        System.out.println("findAllSourceNodes(graph) -> " + Q1.findAllSourceNodes(graph2));
          // Exemple 3 : graph = [[1],[2],[0],[4],[3,5],[6],[5]]
        int[][] graph3 = {
            {1},     // 0
            {2},     // 1
            {0},     // 2
            {4},     // 3
            {3, 5},  // 4
            {6},     // 5
            {5}      // 6
        };

        System.out.println("=== Exemple 3 : Composantes complexes ===");

        System.out.println("hasCycle(graph) -> " +
                Q1.hasCycle(graph3));                    // attendu : true

        System.out.println("stronglyConnectedComponents(graph) -> " +
                Q1.stronglyConnectedComponents(graph3)); // attendu : [[0,1,2], [5,6], [3,4]]

        System.out.println("findBridgeNodes(graph) -> " +
                Q1.findBridgeNodes(graph3));             // attendu : []

        System.out.println("longestDependencyChain(graph) -> " +
                Q1.longestDependencyChain(graph3));      // attendu : 4

        System.out.println("findAllSourceNodes(graph) -> " +
                Q1.findAllSourceNodes(graph3));    
   
        }
}