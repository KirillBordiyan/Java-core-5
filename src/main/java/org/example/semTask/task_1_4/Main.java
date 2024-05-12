package org.example.semTask.task_1_4;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Main {
    private static final Random rnd = new Random();
    public static final int CHAR_NUM_L = 65;
    public static final int CHAR_NUM_H = 90;
    public static final String TO_FIND = "GeekBrains";


    public static void main(String[] args) throws IOException {

        //создание и склейка файлов
        writeFContent("src/main/java/org/example/semTask/task_1_4/sample1.txt", 60, 3);
        writeFContent("src/main/java/org/example/semTask/task_1_4/sample2.txt", 90, 4);

        concatFile("src/main/java/org/example/semTask/task_1_4/sample1.txt",
                "src/main/java/org/example/semTask/task_1_4/sample2.txt",
                "src/main/java/org/example/semTask/task_1_4/outputFile.txt");


        System.out.println();
        System.out.println("поиск методом с семинара:");
        //поиск по семинару
        long i = 0;
        while((i = searchInF("src/main/java/org/example/semTask/task_1_4/exist.txt", i, TO_FIND)) > 0){
            System.out.printf("файл имеет слово, смещение %d \n", i);
        }

        System.out.println();
        System.out.println("поиск методом паттерна:");
        //поиск паттерном
        wordExist("src/main/java/org/example/semTask/task_1_4/exist.txt", TO_FIND);


        System.out.println();
        System.out.println("поиск соответствий с семинара:");
        //цикл создания файлов
        for(int j = 1; j <= 10; j++){
            String filename = "src/main/java/org/example/semTask/task_1_4/files/file_"+j+".txt";
            writeFContent(filename, 50, 5);
            System.out.printf("Файл %s создан\n", filename);
        }
        //поиск и вывод списка файлов
        List<String> res = searchMatch(new File("."), TO_FIND);
        for(String str : res){
            System.out.printf("Файл %s содержит искомое слово %s", str, TO_FIND);
        }
    }

    /**
     * поиск искомого в нескольких файлах директории
     * @param file
     * @param search
     * @return
     * @throws IOException
     */
    private static List<String> searchMatch(File file, String search) throws IOException{
        List<String> list = new ArrayList<>();
        File[] files = file.listFiles();
        if(files == null){
            return list;
        }
        for(int i = 0; i< files.length; i++){
            if(files[i].isFile()){
                if(searchInF(files[i].getCanonicalPath(), search) > -1){
                    list.add(files[i].getCanonicalPath());
                }
            }
        }
        return list;
    }

    /**
     * генерация символов
     * @param amount
     * @return
     */
    private static String generateString(int amount) {
        StringBuilder string = new StringBuilder();
        for (int i = 0; i < amount; i++) {
            string.append((char) rnd.nextInt(CHAR_NUM_L, CHAR_NUM_H + 1));
        }
        return string.toString();
    }

    /**
     * запись в файл
     * @param fileName
     * @param len
     * @param word
     * @throws IOException
     */
    private static void writeFContent(String fileName, int len, int word) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(fileName)) {
            for(int i = 0; i < word; i++){
                if(rnd.nextInt(5) == 0){
                    fos.write(TO_FIND.getBytes(StandardCharsets.UTF_8));
                }
                else{
                    fos.write(generateString(len).getBytes(StandardCharsets.UTF_8));
                }
            }
        }
    }

    /**
     * слияние файлов
     * @param fileInput1
     * @param fileInput2
     * @param fileOutput
     * @throws IOException
     */
    private static void concatFile(String fileInput1, String fileInput2, String fileOutput) throws IOException {
        try (FileOutputStream fileOutputStream = new FileOutputStream(fileOutput)) {
            int bt;
            try (FileInputStream fis = new FileInputStream(fileInput1)) {
                while ((bt = fis.read()) != -1) {
                    fileOutputStream.write(bt);
                }
            }
            fileOutputStream.write('\n');
            try (FileInputStream fis = new FileInputStream(fileInput2)) {
                while ((bt = fis.read()) != -1) {
                    fileOutputStream.write(bt);
                }
            }

        }
    }

    /**
     * поиск в файле
     * @param fileName - файл
     * @param offset   - смещение
     * @param find     - что искать
     * @return
     */
    public static long searchInF(String fileName, long offset, String find) throws IOException {
        try (FileInputStream fis = new FileInputStream(fileName)) {
            fis.skipNBytes(offset);
            byte[] searchByte = find.getBytes(StandardCharsets.UTF_8);
            int c;
            int i = 0;
            while ((c = fis.read()) != -1) {
                if (c == searchByte[i]) i++;
                else {
                    i = 0;
                    if (c == searchByte[i]) i++;
                }
                if (i == searchByte.length) return offset;
                offset++;
            }
            return -1;
        }
    }

    public static long searchInF(String fileName, String find) throws IOException {
        return searchInF(fileName, 0, find);
    }

    /**
     * метод поиска через regex
     * @param fileName
     * @param find
     * @throws IOException
     */
    private static void wordExist(String fileName, String find) throws IOException {

        try (FileInputStream fis = new FileInputStream(fileName)) {
            int count = 0;
            StringBuilder str = new StringBuilder();
            Pattern p = Pattern.compile(find);

            byte[] d = fis.readAllBytes();
            for (byte v : d) {
                str.append(Character.toString(v));
            }
            Matcher m = p.matcher(str);
            while (m.find()) {
                count++;
                System.out.println("искомое входит на: " + m.start());
            }
            System.out.printf("всего вхождений: %d\n", count);
        }
    }
}
