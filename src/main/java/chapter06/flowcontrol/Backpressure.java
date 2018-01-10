package chapter06.flowcontrol;

import java.util.concurrent.LinkedBlockingQueue;

import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.schedulers.Schedulers;
import rx.Subscriber;

public class Backpressure {
	
	public static Observable<Integer> myRange(int from, int count) {
		OnSubscribe<Integer> onSubscribeMyRange = subscriber -> {
			int i = from;
			while (i < from + count) {
				if (!subscriber.isUnsubscribed()) {
					subscriber.onNext(i++);
				} else {
					return;
				}
			}
			subscriber.onCompleted();
		};
		return Observable.unsafeCreate(onSubscribeMyRange);
	}
	
	public static void main(String[] args) {
//		myRange(1, 100)
//			.observeOn(Schedulers.io())
//			.subscribe(x -> {
//				System.out.println("handling " + x);
//				try {
//					Thread.sleep(1000);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			});
		
		LinkedBlockingQueue<Integer> boundedQueue = new LinkedBlockingQueue<>(10);
		
		myRange(1, 100)
			.onBackpressureBuffer()
			.doOnNext(n -> System.out.println(n + " emitted"))
			.observeOn(Schedulers.io())
			.subscribe(new Subscriber<Integer>() {

					@Override
					public void onStart() {
						request(1);
					}
				
					@Override
					public void onCompleted() {
						System.out.println("Completed!");
					}
	
					@Override
					public void onError(Throwable e) {
						System.out.println("Error: " + e);
					}
	
					@Override
					public void onNext(Integer n) {
						while(boundedQueue.remainingCapacity() == 0) {
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
						boundedQueue.add(n);
						System.out.println(n + " added, boundedQueue=" + boundedQueue);
						request(1);
					}
				});
	}
}
