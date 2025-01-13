package by.it.group310902.strizhevskiy.lesson14;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/*
Создайте класс PointsA с методом main.

Пусть у нас есть набор точек в трехмерном пространстве,
и мы хотим разбить их на кластеры на основе расстояний между ними.

Используем структуру данных DSU
с эвристикой по рангу или размеру поддерева
для объединения близких точек в один кластер.

С консоли вводится допустимое расстояние D между точками НЕ ВКЛЮЧИТЕЛЬНО [0,D) и число точек N,
а затем в каждой новой строке вводится точка с координатами X Y Z через пробел.

Точки объединяются в DSU если расстояние между ними допустимо.
Нужно вывести на консоль число точек в полученных кластерах (в порядке возрастания).
Пример:

Ввод:
2 8
1 1 1
9 9 9
1 2 2
9 8 9
3 2 3
3 4 4
8 8 9
5 6 5

Вывод:
3 5
*/

public class PointsA {

	public static void main(String[] args) {
		Scanner sin = new Scanner(System.in);

		int max = sin.nextInt();
		int size = sin.nextInt();

		Point[] points = new Point[size];
		int[] parent = new int[size];
		int[] counts = new int[size];

		for (int i = 0; i < size; i++) {
			points[i] = new Point();
			points[i].x = sin.nextInt();
			points[i].y = sin.nextInt();
			points[i].z = sin.nextInt();
			parent[i] = i;
			counts[i] = 1;
		}

		for (int i = 0; i < size; i++){
			for (int j = i+1; j < size; j++){
				double dist = points[i].dist(points[j]);
				if (!(dist < max)) { continue; }

				int a = i;
				int b = j;
				int p, c;

				while (parent[a] != a) { a = parent[a]; }
				while (parent[b] != b) { b = parent[b]; }

				if (a == b) { continue; }

				if (counts[a] < counts[b]) { p = b; c = a; }
				else { p = a; c = b;}

				counts[p] += counts[c];

				a = i;
				b = j;

				while (parent[a] != p) { c = parent[a]; parent[a] = p; a = c; }
				while (parent[b] != p) { c = parent[b]; parent[b] = p; b = c; }

			}
		}
		
		ArrayList<Integer> sortedCounts = new ArrayList<>();

		boolean[] visited = new boolean[size];

		for (int i = 0; i < size; i++) {
			int c, p = i, a = i;
			while (parent[p] != p) { p = parent[p]; }
			while (parent[a] != p) { c = parent[a]; parent[a] = p; a = c; }

			if (!visited[p]) { sortedCounts.add(counts[p]); }
			visited[p] = true;
		}

		sortedCounts.sort((a,b) -> { return b-a; });

		for (Integer count : sortedCounts) {
			System.out.printf("%s ", count);
		}
	}

	private static class Point {
		int x, y, z;
		public double dist(Point p) {
			int dx = (x - p.x);
			int dy = (y - p.y);
			int dz = (z - p.z);
			return Math.hypot(Math.hypot(dx,dy),dz);
		}
	}
	
}