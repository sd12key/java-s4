public class DoubleLinkedList<T> {
    private DoubleNode<T> head;
    private DoubleNode<T> tail;
    private int size;

    public void insertNodeAtPosition(T value, int position) {
        validatePosition(position, true);
        DoubleNode<T> newNode = new DoubleNode<>(value);

        if (position == 0) {
            newNode.setNext(this.head);
            if (this.head != null) head.setPrev(newNode);
            this.head = newNode;
            if (this.tail == null) this.tail = newNode;
        } else if (position == this.size) {
            newNode.setPrev(this.tail);
            if (this.tail != null) this.tail.setNext(newNode);
            this.tail = newNode;
            if (this.head == null) this.head = newNode;
        } else {
            DoubleNode<T> current = this.getNode(position);
            DoubleNode<T> prev = current.getPrev();
            newNode.setNext(current);
            newNode.setPrev(prev);
            if (prev != null) prev.setNext(newNode);
            current.setPrev(newNode);
        }
        this.size++;
    }

    public void addNode(T value) {
        insertNodeAtPosition(value, size);
    }

    public void deleteNode(int position) {
        validatePosition(position);
        DoubleNode<T> toDelete = this.getNode(position);

        DoubleNode<T> prev = toDelete.getPrev();
        DoubleNode<T> next = toDelete.getNext();

        if (prev != null) prev.setNext(next);
        else head = next;

        if (next != null) next.setPrev(prev);
        else tail = prev;

        toDelete.setNext(null);
        toDelete.setPrev(null);
        this.size--;
    }

    public int[] searchNode(T value) {
        int[] positions = new int[this.size];
        int count = 0;
        DoubleNode<T> current = this.head;

        for (int i = 0; i < this.size; i++) {
            if ((value == null && current.getData() == null) ||
                (value != null && value.equals(current.getData()))) {
                positions[count++] = i;
            }
            current = current.getNext();
        }
        return java.util.Arrays.copyOf(positions, count);
    }

    public String traverseList() {
        if (this.head == null) return "List is empty";
        String result = "List: ";
        DoubleNode<T> current = this.head;
        for (int i = 0; i < this.size; i++) {
            result += "[" + i + "]: " + (current.getData() == null ? "(null)" : current.getData());
            current = current.getNext();
            if (i < this.size - 1) result += " <-> ";
        }
        return result;
    }

    public int getSize() {
        return this.size;
    }

    public DoubleNode<T> getNode(int position) {
        validatePosition(position);

        if (position < this.size / 2) {
            // start from head and move forward
            DoubleNode<T> current = this.head;
            for (int i = 0; i < position; i++) {
                current = current.getNext();
            }
            return current;
        } else {
            // start from tail and move backward
            DoubleNode<T> current = this.tail;
            for (int i = this.size - 1; i > position; i--) {
                current = current.getPrev();
            }
            return current;
        }
    }

    private void validatePosition(int position) {
        validatePosition(position, false);
    }

    private void validatePosition(int position, boolean forInsert) {
        if ((forInsert && (position < 0 || position > this.size)) 
        || (!forInsert && (position < 0 || position >= this.size))) {
            throw new IndexOutOfBoundsException("Invalid " 
            + (forInsert ? "insert " : "") + "position: " + position);
        }
    }

    @Override
    public String toString() {
        return this.traverseList();
    }

}
