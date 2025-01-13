package by.it.group310902.strizhevskiy.lesson13;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeMap;

/*
Создайте класс GraphA в методе main которого считывается строка структуры орграфа вида:
0 -> 2, 1 -> 3, 2 -> 3, 0 -> 1

Затем в консоль выводится его топологическая сортировка вида:
0 1 2 3

P.S. При равнозначности вершин их порядок вывода - лексикографический (т.е. по алфавиту)
*/

public class GraphA {

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
		for (Node node : nodes.values()) { node.visited = false; node.next = 0; }

		Node[] sorted = new Node[nodes.size()];
		int top, sediment = nodes.size()-1;

		for (Node n : nodes.values()) {

			if (n.visited) { continue; }

			n.visited = true;
			
			sorted[0] = n;
			top = 0;

			while (0 <= top) {
				Node next = sorted[top].next();
				if (next == null) { sorted[sediment--] = sorted[top--]; continue; }
				if (next.visited) { continue; }
				next.visited = true;
				sorted[++top] = next;
			}

		}

		for (Node node : sorted) { System.out.printf("%s ", node.key); }
	}

	private static class Node {
		public ArrayList<Node> from = new ArrayList<>();
		public ArrayList<Node> to = new ArrayList<>();

		public boolean visited;
		public int next;

		public String key;

		public Node(String key) {
			this.key = key;
		}

		public Node next() {
			return next < to.size() ? to.get(next++) : null;
		}
	}

	public static void main(String[] args) {
		GraphA graph = new GraphA();
		graph.init();
		graph.sort();	//no cycles
	}

}