package com.emerson.bpm.dsl.asm.visitor;

import java.io.File;
import java.io.IOException;

import org.checkerframework.checker.units.qual.A;

import com.emerson.bpm.dsl.util.DirExplorer;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
 
public class MethodCallsExample {
 
    public static void listMethodCalls(File projectDir) {
        new DirExplorer((level, path, file) -> path.endsWith(".java"), (level, path, file) -> {
            System.out.println(path);
            System.out.println(level);

            System.out.println("file = " + file.getName());

            try {
            	
                new VoidVisitorAdapter<Object>() {

                	@Override
                    public void visit(MethodCallExpr n, Object arg) {
                        super.visit(n, arg);
                        System.out.println(" [L " + n.getBegin() + "] " + n);
                    }

        	        
                }.visit(StaticJavaParser.parse(file), null);
                
                System.out.println(); // empty line
            } catch (IOException e) {
                new RuntimeException(e);
            }
        }).explore(projectDir);
    }
    
    public static void listStringExprs(File dir) {
    	   
    try {
    	 
    		File [] fs = dir.listFiles();

    		for(File f : fs) {
    		
    			if(f.getName().endsWith(".java")) {
    		
    		CompilationUnit compilationUnit = StaticJavaParser.parse(f);
    		compilationUnit.accept(new VoidVisitorAdapter<Void>() {
    	        @Override
    	        public void visit(StringLiteralExpr n, Void arg) {
    	        //    assert(Nodes.getTypeName(n).equals( n.getValue()));
    	        
    	        	System.out.println("got str " + n.getValue());
    	        }
    	    }, null);

    			}
    		
    		}
    		
    } catch(Exception e) {
    	e.printStackTrace();
    }
    
    }
 
    public static void main(String[] args) {
        File projectDir = new File("d:\\tools\\pytools\\src\\main\\java\\gnu\\getopt\\");
        listMethodCalls(projectDir);
//        listStringExprs(projectDir);

    }
}
