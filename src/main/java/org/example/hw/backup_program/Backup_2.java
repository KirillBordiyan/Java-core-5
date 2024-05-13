package org.example.hw.backup_program;

import java.io.IOException;
import java.nio.file.*;

public class Backup_2 {

    public static void main(String[] args) throws IOException {
        Path from = Paths.get("src");
        Path to = Paths.get("backup_hard");

        backup(from, to);
        System.out.println("Скопировано!");

    }

    /**
     * Метод копирования содержимого одной папки в другую (резервное копирование файлов)
     * с использованием потока объектов типа Path из корневой папки, его преобразованием и копированием
     * как и в первом варианте - через рекурсию
     * @param fromDir директория, откуда копировать
     * @param toDir директория, куда копировать
     * @throws IOException в момент, если файл существует или проблема с записью
     */
    public static void backup(Path fromDir, Path toDir) throws IOException {
        //если это директория и ее нет в целевой папке, то создадим ее с таким же названием
        //и try-w-r создадим поток ссылок на "файлы", которые скопируем в целевую папку
        if(Files.isDirectory(fromDir)) {
            if (!Files.exists(toDir)) {
                Files.createDirectories(toDir);
            }

            //создается поток из ссылок на файлы в контексте папки из которой мы что-то достаем
            //далее for мы перебираем каждый этот элемент и
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(fromDir)) {
                for (Path inner : stream) {
                    System.out.printf("Копируем папку %s\n", inner.getFileName());
                    //если передадим "откуда"=src(inner -> main, test), а "куда"=backup
                    //то таргет будет= backup/main, как бы создание нового пути,
                    //только так, чтобы не терялась последовательность (названия)
                    Path target = toDir.resolve(inner.getFileName());
                    backup(inner, target);
                }
            }
        }
        else {
            //если записать вот так:
            //Files.copy(fromDir, to);
            // и такие файлы будут, то выбросит ошибку FileAlreadyExistsException

            //можно сделать через try-catch, где это обработается
            //я выбрал обработку в виде вывода сообщения о существовании такого объекта
            //(можно через перезапись)
            //но если менять исходную папку (from),
            // то лучше полностью чистить целевую/запись в абсолютно другое место
            try{
                Files.copy(fromDir, toDir);
            }catch (IOException e){
                System.out.printf("Файл %s уже создан\n", fromDir.getFileName());
            }
        }
    }
}
