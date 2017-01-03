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
package br.ufpr.gres.core.classpath.old;

import java.io.IOException;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jackson Antonio do Prado Lima <jacksonpradolima at gmail.com>
 * @version 1.0
 */
public class ClassloaderByteArraySource implements ClassByteArraySource {

    private final Logger logger = LoggerFactory.getLogger(ClassPath.class);

    private final ClassPath cp;

    public ClassloaderByteArraySource(final ClassLoader loader) {
        this.cp = new ClassPath(new OtherClassLoaderClassPathRoot(loader));        
    }

    @Override
    public Optional<byte[]> getBytes(final String classname) {
        try {
            return Optional.ofNullable(this.cp.getClassData(classname));
        } catch (final IOException ex) {
            logger.error("Error in getBytes", ex);
            return Optional.empty();
        }
    }

}
