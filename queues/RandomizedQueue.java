/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] q;
    private int tail;

    public RandomizedQueue()                 // construct an empty randomized queue
    {
        q = (Item[]) new Object[2]; // start with a capacity of 2
        tail = 0;
    }


    public boolean isEmpty()                 // is the randomized queue empty?
    {
        return tail == 0;
    }

    public int size()                        // return the number of items on the randomized queue
    {
        return tail;
    }

    private void resize(int capacity) {
        Item[] temp = (Item[]) new Object[capacity];
        for (int i = 0; i < tail; i++) {
            temp[i] = q[i];
        }
        q = temp;
    }

    private void autoEnlarge() {
        if (q.length == tail) resize(q.length * 2);
    }

    private void autoShrink() {
        if (tail <= q.length / 4) resize(q.length / 2);
    }


    public void enqueue(Item item)           // add the item
    {
        if (item == null) throw new java.lang.IllegalArgumentException();
        q[tail++] = item;
        autoEnlarge();
    }

    public Item dequeue()                    // remove and return a random item
    {
        if (isEmpty()) throw new java.util.NoSuchElementException();
        int roll = StdRandom.uniform(tail);
        Item item = q[roll];
        for (int i = roll; i < tail; i++) {
            q[i] = q[i + 1];
        }
        q[tail] = null;
        tail--;
        autoShrink();

        return item;
    }

    public Item sample()                     // return a random item (but do not remove it)
    {
        if (isEmpty()) throw new java.util.NoSuchElementException();
        int roll = StdRandom.uniform(tail);
        return q[roll];
    }

    public Iterator<Item> iterator()         // return an independent iterator over items in random order
    {
        return new ListIterator();
    }

    private class ListIterator implements Iterator<Item> {

        private int i = tail;

        public boolean hasNext() {
            return i > 0;
        }

        public void remove() {
            throw new java.lang.UnsupportedOperationException();
        }

        public Item next() {
            if (tail == 0) throw new java.util.NoSuchElementException();
            return q[--i];
        }
    }

    public static void main(String[] args)   // unit testing (optional)
    {
        // RandomizedQueue<String> testQ = new RandomizedQueue<String>();
        // testQ.enqueue("a");
        // testQ.enqueue("b");
        // testQ.enqueue("c");
        // String delete = testQ.dequeue();
        // StdOut.printf("Removing: %s \n", delete);
        // for (String i : testQ)
        //     StdOut.println(i);
    }
}
