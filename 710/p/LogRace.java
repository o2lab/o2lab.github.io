import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public final class Log {

	static HashMap<String,ArrayList<Event>> addrToEvents = new HashMap<String,ArrayList<Event>>();
	static HashMap<Long,HashSet<Integer>> threadToLocks = new HashMap<Long,HashSet<Integer>>();

	public static class Event {
		long tid;
		boolean isWrite;
		HashSet<Integer> locks = new HashSet<Integer>();
		String addr;
		String sig;
		public Event(long tid2, String addr2, boolean isWrite2, String sig2, HashSet<Integer> locks2) {
			this.tid = tid2;
			this.addr = addr2;
			this.isWrite = isWrite2;
			this.sig = sig2;
			if(locks2!=null)
			this.locks.addAll(locks2);
		}
	}
	private static synchronized void detectRace(String addr, boolean isWrite, String sig){
		long tid = Thread.currentThread().getId();
		HashSet<Integer> locks = threadToLocks.get(tid);
		Event e =  new Event(tid,addr,isWrite,sig,locks);

		ArrayList<Event> events = addrToEvents.get(addr);
		if(events==null){
			events = new ArrayList<Event>();
			addrToEvents.put(addr, events);
		}
		else{
			for(Event e2:events){
				if(tid!=e2.tid){
					if(isWrite||e2.isWrite){

						boolean isRace = false;
						//1. lockset
						if(locks==null||e2.locks==null)
							isRace = true;
						else {
							HashSet<Integer> checklocks = new HashSet<Integer>();
							checklocks.retainAll(e2.locks);
							if(checklocks.isEmpty())
								isRace = true;
						}
						//2. happens-before

							if(isRace)
								System.err.println("Data race detected: \n"
								+ sig+"\n"+e2.sig);
					}
				}
			}
		}
		events.add(e);
	}
	public static void logFieldAcc(final Object o, String name, final boolean isStatic, final boolean isWrite)
	{
		String addr = o==null?name:o+name;
		String sig = "Thread "+Thread.currentThread().getName()+(isWrite?" wrote":" read");
		if(isStatic){
			sig += " static field "+name;
			System.out.println(sig);
		}
		else{
			sig+=" instance field "+name+" of object "+o;
			System.out.println(sig);
		}

		detectRace(addr,isWrite,sig);
	}

	public static void logFieldAcc(final Object o, String name, final boolean isStatic, final boolean isWrite, final Object v)
	{
		if(isStatic)
			System.out.println("Thread "+Thread.currentThread().getName()+(isWrite?" wrote":" read")+ " static field "+name+" with value "+v);
		else
			System.out.println("Thread "+Thread.currentThread().getName()+(isWrite?" wrote":" read")+ " instance field "+name+" of object "+o+" with value "+v);
	}
	  public static  void logArrayAcc(final Object a, int index, final boolean isWrite) {

		  String addr = a+"["+index+"]";
			String sig = "Thread "+Thread.currentThread().getName()+(isWrite?" wrote":" read")+ " array [index] "+addr;
			System.out.println(sig);
			detectRace(addr,isWrite,sig);

	  }
	  public static  void logArrayAcc(final Object a, int index, final boolean isWrite, final Object v) {
			System.out.println("Thread "+Thread.currentThread().getName()+(isWrite?" wrote":" read")+ " array [index] "+a+" ["+index+"] with value "+v);
	  }


  public static  void logStart(final Thread t) {
	  String name = Thread.currentThread().getName();
	    String name_t = t.getName();
	    //System.out.println("Thread "+name+" start new Thread "+name_t);
  }
  public static  void logJoin(final Thread t) {
	  String name = Thread.currentThread().getName();
	    String name_t = t.getName();
	    //System.out.println("Thread "+name+" join Thread "+name_t);
  }
  public static  void logLock(final Object o) {
	  String name = Thread.currentThread().getName();
	  System.out.println("Thread "+name+" lock object "+System.identityHashCode(o));

	  long tid = Thread.currentThread().getId();
	  HashSet<Integer> locks = threadToLocks.get(tid);
	  if(locks==null){
		  locks = new HashSet<Integer>();
		  threadToLocks.put(tid, locks);
	  }
	  locks.add(System.identityHashCode(o));
  }

  public static  void logUnlock(final Object o) {
	  String name = Thread.currentThread().getName();
	  System.out.println("Thread "+name+" unlock object "+System.identityHashCode(o));

	  long tid = Thread.currentThread().getId();
	  HashSet<Integer> locks = threadToLocks.get(tid);
	  if(locks==null){
		  assert(false);//impossible to get here
	  }
	  else
	  {
		  assert(locks.size()>0);
		  locks.remove(locks.size()-1);
	  }
  }

  public static  void logWait(final Object o) {
	  String name = Thread.currentThread().getName();
	  //System.out.println("Thread "+name+" wait signal on object "+System.identityHashCode(o));
  }
  public static  void logNotify(final Object o) {
	  String name = Thread.currentThread().getName();
	  //System.out.println("Thread "+name+" notify signal on object "+System.identityHashCode(o));
  }
  public static  void logNotifyAll(final Object o) {
	  String name = Thread.currentThread().getName();
	  //System.out.println("Thread "+name+" notifyAll signal "+System.identityHashCode(o));
  }
}