import java.io.IOException;
import java.io.PrintStream;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class AdminUnitList {
    List<AdminUnit> units = new ArrayList<>();
    /**
     * Czyta rekordy pliku i dodaje do listy
     * @param filename nazwa pliku
     */

    public void read(String filename) {
        try {
            CSVReader reader = new CSVReader(filename,",",true);
            Map<Long, AdminUnit> idToUnit = new HashMap<>();
            Map<AdminUnit, Long> unitToParentID = new HashMap<>();
            Map<Long,List<AdminUnit>> parentid2child = new HashMap<>();

            while(reader.next()){
                AdminUnit adminUnit = new AdminUnit();
                adminUnit.name = reader.get("name");

                if(!reader.isMissing("area"))
                    adminUnit.area = reader.getDouble("area");
                else adminUnit.area  = 0.0;

                Long idOfUnit = reader.getLong("id");
                Long idOfParentUnit;

                if(!reader.isMissing("parent"))
                    idOfParentUnit = reader.getLong("parent");
                else idOfParentUnit = null;

                idToUnit.put(idOfUnit, adminUnit);
                unitToParentID.put(adminUnit, idOfParentUnit);

                if(!reader.isMissing("admin_level"))
                    adminUnit.adminLevel = reader.getInt("admin_level");
                else adminUnit.adminLevel = 0;

                if(!reader.isMissing("population"))
                    adminUnit.population = reader.getDouble("population"); //moze byc pusta
                else adminUnit.population = 0.0;

                if(!reader.isMissing("density"))
                    adminUnit.density = reader.getDouble("density");   //moze byc pusta
                else adminUnit.density = 0.0;

                if(!reader.isMissing("x1") && !reader.isMissing("y1"))
                    adminUnit.bbox.addPoint(reader.getDouble("x1"),reader.getDouble("y1"));
                if(!reader.isMissing("x2") && !reader.isMissing("y2"))
                    adminUnit.bbox.addPoint(reader.getDouble("x2"),reader.getDouble("y2"));
                if(!reader.isMissing("x3") && !reader.isMissing("y3"))
                    adminUnit.bbox.addPoint(reader.getDouble("x3"),reader.getDouble("y3"));
                if(!reader.isMissing("x4") && !reader.isMissing("y4"))
                    adminUnit.bbox.addPoint(reader.getDouble("x4"),reader.getDouble("y4"));
                units.add(adminUnit);
                if(!reader.isMissing("x5") && !reader.isMissing("y5"))
                    adminUnit.bbox.addPoint(reader.getDouble("x5"),reader.getDouble("y5"));
            }

            unitToParentID.forEach((unit, idParent) -> {
                unit.parent = idToUnit.get(idParent);
            });
            // Jeśli nie znajdzie klucza zwraca null, więc do unit.parent będzie przypisany null

            for (AdminUnit unit : units){
               Long parentID =  unitToParentID.get(unit);
               if(parentID == null){
                   continue;
               }
               if (parentid2child.containsKey(parentID)) {
                   parentid2child.get(parentID).add(unit);
               }
               else {
                   List<AdminUnit> adminUnitList = new ArrayList<>();
                   adminUnitList.add(unit);
                   parentid2child.put(parentID, adminUnitList);
               }
            }

            parentid2child.forEach((idParent, children) -> {
                if(idParent != null) {
                    AdminUnit parentUnit = idToUnit.get(idParent);
                    parentUnit.children = children;
                }
            });

        }
        catch (IOException ioException){
            System.out.println(ioException.getMessage());
        }
    }


    /**
     * Wypisuje zawartość korzystając z AdminUnit.toString()
     * @param out
     */
    void list(PrintStream out){
        for (AdminUnit unit : units){
            out.println(unit);
        }
    }
    /**
     * Wypisuje co najwyżej limit elementów począwszy od elementu o indeksie offset
     * @param out - strumień wyjsciowy
     * @param offset - od którego elementu rozpocząć wypisywanie
     * @param limit - ile (maksymalnie) elementów wypisać
     */
    void list(PrintStream out,int offset, int limit ){
        int endOfList = Math.min(offset + limit, units.size());
        for (int i = offset; i < endOfList; i++){
            out.println(units.get(i));
        }
    }

    /**
     * Zwraca nową listę zawierającą te obiekty AdminUnit, których nazwa pasuje do wzorca
     * @param pattern - wzorzec dla nazwy
     * @param regex - jeśli regex=true, użyj finkcji String matches(); jeśli false użyj funkcji contains()
     * @return podzbiór elementów, których nazwy spełniają kryterium wyboru
     */
    AdminUnitList selectByName(String pattern, boolean regex){
        AdminUnitList ret = new AdminUnitList();
        // przeiteruj po zawartości units
        // jeżeli nazwa jednostki pasuje do wzorca dodaj do ret
        for (AdminUnit unit : units) {
            if (regex) {
                if (unit.name.matches(pattern)) {
                    ret.units.add(unit);
                }
            } else {
                if (unit.name.contains(pattern)) {
                    ret.units.add(unit);
                }
            }
        }
        return ret;
    }


    private void fixMissingValues(AdminUnit unit) {
        if (unit != null && unit.density == 0.0) {
            if (unit.parent != null) {
                fixMissingValues(unit.parent);
                unit.density = unit.parent.density;

                if (unit.population == 0.0)
                    unit.population = unit.area * unit.density;
            }
        }
    }

    public void fixMissingValues() {
        for (AdminUnit unit : units) {
            fixMissingValues(unit);
        }
    }

    /**
     * Zwraca listę jednostek sąsiadujących z jendostką unit na tym samym poziomie hierarchii admin_level.
     * Czyli sąsiadami wojweództw są województwa, powiatów - powiaty, gmin - gminy, miejscowości - inne miejscowości
     * @param unit - jednostka, której sąsiedzi mają być wyznaczeni
     * @param maxdistance - parametr stosowany wyłącznie dla miejscowości, maksymalny promień odległości od środka unit,
     *                    w którym mają sie znaleźć punkty środkowe BoundingBox sąsiadów
     * @return lista wypełniona sąsiadami
     */
    AdminUnitList getNeighbors(AdminUnit unit, double maxdistance) throws IllegalAccessException {
        AdminUnitList neighborsUnitList = new AdminUnitList();
        List<AdminUnit> neighborUnits = new ArrayList<>();

        for (AdminUnit adminUnit : units){
            if((adminUnit.adminLevel != unit.adminLevel) || unit.equals(adminUnit)){
                continue;
            }
            if (unit.bbox.intersects(adminUnit.bbox) || (unit.adminLevel == 8 && unit.bbox.distanceTo(adminUnit.bbox) <= maxdistance)){
                neighborUnits.add(adminUnit);
            }
        }
        neighborsUnitList.units = neighborUnits;

        return neighborsUnitList;
    }

    /**
     * Sortuje daną listę jednostek (in place = w miejscu)
     * @return this
     */
    AdminUnitList sortInplaceByName(){
        class NameComparator implements Comparator<AdminUnit> {
            @Override
            public int compare(AdminUnit o1, AdminUnit o2) {
                return o1.name.compareTo(o2.name);
            }
        }
        units.sort(new NameComparator());
        return this;
    }

    /**
     * Sortuje daną listę jednostek (in place = w miejscu)
     * @return this
     */
    AdminUnitList sortInplaceByArea(){
        units.sort(new Comparator<AdminUnit>(){
            public int compare(AdminUnit o1, AdminUnit o2) {
                return Double.compare(o1.area, o2.area);
            }
        }
        );
        return this;
    }

    /**
     * Sortuje daną listę jednostek (in place = w miejscu)
     * @return this
     */
    AdminUnitList sortInplaceByPopulation(){
        units.sort((o1,o2)->Double.compare(o1.population, o2.population));
        return this;
    }

    AdminUnitList sortInplace(Comparator<AdminUnit> cmp){
        units.sort(cmp);
        return this;
    }

    AdminUnitList sort(Comparator<AdminUnit> cmp){
        AdminUnitList adminUnitList = new AdminUnitList();
        adminUnitList.units = new ArrayList<>(this.units);
        return adminUnitList.sortInplace(cmp);
    }


    /**
     * Zwraca co najwyżej limit elementów spełniających pred począwszy od offset
     * Offest jest obliczany po przefiltrowaniu
     * @param pred - predykat
     * @param - od którego elementu
     * @param limit - maksymalna liczba elementów
     * @return nową listę
     */
    AdminUnitList filter(Predicate<AdminUnit> pred, int offset, int limit){
        AdminUnitList adminUnitList = new AdminUnitList();
        adminUnitList.units = units.stream()
                .filter(pred)
                .skip(offset)
                .limit(limit)
                .collect(Collectors.toList());
        return adminUnitList;
    }

    /**
     * Zwraca co najwyżej limit elementów spełniających pred
     * @param pred - predykat
     * @param limit - maksymalna liczba elementów
     * @return nową listę
     */
    AdminUnitList filter(Predicate<AdminUnit> pred, int limit){
        return this.filter(pred,0, limit);
    }

    AdminUnitList filter(Predicate<AdminUnit> pred) {
        return this.filter(pred,0, Integer.MAX_VALUE);
    }


}