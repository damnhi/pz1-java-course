import java.io.PrintStream;


public class ParagraphWithList extends Paragraph{
    private final UnorderedList points = new UnorderedList();

    public ParagraphWithList() {}

    public ParagraphWithList(String content) {
        super(content);
    }

    public ParagraphWithList setContent(String content){
        this.content = content;
        return this;
    }

    public ParagraphWithList addListItem(String content){
        ListItem listItem = new ListItem(content);
        points.addItem(listItem);
        return this;
    }

    @Override
    public void writeHTML(PrintStream out){
        out.println("<p>");
        out.println(content);
        points.writeHTML(out);
        out.println("</p>");
    }
}
