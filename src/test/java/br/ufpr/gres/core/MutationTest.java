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
package br.ufpr.gres.core;

import br.ufpr.gres.ClassContext;
import br.ufpr.gres.core.classpath.ClassDetails;
import br.ufpr.gres.core.classpath.DynamicClassDetails;
import br.ufpr.gres.core.operators.IMutationOperator;
import br.ufpr.gres.core.operators.method_level.AOR2;
import br.ufpr.gres.core.operators.method_level.ROR;
import br.ufpr.gres.core.premutation.PreMutationAnalyser;
import br.ufpr.gres.core.premutation.PremutationClassInfo;
import br.ufpr.gres.core.visitors.methods.MutatingClassVisitor;
import br.ufpr.gres.core.visitors.methods.empty.NullVisitor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import example.Cal;
import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

/**
 *
 * @author Jackson Antonio do Prado Lima <jacksonpradolima at gmail.com>
 * @version 1.0
 */
public class MutationTest {

    private final ArrayList<IMutationOperator> MUTATORS = new ArrayList<>();

    public MutationTest() {
        MUTATORS.add(AOR2.AOR2);
        MUTATORS.add(ROR.ROR);
    }

    public Mutant getMutation(final MutationIdentifier id, byte[] classToMutate) {
        Collection<IMutationOperator> mutators = new ArrayList<>(MUTATORS);
        Collection<IMutationOperator> mutatorsFiltered = mutators.stream().filter(p -> id.getMutator().equals(p.getName())).collect(Collectors.toList());

        final ClassContext context = new ClassContext();
        context.setTargetMutation(id);

        // Lembrar de usar isso aqui (ClassPathByteArraySource - pitest)
        // GregorMutater... carregar os bytes
//        final Optional<byte[]> bytes = this.byteSource.getBytes(id.getClassName()
//                .asJavaName());
        //final PremutationClassInfo classInfo = performPreScan(classToMutate);
        final ClassReader reader = new ClassReader(classToMutate);
        final ClassWriter w = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);

        final MutatingClassVisitor mca = new MutatingClassVisitor(mutatorsFiltered, context, w);
        reader.accept(mca, ClassReader.EXPAND_FRAMES);

        final List<MutationDetails> details = context.getCollectedMutations();

        return new Mutant(details.stream().filter(p -> p.getId().equals(id)).findFirst().get(), w.toByteArray());
    }

    public Mutant getHigherOrderMutant(final ArrayList<MutationIdentifier> ids, byte[] classToMutate) {
        Collection<IMutationOperator> mutators = new ArrayList<>(MUTATORS);
        //Collection<IMutationOperator> mutatorsFiltered = mutators.stream().filter(p ->.getMutator().equals(p.getName())).collect(Collectors.toList());

        final ClassContext context = new ClassContext();
        context.setTargetMutation(ids);

        // Lembrar de usar isso aqui (ClassPathByteArraySource - pitest)
        // GregorMutater... carregar os bytes
//        final Optional<byte[]> bytes = this.byteSource.getBytes(id.getClassName()
//                .asJavaName());
        //final PremutationClassInfo classInfo = performPreScan(classToMutate);
        final ClassReader reader = new ClassReader(classToMutate);

        final ClassWriter w = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);


        final MutatingClassVisitor mca = new MutatingClassVisitor(mutators, context, w);

        reader.accept(mca, ClassReader.EXPAND_FRAMES);

        List<MutationDetails> details = new ArrayList<>();

        for (MutationDetails detail : context.getCollectedMutations()) {
            if (ids.stream().anyMatch(p -> p.equals(detail.getId()))) {
                details.add(detail);
            }
        }

        return new Mutant(details, w.toByteArray());
    }

    private PremutationClassInfo performPreScan(final byte[] classToMutate) {
        final ClassReader reader = new ClassReader(classToMutate);

        final PreMutationAnalyser an = new PreMutationAnalyser();
        reader.accept(an, 0);
        return an.getClassInfo();
    }

//    @Test
//    public void mutation() throws Exception {
//        String directory = Paths.get(System.getProperty("user.dir")) + File.separator + "examples" + File.separator + "bub";
//        ClassDetails classes = new Resources(directory).getClasses().get(0);
//
//        final byte[] classToMutate = classes.getBytes();
//
//        try (DataOutputStream dout = new DataOutputStream(new FileOutputStream(new File(directory + File.separator + "mutants", "Original.class")))) {
//            dout.write(classToMutate);
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//
//        final ClassContext context = new ClassContext();
//        //context.setTargetMutation();
//
//        final PremutationClassInfo classInfo = performPreScan(classToMutate);
//
//        final ClassReader first = new ClassReader(classToMutate);
//        final NullVisitor nv = new NullVisitor();
//        Collection<IMutationOperator> mutators = new ArrayList<>(MUTATORS);
//
//        final MutatingClassVisitor mca = new MutatingClassVisitor(mutators, context, nv);
//
//        first.accept(mca, ClassReader.EXPAND_FRAMES);
//
//        if (!context.getTargetMutation().isEmpty()) {
//            final List<MutationDetails> details = context.getMutationDetails(context.getTargetMutation().get(0));
//        } else {
//            ArrayList<MutationDetails> details = new ArrayList(context.getCollectedMutations());
//
//            for (IMutationOperator operator : mutators) {
//                int i = 0;
//                for (MutationDetails detail : details.stream().filter(p -> p.getMutator().equals(operator.getName())).collect(Collectors.toList())) {
//                    Mutant mutant = getMutation(detail.getId(), classToMutate);
//                    i++;
//                    System.out.println(mutant.getDetails().toString());
//                    try (DataOutputStream dout = new DataOutputStream(new FileOutputStream(new File(directory + File.separator + "mutants", mutant.getDetails().get(0).getMutator() + "_" + i + ".class")))) {
//                        dout.write(mutant.getBytes());
//                    } catch (IOException ex) {
//                        ex.printStackTrace();
//                    }
//                }
//            }
//
//        }
//
//        ArrayList<MutationIdentifier> details = new ArrayList(context.getCollectedMutations().subList(0, 5).stream().map(MutationDetails::getId).collect(Collectors.toList()));
//        System.out.println("Creating a mutant with order " + details.size());
//
//        Mutant mutant = getHigherOrderMutant(details, classToMutate);
//        System.out.println("The new mutant");
//        System.out.println(mutant.toString());
//
//        try (DataOutputStream dout = new DataOutputStream(new FileOutputStream(new File(directory + File.separator + "mutants", "HOM.class")))) {
//            dout.write(mutant.getBytes());
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//    }


    @Test
    public void testMutationDynamic() throws Exception {


        ClassDetails classes = DynamicClassDetails.get(Cal.class);

        final byte[] classToMutate = classes.getBytes();

//        try (DataOutputStream dout = new DataOutputStream(new FileOutputStream(new File(directory + File.separator + "mutants", "Original.class")))) {
//            dout.write(classToMutate);
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }

        final ClassContext context = new ClassContext();
        //context.setTargetMutation();

        final PremutationClassInfo classInfo = performPreScan(classToMutate);

        final ClassReader first = new ClassReader(classToMutate);
        final NullVisitor nv = new NullVisitor();
        Collection<IMutationOperator> mutators = new ArrayList<>(MUTATORS);

        final MutatingClassVisitor mca = new MutatingClassVisitor(mutators, context, nv);

        first.accept(mca, ClassReader.EXPAND_FRAMES);

        if (!context.getTargetMutation().isEmpty()) {
            final List<MutationDetails> details = context.getMutationDetails(context.getTargetMutation().get(0));
        } else {
            ArrayList<MutationDetails> details = new ArrayList(context.getCollectedMutations());

            for (IMutationOperator operator : mutators) {
                int i = 0;
                for (MutationDetails detail : details.stream().filter(p -> p.getMutator().equals(operator.getName())).collect(Collectors.toList())) {
                    Mutant mutant = getMutation(detail.getId(), classToMutate);
                    i++;
                    System.out.println(mutant.getDetails().toString());
//                    try (DataOutputStream dout = new DataOutputStream(new FileOutputStream(new File(directory + File.separator + "mutants", mutant.getDetails().get(0).getMutator() + "_" + i + ".class")))) {
//                        dout.write(mutant.getBytes());
//                    } catch (IOException ex) {
//                        ex.printStackTrace();
//                    }
                }
            }

        }

        ArrayList<MutationIdentifier> details = new ArrayList(context.getCollectedMutations().subList(0, 5).stream().map(MutationDetails::getId).collect(Collectors.toList()));
        System.out.println("Creating a mutant with order " + details.size());

        Mutant mutant = getHigherOrderMutant(details, classToMutate);
        System.out.println("The new mutant");
        System.out.println(mutant.toString());


        DynamicClassLoader d = new DynamicClassLoader();

        //"Mutant" + (int)(Math.random()*10000) /* HACK */
        Class m = mutant.compile(context.getJavaClassName(), d);
        System.out.println(m + " " + m.getName());
        Object c = m.newInstance();
        System.out.println(m.getMethods());

        System.out.println(m.getMethods()[0].invoke(c, 1, 1, 2, 1, 1983));
        System.out.println(Cal.cal(1, 1, 2, 1, 1983));

//        try (DataOutputStream dout = new DataOutputStream(new FileOutputStream(new File(directory + File.separator + "mutants", "HOM.class")))) {
//            dout.write(mutant.getBytes());
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
    }

}
