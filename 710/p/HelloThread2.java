import java.lang.Thread.State;
import java.util.*;
public class HelloThread2 {
	static int x;
	double y=1;
	static long[] z ;
	boolean b = true;
	char c[] = new char[2];

	public static void main(String[] args) {
		 final HelloThread2 hello = new HelloThread2();

		 Thread t1 = new Thread(new Runnable() {
				 public void run() {
					 if(hello.c[0]=='a')
						 hello.c[1]='0';
					 if(x>0)
						 hello.y=2;
					 if(hello.b) z=new long[1];
					x=2;
				 }
			 });
			 t1.start();

			 x=1;
			 hello.m();


	}
	public void m()
	{
		z=new long[2];

		y=0;
		 c[0]='a';
			c[1]='b';
			b= false;

			z[0]=1;
			z[1]=x+2;
	}

}