package io;

import interfaces.TaskManager;
import memory.InMemoryTaskManager;
import task.Task;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {
    public static void main(String[] args) throws IOException {
        save();
    }

    public static void save() throws IOException {
        // Сохраняем в переменную путь к файлу
        Path path = Paths.get("src/resource", "backup.txt");
        // Если файл не существует, создаем его
        if (!Files.exists(path)) {
            Files.createFile(path);
        }
        FileWriter fileWriter = new FileWriter(path.toString());
        fileWriter.write("id,type,name,status,description,epic");
        fileWriter.close();
    }

    @Override
    public void addTask(int command) {
        super.addTask(command);
    }


    public Task fromString(String value) {

        return null;
    }

    @Override
    public String toString() {
        return "FileBackedTaskManager{}";
    }
}
