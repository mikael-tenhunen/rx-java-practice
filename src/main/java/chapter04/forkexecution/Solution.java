package chapter04.forkexecution;

import static util.Log.log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import rx.Observable;
import rx.schedulers.Schedulers;

public class Solution {
	public static Observable<Integer> deferredCalculation(int seed) {
		return Observable.fromCallable(() -> SlowProducer.calculate(seed));
	}

	/*
	 * poolA: onSubscribe poolB: mellanresultat poolC: onEvent
	 */
	public static void main(String[] args) {
		ExecutorService poolA = Executors.newFixedThreadPool(4);
		ExecutorService poolB = Executors.newFixedThreadPool(4);
		ExecutorService poolC = Executors.newFixedThreadPool(4);

		Observable<Integer> seeds = Observable.just(1, 2, 3, 4);
		seeds.flatMap(seed -> {
					Observable<Integer> asyncCalc = Observable.fromCallable(() -> SlowProducer.calculate(seed));
					return asyncCalc.subscribeOn(Schedulers.from(poolA));
					})
				.observeOn(Schedulers.from(poolB))
				.doOnNext(result -> log("intermediate result=" + result))
				.map(result -> result + "FLÖRP")
				.observeOn(Schedulers.from(poolC))
				.subscribe(
						x -> log("Got:" + x), 
						e -> e.printStackTrace(), 
						() -> log("Completed"));
	}
}
