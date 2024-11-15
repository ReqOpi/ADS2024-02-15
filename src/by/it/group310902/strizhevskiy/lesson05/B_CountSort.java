package by.it.group310902.strizhevskiy.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
������ ������ �������� ����� 1<=n<=10000, ������ - n ����������� �����, �� ����������� 10.
�������� ������������� �� ���������� ������������������ ���� �����.

��� ���������� ���������� ����� �� ���������� O(n)

������: https://karussell.wordpress.com/2010/03/01/fast-integer-sorting-algorithm-on/
������� �������: http://programador.ru/sorting-positive-int-linear-time/
*/

public class B_CountSort {


    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_CountSort.class.getResourceAsStream("dataB.txt");
        B_CountSort instance = new B_CountSort();
        int[] result = instance.countSort(stream);
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    int[] countSort(InputStream stream) throws FileNotFoundException {
        //���������� � ������ ������
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     ������ ������     !!!!!!!!!!!!!!!!!!!!!!!!!
        //������ �������
        int n = scanner.nextInt();
        int[] points = new int[n];

        //������ �����
        for (int i = 0; i < n; i++) {
            points[i] = scanner.nextInt();
        }
        //��� ���������� ������ ������ � ����������� ���������� ���������

        int[] count = new int[11];

        for (int p : points) { count[p]++; }

        for (int c = 0, p = 0; c < count.length; c++) {
            while (0 <= --count[c]) { points[p++] = c; }
        }
        

        //!!!!!!!!!!!!!!!!!!!!!!!!!     ����� ������     !!!!!!!!!!!!!!!!!!!!!!!!!
        return points;
    }

}
