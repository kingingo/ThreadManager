package dev.wolveringer.thread;

public abstract class ThreadFactory {
	private static ThreadFactory instance;

	public static ThreadFactory getInstance() {
		return instance;
	}

	public static void setInstance(ThreadFactory instance) {
		ThreadFactory.instance = instance;
	}

	static {
		instance = new ThreadFactory() {
			@Override
			public ThreadRunner createThread(final Runnable run) {
					return new ThreadRunner() {
						Thread t;
						@Override
						public void start() {
							if(t != null)
								throw new IllegalStateException("Thread is alredy running!");
							t = new Thread(run);
							t.start();
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

	public abstract ThreadRunner createThread(Runnable run);
}
