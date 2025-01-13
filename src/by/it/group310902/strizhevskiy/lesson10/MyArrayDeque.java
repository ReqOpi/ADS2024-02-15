package by.it.group310902.strizhevskiy.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;

/*
    Задание на уровень А

    Создайте class MyArrayDeque<E>, который реализует интерфейс Deque<E>
    и работает на основе приватного массива типа E[]
    БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ
*/

public class MyArrayDeque<E> implements Deque<E> {

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    private int capacity = 10;
    private int size = 0;
    private Object[] elements = new Object[capacity];

    private int head = 0;
    private int tail = 0;


    private void enlarge() {
        Object[] temp = new Object[1+size*3/2];
        if (head+size <= capacity) {
            System.arraycopy(elements,head,temp,0,size);
        } else {
            System.arraycopy(elements,head,temp,0,capacity-head);
            System.arraycopy(elements,0,temp,capacity-head,tail);
        }
        head = 0;
        tail = size;
        capacity = temp.length;
        elements = temp;
    }

    public boolean add(E e) {
        addLast(e);
        return true;
    }

    public void addFirst(E e) {
        if (size == capacity) { enlarge(); }
        if (head == 0) { head = capacity; }
        elements[--head] = e;
        size++;
    }

    public void addLast(E e) {
        if (size == capacity) { enlarge(); }
        elements[tail++] = e;
        if (tail == capacity) { tail = 0; }
        size++;
    }

    public E element() {
        return getFirst();
    }

    public E getFirst() {
        return (E) elements[head];
    }

    public E getLast() {
        return (E) elements[tail == 0 ? capacity-1 : tail-1];
    }

    public E poll() {
        return pollFirst();
    }

    public E pollFirst() {
        if (size == 0) { return null; }
        E temp = (E) elements[head];
        elements[head++] = null;
        if (head == capacity) { head = 0; }
        size--;
        return temp;
    }

    public E pollLast() {
        if (size == 0) { return null; }
        if (tail == 0) { tail = capacity; }
        E temp = (E) elements[--tail];
        elements[tail] = null;
        size--;
        return temp;
    }

    public int size() {
        return size;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder("[");
        if (head+size <= capacity) {
            for (int i = head; i < head+size; i++) { sb.append(elements[i]).append(", "); }
        } else {
            for (int i = head; i < capacity; i++) { sb.append(elements[i]).append(", "); }
            for (int i = 0; i < tail; i++) { sb.append(elements[i]).append(", "); }
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
    
    public boolean remove(Object o) { return false; }
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

}