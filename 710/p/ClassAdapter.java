import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class ClassAdapter extends ClassVisitor {

    public ClassAdapter(ClassVisitor cv) {
        super(Opcodes.ASM5,cv);
    }
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {

        MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);

        if (mv != null) {
            mv = new MethodAdapter(mv);
        }

        return mv;
    }
}