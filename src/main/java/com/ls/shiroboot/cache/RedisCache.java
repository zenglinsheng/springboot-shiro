package com.ls.shiroboot.cache;

import com.ls.shiroboot.utils.JedisUtil;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Set;

@Component
public class RedisCache<K, V> implements Cache<K, V> {

    @Resource
    private JedisUtil jedisUtil;

    private final static String CACHE_PREFIX = "imooc_cache:";
    private final static Integer SHIRO_SESSION_EXPIRE = 3000;

    private String getKey(K k){
        return (CACHE_PREFIX + k);
    }

    @Override
    public V get(K k) throws CacheException {
        System.out.println("从redis中获取授权数据");
        byte[] value = (byte[]) jedisUtil.get(getKey(k));
        if (value != null) {
            return (V) SerializationUtils.deserialize(value);
        }
        return null;
    }

    @Override
    public V put(K k, V v) throws CacheException {
        String key = getKey(k);
        byte[] value = SerializationUtils.serialize(v);
        jedisUtil.set(key, value, SHIRO_SESSION_EXPIRE);
        return v;
    }

    @Override
    public V remove(K k) throws CacheException {
        String key = getKey(k);
        byte[] value = (byte[]) jedisUtil.get(key.toString());
        jedisUtil.del(key);
        if (value != null) {
            return (V) SerializationUtils.deserialize(value);
        }
        return null;
    }

    @Override
    public void clear() throws CacheException {

    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public Set<K> keys() {
        return null;
    }

    @Override
    public Collection<V> values() {
        return null;
    }
}
