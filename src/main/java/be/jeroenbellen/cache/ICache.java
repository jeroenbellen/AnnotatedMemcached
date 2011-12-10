package be.jeroenbellen.cache;

/**
 * User: jeroen
 * Date: 12/8/11
 * Time: 9:12 PM
 */
public interface ICache {
    Object get(String key);
    void put(String key, Object data);
    void put(String key, Object data, int exp);
    boolean containsKey(String key);
}
