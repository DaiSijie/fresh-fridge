package starthack.fridgetogo;

import java.util.Date;
import java.util.GregorianCalendar;

public class Product {
    private String ingredient;
    private Date peremptionDate;
    private Date creationDate;

    public Product(String ingredient, int peremptionYear, int peremptionMonth, int peremptionDay,
                   int creationYear, int creationMonth, int creationDay) {
        this.ingredient = ingredient;
        this.peremptionDate = new GregorianCalendar(peremptionYear, peremptionMonth, peremptionDay).getTime();
        this.creationDate = new GregorianCalendar(creationYear, creationMonth, creationDay).getTime();
    }

    public Product(String ingredient, int peremptionYear, int peremptionMonth, int peremptionDay,
                   Date creationDate) {
        this.ingredient = ingredient;
        this.peremptionDate = new GregorianCalendar(peremptionYear, peremptionMonth, peremptionDay).getTime();
        this.creationDate = creationDate;
    }

    public Product(String ingredient, Date peremptionDate,
                   Date creationDate) {
        this.ingredient = ingredient;
        this.peremptionDate = peremptionDate;
        this.creationDate = creationDate;
    }

    public String getIngredient(){
        return ingredient;
    }

    public Date getPeremptionDate(){
        return peremptionDate;
    }

    public Date getCreationDate(){
        return creationDate;
    }
}
