package org.alexdev.battlectf.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class BattleAttributable {
    private Map<String, Object> attributeMap;

    public BattleAttributable() {
        this.attributeMap = new ConcurrentHashMap<>();
    }

    public void setAttribute(String attributeKey, Object attributeValue) {
        this.attributeMap.put(attributeKey, attributeValue);
    }

    public Object getAttribute(String attributeKey) {
        if (!this.attributeMap.containsKey(attributeKey)) {
            return null;
        }

        return this.attributeMap.get(attributeKey);
    }

    public <T> T resolve(String attributeKey, Class<T> type) {
        if (!this.attributeMap.containsKey(attributeKey)) {
            return null;
        }

        Object member = this.attributeMap.get(attributeKey);
        return type.cast(member);
    }

    public boolean hasAttribute(String attributeKey) {
        return this.attributeMap.containsKey(attributeKey);
    }

    public void removeAttribute(String attributeKey) {
        this.attributeMap.remove(attributeKey);
    }
}