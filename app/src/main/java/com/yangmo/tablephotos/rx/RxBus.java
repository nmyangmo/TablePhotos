package com.yangmo.tablephotos.rx;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;


public class RxBus {
    private final Subject<Object> rxBus = PublishSubject.create().toSerialized();

    private RxBus() {

    }

    public static RxBus getInstance() {
        return RxbusHolder.instance;
    }

    public static class RxbusHolder {
        private static final RxBus instance = new RxBus();
    }

    public void send(Object object) {
        rxBus.onNext(object);
    }

    public <T> Observable<T> toObserverable(Class<T> eventType) {
        return rxBus.ofType(eventType);
    }
}
