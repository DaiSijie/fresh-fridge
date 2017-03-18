package starthack.fridgetogo.data1;

import com.parse.ParseObject;
import com.parse.ParseClassName;


@ParseClassName("IngredientTable")
public class IngredientTable extends ParseObject {


    public IngredientTable() {
        super();
    }

    public IngredientTable(String name, int barCode, int quantity, boolean quantityInMass, boolean inFridge, int minTemp,
                           int maxTemp, int minHum, int maxHum) {
        super();
        setName(name);
        setBarCode(barCode);
        setQuantity(quantity);
        setQuantityInMass(quantityInMass);
        setInFridge(inFridge);
        setMinTemp(minTemp);
        setMaxTemp(maxTemp);
        setMinHum(minHum);
        setMaxHum(maxHum);
    }

    public int getBarCode() {
        return getInt("barCode");
    }

    public String getName() {
        return getString("name");
    }

    public int getQuantity() {
        return getInt("quantity");
    }

    public int getMinTemp() {
        return getInt("min_temperature");
    }

    public int getMaxTemp() {
        return getInt("max_temperature");
    }

    public double getMinHum() {
        return getDouble("min_humidity");
    }

    public double getMaxHum() {
        return getDouble("max_humidity");
    }

    public void setName(String newName) {
        put("name", newName);
    }

    public void setBarCode(int barCode) {
        put("barCode", barCode);
    }

    public void setQuantity(int quantity) {
        put("quantity", quantity);
    }

    public void setMinTemp(int min_temperature) {
        put("min_temperature", min_temperature);
    }

    public void setMaxTemp(int max_temperature) {
        put("max_temperature", max_temperature);
    }

    public void setMinHum(double min_humidity) {
        put("min_humidity", min_humidity);
    }

    public void setMaxHum(double max_humidity) {
        put("max_humidity", max_humidity);
    }

    public void setQuantityInMass(boolean quantityInMass) {
        put("quantityInMass", quantityInMass);
    }

    public void setInFridge(boolean inFridge) {
        put("inFridge", inFridge);
    }
}
