package starthack.fridgetogo;

public class MessageHandler {

    public final int RED = 0;
    public final int ORANGE = 1;
    public final int YELLOW = 2;

    private int light;
    private UDPServer server;
    private UDPClient client;
    private long [] info;

    public MessageHandler(UDPClient client, UDPServer server) throws Exception {
        // client = new UDPClient("172.27.2.156", 8502);
        // server = new UDPServer(8501);

        this.client = client;
        this.server = server;
        this.info = new long[2];
    }

    public long[] getInfo(UDPServer server) throws Exception{
        String message = server.getPacket();
        long temperature = getTemperature(message);
        long humidity = getHumidity(message);

        info[0] = temperature;
        info[1] = humidity;
        return info;
    }

    public long getTemperature(String message) throws Exception{
        String temperature_str = message.substring(message.indexOf("T = ")+4, message.indexOf("H")-1);
        long temperature = new Long(temperature_str);

        return temperature;
    }
    public long getHumidity(String message) throws Exception{
        String humidity_str = message.substring(message.indexOf("H = ")+4, message.length());
        long humidity = new Long(humidity_str);

        return humidity;
    }

    public void setLight(int newLight) {
        light = newLight;
    }

    public void sendChangeDiode() throws Exception {
        client.sendMessage(Integer.toString(light));
    }

}
