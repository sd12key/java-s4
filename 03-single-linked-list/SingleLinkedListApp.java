import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

class Node<T> {
    // data and next fields are made private in order to encapsulate
    // using generics, so the class can hold any type of data
    private T data;
    private Node<T> next;

    // constructor
    public Node(T data) {
        this.data = data;
        this.next = null; // next is initialized to null by default
    }

    // added getters and setters
    // so no need to access fields directly
    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Node<T> getNext() {
        return this.next;
    }

    public void setNext(Node<T> next) {
        this.next = next;
    }
}

class SingleLinkedList<T> {
    // kept only head and size, I do not think we need tail at all
    // also these fields are private to encapsulate the data, accessing them only through methods
    private Node<T> head;
    private int size;

    // constructor, initializes head to null and size to 0
    public SingleLinkedList() {
        this.head = null;
        this.size = 0;
    }
    
    // helper method to validate the position (0-based), 
    // different check for insertion and for getting a node
    // for insertion, position can be equal to size (to insert at the end)
    private void validatePosition(int nodePosition, boolean forInsertion) {
        if ((forInsertion && (nodePosition < 0 || nodePosition > size)) 
                || (!forInsertion && (nodePosition < 0 || nodePosition >= size))) { 
            throw new IndexOutOfBoundsException("Invalid " 
                + (forInsertion ? "insert " : "") + "position: " + nodePosition);
        }
    }

    // overloaded validatePosition method, forInsertion is false by default
    private void validatePosition(int nodePosition) {
        validatePosition(nodePosition, false);
    }

    // list size getter
    public int getSize() {
        return this.size;
    }

    // node getter by position (0-based)
    public Node<T> getNode(int nodePosition) {

        // validate the position, if it is out of bounds, throw an exception
        this.validatePosition(nodePosition);

        // if position is valid, go through the list to get the node at nodePosition
        Node<T> currentNode = this.head;
        for (int i = 0; i < nodePosition; i++) {
            currentNode = currentNode.getNext();
        }
        return currentNode;
    }

    // Insert new node at a specific position (0-based), set data to nodeValue
    public void insertNodeAtPosition(T nodeValue, int nodePosition) {

        // validate nodePosition
        this.validatePosition(nodePosition, true);
        
        // create a new node with the given value
        Node<T> newNode = new Node<>(nodeValue);

        if (nodePosition == 0) {
            newNode.setNext(this.head);
            this.head = newNode;
        } else {
            Node<T> prevNode = getNode(nodePosition - 1);
            newNode.setNext(prevNode.getNext());
            prevNode.setNext(newNode);
        }

        // increment the size of the list
        this.size++;
   
    }

    // insert new node at the end of the list
    public void addNode(T nodeValue) {
        insertNodeAtPosition(nodeValue, this.size);
    }

    // delete node at a specific position (0-based)
    public void deleteNode(int nodePosition) {

        // validate nodePosition
        validatePosition(nodePosition);

        if (nodePosition == 0) {
            // deleting head node
            Node<T> toDelete = head;
            this.head = toDelete.getNext();
            // help garbage collector by setting next of toDelete to null, not really needed I guess
            toDelete.setNext(null);
        } else {
            Node<T> prevNode = getNode(nodePosition - 1);
            Node<T> toDelete = prevNode.getNext();
            prevNode.setNext(toDelete.getNext());
            // help garbage collector by setting next of toDelete to null, not really needed I guess
            toDelete.setNext(null); 
        }

        // decrement the size of the list
        this.size--;
    }

    // traverse the list and print the data of each node
    // Return a string representation of the list using for loop and i++
    public String traverseList() {
        if (head == null) {
            return "List is empty";
        }

        String result = "List (size: " + size + "): ";
        Node<T> currentNode = head;

        for (int i = 0; i < size; i++) {
            result += "[" + i + "]:" + currentNode.getData();
            currentNode = currentNode.getNext();
            if (i < size - 1) {
                result += " -> ";
            }
        }

        // returning string representation of the list
        return result;
    }

    // search for all positions where nodeValue appears, return an array of positions (0-based)
    public int[] searchNode(T nodeValue) {
        // will be storing positions of found nodes with nodeValue contents
        List<Integer> positions = new ArrayList<>();

        Node<T> currentNode = this.head;
        for (int i = 0; i < size; i++) {
            if ((nodeValue == null && currentNode.getData() == null) ||
                (nodeValue != null && nodeValue.equals(currentNode.getData()))) {
                positions.add(i);
            }
            currentNode = currentNode.getNext();
        }

        if (positions.isEmpty()) {
            // returning empty array if not found
            return new int[0];
        } 
        // converting List<Integer> to int[] array
        int[] result = new int[positions.size()];
        for (int i = 0; i < positions.size(); i++) {
            // convert Integer to int
            result[i] = positions.get(i).intValue();
        }
        return result;
    }

}

public class SingleLinkedListApp {
    public static void main(String[] args) {
        System.out.println("\n=== Single Linked List Application ===\n");

        SingleLinkedList<String> list = new SingleLinkedList<>();

        // 1. Test: Empty list
        System.out.println("1. Initial state:");
        System.out.println(list.traverseList()); 

        // 2. Test: Insert at head
        list.insertNodeAtPosition("C", 0);
        System.out.println("\n2. After insert 'C' at position 0:");
        System.out.println(list.traverseList()); // C

        // 3. Insert at head again
        list.insertNodeAtPosition("A", 0);
        System.out.println("\n3. After insert 'A' at position 0:");
        System.out.println(list.traverseList()); // A -> C

        // 4. Insert at middle
        list.insertNodeAtPosition("B", 1);
        System.out.println("\n4. After insert 'B' at position 1:");
        System.out.println(list.traverseList()); // A -> B -> C

        // 5. Insert at end using add()
        list.addNode("D");
        list.addNode("E");
        System.out.println("\n5. After adding 'D' and 'E':");
        System.out.println(list.traverseList()); // A -> B -> C -> D -> E

        // 6. Search: multiple matches
        list.addNode("B");
        System.out.println("\n6. After adding another 'B':");
        System.out.println(list.traverseList());

        int[] found = list.searchNode("B");
        System.out.println("-> Search for 'B': " + Arrays.toString(found)); // [1, 5]

        // 7. Search: not found
        System.out.println("\n7. Search for 'Z': " + Arrays.toString(list.searchNode("Z"))); // []

        // 8. Search: null value
        list.addNode(null);
        list.insertNodeAtPosition(null, 2);
        System.out.println("\n8. After adding nulls:");
        System.out.println(list.traverseList());
        System.out.println("-> Search for null: " + Arrays.toString(list.searchNode(null)));

        // 9. Delete head
        list.deleteNode(0);
        System.out.println("\n9. After deleting head:");
        System.out.println(list.traverseList());

        // 10. Delete middle
        list.deleteNode(2);
        System.out.println("\n10. After deleting position 2:");
        System.out.println(list.traverseList());

        // 11. Delete tail
        list.deleteNode(list.getSize() - 1);
        System.out.println("\n11. After deleting last node:");
        System.out.println(list.traverseList());

        // 12. Final size
        System.out.println("\n12. Final size: " + list.getSize());

        // 13. Test: Insert at invalid position (too high)
        try {
            list.insertNodeAtPosition("X", list.getSize() + 1);
        } catch (IndexOutOfBoundsException ex) {
            System.out.println("\n13. Caught expected exception on insert -> " + ex.getMessage());
        }

        // 14. Test: Delete at invalid position (-1)
        try {
            list.deleteNode(-1);
        } catch (IndexOutOfBoundsException ex) {
            System.out.println("\n14. Caught expected exception on delete -> " + ex.getMessage());
        }

        // 15. Test: Delete at position >= size
        try {
            list.deleteNode(list.getSize());
        } catch (IndexOutOfBoundsException ex) {
            System.out.println("\n15. Caught expected exception on delete -> " + ex.getMessage());
        }

        // 16. Test: Get node at invalid index
        try {
            list.getNode(999);
        } catch (IndexOutOfBoundsException ex) {
            System.out.println("\n16. Caught expected exception on getNode -> " + ex.getMessage());
        }

        System.out.println("\n=== The End ===\n");


    }

}