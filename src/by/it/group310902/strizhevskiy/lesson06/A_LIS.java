package by.it.group310902.strizhevskiy.lesson06;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
������ �� ����������������: ���������� ������������ ���������������������
��.     https://ru.wikipedia.org/wiki/������_������_����������_���������������_���������������������
        https://en.wikipedia.org/wiki/Longest_increasing_subsequence

����:
    ����� ����� 1<=n<=1000
    ������ A[1�n] ����������� �����, �� ������������� 2E9.

����������:
    �������� ������������ 1<=k<=n, ��� �������� �������������� �������
    ��������������������� �������� i[1]<i[2]<�<i[k] <= ����� k,
    ��� ������ ������� A[i[k]] ������ ������ �����������
    �.�. ��� ���� 1<=j<k, A[i[j]]<A[i[j+1]].

������ ������ �������� ������������� ����������������

    Sample Input:
    5
    1 3 3 2 6

    Sample Output:
    3
*/

public class A_LIS {


    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_LIS.class.getResourceAsStream("dataA.txt");
        A_LIS instance = new A_LIS();
        int result = instance.getSeqSize(stream);
        System.out.print(result);
    }

    int getSeqSize(InputStream stream) throws FileNotFoundException {
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

        int result = 0;

        int[] p = new int[n];

        for (int i = 0; i < n; i++) {

            int l = 0, r = result, mid, cmp;
            while (l < r) {
                mid = (l+r)/2;
                cmp = m[p[mid]] - m[i];

                if (cmp == 0) { break; }
                else if (cmp < 0) { l = mid+1; }
                else { r = mid-1; }
            }

            cmp = m[p[l]] - m[i];

            if (l < r || cmp == 0) { continue; }
            if (cmp < 0) { l++; }

            p[l] = i;
            result = Math.max(l, result);
        }
        result++;
        
        //!!!!!!!!!!!!!!!!!!!!!!!!!     ����� ������     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }
}
