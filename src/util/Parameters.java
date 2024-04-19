package util;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;

public class Parameters {
    public static final File FILE = new File("src/resource", "backup.txt");
    public static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    public static final Charset ENCODING = StandardCharsets.UTF_8;

}
