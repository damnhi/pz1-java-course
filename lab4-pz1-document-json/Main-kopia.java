import java.io.IOException;
import java.io.PrintStream;

public class Main {
    public static <FileNotFoundException> void main(String[] args) throws IOException {
        Document cv = new Document("John Doe - Resume");

        cv.setPhoto("https://upload.wikimedia.org/wikipedia/commons/thumb/7/71/Calico_tabby_cat_-_Savannah.jpg/1200px-Calico_tabby_cat_-_Savannah.jpg");

        cv.addSection("Education")
                .addParagraph("2010-2014 Bachelor's Degree in Computer Science, University XYZ")
                .addParagraph("2015-2017 Master's Degree in Software Engineering, University ABC");

        cv.addSection("Skills")
                .addParagraph(
                        new ParagraphWithList().setContent("Programming Languages")
                                .addListItem("Java")
                                .addListItem("Python")
                                .addListItem("JavaScript")
                )
                .addParagraph(
                        new ParagraphWithList().setContent("Tools and Frameworks")
                                .addListItem("Spring Framework")
                                .addListItem("React")
                                .addListItem("Django")
                );


//        cv.writeHTML(System.out);
        cv.writeHTML(new PrintStream("cv.html", "ISO-8859-2"));

        String json = cv.toJson();

        try (PrintStream writer = new PrintStream("cv.json","ISO-8859-2")) {
            writer.print(json);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
