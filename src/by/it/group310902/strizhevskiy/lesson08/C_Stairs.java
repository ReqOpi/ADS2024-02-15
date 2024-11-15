package by.it.group310902.strizhevskiy.lesson08;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
���� ����� 1<=n<=100 �������� �������� �
����� ����� -10000<=a[1],�,a[n]<=10000, �������� �������� ���������.
������� ������������ �����, ������� ����� ��������, ��� �� ��������
����� ����� (�� ������� �� n-� ���������), ������ ��� ���������� ��
���� ��� �� ��� ���������.

Sample Input 1:
2
1 2
Sample Output 1:
3

Sample Input 2:
2
2 -1
Sample Output 2:
1

Sample Input 3:
3
-1 2 1
Sample Output 3:
3

*/

public class C_Stairs {

    int getMaxSum(InputStream stream ) {
        Scanner scanner = new Scanner(stream);
        int n=scanner.nextInt();
        int stairs[]=new int[n];
        for (int i = 0; i < n; i++) {
            stairs[i]=scanner.nextInt();
        }
        //!!!!!!!!!!!!!!!!!!!!!!!!!     ������ ������     !!!!!!!!!!!!!!!!!!!!!!!!!
        int result = 0;

        int maxa = 0;
        int maxb = 0;

        for (int i = 0; i < n-1; i+=2) {
            maxa = stairs[i] + Math.max(maxa, maxb);
            maxb = stairs[i+1] + Math.max(maxa, maxb);
        }

        if (n % 2 == 1) { 
            maxa = stairs[n-1] + Math.max(maxa, maxb);
        }

        result = Math.max(maxa,maxb);

        //!!!!!!!!!!!!!!!!!!!!!!!!!     ����� ������     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }


    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_Stairs.class.getResourceAsStream("dataC.txt");
        C_Stairs instance = new C_Stairs();
        int res=instance.getMaxSum(stream);
        System.out.println(res);
    }

}
