package com.emerson.bpm.dsl.asm.finder;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.emerson.bpm.dsl.asm.visitor.LinkedMultiTreeNode;
import com.emerson.bpm.functor.GenericView.Project;
import com.evatic.paddle.asm.graph.Relation;
import com.evatic.paddle.asm.java.graph.FileElement;
import com.evatic.paddle.asm.java.graph.GraphNode;
import com.evatic.paddle.asm.mem.ClassDepLister;
import com.evatic.paddle.asm.util.ASMFactory;
import com.evatic.paddle.asm.util.DependenciesListener;
import com.github.javaparser.ast.CompilationUnit;

public class SourceFinder {

	String project = "";
	// Initializing sourcepath
	Path srcPath = Paths.get(project);

	// srcPath.setLocation(new File(srcFolder));

	// System.out.println("srcfolder = " + srcFolder);

	// Initialize and set java extensions
	/*
	 * String exts = System.getProperty("java.ext.dirs"); if (exts != null) { Path
	 * extdirs = new Path(project); extdirs.setPath(exts);
	 * javac.setExtdirs(extdirs); if (logger.isDebugEnabled()) {
	 * logger.debug("using extension dir " + exts); } }
	 * 
	 * javac.setSrcdir(srcPath); javac.setProject(project); javac.setDestdir(new
	 * File(binFolder));
	 */
	// System.out.println("FileSelector srcDir = " + new
	// File(srcFolder+packageName.replace('.','/')).getAbsolutePath());

	public static void buildCU() {

		Class klazz = LinkedMultiTreeNode.class;
		
		URL file = klazz.getProtectionDomain().getCodeSource().getLocation();
		
		ASMFactory factory = new ASMFactory();
    
		FileElement fileNode = new FileElement(new File(file.toExternalForm()));
		  
    DependenciesListener builder = new DependenciesListener() {

		@Override
		public GraphNode lookup(GraphNode target) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public GraphNode newNode(GraphNode orphan) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void newDep(GraphNode parent, GraphNode child, Relation t) {

				System.err.println("Got dep parent =" + parent + " to child = " + child + " rel = " + t.getForwardName());
		}

		@Override
		public void newDeps(GraphNode parent, GraphNode[] childs, Relation t) {
			System.err.println("Got dep2 parent =" + parent + " to child = " + Arrays.asList(childs) + " rel = " + t.getForwardName());
			
		}
    	
    };
    
    ClassDepLister depLister = new ClassDepLister(factory, builder, fileNode);
    
    depLister.visit(1, 0, klazz.getName(), klazz.getSimpleName(),
    		klazz.getSuperclass().getName(), new String [] {} );

    
    CompilationUnit cu;
    
    List<CompilationUnit> sourceFilesCollection = new ArrayList();

    /*
    FileSelector selector = new FileSelector() {
    	String srcDir = 
		    new File(srcFolder+packageName.replace('.','/')).getAbsolutePath();


		public boolean isSelected(File baseDir, String fileName, File file)
			throws BuildException {

	//		System.out.println("FileSelector fileName = " + fileName);

			if (file.getParentFile().getAbsolutePath().equals(srcDir) && fileName.endsWith(".java")) {
		//		System.out.println("include file " + fileName + " in baseDir " + baseDir);
				logger.debug("include file " + fileName + " in baseDir " + baseDir);

				sourceFilesCollection.add(file);
				
				return true;
			}
			else {
				return false;
			}
		}
    };
    
    */

    }
	// javac.add(selector);

	// project.executeTarget("compile");
	/*
	 * javac.setVerbose(true);
	 * 
	 * try { javac.execute(); } catch (Exception x) { x.printStackTrace(); }
	 */

	protected Project getProject() {
		Project project = new Project();

		/*
		 * 
		 * project.addBuildListener(new BuildListener() {
		 * 
		 * //@Override public void buildFinished(BuildEvent arg0) {
		 * logger.info("finished build"); // System.out.println("finished build");
		 * 
		 * }
		 * 
		 * // @Override public void buildStarted(BuildEvent arg0) {
		 * logger.info("starting build"); // System.out.println("starting build");
		 * 
		 * }
		 * 
		 * // @Override public void messageLogged(BuildEvent e) { if (e.getException()
		 * != null) { // System.out.println(e.getMessage());
		 * 
		 * logger.error(e.getMessage(), e.getException()); } else { // TODO use log
		 * priority in message // System.out.println(e.getMessage());
		 * logger.error(e.getMessage()); } }
		 * 
		 * // @Override public void targetFinished(BuildEvent e) {
		 * logger.error("target finished: " + e.getTarget()); //
		 * System.out.println("target finished: " + e.getTarget()); //
		 * System.out.println(e.getMessage()); logger.error(e.getMessage()); }
		 * 
		 * // @Override public void targetStarted(BuildEvent e) {
		 * logger.error("target started: " + e.getTarget());
		 * logger.error(e.getMessage()); // System.out.println("target started: " +
		 * e.getTarget()); // System.out.println(e.getMessage());
		 * 
		 * }
		 * 
		 * // @Override public void taskFinished(BuildEvent e) {
		 * logger.error("task finished: " + e.getTask()); logger.error(e.getMessage());
		 * // System.out.println("task finished: " + e.getTask()); //
		 * System.out.println(e.getMessage());
		 * 
		 * }
		 * 
		 * // @Override public void taskStarted(BuildEvent e) {
		 * logger.info("task started: " + e.getTask()); logger.info(e.getMessage()); //
		 * System.out.println("task started:  = " + e.getTask()); //
		 * System.out.println(e.getMessage());
		 * 
		 * } });
		 * 
		 * // project.addBuildListener(l); // if (compiler!=null) //
		 * project.setProperty("build.compiler",compiler);
		 * 
		 * 
		 * try { project.init(); } catch (Throwable t) { t.printStackTrace(); }
		 * 
		 */
		return project;
	}

	/*
	 * 
	 * private void setProperties(Map<String,String> propertyMap,boolean overridable
	 * ) throws Exception{ if(project == null){ throw new
	 * Exception("Properties can not be set. Please initialize the project first.");
	 * }
	 * 
	 * if(propertyMap == null){ throw new
	 * Exception("Properties map provided was null"); }
	 * 
	 * Set propKeys = propertyMap.keySet(); Iterator iterator = propKeys.iterator();
	 * 
	 * while (iterator.hasNext()) { String propertyKey = (String) iterator.next();
	 * String propertyValue = propertyMap.get(propertyKey);
	 * 
	 * if(propertyValue == null) continue;
	 * 
	 * if(overridable){ project.setProperty(propertyKey,propertyValue); } else {
	 * project.setUserProperty(propertyKey,propertyValue); } }
	 * project.setSystemProperties(); }
	 * 
	 * 
	 * private void runTarget(String targetName) throws Exception{ if(project ==
	 * null){ throw new
	 * Exception("Properties can not be set. Please initialize the project first.");
	 * }
	 * 
	 * if(targetName == null){ targetName = project.getDefaultTarget(); }
	 * 
	 * if(targetName == null){ throw new Exception("Target Not found :"+targetName);
	 * }
	 * 
	 * // System.out.println(" exec  tgt " + targetName);
	 * 
	 * project.executeTarget(targetName); }
	 * 
	 */

}
