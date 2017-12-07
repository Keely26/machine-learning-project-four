package Data;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * File access handler for .csv datasets
 * Takes a dataset type and returns a List of examples in Dataset format
 */
public class DatasetBuilder {

    private static final String PATH = "datasets/";
    private static final String CSV = ".csv";
    private static final Pattern COMMA_DELIMITER = Pattern.compile(",");

    public static Dataset buildDataSet(DatasetType type) {
        File file = new File(PATH.concat(type.toString()).concat(CSV));
        List<String> lines = Objects.requireNonNull(getFileStream(file)).collect(Collectors.toList());

        return lines.stream()
                .map(line -> new Datum(Arrays.stream(COMMA_DELIMITER
                        .split(line))
                        .mapToDouble(Double::parseDouble)
                        .toArray()))
                .collect(Collectors.toCollection(Dataset::new));
    }

    private static Stream<String> getFileStream(File file) {
        try {
            return new BufferedReader(new FileReader(file)).lines();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}