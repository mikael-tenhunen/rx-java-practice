package chapter01;

import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Subscriber;
import rx.internal.operators.OnSubscribeAutoConnect;

public class SimpleObservable {

	public static void main(String[] args) {
		Observable.create(new Observable.OnSubscribe<String>() {
			public void call(Subscriber<? super String> t) {
				// TODO Auto-generated method stub
				
			}
		});
	}

}
