package by.it.group310902.strizhevskiy.lesson13;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeMap;

/*
Создайте класс GraphC в методе main которого считывается строка структуры орграфа вида:
C->B, C->I, I->A, A->D, D->I, D->B, B->H, H->D, D->E, H->E, E->G, A->F, G->F, F->K, K->G

Затем в консоль выводятся вершины компонент сильной связности
каждый компонент с новой строки, первый - исток, последний - сток,
пробелов и табуляции в выводе нигде нет, пример для введенного выше графа:
C
ABDHI
E
FGK

P.S. При равнозначности вершин в компоненте порядок их вывода - лексикографический (т.е. по алфавиту)
*/

public class GraphC {

	private TreeMap<String,Node> nodes = new TreeMap<String,Node>( (a,b) -> { return b.compareTo(a); } );

	public void init() {
		Scanner sin = new Scanner(System.in);
		sin.useDelimiter("(\s*)((->)|(,))(\s*)");

		while (sin.hasNext()) {
			String from = sin.next();
			String to = sin.next();

			Node nodeFrom = nodes.get(from);
			if (nodeFrom == null) {
				nodeFrom = new Node(from);
				nodes.put(from,nodeFrom);
			}

			Node nodeTo = nodes.get(to);
			if (nodeTo == null) {
				nodeTo = new Node(to);
				nodes.put(to,nodeTo);
			}

			nodeFrom.to.add(nodeTo);
			nodeTo.from.add(nodeFrom);
		}

		for (Node node : nodes.values()) {
			node.to.sort( (a,b) -> { return b.key.compareTo(a.key); } );
		}
	}

	public void sort() {
		for (Node node : nodes.values()) { node.next = 0; }

		Node[] sorted = new Node[nodes.size()];
		int top, sediment = nodes.size()-1;

		for (Node n : nodes.values()) {

			if (n.isVisited()) { continue; }
			
			sorted[0] = n;
			top = 0;

			while (0 <= top) {
				Node next = sorted[top].next();
				if (next == null) { sorted[sediment--] = sorted[top--]; continue; }
				if (next.isCompleted()) { continue; }
				if (next.isVisited()) {
					int from = top, to = top;
					while (sorted[from] != next) {
						sorted[from].marker = sorted[from-1];
						from--;
					}
					continue;
				}
				sorted[++top] = next;
			}

		}

		int from = 0;
		int to = 1;

		while (from < sorted.length) {
			if (to < sorted.length && sorted[to-1] == sorted[to].marker) {
				to++;
				while (to < sorted.length && sorted[to-1].marker == sorted[to].marker) { to++; }
			}

			Arrays.sort(sorted, from, to, (a,b) -> { return a.key.compareTo(b.key); });

			for (int i = from; i < to; i++) { System.out.print(sorted[i].key); }
			System.out.println();

			from = to++;
		}
	}

	private static class Node {
		public ArrayList<Node> from = new ArrayList<>();
		public ArrayList<Node> to = new ArrayList<>();

		public int next;

		public Node marker;
		public String key;

		public Node(String key) {
			this.key = key;
		}

		public boolean isCompleted() {
			return marker == null ? next > to.size() : marker().isCompleted();
		}

		public boolean isVisited() {
			return next != 0;
		}

		public Node next() {
			Node node = next < to.size() ? to.get(next) : marker == null ? null : marker().next();
			next++;
			return node;
		}

		public Node marker() {
			if (marker == null || marker == this) { return null; }
			while (marker.next >= marker.to.size() && marker.marker() != null) { marker = marker.marker(); }
			return marker;
		}
	}

	public static void main(String[] args) {
		GraphC graph = new GraphC();
		graph.init();
		graph.sort();
	}

}