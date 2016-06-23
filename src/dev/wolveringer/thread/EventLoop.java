package dev.wolveringer.thread;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EventLoop {
	public static final EventLoop DEFAULT_LOOP = new EventLoop(512);
	public static final EventLoop UNLIMITED_LOOP = new EventLoop(-1);
	
	private int MAX_THREADS = 0;
	private List<Runnable> todo = (List<Runnable>) Collections.synchronizedList(new ArrayList<Runnable>());
	private List<ThreadRunner> threads = (List<ThreadRunner>) Collections.synchronizedList(new ArrayList<ThreadRunner>());
	public EventLoop(int maxthreads) {
		this.MAX_THREADS = maxthreads;
	}
	
	public ThreadRunner join(final Runnable run) {
		if (MAX_THREADS <= 0 || threads.size() < MAX_THREADS || MAX_THREADS == -1) {
			ThreadRunner t = ThreadFactory.getInstance().createThread(new Runnable() {
				@Override
				public void run() {
					try {
						run.run();
					} catch (Exception e) {
						e.printStackTrace();
					}
					while (true) {
						Runnable next = null;
						synchronized (todo) {
							if (todo.size() != 0) {
								next = todo.get(0);
								todo.remove(next);
							} else
								break;
						}
						next.run();
					}
					synchronized (threads) {
						threads.remove(this);
					}
				}
			});
			t.start();
			threads.add(t);
			return t;
		} else
			synchronized (todo) {
				todo.add(run);
			}
		return null;
	}
	
	public int getCurruntThreads(){
		synchronized (threads) {
			return threads.size();
		}
	}
	public List<ThreadRunner> getWorkingThreads(){
		synchronized (threads) {
			return Collections.unmodifiableList(threads);
		}
	}
	
	public List<Runnable> getQueue() {
		synchronized (todo) {
			return Collections.unmodifiableList(todo);
		}
	}

	public void terminate() {
		synchronized (todo) {
			todo.clear();
		}
		synchronized (threads) {
			for(ThreadRunner t : new ArrayList<>(threads))
				try{
					t.stop();
				}catch (Exception e) {
					e.printStackTrace();
				}
		}
	}
}

