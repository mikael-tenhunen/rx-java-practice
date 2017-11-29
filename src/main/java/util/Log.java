package util;

public class Log {
	public static void log(String s) {
		System.out.println("Thread=" + Thread.currentThread().getName() + " - " + s);
	}
}
