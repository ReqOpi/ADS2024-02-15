package by.it.group310902.strizhevskiy.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
������ �� ����������������: ���������� �����������
    https://ru.wikipedia.org/wiki/����������_�����������
    http://planetcalc.ru/1721/

����:
    ��� ������ �������� ������ ����� �� ����� 100, ���������� �������� ����� ���������� ��������.

����������:
    ������ ������ �������� ������������� ����������������
    ����������� ��������� ���������� �������������� ���� ������ �������� �����

    Sample Input 1:
    ab
    ab
    Sample Output 1:
    0

    Sample Input 2:
    short
    ports
    Sample Output 2:
    3

    Sample Input 3:
    distance
    editing
    Sample Output 3:
    5

*/

public class B_EditDist {


    int getDistanceEdinting(String one, String two) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!     ������ ������     !!!!!!!!!!!!!!!!!!!!!!!!!
        int[][] buf = new int[1+two.length()][1+one.length()];

        for (int j = 1; j < one.length(); j++) {
            buf[0][j] = j;
        }

        for (int i = 1; i < two.length(); i++) {
            buf[i][0] = i;
        }

        for (int i = 1; i <= two.length(); i++) {
            for (int j = 1; j <= one.length(); j++) {
                int u = buf[i-1][j]+1;
                int l = buf[i][j-1]+1;
                int d = buf[i-1][j-1];
                if (two.charAt(i-1) != one.charAt(j-1)) { d++; }
                buf[i][j] = Math.min(Math.min(u,l),d);
            }
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     ����� ������     !!!!!!!!!!!!!!!!!!!!!!!!!
        return buf[two.length()][one.length()];
    }


    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_EditDist.class.getResourceAsStream("dataABC.txt");
        B_EditDist instance = new B_EditDist();
        Scanner scanner = new Scanner(stream);
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
    }

}