package by.it.group310902.strizhevskiy.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/*
    Задание на уровень C

    Создайте class MyTreeSet<E>, который реализует интерфейс Set<E>
    и работает на основе отсортированного массива (любым способом)
    БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ

    Метод toString() должен выводить элементы в порядке их возрастания
    Формат вывода: скобки (квадратные) и разделитель (запятая с пробелом) должны
    быть такими же как в методе toString() обычной коллекции
*/

public class MyTreeSet<E extends Comparable> implements Set<E> {
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    private int capacity = 10;
    private int size = 0;
    private Object[] elements = new Object[capacity];

    public boolean add(E e) {
        int index = indexOf(e);
        if (index < size && e.equals(elements[index])) {
            return false;
        }
        Object[] temp = size != capacity ? elements : new Object[capacity = 1+size*3/2];
        System.arraycopy(elements, 0, temp, 0, index);
        System.arraycopy(elements, index, temp, index+1, size-index);
        temp[index] = e;
        elements = temp;
        size++;
        return true;
    }

    public boolean addAll(Collection<? extends E> c) {
        boolean result = false;
        for (E e : c) { result |= add(e); }
        return result;
    }

    public void clear() {
        for (int i = 0; i < size; i++) { elements[i] = null; }
        size = 0;
    }

    public boolean contains(Object o) {
        int index = indexOf(o);
        return index < size && o.equals(elements[index]);
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
        int index = indexOf(o);
        if (index >= size || !o.equals(elements[index])) {
            return false;
        }
        System.arraycopy(elements,index+1,elements,index,size-index-1);
        elements[--size] = null;
        return true;
    }

    public boolean removeAll(Collection<?> c) {
        int i = 0;
        int j = 0;
        while (i < size){
            if (c.contains(elements[i])) { j--; }
            i++;
            j++;
            if (i < capacity) { elements[j] = elements[i]; }
        }
        boolean result = i != j;
        size = j;
        while (j < i) { elements[j++] = null; }
        return result;
    }

    public boolean retainAll(Collection<?> c) {
        int i = 0;
        int j = 0;
        while (i < size){
            if (!c.contains(elements[i])) { j--; }
            i++;
            j++;
            if (i < capacity) { elements[j] = elements[i]; }
        }
        boolean result = i != j;
        size = j;
        while (j < i) { elements[j++] = null; }
        return result;
    }

    public int size() {
        return size;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(elements[i]);
            sb.append(", ");
        }
        int len = sb.length();
        if (len > 1) { sb.setLength(len-2); }
        sb.append("]");
        return sb.toString();
    }

    // Вернёт индекс, на который должен быть вставлен элемент в отсортированном массиве
    private int indexOf(Object o) {
        if (size == 0) { return 0;}
        int l = 0, r = size-1, m, c;
        while (l < r){
            m = (l+r)/2;
            c = ((Comparable) o).compareTo(elements[m]);
            if (c == 0) { return m; }
            else if (c > 0) { l = m+1; }
            else { r = m-1; }
        }
        m = (l+r)/2;
        c = ((Comparable) o).compareTo(elements[m]);
        return c <= 0 ? m : m+1;
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    
    public Object[] toArray() { return null; }
    public <T> T[] toArray(T[] a) { return null; }
    public Iterator<E> iterator() { return null; }

}