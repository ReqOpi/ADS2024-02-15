package by.it.group310902.strizhevskiy.lesson15;

import java.io.File;
import java.util.Scanner;
import java.util.ArrayList;
import java.nio.charset.Charset;

/*
Создайте класс SourceScannerC с методом main,
который читает все файлы *.java из каталога src и его подкаталогов.

Каталог можно получить так:
        String src = System.getProperty("user.dir")
                       + File.separator + "src" + File.separator;

Файлы, содержащие в тексте @Test или org.junit.Test (тесты)
не участвуют в обработке.

В каждом тексте файла необходимо:
1. Удалить строку package и все импорты.
2. Удалить все комментарии за O(n) от длины текста.
3. Заменить все последовательности символов с кодом <33 на 32 (один пробел), т.е привести текст к строке.
4. Выполнить trim() для полученной строки.

В полученном наборе текстов:
1. Найти наиболее похожие тексты по метрике "расстояние Левенштейна",
   и определить копия ли это, считая копиями тексты с числом правок <10.
2. Если текст имеет копию(и), то вывести путь файла этого текста
   и в следующих строках путь(и) к копии(ям).
3. Повторить для всех файлов с копиями,
   при выводе сортировать файлы лексикографически по их пути.

Найдите способ корректно обрабатывать ошибки MalformedInputException

Оптимизируйте производительность решения (это может интересно).

Все операции не должны ничего менять на дисках (разрешено только чтение).
Работа не имеет цели найти плагиат, поэтому не нужно менять коды своих программ.
*/

public class SourceScannerC {

   private static final String PACKAGE = "(package([^;]*);)?";
   private static final String IMPORT = "(import([^;]*);)?";
   private static final String WHITESPACE = "\\s";
   private static final String WHITESPACES = "\\s*";

   private static final String SINGLE_LINE_COMMENT = "//([^\n]*)([\n]?)";
   private static final String MULTILINE_COMMENT = "/[*]([^*]*[*](?=[^/]))*[*]/";
   private static final String COMMENT = "("+SINGLE_LINE_COMMENT+")|("+MULTILINE_COMMENT+")";

   private static final String WHITESPACES_AND_COMMENTS_S = "(("+WHITESPACES+")|("+COMMENT+"))*";
   private static final String WHITESPACES_AND_COMMENTS_P = "(("+WHITESPACE+"+)|("+COMMENT+"))+";

   private static String srcPath = System.getProperty("user.dir") + File.separator + "src" + File.separator;
   private static File src = new File(srcPath);
   private static int srcNameSize = srcPath.length();

   private static ArrayList<FileNote> fileNotes;

   private static class FileNote { int size; String path; String data; }

   private static int lowEditDist = 10;

   public static void main(String[] args) {
      fileNotes = new ArrayList<FileNote>();
      processDir(src);
      fileNotes.sort((a,b) -> {
         return a.size != b.size ? a.size-b.size : a.path.compareTo(b.path);
      });
      for (int i = 0; i < fileNotes.size(); i++) {
         System.out.println(i+"/"+fileNotes.size());
         for (int j = i+1; j < fileNotes.size(); j++) {
            FileNote fn = fileNotes.get(i);
            FileNote tn = fileNotes.get(j);
            if (isDistanceEdintingLow(fn.data, tn.data)) {
               System.out.println("------------------------------");
               System.out.println(fn.path);
               System.out.println(tn.path);
               System.out.println("------------------------------");
            }
         }
      }
   }

   static void processDir(File dir) {
      for (File file : dir.listFiles()) {
         if (file.isDirectory()) { processDir(file); }
         else if (file.getName().endsWith(".java")) { processFile(file); }
      }
   }

   static void processFile(File file) {
      if (processFile(file, Charset.defaultCharset().name())) { return; }
      System.out.println("reopen "+file);
      if (processFile(file,"ISO-8859-1")) { return; }
   }

   static boolean processFile(File file, String charsetName) {
      try {
         Scanner fin = new Scanner(file, charsetName);

         fin.skip(WHITESPACES_AND_COMMENTS_S)
            .skip(PACKAGE);

         String match;
         do {
            match = fin.skip(WHITESPACES_AND_COMMENTS_S).findWithinHorizon(IMPORT,0);
            if (match.contains("org.junit.Test")) { return true; }
         } while (match.length() != 0);
         
         String fileUsefulData = fin.useDelimiter("@Test").next();

         if (fin.hasNext()) { return true; }

         fileUsefulData = fileUsefulData.replaceAll(WHITESPACES_AND_COMMENTS_P, " ").trim();

         FileNote fn = new FileNote();
         fn.size = fileUsefulData.getBytes().length;
         fn.path = file.getAbsolutePath().substring(srcNameSize);
         fn.data = fileUsefulData;

         fileNotes.add(fn);
         
         return true;
      } catch (Exception e) {
         return false;
      }
   }

   static boolean isDistanceEdintingLow(String one, String two) {
         if (Math.abs(one.length()-two.length()) >= lowEditDist) { return false; }

        int[][] buf = new int[1+two.length()][1+one.length()];

        for (int j = 1; j < one.length(); j++) {
            buf[0][j] = j;
        }

        for (int i = 1; i < two.length(); i++) {
            buf[i][0] = i;
        }

        for (int i = 1; i <= two.length(); i++) {
            int lvl = Integer.MAX_VALUE;
            for (int j = 1; j <= one.length(); j++) {
                int u = buf[i-1][j]+1;
                int l = buf[i][j-1]+1;
                int d = buf[i-1][j-1];
                if (two.charAt(i-1) != one.charAt(j-1)) { d++; }
                buf[i][j] = Math.min(Math.min(u,l),d);
                lvl = Math.min(lvl, buf[i][j]);
            }
            if (lvl >= lowEditDist) { return false; }
        }

        return true;
    }

}
