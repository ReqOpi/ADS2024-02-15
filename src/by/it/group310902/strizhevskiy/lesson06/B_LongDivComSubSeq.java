package by.it.group310902.strizhevskiy.lesson06;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
������ �� ����������������: ���������� ������� ���������������������

����:
    ����� ����� 1<=n<=1000
    ������ A[1�n] ����������� �����, �� ������������� 2E9.

����������:
    �������� ������������ 1<=k<=n, ��� �������� �������������� �������
    ��������������������� �������� i[1]<i[2]<�<i[k] <= ����� k,
    ��� ������� ������ ������� A[i[k]] ������� �� ����������
    �.�. ��� ���� 1<=j<k, A[i[j+1]] ������� �� A[i[j]].

������ ������ �������� ������������� ����������������

    Sample Input:
    4
    3 6 7 12

    Sample Output:
    3
*/

public class B_LongDivComSubSeq {


    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_LongDivComSubSeq.class.getResourceAsStream("dataB.txt");
        B_LongDivComSubSeq instance = new B_LongDivComSubSeq();
        int result = instance.getDivSeqSize(stream);
        System.out.print(result);
    }

    int getDivSeqSize(InputStream stream) throws FileNotFoundException {
        //���������� � ������ ������
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     ������ ������     !!!!!!!!!!!!!!!!!!!!!!!!!
        //����� ����� ������������������
        int n = scanner.nextInt();
        int[] m = new int[n];
        //������ ��� ������������������
        for (int i = 0; i < n; i++) {
            m[i] = scanner.nextInt();
        }
        //��� ���������� ������ ������ �������� ������������� ���������������� (!!!)
        int result = 0;

        int[] p = new int[n];

        for (int i = 0; i < n; i++) {
            p[i] = 1;
            for (int j = 0; j < i; j++) {
                if (m[j] != 0 && m[i] % m[j] == 0) {
                    p[i] = Math.max(p[i],p[j]+1);
                }
            }
        }

        for (int q : p) {
            result = Math.max(q, result);
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     ����� ������     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

}