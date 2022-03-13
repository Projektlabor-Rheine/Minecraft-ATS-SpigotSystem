package de.atsrheine.mcspigotplugin.util;

import java.util.function.Consumer;

public class FailableThread<T extends Exception> extends Thread{
	
	// Callbacks for the execution and the fail callback
	private FailCallback<T> onRun;
	private Consumer<T> onFail;
	
	public FailableThread(FailCallback<T> onRun, Consumer<T> onFail) {
		this.onRun = onRun;
		this.onFail = onFail;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		try {
			this.onRun.run();
		}catch(Exception exc) {
			if(this.onFail != null)
				this.onFail.accept((T)exc);
		}
	}

}
