package com.emerson.bpm.dsl.asm;


import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;


public class ClassPrinter extends ClassVisitor {

    @SuppressWarnings("restriction")
	public ClassPrinter() {
        super(org.objectweb.asm.Opcodes.ASM4);
    }

    public void visit(int version, int access, String name,
                      String signature, String superName, String[] interfaces) {
        System.out.println(name + " extends " + superName + " {");
    }

    public void visitSource(String source, String debug) {
   
    }

    public void visitOuterClass(String owner, String name, String desc) {

    }

    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        return null;
    }

    public void visitAttribute(Attribute attr) {
    }

    public void visitInnerClass(String name, String outerName,
                                String innerName, int access) {
    }

    public FieldVisitor visitField(int access, String name, String desc,
                                   String signature, Object value) {
        System.out.println("    " + desc + " " + name);
    
 	   System.out.println(" fiels name = " + name);
 	   
        return null;
    }

    /*
     * 
     * (non-Javadoc)
     * @see org.objectweb.asm.ClassVisitor#visitMethod(int, java.lang.String, java.lang.String, java.lang.String, java.lang.String[])
     * TODO:
     * This method scans class methods for annotations such as provides and extracts the method signature
     * for writing into a interface file. The parameter type must first be deduced so the appropriate
     * import headers can be generated.
     */
    public MethodVisitor visitMethod(int access, String name,
                                     String desc, String signature, String[] exceptions) {
        System.out.println("    " + name + desc);
        
        return null;
    }

    public void visitEnd() {
        System.out.println("}");
    }
    
}
