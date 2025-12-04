/*
 * A T T E S T A T I O N   D ' I N T É G R I T É   A C A D É M I Q U E
 *
 * [ ] Je certifie n'avoir utilisé aucun outil d'IA générative
 *     pour résoudre ce problème.
 *
 * [ oui] J'ai utilisé un ou plusieurs outils d'IA générative.
 *     Détails ci-dessous :
 *
 * Outil(s) utilisé(s) :
 * chat gpt, Claude 
 *
 * Raison(s) de l'utilisation :
 * Tests, 
 * commentaires
 *
 * 
 *
 * Parties du code affectées :
 * 
 * Aucune
 * 
 */



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
            this.isRed = true;
            this.accessCount = 1;
            this.lastAccessTime = timestamp;
        }
    }

    private RBNode root;

    public Q2() {
        this.root = null;
    }

    // Insère une nouvelle paire clé-valeur dans l'arbre
    // Met à jour si la clé existe déjà
    public void insert(int key, String value, long timestamp) {
        if (root == null) {
            root = new RBNode(key, value, timestamp);
            root.isRed = false;  // La racine doit être noire
            return;
        }

        RBNode newNode = root;
        RBNode parent = null;

        // Recherche de la position d'insertion (comme un ABR standard)
        while (newNode != null) {
            parent = newNode;
            if (key < newNode.key) {
                newNode = newNode.left;
            } else if (key > newNode.key) {
                newNode = newNode.right;
            } else {
                // Clé existe déjà, mise à jour
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

    // Vérifie si un nœud est l'enfant droit de son parent
    private boolean isRight(RBNode node) {
        return node.parent != null && node == node.parent.right;
    }

    // Corrige les violations des propriétés rouge-noir après insertion
    // Gère les cas de double rouge
    private void fixInsert(RBNode node) {
        while (node != root && node.isRed && node.parent.isRed) {
            RBNode parent = node.parent;
            RBNode grandParent = parent.parent;

            RBNode uncle;
            if (!isRight(parent)) {
                uncle = grandParent.right;
            } else {
                uncle = grandParent.left;
            }

            if (parent.isRed) {
                // Cas 1 : Oncle noir - restructuration nécessaire
                if (uncle == null || !uncle.isRed) {
                    RBNode newRoot;
                    if (!isRight(node)) {
                        if (!isRight(parent)) {
                            newRoot = rotateRight(grandParent);
                        } else {
                            rotateLeft(parent);
                            newRoot = rotateRight(grandParent);
                        }
                    } else {
                        if (!isRight(parent)) {
                            rotateRight(parent);
                            newRoot = rotateLeft(grandParent);
                        } else {
                            newRoot = rotateLeft(grandParent);
                        }
                    }
                    newRoot.isRed = false;
                    if (newRoot.left != null) newRoot.left.isRed = true;
                    if (newRoot.right != null) newRoot.right.isRed = true;
                } else {
                    // Cas 2 : Oncle rouge - recoloration
                    parent.isRed = false;
                    uncle.isRed = false;
                    grandParent.isRed = true;
                    root.isRed = false;
                    fixInsert(grandParent); 
                }
            }
        }

        root.isRed = false;
    }

    // Effectue une rotation simple à droite
    // Utilisée pour rééquilibrer l'arbre
    private RBNode rotateRight(RBNode grandParent) {
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

        return parent;
    }

    // Effectue une rotation simple à gauche
    // Utilisée pour rééquilibrer l'arbre
    private RBNode rotateLeft(RBNode grandParent) {
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

        return parent;
    }

    // Recherche et retourne la valeur associée à une clé
    // Met à jour les statistiques d'accès
    public String get(int key, long timestamp) {
        RBNode current = root;

        while (current != null) {
            if (key == current.key) {
                current.accessCount++;
                current.lastAccessTime = timestamp;
                return current.value;
            }
            if (key < current.key) {
                current = current.left;
            }
            else {
                current = current.right;
            }
        }
        return null;
    }

    // Supprime un nœud de l'arbre tout en maintenant les propriétés rouge-noir
    public boolean delete(int key) {
        RBNode current = root;
        RBNode node = null;

        // Recherche du nœud à supprimer
        while (current != null) {
            if (key == current.key) {
                node = current;
                break;
            }
            if (key < current.key) {
                current = current.left;
            }
            else {
                current = current.right;
            }
        }

        if (node == null){
            return false;
        }

        // Cas 1 : Nœud feuille
        if(node.left == null && node.right == null) {
            if (node.isRed) {
                if (node == root) {
                    root = null;
                } else if (isRight(node)) {
                    node.parent.right = null;
                } else {
                    node.parent.left = null;
                }
                return true;
            }
            else {
                if (node == root) {
                    root = null;
                    return true;
                }
                fixDelete(node);
                if (isRight(node)) {
                    node.parent.right = null;
                } else {
                    node.parent.left = null;
                }
                return true;
            }
        }
        // Cas 2 : Nœud avec un seul enfant
        else if (node.left == null ^ node.right == null) {
            RBNode child;
            if (node.left == null) {
                child = node.right;
            } else {
                child = node.left;
            }
            if(node == root) {
                root = child;
                if (child != null) {
                    child.parent = null;
                    child.isRed = false;
                }
                return true;
            }
            else if (isRight(node)) {
                node.parent.right = child;
                child.parent = node.parent;
            }
            else {
                node.parent.left = child;
                child.parent = node.parent;
            }
            
            if (!node.isRed && child.isRed) {
                child.isRed = false;
            }
            
            return true;
        }
        // Cas 3 : Nœud avec deux enfants
        else {
            // Trouver le prédécesseur (maximum du sous-arbre gauche)
            RBNode predecessor = node.left;
            while (predecessor.right != null) {
                predecessor = predecessor.right;
            }
            
            // Copier les données du prédécesseur        
            node.key = predecessor.key;
            node.value = predecessor.value;
            node.accessCount = predecessor.accessCount;
            node.lastAccessTime = predecessor.lastAccessTime;
            
            if (predecessor.left == null) {
                if (predecessor.isRed) {
                    if (predecessor.parent == node) {
                        node.left = null;
                    } else {
                        predecessor.parent.right = null;
                    }
                } else {
                    fixDelete(predecessor);
                    if (predecessor.parent == node) {
                        node.left = null;
                    } else {
                        predecessor.parent.right = null;
                    }
                }
            } else {
                RBNode child = predecessor.left;
                if (predecessor.parent == node) {
                    node.left = child;
                } else {
                    predecessor.parent.right = child;
                }
                child.parent = predecessor.parent;
                
                if (!predecessor.isRed && child.isRed) {
                    child.isRed = false;
                }
            }
            
            return true;
        }
    }

    // Corrige les violations des propriétés rouge-noir après suppression
    // Gère les cas de double noir
    private void fixDelete(RBNode node) {
        while (node != root) {
            RBNode parent = node.parent;
            RBNode sibling;
            boolean nodeIsRight = isRight(node);
            
            if (nodeIsRight) {
                sibling = parent.left;
            } else {
                sibling = parent.right;
            }
            
            // Cas 3 : Frère rouge
            if (sibling != null && sibling.isRed) {
                sibling.isRed = false;
                parent.isRed = true;
                if (nodeIsRight) {
                    rotateRight(parent);
                    sibling = parent.left;
                } else {
                    rotateLeft(parent);
                    sibling = parent.right;
                }
            }
            
            if (sibling == null) {
                node = parent;
                continue;
            }
            
            boolean siblingLeftRed = (sibling.left != null && sibling.left.isRed);
            boolean siblingRightRed = (sibling.right != null && sibling.right.isRed);
            
            // Cas 2 : Frère noir avec deux enfants noirs
            if (!siblingLeftRed && !siblingRightRed) {
                sibling.isRed = true;
                if (parent.isRed) {
                    parent.isRed = false;
                    break;
                } else {
                    node = parent;
                    continue;
                }
            }
            
            // Cas 1 : Frère noir avec au moins un enfant rouge
            if (nodeIsRight) {
                if (siblingLeftRed) {
                    RBNode newRoot = rotateRight(parent);
                    newRoot.isRed = parent.isRed;
                    parent.isRed = false;
                    if (newRoot.left != null) newRoot.left.isRed = false;
                } else {
                    sibling.right.isRed = false;
                    sibling.isRed = true;
                    rotateLeft(sibling);
                    RBNode newRoot = rotateRight(parent);
                    newRoot.isRed = parent.isRed;
                    parent.isRed = false;
                }
            } else {
                if (siblingRightRed) {
                    RBNode newRoot = rotateLeft(parent);
                    newRoot.isRed = parent.isRed;
                    parent.isRed = false;
                    if (newRoot.right != null) newRoot.right.isRed = false;
                } else {
                    sibling.left.isRed = false;
                    sibling.isRed = true;
                    rotateRight(sibling);
                    RBNode newRoot = rotateLeft(parent);
                    newRoot.isRed = parent.isRed;
                    parent.isRed = false;
                }
            }
            break;
        }
    }

    // Retourne toutes les valeurs dont les clés sont dans l'intervalle [minKey, maxKey]
    public List<String> getRangeValues(int minKey, int maxKey) {
        ArrayList<String> values = new ArrayList<>();
        RBNode current = root;
        addToList(current, values, minKey, maxKey);
        return values;
    }

    // Parcours infixe pour ajouter les valeurs dans l'intervalle à la liste
    private void addToList(RBNode node, List<String> values, int minKey, int maxKey) {
        if (node == null) return;
        if (node.key < minKey) {
            addToList(node.right, values, minKey, maxKey);
            return;
        }
        addToList(node.left, values, minKey, maxKey);
        values.add(node.value);
        addToList(node.right, values, minKey, maxKey);
    }

    // Calcule la hauteur noire de l'arbre (nombre de nœuds noirs sur un chemin racine-feuille)
    public int getBlackHeight() {
        return heightHelp(root);
    }

    // Fonction récursive pour calculer la hauteur noire
    int heightHelp(RBNode node) {
        if (node == null) {
            return 0;
        }
        if (node.isRed) {
            return Math.max(heightHelp(node.left), heightHelp(node.right));
        }
        return 1 + Math.max(heightHelp(node.left), heightHelp(node.right));
    }

    // Vérifie que l'arbre respecte toutes les propriétés rouge-noir
    public boolean verifyProperties() {
        if (root.isRed) {
            return false;
        }

        return verifyDoubleRed(root) && verifyBlackHeight(root) != 0;
    }

    // Vérifie qu'il n'y a pas de double rouge (nœud rouge avec enfant rouge)
    boolean verifyDoubleRed(RBNode node) {
        if (node == null) {
            return true;
        }

        if (node.isRed) {
            if (node.parent != null && node.parent.isRed) {
                return false;
            }
        }

        return verifyDoubleRed(node.left) && verifyDoubleRed(node.right);
    }

    // Vérifie que tous les chemins ont la même hauteur noire
    int verifyBlackHeight(RBNode node) {
        if (node == null) {
            return 1;
        }

        if (verifyBlackHeight(node.left) == 0){
            return 0;
        }

        if (verifyBlackHeight(node.right) == 0){
            return 0;
        }

        if (verifyBlackHeight(node.left) != verifyBlackHeight(node.right)) {
            return 0;
        }

        if (node.isRed) return verifyBlackHeight(node.left);
        return 1 + verifyBlackHeight(node.left);
    }

    // Retourne les k clés les plus accédées en ordre décroissant de fréquence
    public List<Integer> getMostAccessedKeys(int k) {
        List<RBNode> top = new ArrayList<>();
        allNodes(root, top);
        top.sort((a,b) -> Integer.compare(b.accessCount, a.accessCount));
        List<Integer> keys = new ArrayList<>();
        for (RBNode n : top) {
            if (top.indexOf(n) >= k) {
                break;
            }
            keys.add(n.key);
        }
        return keys;
    }

    // Ajoute tous les nœuds de l'arbre à une liste (parcours préfixe)
    void allNodes(RBNode node, List<RBNode> list) {
        if (node == null) {
            return;
        }
        list.add(node);
        allNodes(node.left, list);
        allNodes(node.right, list);
    }

    // Supprime toutes les entrées dont lastAccessTime est trop ancien
    public void evictOldEntries(long newNodeTime, long maxAge) {
        List<RBNode> nodes = new ArrayList<>();
        allNodes(root, nodes);
        for(RBNode node : nodes) {
            if (newNodeTime - node.lastAccessTime > maxAge) {
                delete(node.key);
            }
        }
    }

    // Compte le nombre total de nœuds rouges dans l'arbre
    public int countRedNodes() {
        return countRedRecursion(root);
    }

    // Fonction récursive pour compter les nœuds rouges
    int countRedRecursion(RBNode node) {
        if (node == null) return 0;
        if (node.isRed) return 1 + countRedRecursion(node.left) + countRedRecursion(node.right);
        return countRedRecursion(node.left) + countRedRecursion(node.right);
    }

    // Retourne le nombre de nœuds rouges par niveau de l'arbre
    // Utilise un parcours en largeur (BFS)
    public Map<String, Integer> getColorStatisticsByLevel() {
        Map<String, Integer> stats = new HashMap<>();
        
        if (root == null) {
            return stats;
        }
        
        Queue<RBNode> queue = new LinkedList<>();
        queue.offer(root);
        int level = 0;
        
        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            int redCount = 0;
            
            for (int i = 0; i < levelSize; i++) {
                RBNode node = queue.poll();
                
                if (node.isRed) {
                    redCount++;
                }
                
                if (node.left != null) {
                    queue.offer(node.left);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                }
            }
            
            stats.put(String.valueOf(level), redCount);
            
            level++;
        }
        
        return stats;
    }

}