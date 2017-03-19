package starthack.fridgetogo;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class InfoFragment extends Fragment {

    public InfoFragment() {
        // Required empty public constructor
    }


    public static InfoFragment newInstance() {
        InfoFragment fragment = new InfoFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_info, container, false);

        TextView temperature = (TextView)view.findViewById(R.id.temperature);
        TextView humidity = (TextView)view.findViewById(R.id.humidity);

        double temperatureInDegrees = Database.getTemperature()/1000.0;
        temperature.setText("Temperature : " + temperatureInDegrees + "°C");
        humidity.setText("Humidity : " + Database.getHumidity() + "%");

        int color;
        String goodness;
        switch(getHappinness(temperatureInDegrees)){
            case 0:
                color = 0xF44336;
                goodness = "bad";
                break;
            case 1:
                color = 0xFF9800;
                goodness = "so-so";
                break;
            case 2:
                color = 0x00E676;
                goodness = "good";
                break;
            default:
                color = 0x00BCD4;
                goodness = "not known";
                break;
        }

        TextView state = (TextView)view.findViewById(R.id.status);
        state.setText("Status : " + goodness);
        //state.setBackgroundColor(color);

        return view;
    }

    public int getHappinness(double temperature){
        double tolerance = 5.0;

        //if every ingredient is in its zone then very happy
        boolean happy1 = true;
        boolean happy2 = true;
        for(String ing: Database.getCart().keySet()){
            happy1 = happy1 && temperature >= Database.getMinForIngredient(ing) && temperature <= Database.getMaxForIngredient(ing);
            happy2 = happy2 && temperature >= Database.getMinForIngredient(ing) - tolerance && temperature <= Database.getMaxForIngredient(ing) + tolerance;
        }

        return happy1? 2 : (happy2? 1 : 0);
    }
}
