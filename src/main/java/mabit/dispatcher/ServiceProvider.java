package mabit.dispatcher;

import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by martin on 8/9/2016.
 */
public enum ServiceProvider {
    INSTANCE;

    private final static Logger Log = Logger.getLogger(ServiceProvider.class);


    private Map<Class<?>,Class<?>> services = new HashMap<>();
    private Map<Class<?>,Object> cache = new HashMap<>();

    public <T> void registerService(Class<T> service, Class<? extends T> provider) {
        services.put(service,provider);
    }

    public <T> T getServiceIfCreated(Class<T> serviceType) {
        Object instance = cache.get(serviceType);
        if(instance ==null) {
           return null;
        }
        return serviceType.cast(instance);
    }

    public <T> T getService(Class<T> serviceType) {
        Class<?> providerType = services.get(serviceType);
        if(providerType==null) {
            //TODO logs
            return null;
        }
        try {
            Object instance = cache.get(serviceType);
            if(instance ==null) {
                instance = providerType.getConstructor().newInstance();
                Log.info("Creating service " + serviceType.getCanonicalName()
                        + " with " + instance.getClass().getCanonicalName());
                cache.put(serviceType,instance);
            }

            return serviceType.cast(instance);
        } catch(Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException("No service available for " + serviceType.toString());
        }
    }
}
