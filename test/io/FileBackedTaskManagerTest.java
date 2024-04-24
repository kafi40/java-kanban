package io;

import org.junit.jupiter.api.Test;
import java.io.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FileBackedTaskManagerTest {

    @Test
    public void testCreateFileException() {
        assertThrows(IOException.class, () -> {
            File tempFile = File.createTempFile("backup", "", new File("sr/resource"));
            tempFile.deleteOnExit();
        }, "Не корректное создание файла должно привести к исключению");
    }

    @Test
    public void testCreateFileWriter() {
        assertThrows(IOException.class, () -> {
            FileWriter fileWriter = new FileWriter("");
            fileWriter.write("Какой-то текст");
        }, "Не корректно созданный экземпляр FileWriter должен привести к исключению");
    }

    @Test
    public void testCreateBufferReader() {
        assertThrows(IOException.class, () -> {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(""));
            bufferedReader.readLine();
        }, "Не корректно созданный экземпляр BufferedReader должен привести к исключению");
    }
}
