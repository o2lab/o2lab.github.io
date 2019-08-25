
/**
 * Given two integers, compute their greatest common divisor
 */
public class GCD {
	public static void main(String[] args) {
		if(args.length<2)
		{
			System.err.println("java GCD int1 int2\n"
					+"Example: java GCD 876 267");
			return;
		}
	    int p = Integer.parseInt(args[0]);
	    int q = Integer.parseInt(args[1]);
	    System.out.println("gcd(" + p + ", " + q + ") = " + gcd(p, q));
	}

	public static int gcd(int b, int d)
	{
	    int gcd = 1;

	    if(b>d)
	    {
	        for(int i = d; i >=1; i--)
	        {
	            if(b%i==0 && d%i ==0)
	            {
	                return i;
	            }
	        }
	    }
	    else
	    {
	        for(int j = b; j >=1; j--)
	        {
	            if(b%j==0 && d% j==0)
	            {
	                return j;
	            }
	        }
	    }
	    return gcd;
	}
}
