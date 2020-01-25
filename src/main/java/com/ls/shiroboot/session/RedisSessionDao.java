package com.ls.shiroboot.session;

import com.ls.shiroboot.utils.JedisUtil;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.springframework.util.CollectionUtils;
import org.springframework.util.SerializationUtils;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class RedisSessionDao extends AbstractSessionDAO {

    @Resource
    private JedisUtil jedisUtil;

    private final static String SHIRO_SESSION_PREFIX = "imooc_session_";
    private final static Integer SHIRO_SESSION_EXPIRE = 3000;

    private String getKey(String key){
        return SHIRO_SESSION_PREFIX + key;
    }

    private void saveSession(Session session){
        if (session != null && session.getId() != null) {
            String key = getKey(session.getId().toString());
            byte[] value = SerializationUtils.serialize(session);
            jedisUtil.set(key, value, SHIRO_SESSION_EXPIRE);
        }
    }

    @Override
    protected Serializable doCreate(Session session) {
        Serializable sessionId = generateSessionId(session);
        assignSessionId(session, sessionId);
        saveSession(session);
        return sessionId;
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {
        if (sessionId == null) {
            return null;
        }
        String key = getKey(sessionId.toString());
        byte[] value = (byte[]) jedisUtil.get(key);
        return (Session) SerializationUtils.deserialize(value);
    }

    @Override
    public void update(Session session) throws UnknownSessionException {
        saveSession(session);
    }

    @Override
    public void delete(Session session) {
        if (session != null && session.getId() != null) {
            return;
        }
        String key = getKey(session.getId().toString());
        jedisUtil.del(key);
    }

    @Override
    public Collection<Session> getActiveSessions() {
        Set<String> keys = (Set) jedisUtil.get(SHIRO_SESSION_PREFIX);
        Set<Session> sessions = new HashSet<>(keys.size());
        if (CollectionUtils.isEmpty(keys)) {
            return sessions;
        }
        for (String key:keys) {
            Session session = (Session) SerializationUtils.deserialize(key.getBytes());
            sessions.add(session);
        }
        return sessions;
    }
}
