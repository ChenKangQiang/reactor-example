package org.example.reactorproject;

import lombok.SneakyThrows;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @Description:
 * @Author: chenkangqiang
 * @Date: 2020/7/30
 */
public class FluxDemo {

    private static final ExecutorService EXECUTOR_SERVICE = Executors.newCachedThreadPool();

    public static void main(String[] args) {
        // 使用just创建
        Flux<Integer> justFlux = Flux.just(1, 2, 3, 4, 5);
        justFlux.subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) {
                System.out.println("just " + integer);
            }
        });
        System.out.println("************************");

        // 使用create创建，这里使用异步线程中创建
        Flux<Integer> createFlux = Flux.create(new Consumer<FluxSink<Integer>>() {
            @Override
            public void accept(FluxSink<Integer> integerFluxSink) {
                CompletableFuture.supplyAsync(new Supplier<Integer>() {
                    @SneakyThrows
                    @Override
                    public Integer get() {
                        TimeUnit.SECONDS.sleep(2);
                        return 100;
                    }
                }, EXECUTOR_SERVICE).thenApply(new Function<Integer, Integer>() {
                    @Override
                    public Integer apply(Integer integer) {
                        integerFluxSink.next(integer);
                        integerFluxSink.complete();
                        return integer;
                    }
                });
            }
        });

        // 订阅触发
        createFlux.subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) {
                System.out.println("createFlux subscribe " + integer);
            }
        });

        System.out.println("************************");


        // 使用create创建，这里使用同步线程中创建
        Flux<Integer> createSyncFlux = Flux.create(new Consumer<FluxSink<Integer>>() {
            @SneakyThrows
            @Override
            public void accept(FluxSink<Integer> integerFluxSink) {
                TimeUnit.SECONDS.sleep(2);
                integerFluxSink.next(200);
                integerFluxSink.complete();
            }
        });

        createSyncFlux.subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) {
                System.out.println("createSyncFlux subscribe " + integer);
            }
        });

        System.out.println("************************");
    }

}
