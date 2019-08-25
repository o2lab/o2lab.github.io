import java.lang.Thread.State;
import java.util.*;
public class HelloThread1 {
	private List list = new LinkedList();
	public static void main(String[] args) {
		 final HelloThread1 en = new HelloThread1();

		 Thread t1 = new Thread(new Runnable() {
				 public void run() {
					 try {
						  en.removeItem();
					 } catch ( InterruptedException ix ) {
					 } catch ( Exception x ) {
					 }
				 }
			 });
			 t1.start();
			 Thread t2 = new Thread(new Runnable() {
				 public void run() {
						 en.addItem("Hello!");
				 }
			 });
			 t2.start();
			 try {
				 en.clearItem();
				 Thread.sleep(100);
				 if(t1.getState()==State.WAITING)
					 System.err.println("Deadlock!");
				 System.exit(0);
			} catch (InterruptedException e) {}
	}
	public  synchronized void clearItem(){
		list.clear();
	}

	 public String removeItem() throws InterruptedException {
		 synchronized ( list ) {
			 if ( list.isEmpty() ) { //if clause is risky
				 list.wait();
			 }
			 //delete an element
			 String item = (String) list.remove(0);
			 return item;
		 }
	 }

	 public void addItem(String item) {
		 synchronized ( list ) {
		 //add an element
			 list.add(item);
			 if(!list.isEmpty()){
				 //notify waiting thread
				 list.notify();
			 }
		 }
	 }
}