package starthack.fridgetogo;

public class MessageHandler {

    enum Light {
        RED, ORANGE, YELLOW
    }

    private  Light light;
    private  UDPServer server;
    private  UDPClient client;
    private long [] info;

    public MessageHandler(UDPClient client, UDPServer server) throws Exception {
        // client = new UDPClient("172.27.2.156", 8502);
        // server = new UDPServer(8501);

        this.client = client;
        this.server = server;
    }

    public long[] getInfo(UDPServer server) throws Exception{
        String message = server.getPacket();
        long temperature = getTemperature(message);
        long humidity = getHumidity(message);

        long[] info = new long[2];
        this.info = info;
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

    public void setLight(Light newLight) {
        light = newLight;
    }

    public void sendChangeDiode() throws Exception {
        client.sendMessage(light.toString());
        client.close();
    }

}
