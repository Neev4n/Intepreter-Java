import lox.*;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static lox.Lox.*;

public class Main {
  public static void main(String[] args) throws IOException {
    if (args.length < 2) {
      System.err.println("Usage: jlox <command> <script>");
      System.exit(64);
    }

    String command = args[0];
    String path = args[1];

    byte[] bytes = Files.readAllBytes(Paths.get(path));
    String source = new String(bytes, Charset.defaultCharset());

    switch (command) {
      case "tokenize" -> runTokenize(source);
      case "parse" -> runParse(source);
      case "evaluate" -> runEvaluate(source);
      default -> {
        System.out.println("Unknown command: " + command);
        System.exit(1);
      }
    }
  }

}
