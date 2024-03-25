import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

import static org.junit.Assert.*;

public class CSVReaderTest {
    String filename = "csvfiles/titanic-part.csv";
    String delimiter = ",";

    @Test
    public void testReadFile() throws IOException {
        CSVReader reader = new CSVReader(filename, delimiter, true);

        while (reader.next()) {
            String[] record = reader.getCurrent();
            for (String line : record) {
                System.out.print(line + " ");
            }
            System.out.println();
        }
    }
    @Test
    public void testReadDifferentSource() throws IOException {
        String text = "a,b,c\n123.4,567.8,91011.12";
        CSVReader reader = new CSVReader(new StringReader(text), delimiter, true);

        while (reader.next()) {
            String[] record = reader.getCurrent();
            for (String field : record) {
                System.out.print(field + " ");
            }
            System.out.println();
        }
    }

    @Test
    public void testValueTypes() throws IOException {
        CSVReader reader = new CSVReader(filename, delimiter, true);

        assertTrue(reader.next());

        String stringValue = reader.get("Sex");
        assertNotNull(stringValue);
        assertEquals("male", stringValue);

        int intValue = reader.getInt("PassengerId");
        assertEquals(1, intValue);

        double doubleValue = reader.getDouble("Fare");
        assertEquals(7.25, doubleValue, 0.001);
    }

    @Test
    public void testMissingValues() throws IOException {
        CSVReader reader = new CSVReader(filename, delimiter, true);

        assertTrue(reader.next());

        String missingValue = reader.get(10);
        assertNotNull(missingValue);
        assertEquals("", missingValue);

    }

    @Test
    public void testInvalidColumn() throws IOException{
        CSVReader reader = new CSVReader(filename, delimiter, true);

        assertTrue(reader.next());

        assertThrows(IllegalArgumentException.class, () -> {
            reader.get("FakeColumn");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            reader.getInt(100);
        });
    }
}