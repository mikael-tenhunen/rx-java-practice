package chapter01;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rx.Observable;
import rx.Subscriber;

public class SimpleObservable {

	private Logger log = LoggerFactory.getLogger(getClass());
	
	public Observable<Integer> intObserver() {
		// Deprecated way of creating Observable with OnSubscribe
		Observable<Integer> ints = Observable.create(new Observable.OnSubscribe<Integer>() {
			public void call(Subscriber<? super Integer> subscriber) {
				// TODO Auto-generated method stub
				log.info("Create");
				subscriber.onNext(1);
				subscriber.onNext(2);
				subscriber.onNext(3);
				subscriber.onCompleted();
				log.info("completed");
			}
		});
		return ints;
	}
	
	public static void main(String[] args) {
		Observable<Integer> intObserver = new SimpleObservable().intObserver();
		intObserver.subscribe(event -> System.out.println("event1: " + event));
		
		Observable.create(subscriber -> {
			subscriber.onNext(1);
			subscriber.onNext(2);
			subscriber.onNext(3);
			subscriber.onCompleted();
		}).subscribe(event -> System.out.println("event2: " + event));
	}

}
