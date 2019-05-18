package org.alexdev.battlectf.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class BattleAttributable {
    private Map<BattleAttribute, Object> attributeMap;

    public BattleAttributable() {
        this.attributeMap = new ConcurrentHashMap<>();
    }

    public void setAttribute(BattleAttribute attributeKey, Object attributeValue) {
        this.attributeMap.put(attributeKey, attributeValue);
    }

    public Object getAttribute(BattleAttribute attributeKey) {
        if (!this.attributeMap.containsKey(attributeKey)) {
            return null;
        }

        return this.attributeMap.get(attributeKey);
    }

    public <T> T resolve(BattleAttribute attributeKey, Class<T> type) {
        if (!this.attributeMap.containsKey(attributeKey)) {
            return null;
        }

        Object member = this.attributeMap.get(attributeKey);
        return type.cast(member);
    }

    public boolean hasAttribute(BattleAttribute attributeKey) {
        return this.attributeMap.containsKey(attributeKey);
    }

    public void removeAttribute(BattleAttribute attributeKey) {
        this.attributeMap.remove(attributeKey);
    }
}