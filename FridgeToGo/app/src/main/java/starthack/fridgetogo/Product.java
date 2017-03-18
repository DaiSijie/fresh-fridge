package starthack.fridgetogo;

import java.util.Date;
import java.util.GregorianCalendar;

public class Product {
    private static Ingredient ingredient;
    private static Date peremptionDate;
    private static Date creationDate;
    private static double price;

    public Product(Ingredient ingredient, int peremptionYear, int peremptionMonth, int peremptionDay,
                   int creationYear, int creationMonth, int creationDay, double price) {
        this.ingredient = ingredient;
        this.peremptionDate = new GregorianCalendar(peremptionYear, peremptionMonth, peremptionDay).getTime();
        this.peremptionDate = new GregorianCalendar(creationYear, creationMonth, creationDay).getTime();
        this.price = price;
    }

    public Ingredient getIngredient(){
        return ingredient;
    }

    public Date getPeremptionDate(){
        return peremptionDate;
    }

    public Date getCreationDate(){
        return creationDate;
    }

    public double getPrice(){
        return price;
    }
}
