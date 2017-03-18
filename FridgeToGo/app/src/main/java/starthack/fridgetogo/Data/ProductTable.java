package starthack.fridgetogo;

import com.parse.ParseObject;
import com.parse.ParseClassName;

import java.util.Date;
import java.util.GregorianCalendar;


@ParseClassName("ProductTable")
public class ProductTable extends ParseObject {

    public ProductTable() {
        super();
    }

    public ProductTable(IngredientTable ingredient, int peremptionYear, int peremptionMonth, int peremptionDay,
                   int creationYear, int creationMonth, int creationDay, double price) {
        super();
        setBarCode(ingredient.getBarCode());
        setPeremptionDate(new GregorianCalendar(peremptionYear, peremptionMonth, peremptionDay).getTime());
        setCreationDate(new GregorianCalendar(creationYear, creationMonth, creationDay).getTime());
        setPrice(price);
    }

    public int getBarCode() {
        return getInt("barCode");
    }

    public Date getPeremptionDate() {
        return getDate("peremptionDate");
    }

    public Date getCreationDate() {
        return getDate("creationDate");
    }

    public double getPrice() {
        return getDouble("price");
    }

    public void setBarCode(int barCode) {
        put("barCode", barCode);
    }

    public void setPeremptionDate(Date peremptionDate) {
        put("peremptionDate", peremptionDate);
    }

    public void setCreationDate(Date creationDate) {
        put("creationDate", creationDate);
    }

    public void setPrice(double price) {
        put("price", price);
    }


}
