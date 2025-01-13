package by.it.group310902.strizhevskiy.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;

/*
    Задание на уровень B

    Создайте class MyLinkedList<E>, который реализует интерфейс Deque<E>
    и работает на основе двунаправленного связного списка
    БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ
*/

public class MyLinkedList<E> implements Deque<E> {
    
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    private Node<E> head = new Node<>(null, null, null);
    private Node<E> tail = head;
    private int size = 0;

    public boolean add(E e) {
        addLast(e);
        return true;
    }

    public void addFirst(E e) {
        Node<E> node = new Node<>(e, null, head);
        head.before = node;
        head = node;
        size++;
    }

    public void addLast(E e) {
        Node<E> node = new Node<>(null, tail, null);
        tail.element = e;
        tail.after = node;
        tail = node;
        size++;
    }

    public E element() {
        return getFirst();
    }

    public E getFirst() {
        return head.element;
    }

    public E getLast() {
        return size == 0 ? null : tail.before.element;
    }

    public E poll() {
        return pollFirst();
    }

    public E pollFirst() {
        if (size == 0) { return null; }
        E element = head.element;
        head = head.after;
        head.before = null;
        size--;
        return element;
    }

    public E pollLast() {
        if (size == 0) { return null; }
        tail = tail.before;
        tail.after = null;
        E element = tail.element;
        tail.element = null;
        size--;
        return element;
    }

    public boolean remove(Object o) {
        Node<E> node = head;
        for (int i = 0; i < size; i++) {
            if (o == node.element || o != null && o.equals(node.element)) {
                if (node != head) { node.before.after = node.after; }
                else { head = node.after; }
                node.after.before = node.before;
                size--;
                return true;
            }
            node = node.after;
        }
        return false;
    }

    public E remove(int index) {
        if (index < 0 || index >= size) { return null; }
        Node<E> node = head;
        for (int i = 0; i < index; i++) {
            node = node.after;
        }
        if (node != head) { node.before.after = node.after; }
        else { head = node.after; }
        node.after.before = node.before;
        size--;
        return node.element;
    }

    public int size() {
        return size;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder("[");
        Node<E> node = head;
        for (int i = 0; i < size; i++) {
            sb.append(node.element).append(", ");
            node = node.after;
        }
        int len = sb.length();
        if (len > 1) { sb.setLength(len-2); }
        sb.append("]");
        return sb.toString();
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    
    public E remove() { return null; }
    public Iterator iterator() { return null; }
    public boolean contains(Object o) { return false; }
    public boolean addAll(Collection<? extends E> c) { return false; }
    public E peek() { return null; }
    public E removeFirst() { return null; }
    public E removeLast() { return null; }
    public E pop() { return null; }
    public void push(E e) { return; }
    public boolean offerLast(E e) { return false; }
    public E peekFirst() { return null; }
    public boolean removeFirstOccurrence(Object o) { return false; }
    public boolean offerFirst(E e) { return false; }
    public E peekLast() { return null; }
    public boolean removeLastOccurrence(Object o) { return false; }
    public boolean offer(E e) { return false; }
    public Iterator descendingIterator() { return null; }
    public void clear() { return; }
    public boolean isEmpty() { return false; }
    public <T> T[] toArray(T[] a) { return null; }
    public Object[] toArray() { return null; }
    public boolean removeAll(Collection<?> c) { return false; }
    public boolean retainAll(Collection<?> c) { return false; }
    public boolean containsAll(Collection<?> c) { return false; }

    private static class Node<E> {
        Node<E> before;
        Node<E> after;
        E element;

        public Node(E element, Node<E> before, Node<E> after) {
            init(element, before, after);
        }

        public final void init(E element, Node<E> before, Node<E> after) {
            this.before = before;
            this.after = after;
            this.element = element;
        }
    }

}