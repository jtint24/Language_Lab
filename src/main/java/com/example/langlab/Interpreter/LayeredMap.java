package com.example.langlab.Interpreter;

import com.example.langlab.Elements.Value;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LayeredMap<T, U> {
    ArrayList<HashMap<T, U>> layers = new ArrayList<>();
    boolean copyLayers;

    public LayeredMap(boolean copyLayers) {
        this.copyLayers = copyLayers;
    }

    public void addLayer() {
        layers.add(0,new HashMap<>());
    }
    public void removeLayer() {
        HashMap<T,U> poppedLayer = layers.remove(0);

        if (copyLayers) {
            HashMap<T, U> newLayer = layers.get(0);
            for (Map.Entry<T, U> entry : poppedLayer.entrySet()) {
                if (newLayer.containsKey(entry.getKey())) {
                    newLayer.put(entry.getKey(), entry.getValue());
                }
            }
        }
    }
    public U get(T key) {
        for (HashMap<T, U> layer : layers) {
            if (layer.containsKey(key)) {
                return layer.get(key);
            }
        }
        return null;
    }

    public void put(T key, U value) {
        layers.get(0).put(key, value);
    }

    public boolean contains(T key) {
        for (HashMap<T, U> layer : layers) {
            if (layer.containsKey(key)) {
                return true;
            }
        }
        return false;
    }

    public HashMap<T, U> toHashMap() {
        return toHashMap(layers.size());
    }

    public HashMap<T, U> toHashMap(int start) {
        HashMap<T, U> retMap = new HashMap<>();
        for (int i = 0; i<layers.size()-start; i++) {
            HashMap<T, U> layer = layers.get(i);
            retMap.putAll(layer);
        }
        return retMap;
    }
}
