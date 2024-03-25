import java.io.PrintStream;

public class Paragraph {
    protected String content;

    public Paragraph() {
    }

    public Paragraph(String content){
        this.content = content;
    }

    public Paragraph setContent(String content){
        this.content = content;
        return this;
    }

    public void writeHTML(PrintStream out){
        out.println("<p>");
        out.println(content);
        out.println("</p>");
    };


}
