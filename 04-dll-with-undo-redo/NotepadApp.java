// ==== DoubleNode class ===================================
class DoubleNode<T> {
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

// ==== DoubleLinkedList class ==============================
class DoubleLinkedList<T> {
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
            result += "[" + i + "]:" + (current.getData() == null ? "(null)" : current.getData());
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

class Notepad<T> {
    // Notepad class uses DoubleLinkedList to store elements 
    // (lines of text, or any other type T)
    private final DoubleLinkedList<T> elements;
    // UndoRedoHistory to keep track of changes
    private final UndoRedoHistory<T> history;

    public Notepad() {
        this.elements = new DoubleLinkedList<>();
        this.history = new UndoRedoHistory<>(this);
    }

    public void addElement(T element) {
        // add element at the end of the list
        this.elements.addNode(element);
    
        // record the addition — previous value is null, new value is the element
        this.history.recordChange(this.elements.getSize() - 1, null, element);
    }

    public void insertElementAtPosition(int position, T element) {

        // insert the new element at the specified position
        this.elements.insertNodeAtPosition(element, position);
    
        // record the insertion — previous value is null, new value is the element
        this.history.recordChange(position, null, element);
    }

    public void editElement(int position, T newElement) {
        // retrieve old element before making changes
        T oldElement = this.elements.getNode(position).getData();

        // apply new element
        this.elements.getNode(position).setData(newElement);

        // record the change — previous value is the old element, new value is the new element
        this.history.recordChange(position, oldElement, newElement);
    }

    public void deleteElement(int position) {
        T oldElement = this.elements.getNode(position).getData(); 
        this.elements.deleteNode(position); 

        // record the deletion — previous value is the old element, new value is null
        this.history.recordChange(position, oldElement, null); 
    }

    public T getLine(int position) {
        return elements.getNode(position).getData();
    }

    public int getSize() { return this.elements.getSize(); }

    // apply Undo/Redo action
    public void applyUndoRedo(int position, T currentValue, T previousValue) {
        if (currentValue == null) {
            // deleting element
            this.elements.deleteNode(position);
        } else if (previousValue == null) {
            // inserting element
            this.elements.insertNodeAtPosition(currentValue, position);
        } else {
            // edit/update operation
            this.elements.getNode(position).setData(currentValue);
        }
    }
    
    public UndoRedoHistory<T> getUndoRedoHistory() { return this.history; }
    public DoubleLinkedList<T> getElements() { return this.elements; }

    // methods to handle undo/redo actions
    public void undo() {
        this.history.undo();
    }
    
    public void redo() {
        this.history.redo();
    }

    public void undoAll() {
        while (this.getUndoRedoHistory().canUndo()) {
            this.undo();
        }
    }

    public void redoAll() {
        while (this.getUndoRedoHistory().canRedo()) {
            this.redo();
        }
    }

    public boolean canUndo() {
        return this.getUndoRedoHistory().canUndo();
    }

    public boolean canRedo() {
        return this.getUndoRedoHistory().canRedo();
    }    

}

// ==== UndoRedoRecord class ==============================================
// it represents single undo/redo record, stores position and values
class UndoRedoRecord<T> {
    private int position;
    private T previousValue;
    private T newValue;

    public UndoRedoRecord(int position, T previousValue, T newValue) {
        this.position = position;
        this.previousValue = previousValue;
        this.newValue = newValue;
    }

    public int getPosition() { return this.position; }
    public T getPreviousValue() { return this.previousValue; }
    public T getNewValue() { return this.newValue; }
}

// ==== UndoRedoHistory class =============================================
// based on DoubleLinkedList, stores history of changes
class UndoRedoHistory<T> extends DoubleLinkedList<UndoRedoRecord<T>> {
    private int currentState = -1; 
    private final Notepad<T> notepad;

    // constructor takes Notepad instance
    // this is way we how we apply Undo/Redo actions to the notepad
    // assume Notepad must have method applyUndoRedo(int position, T value)
    public UndoRedoHistory(Notepad<T> notepad) {
        this.notepad = notepad;
    }

    // we have to remove all future states if adding a new state
    // history must be linear, at least this is how I understand undo/redo logic
    // all editors behave like this, if I add A,B,C, remove C,B, then add D - cannot redo B,C
    public void recordChange(int position, T previousValue, T newValue) {
        while (this.getSize() > (this.currentState + 1)) {
            this.deleteNode(this.getSize() - 1);
        }
        this.addNode(new UndoRedoRecord<>(position, previousValue, newValue));
        this.currentState++;
    }

    // we can undo if current state is 0,1,....
    public boolean canUndo() {
        return (this.currentState >= 0);
    }

    // we can redo if [ currentState < (size - 1) ]
    public boolean canRedo() {
        return (this.currentState < (this.getSize() - 1));
    }

    public void undo() {
        if (!this.canUndo()) return;
        UndoRedoRecord<T> record = this.getNode(currentState).getData();
    
        // apply previous value (reverse the change)
        notepad.applyUndoRedo(record.getPosition(), record.getPreviousValue(), record.getNewValue());

        // decrement current state to point to the previous record
        this.currentState--;
    }
    
    public void redo() {
        if (!this.canRedo()) return;

        // increment current state to point to the next record
        this.currentState++;

        UndoRedoRecord<T> record = this.getNode(currentState).getData();
   
        // re-apply new value
        notepad.applyUndoRedo(record.getPosition(), record.getNewValue(), record.getPreviousValue());
    }
    
 
}


// ==== NotepadApp class ====================================
public class NotepadApp {
    public static void main(String[] args) {
        System.out.println("\n=== NOTEPAD UNDO/REDO TEST ===");
        Notepad<String> notepad = new Notepad<>();

        System.out.println("\nAdding A, B, C");
        notepad.addElement("A");
        notepad.addElement("B");
        notepad.addElement("C");
        System.out.println(notepad.getElements() + "\nCan undo: " + notepad.canUndo() + "\nCan redo: " + notepad.canRedo());

        System.out.println("\nAdding 'A.5' between A and B");
        notepad.insertElementAtPosition(1, "A.5");
        System.out.println("Adding 'B.5' between B and C");
        notepad.insertElementAtPosition(3, "B.5");
        System.out.println(notepad.getElements() + "\nCan undo: " + notepad.canUndo() + "\nCan redo: " + notepad.canRedo());        

        System.out.println("\nUNDO (should remove B.5)");
        notepad.undo();
        System.out.println(notepad.getElements() + "\nCan undo: " + notepad.canUndo() + "\nCan redo: " + notepad.canRedo());        

        System.out.println("\nREDO (should restore B.5)");
        notepad.redo();
        System.out.println(notepad.getElements() + "\nCan undo: " + notepad.canUndo() + "\nCan redo: " + notepad.canRedo());        

        System.out.println("\nUNDO 3x (expect A and B)");
        notepad.undo();
        notepad.undo();
        notepad.undo();
        System.out.println(notepad.getElements() + "\nCan undo: " + notepad.canUndo() + "\nCan redo: " + notepad.canRedo());        

        System.out.println("\nREDO 3x (full restore)");
        notepad.redo();
        notepad.redo();
        notepad.redo();
        System.out.println(notepad.getElements() + "\nCan undo: " + notepad.canUndo() + "\nCan redo: " + notepad.canRedo());        

        System.out.println("\nEdit A -> A1");
        notepad.editElement(0, "A1");
        System.out.println("Edit B -> B1");
        notepad.editElement(2, "B1");
        System.out.println(notepad.getElements() + "\nCan undo: " + notepad.canUndo() + "\nCan redo: " + notepad.canRedo());        

        System.out.println("\nUNDO 2x (expect revert both edits)");
        notepad.undo();
        notepad.undo();
        System.out.println(notepad.getElements() + "\nCan undo: " + notepad.canUndo() + "\nCan redo: " + notepad.canRedo());        
        
        System.out.println("\nInsert X at pos.1");
        notepad.insertElementAtPosition(1, "X");
        System.out.println(notepad.getElements() + "\nCan undo: " + notepad.canUndo() + "\nCan redo: " + notepad.canRedo());        
        
        System.out.println("\nUNDO (should remove X)");
        notepad.undo();
        System.out.println(notepad.getElements() + "\nCan undo: " + notepad.canUndo() + "\nCan redo: " + notepad.canRedo());        
        
        System.out.println("\nREDO 2x (just restore X)");
        notepad.redo();
        notepad.redo();
        System.out.println(notepad.getElements() + "\nCan undo: " + notepad.canUndo() + "\nCan redo: " + notepad.canRedo());        

        System.out.println("\nUNDO ALL");
        notepad.undoAll();
        System.out.println(notepad.getElements() + "\nCan undo: " + notepad.canUndo() + "\nCan redo: " + notepad.canRedo());        
        
        System.out.println("\nREDO ALL");
        notepad.redoAll();
        System.out.println(notepad.getElements() + "\nCan undo: " + notepad.canUndo() + "\nCan redo: " + notepad.canRedo());        

        System.out.println("\nAdd null");
        notepad.addElement(null);
        System.out.println(notepad.getElements() + "\nCan undo: " + notepad.canUndo() + "\nCan redo: " + notepad.canRedo());        

        System.out.println("\nUNDO null");
        notepad.undo();
        System.out.println(notepad.getElements() + "\nCan undo: " + notepad.canUndo() + "\nCan redo: " + notepad.canRedo());        

        System.out.println("\n=== THE END ===\n");
    }
}
