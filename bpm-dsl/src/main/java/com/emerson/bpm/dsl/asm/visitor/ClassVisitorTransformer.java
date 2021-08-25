package com.emerson.bpm.dsl.asm.visitor;

import java.io.File;
import java.io.IOException;

import com.emerson.bpm.api.Session;
import com.emerson.bpm.dsl.asm.tree.NodeFactory;
import com.emerson.bpm.dsl.util.DirExplorer;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.visitor.TreeVisitor;
import com.picasso.paddle.annotation.Inject;
import com.picasso.paddle.annotation.ProxyFactory;

public class ClassVisitorTransformer<R, A> {

	@ProxyFactory(eagerCreate=false, createStrategy="byName", suffix="Handler", hpackage="com.foo")
	NodeFactory factory;	
	
	@Inject(initmethod="begin", params={"StatefulSession"}, virtualparams="startnew:false", defer=true, factory="ServiceFactory", method="m")
	Session session;
	
    public void listStatements(File projectDir) {
    	
        new DirExplorer((level, path, file) -> path.endsWith(".java"), (level, path, file) -> {
            System.out.println(path);
            System.out.println(level);

            System.out.println("file = " + file.getName());

            try {
            	
                new TreeVisitor() {
                	
            	    @SuppressWarnings("unchecked")
					public void process(Node node) {
            	    	
            	    	factory.get(node)
            	    					.process(node);
            	    	
            	    }

                    
                }.visitPreOrder(StaticJavaParser.parse(file).findRootNode());
                
                System.out.println(); // empty line
            } catch (IOException e) {
                new RuntimeException(e);
            }
    
        	}).explore(projectDir);    	
    	
    }

	

    public void listMethodCalls(File projectDir) {
    
    }
    
	
}
