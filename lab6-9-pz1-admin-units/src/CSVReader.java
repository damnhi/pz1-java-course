import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class CSVReader {
    BufferedReader reader;
    String delimiter;
    boolean hasHeader;
    String[] current;

    // nazwy kolumn w takiej kolejności, jak w pliku
    List<String> columnLabels = new ArrayList<>();
    // odwzorowanie: nazwa kolumny -> numer kolumny
    Map<String, Integer> columnLabelsToInt = new HashMap<>();


    public CSVReader(Reader reader, String delimiter, boolean hasHeader) throws IOException {
        this.reader = new BufferedReader(reader);
        this.delimiter = delimiter;
        this.hasHeader = hasHeader;
        if (hasHeader) parseHeader();
    }

    /**
     * @param filename  - nazwa pliku
     * @param delimiter - separator pól
     * @param hasHeader - czy plik ma wiersz nagłówkowy
     */

    public CSVReader(String filename, String delimiter, boolean hasHeader) throws IOException {
        this(new BufferedReader(new FileReader(filename)), delimiter, hasHeader);
    }

    public CSVReader(String filename, String delimiter) throws IOException {
        this(filename, delimiter, false);
    }

    public CSVReader(String filename) throws IOException {
        this(filename, ",", false);
    }

    void parseHeader() throws IOException {
        String line = reader.readLine();
        if (line == null) {
            return;
        }

        String[] header = line.split(delimiter + "(?=([^\"]*\"[^\"]*\")*[^\"]*$)");

        for (int i = 0; i < header.length; i++) {
            columnLabels.add(header[i]);
            columnLabelsToInt.put(header[i], i);
        }
    }

    boolean next() throws IOException {
        String line = reader.readLine();
        if (line == null) {
            return false;
        }

        current = line.split(delimiter  + "(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
        return true;
    }

    public String[] getCurrent() {
        return current;
    }

    public List<String> getColumnLabels() {
        return columnLabels;
    }

    int getRecordLength() {
        return current.length;
    }

    boolean isMissing(int columnIndex) {
        return columnIndex < 0 || columnIndex >= current.length || current[columnIndex].isEmpty();
    }

    boolean isMissing(String columnLabel) {
        if (!columnLabelsToInt.containsKey(columnLabel)) {
            throw new IllegalArgumentException("Invalid columnLabel");
        }
        int columnIndex = columnLabelsToInt.get(columnLabel);

        return this.isMissing(columnIndex);
    }

    String get(int columnIndex) {
        if (isMissing(columnIndex)) return "";

        return current[columnIndex];
    }

    String get(String columnLabel) {
        if (!columnLabelsToInt.containsKey(columnLabel)) {
            throw new IllegalArgumentException("Invalid columnLabel");
        }
        int columnIndex = columnLabelsToInt.get(columnLabel);
        return get(columnIndex);
    }

    int getInt(int columnIndex) {
        String value = get(columnIndex);
        if (value.isEmpty()) {
            throw new IllegalArgumentException("Value is missing or empty");
        }
        return Integer.parseInt(value);
    }

    int getInt(String columnLabel) {
        return getInt(columnLabelsToInt.get(columnLabel));
    }

    long getLong(int columnIndex) {
        String value = get(columnIndex);
        if (value.isEmpty()) {
            throw new IllegalArgumentException("Value is missing or empty");
        }
        return Long.parseLong(value);
    }

    long getLong(String columnLabel) {
        int columnIndex = columnLabelsToInt.get(columnLabel);
        return getLong(columnIndex);
    }

    double getDouble(int columnIndex) {
        String value = get(columnIndex);
        if (value.isEmpty()) {
            throw new IllegalArgumentException("Value is missing or empty");
        }
        return Double.parseDouble(value);
    }

    double getDouble(String columnLabel) {
        int columnIndex = columnLabelsToInt.get(columnLabel);
        return getDouble(columnIndex);
    }

    LocalTime getTime(int columnIndes, String format){
        String timeString = get(columnIndes);
        return LocalTime.parse(timeString, DateTimeFormatter.ofPattern(format));
    }
    LocalDate getDate(int columnIndex, String format) {
        String dateString = get(columnIndex);
        return LocalDate.parse(dateString, DateTimeFormatter.ofPattern(format));
    }

    LocalDateTime getDateTime(int columnIndex, String timeFormat, String dateFormat) {
        String dateTimeString = get(columnIndex);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(timeFormat + " " + dateFormat);
        return LocalDateTime.parse(dateTimeString, dateTimeFormatter);
    }

}