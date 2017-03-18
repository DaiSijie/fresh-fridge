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
        mapping.get(barcode)
    }
}
