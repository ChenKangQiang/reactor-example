package org.example.reactorproject;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.core.publisher.MonoSink;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import reactor.util.function.Tuple2;

import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @Description:
 * @Author: chenkangqiang
 * @Date: 2020/7/31
 */
@Slf4j
public class MonoDemo {

    private static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(20);
    private static final Scheduler SCHEDULER = Schedulers.fromExecutor(EXECUTOR_SERVICE);

    public static void main(String[] args) throws InterruptedException {

        log.info("start");

//        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(new Supplier<Integer>() {
//            @SneakyThrows
//            @Override
//            public Integer get() {
//                TimeUnit.SECONDS.sleep(1);
//                return 100;
//            }
//        }).thenCompose(new Function<Integer, CompletionStage<String>>() {
//            @Override
//            public CompletionStage<String> apply(Integer integer) {
//                return CompletableFuture.supplyAsync(new Supplier<String>() {
//                    @SneakyThrows
//                    @Override
//                    public String get() {
//                        TimeUnit.SECONDS.sleep(2);
//                        String result = integer + " result";
//                        return result;
//                    }
//                });
//            }
//        });
//
//        Mono<String> mono1 = Mono.fromFuture(future1);
//
//        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(new Supplier<Integer>() {
//            @SneakyThrows
//            @Override
//            public Integer get() {
//                TimeUnit.SECONDS.sleep(1);
//                return 200;
//            }
//        }).thenCompose(new Function<Integer, CompletionStage<String>>() {
//            @Override
//            public CompletionStage<String> apply(Integer integer) {
//                return CompletableFuture.supplyAsync(new Supplier<String>() {
//                    @SneakyThrows
//                    @Override
//                    public String get() {
//                        TimeUnit.SECONDS.sleep(3);
//                        String result = integer + " result";
//                        return result;
//                    }
//                });
//            }
//        });
//
//        Mono<String> mono2 = Mono.fromFuture(future2);
//
//        Mono<String> zip = mono1.zipWith(mono2).map(new Function<Tuple2<String, String>, String>() {
//            @Override
//            public String apply(Tuple2<String, String> objects) {
//                return objects.getT1() + "|" + objects.getT2();
//            }
//        });
//
//        zip.subscribe(new Consumer<String>() {
//            @Override
//            public void accept(String s) {
//                log.info(s);
//            }
//        });

        MonoDemo monoDemo = new MonoDemo();
        Mono<String> mono = monoDemo.doAsyncRpcProcessFromFuture();
        mono.subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) {
                log.info("subscribe : " + s);
            }
        });


        log.info("end");
        TimeUnit.SECONDS.sleep(10);
    }


    private Mono<String> doAsyncRpcProcess() {
        Mono<String> mono = Mono.create(new Consumer<MonoSink<String>>() {
            @SneakyThrows
            @Override
            public void accept(MonoSink<String> stringMonoSink) {
                // 模拟RPC调用耗时
                TimeUnit.SECONDS.sleep(2);
                stringMonoSink.success("hello");
            }
        }).subscribeOn(SCHEDULER);
        return mono;
    }

    private Mono<String> doAsyncRpcProcessFromFuture() {
        // 异步RPC调用返回的Future
        CompletableFuture<String> future = CompletableFuture.supplyAsync(new Supplier<String>() {
            @SneakyThrows
            @Override
            public String get() {
                TimeUnit.SECONDS.sleep(4);
                log.info("Task Complete");
                return "hello";
            }
        }, EXECUTOR_SERVICE);
        Mono<String> mono = Mono.fromFuture(future);
        return mono;
    }
}
