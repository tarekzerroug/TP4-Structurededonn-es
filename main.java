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
    }
}