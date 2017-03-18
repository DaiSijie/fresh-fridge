package starthack.fridgetogo;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by thity27 on 18.03.17.
 */

public class BarcodeMapping {
    private Map<Long, Ingredient> mapping;

    public BarcodeMapping(){
        mapping = new HashMap<>();
    }

    public Ingredient findBarcode(long barcode){
        return mapping.get(barcode);
    }

    public void add(long barcode, Ingredient ingredient){
        mapping.put(barcode, ingredient);
    }
}
