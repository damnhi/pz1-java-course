import java.util.List;

public class AdminUnit {
    String name;
    int adminLevel;
    double population;
    double area;
    double density;
    AdminUnit parent;
    BoundingBox bbox = new BoundingBox();
    List<AdminUnit> children;

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append(name)
                .append(", ");

        if (adminLevel != 0) {
            stringBuilder.append(adminLevel);
            stringBuilder.append(", ");
        } else {
            stringBuilder.append(", ");
        }

        if (population != 0.0) {
            stringBuilder
                    .append(population)
                    .append(", ");
        } else {
            stringBuilder.append(", ");
        }

        if (area != 0.0) {
            stringBuilder.append(area);
        } else {
            stringBuilder.append(", ");
        }

        stringBuilder.append(", ")
                .append(density);

        if (!bbox.isEmpty()) {
            stringBuilder.append(", ")
                    .append(bbox);
        }

        return stringBuilder.toString();
    }

}