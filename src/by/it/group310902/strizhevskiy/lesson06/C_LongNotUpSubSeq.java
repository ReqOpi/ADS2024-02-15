package by.it.group310902.strizhevskiy.lesson06;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
������ �� ����������������: ���������� �������������� ���������������������

����:
    ����� ����� 1<=n<=1E5 ( �������� �������� �� �����������! )
    ������ A[1�n] ����������� �����, �� ������������� 2E9.

����������:
    �������� ������������ 1<=k<=n, ��� �������� �������������� �������
    ��������������������� �������� i[1]<i[2]<�<i[k] <= ����� k,
    ��� ������� ������ ������� A[i[k]] �� ������ ������ �����������
    �.�. ��� ���� 1<=j<k, A[i[j]]>=A[i[j+1]].

    � ������ ������ �������� � ����� k,
    �� ������ - � ������� i[1]<i[2]<�<i[k]
    �������� A[i[1]]>=A[i[2]]>= ... >=A[i[n]].

    (������ ���������� � 1)

������ ������ �������� ������������� ����������������

    Sample Input:
    5
    5 3 4 4 2

    Sample Output:
    4
    1 3 4 5
*/


public class C_LongNotUpSubSeq {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_LongDivComSubSeq.class.getResourceAsStream("dataC.txt");
        C_LongNotUpSubSeq instance = new C_LongNotUpSubSeq();
        int result = instance.getNotUpSeqSize(stream);
        System.out.print(result);
    }

    int getNotUpSeqSize(InputStream stream) throws FileNotFoundException {
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
        int[] z = new int[n];

        for (int i = 1; i < n; i++) {

            int l = 0, r = result, mid, cmp;
            while (l < r) {
                mid = (l+r)/2;
                cmp = m[p[mid]] - m[i];

                if (cmp >= 0) { l = mid+1; }
                else { r = mid-1; }
            }

            cmp = m[p[l]] - m[i];

            if (cmp >= 0) { l++; }

            p[l] = i;
            if (l > result) {
                result = l;
                while (0 <= l && z[l] != p[l]) { z[l] = p[l]; l--; }
            }
        }
        result++;

        System.out.println(result);
        for (int i = 0; i < result; i++) {
            System.out.printf("%d ", z[i]+1);
        }
        System.out.println();

        //!!!!!!!!!!!!!!!!!!!!!!!!!     ����� ������     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

}