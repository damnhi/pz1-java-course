import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class Section {

    private String title;
    private final List<Paragraph> paragraphs = new ArrayList<>() ;

    public Section(String title) {
        this.title = title;
    }

    public Section setTitle(String title){
        this.title = title;
        return this;
    }

    public Section addParagraph(String paragraphText){
        paragraphs.add(new Paragraph(paragraphText));

        return this;
    }

    public Section addParagraph(Paragraph p) {
        paragraphs.add(p);
        return this;
    }

    public void writeHTML(PrintStream out){
        out.println("<section>");
        out.println(title);
        for(Paragraph p : paragraphs){
            p.writeHTML(out);
        }

        out.println("</section>");
    }

    public String getTitle() {
        return title;
    }

    public List<Paragraph> getParagraphs() {
        return paragraphs;
    }

}
