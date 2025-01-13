package by.it.group310902.strizhevskiy.lesson12;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Set;
import java.util.SortedMap;

/*
    Задание на уровень C

    Создайте class MySplayMap, который реализует интерфейс NavigableMap<Integer, String>
    и работает на основе splay-дерева
    БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ

    Метод toString() должен выводить элементы в порядке возрастания ключей
    Формат вывода: скобки (фигурные) и разделители
    (знак равенства и запятая с пробелом) должны
    быть такими же как в методе toString() обычной коллекции
*/

public class MySplayMap<K extends Comparable,V> implements NavigableMap<K,V> {
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    private Entry<K,V> root;
    private int size;
    
    public K ceilingKey(K key) {
        Entry<K,V> entry = ceilingEntry(key);
        return entry == null ? null : entry.k;
    }

    public void clear() {
        Entry<K,V> entry = root == null ? null : root.ldead();

        while (entry != null) {
            Entry<K,V> next = entry.nextLRP();
            entry.p = null;
            entry.l = null;
            entry.r = null;
            entry = next;
        }

        root = null;
        size = 0;
    }

    public boolean containsKey(Object key) {
        Entry<K,V> entry = root;
        while (entry != null) {
            int cmp = ((Comparable) key).compareTo(entry.k);

            if (cmp == 0) {
                return true;
            }

            entry = cmp < 0 ? entry.l : entry.r;
        }
        return false;
    }

    public boolean containsValue(Object value) {
        Entry<K,V> entry = root == null ? null : root.lmost();

        while (entry != null) {
            if (value.equals(entry.v)) {
                return true;
            }
            entry = entry.next();
        }

        return false;
    }

    public K firstKey() {
        return root == null ? null : root.lmost().k;
    }

    public K floorKey(K key) {
        Entry<K,V> entry = floorEntry(key);
        return entry == null ? null : entry.k;
    }

    public V get(Object key) {
        Entry<K,V> entry = root;
        while (entry != null) {
            int cmp = ((Comparable) key).compareTo(entry.k);

            if (cmp == 0) {
                splay(entry);
                return entry.v;
            }

            entry = cmp < 0 ? entry.l : entry.r;
        }
        return null;
    }

    public SortedMap<K,V> headMap(K toKey) {
        return new MySplaySubMap<K,V>(this, null, toKey);
    }

    public K higherKey(K key) {
        Entry<K,V> entry = floorEntry(key);
        if (entry == null) { return firstKey(); }
        entry = entry.next();
        return entry == null ? null : entry.k;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public K lastKey() {
        return root == null ? null : root.rmost().k;
    }

    public K lowerKey(K key) {
        Entry<K,V> entry = ceilingEntry(key);
        if (entry == null) { return lastKey(); }
        entry = entry.prev();
        return entry == null ? null : entry.k;
    }

    public V put(K key, V value) {
        Entry<K,V> parent = null;
        Entry<K,V> entry = root;
        int cmp = 0;
        while (entry != null) {
            cmp = key.compareTo(entry.getKey());
            if (cmp == 0) {
                splay(entry);
                return entry.setValue(value);
            }
            parent = entry;
            entry = cmp < 0 ? entry.l : entry.r;
        }

        entry = new Entry<K,V>(parent, key, value);
        size++;

        if (size == 1) {
            root = entry;
            return null;
        }

        if (cmp < 0) {
            parent.l = entry;
        } else {
            parent.r = entry;
        }
        
        splay(entry);

        return null;
    }

    public V remove(Object key) {
        Entry<K,V> entry = root;
        int cmp = 0;
        while (entry != null) {
            cmp = ((Comparable) key).compareTo(entry.getKey());
            if (cmp == 0) {
                return remove(entry);
            }
            entry = cmp < 0 ? entry.l : entry.r;
        }
        return null;
    }

    public int size() {
        return size;
    }

    public SortedMap<K,V> tailMap(K fromKey) {
        return new MySplaySubMap(this, fromKey, null);
    }

    public String toString() {
        Entry<K,V> entry = root == null ? null : root.lmost();
        StringBuilder sb = new StringBuilder("{");

        while (entry != null) {
            sb.append(entry.k.toString());
            sb.append("=");
            sb.append(entry.v.toString());
            sb.append(", ");
            entry = entry.next();
        }

        int len = sb.length();
        if (len > 1) { sb.setLength(len-2); }
        sb.append("}");
        return sb.toString();
    }

    public Entry<K,V> ceilingEntry(K key) {
        Entry<K,V> parent = null;
        Entry<K,V> entry = root;
        int cmp = 0;
        while (entry != null) {
            cmp = key.compareTo(entry.getKey());
            if (cmp == 0) {
                return entry;
            }
            parent = entry;
            entry = cmp < 0 ? entry.l : entry.r;
        }
        return parent == null ? null : cmp < 0 ? parent : parent.next();
    }

    public Entry<K,V> floorEntry(K key) {
        Entry<K,V> parent = null;
        Entry<K,V> entry = root;
        int cmp = 0;
        while (entry != null) {
            cmp = key.compareTo(entry.getKey());
            if (cmp == 0) {
                return entry;
            }
            parent = entry;
            entry = cmp < 0 ? entry.l : entry.r;
        }
        return parent == null ? null : cmp < 0 ? parent.prev() : parent;
    }

    private V remove(Entry<K,V> entry) {
        Entry<K,V> child;
        V value = entry.getValue();
        size--;
        if (entry.l == null || entry.r == null) {
            child = entry.l != null ? entry.l : entry.r;

            if (entry == root) {
                root = child;
                child.p = null;
                return value;
            }

            Entry<K,V> parent = entry.p;
            if (child != null) { child.p = parent; }
            if(entry == parent.l) { parent.l = child; }
            else { parent.r = child; }
            splay(parent);
        } else {
            child = entry.l.rmost();

            entry.k = child.k;
            entry.v = child.v;

            Entry<K,V> parent = child.p;
            if (child.l != null) { child.l.p = parent; }
            if(child == parent.l) { parent.l = child.l; }
            else { parent.r = child.l; }
            splay(parent);
        }

        return value;
    }

    private void splay(Entry entry) {
        if (entry == null) { return; }

        while (entry != root) {
            Entry parent = entry.p;

            if (parent == root) {
                if (parent.l == entry) { rightRotate(parent); } else { leftRotate(parent); } //zig
                continue;
            }

            Entry grandad = parent.p;

            if (grandad.l == parent) {
                if (parent.l == entry) {    //zig-zig
                    rightRotate(grandad);
                    rightRotate(parent);
                } else {
                    leftRotate(parent);     //zig-zag
                    rightRotate(grandad);
                }
            } else {
                if (parent.r == entry) {    //zig-zig
                    leftRotate(grandad);
                    leftRotate(parent);
                } else {
                    rightRotate(parent);    //zig-zag
                    leftRotate(grandad);
                }
            }
        }

    }

    private void rightRotate(Entry entry) {
        Entry parent = entry.p;
        Entry tempL = entry.l;
        Entry tempLR = tempL.r;

        tempL.r = entry;
        entry.p = tempL;

        entry.l = tempLR;
        if (tempLR != null) { tempLR.p = entry; }

        if (tempL != null) { tempL.p = parent; }

        if (entry == root) {
            root = tempL;
            return;
        }
        
        if(entry == parent.l) { parent.l = tempL; }
        else { parent.r = tempL; }
    }

    private void leftRotate(Entry entry) {
        Entry parent = entry.p;
        Entry tempR = entry.r;
        Entry tempRL = tempR.l;

        tempR.l = entry;
        entry.p = tempR;

        entry.r = tempRL;
        if (tempRL != null) { tempRL.p = entry; }

        if (tempR != null) { tempR.p = parent; }

        if (entry == root) {
            root = tempR;
            return;
        }
        
        if(entry == parent.l) { parent.l = tempR; }
        else { parent.r = tempR; }
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    public Comparator<? super K> comparator() { return null; }
    public NavigableSet<K> descendingKeySet() { return null; }
    public NavigableMap<K,V> descendingMap() { return null; }
    public Set<Map.Entry<K,V>> entrySet() { return null; }
    public Map.Entry<K,V> firstEntry() { return null; }
    public NavigableMap<K,V> headMap(K toKey, boolean inclusive) { return null; }
    public Map.Entry<K,V> higherEntry(K key) { return null; }
    public Set<K> keySet() { return null; }
    public Map.Entry<K,V> lastEntry() { return null; }
    public Map.Entry<K,V> lowerEntry(K key) { return null; }
    public NavigableSet<K> navigableKeySet() { return null; }
    public Map.Entry<K,V> pollFirstEntry() { return null; }
    public Map.Entry<K,V> pollLastEntry() { return null; }
    public void putAll(Map<? extends K,? extends V> m) {}
    public NavigableMap<K,V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) { return null; }
    public SortedMap<K,V> subMap(K fromKey, K toKey) { return null; }
    public NavigableMap<K,V> tailMap(K fromKey, boolean inclusive) { return null; }
    public Collection<V> values() { return null; }

    private static class Entry<K,V> implements Map.Entry<K,V> {

        public Entry<K,V> p;
        public Entry<K,V> l;
        public Entry<K,V> r;
        
        public K k;
        public V v;

        public Entry(Entry<K,V> parent, K key, V value) {
            this.p = parent;
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

        public Entry<K,V> next() {
            return nextLPR();
        }

        public Entry<K,V> prev() {
            return prevLPR();
        }

        // Inorder, First - root.lmost()
        public Entry<K,V> nextLPR() {
            Entry<K,V> entry = this;
            Entry<K,V> child = null;

            if (entry.r != null) { return entry.r.lmost(); }

            do { child = entry; entry = entry.p; } while (entry != null && child == entry.r);

            return entry;
        }

        // Inorder, Last - root.rmost()
        public Entry<K,V> prevLPR() {
            Entry<K,V> entry = this;
            Entry<K,V> child = null;

            if (entry.l != null) { return entry.l.rmost(); }

            do { child = entry; entry = entry.p; } while (entry != null && child == entry.l);

            return entry;
        }

        // Preorder, First - root.ldead()
        public Entry<K,V> nextLRP() {
            if (p != null && p.r != null && p.r != this) { return p.r.ldead(); }

            return p;
        }

        public Entry<K,V> lmost() {
            Entry<K,V> entry = this;
            while (entry.l != null) { entry = entry.l; }
            return entry;
        }

        public Entry<K,V> rmost() {
            Entry<K,V> entry = this;
            while (entry.r != null) { entry = entry.r; }
            return entry;
        }

        public Entry<K,V> ldead() {
            Entry<K,V> entry = this.lmost();
            while (entry.r != null) { entry = entry.r.lmost(); }
            return entry;
        }

        public Entry<K,V> rdead() {
            Entry<K,V> entry = this.rmost();
            while (entry.l != null) { entry = entry.l.rmost(); }
            return entry;
        }

    }

    private static class MySplaySubMap<K extends Comparable,V> implements SortedMap<K,V> {
        private MySplayMap<K,V> map;
        private K fromKey;
        private K toKey;

        public MySplaySubMap(MySplayMap<K,V> map, K fromKey, K toKey) {
            this.map = map;
            this.fromKey = fromKey;
            this.toKey = toKey;
        }

        public void clear() {
            if (map.isEmpty()) { return; }

            MySplayMap.Entry<K,V> entry = fromKey == null ? map.root.lmost() : map.ceilingEntry(fromKey);
            MySplayMap.Entry<K,V> to = toKey == null ? null : map.ceilingEntry(toKey);

            while (entry != null && entry != to) {
                MySplayMap.Entry<K,V> next = entry.next();
                map.remove(entry.k);
                entry = next;
            }
        }

        public boolean containsKey(Object key) {
            if (fromKey != null && fromKey.compareTo(key) > 0) {
                return false;
            }
            if (toKey != null && toKey.compareTo(key) <= 0) {
                return false;
            }
            return map.containsKey(key);
        }

        public boolean containsValue(Object value) {
            if (map.isEmpty()) { return false; }

            MySplayMap.Entry<K,V> entry = fromKey == null ? map.root.lmost() : map.ceilingEntry(fromKey);
            MySplayMap.Entry<K,V> to = toKey == null ? null : map.ceilingEntry(toKey);

            while (entry != null && entry != to) {
                if (value.equals(entry.v)) {
                    return true;
                }
                entry = entry.next();
            }

            return false;
        }

        public K firstKey() {
            return fromKey == null ? map.firstKey() : map.ceilingEntry(fromKey).k;
        }

        public V get(Object key) {
            if (fromKey != null && fromKey.compareTo(key) > 0) {
                return null;
            }
            if (toKey != null && toKey.compareTo(key) <= 0) {
                return null;
            }
            return map.get(key);
        }

        public SortedMap<K,V> headMap(K toKey) {
            if (this.toKey != null && this.toKey.compareTo(toKey) <= 0) {
                return this;
            }
            return new MySplaySubMap<K,V>(map, fromKey, toKey);
        }

        public boolean isEmpty() {
            return size() == 0;
        }

        public K lastKey() {
            return toKey == null ? map.lastKey() : map.floorEntry(toKey).k;
        }

        public V put(K key, V value) {
            if (fromKey != null && fromKey.compareTo(key) > 0) {
                return null;
            }
            if (toKey != null && toKey.compareTo(key) <= 0) {
                return null;
            }
            return map.put(key, value);
        }

        public V remove(Object key) {
            if (fromKey != null && fromKey.compareTo(key) > 0) {
                return null;
            }
            if (toKey != null && toKey.compareTo(key) <= 0) {
                return null;
            }
            return map.remove(key);
        }

        public int size() {
            if (map.isEmpty()) { return 0; }

            MySplayMap.Entry<K,V> entry = fromKey == null ? map.root.lmost() : map.ceilingEntry(fromKey);
            MySplayMap.Entry<K,V> to = toKey == null ? null : map.ceilingEntry(toKey);

            int size = 0;

            while (entry != null && entry != to) {
                size++;
                entry = entry.next();
            }

            return size;
        }

        public SortedMap<K,V> tailMap(K fromKey) {
            if (this.fromKey != null && this.fromKey.compareTo(fromKey) > 0) {
                return this;
            }
            return new MySplaySubMap<K,V>(map, fromKey, toKey);
        }

        public String toString() {
            if (map.isEmpty()) { return "{}"; }

            MySplayMap.Entry<K,V> entry = fromKey == null ? map.root.lmost() : map.ceilingEntry(fromKey);
            MySplayMap.Entry<K,V> to = toKey == null ? null : map.ceilingEntry(toKey);
            StringBuilder sb = new StringBuilder("{");
    
            while (entry != null && entry != to) {
                sb.append(entry.k.toString());
                sb.append("=");
                sb.append(entry.v.toString());
                sb.append(", ");
                entry = entry.next();
            }
    
            int len = sb.length();
            if (len > 1) { sb.setLength(len-2); }
            sb.append("}");
            return sb.toString();
        }

        /////////////////////////////////////////////////////////////////////////

        public Comparator<? super K> comparator() { return null; }
        public Set<Map.Entry<K,V>> entrySet() { return null; }
        public Set<K> keySet() { return null; }
        public void putAll(Map<? extends K,? extends V> m) {}
        public SortedMap<K,V> subMap(K fromKey, K toKey) { return null; }
        public Collection<V> values() { return null; }
    }

}