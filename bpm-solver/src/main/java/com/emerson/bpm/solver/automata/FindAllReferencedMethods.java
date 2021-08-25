package com.emerson.bpm.solver.automata;

import java.io.IOException;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Handle;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;


public class FindAllReferencedMethods {

	
    public static void main(String[] args) throws IOException {
    
    	ClassReader r = new ClassReader(FindAllReferencedMethods.class.getName());
        r.accept(new ClassVisitor(Opcodes.ASM5) {
            @Override
            public MethodVisitor visitMethod(
                   int access, String name, String desc, String sig, String[] ex) {
                return new MethodRefCollector();
            }

        }, ClassReader.SKIP_DEBUG|ClassReader.SKIP_FRAMES);
    }

    static void referencedMethod(String owner, String name, String desc) {
        System.out.println(
            Type.getObjectType(owner).getClassName() + "." + name + " " + desc);
    
        System.out.println(owner);
        System.out.println(name);
        System.out.println(desc);
        	    
    }

    static class MethodRefCollector extends MethodVisitor {
        public MethodRefCollector() {
            super(Opcodes.ASM5);
        }

        @Override
        public void visitMethodInsn(
                    int opcode, String owner, String name, String desc, boolean itf) {

        	
        	
        	
        	referencedMethod(owner, name, desc);
        }

        @Override
        public void visitInvokeDynamicInsn(
                    String name, String desc, Handle bsm, Object... bsmArgs) {
            if(bsm.getOwner().equals("java/lang/invoke/LambdaMetafactory")
            && bsm.getDesc().equals(bsm.getName().equals("altMetafactory")?
                                    ALT_SIG: MF_SIG)) {

                System.out.println("dynamic");

                
                    System.out.println(name);
                    System.out.println(desc);
                
            	Handle target = (Handle)bsmArgs[1];
                referencedMethod(target.getOwner(), target.getName(), target.getDesc());
            }
        }
    }
    static String MF_SIG = "(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;"
        +"Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/"
        +"MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;";
    static String ALT_SIG = "(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;"
        +"Ljava/lang/invoke/MethodType;[Ljava/lang/Object;)Ljava/lang/invoke/CallSite;";
}