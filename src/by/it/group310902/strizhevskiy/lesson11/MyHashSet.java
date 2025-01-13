package by.it.group310902.strizhevskiy.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/*
    Задание на уровень А

    Создайте class MyHashSet<E>, который реализует интерфейс Set<E>
    и работает на основе массива с адресацией по хеш-коду
    и односвязным списком для элементов с коллизиями
    БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ

    Метод toString() может выводить элементы в произвольном порядке
    Формат вывода: скобки (квадратные) и разделитель (запятая с пробелом) должны
    быть такими же как в методе toString() обычной коллекции
*/

public class MyHashSet<E> implements Set<E> {
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    private int capacity = 128;
    private int size = 0;
    private Node<E>[] table = new Node[capacity];


    public boolean add(E e) {
        int hash = e.hashCode() & (capacity-1);
        Node<E> node = table[hash];
        while (node != null) {
            if (node.element.equals(e)) { return false; }
            node = node.under;
        }
        table[hash] = new Node(e, table[hash]);
        size++;
        return true;
    }

    public void clear() {
        for (int i = 0; i < capacity; i++) { table[i] = null; }
        size = 0;
    }

    public boolean contains(Object o) {
        int hash = o.hashCode() & (capacity-1);
        Node<E> node = table[hash];
        while (node != null) {
            if (node.element.equals(o)) { return true; }
            node = node.under;
        }
        return false;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean remove(Object o) {
        int hash = o.hashCode() & (capacity-1);
        Node<E> node = table[hash];
        if (node == null) { return false; }
        else if (node.element.equals(o)) {
            table[hash] = node.under;
            size--;
            return true;
        }
        Node<E> under;
        while ((under = node.under) != null) {
            if (under.element.equals(o)) {
                node.under = under.under;
                size--;
                return true;
            }
            node = under;
        }
        return false;
    }

    public int size() {
        return size;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < capacity; i++) {
            Node<E> node = table[i];
            while (node != null) {
                sb.append(node.element);
                sb.append(", ");
                node = node.under;
            }
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
    
    public Object[] toArray() { return null; }
    public <T> T[] toArray(T[] a) { return null; }
    public Iterator<E> iterator() { return null; }
    public boolean addAll(Collection<? extends E> c) { return false; }
    public boolean removeAll(Collection<?> c) { return false; }
    public boolean retainAll(Collection<?> c) { return false; }
    public boolean containsAll(Collection<?> c) { return false; }

    private static class Node<E> {
        Node<E> under;
        E element;

        public Node(E element, Node<E> under) {
            init(element, under);
        }

        public final void init(E element, Node<E> under) {
            this.element = element;
            this.under = under;
        }
    }

}