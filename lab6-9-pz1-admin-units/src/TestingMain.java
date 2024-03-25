import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;

public class TestingMain {
    public static void main(String[] args) throws FileNotFoundException, IllegalAccessException {
        AdminUnitList adminUnitList = new AdminUnitList();
        adminUnitList.read("csvfiles/admin-units.csv");

        PrintStream output = new PrintStream("output.txt");

        adminUnitList.fixMissingValues();

        // Przykład 1: kod, który wyświetla jednostki o nazwach na “Ż” posortowane według powierzchni
        adminUnitList.filter(a->a.name.startsWith("Ż")).sortInplaceByArea().list(output);

        // Przykład 2: wybór (i sortowanie) elementów zaczynających się na “K”
        adminUnitList.filter(a->a.name.startsWith("K")).sortInplaceByName().list(output);

        // Przykład 3: wybór jednostek będących powiatami, których parent.name to województwo małopolskie
        adminUnitList.filter(a ->
                        a.parent != null && a.parent.name.matches("województwo małopolskie")
                )
                .list(output);

        // Przykład 4: Wybór jednostek, które są jednostkami nadrzędnymi (nie mają parent)
        adminUnitList.filter(a -> a.parent == null).list(output);

        // Przykład 5: Wybór jednostek, których powierzchnia jest większa niż 1000
        adminUnitList.filter(a -> a.area > 1000).list(output);

        // Przykład 6: Wybór jednostek, nie zaczynających małym w oraz których populacja jest większa niż 700 000
        Predicate<AdminUnit> startsWithLittleW = a -> a.name.startsWith("w");
        Predicate<AdminUnit> bigPopulation = a -> a.population > 700000;

        adminUnitList.filter(startsWithLittleW.negate().and(bigPopulation))
                .sortInplaceByName()
                .list(output);

        // Testing AdminUnitQuery
        AdminUnitQuery query = new AdminUnitQuery()
                .selectFrom(adminUnitList)
                .where(a->a.area>1000)
                .or(a->a.name.startsWith("Sz"))
                .sort((a,b)->Double.compare(a.area,b.area))
                .limit(100);
        query.execute().list(output);

        // Powiaty z województwa mazowieckiego, posortowane malejąco według gęstości zaludnienia:
        AdminUnitQuery query2 = new AdminUnitQuery()
                .selectFrom(adminUnitList)
                .where(a -> a.adminLevel == 6 && "województwo mazowieckie".equals(a.parent.name))
                .sort((a, b) -> Double.compare(b.density, a.density));
        query2.execute().list(output);

        // Jednostki administracyjne o nazwach zaczynających się od "P" lub adminLevel większy niż 7, posortowane według powierzchni rosnąco:
        AdminUnitQuery query3 = new AdminUnitQuery()
                .selectFrom(adminUnitList)
                .where(a -> a.name.startsWith("P"))
                .or(a -> a.adminLevel < 7)
                .sort(Comparator.comparingDouble(a -> a.area))
                .limit(100)
                .offset(100);
        query3.execute().list(output);

        // Jednostki administracyjne o adminLevel równej 8 i populacji większej niż 500000
        AdminUnitQuery query4 = new AdminUnitQuery()
                .selectFrom(adminUnitList)
                .where(a -> a.adminLevel == 8)
                .and(a -> a.population > 500000);
        query4.execute().list(output);

        // testy sąsiadów
        AdminUnit unitToCheck = adminUnitList.selectByName("województwo małopolskie", true).units.get(0);

        double t1 = System.nanoTime()/1e6;
        adminUnitList.getNeighbors(unitToCheck,10.00).list(output);
        double t2 = System.nanoTime()/1e6;

        System.out.printf(Locale.US,"t2-t1=%f\n",t2-t1);
    }
}
