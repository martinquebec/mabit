package mabit.util;

import com.fatboyindustrial.gsonjodatime.Converters;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by martin on 4/9/2016.
 */
public class GsonUtils {
    private static Gson me =null;

    private static Gson createGSon() {
        return Converters.registerDateTime(new GsonBuilder()).create();
    }

    public static Gson get() {
        return (me==null) ? me = createGSon() : me;
    }
}
