
/**
 * Description: 
 * This is assignment project of Software Architecture and Design Course
 * The purpose of this project is to build a web crawler to get information from
 * some specific sites. In this case, crawler will get information about drug 
 * from www.drugs.com
*/

/**
 * Created at: 10:20:35 AM  Jul 15, 2013
 * @author TanNhat
 * Project: wcrawler
 */
package wcrawler.core;

import java.util.ArrayList;
import java.util.concurrent.*;



public class MultiThreadManager {
    private enum State{Ready, Pausing, Running};
    private State state;
    private ExecutorService executor;
    private ArrayList<Runnable> taskToRun;

    public MultiThreadManager(int maxThread) {
        executor = Executors.newFixedThreadPool(maxThread);
        state = State.Ready;
    }  
    
    synchronized public void start(){
        //execute all task in queue
        for(Runnable task:taskToRun){
            executor.execute(task);
        }
        
        //clear queue
        taskToRun.clear();
        
        state = State.Running;
    }
    
    synchronized public void pause(){
        state = State.Pausing;
    }
    
    synchronized public void resume(){
        start();
    }
    
    synchronized public void stop(){
        //stop executor
        executor.shutdown();
        
        //wait for all task to finish
        while(!executor.isTerminated()){};
        
        //clear the queue
        taskToRun.clear();
        
        state = State.Ready;
    }
    
    synchronized public void addTask(Runnable task){
        //if in running state, execute task immediately,
        //otherwise push it to queue
        if(state == State.Running){
            executor.execute(task);
        }
        else{
            taskToRun.add(task);
        }
    }
}
