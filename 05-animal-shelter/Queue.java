public class Queue<T> {
    private final DoubleLinkedList<T> queue = new DoubleLinkedList<>();

    public T get(int index) {
        try {
            return queue.getNode(index).getData();
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    public T delete(int index) {
        try {
            T item = queue.getNode(index).getData();
            queue.deleteNode(index);
            return item;
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    public int size() {
        return queue.getSize();
    }

    public boolean isEmpty() {
        return queue.getSize() == 0;
    }

    public void enqueue(T item) {
        queue.addNode(item);
    }
    
    public T dequeue() {
        if (this.isEmpty()) { return null; }
        T item = queue.getNode(0).getData();
        queue.deleteNode(0);
        return item;
    }

    public T peek() {
        if (this.isEmpty()) { return null; }
        return queue.getNode(0).getData();
    }

    @Override
    public String toString() {
        return queue.toString();
    }

}