public class Q2test {
    
    public static void main(String[] args) {
        System.out.println("=== RED-BLACK TREE CACHE TESTS ===\n");
        
        testInsertAndGet();
        testDelete();
        testRangeValues();
        testBlackHeight();
        testVerifyProperties();
        testMostAccessedKeys();
        testEvictOldEntries();
        //testCountRedNodes();
        //testColorStatisticsByLevel();
        
        System.out.println("\n=== ALL TESTS COMPLETED ===");
    }
    
    // Test 1: Insert and Get
    public static void testInsertAndGet() {
        System.out.println("--- Test 1: Insert and Get ---");
        Q2 cache = new Q2();
        
        cache.insert(10, "data10", 0);
        cache.insert(20, "data20", 0);
        cache.insert(30, "data30", 0);
        cache.insert(15, "data15", 0);
        cache.insert(25, "data25", 0);
        
        System.out.println("Get(15, 100): " + cache.get(15, 100)); // Should be "data15"
        System.out.println("Get(20, 100): " + cache.get(20, 100)); // Should be "data20"
        System.out.println("Get(99, 100): " + cache.get(99, 100)); // Should be null
        
        // Test updating existing key
        cache.insert(15, "updated15", 200);
        System.out.println("Get(15, 300) after update: " + cache.get(15, 300)); // Should be "updated15"
        
        System.out.println("✓ Test 1 completed\n");
    }
    
    // Test 2: Delete
    public static void testDelete() {
        System.out.println("--- Test 2: Delete ---");
        Q2 cache = new Q2();
        
        // Build tree
        cache.insert(20, "data20", 0);
        cache.insert(10, "data10", 0);
        cache.insert(30, "data30", 0);
        cache.insert(15, "data15", 0);
        cache.insert(25, "data25", 0);
        cache.insert(5, "data5", 0);
        
        System.out.println("Before delete - Get(15): " + cache.get(15, 0));
        System.out.println("Delete(15): " + cache.delete(15)); // Should be true
        System.out.println("After delete - Get(15): " + cache.get(15, 0)); // Should be null
        
        System.out.println("Delete(99): " + cache.delete(99)); // Should be false (not found)
        
        // Delete root
        System.out.println("Delete(20): " + cache.delete(20)); // Should be true
        System.out.println("After delete root - Get(20): " + cache.get(20, 0)); // Should be null
        
        System.out.println("✓ Test 2 completed\n");
    }
    
    // Test 3: Get Range Values
    public static void testRangeValues() {
        System.out.println("--- Test 3: Get Range Values ---");
        Q2 cache = new Q2();
        
        cache.insert(10, "data10", 0);
        cache.insert(20, "data20", 0);
        cache.insert(30, "data30", 0);
        cache.insert(15, "data15", 0);
        cache.insert(25, "data25", 0);
        cache.insert(5, "data5", 0);
        
        System.out.println("Range [12, 27]: " + cache.getRangeValues(12, 27));
        // Should be ["data15", "data20", "data25"] in sorted order
        
        System.out.println("Range [0, 100]: " + cache.getRangeValues(0, 100));
        // Should be all values in sorted order
        
        System.out.println("Range [100, 200]: " + cache.getRangeValues(100, 200));
        // Should be empty []
        
        System.out.println("✓ Test 3 completed\n");
    }
    
    // Test 4: Black Height
    public static void testBlackHeight() {
        System.out.println("--- Test 4: Black Height ---");
        Q2 cache = new Q2();
        
        cache.insert(20, "data20", 0);
        System.out.println("After 1 insert, Black Height: " + cache.getBlackHeight());
        
        cache.insert(10, "data10", 0);
        cache.insert(30, "data30", 0);
        System.out.println("After 3 inserts, Black Height: " + cache.getBlackHeight());
        
        cache.insert(15, "data15", 0);
        cache.insert(25, "data25", 0);
        cache.insert(5, "data5", 0);
        cache.insert(35, "data35", 0);
        System.out.println("After 7 inserts, Black Height: " + cache.getBlackHeight());
        
        System.out.println("✓ Test 4 completed\n");
    }
    
    // Test 5: Verify Properties
    public static void testVerifyProperties() {
        System.out.println("--- Test 5: Verify Properties ---");
        Q2 cache = new Q2();
        
        cache.insert(20, "data20", 0);
        cache.insert(10, "data10", 0);
        cache.insert(30, "data30", 0);
        cache.insert(15, "data15", 0);
        cache.insert(25, "data25", 0);
        
        System.out.println("Tree is valid RB tree: " + cache.verifyProperties());
        // Should be true
        
        // After deletion
        cache.delete(15);
        System.out.println("After deletion, still valid: " + cache.verifyProperties());
        // Should be true
        
        System.out.println("✓ Test 5 completed\n");
    }
    
    // Test 6: Most Accessed Keys
    public static void testMostAccessedKeys() {
        System.out.println("--- Test 6: Most Accessed Keys ---");
        Q2 cache = new Q2();
        
        cache.insert(10, "data10", 0);
        cache.insert(20, "data20", 0);
        cache.insert(30, "data30", 0);
        cache.insert(15, "data15", 0);
        cache.insert(25, "data25", 0);
        
        // Access some keys multiple times
        cache.get(15, 100);
        cache.get(15, 100);
        cache.get(15, 100); // 15 accessed 4 times total (1 insert + 3 gets)
        
        cache.get(20, 100);
        cache.get(20, 100); // 20 accessed 3 times total
        
        cache.get(25, 100); // 25 accessed 2 times total
        
        System.out.println("Top 3 most accessed: " + cache.getMostAccessedKeys(3));
        // Should be [15, 20, 25] in descending order of access
        
        System.out.println("Top 2 most accessed: " + cache.getMostAccessedKeys(2));
        // Should be [15, 20]
        
        System.out.println("✓ Test 6 completed\n");
    }
    
    // Test 7: Evict Old Entries
    public static void testEvictOldEntries() {
        System.out.println("--- Test 7: Evict Old Entries ---");
        Q2 cache = new Q2();
        
        cache.insert(10, "data10", 0);
        cache.insert(20, "data20", 50);
        cache.insert(30, "data30", 100);
        cache.insert(15, "data15", 150);
        cache.insert(25, "data25", 200);
        
        System.out.println("Before eviction - Get(10): " + cache.get(10, 300));
        System.out.println("Before eviction - Get(25): " + cache.get(25, 300));
        
        // Evict entries older than 150 time units (currentTime=300, maxAge=150)
        // Should remove 10 (lastAccess=0), 20 (lastAccess=50), 30 (lastAccess=100)
        cache.evictOldEntries(300, 150);
        
        System.out.println("After eviction - Get(10): " + cache.get(10, 300)); // Should be null
        System.out.println("After eviction - Get(20): " + cache.get(20, 300)); // Should be null
        System.out.println("After eviction - Get(30): " + cache.get(30, 300)); // Should be null
        System.out.println("After eviction - Get(15): " + cache.get(15, 300)); // Should exist
        System.out.println("After eviction - Get(25): " + cache.get(25, 300)); // Should exist
        
        System.out.println("✓ Test 7 completed\n");
    }
    
    // Test 8: Count Red Nodes
    public static void testCountRedNodes() {
        System.out.println("--- Test 8: Count Red Nodes ---");
        Q2 cache = new Q2();
        
        cache.insert(20, "data20", 0);
        System.out.println("After 1 insert, Red nodes: " + cache.countRedNodes());
        // Should be 0 (root is black)
        
        cache.insert(10, "data10", 0);
        cache.insert(30, "data30", 0);
        System.out.println("After 3 inserts, Red nodes: " + cache.countRedNodes());
        // Should be 2 (both children are red)
        
        cache.insert(15, "data15", 0);
        cache.insert(25, "data25", 0);
        System.out.println("After 5 inserts, Red nodes: " + cache.countRedNodes());
        
        System.out.println("✓ Test 8 completed\n");
    }
    
    // Test 9: Color Statistics By Level
    public static void testColorStatisticsByLevel() {
        System.out.println("--- Test 9: Color Statistics By Level ---");
        Q2 cache = new Q2();
        
        cache.insert(20, "data20", 0);
        cache.insert(10, "data10", 0);
        cache.insert(30, "data30", 0);
        cache.insert(15, "data15", 0);
        cache.insert(25, "data25", 0);
        
        var stats = cache.getColorStatisticsByLevel();
        System.out.println("Color statistics by level:");
        for (var entry : stats.entrySet()) {
            System.out.println("  Level " + entry.getKey() + ": " + entry.getValue() + " nodes");
        }
        
        System.out.println("✓ Test 9 completed\n");
    }
    
    // Bonus: Stress test
    public static void stressTest() {
        System.out.println("--- Stress Test ---");
        Q2 cache = new Q2();
        
        // Insert many values
        for (int i = 0; i < 100; i++) {
            cache.insert(i, "data" + i, i);
        }
        
        System.out.println("Inserted 100 nodes");
        System.out.println("Tree is valid: " + cache.verifyProperties());
        System.out.println("Black height: " + cache.getBlackHeight());
        System.out.println("Red nodes: " + cache.countRedNodes());
        
        // Delete half of them
        for (int i = 0; i < 50; i++) {
            cache.delete(i);
        }
        
        System.out.println("\nAfter deleting 50 nodes:");
        System.out.println("Tree is still valid: " + cache.verifyProperties());
        System.out.println("Black height: " + cache.getBlackHeight());
        
        System.out.println("✓ Stress test completed\n");
    }
}