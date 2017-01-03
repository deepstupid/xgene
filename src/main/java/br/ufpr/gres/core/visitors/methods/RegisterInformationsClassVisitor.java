/*
 * Copyright 2017 Jackson Antonio do Prado Lima <jacksonpradolima at gmail.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package br.ufpr.gres.core.visitors.methods;

import br.ufpr.gres.ClassContext;
import br.ufpr.gres.ClassInfo;
import org.objectweb.asm.ClassVisitor;

/**
 *
 * @author Jackson Antonio do Prado Lima <jacksonpradolima at gmail.com>
 * @version 1.0
 */
public class RegisterInformationsClassVisitor extends MutatingClassAdapter {

    private final ClassContext context;

    public RegisterInformationsClassVisitor(ClassContext context, ClassVisitor cv) {
        super(cv);
        this.context = context;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        this.context.registerClass(new ClassInfo(version, access, name, signature, superName, interfaces));
    }

    @Override
    public void visitSource(String source, String debug) {
        super.visitSource(source, debug);
        this.context.registerSourceFile(source);
    }
}
