package dev.wolveringer.thread;

public interface ThreadRunner {
	public void start();
	public void stop();
	public Thread getThread();
}
