package io.github.lvyahui8.sdk.weak;

import io.github.lvyahui8.sdk.utils.AsyncTaskExecutor;

import javax.sound.sampled.Line;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.function.Supplier;

/**
 * @author feego lvyahui8@gmail.com
 * @date 2021/9/18
 */
public class WeakDependencyGroup {

    public List<CompletableFuture<?>> futures = new LinkedList<>();

    public class WeakDependencyItem<RES> {
        CompletableFuture<RES> future;
        OnException<RES> onException;
    }

    public List<WeakDependencyItem<?>> weakDependencyItems;

    public <RES> WeakDependencyGroup add(Supplier<RES> weakDependency, OnException<RES> onException) {
        WeakDependencyItem<RES> weakDependencyItem = new WeakDependencyItem<>();
        try {
            weakDependencyItem.future = CompletableFuture.supplyAsync(() -> {
                try {
                    return weakDependency.get();
                } catch (Exception ignored) {
                    return onException.apply();
                }
            }, AsyncTaskExecutor.getExecutor());
            futures.add(weakDependencyItem.future);
        } catch (Exception ignored) {
        }
        weakDependencyItem.onException = onException;
        return this;
    }

    public Map<Class<?>,Object> get(long timeoutMs) {
        if (timeoutMs > 0){
            CompletableFuture<Void> future = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
            try {
                future.get(timeoutMs, TimeUnit.MICROSECONDS);
            } catch (Exception ignored) {
            }
        }

        for (WeakDependencyItem<?> weakDependencyItem : weakDependencyItems) {
            Type retType = ((ParameterizedType) weakDependencyItem.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        }
        return null;
    }
}
