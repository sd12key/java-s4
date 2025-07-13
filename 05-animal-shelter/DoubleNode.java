public class DoubleNode<T> {
    private T data;
    private DoubleNode<T> next;
    private DoubleNode<T> prev;

    public DoubleNode(T data) {
        this.data = data;
    }

    public T getData() { return this.data; }
    public void setData(T data) { this.data = data; }

    public DoubleNode<T> getNext() { return this.next; }
    public void setNext(DoubleNode<T> next) { this.next = next; }

    public DoubleNode<T> getPrev() { return this.prev; }
    public void setPrev(DoubleNode<T> prev) { this.prev = prev; }

}
