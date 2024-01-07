package com.eronalves.spellchecker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Hello world!
 *
 */
public class App {
  public static void main (String[] args)
      throws IOException {
    ClassLoader classLoader = ClassLoader.getSystemClassLoader();
    Set<String> onMemDictionary = new HashSet<>();
    UnaryOperator<String> color =
        (text) -> String.format("\033[96m%s\033[0m", text);

    try (
        InputStream dict = classLoader.getResourceAsStream("dictionary.txt");
        InputStreamReader dictReader = new InputStreamReader(dict);
        BufferedReader dictBuf = new BufferedReader(dictReader);
        Stream<String> lines = dictBuf.lines();
    ) {
      lines.flatMap(l -> Arrays.stream(l.split(" ")))
          .forEach(onMemDictionary::add);
    }

    System.out.println(
        "Comece a digitar o texto abaixo. Para cada linha, iremos verificar o seu texto e indicar as palavras possivelmente incorretas"
    );

    try (Scanner scanner = new Scanner(System.in)) {
      while (true) {
        String line = scanner.nextLine();
        System.out.println(
            Arrays.stream(line.split(" "))
                .map(
                    word -> onMemDictionary.contains(word.toLowerCase())
                        ? word
                        : color.apply(word)
                )
                .collect(Collectors.joining(" "))
        );
      }
    }
  }
}
