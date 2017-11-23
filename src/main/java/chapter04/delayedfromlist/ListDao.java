package chapter04.delayedfromlist;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;

public class ListDao {
	private final String prefix;
	private final List<Integer> ints = new LinkedList<>();
	
	public ListDao(String prefix) {
		this.prefix = prefix;
		ints.addAll(Arrays.asList(1,2,3,4));
	}
	
	public void addElement(int i) {
		ints.add(i);
	}
	
	public Observable<String> getElements() {
		return Observable.zip(Observable.interval(1, TimeUnit.SECONDS).take(ints.size()),
				Observable.from(ints), (delay, element) -> element)
			.map(i -> prefix + i);
//		return Observable.interval(1, TimeUnit.SECONDS).map(aLong -> prefix + aLong);
	}
}
