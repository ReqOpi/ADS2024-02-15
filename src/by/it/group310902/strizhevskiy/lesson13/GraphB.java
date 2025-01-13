package by.it.group310902.strizhevskiy.lesson13;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeMap;

/*
Создайте класс GraphB в методе main которого считывается строка структуры орграфа вида:
1 -> 2, 1 -> 3, 2 -> 3

Затем в консоль выводится фраза о наличии циклов.
Возможные варианты yes и no.
Для указанного примера будет такой вывод:
no
*/

public class GraphB {
	
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

	public boolean hasCycles() {
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
				if (next.isVisited()) { return true; }
				sorted[++top] = next;
			}

		}

		return false;
	}

	private static class Node {
		public ArrayList<Node> from = new ArrayList<>();
		public ArrayList<Node> to = new ArrayList<>();

		public int next;

		public String key;

		public Node(String key) {
			this.key = key;
		}

		public boolean isCompleted() {
			return next > to.size();
		}

		public boolean isVisited() {
			return next != 0;
		}

		public Node next() {
			Node node = next < to.size() ? to.get(next) : null;
			next++;
			return node;
		}
	}

	public static void main(String[] args) {
		GraphB graph = new GraphB();
		graph.init();
		System.out.println(graph.hasCycles() ? "yes" : "no");
	}

}