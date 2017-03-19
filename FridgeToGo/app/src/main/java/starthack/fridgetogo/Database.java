package starthack.fridgetogo;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by thity27 on 18.03.17.
 */

public class Database {

    public static boolean someChange1 = true;
    public static boolean someChange2 = true;

    public static XDKThread t;

    public static Long barcode;

    public static boolean shoppingMode = false;

    //THIS SHOULD BE HARDCODED
    private static ArrayList<String> ingredient = fillStuff1();
    private static ArrayList<String> dishNames = fillStuff3();
    private static ArrayList<ArrayList<String>> ingredientsPerDish = fillStuff4();
    private static HashMap<String, Double> ing2max = fillStuff5();
    private static HashMap<String, Double> ing2min = fillStuff6();

    //THIS IS THE REAL DYNAMIC DATA
    public static HashMap<String, ArrayList<Date>> fridgeContent = new HashMap<>();
    public static HashMap<String, ArrayList<Date>> cartContent = new HashMap<>();
    public static HashMap<Long, String> barcodeToIngredient = new HashMap<>();
    public static String currentIngredient = "";

    //Machine learning stuff
    private static ArrayList<Double[]> quotes = fillStuff2();
    private static Double[] w = {0.,0.,0.,0.,0.,0.};
    public static final Classifier c = new Classifier();
    public static final Suggester s = new Suggester();

    public static ArrayList<Double[]> getQuotes(){
        return quotes;
    }

    public static HashMap<String, ArrayList<Date>> getCart(){
        return cartContent;
    }



    public static void putInCart(String name, Date date) {
        if (!cartContent.containsKey(name))
            cartContent.put(name, new ArrayList<Date>());
        cartContent.get(name).add(date);
        t.addCartElement(name);
    }

    public static void throwCartInFridge(){
        for(Map.Entry<String, ArrayList<Date>> e : cartContent.entrySet()){
            if(!fridgeContent.containsKey(e.getKey()))
                fridgeContent.put(e.getKey(), new ArrayList<Date>());
            fridgeContent.get(e.getKey()).addAll(e.getValue());
        }

        someChange1 = true;
        someChange2 = true;

        cartContent = new HashMap<>();
        t.flushCart();
    }


    private static ArrayList<String> fillStuff3(){
        String[] data = {"Chicken parmesan", "Tomato pasta", "Tuna pasta", "Tuna toasts", "Lasagna",
                "Curry chicken", "Croque-monsieurs", "Pizza", "Beef stir-fry", "Zürcher Geschnetzeltes",
                "Pesto pasta", "Carbonara pasta", "Omelette", "Sunny-side up egg", "Fajita",
                "Corn Flakes", "Cookies", "Rösti", "Fondue", "Raclette", "Hamburger", "Burrito",
                "Coleslaw", "Risotto", "Tiramisu", "Guacamole", "Kale salad", "Cabbage meatballs",
                "Crepes", "Gratted pasta", "Beef stroganoff", "Lemon shrimps", "Sausage",
                "Tuna salad", "Russian salad", "Mashed potatoes", "Tomato-mozzarella salad"};
        return new ArrayList<String>(Arrays.asList(data));
    }

    private static HashMap<String, Double> fillStuff5(){
        String[][] data = {{"Curry", "-20", "60"},
            {"Nuts", "0", "80"},
            {"White wine", "15", "30"},
            {"Potatoes", "10", "30"},
            {"Pepper", "-20", "60"},
            {"Bacon", "5", "15"},
            {"Vinegar", "0", "30"},
            {"Pecorino", "5", "15"},
            {"Lemon", "10", "40"},
            {"Veal", "5", "15"},
            {"Sour cream", "5", "10"},
            {"Celery", "15", "30"},
            {"Beef sirloin", "5", "15"},
            {"Basilic", "5", "15"},
            {"Cocoa powder", "-20", "60"},
            {"Beef Jerky", "5", "30"},
            {"Lasagna noodles", "-20", "60"},
            {"Vanilla extract", "-20", "60"},
            {"Tomatoes", "10", "30"},
            {"Mascarpone", "5", "15"},
            {"Shrimp", "5", "15"},
            {"Milk", "5", "20"},
            {"Sausage", "5", "20"},
            {"Ketchup", "0", "30"},
            {"Onion", "10", "30"},
            {"Baking soda", "-20", "60"},
            {"Flour", "-20", "60"},
            {"Broccoli", "10", "30"},
            {"Tuna", "5", "15"},
            {"Green peas", "10", "30"},
            {"Parmesan", "5", "15"},
            {"Raclette", "5", "15"},
            {"Avocado", "15", "20"},
            {"Chicken", "5", "15"},
            {"Parsley", "10", "30"},
            {"Whipped cream", "5", "15"},
            {"Paris mushroom", "5", "20"},
            {"Ground beef", "5", "15"},
            {"Mozzarella", "5", "15"},
            {"Sesame seeds", "-20", "60"},
            {"Yogurt", "5", "15"},
            {"Pignons", "0", "30"},
            {"Red wine", "15", "30"},
            {"Coffee", "0", "30"},
            {"Kale", "10", "20"},
            {"Corn Flakes", "0", "50"},
            {"Toast", "5", "30"},
            {"Red bel pepper", "10", "20"},
            {"Coconut milk", "10", "30"},
            {"Cabbage", "10", "30"},
            {"Sugar", "-20", "60"},
            {"Rice", "-20", "60"},
            {"Pizza dough", "5", "15"},
            {"Egg", "5", "25"},
            {"Fondue cheese", "5", "15"},
            {"Butter", "5", "15"},
            {"Buns", "10", "30"},
            {"Chocolate", "-20", "30"},
            {"Biscuits", "-20", "30"},
            {"Olives", "10", "40"},
            {"Olive oil", "-10", "60"},
            {"Mayo", "5", "30"},
            {"Fajita bread", "5", "30"},
            {"Carrots", "5", "30"},
            {"Ham", "5", "20"},
            {"Garlic", "5", "30"},
            {"Pasta", "-20", "60"},
            {"Mustard", "5", "25"}};

        HashMap<String, Double> ing2max = new HashMap<>();
        for(int i = 0; i < data.length; i++){
            ing2max.put(data[i][0], Double.parseDouble(data[i][2]));
        }
        return ing2max;
    }

    private static HashMap<String, Double> fillStuff6(){
        String[][] data = {{"Curry", "-20", "60"},
                        {"Nuts", "0", "80"},
                        {"White wine", "15", "30"},
                        {"Potatoes", "10", "30"},
                        {"Pepper", "-20", "60"},
                        {"Bacon", "5", "15"},
                        {"Vinegar", "0", "30"},
                        {"Pecorino", "5", "15"},
                        {"Lemon", "10", "40"},
                        {"Veal", "5", "15"},
                        {"Sour cream", "5", "10"},
                        {"Celery", "15", "30"},
                        {"Beef sirloin", "5", "15"},
                        {"Basilic", "5", "15"},
                        {"Cocoa powder", "-20", "60"},
                        {"Beef Jerky", "5", "30"},
                        {"Lasagna noodles", "-20", "60"},
                        {"Vanilla extract", "-20", "60"},
                        {"Tomatoes", "10", "30"},
                        {"Mascarpone", "5", "15"},
                        {"Shrimp", "5", "15"},
                        {"Milk", "5", "20"},
                        {"Sausage", "5", "20"},
                        {"Ketchup", "0", "30"},
                        {"Onion", "10", "30"},
                        {"Baking soda", "-20", "60"},
                        {"Flour", "-20", "60"},
                        {"Broccoli", "10", "30"},
                        {"Tuna", "5", "15"},
                        {"Green peas", "10", "30"},
                        {"Parmesan", "5", "15"},
                        {"Raclette", "5", "15"},
                        {"Avocado", "15", "20"},
                        {"Chicken", "5", "15"},
                        {"Parsley", "10", "30"},
                        {"Whipped cream", "5", "15"},
                        {"Paris mushroom", "5", "20"},
                        {"Ground beef", "5", "15"},
                        {"Mozzarella", "5", "15"},
                        {"Sesame seeds", "-20", "60"},
                        {"Yogurt", "5", "15"},
                        {"Pignons", "0", "30"},
                        {"Red wine", "15", "30"},
                        {"Coffee", "0", "30"},
                        {"Kale", "10", "20"},
                        {"Corn Flakes", "0", "50"},
                        {"Toast", "5", "30"},
                        {"Red bel pepper", "10", "20"},
                        {"Coconut milk", "10", "30"},
                        {"Cabbage", "10", "30"},
                        {"Sugar", "-20", "60"},
                        {"Rice", "-20", "60"},
                        {"Pizza dough", "5", "15"},
                        {"Egg", "5", "25"},
                        {"Fondue cheese", "5", "15"},
                        {"Butter", "5", "15"},
                        {"Buns", "10", "30"},
                        {"Chocolate", "-20", "30"},
                        {"Biscuits", "-20", "30"},
                        {"Olives", "10", "40"},
                        {"Olive oil", "-10", "60"},
                        {"Mayo", "5", "30"},
                        {"Fajita bread", "5", "30"},
                        {"Carrots", "5", "30"},
                        {"Ham", "5", "20"},
                        {"Garlic", "5", "30"},
                        {"Pasta", "-20", "60"},
                        {"Mustard", "5", "25"}};

        HashMap<String, Double> ing2min = new HashMap<>();
        for (int i = 0; i < data.length; i++) {
            ing2min.put(data[i][0], Double.parseDouble(data[i][1]));
        }
        return ing2min;
    }

    private static ArrayList<ArrayList<String>> fillStuff4(){
        String[][] data = {{"Chicken", "Egg", "Parmesan", "Olive oil", "Basilic", "Mozzarella"},
                {"Basilic", "Onion", "Garlic", "Tomatoes", "Olive oil", "Red wine", "Pasta", "Parmesan"},
                {"Tuna", "Butter", "Olive oil", "Pasta"}, {"Toast", "Tuna", "Mayo", "Onion", "Tomatoes"},
                {"Ground beef", "Onion", "Garlic", "Basilic", "Tomatoes", "Egg", "Parmesan", "Lasagna noodles", "Mozzarella"},
                {"Olive oil", "Onion", "Garlic", "Curry", "Chicken", "Yogurt", "Coconut milk"},
                {"Toast", "Raclette", "Ham", "Ketchup"}, {"Pizza dough", "Tomatoes", "Mozzarella", "Olives"},
                {"Olive oil", "Beef sirloin", "Broccoli", "Red bel pepper", "Carrots", "Onion", "Garlic", "Sesame seeds"},
                {"Butter", "Veal", "Flour", "Onion", "Paris mushroom", "White wine"},
                {"Pasta", "Olive oil", "Basilic", "Pignons"},
                {"Bacon", "Pecorino", "Parmesan", "Egg", "Pasta", "Garlic", "Butter"}, {"Milk", "Egg"}, {"Egg"},
                {"Chicken", "Fajita bread", "Onion", "Tomatoes", "Avocado", "Sour cream"}, {"Milk", "Corn Flakes"},
                {"Sugar", "Butter", "Vanilla extract", "Egg", "Flour", "Baking soda", "Nuts", "Chocolate"},
                {"Potatoes", "Olive oil"}, {"Fondue cheese", "Garlic", "White wine"}, {"Raclette", "Beef Jerky"},
                {"Ground beef", "Buns", "Tomatoes", "Onion"}, {"Ground beef", "Rice", "Tomatoes", "Onion"},
                {"Cabbage", "Carrots", "Sour cream", "Onion", "Vinegar", "Mustard", "Celery", "Pepper"},
                {"Rice", "Parmesan", "Onion", "White wine"},
                {"Egg", "Sugar", "Milk", "Mascarpone", "Coffee", "Biscuits", "Cocoa powder"},
                {"Avocado", "Parsley", "Lemon"}, {"Kale", "Olive oil", "Vinegar"}, {"Ground beef", "Cabbage"},
                {"Milk", "Egg", "Flour"}, {"Pasta", "Whipped cream", "Parmesan", "Ham"},
                {"Beef sirloin", "Paris mushroom", "Garlic", "Onion", "Butter", "Flour", "Sour cream", "Egg"},
                {"Butter", "Garlic", "Shrimp", "Lemon", "Parsley"}, {"Sausage", "Mustard"},
                {"Tuna", "Onion", "Tomatoes", "Mayo", "Sour cream"}, {"Green peas", "Carrots", "Potatoes", "Egg", "Mayo"},
                {"Potatoes", "Whipped cream", "Butter"}, {"Tomatoes", "Mozzarella", "Basilic", "Olive oil"}};

        ArrayList<ArrayList<String>> toReturn = new ArrayList<>();
        for(int i = 0; i < data.length; i++){
            toReturn.add(new ArrayList<String>(Arrays.asList(data[i])));
        }

        return toReturn;
    }

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

    public static ArrayList<String> getIngredientsForDish(int dish){
        return ingredientsPerDish.get(dish);
    }

    public static Double[] getw(){
        return w;
    }

    public static ArrayList<Product> getFridgeAsProductList(){
        ArrayList<Product> toReturn = new ArrayList<>();
        for(Map.Entry<String, ArrayList<Date>> e : fridgeContent.entrySet()){
            Log.d("jjj", "Adding::: " + e.getKey());
            for(Date d: e.getValue()){
                Product p = new Product(e.getKey(), d, d);
                toReturn.add(p);
            }
        }

        Log.d("jjjj", "in my fridge, I have:::");
        for(Product p: toReturn)
            Log.d("jjj", p.getIngredient());

        return toReturn;
    }

    public static Double getMinForIngredient(String ing){
        return ing2min.get(ing);
    }

    public static double getMaxForIngredient(String ing){
        return ing2max.get(ing);
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
        Log.d("Mybls", "added: " + name);
        if(!fridgeContent.containsKey(name))
            fridgeContent.put(name, new ArrayList<Date>());
        fridgeContent.get(name).add(date);
        FridgeToGo.refreshPreferences();
    }

    public static boolean removeObjectInFridge(String name, Date date){
        if(fridgeContent.containsKey(name)) {
            FridgeToGo.refreshPreferences();
            return fridgeContent.get(name).remove(date);

        }
        FridgeToGo.refreshPreferences();
        return false;

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

    public static ArrayList<String> getDishNames(){
        return dishNames;
    }
}
