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

   private static int lowEditDist = 9;

   private static class FileNote {
      FileNote parent; int count; boolean visited;
      int size; String path; String[] data;

      public FileNote() { parent = this; }

      public void setParent(FileNote p) {
         if (parent != p) {
            if (parent != this) { parent.setParent(p); }
            parent = p;
         }
      }

      public FileNote getParent() {
         if (parent == this) { return this; }
         FileNote p = parent.getParent();
         parent = p;
         return parent;
      }
   }

   public static void main(String[] args) {
      fileNotes = new ArrayList<FileNote>();
      processDir(src);
      fileNotes.sort((a,b) -> {
         return a.size != b.size ? a.size-b.size : a.path.compareTo(b.path);
      });

      FileNote[] link = new FileNote[2];

      for (int i = 0; i < fileNotes.size(); i++) {
         System.out.printf("%s/%s%n", i, fileNotes.size());
         for (int j = i+1; j < fileNotes.size(); j++) {
            link[0] = fileNotes.get(i);
            link[1] = fileNotes.get(j);

            if (link[1].size - link[0].size > lowEditDist) { break; }

            if (isDistanceEdintingLow(link[0].data, link[1].data)) {
               link[0] = link[0].getParent();
               link[1] = link[1].getParent();

               if (link[0] == link[1]) { continue; }

               if (link[0].count > link[1].count) {
                  link[0].count += link[1].count;
                  link[1].setParent(link[0]);
               } else {
                  link[1].count += link[0].count;
                  link[0].setParent(link[1]);
               }
            }
         }
      }
      
      fileNotes.sort((a,b) -> {
         return a.getParent().hashCode() - b.getParent().hashCode();
      });

      FileNote g = fileNotes.get(0);
      for (FileNote fn : fileNotes) {
         if (g != fn.getParent()) {
            System.out.println();
            g = fn.getParent();
         }
         System.out.println(fn.path);
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
         fn.path = file.getAbsolutePath().substring(srcNameSize);
         fn.data = fileUsefulData.split("[{};] ?");
         fn.size = fn.data.length;

         fileNotes.add(fn);
         
         return true;
      } catch (Exception e) {
         return false;
      }
   }

   static boolean isDistanceEdintingLow(String[] one, String[] two) {
         if (one.length > two.length) {
            String[] temp = one;
            one = two;
            two = temp;
         }

         if (two.length - one.length > lowEditDist) {
            return false;
         }

        int[] prev = new int[one.length+1];
        int[] curr = new int[one.length+1];
        int[] temp;


        for (int j = 0; j < one.length+1; j++) {
            prev[j] = j;
        }

        for (int i = 1; i <= two.length; i++) {
            curr[0] = i;
            for (int j = 1; j <= one.length; j++) {
                int u = prev[j]+1;
                int l = curr[j-1]+1;
                int d = prev[j-1];
                if (!two[i-1].equals(one[j-1])) { d++; }
                curr[j] = Math.min(Math.min(u,l),d);
            }
            temp = prev; prev = curr; curr = temp;
        }

        return prev[one.length] <= lowEditDist;
    }

}
