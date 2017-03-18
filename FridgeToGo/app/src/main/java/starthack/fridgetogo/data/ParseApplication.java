package starthack.fridgetogo.data;

import android.app.Application;

public class ParseApplication extends Application {
    public static String PARSE_APPLICATION_ID = "";
    public static String PARSE_CLIENT_KEY = "";

    @Override
    public void onCreate() {
        super.onCreate();

        // Within the Android Application where Parse is initialized
        /*Parse.enableLocalDatastore(this);
        Parse.initialize(this, PARSE_APPLICATION_ID, PARSE_CLIENT_KEY);
        // set applicationId, and server server based on the values in the Heroku settings.
        // clientKey is not needed unless explicitly configured
        // any network interceptors must be added with the Configuration Builder given this syntax
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(PARSE_APPLICATION_ID) // should correspond to APP_ID env variable
                .clientKey(PARSE_CLIENT_KEY)  // set explicitly unless clientKey is explicitly configured on Parse server
                .addNetworkInterceptor(new ParseLogInterceptor())
                .server("https://my-parse-app-url.herokuapp.com/parse/").build());
        // Register your parse models
        //IngredientTable.registerSubclass(IngredientTable.class);*/
    }
}
