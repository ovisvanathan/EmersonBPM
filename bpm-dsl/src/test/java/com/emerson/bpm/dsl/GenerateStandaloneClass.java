package com.emerson.bpm.dsl;

import java.io.InputStream;

import org.apache.log4j.BasicConfigurator;

import nz.org.take.compiler.reference.DefaultCompiler;
import nz.org.take.compiler.util.DefaultLocation;
import nz.org.take.compiler.util.DefaultNameGenerator;
import nz.org.take.compiler.util.jalopy.JalopyCodeFormatter;
import nz.org.take.nscript.ScriptKnowledgeSource;

public class GenerateStandaloneClass {

	/**
	 * Generate the sources for the example.
	 * @param args
	 */
	public void generate(String resourceName, String srcFolder) throws Exception {
	
		
		BasicConfigurator.configure();
		nz.org.take.compiler.Compiler compiler = new DefaultCompiler();
		compiler.add(new JalopyCodeFormatter());
		compiler.setNameGenerator(new DefaultNameGenerator());
		compiler.setLocation(new DefaultLocation(srcFolder));
		// generate kb
		InputStream script = GenerateStandaloneClass.class.getResourceAsStream("/GTE.take");
		ScriptKnowledgeSource ksource = new ScriptKnowledgeSource(script);
		
		compiler.setPackageName("com.emerson.bpm.dsl");
		compiler.setClassName("GTX");
		
		long before = System.currentTimeMillis();
		compiler.compile(ksource.getKnowledgeBase());
		System.out.println("Compilation took "+(System.currentTimeMillis()-before)+"ms");
		
	}
	
	
}
