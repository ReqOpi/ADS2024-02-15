package by.it.group310902.strizhevskiy.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;

/*
    Задание на уровень C

    Создайте class MyPriorityQueue<E>, который реализует интерфейс Queue<E>
    и работает на основе кучи, построенной на приватном массиве типа E[]
    БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ
*/

public class MyPriorityQueue<E extends Comparable> implements Deque<E> {
    
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    private int capacity = 10;
    private int size = 0;
    private Object[] heap = new Object[capacity];


    public boolean add(E e) {
        if (size == capacity) {
            Object[] temp = new Object[capacity = 1+size*3/2];
            System.arraycopy(heap, 0, temp, 0, size);
            heap = temp;
        }
        heap[size++] = e;
        siftUp(size-1);
        return true;
    }

    public boolean addAll(Collection<? extends E> c) {
        for (E element : c) { add(element); }
        return c.size() != 0;
    }

    public void clear() {
        for (int i = 0; i < size; i++) { heap[i] = null; }
        size = 0;
    }

    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if(!contains(o)) { return false; }
        }
        return true;
    }

    public E element() {
        return peek();
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean offer(E e) {
        return add(e);
    }

    public E peek() {
        return (E) heap[0];
    }

    public E poll() {
        if (size == 0) { return null; }
        E temp = (E) heap[0];
        heap[0] = heap[--size];
        heap[size] = null;
        siftDown(0);
        return temp;
    }

    public E remove() {
        return poll();
    }

    public boolean removeAll(Collection<?> c) {
        int oldsize = size;
        int newsize = 0;
        for (int i = 0; i < oldsize; i++) {
            if (!c.contains(heap[i])) {
                heap[newsize++] = heap[i];
            }
        }
        for (int i = newsize; i < oldsize; i++) {
            heap[i] = null;
        }
        size = newsize;
        heapify();
        return newsize != oldsize;
    }

    public boolean retainAll(Collection<?> c) {
        int oldsize = size;
        int newsize = 0;
        for (int i = 0; i < oldsize; i++) {
            if (c.contains(heap[i])) {
                heap[newsize++] = heap[i];
            }
        }
        for (int i = newsize; i < oldsize; i++) {
            heap[i] = null;
        }
        size = newsize;
        heapify();
        return newsize != oldsize;
    }

    public int size() {
        return size;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(heap[i]);
            sb.append(", ");
        }
        int len = sb.length();
        if (len > 1) { sb.setLength(len-2); }
        sb.append("]");
        return sb.toString();
    }

    private void siftDown(int i) {
        int c = i, l = 2*c+1, r = l+1, m;
        while(l < size){
            if(l == size-1){
                m = l;
            }
            else {
                m = ((E) heap[l]).compareTo(heap[r]) <= 0 ? l : r;
            }
            if(((E) heap[c]).compareTo(heap[m]) <= 0) break;
            E temp = (E) heap[c];
            heap[c] = heap[m];
            heap[m] = temp;
            c = m; l = 2*c+1; r = l+1;
        }
        i = c;
    }

    private void siftUp(int i) {
        int c = i, m = (c-1)/2;
        while(c != 0){
            if(((E) heap[c]).compareTo(heap[m]) >= 0) break;
            E temp = (E) heap[c];
            heap[c] = heap[m];
            heap[m] = temp;
            c = m; m = (c-1)/2;
        }
        i = c;
    }

    private void heapify() {
        for (int i = size/2; i >= 0; i--)
            siftDown(i);
    }

    private int indexOf(Object o) {
        for (int i = 0; i < size; i++) {
            if (heap[i].equals(o)) {
                return i;
            }
        }
        return -1;
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    
    public boolean remove(Object o) { return false; }
    public Iterator iterator() { return null; }
    public E getFirst() { return null; }
    public E getLast() { return null; }
    public void addFirst(E e) { return; }
    public void addLast(E e) { return; }
    public E removeFirst() { return null; }
    public E removeLast() { return null; }
    public void push(E e) { return; }
    public E pop() { return null; }
    public E pollFirst() { return null; }
    public E pollLast() { return null; }
    public boolean offerLast(E e) { return false; }
    public E peekFirst() { return null; }
    public boolean removeFirstOccurrence(Object o) { return false; }
    public boolean offerFirst(E e) { return false; }
    public E peekLast() { return null; }
    public boolean removeLastOccurrence(Object o) { return false; }
    public Iterator descendingIterator() { return null; }
    public <T> T[] toArray(T[] a) { return null; }
    public Object[] toArray() { return null; }

}