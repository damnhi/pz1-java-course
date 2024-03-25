import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import static org.junit.Assert.*;

public class SectionTest {

    @Test
    public void setTitle() {
        Section section = new Section("Title");
        String newTitle = "New Title";
        section.setTitle(newTitle);

        assertEquals(newTitle, section.getTitle());
    }

    @Test
    public void addParagraph() {
        Section section = new Section("Test");
        Paragraph paragraph = new Paragraph();

        section.addParagraph(paragraph);

        assertTrue(section.getParagraphs().contains(paragraph));
    }

    @Test
    public void addStringParagraph() {
        Section section = new Section("Test");
        String title = "Title";
        section.addParagraph(title);

        boolean isInSection = false;
        for(Paragraph paragraph : section.getParagraphs()){
            if (paragraph.content.equals(title)) {
                isInSection = true;
                break;
            }
        }

        assertTrue(isInSection);
    }

    @Test
    public void writeHTML() {
        String title = "test section";

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(os);

        new Section(title).writeHTML(ps);

        String result = null;

        try {
            result = os.toString("ISO-8859-2");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        assertTrue(result.contains("<section>"));
        assertTrue(result.contains(title));
        assertTrue(result.contains("</section>"));
    }
}