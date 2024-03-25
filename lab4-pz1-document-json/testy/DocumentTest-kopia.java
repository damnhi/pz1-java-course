import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class DocumentTest {

    @Test
    public void testOfGson() {
        Document cv = new Document("Jana Kowalski - CV");
        cv.setPhoto("https://upload.wikimedia.org/wikipedia/commons/thumb/7/71/Calico_tabby_cat_-_Savannah.jpg/1200px-Calico_tabby_cat_-_Savannah.jpg");
        cv.addSection("Wykształcenie")
                .addParagraph("2000-2005 Przedszkole im. Królewny Snieżki w ...")
                .addParagraph("2006-2012 SP7 im Ronalda Regana w ...")
                .addParagraph(
                        new ParagraphWithList().setContent("Kursy")
                                .addListItem("Języka Angielskiego")
                                .addListItem("Języka Hiszpańskiego")
                                .addListItem("Szydełkowania")
                );
        cv.addSection("Umiejętności")
                .addParagraph(
                        new ParagraphWithList().setContent("Znane technologie")
                                .addListItem("C")
                                .addListItem("C++")
                                .addListItem("Java")
                );

        List<String> contentOfParagraphList = new ArrayList<>(Arrays.asList("Języka Angielskiego",
                "Języka Hiszpańskiego",
                "Szydełkowania",
                "C",
                "C++",
                "Java"));

        String jsonFromDocument = cv.toJson();
        String documentFromJson = Document.fromJson(cv.toJson()).toJson();

        assertEquals(jsonFromDocument, documentFromJson);
        for (String content : contentOfParagraphList){
            assertTrue(documentFromJson.contains(content));
        }

    }


}