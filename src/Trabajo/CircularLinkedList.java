package Trabajo;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Clase CircularLinkedList: Implementa una lista enlazada circular genérica.
 * Permite almacenar elementos en una estructura circular en la que el último nodo se enlaza con el primero.
 * Esta implementación incluye funcionalidades básicas como agregar, eliminar, y recorrer los elementos.
 */
public class CircularLinkedList<T> implements Iterable<T> {

    // Clase interna que representa un nodo en la lista enlazada circular
    private class Node {
        T element;   // Elemento que almacena el nodo
        Node next;   // Referencia al siguiente nodo en la lista

        // Constructor para crear un nodo con un elemento
        Node(T element) {
            this.element = element;
        }
    }

    // Atributos de la lista circular
    private Node head = null;  // Referencia al primer nodo de la lista
    private Node tail = null;  // Referencia al último nodo de la lista
    private int size = 0;      // Tamaño de la lista (número de elementos)

    /**
     * Método para agregar un elemento al final de la lista.
     * Si la lista está vacía, el nuevo nodo será el único nodo, y se enlaza consigo mismo.
     * Si no está vacía, se enlaza el nuevo nodo al final y se actualiza el último nodo (tail).
     *
     * @param element Elemento a agregar a la lista
     */
    public void addLast(T element) {
        Node newNode = new Node(element);  // Crear un nuevo nodo con el elemento dado
        if (isEmpty()) {  // Si la lista está vacía
            head = newNode;    // El nuevo nodo será la cabeza
            tail = newNode;    // El nuevo nodo también será la cola
            tail.next = head;  // La cola apunta a la cabeza para formar la estructura circular
        } else {  // Si la lista no está vacía
            tail.next = newNode;  // El último nodo apunta al nuevo nodo
            tail = newNode;       // Actualizar la cola al nuevo nodo
            tail.next = head;     // La cola apunta nuevamente a la cabeza para mantener la circularidad
        }
        size++;  // Incrementar el tamaño de la lista
    }

    /**
     * Método para eliminar el primer elemento de la lista.
     * Si la lista tiene un solo elemento, se eliminan ambos referencias (head y tail).
     * Si hay más de un elemento, se elimina el primero y la cabeza se actualiza al siguiente nodo.
     *
     * @return El elemento que fue eliminado de la lista
     * @throws NoSuchElementException Si la lista está vacía
     */
    public T removeFirst() {
        if (isEmpty()) {  // Si la lista está vacía
            throw new NoSuchElementException("La lista está vacía.");  // Lanzar excepción
        }
        T removedElement = head.element;  // Guardar el elemento que se va a eliminar
        if (head == tail) {  // Si hay solo un elemento en la lista
            head = null;  // Eliminar la referencia de head
            tail = null;  // Eliminar la referencia de tail
        } else {  // Si hay más de un elemento
            head = head.next;  // Actualizar head al siguiente nodo
            tail.next = head;  // Asegurar que tail apunte al nuevo head, manteniendo la circularidad
        }
        size--;  // Disminuir el tamaño de la lista
        return removedElement;  // Devolver el elemento eliminado
    }

    /**
     * Método para obtener el primer elemento de la lista sin eliminarlo.
     *
     * @return El primer elemento de la lista
     * @throws NoSuchElementException Si la lista está vacía
     */
    public T getFirst() {
        if (isEmpty()) {  // Si la lista está vacía
            throw new NoSuchElementException("La lista está vacía.");  // Lanzar excepción
        }
        return head.element;  // Devolver el elemento en la cabeza de la lista
    }

    /**
     * Método para verificar si la lista está vacía.
     *
     * @return true si la lista está vacía, false si tiene al menos un elemento
     */
    public boolean isEmpty() {
        return size == 0;  // La lista está vacía si el tamaño es 0
    }

    /**
     * Implementación de la interfaz Iterable, que permite recorrer los elementos de la lista
     * usando un iterador personalizado.
     *
     * @return Un iterador para recorrer la lista circular
     */
    @Override
    public Iterator<T> iterator() {
        return new CircularLinkedListIterator();  // Devolver una instancia del iterador personalizado
    }

    // Clase interna que implementa el iterador para la lista circular
    private class CircularLinkedListIterator implements Iterator<T> {
        private Node current;      // Nodo actual en el recorrido
        private Node previous;     // Nodo previo al nodo actual
        private Node lastReturned; // Último nodo devuelto por el iterador
        private boolean canRemove; // Flag que indica si es posible eliminar un nodo
        private int elementsVisited; // Contador de los elementos ya recorridos

        // Constructor del iterador
        public CircularLinkedListIterator() {
            current = head;       // Iniciar en el primer nodo
            previous = tail;      // El nodo previo al inicio es la cola
            lastReturned = null;  // Inicialmente, no se ha devuelto ningún nodo
            canRemove = false;    // No se puede eliminar sin haber recorrido al menos un nodo
            elementsVisited = 0;  // Ningún elemento ha sido visitado al iniciar
        }

        /**
         * Método para verificar si hay más elementos que recorrer en la lista.
         *
         * @return true si hay más elementos por recorrer, false si ya se recorrieron todos
         */
        @Override
        public boolean hasNext() {
            return elementsVisited < size;  // Hay más elementos si no se han recorrido todos
        }

        /**
         * Método para devolver el siguiente elemento en el recorrido de la lista.
         *
         * @return El siguiente elemento de la lista
         * @throws NoSuchElementException Si ya se recorrieron todos los elementos
         */
        @Override
        public T next() {
            if (elementsVisited >= size || current == null) {  // Si ya se recorrieron todos los elementos o la lista está vacía
                throw new NoSuchElementException();  // Lanzar excepción
            }
            lastReturned = current;   // Guardar el nodo actual como el último devuelto
            current = current.next;   // Avanzar al siguiente nodo
            elementsVisited++;        // Incrementar el contador de elementos visitados
            canRemove = true;         // Permitir la eliminación del nodo
            return lastReturned.element;  // Devolver el elemento del nodo
        }

        /**
         * Método para eliminar el último elemento devuelto por el iterador.
         *
         * @throws IllegalStateException Si no se ha devuelto ningún elemento o ya se eliminó
         */
        @Override
        public void remove() {
            if (!canRemove) {  // Si no se puede eliminar
                throw new IllegalStateException();  // Lanzar excepción
            }
            if (lastReturned == null) {  // Si no se ha devuelto ningún elemento
                throw new IllegalStateException();  // Lanzar excepción
            }

            // Eliminar el nodo lastReturned
            if (size == 1) {  // Caso especial: solo hay un elemento en la lista
                head = null;  // Eliminar la referencia a la cabeza
                tail = null;  // Eliminar la referencia a la cola
            } else if (lastReturned == head) {  // Si el nodo a eliminar es la cabeza
                head = head.next;  // La nueva cabeza será el siguiente nodo
                tail.next = head;  // Actualizar el puntero de la cola para mantener la circularidad
            } else {  // Si el nodo a eliminar está en el medio o al final de la lista
                // Encontrar el nodo previo al lastReturned
                Node prev = head;
                while (prev.next != lastReturned) {  // Buscar el nodo previo
                    prev = prev.next;
                }
                prev.next = lastReturned.next;  // Saltar el nodo eliminado

                if (lastReturned == tail) {  // Si el nodo eliminado es la cola
                    tail = prev;  // Actualizar la cola
                }
            }
            size--;  // Reducir el tamaño de la lista
            elementsVisited--;  // Reducir el contador de elementos visitados
            lastReturned = null;  // Resetear el nodo eliminado
            canRemove = false;    // No se puede volver a eliminar hasta avanzar al siguiente
        }
    }
}
