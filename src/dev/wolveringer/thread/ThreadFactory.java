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
			@Override
			public ThreadRunner createThread(final Runnable run) {
					return new ThreadRunner() {
						Thread t;
						@Override
						public void start() {
							if(t != null)
								throw new IllegalStateException("Thread is alredy running!");
							t = EventLoop.UNLIMITED_LOOP.join(run);
						}
						public void stop() {
							if(t == null)
								throw new IllegalStateException("Thread isnt running!");
							t.interrupt();
							t = null;
						};
					};
			}
		};
	}

	public abstract void createAsync(Runnable run);

	public abstract void createAsync(Runnable run, EventLoop loop);
	
	public abstract ThreadRunner createThread(Runnable run);
}
