package starthack.fridgetogo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by thity27 on 18.03.17.
 */

public class Database {

    //THIS SHOULD BE HARDCODED
    private static ArrayList<String> ingredient = fillStuff1();
    //private static ArrayList<String> dishNames = fillStuff3();
   // private static ArrayList<ArrayList<String>> ingredientsPerDish = fillStuff4();

    //THIS IS THE REAL DYNAMIC DATA
    private static HashMap<String, ArrayList<Date>> fridgeContent = new HashMap<>();
    private static HashMap<Long, String> barcodeToIngredient = new HashMap<>();
    public static String currentIngredient = "xxx";

    //Machine learning stuff
    private static ArrayList<Double[]> quotes = fillStuff2();
    private static Double[] w;

    private static ArrayList<Double[]> fillStuff2(){
        return new ArrayList<Double[]>(Arrays.asList(new Double[][]{{0.8,0.1,0.4,0.,0.6,0.1},{0.,0.2,0.8,0.,0.1,0.3},{0.1,0.2,0.7,0.9,0.3,0.2},
        {0.1,0.3,0.5,0.9,0.5,0.1},{0.7,0.3,0.2,0.,0.7,0.3},{0.7,0.2,0.4,0.,0.3,0.2},{0.1,0.,0.2,0.,0.8,0.1},
        {0.,0.4,0.8,0.,0.5,0.3},{1.,0.8,0.5,0.,0.5,0.2},{1.,0.1,0.3,0.,0.5,0.1},{0.,0.5,1.,0.,0.3,0.},{0.7,0.,0.1,0.,0.8,0.4},
        {0.,0.,1.,0.,0.1,0.2},{0.,0.,1.,0.,0.3,0.},{0.7,0.7,0.6,0.,0.7,0.1},{0.,0.,0.7,0.,0.3,0.8},{0.,0.,0.,0.,0.9,0.9},
        {0.,0.,0.1,0.,0.8,0.4},{0.,0.,0.,0.,0.9,0.2},{0.,0.,0.,0.,0.9,0.2},{0.9,0.4,0.6,0.,0.8,0.},{0.9,0.6,0.3,0.,0.9,0.3},
        {0.,0.8,0.9,0.,0.6,0.6},{0.,0.,0.2,0.,0.6,0.1},{0.,0.,0.2,0.,0.8,1.},{0.,1.,1.,0.,0.5,0.},{0.,1.,1.,0.,0.1,0.},{1.,0.3,0.5,0.,0.3,0.1},
        {0.,0.,0.9,0.,0.4,0.8},{0.2,0.,0.3,0.,0.6,0.},{1.,0.,0.4,0.,0.8,0.},{0.2,0.3,0.8,1.,0.2,0.},{1.,0.,0.,0.,0.4,0.},
        {0.,0.8,0.9,1.,0.6,0.2},{0.,1.,0.9,0.,0.8,0.6},{0.,0.6,0.2,0.,0.5,0.5},{0.,1.,1.,0.,0.4,0.3}}));
    }

    private static ArrayList<String> fillStuff1(){
        return new ArrayList<String>(Arrays.asList(new String[]{"Curry","Nuts","White wine","Potatoes","Pepper",
                "Bacon","Vinegar","Pecorino","Lemon","Veal","Sour cream","Celery","Beef sirloin",
                "Basilic","Cocoa powder","Beef Jerky","Lasagna noodles","Vanilla extract","Tomatoes",
                "Mascarpone","Shrimp","Milk","Sausage","Ketchup","Onion","Baking soda","Flour",
                "Broccoli","Tuna","Green peas","Parmesan","Raclette","Avocado","Chicken","Parsley",
                "Whipped cream","Paris mushroom","Ground beef","Mozzarella","Sesame seeds","Yogurt",
                "Pignons","Red wine","Coffee","Kale","Corn Flakes","Toast","Red bel pepper",
                "Coconut milk","Cabbage","Sugar","Rice","Pizza dough","Egg","Fondue cheese","Butter",
                "Buns","Chocolate","Biscuits","Olives","Olive oil","Mayo","Fajita bread","Carrots",
                "Ham","Garlic","Pasta","Mustard"}));
    }

    private Database(){
        //empty hidden constructor
    }

    private static Double[] getw(){
        return w;
    }

    public static ArrayList<Product> getFridgeAsProductList(){
        ArrayList<Product> toReturn = new ArrayList<>();
        for(Map.Entry<String, ArrayList<Date>> e : fridgeContent.entrySet()){
            for(Date d: e.getValue()){
                Product p = new Product(e.getKey(), d, d);
                toReturn.add(p);
            }
        }
        return toReturn;
    }

    private static void setw(Double[] neww){
        w = neww;
    }

    public static ArrayList<String> getIngredientList(){
        return ingredient;
    }

    public static HashMap<String, ArrayList<Date>> getFridgeContent(){
        return fridgeContent;
    }

    public static void putNewObjectInFridge(String name, Date date){
        if(!fridgeContent.containsKey(name))
            fridgeContent.put(name, new ArrayList<Date>());
        fridgeContent.get(name).add(date);
    }

    public static boolean removeObjectInFridge(String name, Date date){
        if(fridgeContent.containsKey(name))
            return fridgeContent.get(name).remove(date);
        else return false;
    }

    public static boolean isBarcodeKnown(Long code){
        return barcodeToIngredient.containsKey(code);
    }

    public static String getIngredientFromCode(Long code){
        return barcodeToIngredient.get(code);
    }

    public static void putCodeToIngredient(String ingredient, Long code){
        barcodeToIngredient.put(code, ingredient);
    }



}
