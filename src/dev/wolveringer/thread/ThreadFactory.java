package dev.wolveringer.thread;

public abstract class ThreadFactory {
	public static ThreadFactory THREAD_FACTORY;

	static {
		THREAD_FACTORY = new ThreadFactory() {
			@Override
			public void createAsync(Runnable run, EventLoop loop) {
				loop.join(run);
			}

			@Override
			public void createAsync(Runnable run) {
				createAsync(run, EventLoop.UNLIMITED_LOOP);
			}
		};
	}

	public abstract void createAsync(Runnable run);

	public abstract void createAsync(Runnable run, EventLoop loop);
}
