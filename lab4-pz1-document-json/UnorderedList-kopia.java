import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class UnorderedList {
    private final List<ListItem> listOfContent = new ArrayList<>();

    public UnorderedList addItem(ListItem item){
        listOfContent.add(item);
        return this;
    }

    public void writeHTML(PrintStream out){
        out.println("<ul>");

        for (ListItem listItem : listOfContent){
            listItem.writeHTML(out);
        }

        out.println("</ul>");
    }


    public List<ListItem> getListOfContent() {
        return listOfContent;
    }
}
