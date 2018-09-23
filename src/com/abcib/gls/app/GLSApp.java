/**
 * 
 */
package com.abcib.gls.app;

import java.util.Timer;
import java.util.TimerTask;

import com.abcib.gls.intf.feed.notify.InFeedNotifier;

/**
 * @author santosh.barada
 *
 */
public class GLSApp {

	/**
	 * @param args
	 */
	public static void main(String args[]){
    	TimerTask repeatedTask = new TimerTask() {
			@Override
			public void run() {
				InFeedNotifier.runTask();
			}
        };
        Timer timer = new Timer("Timer");
         
        long delay  = 0L;
        long period = 1000L;
        timer.scheduleAtFixedRate(repeatedTask, delay, period);
	}

}
