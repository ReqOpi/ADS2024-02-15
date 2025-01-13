package by.it.group310902.strizhevskiy.lesson12;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/*
    Задание на уровень А

    Создайте class MyAvlMap, который реализует интерфейс Map<Integer, String>
    и работает на основе АВЛ-дерева
    БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ

    Метод toString() должен выводить элементы в порядке возрастания ключей
    Формат вывода: скобки (фигурные) и разделители
    (знак равенства и запятая с пробелом) должны
    быть такими же как в методе toString() обычной коллекции
*/

public class MyAvlMap<K extends Comparable,V> implements Map<K,V> {
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    private Entry<K,V> root;
    private int size;
    
    public void clear() { root = null; size = 0; }

    public boolean containsKey(Object key) {
        Entry<K,V> entry = root;
        while (entry != null) {
            int cmp = ((Comparable) key).compareTo(entry.getKey());
            if (cmp == 0) {
                return true;
            }
            entry = cmp < 0 ? entry.l : entry.r;
        }
        return false;
    }

    public V get(Object key) {
        Entry<K,V> entry = root;
        while (entry != null) {
            int cmp = ((Comparable) key).compareTo(entry.getKey());
            if (cmp == 0) {
                return entry.getValue();
            }
            entry = cmp < 0 ? entry.l : entry.r;
        }
        return null;
    }

    public boolean isEmpty() { return size == 0; }

    public V put(K key, V value) {
        Entry<K,V>[] entries = new Entry[34];
        int top = -1;

        Entry<K,V> entry = root;
        int cmp = 0;
        while (entry != null) {
            cmp = key.compareTo((entries[++top] = entry).getKey());
            if (cmp == 0) {
                return entry.setValue(value);
            }
            entry = cmp < 0 ? entry.l : entry.r;
        }

        entry = new Entry(key, value);
        size++;

        if (top == -1) {
            root = entry;
            return null;
        }

        if (cmp < 0) {
            entries[top].l = entry;
        } else {
            entries[top].r = entry;
        }

        while (top >= 0) {
            updateHeight(entries[top]);
            balance(entries, top);
            top--;
        }

        return null;
    }

    public V remove(Object key) {
        Entry<K,V>[] entries = new Entry[34];
        int top = -1;

        Entry<K,V> entry = root;
        int cmp = 0;
        while (entry != null) {
            cmp = ((Comparable) key).compareTo((entries[++top] = entry).getKey());
            if (cmp == 0) {
                return remove(entries, top);
            }
            entry = cmp < 0 ? entry.l : entry.r;
        }

        return null;
    }

    public int size() { return size; }

    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        toString(sb, root);
        int len = sb.length();
        if (len > 1) { sb.setLength(len-2); }
        sb.append("}");
        return sb.toString();
    }

    private void toString(StringBuilder sb, Entry<K,V> entry) {
        if (entry == null) { return; }
        toString(sb, entry.l);
        sb.append(entry.getKey());
        sb.append("=");
        sb.append(entry.getValue());
        sb.append(", ");
        toString(sb, entry.r);
    }

    private int getHeight(Entry entry) { return entry == null ? -1 : entry.h; }

    private int getBalance(Entry entry) { return entry == null ? 0 : getHeight(entry.r)-getHeight(entry.l); }

    private void updateHeight(Entry entry) {
        int lh = getHeight(entry.l);
        int rh = getHeight(entry.r);
        entry.h = 1+Math.max(lh,rh);
    }

    private void rightRotate(Entry[] entries, int top) {
        Entry entry = entries[top];
        Entry tempL = entry.l;
        Entry tempLR = tempL.r;

        tempL.r = entry;
        entry.l = tempLR;

        updateHeight(entry);
        updateHeight(tempL);

        if (top == 0) {
            root = tempL;
            return;
        }
        
        Entry parent = entries[top-1];
        if(entry == parent.l) { parent.l = tempL; }
        else { parent.r = tempL; }
    }

    private void leftRotate(Entry[] entries, int top) {
        Entry entry = entries[top];
        Entry tempR = entry.r;
        Entry tempRL = tempR.l;

        tempR.l = entry;
        entry.r = tempRL;

        updateHeight(entry);
        updateHeight(tempR);

        if (top == 0) {
            root = tempR;
            return;
        }
        
        Entry parent = entries[top-1];
        if(entry == parent.l) { parent.l = tempR; }
        else { parent.r = tempR; }
    }

    private void balance(Entry[] entries, int top) {
        int balance = getBalance(entries[top]);
        if (balance == -2) {
            if (getBalance(entries[top].l) == 1) {
                entries[top+1] = entries[top].l;
                leftRotate(entries, top+1);
            }
            rightRotate(entries, top);
        } else if (balance == 2) {
            if (getBalance(entries[top].r) == -1) {
                entries[top+1] = entries[top].r;
                rightRotate(entries, top+1);
            }
            leftRotate(entries, top);
        }
    }

    private V remove(Entry<K,V>[] entries, int top) {
        Entry<K,V> entry = entries[top];
        Entry<K,V> child;
        V value = entry.getValue();
        size--;
        if (entry.l == null || entry.r == null) {
            child = entry.l != null ? entry.l : entry.r;

            if (top == 0) {
                root = child;
                return value;
            }

            Entry<K,V> parent = entries[top-1];
            if(entry == parent.l) { parent.l = child; }
            else { parent.r = child; }
        } else {
            child = entry.l;

            while (child != null) {
                child = (entries[++top] = child).r;
            }
            child = entries[top];

            entry.k = child.k;
            entry.v = child.v;

            Entry<K,V> parent = entries[top-1];
            if(child == parent.l) { parent.l = child.l; }
            else { parent.r = child.l; }
        }
        
        while (top >= 0) {
            System.out.println(top);
            updateHeight(entries[top]);
            balance(entries, top);
            top--;
        }

        return value;
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    public boolean containsValue(Object value) { return false; }
    public Set<Map.Entry<K,V>> entrySet() { return null; }
    public Set<K> keySet() { return null; }
    public void putAll(Map<? extends K,? extends V> m) {}
    public Collection<V> values() { return null; }

    private static class Entry<K,V> implements Map.Entry<K,V> {

        public Entry<K,V> l, r;
        public int h;
        
        public K k;
        public V v;

        public Entry(K key, V value) {
            this.k = key;
            this.v = value;
        }

        public K getKey() { return k; }
        public V getValue() { return v; }

        public V setValue(V value) {
            V old = v;
            v = value;
            return old;
        }
    }

}