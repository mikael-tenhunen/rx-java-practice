package chapter04.forkexecution;

import static util.Log.log;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import rx.Observable;
import rx.schedulers.Schedulers;
import util.Log;

public class SlowProducer {
	Random random = new Random();

	public static int calculate(int seed) {
		Random random = new Random(seed);
		int sleepTime = random.nextInt(4000);
		try {
			Thread.sleep(random.nextInt(4000));
			log("sleepTime=" + sleepTime);
			return sleepTime;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	public static Observable<Integer> deferredCalculation(int seed) {
		return Observable.fromCallable(() -> calculate(seed));
	}

	/*
	 * poolA: onSubscribe 
	 * poolB: intermediate result 
	 * poolC: onEvent (FLÖRP)
	 * 
	 * Få alla beräkningar att köras samtidigt
	 * 
	 */
	public static void main(String[] args) {
		ExecutorService poolA = Executors.newFixedThreadPool(4);
		ExecutorService poolB = Executors.newFixedThreadPool(4);
		ExecutorService poolC = Executors.newFixedThreadPool(4);

		
		Observable.just(1, 2, 3, 4)
		.map(seed -> SlowProducer.calculate(seed))
		.doOnNext(result -> log("intermediate result=" + result))
		.map(result -> result + "FLÖRP")
		.subscribe(
				x -> log("Got:" + x), 
				e -> e.printStackTrace(), 
				() -> log("Completed"));
		
	}
}
