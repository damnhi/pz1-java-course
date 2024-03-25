import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class Document {
    private String title;
    private Photo photo;
    private final List<Section> sections = new ArrayList<>();

    public Document(String title){
        this.title = title;
    }

    public Document setTitle(String title){
        this.title = title;
        return this;
    }

//
    public Document setPhoto(String photoUrl){
        photo = new Photo(photoUrl);
        return this;
    }

    public Section addSection(String sectionTitle){
        // utwórz sekcję o danym tytule i dodaj do sections
         Section sectionToAdd = new Section(sectionTitle);

         sections.add(sectionToAdd);
         return sectionToAdd;
    }

    public Document addSection(Section s){
        sections.add(s);
        return this;
    }

    public void writeHTML(PrintStream out) {
        out.println("<html>");
        out.println("<head>");
        out.println("<title>" + title + "</title>");
        out.println("</head>");
        out.println("<body>");

        if (photo != null) {
            photo.writeHTML(out);
        }

        for (Section section : sections) {
            section.writeHTML(out);
        }

        out.println("</body>");
        out.println("</html>");
    }

    String toJson(){
        RuntimeTypeAdapterFactory<Paragraph> adapter =
                RuntimeTypeAdapterFactory
                        .of(Paragraph.class)
                        .registerSubtype(Paragraph.class)
                        .registerSubtype(ParagraphWithList.class);
        Gson gson = new GsonBuilder().registerTypeAdapterFactory(adapter).setPrettyPrinting().create();
        return gson.toJson(this);}

    static Document fromJson(String jsonString){
        RuntimeTypeAdapterFactory<Paragraph> adapter =
                RuntimeTypeAdapterFactory
                        .of(Paragraph.class)
                        .registerSubtype(Paragraph.class)
                        .registerSubtype(ParagraphWithList.class);
        Gson gson = new GsonBuilder().registerTypeAdapterFactory(adapter).create();
        return gson.fromJson(jsonString, Document.class);
    }

}
