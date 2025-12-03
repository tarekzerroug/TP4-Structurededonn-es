import java.util.*;

public class Q2 {

    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private class RBNode {
        int key;
        String value;
        boolean isRed;
        int accessCount;
        long lastAccessTime;
        RBNode left, right, parent;

        public RBNode(int key, String value, long timestamp) {
            this.key = key;
            this.value = value;
            this.isRed = RED;
            this.accessCount = 1;
            this.lastAccessTime = timestamp;
        }
    }

    private RBNode root;

    public Q2() {
        this.root = null;
    }

    public void insert(int key, String value, long timestamp) {
        if (root == null) {
            root = new RBNode(key, value, timestamp);
            root.isRed = false;
            return;
        }

        RBNode newNode = root;
        RBNode parent = null;

        while (newNode != null) {
            parent = newNode;
            if (key < newNode.key) {
                newNode = newNode.left;
            } else if (key > newNode.key) {
                newNode = newNode.right;
            } else {
                newNode.value = value;
                newNode.accessCount++;
                newNode.lastAccessTime = timestamp;
                return;
            }
        }
        newNode = new RBNode(key, value, timestamp);
        newNode.isRed = true;
        newNode.parent = parent;

        if (key < parent.key) {
            parent.left = newNode;
        } else {
            parent.right = newNode;
        }
        
        fixInsert(newNode);
    }

    private boolean isRight(RBNode node) {
        return node.parent != null && node == node.parent.right;
    }


    private void fixInsert(RBNode node) {
        RBNode parent = node.parent;
        if (parent.parent == null) return;
        RBNode grandParent = parent.parent;

        RBNode uncle = null;

        if (!isRight(parent)) {
            uncle = grandParent.right;
        } else {
            uncle = grandParent.left;
        }

        if (parent.isRed) {
            if (uncle == null || !uncle.isRed) {
                if (!isRight(node)) {
                    if (!isRight(parent)) {
                        rotateRight(grandParent);
                    }
                    else {
                        rotateRight(parent);
                        rotateLeft(grandParent);
                    }
                }
                else {
                    if (!isRight(parent)) {
                        rotateLeft(parent);
                        rotateRight(grandParent);
                    }
                    else {
                        rotateLeft(grandParent);
                        grandParent.isRed = true;
                        parent.isRed = false;
                    }                    
                }
                grandParent.isRed = true;
                parent.isRed = false;
            }
            else {
                parent.isRed = false;
                uncle.isRed = false;
                grandParent.isRed = true;
                root.isRed = false;
                fixInsert(parent);
            }
        }

        root.isRed = false;
    }

    private void rotateRight(RBNode grandParent) {
        RBNode parent = grandParent.left;          
        RBNode temp = parent.right;                

        if(grandParent == root) {
            root = parent;
            parent.parent = null;
        } else {
            RBNode greatGrandParent = grandParent.parent;
            if(!isRight(grandParent)) greatGrandParent.left = parent;
            else greatGrandParent.right = parent;
            parent.parent = greatGrandParent;
        }

        parent.right = grandParent;
        grandParent.parent = parent;

        grandParent.left = temp;
        if(temp != null) temp.parent = grandParent;
    }

    private void rotateLeft(RBNode grandParent) {
        RBNode parent = grandParent.right;
        RBNode temp = parent.left;

        if (grandParent == root) {
            root = parent;
            parent.parent = null;
        } else {
            RBNode gp = grandParent.parent;
            if (grandParent == gp.left) gp.left = parent;
            else gp.right = parent;
            parent.parent = gp;
        }

        parent.left = grandParent;
        grandParent.parent = parent;

        grandParent.right = temp;
        if (temp != null) temp.parent = grandParent;
    }


    public String get(int key, long timestamp) {
        // TODO:
        return null;
    }

    public boolean delete(int key) {
        // TODO:
        return false;
    }

    public List<String> getRangeValues(int minKey, int maxKey) {
        // TODO:
        return new ArrayList<>();
    }

    public int getBlackHeight() {
        // TODO:
        return 0;
    }

    public boolean verifyProperties() {
        // TODO:
        return false;
    }

    public List<Integer> getMostAccessedKeys(int k) {
        // TODO:
        return new ArrayList<>();
    }

    public void evictOldEntries(long newNodeTime, long maxAge) {
        // TODO:
    }

    public int countRedNodes() {
        // TODO:
        return 0;
    }

    public Map<String, Integer> getColorStatisticsByLevel() {
        // TODO:
        return new HashMap<>();
    }

}
