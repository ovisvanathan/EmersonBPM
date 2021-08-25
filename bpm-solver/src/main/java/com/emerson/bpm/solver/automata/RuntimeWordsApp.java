package com.emerson.bpm.solver.automata;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;

public class RuntimeWordsApp {

	public static void main(String[] args) {

		    String kwords = "foo, baz";

		    try {
				String words = "foo bar baz";
				InputStream is = new ByteArrayInputStream(words.getBytes(StandardCharsets.UTF_8));
				   
				RuntimeWordsLexer lexer = new RuntimeWordsLexer(
							CharStreams.fromStream(is), Stream.of(kwords));
				CommonTokenStream tokens = new CommonTokenStream(lexer);
				RuntimeWordsParser parser = new RuntimeWordsParser(tokens);        
				parser.parse();
			} catch (RecognitionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

}
