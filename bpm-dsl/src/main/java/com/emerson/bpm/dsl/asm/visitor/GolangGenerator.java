package com.emerson.bpm.dsl.asm.visitor;

import java.io.IOException;
import java.io.InputStream;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.google.common.base.Strings;

public class GolangGenerator {
	
	
	
	static ClassReader reader;
	static ClassWriter  writer;
	
	enum CollectType {
		METHOD,
		FIELD, ALL		
	};
	
	static boolean done = false;
		
	public static void main(String[] args) {

		try {	
			
			GolangGenerator.class.getClassLoader().getResourceAsStream("jav/LinkedMultiTreeNode.java.source");

			/*
		    new ((file) -> path.endsWith(".java"), (level, path, file) -> {

		    	System.out.println(path);
	            System.out.println(Strings.repeat("=", path.length()));
	            try {
	                new VoidVisitorAdapter<Object>() {
	                    @Override
	                    public void visit(MethodCallExpr n, Object arg) {
	                        super.visit(n, arg);
	                        System.out.println(" [L " + n.getBeginLine() + "] " + n);
	                    }
	                }.visit(JavaParser.parse(file), null);
	                System.out.println(); // empty line
	            } catch (ParseException | IOException e) {
	                new RuntimeException(e);
	            }
	        }).explore(projectDir);
		    
		    */
		    
		        
		        
		       // assertEquals(3, typeDeclaration.getAllFields().size());			
			

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

	  protected static CompilationUnit parseSample(String sampleName) {
	        return parseSample(sampleName, "java.source");
	    }

	    private static CompilationUnit parseSample(String sampleName, String extension) {
	        InputStream is = GolangGenerator.class.getClassLoader().getResourceAsStream(sampleName + "." + extension);
	        if (is == null) {
	            throw new RuntimeException("Unable to find sample " + sampleName);
	        }
	        return StaticJavaParser.parse(is);
	    }
    
}