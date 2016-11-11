package andy.test;

import java.util.concurrent.Exchanger;

/**
 * @author Andy<andy_513@163.com>
 */
public class SendAndReceiver {
	private final Exchanger<StringBuilder> exchanger = new Exchanger<StringBuilder>();

	private class Sender implements Runnable {
		public void run() {
			try {
				StringBuilder content = new StringBuilder("Hello");
				content = exchanger.exchange(content);
				System.out.println("1:\t" + content);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	}

	private class Receiver implements Runnable {
		public void run() {
			try {
				StringBuilder content = new StringBuilder("World");
				content = exchanger.exchange(content);
				System.out.println("2:\t" + content);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	}

	public void exchange() {
		new Thread(new Sender()).start();
		new Thread(new Receiver()).start();
	}
	
	public static void main(String[] args) {
		new SendAndReceiver().exchange();
	}
}
