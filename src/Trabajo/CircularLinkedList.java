package Trabajo;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class CircularLinkedList<T> implements Iterable<T> {
    // Nodo interno de la lista circular
    private class Node {
        T element;
        Node next;

        Node(T element) {
            this.element = element;
        }
    }

    // Atributos de la lista circular
    private Node head = null;
    private Node tail = null;
    private int size = 0;

    // Método para agregar un elemento al final de la lista
    public void addLast(T element) {
        Node newNode = new Node(element);
        if (isEmpty()) {
            head = newNode;
            tail = newNode;
            tail.next = head;
        } else {
            tail.next = newNode;
            tail = newNode;
            tail.next = head;
        }
        size++;
    }

    public T removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException("La lista está vacía.");
        }
        T removedElement = head.element;
        if (head == tail) { 
            head = null;
            tail = null;
        } else {
            head = head.next;
            tail.next = head;
        }
        size--;
        return removedElement;
    }

    public T getFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException("La lista está vacía.");
        }
        return head.element;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public Iterator<T> iterator() {
        return new CircularLinkedListIterator();
    }

    private class CircularLinkedListIterator implements Iterator<T> {
        private Node current;
        private Node previous;
        private Node lastReturned;
        private boolean canRemove;
        private int elementsVisited;

        public CircularLinkedListIterator() {
            current = head;
            previous = tail;
            lastReturned = null;
            canRemove = false;
            elementsVisited = 0;
        }

        @Override
        public boolean hasNext() {
            return elementsVisited < size;
        }

        @Override
        public T next() {
            if (elementsVisited >= size || current == null) {
                throw new NoSuchElementException();
            }
            lastReturned = current;
            current = current.next;
            elementsVisited++;
            canRemove = true;
            return lastReturned.element;
        }

        @Override
        public void remove() {
            if (!canRemove) {
                throw new IllegalStateException();
            }
            if (lastReturned == null) {
                throw new IllegalStateException();
            }

            // Eliminar el nodo lastReturned
            if (size == 1) {
                // Solo un elemento
                head = null;
                tail = null;
            } else if (lastReturned == head) {
                head = head.next;
                tail.next = head;
            } else {
                // Encontrar el nodo previo a lastReturned
                Node prev = head;
                while (prev.next != lastReturned) {
                    prev = prev.next;
                }
                prev.next = lastReturned.next;
                if (lastReturned == tail) {
                    tail = prev;
                }
            }
            size--;
            elementsVisited--;
            lastReturned = null;
            canRemove = false;
        }
    }
}
