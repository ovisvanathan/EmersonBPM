package com.emerson.bpm.dsl;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.emerson.bpm.api.FactHandle;
import com.emerson.bpm.api.Session;
import com.emerson.bpm.api.Topology;
import com.emerson.bpm.dsl.client.ClientReteBuilder;
import com.emerson.bpm.dsl.util.NetworkBuilder;
import com.emerson.bpm.dsl.util.TakeBuilder;
import com.emerson.bpm.dsl.util.TakeParser;
import com.emerson.bpm.engine.EmersonSession;
import com.emerson.bpm.engine.SessionState;
import com.emerson.bpm.model.Person;
import com.emerson.bpm.nodes.react.ObjectTypeNode;
import com.emerson.bpm.nodes.react.PrototypeMap;
import com.emerson.bpm.nodes.rel.RelationNode;

import junit.framework.TestCase;
import nz.org.take.KnowledgeBase;
import nz.org.take.TakeException;
import nz.org.take.deployment.KnowledgeBaseManager;
import nz.org.take.nscript.Parser;
import nz.org.take.nscript.ScriptException;
import nz.org.take.nscript.ScriptKnowledgeSource;


public class RuleClientTest  extends TestCase {

	Topology rete;
	
	private String [] uservFiles = {
		"DriverCategory.rel",
		"IsEligible.rel",
		"AdditionalDriverPremium.rel",
		"AdditionalPremium.rel",
		"AutoEligibility.rel",
		"BasePremium.rel",
		"HasTrainingCertification.rel",
		"InsuranceEligibility.rel",
		"isHighRiskDriver.rel",
		"IsLongTermClient.rel",
		"IsNew.rel",
		"IsSpecialLocation.rel",
		"PolicyEligibilityScore.rel",
		"PotentialOccupantInjuryRating.rel",
		"PotentialTheftRating.rel",
		"PremiumDiscount.rel",
	};
		
	
	public RuleClientTest() {
		
	}
	
	public static void main(String [] args) {
		
		RuleClientTest test = new RuleClientTest();
		
	//	test.buildNetworkUservTest();

	//	test.takeParserVariableTest();

		testPC();
	}
	
	interface A {
		
		Object divBy10(Object x);
	}
	
	static interface D extends A {
		
	//	int divBy10(int x);
		
	}
	
	static class Dix implements D {

		@Override
		public Object divBy10(Object x) {
			double d = ((int)x)/10;
			System.out.println("d = " + d);
			return 0;
		}
		
	}
	
	static void testPC() {

		A a = new Dix();
		
		a.divBy10(100);
		
	}
	
	
	public Topology buildNetwork() {

		Session session = EmersonSession.begin(SessionState.StatefulSession);
		
		Topology rete = session.getWorkingMemory().getRete();
		
		NetworkBuilder builder = ClientReteBuilder.builder2("first rule", rete);		

	//	Ontop(A, B) -> A [>+] B;
		
		PrototypeMap A, B, C;
		
		A = new PrototypeMap(105, "A");
		
		B = new PrototypeMap(96, "B");

		C = new PrototypeMap(74, "C");
				
		Relation rel = new Relation("Ontop", 2, "${A} > ${B}", true);
		
		RelationNode ontopCB = new RelationNode(rel, C, B);

		RelationNode ontopBA = new RelationNode(rel, B, A);

		RelationNode ontopAC = new RelationNode(rel, A, C);

		builder.newRelation("Ontop", 2, "${A} > ${B}", true)
			.actor("A", A)
			.actor("B", B)
			.actor("C", C)
			.fact(ontopCB)
			.fact(ontopBA)
			.stopWhen("Ontop" , "A", "C")
			.build();
				
		return rete;		
	}

	public Topology buildNetworkNoActors() {

		Session session = EmersonSession.begin(SessionState.StatefulSession);
		
		Topology rete = session.getWorkingMemory().getRete();
		
		NetworkBuilder builder = ClientReteBuilder.builder2("first rule", rete);		

	//	Ontop(A, B) -> A [>+] B;
				
		Relation rel = new Relation("Ontop", 2, "${A} > ${B}", true);
		
		builder.newRelation("Ontop", 2, "${A} > ${B}", true)
			.stopWhen("Ontop", "A", "C")
			.build();
				
		return rete;		
	}

//	@Test
	public void executeNetworkNoInputTest() {

		buildNetwork();
		rete.connect();
		
	}

//	@Test
	public void executeNetworkSingleTest() {
		
		buildNetworkNoActors();

		PrototypeMap A, B, C;

		A = new PrototypeMap(105, "A");
		
		B = new PrototypeMap(96, "B");

		C = new PrototypeMap(74, "C");
		
		rete.assertFact(new ObjectTypeNode(A));		
		rete.assertFact(new ObjectTypeNode(B));		
		rete.assertFact(new ObjectTypeNode(C));		

		
	}

//	@Test
	public void executeNetworkAllTest() {

		List<ObjectTypeNode> nodes = new ArrayList();

		PrototypeMap A, B, C;

		A = new PrototypeMap(105, "A");
		
		B = new PrototypeMap(96, "B");

		C = new PrototypeMap(74, "C");

		nodes.add(new ObjectTypeNode(A));
		nodes.add(new ObjectTypeNode(B));
		nodes.add(new ObjectTypeNode(C));

		for(FactHandle af : nodes)
			rete.assertFact(af);
		
		
	}

	public Topology buildNetworkFamily() {

		Session session = EmersonSession.begin(SessionState.StatefulSession);
		
		Topology rete = session.getWorkingMemory().getRete();
		
		NetworkBuilder builder = ClientReteBuilder.builder2("first rule", rete);		

	//	Ontop(A, B) -> A [>+] B;
		
		Person A, B, C, D, E, R, X;
		
		A = new Person(1, "kospek", 24);
		
		B = new Person(24, "Groyner", 32);

		C = new Person(21, "Wemby", 56);
		
		D = new Person(32, "Lasko", 91);

		E = new Person(15, "Bleeko", 34);

		R = new Person(34, "Melda", 47);

		X = new Person(47, "Zober", 102);

		Relation rel = new Relation("isFatherOF", 2, "a.fatherId == b.id", true);

		
		Relation rel2 = new Relation("isGrandFatherOF", rel);
		// 		Relation rel2 = new CompoundRelation(
		//					"isExtremelyWealthyGrandFatherOF", "gf.assets > 1000000", rel);

		
	//	RelationNode ontopCB = new CMPRelationNode(rel);

		builder.newCMPRelation(rel2)
			.fact(rel.getName(), A, B, C, D, E, R, X)
			.stopWhen("Ontop" , "A", "C")
			.build();
				
		return rete;		
	}

	
	@Test
	public void takeParserVariableTest() {
		
		KnowledgeBase kb = null;
		try {
	
				String s = "ref int x\n" + 
						"ref int y\n" + 
						"query whichIsGreater[in, in, out]\n" +						
						"AE_PTC01: if x > y then whichIsGreater[x, y, x]\n" +
						"AE_PTC02: if y > x then whichIsGreater[x, y, y]\n";						
				
				
					StringReader sr = new StringReader(s);
						
					Parser p = new Parser();
					
					kb = p.parse(sr);

			//		Map<K, V> bindvars = new HashMap();
					
					
			//		bindvars.put("x", 4);
					
			//		bindvars.put("y", 5);
					
			//		kb.setConstants(bindvars);

					String jcp = System.getProperty("java.class.path");
					
					
					try {
						GenerateStandaloneClass gms = new GenerateStandaloneClass();
						
						gms.generate("GTE.take", "C:\\Users\\Omprakash\\workspace7\\EmersonBPM\\bpm-dsl\\takeWorkingDir\\src");
						
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					

					/*
					KnowledgeBaseManager<GTX> kbm = new KnowledgeBaseManager<GTX>(
							"C:\\Users\\Omprakash\\workspace7\\EmersonBPM\\bpm-dsl\\bin\\main;.;" + jcp);
					
					kbm.setProjectName("vars");

					kbm.setRulesName("varscheck");

					kbm.setBaseDir("C:\\Users\\Omprakash\\workspace7\\EmersonBPM\\bpm-dsl");
					
					
					GTX KB = kbm.getKnowledgeBase(
							whichIsGreater.class, 
							new ScriptKnowledgeSource(
									new ByteArrayInputStream(s.getBytes())),
							null);
					

					System.out.println(" whichIsGreater = " + KB.whichIsGreater(5, 10));
					*/	
					
		} catch (ScriptException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TakeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	
	@Test
	public Topology buildNetworkUservTest() {

		Session session = EmersonSession.begin(SessionState.StatefulSession);
		
		Topology rete = session.getWorkingMemory().getRete();

		/*
		try {
			
			for(String fname : uservFiles) {
				
				URL file = this.getClass().getResource("resources/userv/" + fname);
				
				System.out.println();
				org.mandarax.compiler.Compiler compiler = new DefaultCompiler();
						Location location = new FileSystemLocation(new File("output_folder"));
						compiler.compile(location, CompilationMode.RELATIONSHIP_TYPES, file);
						compiler.compile(location, CompilationMode.QUERIES, file);
					
			}
		} catch (MandaraxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
	
		InputStream is = null;
		try {
			
				is = this.getClass().getResourceAsStream("/userv.take");
									
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		KnowledgeBase kb = null;
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(
					new File("C:\\Users\\Omprakash\\workspace7\\EmersonBPM\\bpm-dsl\\bin\\main\\userv.take"))));
	
			Parser p = new Parser();
			
			TakeParser takeparser = new TakeParser();
						
	//		kb = takeparser.parse(reader);

			kb = p.parse(reader);

			TakeBuilder builder = new TakeBuilder(kb, takeparser);
		
			builder.build();
			
		} catch (ScriptException | FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*
	
		NetworkBuilder builder = ClientReteBuilder.builder2("first rule", rete);		

	//	Ontop(A, B) -> A [>+] B;
		
		Person A, B, C, D, E, R, X;
		
		A = new Person(1, "kospek", 24);
		
		B = new Person(24, "Groyner", 32);

		C = new Person(21, "Wemby", 56);
		
		D = new Person(32, "Lasko", 91);

		E = new Person(15, "Bleeko", 34);

		R = new Person(34, "Melda", 47);

		X = new Person(47, "Zober", 102);

		Relation rel = new Relation("isFatherOF", 2, "a.fatherId == b.id", true);

		
		Relation rel2 = new Relation("isGrandFatherOF", rel);
		// 		Relation rel2 = new CompoundRelation(
		//					"isExtremelyWealthyGrandFatherOF", "gf.assets > 1000000", rel);

		
	//	RelationNode ontopCB = new CMPRelationNode(rel);

		builder.newCMPRelation(rel2)
			.fact(rel.getName(), A, B, C, D, E, R, X)
			.stopWhen("Ontop" , "A", "C")
			.build();
			
		*/
		
		return rete;		
	}

}
