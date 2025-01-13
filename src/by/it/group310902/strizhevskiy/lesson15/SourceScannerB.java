package by.it.group310902.strizhevskiy.lesson15;

import java.io.File;
import java.util.Scanner;
import java.util.ArrayList;
import java.nio.charset.Charset;

/*
Создайте класс SourceScannerB с методом main,
который читает все файлы *.java из каталога src и его подкаталогов.

Каталог можно получить так:
        String src = System.getProperty("user.dir")
                       + File.separator + "src" + File.separator;

Файлы, содержащие в тексте @Test или org.junit.Test (тесты)
не участвуют в обработке.

В каждом тексте файла необходимо:
1. Удалить строку package и все импорты за O(n) от длины текста.
2. Удалить все комментарии за O(n) от длины текста.
3. Удалить все символы с кодом <33 в начале и конце текстов.
4. Удалить пустые строки

В полученном наборе текстов:
1. Рассчитать размер в байтах для полученных текстов
   и вывести в консоль
   размер и относительный от src путь к каждому из файлов (по одному в строке)
2. При выводе сортировать файлы по размеру,
   а если размер одинаковый,
   то лексикографически сортировать пути

Найдите способ корректно обрабатывать ошибки MalformedInputException

Все операции не должны ничего менять на дисках (разрешено только чтение)
Работа не имеет цели найти плагиат, поэтому не нужно менять коды своих программ.
*/

public class SourceScannerB {

   private static final String PACKAGE = "(package([^;]*);)?";
   private static final String IMPORT = "(import([^;]*);)?";
   private static final String WHITESPACES = "\\s*";
   private static final String BLANK_LINES = "([\\s^\n]*\n)+";

   private static final String SINGLE_LINE_COMMENT = "//([^\n]*)([\n]?)";
   private static final String MULTILINE_COMMENT = "/[*]([^*]|([*][^/]))*[*]/";
   private static final String COMMENT = "("+SINGLE_LINE_COMMENT+")|("+MULTILINE_COMMENT+")";

   private static final String WHITESPACES_AND_COMMENTS = "(("+WHITESPACES+")|("+COMMENT+"))*";
   private static final String BLANK_LINES_AND_COMMENTS = "(("+BLANK_LINES+")|(\\s*("+COMMENT+")))+";

   private static String srcPath = System.getProperty("user.dir") + File.separator + "src" + File.separator;
   private static File src = new File(srcPath);
   private static int srcNameSize = srcPath.length();

   private static ArrayList<FileNote> fileNotes = new ArrayList<FileNote>();

   private static class FileNote { int size; String path; }

   public static void main(String[] args) {
      processDir(src);
      fileNotes.sort((a,b) -> {
         return a.size != b.size ? a.size-b.size : a.path.compareTo(b.path);
      });
      for (FileNote fn : fileNotes) {
         System.out.printf("%s %s%n", fn.size, fn.path);
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

         fin.skip(WHITESPACES_AND_COMMENTS)
            .skip(PACKAGE);

         String match;
         do {
            match = fin.skip(WHITESPACES_AND_COMMENTS).findWithinHorizon(IMPORT,0);
            if (match.contains("org.junit.Test")) { return true; }
         } while (match.length() != 0);
         
         String fileUsefulData = fin.useDelimiter("@Test").next();

         if (fin.hasNext()) { return true; }

         fileUsefulData = fileUsefulData.replaceAll(BLANK_LINES_AND_COMMENTS, "\n").trim();

         FileNote fn = new FileNote();
         fn.size = fileUsefulData.getBytes().length;
         fn.path = file.getAbsolutePath().substring(srcNameSize);

         fileNotes.add(fn);
         
         return true;
      } catch (Exception e) {
         return false;
      }
   }

}
