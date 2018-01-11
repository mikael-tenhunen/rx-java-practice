package chapter06.flowcontrol;

import java.util.concurrent.LinkedBlockingQueue;

import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.schedulers.Schedulers;
import rx.Subscriber;
import rx.observables.SyncOnSubscribe;

public class Backpressure {
	
	@SuppressWarnings("deprecation")
	public static Observable<Integer> myRange(int from, int count) {

		OnSubscribe onSubscribe = SyncOnSubscribe.createStateless(
					observer -> {
						observer.onNext(1);
					}
				);
		
		return Observable.create(onSubscribe);
		
//		return Observable.create(subscriber -> {
//			int i = from;
//			while (i < from + count) {
//				if (!subscriber.isUnsubscribed()) {
//					subscriber.onNext(i++);
//				} else {
//					return;
//				}
//			}
//			subscriber.onCompleted();
//		});
	}
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws InterruptedException {
		myRange(1, 1000_000)
//			.doOnNext(n -> System.out.println("before backpressure: " + n))
//			.onBackpressureBuffer()
			.observeOn(Schedulers.io())
			.subscribe(
					new Subscriber() {
						public void onStart() {
//							request(1);						
							}
						
						public void onCompleted() {
						}

						public void onError(Throwable e) {
							System.out.println("Error " + e);
						}

						public void onNext(Object t) {
							// TODO Auto-generated method stub
							System.out.println(Thread.currentThread().getName());
							try {
								Thread.sleep(1000);
								System.out.println("onnext" + t);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
//							request(1);
						}
					}
			);
		
		
		Thread.sleep(10000);
//		LinkedBlockingQueue<Integer> boundedQueue = new LinkedBlockingQueue<>(10);
		
//		myRange(1, 100)
//			.onBackpressureBuffer()
//			.doOnNext(n -> System.out.println(n + " emitted"))
//			.observeOn(Schedulers.io())
//			.subscribe(new Subscriber<Integer>() {
//
//					@Override
//					public void onStart() {
//						request(1);
//					}
//				
//					@Override
//					public void onCompleted() {
//						System.out.println("Completed!");
//					}
//	
//					@Override
//					public void onError(Throwable e) {
//						System.out.println("Error: " + e);
//					}
//	
//					@Override
//					public void onNext(Integer n) {
//						while(boundedQueue.remainingCapacity() == 0) {
//							try {
//								Thread.sleep(1000);
//							} catch (InterruptedException e) {
//								e.printStackTrace();
//							}
//						}
//						boundedQueue.add(n);
//						System.out.println(n + " added, boundedQueue=" + boundedQueue);
//						request(1);
//					}
//				});
	}
}
