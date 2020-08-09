package org.example.completablefuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @Description:
 * @Author: chenkangqiang
 * @Date: 2020/8/5
 */
public class CompletableFutureTest {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> futureA = callServiceA(100);

        CompletableFuture<Integer> futureD = futureA.thenCompose(new Function<Integer, CompletionStage<Integer>>() {
            @Override
            public CompletionStage<Integer> apply(Integer integer) {
                return callServiceB(integer);
            }
        }).thenCompose(new Function<Integer, CompletionStage<Integer>>() {
            @Override
            public CompletionStage<Integer> apply(Integer integer) {
                return callServiceD(integer);
            }
        });

        CompletableFuture<Integer> futureE = futureA.thenCompose(new Function<Integer, CompletionStage<Integer>>() {
            @Override
            public CompletionStage<Integer> apply(Integer integer) {
                return callServiceC(integer);
            }
        }).thenCompose(new Function<Integer, CompletionStage<Integer>>() {
            @Override
            public CompletionStage<Integer> apply(Integer integer) {
                return callServiceE(integer);
            }
        });

        CompletableFuture<Integer> futureF = futureE.thenCombine(futureD, new BiFunction<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer integer, Integer integer2) {
                return integer + integer2;
            }
        });


        CompletableFuture<Integer> futureK = callServiceG(100).thenCompose(new Function<Integer, CompletionStage<Integer>>() {
            @Override
            public CompletionStage<Integer> apply(Integer integer) {
                return callServiceH(integer);
            }
        }).thenCompose(new Function<Integer, CompletionStage<Integer>>() {
            @Override
            public CompletionStage<Integer> apply(Integer integer) {
                return callServiceK(integer);
            }
        });

        CompletableFuture<Integer> futureP = futureK.thenCombine(futureF, new BiFunction<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer integer, Integer integer2) {
                return integer + integer2;
            }
        });


        Integer result = futureP.get();
    }



    private static CompletableFuture<Integer> callServiceA(Integer input) {
        return CompletableFuture.completedFuture(++input);
    }


    private static CompletableFuture<Integer> callServiceB(Integer input) {
        return CompletableFuture.completedFuture(++input);
    }

    private static CompletableFuture<Integer> callServiceC(Integer input) {
        return CompletableFuture.completedFuture(++input);
    }

    private static CompletableFuture<Integer> callServiceD(Integer input) {
        return CompletableFuture.completedFuture(++input);
    }

    private static CompletableFuture<Integer> callServiceE(Integer input) {
        return CompletableFuture.completedFuture(++input);
    }

    private static CompletableFuture<Integer> callServiceG(Integer input) {
        return CompletableFuture.completedFuture(++input);
    }

    private static CompletableFuture<Integer> callServiceH(Integer input) {
        return CompletableFuture.completedFuture(++input);
    }

    private static CompletableFuture<Integer> callServiceK(Integer input) {
        return CompletableFuture.completedFuture(++input);
    }

}
