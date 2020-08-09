package org.example.reactorproject;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.core.publisher.MonoSink;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @Description:
 * @Author: chenkangqiang
 * @Date: 2020/8/5
 */
@Slf4j
public class MonoSchedulerDemo {

    private static final ExecutorService EXECUTOR_SERVICE = Executors.newCachedThreadPool();
    private static final Scheduler SCHEDULER = Schedulers.fromExecutor(EXECUTOR_SERVICE);

    public static void main(String[] args) throws InterruptedException {

        Mono.just(10)
                .map(new Function<Integer, Integer>() {
                    @Override
                    public Integer apply(Integer integer) {
                        log.info("map1");
                        return integer + 10;
                    }
                })
                .publishOn(Schedulers.newParallel("myParallel"))
                .map(new Function<Integer, Integer>() {
                    @Override
                    public Integer apply(Integer integer) {
                        log.info("map2");
                        return integer + 10;
                    }
                })
                .subscribeOn(Schedulers.newElastic("myElastic"))
                .map(new Function<Integer, Integer>() {
                    @Override
                    public Integer apply(Integer integer) {
                        log.info("map3");
                        return integer + 10;
                    }
                })
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) {
                        log.info(String.valueOf(integer));
                    }
                });

        log.info("**************************");
        TimeUnit.SECONDS.sleep(4);
    }

    private static Mono<String> doAsyncRpcProcess() {
        Mono<String> mono = Mono.create(new Consumer<MonoSink<String>>() {
            @SneakyThrows
            @Override
            public void accept(MonoSink<String> stringMonoSink) {
                // 模拟RPC调用耗时
                log.info(Thread.currentThread().getName());
                TimeUnit.SECONDS.sleep(2);
                stringMonoSink.success("hello");
            }
        }).subscribeOn(SCHEDULER);
        return mono;
    }

}
