package Trabajo;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class CircularLinkedList<T> implements Iterable<T> {
    // Nodo interno de la lista circular
    private class Node<T> {
        T element;
        Node<T> next;

        Node(T element) {
            this.element = element;
        }
    }

    // Atributos de la lista circular
    private Node<T> head = null;
    private Node<T> tail = null;
    private int size = 0;

    // Método para agregar un elemento al final de la lista
    public void addLast(T element) {
        Node<T> newNode = new Node<>(element);
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

    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public Iterator<T> iterator() {
        return new CircularLinkedListIterator();
    }

    private class CircularLinkedListIterator implements Iterator<T> {
        private Node<T> current = head;
        private int elementsVisited = 0;

        @Override
        public boolean hasNext() {
            return elementsVisited < size;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            T element = current.element;
            current = current.next;
            elementsVisited++;
            return element;
        }
    }
}
