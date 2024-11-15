package by.it.group310902.strizhevskiy.lesson08;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
������ �� ����������������: ������ ��� ��������

������ ������ ����� �������� ����� �����
    1<=W<=100000     ����������� �������
    1<=n<=300        ����� ������� �������
                    (������ ����� ������������ ������ ���� ���).
��������� ������ �������� n ����� �����, �������� ���� ������� �� �������:
  0<=w[1]<=100000 ,..., 0<=w[n]<=100000

������� �������� ������������� ����������������
������������ ��� ������, ������� ����� ������ � �������.

Sample Input:
10 3
1 4 8
Sample Output:
9

*/

public class B_Knapsack {

    int getMaxWeight(InputStream stream ) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!     ������ ������     !!!!!!!!!!!!!!!!!!!!!!!!!
        Scanner scanner = new Scanner(stream);
        int w=scanner.nextInt();
        int n=scanner.nextInt();
        int gold[]=new int[n];
        for (int i = 0; i < n; i++) {
            gold[i]=scanner.nextInt();
        }

        java.util.Arrays.sort(gold);

        int result = w-getFreeSpace(gold, w, 0);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     ����� ������     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    public int getFreeSpace(int[] gold, int freeSpace, int i){
        if (i >= gold.length) { return freeSpace; }

        int count = 1;
        int left = freeSpace - gold[i];

        if (left < 0) { return freeSpace; }

        int currSpace = left;

        while (left > 0 && count >= 0) {
            left = Math.min(left, getFreeSpace(gold, currSpace, i+1));
            currSpace += gold[i];
            count--;
        }
        
        return left;
    }

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_Knapsack.class.getResourceAsStream("dataB.txt");
        B_Knapsack instance = new B_Knapsack();
        int res=instance.getMaxWeight(stream);
        System.out.println(res);
    }

}
