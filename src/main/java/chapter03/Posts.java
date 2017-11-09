package chapter03;

import static rx.Observable.just;

import rx.Observable;

public class Posts {

	public static void main(String[] args) {
		Observable<Post> postsandreplies = Observable.just(
				new Post(Observable.just(new Post("r1"), new Post("r2")), "p1"),
				new Post(Observable.just(new Post("r3"), new Post("r4")), "p2")
				);

		/**
		 * Posts have observables of other posts as replies. Merge one level of replies with posts.
		 */
		Observable<Observable<Post>> map = 
				postsandreplies.map(post -> Observable.just(post).mergeWith(post.replies));
		map.subscribe(o -> o.subscribe(op -> System.out.println(op)));

	}


	static class Post {

		String msg;

		@Override
		public String toString() {
			return "Message [msg=" + msg + "]";
		}

		Observable<Post> replies;

		public Post(Observable<Post> replies, String msg) {
			this.replies = replies;
			this.msg = msg;
		}

		public Post(String msg) {
			this.replies = Observable.empty();
			this.msg = msg;
		}
	}

}
