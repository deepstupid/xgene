/*
 * Copyright 2016 Jackson Antonio do Prado Lima <jacksonpradolima at gmail.com>.
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
package br.ufpr.gres.core.classpath;

import br.ufpr.gres.ClassContext;
import br.ufpr.gres.ClassInfo;
import br.ufpr.gres.core.visitors.methods.RegisterInformationsClassVisitor;
import br.ufpr.gres.core.visitors.methods.empty.NullVisitor;
import org.objectweb.asm.ClassReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Modifier;

/**
 * @author Jackson Antonio do Prado Lima <jacksonpradolima at gmail.com>
 * @version 1.0
 */
abstract public class ClassDetails {

    private final Logger logger = LoggerFactory.getLogger(ClassDetails.class);

    private final ClassInfo classInfo;
    private ClassName className;

    private Class clazz;

    public ClassDetails(ClassInfo classInfo, Class clazz) throws ClassNotFoundException {
        this.classInfo = classInfo;

        this.className = new ClassName(this.classInfo.getName());

        this.clazz = clazz;
    }

    abstract public byte[] getBytes() throws IOException;


    /**
     * Class name without package
     *
     * @return
     */
    public ClassName getClassName() {
        return this.className;
    }


    /**
     * Examine if class <i>c</i> is an applet class
     */
    private boolean isApplet(Class c) {
        while (c != null) {
            if (c.getName().indexOf("java.applet") == 0) {
                return true;
            }

            c = c.getSuperclass();
            if (c.getName().indexOf("java.lang") == 0) {
                return false;
            }
        }
        return false;
    }

    /**
     * Examine if class <i>c</i> is a GUI class
     */
    private boolean isGUI(Class c) {
        while (c != null) {
            if ((c.getName().indexOf("java.awt") == 0)
                    || (c.getName().indexOf("javax.swing") == 0)) {
                return true;
            }

            c = c.getSuperclass();
            if (c.getName().indexOf("java.lang") == 0) {
                return false;
            }
        }
        return false;
    }

    public Class getClassInstance() throws InstantiationException, IllegalAccessException {
        // initialization of the test set class
        if (clazz.newInstance() == null) {
            throw new InstantiationException();
        }
        return clazz;
    }

    /**
     * Verify if a class is testable
     *
     * @return Return true if a class is testable; otherwise false
     * @throws java.lang.ClassNotFoundException
     * @throws java.io.IOException
     */
    public boolean isTestable() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        Class c = getClassInstance();

        if (c.isInterface()) {
            logger.error("Can't apply mutation because " + className + " is 'interface'");
            return false;
        }

        if (Modifier.isAbstract(c.getModifiers())) {
            logger.error("Can't apply mutation because " + className + " is 'abstract' class");
            return false;
        }

        if (isGUI(c)) {
            logger.error("Can't apply mutation because " + className + " is 'GUI' class");
            return false;
        }
        if (isApplet(c)) {
            logger.error("Can't apply mutation because " + className + " is 'applet' class");
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
        return this.className.toString();
    }
}
