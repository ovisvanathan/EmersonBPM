package com.emerson.bpm.dsl.asm.visitor;

import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ACC_STATIC;
import static org.objectweb.asm.Opcodes.ASM4;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Logger;

import org.json.JSONObject;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.util.TraceClassVisitor;

import com.evatic.paddle.asm.Printer;
import com.horus.velograph.model.Graph;
import com.horus.velograph.model.Graph.GraphType;
import com.horus.velograph.model.Vertex;
import com.mtask.phz.ObjectFactory;

public class OBFIteratorGenerator {
	
	
	String [] myclasses = {
		"PipelineType",
		"StepType",
		"StepDefnType",
		"FunctionType",
		"ParamType",
		"ParamsType"
	};
	
	public static Iterator iteratorFor(ObjectFactory obf) {
		
		
		Method [] mds = obf.getClass().getDeclaredMethods();
		
		for(Method m : mds)
			System.out.println("mds = " + m.getName());
		
		return null;
	}
	
	static ClassReader reader;
	static ClassWriter  writer;
	
	enum CollectType {
		METHOD,
		FIELD		
	};
	
	static boolean done = false;
	
	static Map<String, Class> classesMap;
	
	public static void main(String[] args) {

		ObjectFactory obf = new ObjectFactory();
		
		try {
			
			
				ClassLoader cl = OBFIteratorGenerator.class.getClassLoader();
				
				if(cl instanceof URLClassLoader) {
					URLClassLoader ucl = (URLClassLoader) cl;
					
					URL [] urls = ucl.getURLs();
					
					for(URL u : urls) {
					
			//			System.out.println(" url = " + u.toExternalForm());
						
						if(u.toExternalForm().endsWith("taskpipeline-1.0.jar"))							
							classesMap = loadClassFromJar(u);
						
					}
					
				}
			
			
			
			String classFile = "/" + ObjectFactory.class.getName().replace('.', '/') + ".class";
		//	System.out.println(classFile);
			
			URL url = obf.getClass().getResource(classFile);
			
			reader = new ClassReader(obf.getClass().getName());
			writer = new ClassWriter(reader, 0);
			    
			PublicizeMethodAdapter pubMethAdapter = new PublicizeMethodAdapter(writer, CollectType.METHOD);
			reader.accept(pubMethAdapter, 0);
     //  return writer.toByteArray();
			publicizeMethod(obf, CollectType.METHOD);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

	
    private static Map loadClassFromJar(URL pathToJar) {

    	Map<String, Class> classesMap = new HashMap();
    	try {
    		
    			String proto = pathToJar.getProtocol();
    			String path = pathToJar.toExternalForm();

    			path = path.substring(proto.length()+1);

    			System.out.println(" path = " + path);
    			
    			
    		
			JarFile jarFile = new JarFile(path);
			Enumeration<JarEntry> e = jarFile.entries();

			URL[] urls = { new URL("jar:file:" + pathToJar+"!/") };
			URLClassLoader cl = URLClassLoader.newInstance(urls);

			while (e.hasMoreElements()) {
			    JarEntry je = e.nextElement();
			    if(je.isDirectory() || !je.getName().endsWith(".class")){
			        continue;
			    }
   
			    // -6 because of .class
			    String className = je.getName().substring(0,je.getName().length()-6);
			    className = className.replace('/', '.');
			    Class c = cl.loadClass(className);

			    classesMap.put(c.getSimpleName(), c);
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    
    	return classesMap;
    }


	public static byte[] publicizeMethod(Object obf, CollectType collectType) {
    
    	try {
			reader = new ClassReader(obf.getClass().getName());
			writer = new ClassWriter(reader, 0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        List<String> retTypes = new LinkedList();

        Graph g = new Graph("DEP12197");
        
        g.setGraphType(GraphType.DIRECTED);

    	PublicizeMethodAdapter pubMethAdapter = new PublicizeMethodAdapter(writer, collectType);
        reader.accept(pubMethAdapter, 0);
        
        	while(!done) {
        			
        			if(!pubMethAdapter.isCollecting()) {
        				
        				List mtext = pubMethAdapter.getMethodText();
			        
        				for(int i=0;i<mtext.size();i++) {			        
				        	String s= null;
				        	if(i % 2 == 0)
				        		s = (String) mtext.get(i);
				        	
						        if(s != null) {	
						        	s = s.replaceAll("\n", "");
					        	
						        	int pos = s.indexOf("public");
					        	
						        	if(pos != -1) {					
						        		s = s.substring(pos);				
						        		int lpos = s.lastIndexOf("L");
							        	if(lpos != -1) {
							        		s = s.substring(lpos+1);
							
							        		System.out.println(" s = " + s);
							        		
							        		retTypes.add(s);
							        	}
						        	}        	
							        	
						        }
        				}

		        		System.out.println(" CMAP = " + classesMap);

							for(String x : retTypes) {
							        	
				   //     		String sx = x.replaceAll("/", "\\.");
				        		System.out.println(" sx = " + x);

				        		if(x.endsWith("JAXBElement;"))
				        			continue;
				        		
				        		
				        		Object ob = null;
								try {
										
										x = x.replaceAll(";", "");
										String xp = x.substring(x.lastIndexOf("/")+1);

										System.out.println(" XP = " + xp);
						        		System.out.println(" CMAP contains= " + classesMap.containsKey(xp));
						        		
										Class sclass = classesMap.get(xp);

										System.out.println(" CMAP sclass = " + sclass);
										
										Field [] fields = sclass.getDeclaredFields();
							
										for(Field f : fields) {
										
											Vertex v1 = new Vertex(sclass.getCanonicalName());

												
											Vertex v2 = null;
											Class fieldClass = (Class) f.getType();
										    Type gentype = f.getGenericType();
										    if (gentype instanceof ParameterizedType) {
										        ParameterizedType pType = (ParameterizedType) gentype;
										        System.out.print("Raw type: " + pType.getRawType() + " - ");
										
												Type [] args = pType.getActualTypeArguments();
												
												if(args.length == 1) {// list or array
												
													Class argType = (Class) pType.getActualTypeArguments()[0];
											
													String argTypeName = argType.getCanonicalName();
													
													String argval = 
															"{ \"RawName\" : \"" + fieldClass.getName() + "\", \"GenericName\" : \"" + argTypeName + "\", \"isList\" : \"true\" }"; 
													
													JSONObject argobj = new JSONObject(argval);

													v2 = new Vertex(f.getName());

													v2.setVertexData(argobj.toString());
													
												} else if(args.length == 2)  {// map type
													
													Class argType1 = (Class) pType.getActualTypeArguments()[0];
													
													String argTypeName1 = argType1.getCanonicalName();

													Class argType2 = (Class) pType.getActualTypeArguments()[1];
													
													String argTypeName2 = argType2.getCanonicalName();
													
													String argval = 
															"{ \"KeyType\" : \"" + argTypeName1 + "\", \"ValueType\" : \"" + argTypeName2 + "\", \"isMap\" : \"true\" }"; 
													
													JSONObject argobj = new JSONObject(argval);

													v2 = new Vertex(f.getName());

													v2.setVertexData(argobj.toString());
													
												} else if(args.length >= 3) {
													//some other type
													
													Type [] argTypes = pType.getActualTypeArguments();
													
													StringBuffer sbuf = new StringBuffer();
													for(int k=0;k<argTypes.length;k++) {
														Class kcx = (Class) argTypes[k];
														String cxname = kcx.getName();
													
														sbuf.append("{\"PARAM_" + k + "_TYPE\" : \"" + cxname + "\"");
														
														if(k != argTypes.length-1)
															sbuf.append(",");
													}
													
													sbuf.append("}");
												
													JSONObject argobj = new JSONObject(sbuf.toString());

													v2 = new Vertex(f.getName());

													v2.setVertexData(argobj.toString());
													
												}
												
										    } else {
												v2 = new Vertex(f.getName());

										    	if(List.class.isAssignableFrom(fieldClass) ||
										    			Set.class.isAssignableFrom(fieldClass) ||
										    			 Iterable.class.isAssignableFrom(fieldClass)) {
										    					v2.setVertexData("{\"RawType:\"" + fieldClass.getName() + "\", \"isList\":\"true\"}");												    	
										    			 } else
										    					v2.setVertexData("{\"RawType:\"" + fieldClass.getName() + "\"}");												    	
										    }
											
											g.addEdge(v1, v2);
											
										}
								    	
							//		ob = PaddleUtils.getBindingInstance(sx);
							//	} catch (ClassNotFoundException e) {
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
				        		
				        	}


					/*			
						List<Vertex> vertices = g.getChildren(StreamTraversal.DFS, new SchemaAwareIteratorGeneratorVisitor());	

						for(Vertex v : vertices) {
						
							System.out.println("From vertex = " + v.getVertLabel());
							
							Set<String> edgeverts = v.getEdgeVertices();

							Map vertsMap = g.getVerticesMap();
							for(String s : edgeverts) {

								System.out.println("To vertex str = " + s);

								Vertex to = (Vertex) vertsMap.get(s);
								
								System.out.println("To vertex = " + to.getVertLabel());
								
								System.out.println("To vertex List = " + to.getVertexData());
							}
														
						}
					*/	
						
						
        				done = true;	
			        	
        		}
		    }
			
			        
 //       return writer.toByteArray();
        	return null;
	}
    
    
    static class PublicizeMethodAdapter extends ClassVisitor {

        final Logger logger = Logger.getLogger("PublicizeMethodAdapter");
        TraceClassVisitor tracer;
        PrintWriter pw = new PrintWriter(System.out);

        CollectType collectType;
        
        List methodText;
        List fieldText;
                
        boolean collecting = true;

        public boolean isCollecting() {
			return collecting;
		}

		public CollectType getCollectType() {
			return collectType;
		}

		public List getFieldText() {
			return fieldText;
		}

		public Logger getLogger() {
			return logger;
		}

		public TraceClassVisitor getTracer() {
			return tracer;
		}

		public PrintWriter getPw() {
			return pw;
		}

		public List getMethodText() {
			return methodText;
		}

		public PublicizeMethodAdapter(ClassVisitor cv, CollectType collectType) {
            super(ASM4, cv);
            this.cv = cv;
            tracer = new TraceClassVisitor(cv, pw);
            this.collectType = collectType;		
        }

        @Override
        public MethodVisitor visitMethod(int access,
                String name,
                String desc,
                String signature,
                String[] exceptions) {

            if (name.equals("toUnsignedString0")) {
   //             logger.info("Visiting unsigned method");
                return tracer.visitMethod(ACC_PUBLIC + ACC_STATIC, name, desc, signature, exceptions);
            }
            
            
   //         System.out.println("sig =" + name);
            
            MethodVisitor mv = tracer.visitMethod(access, name, desc, signature, exceptions);

           // 	Printer p = tracer.getPrinter();            
	        //    methodText = p.getText();
         
	            //       printList(list, 0);            
            return mv;
        }

        
        @Override
        public FieldVisitor visitField (
            final int access,
            final String name,
            final String desc,
            final String signature,
            final Object value) {

        	FieldVisitor mv = tracer.visitField(access, name, desc, signature, value);
        		
        	//	Printer p = tracer.getPrinter();
                
	         //   fieldText = p.getText();

	    	//	System.out.println(" fieldText  = " + fieldText);
				
        	return mv;
        }

        
        @Override
        public void visitOuterClass(final String owner, 
        			final String name, final String descriptor) {
        	collecting = true;
        }

        private void printList(List list, int ind) {

     //   	System.err.println(" in PL = ");

            int i=0;
            for(int k=0;k<list.size();k++) {	
            	
            	Object x = list.get(k);
            	
            //	if(x instanceof List)
            //		printList((List) x, ind+4);

            //	printTabs(ind);
            	System.err.println(" item at k = " + k + " " + x);
            }

            
		}

		private void printTabs(int ind) {

			for(int p=0;p<ind;p++)
            	System.err.print("  ");
				
		}

		public void visitEnd() {
			
        	collecting = false;
			
			
            tracer.visitEnd();
            System.out.println(tracer.p.getText());
        }

    }
    
    
}