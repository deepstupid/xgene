package br.ufpr.gres.core.classpath;

import br.ufpr.gres.ClassContext;
import br.ufpr.gres.ClassInfo;
import br.ufpr.gres.core.visitors.methods.RegisterInformationsClassVisitor;
import br.ufpr.gres.core.visitors.methods.empty.NullVisitor;
import org.objectweb.asm.ClassReader;

import java.io.IOException;

public class DynamicClassDetails extends ClassDetails {

    private final byte[] bytes;

    public static br.ufpr.gres.core.classpath.DynamicClassDetails get(Class c) throws IOException, ClassNotFoundException {
        final ClassContext context = new ClassContext();


        byte[] bytes = ClassLoader.getSystemResourceAsStream(c.getName().replace('.', '/') + ".class").readAllBytes();

        final ClassReader first = new ClassReader(bytes);
        final NullVisitor nv = new NullVisitor();
        final RegisterInformationsClassVisitor mca = new RegisterInformationsClassVisitor(context, nv);

        first.accept(mca, ClassReader.EXPAND_FRAMES);

        return new br.ufpr.gres.core.classpath.DynamicClassDetails(context.getClassInfo(), c, bytes);
    }

    public DynamicClassDetails(ClassInfo info, Class clazz, byte[] bytes) throws ClassNotFoundException {
        super(info, clazz);
        this.bytes = bytes;
    }

    @Override
    public byte[] getBytes() throws IOException {
        return bytes;
    }
}
