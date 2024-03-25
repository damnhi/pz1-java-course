import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class UnorderedListTest {

    @Test
    public void addItem() {
        UnorderedList unorderedList = new UnorderedList();
        List<ListItem> listOfItems = new ArrayList<>();

        for (int i = 1; i < 4; ++i){
            String contentOfItem = "Item" + Integer.toString(i);
            ListItem listItem = new ListItem(contentOfItem);

            listOfItems.add(listItem);
            unorderedList.addItem(listItem);
        }

        assertEquals(listOfItems, unorderedList.getListOfContent());

    }

    @Test
    public void writeHTML() {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(os);

        UnorderedList unorderedList = new UnorderedList();
        String content = "Point";
        unorderedList.addItem(new ListItem(content));

        unorderedList.writeHTML(ps);
        String result = null;

        try {
            result = os.toString("ISO-8859-2");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        System.out.println(result);

        assertTrue(result.contains("<ul>"));
        assertTrue(result.contains("<li>"));
        assertTrue(result.contains(content));
        assertTrue(result.contains("</li>"));
        assertTrue(result.contains("</ul>"));
    }
}