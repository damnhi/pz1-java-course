import java.text.DecimalFormat;
import java.util.Locale;

public class BoundingBox {
    double xmin = Double.NaN;
    double ymin = Double.NaN;
    double xmax = Double.NaN;
    double ymax = Double.NaN;

    /**
     * Powiększa BB tak, aby zawierał punkt (x,y)
     * Jeżeli był wcześniej pusty - wówczas ma zawierać wyłącznie ten punkt
     * @param x - współrzędna x
     * @param y - współrzędna y
     */

    void addPoint(double x, double y){
        if(isEmpty()){
            this.xmin = x;
            this.ymin = y;
            this.xmax = x;
            this.ymax = y;
        }
        // Set X cord
        this.xmax = Double.max(this.xmax, x);
        this.xmin = Double.min(this.xmin, x);

        // Set Y cord
        this.ymax = Double.max(this.ymax, y);
        this.ymin = Double.min(this.ymin, y);
    }

    boolean contains(double x, double y){
        return x <= xmax && x >= xmin && y <= ymax && y >= ymin;
    }

    /**
     * Sprawdza czy dany BB zawiera bb
     * @param bb
     * @return
     */
    boolean contains(BoundingBox bb){
        return this.contains(bb.xmax, bb.ymax) && this.contains(bb.xmin ,bb.ymin);
    }

    /**
     * Sprawdza, czy dany BB przecina się z bb
     * @param bb
     * @return
     */
    boolean intersects(BoundingBox bb){
        if (this.ymax < bb.ymin || this.ymin > bb.ymax) {
            return false;
        }
        if (this.xmax < bb.xmin || this.xmin > bb.xmax) {
            return false;
        }
        return true;
    }
    /**
     * Powiększa rozmiary tak, aby zawierał bb oraz poprzednią wersję this
     * Jeżeli był pusty - po wykonaniu operacji ma być równy bb
     * @param bb
     * @return
     */
    BoundingBox add(BoundingBox bb){
        if (bb == null || bb.isEmpty()) {
            return this;
        }
        if (isEmpty()){
            this.addPoint(bb.xmax, bb.ymax);
            this.addPoint(bb.xmin, bb.ymin);
        }
        this.xmax  = Math.max(this.xmax , bb.xmax);
        this.ymax = Math.max(this.ymax, bb.ymax);
        this.xmin = Math.min(this.xmin , bb.xmin);
        this.ymin = Math.min(this.ymin, bb.ymin);
        return this;
    }

    /**
     * Sprawdza czy BB jest pusty
     * @return
     */
    boolean isEmpty(){
        return Double.isNaN(xmin) && Double.isNaN(xmax);
    }

    /**
     * Sprawdza czy
     * 1) typem o jest BoundingBox
     * 2) this jest równy bb
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoundingBox that = (BoundingBox) o;
        if (that.isEmpty() && this.isEmpty()) return true;
        if (that.isEmpty() || this.isEmpty()) return false;
        return Double.compare(that.xmin, xmin) == 0 && Double.compare(that.xmax, xmax) == 0 && Double.compare(that.ymin, ymin) == 0 && Double.compare(that.ymax, ymax) == 0;
    }

    /**
     * Oblicza i zwraca współrzędną x środka
     * @return if !isEmpty() współrzędna x środka else wyrzuca wyjątek
     * (sam dobierz typ)
     */
    double getCenterX() throws IllegalAccessException {
        if (!isEmpty()){
            return (xmax + xmin)/2;
        }
        else {
            throw new IllegalAccessException("Box is empty!");
        }
    }

    /**
     * Oblicza i zwraca współrzędną y środka
     * @return if !isEmpty() współrzędna y środka else wyrzuca wyjątek
     * (sam dobierz typ)
     */
    double getCenterY() throws IllegalAccessException {
        if (!isEmpty()){
            return (ymax + ymin)/2;
        }
        else {
            throw new IllegalAccessException("Box is empty!");
        }
    }

    /**
     * Oblicza odległość pomiędzy środkami this bounding box oraz bbx
     * @param bbx prostokąt, do którego liczona jest odległość
     * @return if !isEmpty odległość, else wyrzuca wyjątek lub zwraca maksymalną możliwą wartość double
     * Ze względu na to, że są to współrzędne geograficzne, zamiast odległości użyj wzoru haversine
     * (ang. haversine formula)
     *
     * Gotowy kod można znaleźć w Internecie...
     */
    double distanceTo(BoundingBox bbx) throws IllegalAccessException {
        if (!isEmpty() && !bbx.isEmpty()){
           return haversine(this.getCenterX(),this.getCenterY(),bbx.getCenterX(),bbx.getCenterY());

        }
        else {
            throw new IllegalAccessException("Box is empty!");
        }
    }

    double haversine(double lat1, double lon1,
                            double lat2, double lon2)
    {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        double a = Math.pow(Math.sin(dLat / 2), 2) +
                Math.pow(Math.sin(dLon / 2), 2) *
                        Math.cos(lat1) *
                        Math.cos(lat2);
        double rad = 6371;
        double c = 2 * Math.asin(Math.sqrt(a));
        return rad * c;
    }

    public String toString() {
        return xmin + ", " + ymin + ", " + xmax + ", " + ymax;
    }

    public void getWKT() {
        if (isEmpty()) {
            System.out.println("EMPTY");
        } else {
            System.out.printf(Locale.US,"POLYGON((%f %f, %f %f, %f %f, %f %f))",
                    xmin, ymin, xmax, ymin, xmax, ymax, xmin, ymax
            );
        }
    }


}
