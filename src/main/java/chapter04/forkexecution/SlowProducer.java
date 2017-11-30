package chapter04.forkexecution;

import static util.Log.log;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import rx.Observable;
import rx.schedulers.Schedulers;
import util.Log;

public class SlowProducer {
	
	public static Observable<Integer> calculateObs(int seed){
		return   Observable.fromCallable(() -> calculate(seed));
	}
	
	public static int calculate(int seed) {
//		Random random = new Random(seed);
//		int sleepTime = random.nextInt(4000);
		int sleepTime = 4000;
		try {
			Thread.sleep(sleepTime);
			log("sleepTime=" + sleepTime);
			return sleepTime;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	/*
	 * poolA: onSubscribe 
	 * poolB: intermediate result 
	 * poolC: onEvent (FLÃ–RP)
	 * 
	 * Få alla beräkningar att köras samtidigt
	 * 
	 */
	public static void main(String[] args) {
		ExecutorService poolA = Executors.newFixedThreadPool(4);
		ExecutorService poolB = Executors.newFixedThreadPool(4);
		ExecutorService poolC = Executors.newFixedThreadPool(4);

		long tid = System.currentTimeMillis();
		
		Observable.just(1, 2, 3, 4)
		.flatMap(seed -> SlowProducer.calculateObs(seed)
				.subscribeOn(Schedulers.from(poolA)))
		.observeOn(Schedulers.from(poolB))
		.doOnNext(result -> log("intermediate result=" + result))
		.reduce((n,i) -> n + i)
		.observeOn(Schedulers.from(poolC))
		.subscribe(
				x -> log("Got:" + x), 
				e -> e.printStackTrace(), 
				() -> log(("total: " + (System.currentTimeMillis() - tid))));
		
	}
}
