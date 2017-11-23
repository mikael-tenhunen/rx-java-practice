package chapter04.pushaftercreation;

import rx.subjects.PublishSubject;

public class PushItemsAfterCreation {
	
	public static void main(String[] args) {
		PublishSubject<String> subject = PublishSubject.create();

		
		subject.subscribe(s -> System.out.println(s));
		
		subject.onNext("s1");
		subject.onNext("s2");
		subject.onNext("s3");
		subject.onNext("s4");
	}
}
