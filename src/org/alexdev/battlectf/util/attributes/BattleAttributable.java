package org.alexdev.battlectf.util.attributes;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class BattleAttributable {
    private Map<BattleAttribute, Object> attributeMap;

    public BattleAttributable() {
        this.attributeMap = new ConcurrentHashMap<>();
    }

    public void set(BattleAttribute attributeKey, Object attributeValue) {
        this.attributeMap.put(attributeKey, attributeValue);
    }

    public <T> T get(BattleAttribute attributeKey) {
        if (!this.attributeMap.containsKey(attributeKey)) {
            return null;
        }

        return (T)this.attributeMap.get(attributeKey);
    }

    public <T> T getOrDefault(BattleAttribute attributeKey, Object object) {
        if (!this.attributeMap.containsKey(attributeKey)) {
            return object != null ? (T)object : null;
        }

        return (T)this.attributeMap.get(attributeKey);
    }

    public boolean has(BattleAttribute attributeKey) {
        return this.attributeMap.containsKey(attributeKey);
    }

    public void remove(BattleAttribute attributeKey) {
        this.attributeMap.remove(attributeKey);
    }
}