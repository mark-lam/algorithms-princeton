/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import java.util.Iterator;

public class Deque<Item> implements Iterable<Item> {
    private Node first, last;
    private int size;

    public Deque()                           // construct an empty deque
    {
        size = 0;
        first = null;
        last = null;
    }

    private class Node {
        Item item;
        Node next;
        Node prev;
    }

    public boolean isEmpty()                 // is the deque empty?
    {
        return size == 0;
    }

    public int size()                        // return the number of items on the deque
    {
        return size;
    }

    public void addFirst(Item item)          // add the item to the front
    {
        if (item == null) throw new java.lang.IllegalArgumentException();
        Node oldfirst = first;
        first = new Node();
        first.item = item;
        first.next = oldfirst;
        first.prev = null;
        if (isEmpty()) last = first; // have to set last, if user starts with addFirst
        else oldfirst.prev = first;
        size++;
    }

    public void addLast(Item item)           // add the item to the end
    {
        if (item == null) throw new java.lang.IllegalArgumentException();
        Node oldlast = last;
        last = new Node();
        last.item = item;
        last.prev = oldlast;
        last.next = null;
        if (isEmpty()) first = last; // have to set first, if user starts with addLast
        else oldlast.next = last;
        size++;
    }

    public Item removeFirst()                // remove and return the item from the front
    {
        if (size == 0) throw new java.util.NoSuchElementException();

        Item item = first.item;
        first = first.next;
        size--;
        if (size == 0)
            last = first; // if only had 1 remaining, first is null; last needs to be re-set
        else first.prev = null;


        return item;
    }

    public Item removeLast()                 // remove and return the item from the end
    {
        if (size == 0) throw new java.util.NoSuchElementException();

        Item item = last.item;
        last = last.prev;
        size--;
        if (size == 0)
            first = last; // if only had 1 remaining, last is null; first needs to be re-set
        else last.next = null;

        return item;
    }

    public Iterator<Item> iterator()         // return an iterator over items in order from front to end
    {
        return new ListIterator();
    }

    private class ListIterator implements Iterator<Item> {
        private Node current = first;

        public boolean hasNext() {
            return (current != null);
        }

        public void remove() {
            throw new java.lang.UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) throw new java.util.NoSuchElementException();
            Item item = current.item;
            current = current.next;
            return item;
        }
    }

    public static void main(String[] args)   // unit testing (optional)
    {
        // Deque<String> testDeck = new Deque<String>();
        // testDeck.addFirst("b");
        // testDeck.addLast("c");
        // testDeck.removeLast();
        // testDeck.removeLast();
        // StdOut.printf("Size: %d\n", testDeck.size());
        // for (String i : testDeck)
        //     StdOut.println(i);
    }
}
