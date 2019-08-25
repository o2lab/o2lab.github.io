import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

public class Transformer implements ClassFileTransformer {
	private final String[] excludeList ={"apple/","java/","javax/", "sun/","sunw/", "com/","org/","Log"};

    public static void premain(String agentArgs, Instrumentation inst) {
		inst.addTransformer(new Transformer());
	}

    public byte[] transform(ClassLoader loader,String cname, Class<?> c, ProtectionDomain d, byte[] cbuf)
            throws IllegalClassFormatException {

    	//System.out.println("transforming class "+cname);

    	boolean toInstrument = true;

    	for (int i = 0; i < excludeList.length; i++) {
            String s = excludeList[i];
            if (cname.startsWith(s)) {
                toInstrument = false;
                break;
            }
        }
    	if (toInstrument) {
            ClassReader cr = new ClassReader(cbuf);
            ClassWriter cw = new ClassWriter(cr, 0);
            ClassVisitor cv = new ClassAdapterSample(cw);
            cr.accept(cv, 0);
            byte[] ret = cw.toByteArray();
            return ret;
    	}

    	return cbuf;
    }
}