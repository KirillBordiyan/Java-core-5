package org.example.hw.task_tree_hw;

import java.io.File;

public class Tree {
    public static void main(String[] args) {
        print(new File("."), "", true);
    }

    public static void print(File file, String indent, boolean isLast) {
        //начальные отступы и значки
        System.out.print(indent);
        if (isLast) {
            System.out.print("└─");
            indent += "  ";
        } else {
            System.out.print("├─");
            indent += "│  ";
        }
        System.out.println(file.getName());

        //если file - файл, где нельзя получить список файлов
        //то прерываем и идем дальше
        File[] files = file.listFiles();
        if (files == null) return;

        //проход для подсчета кол-ва элементов внутри директории с учетом скрытых
        int subDir = 0;
        for (File item : files) {
            if (item.isDirectory() || item.isFile()) {
                subDir++;
            }
        }

        //проход внутри найденной директории, счетчик нужен, чтобы понимать, последняя (замыкающая) она или нет
        //для понимания символа вначале строки
        //если найденные == ++текущая, то она последняя
        int subDirCount = 0;
        for (File value : files) {
            print(value, indent, subDir == ++subDirCount);
        }
    }
}
