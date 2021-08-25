package com.emerson.bpm.dsl.util;

import java.io.File;

public class JavaToAST {

	public interface FileHandler {
        void handle(int level, String path, File file);
    }

    public interface Filter {
        boolean interested(int level, String path, File file);
    }
    
    private FileHandler fileHandler;
    private Filter filter;
    
	public JavaToAST(Filter filter, FileHandler fileHandler) {
        this.filter = filter;
        this.fileHandler = fileHandler;
    }

    public void explore(File root) {
        explore(0, "", root);
    }

	private void explore(int i, String string, File root) {
		// TODO Auto-generated method stub
		
	}


}
