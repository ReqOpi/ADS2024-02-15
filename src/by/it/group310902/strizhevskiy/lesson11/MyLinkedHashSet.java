package by.it.group310902.strizhevskiy.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/*
    Задание на уровень B

    Создайте class MyLinkedHashSet<E>, который реализует интерфейс Set<E>
    и работает на основе массива с адресацией по хеш-коду
    и односвязным списком для элементов с коллизиями
    БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ

    Метод toString() должен выводить элементы в порядке их добавления в коллекцию
    Формат вывода: скобки (квадратные) и разделитель (запятая с пробелом) должны
    быть такими же как в методе toString() обычной коллекции
*/

public class MyLinkedHashSet<E> implements Set<E> {
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    private int capacity = 128;
    private int size = 0;
    private Node<E>[] table = new Node[capacity];

    private Node<E> head = new Node<E>();
    private Node<E> tail = head;

    public boolean add(E e) {
        int hash = e.hashCode() & (capacity-1);
        Node<E> node = table[hash];
        while (node != null) {
            if (node.element.equals(e)) { return false; }
            node = node.under;
        }
        tail.element = e;
        tail.under = table[hash];
        table[hash] = tail;
        tail.after = new Node<E>();
        tail.after.before = tail;
        tail = tail.after;
        size++;
        return true;
    }

    public boolean addAll(Collection<? extends E> c) {
        boolean result = false;
        for (E e : c) { result |= add(e); }
        return result;
    }

    public void clear() {
        for (int i = 0; i < capacity; i++) { table[i] = null; }
        for (int i = 0; i < size; i++) {
            head.before = null;
            head.under = null;
            head = head.after;
        }
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

    public boolean containsAll(Collection<?> c) {
        boolean result = true;
        for (Object o : c) { result &= contains(o); }
        return result;
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

            if (node == head) { head = node.after; }
            else { node.before.after = node.after; }
            node.after.before = node.before;

            size--;
            return true;
        }

        Node<E> under = node.under;
        while (under != null) {
            if (under.element.equals(o)) {
                node.under = under.under;

                if (under == head) { head = under.after; }
                else { under.before.after = under.after; }
                under.after.before = under.before;

                size--;
                return true;
            }
            node = under;
            under = under.under;
        }
        return false;
    }

    public boolean removeAll(Collection<?> c) {
        boolean result = false;
        for (Object o : c) { result |= remove(o); }
        return result;
    }

    public boolean retainAll(Collection<?> c) {
        boolean result = false;
        Node<E> node = head;
        while (node != tail) {
            if (!c.contains(node.element)) { result |= remove(node.element); }
            node = node.after;
        }
        return result;
    }

    public int size() {
        return size;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder("[");
        Node<E> node = head;
        while (node != tail) {
            sb.append(node.element);
            sb.append(", ");
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

    public Object[] toArray() { return null; }
    public <T> T[] toArray(T[] a) { return null; }
    public Iterator<E> iterator() { return null; }

    private static class Node<E> {
        public Node<E> before;
        public Node<E> after;
        public Node<E> under;

        public E element;
    }

}