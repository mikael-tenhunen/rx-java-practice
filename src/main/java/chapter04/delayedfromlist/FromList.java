package chapter04.delayedfromlist;

import java.util.concurrent.ThreadLocalRandom;

import rx.Observable;
import rx.schedulers.Schedulers;

public class FromList {

	
	public static void main(String[] args) throws InterruptedException {
		ListDao alfaList = new ListDao("alfa");
		ListDao betaList = new ListDao("beta");
		
		Observable<String> alfa = alfaList.getElements();
		Observable<String> beta = betaList.getElements();
		
		alfa
			.subscribeOn(Schedulers.io())
			.subscribe(System.out::println);
		beta
			.subscribeOn(Schedulers.io())
			.subscribe(System.out::println);
		
		/* can not add elements
		alfaList.addElement(8);
		alfaList.addElement(9);
		alfaList.addElement(10);
		alfaList.addElement(11);
		*/
		
		Thread.sleep(10000);
		
		System.out.println("Done!");
	}
	
	static Observable<Integer> getInt() {
		return Observable.defer(() -> Observable.just(getRandomInt()));
	}
	
	static Observable<String> getString() {
		return Observable.defer(() -> Observable.just(getRandomString()));
	}
	
	private static int getRandomInt() {
		return ThreadLocalRandom.current().nextInt(0, Integer.MAX_VALUE);
	}
	
	private static String getRandomString() {
		return String.valueOf(getRandomInt());
	}
	

}
