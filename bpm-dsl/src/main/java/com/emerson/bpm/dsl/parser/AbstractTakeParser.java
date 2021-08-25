package com.emerson.bpm.dsl.parser;

/**
 * Copyright 2008 Jens Dietrich Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 
 * Unless required by applicable law or agreed to in writing, software distributed under the 
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, 
 * either express or implied. See the License for the specific language governing permissions 
 * and limitations under the License.
 */
/**
 * Script parser. Stateful, instances should not be shared. 
 * @author <a href="http://www-ist.massey.ac.nz/JBDietrich/">Jens Dietrich</a>
 */

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import nz.org.take.AbstractPredicate;
import nz.org.take.AggregationFunction;
import nz.org.take.Annotatable;
import nz.org.take.AnnotationKeys;
import nz.org.take.ComplexTerm;
import nz.org.take.Constant;
import nz.org.take.DefaultKnowledgeBase;
import nz.org.take.DerivationRule;
import nz.org.take.ExternalFactStore;
import nz.org.take.Fact;
import nz.org.take.JPredicate;
import nz.org.take.KnowledgeBase;
import nz.org.take.Predicate;
import nz.org.take.Prerequisite;
import nz.org.take.PropertyPredicate;
import nz.org.take.Query;
import nz.org.take.SimplePredicate;
import nz.org.take.Term;
import nz.org.take.Variable;
import nz.org.take.nscript.AnnotationPropagationPolicy;
import nz.org.take.nscript.JSPELParser;
import nz.org.take.nscript.Parser;
import nz.org.take.nscript.ParserSupport;
import nz.org.take.nscript.PropertyFinder;
import nz.org.take.nscript.QuerySpec;
import nz.org.take.nscript.ScriptException;
import nz.org.take.nscript.Tokenizer;


public class AbstractTakeParser  extends ParserSupport {

		public static final String _NAME1 = "[a-zA-Z][a-zA-Z0-9_]*"; // names for types
		public static final String _NAME2 = "[a-zA-Z_][a-zA-Z0-9_]*"; // names for var and ref declarations
		public static final Pattern TYPE_NAME = Pattern.compile(_NAME1+"(\\."+_NAME1+")*");
		public static final Pattern NAME = Pattern.compile(_NAME2);
		public static final Pattern LIST_OF_NAMES = Pattern.compile(_NAME2+"([\\s]*\\,[\\s]*"+_NAME2+")*");
		public static final Pattern IMPORT = Pattern.compile("(import|IMPORT)[\\s]+"+_NAME2+"(\\."+_NAME2+")*(\\.\\*)*[\\s]*[;]?[\\s]*");
		public static final Pattern COMMENT = Pattern.compile("//(.)*");
		public static final Pattern GLOBAL_ANNOTATION = Pattern.compile("@@(.)+=(.)+");
		public static final Pattern LOCAL_ANNOTATION = Pattern.compile("@[^@](.)*=[\\s]*(.)*");	
		public static final Pattern EXTERNAL = Pattern.compile("external[\\s]+[a-zA-Z_][a-zA-Z0-9_]*:[\\s]+[a-zA-Z_][a-zA-Z0-9_]*\\[(.)*\\]");	
		public static final Pattern AGGREGATION = Pattern.compile("(aggregation|AGGREGATION)[\\s]+(.)*\\[(.)*\\]");	 // not very precise, needs improvement
//		public static final Pattern QUERY = Pattern.compile("(not[\\s]+)?"+_NAME2+"[\\s]*\\[[\\s]*(in|out)([\\s]*,[\\s]*(in|out))*[\\s]*\\]");

		//allows in OR IN, out or OUT, not or NOT
		public static final Pattern QUERY = Pattern.compile("((not|NOT)[\\s]+)?"+_NAME2+"[\\s]*\\[[\\s]*((in|out)|(IN|OUT))([\\s]*,[\\s]*((in|out)|(IN|OUT)))*[\\s]*\\]");

//		public static final Pattern RULE = Pattern.compile(_NAME1+"[\\s]*:[\\s]+if[\\s]+(.)*");
		//no space needed after : and allows if or IF
		public static final Pattern RULE = Pattern.compile(_NAME1+"[\\s]*:[\\s]*(if|IF)[\\s]+(.)*");

		public static final Pattern FACT = Pattern.compile(_NAME1+"[\\s]*:(.)*");
		public static final Pattern CONDITION1 = Pattern.compile(_NAME2+"\\[(.)*\\]");
		public static final Pattern STRING_LITERAL = Pattern.compile("\'(.)*\'"); // TODO handle escaped quotes

		//changing to protected for subclassing on 24/05/2011 by OV
		protected ClassLoader classLoader = Parser.class.getClassLoader(); 	
		protected KnowledgeBase kb = null;
		protected Map<String,Variable> variables = new HashMap<String,Variable>();
		protected Map<String,Constant> constants = new HashMap<String,Constant>();
		protected Map<String,AggregationFunction> aggregationFunctions = new HashMap<String,AggregationFunction>();
		protected Map<String,String> localAnnotations = new HashMap<String,String>();
		protected List<QuerySpec> querySpecs = new ArrayList<QuerySpec>();	
		protected Map<String,Predicate> predicatesByName = new HashMap<String,Predicate>();
		protected Map<SimplePredicate,SimplePredicate> predicates = new HashMap<SimplePredicate,SimplePredicate>(); // use map for simple lookup with get
		protected List<String> importedClasses = new ArrayList<String>();
		protected List<String> importedPackages = new ArrayList<String>();
		protected List<ScriptException> issues = null;
		protected Collection<String> ids = new HashSet<String>();
		protected boolean verificationMode = false;	
		protected JSPELParser elParser = new JSPELParser(variables,constants,aggregationFunctions);
		protected AnnotationPropagationPolicy annotationPolicy = AnnotationPropagationPolicy.ALL;
		protected boolean usesExtFactstore = false;	
		protected boolean usesInlineFacts = false;	

		//OV added on 250511 
		List<String> varLines = new ArrayList<String>();
		List<String> refLines = new ArrayList<String>();

		public AbstractTakeParser() {
			super();
			this.importedPackages.add("java.lang");
		}
		
		public List<ScriptException> check (Reader reader)  throws ScriptException {
			verificationMode = true;
			this.issues = new ArrayList<ScriptException>();
			parse(reader);
			return this.issues;
		}
		
		public KnowledgeBase parse (Reader reader) throws ScriptException  {
			kb = new DefaultKnowledgeBase();

			System.out.println("ATP enter parse = ");

			LineNumberReader bufReader = new LineNumberReader(reader);
			String line = null;
			try {
				while ((line=bufReader.readLine())!=null) {
					int no = bufReader.getLineNumber();

					System.out.println("ATP parse linenum = " + no);
					
					line = line.trim();

					System.out.println("ATP parse line = " + line);

					if (line.length()>0) {
						try {
							parseLine(line,no);	
						}
						catch (RuntimeException x) {
							this.error(no,x,"Exception parsing line");
						}
					}
				}
				
				// build queries
				buildQueries();
				
			} catch (IOException e) {
				throw new ScriptException(e);
			}	
			return kb;
		}
		
		private void parseLine(String line,int no) throws ScriptException {

			if (line.startsWith("//")) {
				// comment, don't parse
			}
			else if (line.startsWith("@@")) {
				debug("parse line "," as global annotation: ",line);
				parseGlobalAnnotation(line,no);
			}
			else if (line.startsWith("@")) {
				debug("parse line "," as local annotation: ",line);
				parseLocalAnnotation(line,no);
			}
			else if (line.startsWith("import")) {
				debug("parse line "," as import: ",line);
				parseImport(line,no);
			}
			else if (line.startsWith("var ")) {
				debug("parse line "," as var declaration: ",line);
				parseVarDeclaration(line,no);
			}
			else if (line.startsWith("ref ")) {
				debug("parse line "," as ref declaration: ",line);
				parseRefDeclaration(line,no);
			}
			else if (line.startsWith("query ")) {
				debug("parse line "," as query: ",line);
				parseQuery(line,no);
			}
			else if (line.startsWith("external ")) {
				debug("parse line "," as external fact store: ",line);
				parseExternalFactStore(line,no);
			}
			else if (line.startsWith("aggregation ")) {
				debug("parse line "," as external fact store: ",line);
				parseAggregation(line,no);
			}
			else if (RULE.matcher(line).matches()) {
				debug("parse line "," as rule: ",line);
				parseRule(line,no);
			}
			else if (FACT.matcher(line).matches()) {
				debug("parse line "," as fact: ",line);
				parseFact(line,no);
			}

			else {
				error(no,"Unable to parse this line (unknown syntax type): ",line);
			}
			
		}
		private void parseAggregation(String line, int no) throws ScriptException {
			check(no,line,AGGREGATION,"this is not a valid aggregation declaration");
			line = line.substring(12).trim(); // take off aggregation
			
			// name
			int sep = line.indexOf('='); // separate function name
			String name = line.substring(0,sep).trim();
			line = line.substring(sep+1).trim();
			AggregationFunction f = new AggregationFunction();
			f.setName(name);
			
			// kind of aggregation 
			sep = line.indexOf(' '); // separate function name
			String aggFunction = line.substring(0,sep);
			line = line.substring(sep+1).trim();
			if (aggFunction.equals("avg")) {
				f.setAggregation(nz.org.take.Aggregations.AVG);
			}
			else if (aggFunction.equals("sum")) {
				f.setAggregation(nz.org.take.Aggregations.SUM);
			}
			else if (aggFunction.equals("count")) {
				f.setAggregation(nz.org.take.Aggregations.COUNT);
			}
			else if (aggFunction.equals("min")) {
				f.setAggregation(nz.org.take.Aggregations.MIN);
			}
			else if (aggFunction.equals("max")) {
				f.setAggregation(nz.org.take.Aggregations.MAX);
			}
			else {
				error(no,"Unknown aggregation function ",aggFunction);
			}
			
			// variable
			sep = line.indexOf(' '); 
			String vname = line.substring(0,sep);
			line = line.substring(sep+1).trim();
			Variable var = this.variables.get(vname);
			if (var==null)
				this.error(no,"The variable used in an aggregation must be declared first using the statement \"var <a type> ",vname,"\"");
			f.setVariable(var);
			
			Fact query = this.parseCondition(line, no,false,false);
			
			
			Variable variable = f.getVariable();
			
			boolean checkVar = false;
			for (Term t:query.getTerms()) {
				if (t.equals(variable)) {
					checkVar = true;
				}
		//		if (t instanceof ComplexTerm) {
		//			throw new IllegalArgumentException("Complex terms in the definition of aggregations are not (yet) supported: " + this.getName());
		//		} 
			}
			
			f.setQuery(query);
			
			aggregationFunctions.put(f.getName(),f);
		}

		private void buildQueries() throws ScriptException {
			for (QuerySpec spec:this.querySpecs) {
				String id = this.getId(spec);
				Predicate p = this.predicatesByName.get(id);
				if (p==null) {
					this.error(spec.getLine(),"There is no rule or fact supporting the query predicate ",spec.getPredicate()," in the script");
				}
				else if (p.isNegated()) {
					this.error(spec.getLine(),"There is no rule or fact supporting the unnegated query predicate ",spec.getPredicate()," in the script");
				}
				else {
					Query query = new Query();
					query.setPredicate(p);
					boolean[] io = new boolean[spec.getIoSpec().size()];
					for (int i=0;i<io.length;i++) {
						io[i]=spec.getIoSpec().get(i);
					}
					query.setInputParams(io);
					query.addAnnotations(spec.getAnnotations());
					takeOverAnnotations(query);
					this.kb.add(query);
				}	
			}
		}

		private void error(int no,String line,Pattern pattern,String... description) throws ScriptException{
			StringBuffer buf = new StringBuffer();
			buf.append("Parser exception at line ");
			buf.append(no);
			buf.append(' ');
			buf.append(line);
			buf.append(" does not match pattern ");
			buf.append(pattern.pattern());
			buf.append(' ');
			for (String t:description)
				buf.append(t);
			error(no,buf.toString());
		}

		private void check(int no,String txt,Pattern pattern,String... errorMessage) throws ScriptException {
			if (!pattern.matcher(txt).matches()) 
				this.error(no,txt,pattern,errorMessage);
		}

		//changing to protected for subclassing on 24/05/2011 by OV
		protected void parseVarDeclaration(String line, int no) throws ScriptException {
			line = line.substring(4).trim();
			int sep = line.indexOf(' ');
			String type =line.substring(0,sep).trim();
			check(no,type,TYPE_NAME,"this is not a valid type name");
			Class clazz = this.classForName(type,no);
			String names=line.substring(sep+1).trim();
			String name = null;
			check(no,names,LIST_OF_NAMES,"this is not a valid (list of) name(s)");
			varLines.add(line);

			StringTokenizer t = new StringTokenizer(names,",");
			while (t.hasMoreTokens()) {
				name = t.nextToken().trim();
				Variable var = new Variable();
				var.setType(clazz);
				var.setName(name);
				this.variables.put(name,var);
			}
		}

		//changing to protected for subclassing on 24/05/2011 by OV
		protected void parseRefDeclaration(String line, int no) throws ScriptException {
			line = line.substring(4).trim();
			int sep = line.indexOf(' ');
			String type =line.substring(0,sep).trim();
			check(no,type,TYPE_NAME,"this is not a valid type name");
			Class clazz = this.classForName(type,no);
			String names=line.substring(sep+1).trim();
			String name = null;
			check(no,names,LIST_OF_NAMES,"this is not a valid (list of) name(s)");
			refLines.add(line);

			StringTokenizer t = new StringTokenizer(names,",");
			while (t.hasMoreTokens()) {
				name = t.nextToken().trim();
				Constant c = new Constant();
				c.setType(clazz);
				c.setRef(name);

			//	System.out.println(" parser const class = "+ clazz.getName());
		//		System.out.println(" parser const name = "+ name);
				this.constants.put(name, c);
			}	

			kb.setConstants(this.constants);
		}
		
		private void parseQuery(String line, int no) throws ScriptException {
			line = line.substring(6).trim();
			
			QuerySpec query = new QuerySpec();
			query.setLine(no);
			this.consumeAnnotations(query);
			
			// TODO should we support explicit negation here?
			if (line.startsWith("not ")) {
				this.error(no,"Negation is not supported here");
				//query.setNegated(true);
				//line = line.substring(4).trim();
			}

			int sep = line.indexOf('[');
			String name = line.substring(0,sep).trim();
			String sign = line.substring(sep+1,line.length()-1).trim();
			
			query.setPredicate(name);
			
			// parse i/o
			StringTokenizer tok = new StringTokenizer(sign,",");
			while (tok.hasMoreTokens()) {
				String token = tok.nextToken().trim();
				query.getIoSpec().add("in".equals(token));
			}
		
			this.querySpecs.add(query);
		}
		
		protected void parseRule(String line, int no) throws ScriptException {

			System.out.println("ATP enter parseRule = ");

			int sep = line.indexOf(":"); //take off label
			String label = line.substring(0,sep).trim();
			line = line.substring(sep);
			sep = line.indexOf("if ");
			line = line.substring(sep+3).trim();
			List<String> unparsedConditions = new ArrayList<String>();
			String unparsedHead = null;
			BitSet neg = new BitSet();
			
			StringBuffer b = new StringBuffer();
			
			List<String> mainTokens = Tokenizer.tokenize(line," then ");
			if (mainTokens.size()!=2) {
				error(no,line,null,"this is a malformed rule");
			}
			String bodyS = mainTokens.get(0);
			unparsedHead = mainTokens.get(1);

		//	System.out.println("parseRule bodyS= "+bodyS);
		//	System.out.println("parseRule unparsedHead= "+unparsedHead);

			List<String> tokens = Tokenizer.tokenize(bodyS," and ");
			
			for (int i=0;i<tokens.size();i++) {
				String token = tokens.get(i).trim();
				if (token.startsWith("not ")) {
					token = token.substring(3).trim();
					neg.set(i);
				}
				unparsedConditions.add(token);
			}
			
			DerivationRule rule = new DerivationRule();
			this.checkId(label,no);
			rule.setId(label);
			Fact head = this.parseCondition(unparsedHead,no,false,false);
			rule.setHead(head);
			for (int i=0;i<unparsedConditions.size();i++) {
				Prerequisite prereq = (Prerequisite)this.parseCondition(unparsedConditions.get(i),no,true,neg.get(i));
				rule.getBody().add(prereq);
			}
			this.consumeAnnotations(rule);
			this.kb.add(rule);
			
		}
		
		
		private void parseFact(String line, int no) throws ScriptException {
			usesInlineFacts = true;
			int sep = line.indexOf(":"); //take off label
			String label = line.substring(0,sep);
			line = line.substring(sep+1).trim();		
			Fact fact = this.parseCondition(line,no,false,false);
			this.checkId(label,no);
			// if fact contains variables, it should be added as a rule
			// TODO: this should better be done by the compiler
			boolean hasVars = false;
			for (Term t:fact.getTerms()) {
				hasVars = hasVars || (t instanceof Variable) || (t instanceof ComplexTerm);
			}
			
			if (hasVars) {
				DerivationRule rule = new DerivationRule();
				rule.setId(label);
				rule.setHead(fact);
				this.kb.add(rule);
			}
			else {
				fact.setId(label);
				this.kb.add(fact);
			}

		}
		
		private void parseImport(String line, int no) throws ScriptException {
			check(no,line,IMPORT,"this is not a valid import statement");
			line = line.substring(7).trim();
			
			// for convenience, accept ; at the end of the line
			if (line.endsWith(";")) {
				line = line.substring(0,line.length()-1).trim();
			}
			// for import, last import wins
			if (line.endsWith(".*")) {
				importedPackages.add(0,line.substring(0,line.length()-2));
			}
			else {
				importedClasses.add(0,line);
			}
			
		}
		
		private void parseGlobalAnnotation(String line, int no) throws ScriptException {
			check(no,line,GLOBAL_ANNOTATION,"this is not a valid global annotation");
			line = line.substring(2).trim();
			int sep = line.indexOf('=');
			String key=line.substring(0,sep).trim();
			String value=line.substring(sep+1).trim();
			this.kb.addAnnotation(key, value);
		}
		
		private void parseLocalAnnotation(String line, int no) throws ScriptException {
			check(no,line,LOCAL_ANNOTATION,"this is not a valid local annotation");
			line = line.substring(1).trim();
			int sep = line.indexOf('=');
			String key=line.substring(0,sep).trim();
			String value=line.substring(sep+1).trim();
		//	System.out.println("parseLocalAnnotation key= "+key);
		//	System.out.println("parseLocalAnnotation val= "+value);
			this.localAnnotations.put(key, value);
		}
		
		private void parseExternalFactStore(String line, int no) throws ScriptException {
			check(no,line,EXTERNAL,"this is not a valid external fact store");
			line = line.substring(9).trim(); // take off external
			int sep = line.indexOf(":"); //take off label
			String id = line.substring(0,sep);
			line = line.substring(sep+1).trim();
			
			usesExtFactstore = true;

			sep = line.indexOf('[');
			String name = line.substring(0,sep).trim();
			String typeNames = line.substring(sep+1,line.length()-1).trim();
			
			StringTokenizer tokenizer = new StringTokenizer(typeNames,",");
			List<Class> types = new ArrayList<Class>();
			while (tokenizer.hasMoreTokens()) {
				String typeName = tokenizer.nextToken().trim();
				check(no,typeName,TYPE_NAME,"this is not a valid type name");
				Class clazz = this.classForName(typeName,no);
				types.add(clazz);
			}
			SimplePredicate predicate = new SimplePredicate();
			predicate.setName(name);
			predicate.setSlotTypes(types.toArray(new Class[types.size()]));
			String pid = this.getId(predicate);
			Predicate existingPredicate = predicatesByName.get(pid);
			if (existingPredicate==null) {
				predicatesByName.put(pid,predicate);
			}
			else {
				predicate = (SimplePredicate)existingPredicate;
			}
			ExternalFactStore exFacts = new ExternalFactStore();
			exFacts.setId(id);
			exFacts.setPredicate(predicate);
			this.consumeAnnotations(exFacts);
			
			this.kb.add(exFacts);
		}

		private Class classForName(String type,int line) throws ScriptException {
			
			if ("char".equals(type))
				return Character.TYPE;
			if ("byte".equals(type))
				return Byte.TYPE;
			if ("int".equals(type))
				return Integer.TYPE;
			if ("short".equals(type))
				return Short.TYPE;
			if ("long".equals(type))
				return Long.TYPE;
			if ("double".equals(type))
				return Double.TYPE;
			if ("float".equals(type))
				return Float.TYPE;
			if ("boolean".equals(type))
				return Boolean.TYPE;
			
			// try whether this is a class name
			try {
				return this.classLoader.loadClass(type);
			}
			catch (ClassNotFoundException x){}
			// try whether there is a matching imported class
			String xtype = "."+type;
			for (String c:this.importedClasses) {
				if (c.endsWith(xtype)) {
					try {
						return this.classLoader.loadClass(c);
					}
					catch (Exception x){}	
				}
			}
			// try whether there is a matching imported package
			for (String p:this.importedPackages) {
				try {
					return this.classLoader.loadClass(p+'.'+type);
				}
				catch (Exception x){}	
			}
			this.error(line, "Cannot load the type ",type);
			return null;
		
		}
		
		protected Fact parseCondition (String s,int no, boolean isPrerequisite,boolean isNegated) throws ScriptException {

		//		System.out.println("parseCondition s= "+s);
			
			if (CONDITION1.matcher(s).matches()) {
				int sep = -1;
				Fact fact = isPrerequisite?new Prerequisite():new Fact();
				// type of the condition is predicate[terms]
				sep=s.indexOf('[');
				String p = s.substring(0,sep);

				
				String t = s.substring(sep+1,s.length()-1);
				List<String> unparsedTerms = Tokenizer.tokenize(t,",");
				List<Term> terms = new ArrayList<Term>();
				for (String ut:unparsedTerms) {
			//		System.out.println("unparsedTerm= "+ut);
					terms.add(parseTerm(ut,no));
				}
				Class[] types = new Class[terms.size()];
				for (int i=0;i<terms.size();i++) {
					types[i]=terms.get(i).getType();
				}

				Predicate predicate = this.buildPredicate(p, isNegated, terms.toArray(new Term[terms.size()]),no);
				fact.setPredicate(predicate);
				fact.setTerms(terms.toArray(new Term[terms.size()]));
				return fact;
			}
			else {
				if (!isPrerequisite) {
					throw new ScriptException("Error in line " + no + " - the rule head must have the following form: predicate[term_1,..,term_n]");
				}
				else {
					// parse with JUEL
					return elParser.parseCondition(s,no,isNegated);
				}
			}
		}

		protected Term parseTerm(String s,int line) throws ScriptException {
			return this.elParser.parseTerm(s, line);
		}

		
		
		private void consumeAnnotations(Annotatable a) {

		//	System.out.println("consumeAnnotations ac= "+a.getClass().getName());

			for (String gaKey:this.kb.getAnnotations().keySet()) {
				if (this.annotationPolicy.propagateAnnotation(gaKey)) {
					a.addAnnotation(gaKey,this.kb.getAnnotation(gaKey));
				}
			}
			a.addAnnotations(this.localAnnotations);

			Map<String, String> localAnnots = a.getAnnotations();
			Set loc_annots_set = localAnnots.keySet();
			Iterator iterloc = loc_annots_set.iterator();
			while(iterloc.hasNext()) {
				String annot_name = (String) iterloc.next();			
				String annot_val = (String) localAnnots.get(annot_name);			
		//		System.out.println("Annots name= "+annot_name);
		//		System.out.println("Annots val = "+annot_val);
			}

			this.localAnnotations.clear();
		}
		
		private void checkId(String id,int line) throws ScriptException {

			if (id==null || id.trim().length()==0)
				this.error(line,"element has no proper id");
			else if (ids.contains(id)) 
				this.error(line,"duplicated id ",id);
			else 
				ids.add(id);
		}
		
		// take over query annotations for the query predicate
		private void takeOverAnnotations(Query q) {
			Predicate p = q.getPredicate();

		//	System.out.println("Predicate name= "+p.getName());

			for (Entry<String,String> e:q.getAnnotations().entrySet())  {
				String key = e.getKey();
				p.addAnnotation(key,e.getValue());	
				if (AnnotationKeys.TAKE_GENERATE_SLOTS.equals(key)) {
			//		System.out.println("TAKE_GENERATE_SLOTS= "+key);

					// set slot names from annotation
					List<String> slots = new ArrayList<String>();
					for (StringTokenizer tok = new StringTokenizer(e.getValue(),",");tok.hasMoreTokens();) {
						slots.add(tok.nextToken().trim());
					}
					if (slots.size()!=p.getSlotTypes().length) {
						// TODO log warning
					}
					else {
						String[] arr = slots.toArray(new String[slots.size()]);

					//	for(int x=0;x<arr.length;x++)
					//		System.out.println("SLOTS[i]= "+arr[x]);

						if (p instanceof AbstractPredicate) {
	 		//				System.out.println("is abstractpredicate.setting slotnames ");
							((AbstractPredicate)p).setSlotNames(arr);
						}
						//else  TODO log warning
					}
				}
			}
		}
		
		protected nz.org.take.Predicate buildPredicate(String name,boolean negated, Term[] terms,int line) throws ScriptException {
			Predicate predicate = null;

		//	System.out.println("buildPredicate name= "+name);

			Method m = getMethod(name,terms);
			PropertyDescriptor property = null;
			if (m==null)
				property = getProperty(name, terms[0].getType());
			
			if (m!=null) {
				debug("Interpreting predicate symbol ",name," as Java method ",m," in line ",line);

			//	System.out.println("buildPredicate as Java method= ");

				JPredicate p = new JPredicate();
				Class[] paramTypes = getParamTypes( terms);
				p.setMethod(m);
				p.setNegated(negated);
				predicate=p;			
			}
			else if (property!=null) {
				debug("Interpreting predicate symbol ",name," as bean property"," in line ",line);

			//	System.out.println("buildPredicate as bean property= ");

				PropertyPredicate p = new PropertyPredicate();
				p.setProperty(property);
				p.setOwnerType(terms[0].getType());
				p.setNegated(negated);
				// todo remove this line that just does lazy initialization
				predicate=p;			
			}
			else {
				debug("Interpreting predicate symbol ",name," as simple predicate"," in line ",line);

			//	System.out.println("buildPredicate as simple predicate= ");
				SimplePredicate p = new SimplePredicate();
				p.setName(name);
				p.setNegated(negated);
				Class[] types = new Class[terms.length];
				for (int i=0;i<terms.length;i++) { 
					types[i] = terms[i].getType();			
				}
				p.setSlotTypes(types);
				predicate=p;
			}
			String id = this.getId(predicate);
			Predicate existingPredicate = predicatesByName.get(id);
			if (existingPredicate==null) {
				predicatesByName.put(this.getId(predicate),predicate);
				return predicate;
			}
			else
				return existingPredicate;
		}
		
		protected Class[] getParamTypes(nz.org.take.Term[] terms) {
			Class[] paramTypes = new Class[terms.length-1];
			for (int i=1;i<terms.length;i++) {
				paramTypes[i-1]=terms[i].getType();
			}
			return paramTypes;
		}
		
		protected Method getMethod(String name,nz.org.take.Term[] terms) throws ScriptException  {		
			Class clazz = terms[0].getType();

			Class[] paramTypes = new Class[terms.length-1];
			Method m = null;
			for (int i=1;i<terms.length;i++) {
				paramTypes[i-1]=terms[i].getType();
			}

		
			try {
				m = clazz.getMethod(name,paramTypes);
			}
			catch (Exception x) {}
			
			if (m==null) {
				// start investigating supertypes

				Method[] methods = clazz.getMethods();
				for (Method m1:methods) {
					if (m1.getReturnType()==Boolean.TYPE && Modifier.isPublic(m1.getModifiers())) {
						if (m1.getName().equals(name) && m1.getParameterTypes().length==paramTypes.length) {
							// check types
							boolean ok = true;
							for (int i=0;i<paramTypes.length;i++) {
								ok = ok && m1.getParameterTypes()[i].isAssignableFrom(paramTypes[i]);
							}
							if (ok){
								m = m1;
								break;
							}
						}
					}
				}
			}
			return m;
			
		}
		
		protected PropertyDescriptor getProperty(String name,Class clazz) throws ScriptException  {
			return PropertyFinder.findProperty(clazz,name);
		}
		
		protected String getId(Predicate p) {
			return p.getName()+'_'+p.getSlotTypes().length+(p.isNegated()?"-":"+");
		}
		private String getId(QuerySpec q) {
			return q.getPredicate()+'_'+q.getIoSpec().size()+"+";
		}
		
		protected void error(int no, String message) throws ScriptException {
			if (this.verificationMode) {
				this.issues.add(new ScriptException(message,no));
			}
			else {
				super.error(no, message);
			}
		}
		
		protected void error(int no, Exception x, String message) throws ScriptException {
			if (this.verificationMode) {
				this.issues.add(new ScriptException(message,x,no));
			}
			else {
				super.error(no, x, message);
			}
		}

		public ClassLoader getClassLoader() {
			return classLoader;
		}

		public void setClassLoader(ClassLoader classloader) {
			this.classLoader = classloader;
		}

		public AnnotationPropagationPolicy getAnnotationPolicy() {
			return annotationPolicy;
		}

		public void setAnnotationPolicy(AnnotationPropagationPolicy annotationPolicy) {
			this.annotationPolicy = annotationPolicy;
		}

		// OV added getters 25122010

		public Map getVariables() {	
			return variables;
		}

		public Map getConstants() {	
			return constants;
		}

		public Map getAggregationFunctions() {	
			return aggregationFunctions;
		}

		public Map getLocalAnnotations() {	
			return localAnnotations;
		}

		public List getQuerySpecs() {	
			return querySpecs;
		}

		public Map getPredicatesByName() {	
			return predicatesByName;
		}

		public Map getPredicates() {	
			return predicates;
		}

		public List getImportedClasses() {	
			return importedClasses;
		}

		public List getImportedPackages() {	
			return importedPackages;
		}

		public List getIssues() {	
			return issues;
		}

		public boolean getUsesExtFactstore() {	
			return usesExtFactstore;
		}

		public boolean getUsesInlineFacts() {	
			return usesInlineFacts;
		}

		public List<String> getVariableDecls() {	
			return varLines;
		}

		
		public List<String> getRefDecls() {	
			return refLines;
		}


	}

