package org.example.rxjava;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.functions.Consumer;

/**
 * @Description:
 * @Author: chenkangqiang
 * @Date: 2020/8/5
 */
public class FlowableDemo {

    public static void main(String[] args) {
        Flowable<Integer> flowable = Flowable.just(100);
        flowable.subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {

            }
        });
    }
}
