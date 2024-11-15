package by.it.group310902.strizhevskiy.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
����������������� � �������.
�� ������� ����������� ���� ��� ��������� �����.
�������� ������ � ���, ����� ������ �� ��� ���������� � ����������� (������� ������)
�������� ������ ������� �� ������� (����� ������ ������� �������).
��� ���������� ���������� ��� ������� ������� ������� ����� ��� ��������.

� ������ ������ ������ ��� ����� �����:
    ����� ��������� ����� (�������) 1<=n<=50000
    ����� ������� (�����) 1<=m<=50000.

��������� n ����� �������� �� ��� ����� ����� ai � bi (ai<=bi) -
���������� ������ �������� (����� ������ ����� �����-�� ������).
��������� ������ �������� m ����� ����� - ���������� �����.
��� ���������� �� ��������� 10E8 �� ������ (!).

����� ��������� ������������� �������, ���� ��� ��������� ������ ���� ��� �� �������.

��� ������ ����� � ������� �� ��������� �� ����� ��������,
�������� �������� ��� �����������.
    Sample Input:
    2 3
    0 5
    7 10
    1 6 11
    Sample Output:
    1 0 0

*/

public class A_QSort {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_QSort.class.getResourceAsStream("dataA.txt");
        A_QSort instance = new A_QSort();
        int[] result = instance.getAccessory(stream);
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    int[] getAccessory(InputStream stream) throws FileNotFoundException {
        //���������� � ������ ������
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     ������ ������     !!!!!!!!!!!!!!!!!!!!!!!!!
        //����� �������� ���������������� �������
        int n = scanner.nextInt();
        Segment[] segments = new Segment[n];
        //����� �����
        int m = scanner.nextInt();
        int[] points = new int[m];
        int[] result = new int[m];

        //������ ���� �������
        for (int i = 0; i < n; i++) {
            //������ ������ � ����� ������� �������
            segments[i] = new Segment(scanner.nextInt(), scanner.nextInt());
        }
        //������ �����
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }
        //��� ���������� ������ ������ � ����������� ������� ����������
        //� ������ ������� Segment ���������� ������ ��� ���� ������ ����������

        qsort(segments, 0, segments.length);

        for (int i = 0; i < points.length; i++) {
            for (Segment s : segments) {
                if (points[i] < s.start) { break; }
                if (points[i] <= s.stop)
                    result[i]++;
            }
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     ����� ������     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    private <T extends Comparable<T>> void qsort(T[] a, int from, int to){
        if (to-from <= 1) { return; }

        int l = from, r = to-1, m = (int)(Math.random()*(to-from)+from);
        T pivot = a[m];

        while (true) {
            while (a[l].compareTo(pivot) < 0) { l++; }
            while (a[r].compareTo(pivot) > 0) { r--; }

            if (l >= r) { break; }

            T temp = a[l];
            a[l++] = a[r];
            a[r--] = temp;
        }
        
        qsort(a,from,l);
        qsort(a,l,to);
    }

    //�������
    private class Segment implements Comparable<Segment> {
        int start;
        int stop;

        Segment(int start, int stop) {
            this.start = start;
            this.stop = stop;
            //��� ������-�� ����� �������� ����������� �� ������ ����
            //����� �������� ������ � �������� �������
        }

        @Override
        public int compareTo(Segment o) {
            return (int) Math.signum(start-o.start);
        }
    }

}
