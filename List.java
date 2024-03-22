/** A linked list of character data objects.
 *  (Actually, a list of Node objects, each holding a reference to a character data object.
 *  However, users of this class are not aware of the Node objects. As far as they are concerned,
 *  the class represents a list of CharData objects. Likwise, the API of the class does not
 *  mention the existence of the Node objects). */
public class List {

    // Points to the first node in this list
    private Node first;

    // The number of elements in this list
    private int size;


    /** Constructs an empty list. */
    public List() {
        first = null;
        size = 0;
    }

    /** Returns the number of elements in this list. */
    public int getSize() {
 	      return size;
    }

    /** Returns the first element in the list */
    public CharData getFirst() {
        return first.cp;
    }

    public Node getFirstN(){return first;}

    /** GIVE Adds a CharData object with the given character to the beginning of this list. */
    public void addFirst(char chr) {
        CharData cp = new CharData(chr);
        Node newNode = new Node(cp);
        newNode.next = first;
        first = newNode;
        size++;

    }
    
    /** GIVE Textual representation of this list. */
    public String toString() {
        Node curr = first;
        String s = "(";
        while(curr != null){
            s += curr.toString() ;
            curr = curr.next;
        }
        s = s + "\b";
        return s+")";
    }

    /** Returns the index of the first CharData object in this list
     *  that has the same chr value as the given char,
     *  or -1 if there is no such object in this list. */
    public int indexOf(char chr) {
        Node curr = first;
        int index = 0;
        while(curr != null) {
            if(curr.cp.equals(chr))
                return index;

            curr = curr.next;
            index++;
        }
        return -1;
    }

    /** If the given character exists in one of the CharData objects in this list,
     *  increments its counter. Otherwise, adds a new CharData object with the
     *  given chr to the beginning of this list. */
    public void update(char chr) {
        if(indexOf(chr) == -1)
            addFirst(chr);
        else{
            Node curr = first;
            while (curr != null){
                if(curr.cp.chr == chr){
                    curr.cp.count++;
                    break;
                }
                curr = curr.next;

            }
            //ListIterator iterator = listIterator(indexOf(chr));
            //iterator.current.cp.count++;
        }

    }

    /** GIVE If the given character exists in one of the CharData objects
     *  in this list, removes this CharData object from the list and returns
     *  true. Otherwise, returns false. */
    public boolean remove(char chr) {
        if(indexOf(chr) == -1)
            return false;
        Node prev = null;
        Node curr = first;
        while(curr != null && curr.cp.equals(chr)){
            prev = curr;
            curr = curr.next;

        }
        if(curr == null)
            return false;
        if(prev == null)
            first = first.next;
        else
            prev.next = curr.next;

        size--;
        return true;



    }

    /** Returns the CharData object at the specified index in this list. 
     *  If the index is negative or is greater than the size of this list, 
     *  throws an IndexOutOfBoundsException. */
    public CharData get(int index) {
        if(index < 0 || index >= size)
            throw new IndexOutOfBoundsException("Index" + index + "is out of bounds");

        ListIterator iterator = listIterator(index);
        return iterator.current.cp;
    }

    /** Returns an array of CharData objects, containing all the CharData objects in this list. */
    public CharData[] toArray() {
	    CharData[] arr = new CharData[size];
	    Node current = first;
	    int i = 0;
        while (current != null) {
    	    arr[i++]  = current.cp;
    	    current = current.next;
        }
        return arr;
    }

    /** Returns an iterator over the elements in this list, starting at the given index. */
    public ListIterator listIterator(int index) {
	    // If the list is empty, there is nothing to iterate   
	    if (size == 0) return null;
	    // Gets the element in position index of this list
	    Node current = first;
	    int i = 0;
        while (i < index) {
            current = current.next;
            i++;
        }
        // Returns an iterator that starts in that element
	    return new ListIterator(current);
    }
}
