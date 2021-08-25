package com.emerson.bpm.rule.parser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Method;
import java.net.URL;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;
import org.drools.compiler.lang.descr.PackageDescr;
import org.junit.Test;
import org.kie.internal.builder.conf.LanguageLevelOption;

import junit.framework.TestCase;

public class RuleParser extends TestCase {
	
	private IDRLParser parser;
	  
	/*
	public void test1() {

		try {
			final PackageDescr pkg = (PackageDescr) parseResource("rule", "rule_duration_expression.drl");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	*/

	@Test
	public void test2() {

		try {
			final PackageDescr pkg = (PackageDescr) parseResource("compilationUnit", "CashFlow.drl");
		
			assertNotNull(pkg);
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private Object parse(final String parserRuleName, final String text) throws Exception {
		return execParser(parserRuleName, new ANTLRStringStream(text));
	}

	private Object parseResource(final String parserRuleName, final String name) throws Exception {
		
		URL uf = RuleParser.class.getClassLoader().getResource(name);
				
		System.out.println(" url = "  + uf);
		
		
		final Reader reader = new InputStreamReader(getClass().getClassLoader().getResourceAsStream(name));
		final StringBuilder text = new StringBuilder();

		final char[] buf = new char[1024];
		int len = 0;
		while ((len = reader.read(buf)) >= 0) {
			text.append(buf, 0, len);
		}
		return parse(parserRuleName, text.toString());
	}

	public Object execParser(String testRuleName, CharStream charStream) throws Exception {
		try {
			createParser(charStream);
			
			/** Use Reflection to get rule method from parser */
			Method ruleName = null;
			Object[] params = null;
			for (Method method : DRL6Parser.class.getMethods()) {
				if (method.getName().equals(testRuleName)) {
					ruleName = method;
					Class<?>[] parameterTypes = method.getParameterTypes();

					for(Object x : parameterTypes)
						System.out.println(" x = " + x);

					params = new Object[parameterTypes.length];
				}
			}

			
			/** Invoke grammar rule, and get the return value */
			Object ruleReturn = ruleName.invoke(parser, params);

			if (parser.hasErrors()) {
				System.out.println(parser.getErrorMessages());
			}

			return ruleReturn;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	private void createParser(CharStream charStream) {
		parser = buildParser(charStream, LanguageLevelOption.DRL6);
	}

    public static IDRLParser buildParser(DRLLexer lexer, LanguageLevelOption languageLevel) {
        CommonTokenStream stream = new CommonTokenStream( lexer );
        return getDRLParser(stream, languageLevel);
    }

	public static IDRLParser buildParser(CharStream input, LanguageLevelOption languageLevel) {
        return buildParser(getDRLLexer( input, languageLevel ), languageLevel);
    }

	  public static IDRLParser getDRLParser(CommonTokenStream stream, LanguageLevelOption languageLevel) {
	        switch (languageLevel) {
	            case DRL5:
	                return new DRL5Parser(stream);
	            case DRL6:
	                return new DRL6Parser(stream);
	            case DRL6_STRICT:
	                return new DRL6StrictParser(stream);
	        }
	        throw new RuntimeException("Unknown language level");
	    }

	    public static DRLLexer getDRLLexer(CharStream input, LanguageLevelOption languageLevel) {
	        switch (languageLevel) {
	            case DRL5:
	                return new DRL5Lexer(input);
	            case DRL6:
	            case DRL6_STRICT:
	                return new DRL6Lexer(input);
	        }
	        throw new RuntimeException("Unknown language level");
	    }


	private void assertEqualsIgnoreWhitespace(final String expected, final String actual) {
	
		String nosp = actual.replaceAll(" ", "");
		
		assertEquals(expected, nosp);
	
	}

	private Reader getReader(final String name) throws Exception {
		final InputStream in = getClass().getResourceAsStream(name);
		return new InputStreamReader(in);
	}
}
