package be.jeroenbellen.cache;

import net.rubyeye.xmemcached.XMemcachedClient;
import net.rubyeye.xmemcached.exception.MemcachedException;

import java.util.concurrent.TimeoutException;

/**
 * User: jeroen
 * Date: 12/8/11
 * Time: 9:28 PM
 */
// TODO Do something with the exceptions
public class MemcachedCache implements ICache {
    private XMemcachedClient xMemcachedClient;

    public MemcachedCache(XMemcachedClient xMemcachedClient) {
        this.xMemcachedClient = xMemcachedClient;
    }

    public Object get(String key) {
        try {
            return this.xMemcachedClient.get(key);
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (MemcachedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void put(String key, Object data) {
        try {
            this.xMemcachedClient.set(key, 0, data);
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (MemcachedException e) {
            e.printStackTrace();
        }
    }

    public boolean containsKey(String key) {
        try {
            return this.xMemcachedClient.get(key) != null;
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (MemcachedException e) {
            e.printStackTrace();
        }
        return false;
    }
}
