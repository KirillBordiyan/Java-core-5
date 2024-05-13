package org.example.hw.backup_program;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Backup {
    public static void main(String[] args) throws IOException {
        String folderFrom = "src/main/java/org/example/semTask/task_1_4/files";
        String folderTo = "backup_easy";

        backup(folderFrom, folderTo);
        System.out.println("Скопировано!");
    }

    /**
     * Метод копирования содержимого одной папки в другую (резервное копирование файлов)
     * путем прочтения исходного и создания нового или, при необходимости, перезаписи существующего
     * @param folderFrom откуда копировать
     * @param folderTo в какую копировать
     * @throws IOException при ошибке в момент чтения/записи
     */
    public static void backup(String folderFrom, String folderTo) throws IOException{
        File from = new File(folderFrom);
        File to = new File(folderTo);

        if(from.isDirectory()) {
            if (!to.exists()) {
                to.mkdir();
            }

            File[] files = from.listFiles();
            if(files == null) return;

            for (File file : files) {
                backup(folderFrom+"\\"+file.getName(), folderTo+ "\\" + file.getName());
            }
        }
        else {
            //по сравнению со сложным вариантом -
            //тут всегда уже существующие файлы ПЕРЕЗАПИСЫВАЮТСЯ!! это важно
            try(FileInputStream fis = new FileInputStream(from);
                FileOutputStream fos = new FileOutputStream(to)){
                    fos.write(fis.readAllBytes());
            }catch (IOException e){
                System.out.println("Ошибка: \n"+e.getMessage());
            }
        }
    }
}
