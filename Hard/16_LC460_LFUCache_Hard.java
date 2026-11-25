import java.util.*;

class LFUCache {

    private int capacity;
    private int minFreq;

    private Map<Integer, Integer> keyToValue;

    private Map<Integer, Integer> keyToFreq;

    private Map<Integer, LinkedHashSet<Integer>> freqToKeys;

    public LFUCache(int capacity) {
        this.capacity = capacity;
        this.minFreq = 0;

        keyToValue = new HashMap<>();
        keyToFreq = new HashMap<>();
        freqToKeys = new HashMap<>();
    }

    public int get(int key) {

        if (!keyToValue.containsKey(key)) {
            return -1;
        }

        increaseFrequency(key);

        return keyToValue.get(key);
    }

    public void put(int key, int value) {

        if (capacity == 0) {
            return;
        }

        if (keyToValue.containsKey(key)) {
            keyToValue.put(key, value);
            increaseFrequency(key);
            return;
        }

        if (keyToValue.size() == capacity) {

            LinkedHashSet<Integer> keys = freqToKeys.get(minFreq);

            int keyToRemove = keys.iterator().next();

            keys.remove(keyToRemove);

            keyToValue.remove(keyToRemove);
            keyToFreq.remove(keyToRemove);
        }

        keyToValue.put(key, value);
        keyToFreq.put(key, 1);

        freqToKeys
            .computeIfAbsent(1, k -> new LinkedHashSet<>())
            .add(key);

        minFreq = 1;
    }

    private void increaseFrequency(int key) {

        int freq = keyToFreq.get(key);

        LinkedHashSet<Integer> oldKeys = freqToKeys.get(freq);
        oldKeys.remove(key);

        if (freq == minFreq && oldKeys.isEmpty()) {
            minFreq++;
        }

        int newFreq = freq + 1;
        keyToFreq.put(key, newFreq);

        freqToKeys
            .computeIfAbsent(newFreq, k -> new LinkedHashSet<>())
            .add(key);
    }
}