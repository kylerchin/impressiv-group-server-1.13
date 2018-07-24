package net.minecraft.server;

import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Function;

public class StatisticWrapper<T> implements Iterable<Statistic<T>> {

    private final RegistryMaterials<MinecraftKey, T> a;
    private final Map<T, Statistic<T>> b = new IdentityHashMap();

    public StatisticWrapper(RegistryMaterials<MinecraftKey, T> registrymaterials) {
        this.a = registrymaterials;
    }

    public Statistic<T> a(T t0, Counter counter) {
        return (Statistic) this.b.computeIfAbsent(t0, (object) -> {
            return new Statistic(this, object, counter);
        });
    }

    public RegistryMaterials<MinecraftKey, T> a() {
        return this.a;
    }

    public Iterator<Statistic<T>> iterator() {
        return this.b.values().iterator();
    }

    public Statistic<T> b(T t0) {
        return this.a(t0, Counter.DEFAULT);
    }
}
