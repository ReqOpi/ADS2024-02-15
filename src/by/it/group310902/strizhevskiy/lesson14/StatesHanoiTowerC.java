package by.it.group310902.strizhevskiy.lesson14;

import java.util.Scanner;

/*
Создайте класс StatesHanoiTowerC с методом main.

Самостоятельно или с помощью интернета, или с помощью ботов типа ChatGPT
найдите и реализуйте решение задачи "Ханойские башни":

Даны три стержня A B C, на один из которых нанизаны N колец,
причём кольца отличаются размером и лежат меньшее на большем.
Задача состоит в том, чтобы перенести пирамиду из N колец за
наименьшее число ходов на другой стержень.
За один раз разрешается переносить только одно кольцо,
причём нельзя класть большее кольцо на меньшее.

В методе main вводится высота N стартовой пирамиды А.
Переместить кольца нужно на пустую пирамиду B.
В задаче есть еще одна пустая пирамида С.

Сгруппируйте с помощью DSU в поддеревья все те шаги решения задачи
у которых одинаковая наибольшая высота пирамид A B C.
Стартовое состояние учитывать не нужно.

Выведите на консоль размеры получившихся поддеревьев в порядке возрастания.
Коллекциями пользоваться нельзя!
Эвристики DSU:
1. по размеру поддерева
2. по сокращению пути поддерева

=====================================================================
Пример1:
Ввод:
10

Вывод:
1 4 38 64 252 324 340

=====================================================================
Пример2:
Ввод:
5

Вывод:
1 4 8 18

*/

public class StatesHanoiTowerC {

	int[][] tower = new int[3][];
	int[] top = new int[3];

	int[] height;

	int size;

	public void init(int s) {
		size = s;

		height = new int[size];

		tower[0] = new int[size];
		tower[1] = new int[size];
		tower[2] = new int[size];

		for (int i = 0; i < size; i++) {
			tower[0][i] = size - i;
		}

		top[0] = size;
	}

	public void move() {
		boolean changed = move(size, 0, 1);
		if (changed) { countState(); /*printState();*/ }
	}

	public boolean move(int size, int from, int to) {
		if (size == 0 || top[from] == 0) { return false; }

		boolean changed;

		changed = move(size-1, from, 3-from-to);
		if (changed) { countState(); /*printState();*/ }

		tower[to][top[to]++] = tower[from][--top[from]];
		countState();
		/*printState();*/

		return move(size-1, 3-from-to, to);
	}

	public void countState() {
		height[Math.max(Math.max(top[0],top[1]),top[2])-1]++;
	}

	public void printState() {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < top[i]; j++) { System.out.printf("%s ", tower[i][j]); }
			System.out.println();
		}
		System.out.println();
	}

	public static void main(String[] args) {
		StatesHanoiTowerC t = new StatesHanoiTowerC();
		t.init(new Scanner(System.in).nextInt());
		t.move();

		java.util.Arrays.sort(t.height);

		for (int i = 0; i < t.height.length; i++) {
			if (t.height[i] == 0) { continue; }
			System.out.printf("%s ", t.height[i]);
		}
	}
	
}