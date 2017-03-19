package starthack.fridgetogo;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by thity27 on 19.03.17.
 */

public class XDKThread  extends Thread {

    private UDPClient client;
    private UDPServer server;
    private MessageHandler messageHandler;
    private ArrayList<String> cart;

    HashMap<String, Double> maxM = new HashMap<>();
    HashMap<String, Double> minM = new HashMap<>();

    public XDKThread(String threadName, ArrayList<String> cart, HashMap<String, Double> maxM, HashMap<String, Double> minM){
        this.cart = cart;
        this.minM = minM;
        this.maxM = maxM;
        try {
            client = new UDPClient("192.168.43.116", 8501);
            server = new UDPServer(8502);
            messageHandler = new MessageHandler(client, server);
        }catch(Exception e){
            Log.d("error", "some error");
        }
    }

    public void run() {
        long[] info = new long[2];
        while(true){
            try {
                info = messageHandler.getInfo(server);
                Database.lock.lock();
                try{
                    Database.temperature = info[0];
                    Database.humidity = info[1];
                } finally {
                    Database.lock.unlock();
                }
                messageHandler.setLight(getHappinness(info[0]/1000.));
                messageHandler.sendChangeDiode();
                this.sleep(500);
            } catch (Exception e) {
                Log.d("error", e.toString());
            }
        }
    }

    public void addCartElement(String s){
        this.cart.add(s);
    }

    public void flushCart(){
        this.cart.clear();
    }

    /**
     * 1 = very happy
     * 0.5 = meh
     * 0 = very unhappy
     * @param temperature
     * @return
     */
    public int getHappinness(double temperature){


        double tolerance = 5.0;

        //if every ingredient is in its zone then very happy
        boolean happy1 = true;
        boolean happy2 = true;
        for(String ing: cart){
            happy1 = happy1 && temperature >= minM.get(ing) && temperature <= maxM.get(ing);
            happy2 = happy2 && temperature >= minM.get(ing) - tolerance && temperature <= maxM.get(ing) + tolerance;
        }

        return happy1? 2 : (happy2? 1 : 0);
    }

}
