package org.example.rxjava;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;


import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * @Description:
 * @Author: chenkangqiang
 * @Date: 2020/7/31
 */
@Slf4j
public class ObservableDemo {

    private static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(20);
    private static final Scheduler SCHEDULER = Schedulers.from(EXECUTOR_SERVICE);

    public static void main(String[] args) throws InterruptedException {
//        // 创建被观察者，利用发射器创建一个被观察者, 被观察者是一个耗时的rpc同步调用，在主线程中
//        Observable<Integer> observable = doRpcProcess();
//        // 触发订阅，主线程阻塞，等待发射器发射数据，接收器处理数据
//        observable.subscribe(new Consumer<Integer>() {
//            @Override
//            public void accept(Integer integer) throws Exception {
//                log.info("subscribe observable: " + integer);
//            }
//        });
//
//        log.info("****************************1");
//
//        // 创建被观察者，被观察者是一个耗时的rpc调用，我们将被观察者调度在另一个线程里，那么观察者和被观察者会处于同一线程
//        Observable<Integer> asyncObservable = doAsyncRpcProcess();
//        // 触发订阅，订阅接收到数据后的处理流程在子线程中
//        asyncObservable.subscribe(new Consumer<Integer>() {
//            @Override
//            public void accept(Integer integer) throws Exception {
//                TimeUnit.SECONDS.sleep(15);
//                log.info("subscribe asyncObservable: " + integer);
//            }
//        });
//
//        log.info("****************************2");

//        // 创建被观察者，被观察者是一个耗时的rpc调用，并返回Future，并将观察者调度到另一个线程
//        Observable<Integer> futureObservable = doAsyncRpcProcessFromFuture().observeOn(SCHEDULER);
//        // 触发订阅，由于是Future转Observable，这里会阻塞等待发射器完成，observeOn只会影响接收到数据后的处理流程
//        futureObservable.subscribe(new Consumer<Integer>() {
//            @Override
//            public void accept(Integer integer) throws Exception {
//                TimeUnit.SECONDS.sleep(15);
//                log.info("subscribe futureObservable: " + integer);
//            }
//        });

//        log.info("****************************3");

        // 创建被观察者，被观察者是一个耗时的rpc调用，并返回Future
        Observable<Integer> futureObservableInMain = doAsyncRpcProcessFromFuture();
        // 订阅触发，主线程阻塞，类似Future.get()
        futureObservableInMain.subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {

            }
        });

        log.info("****************************4");

        log.info("****************************end");

        clear();
    }


    private static Observable<Integer> doRpcProcess() {
        Observable<Integer> syncObservable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> observableEmitter) throws Exception {
                TimeUnit.SECONDS.sleep(2);
                observableEmitter.onNext(400);
                observableEmitter.onComplete();
            }
        });
        return syncObservable;
    }

    private static Observable<Integer> doAsyncRpcProcess() {
        Observable<Integer> asyncObservable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> observableEmitter) throws Exception {
                // 模拟RPC耗时
                TimeUnit.SECONDS.sleep(2);
                observableEmitter.onNext(200);
                observableEmitter.onComplete();
            }
        }).subscribeOn(SCHEDULER);
        return asyncObservable;
    }

    private static Observable<Integer> doAsyncRpcProcessFromFuture() {
        // 异步RPC调用返回的Future
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(new Supplier<Integer>() {
            @SneakyThrows
            @Override
            public Integer get() {
                TimeUnit.SECONDS.sleep(4);
                log.info("Task Complete");
                return 500;
            }
        }, EXECUTOR_SERVICE);
//        Observable<Integer> asyncObservable = Observable.fromFuture(future);
        Observable<Integer> asyncObservable = Observable.fromCompletionStage(future);
        log.info("Get Future");
        return asyncObservable;
    }


    private static void clear() {
        SCHEDULER.shutdown();
        EXECUTOR_SERVICE.shutdown();
    }

}
