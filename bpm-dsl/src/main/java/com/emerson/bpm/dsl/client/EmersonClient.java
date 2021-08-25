package com.emerson.bpm.dsl.client;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.function.Function;

import com.emerson.bpm.api.COMPARATOR;
import com.emerson.bpm.api.SDKNode;
import com.emerson.bpm.api.Session;
import com.emerson.bpm.api.Topology;
import com.emerson.bpm.api.WorkingMemory;
import com.emerson.bpm.model.Account;
import com.emerson.bpm.model.Address;
import com.emerson.bpm.model.Bank;
import com.emerson.bpm.model.Branch;
import com.emerson.bpm.model.CrimCriteria;
import com.emerson.bpm.model.LoanApplication;
import com.emerson.bpm.model.LoanProduct;
import com.emerson.bpm.model.Person;
import com.emerson.bpm.model.TxnCriteria;
import com.emerson.bpm.nodes.AlphaNode;
import com.emerson.bpm.nodes.JoinNode;
import com.emerson.bpm.nodes.RTMNode;
import com.emerson.bpm.nodes.cmp.AlphaNodeComparator;
import com.emerson.bpm.nodes.cmp.JoinNodeComparator;
import com.emerson.bpm.nodes.match.DoesNotExistsComparator;
import com.emerson.bpm.nodes.match.FieldCriteriaComparator;
import com.emerson.bpm.nodes.match.FieldJoinComparator;
import com.emerson.bpm.nodes.match.FieldListValueComparator;
import com.emerson.bpm.nodes.match.FieldNameComparator;
import com.emerson.bpm.nodes.match.FieldValueComparator;
import com.emerson.bpm.nodes.react.ObjectTypeNode;
import com.emerson.bpm.util.Duration;
import com.emerson.bpm.util.EmersonUtils;
import com.emerson.bpm.util.ServiceFactory;

import io.github.benas.jpopulator.api.Populator;
import io.github.benas.jpopulator.impl.PopulatorBuilder;

public class EmersonClient<T> {

	Session sess;

	Person person;
	
	Bank bank;

	Branch branch;
			 
	Account account;
	
	LoanProduct loanProduct;
	
	LoanApplication la8;
	
	TxnCriteria txncrit;

	//name
	Function<Integer, Integer> net1 = (r) -> { 
		buildNetwork1();
		return 0;
	};

	//addr
	Function<Integer, Integer> net2 = (a) -> { 
		buildNetwork2();		
		return 0;
	};

	// both
	Function<Integer, Integer> net3 = (a) -> { 
		buildNetwork1();
		buildNetwork2();
		return 0;		
	};
	
	// callback
	Function<SDKNode, Integer> net4 = (a) -> { 
		EmersonUtils.printResult(a);
		return 1;
	};

	public class ClientBuilder  {

		public void buildNetwork() {			

			Topology Topology = sess.getWorkingMemory().getRete();

			ObjectTypeNode otnodePers = new ObjectTypeNode(Person.class);
			
			// rule 1
			//no cpmparison. just specify COMPARAOR.EXISTS
			AlphaNode p1 = new AlphaNode(Person.class);

			otnodePers.addTupleSink(p1);

			ObjectTypeNode otnodeAcct = new ObjectTypeNode(Account.class);

			AlphaNode a1 = new AlphaNode(Account.class);

			otnodeAcct.addTupleSink(a1);
						
			JoinNode pacjoin = new JoinNode(p1, a1, new FieldNameComparator("name", "custName"));

			p1.addTupleSink(pacjoin);
			a1.addTupleSink(pacjoin);

			ObjectTypeNode otnodeBank = new ObjectTypeNode(Bank.class);
			
			AlphaNode p2 = new AlphaNode(Bank.class);
			
			otnodeBank.addTupleSink(p2);
			
			JoinNode bck = new JoinNode(p2, pacjoin, new FieldNameComparator("bankName", "name"));
			
			pacjoin.addTupleSink(bck);
			p2.addTupleSink(bck);
			
			ObjectTypeNode otnodeLP = new ObjectTypeNode(LoanProduct.class);
			
			AlphaNode lp3 = new AlphaNode(LoanProduct.class);

			otnodeLP.addTupleSink(lp3);
			
			JoinNode blip2 = new JoinNode(p2, lp3, new FieldListValueComparator("products", "loanProduct.name"));

			p2.addTupleSink(blip2);
			lp3.addTupleSink(blip2);

			ObjectTypeNode otnodeLA = new ObjectTypeNode(LoanApplication.class);
			
			AlphaNode la8i = new AlphaNode(LoanApplication.class);

			otnodeLA.addTupleSink(la8i);
			
			JoinNode pla4= new JoinNode(bck, la8i, new FieldNameComparator("name", "custName"));
			
			bck.addTupleSink(pla4);
			la8i.addTupleSink(pla4);			

			//rule 2	
			AlphaNode ps11 = new AlphaNode(new FieldValueComparator("score", 700, COMPARATOR.GT));
			
			otnodePers.addTupleSink(ps11);
			
			JoinNode psc26= new JoinNode(pla4, ps11, new FieldNameComparator("custName", "name"));

			pla4.addTupleSink(psc26);
			ps11.addTupleSink(psc26);
				
			//rule 3
			ObjectTypeNode otnodeTxn = new ObjectTypeNode(TxnCriteria.class);
			
			AlphaNode tcx1 = new AlphaNode(new FieldCriteriaComparator(new Object [] { "name", "latif", "acnum", "1x27a" }));

			otnodeTxn.addTupleSink(tcx1);
			
			JoinNode pstx1 = new JoinNode(pla4, tcx1, new FieldNameComparator("accnum", "accountNum"));
						
			pla4.addTupleSink(pstx1);
			
			tcx1.addTupleSink(pstx1);
						
			JoinNode pmct45 = new JoinNode(psc26, pstx1, new FieldNameComparator("accnum", "accountNum"));
			
			psc26.addTupleSink(pmct45);
			pstx1.addTupleSink(pmct45);

			//rule4 not
			ObjectTypeNode otnodeCrim = new ObjectTypeNode(CrimCriteria.class);
			
			AlphaNode cc2 = new AlphaNode(CrimCriteria.class);

			otnodeCrim.addTupleSink(cc2);
			
			JoinNode lacc8 = new JoinNode(pla4, cc2, new DoesNotExistsComparator(cc2, "lap.custName", "person.name"));			
			
			pla4.addTupleSink(lacc8);
	//		lacc8.setConstraint(new EmptyBetaConstraints());
			
			
			//rule5
			AlphaNode lala5 = new AlphaNode("amount", 10000000, COMPARATOR.GT);

			pla4.addTupleSink(lala5);
			
			JoinNode lpdc4 = new JoinNode(lala5, p2, 
					new FieldJoinComparator(
							new String [] { "branchCode", "mainBranchCode", "bankName", "name"}, 
							new Object [] { "custName", COMPARATOR.EQUALS, "simba" }
					));
			

			lala5.addTupleSink(lpdc4);
			p2.addTupleSink(lpdc4);

			JoinNode ymct6 = new JoinNode(lacc8, lpdc4);

			lacc8.addTupleSink(ymct6);
			lpdc4.addTupleSink(ymct6);


			JoinNode rxbts7 = new JoinNode(pmct45, ymct6);
			
			
			pmct45.addTupleSink(rxbts7);
			ymct6.addTupleSink(rxbts7);
			
			RTMNode rtmn1 = new RTMNode("Loan Eligibility ");
			
			rxbts7.addTupleSink(rtmn1);
			
			Topology.build();
			
			
		}
	}


	/*
	 * The Builder of the Topology network for testing purposes
	 * For user testing extend ClientTopologyBuilder
	 *  - uses Client API for builder
	 * for System testing extends TopologyBuilder
	 */
	public class EmersonClientBuilder<T> extends ClientReteBuilder<T> {
		T arg0;
		T arg1;

		public EmersonClientBuilder(T arg0) {
			this.arg0 = arg0;
		}

		public EmersonClientBuilder(T arg0, T arg1) {
			this.arg0 = arg0;
			this.arg1 = arg1;			
		}
		
		public T getArg() { return arg0; }

		public void reset() {
			
		}

		public void clientBuildNetwork(Function arg) {			
			arg.andThen((Function) getCB());
		}

		public T getCB() {
			return this.arg1;
		}

		
	}
	
	private EmersonClientBuilder<T> newMCBuilder(T arg, T arg2) {
		return new EmersonClientBuilder<T>(arg, arg2);
	}
	
	public EmersonClient() {
	
	//	sess = MonarchSession.begin(new EmersonClientBuilder((T) net1));

		sess = ServiceFactory.getSession();
		
		WorkingMemory wm = sess.getWorkingMemory();

	//	Person p = new Person("naka");
		
	//	wm.insert(p);
		
		/*
		wm.getTopology().assertFact(new Person("abu"), newMCBuilder((T) net1, (T) net4));
		
		int fired = wm.fireAllRules();
	
		assertEquals(0, fired);

		wm.getTopology().assertFact(new Person("barso"));
		
		fired = wm.fireAllRules();
	
		assertEquals(1, fired);
	
		wm.getTopology().reset();		
				
		wm.getTopology().assertFact(new Person("barso"), newMCBuilder((T) net2, (T) net4));
		
		fired = wm.fireAllRules();
		 
		assertEquals(0, fired);
	 
		wm.getTopology().assertFact(new Address("Tel Aviv"));
		
		 fired = wm.fireAllRules();

		 assertEquals(0, fired);

 		 wm.getTopology().assertFact(new Address("Prague"));
		
		 fired = wm.fireAllRules();

		 assertEquals(1, fired);
		 
		wm.getTopology().assertFact(new Person("barso"), newMCBuilder((T) net3, (T) net4));
	
		*/
		
		try {
			buildNetwork3();
			
			insertFacts(wm);
			
			int fired = wm.fireAllRules();
 
			assertEquals(1, fired);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		
	}
	
	public static void main(String[] args) {
		EmersonClient mc = new EmersonClient();
	}

	public void buildNetwork1() {

		Topology Topology = sess.getWorkingMemory().getRete();
		
	//	AlphaNodeComparator anc1 = new AlphaNodeComparator(rete,
	//			Person.class, COMPARATOR.EQUALS, "name",  "Barso");

	//	Topology.add(anc1);				 
		
		Topology.build();
	}

	public void buildNetwork2() {

		Topology Topology = sess.getWorkingMemory().getRete();

	//	createModels();
		
		/*
		Topology.add(
				new KeyTuple(new Person("Barso"), "name", COMPARATOR.EQUALS, "Bob"));
		
		Topology.add(
				new KeyTuple(new Address("Paris"), "city", COMPARATOR.EQUALS, "Paris"));
		*/
		
		/*
		AlphaNodeComparator anc1 = new AlphaNodeComparator(rete,
				Person.class, COMPARATOR.EQUALS, "name",  "Barso");

		AlphaNodeComparator anc2 = new AlphaNodeComparator(rete,
				Address.class, COMPARATOR.EQUALS, "city", "Prague");
		*/
		
	//	JoinNodeComparator jnc = new JoinNodeComparator(anc1, anc2);

	//	anc1.addTupleSink(jnc);
	//	anc2.addTupleSink(jnc);
		
	//	RTMNode rtm = new RTMNode("When name matches fire");
		
	//	jnc.addTupleSink(rtm);
		
	//	Topology.build(rtm);
		
	}

	
	/*
	 * Person()
	 * Bank()
	 * Account()
	 * LoanProduct()
	 * LoanApplication()
	 * account.bank=bank.name
	 * then
	 * LoanApplication.status = submitted
	 * 
	 * person.account.accountType== current
	 * then 
	 * AvailProducts=='bizloan', 'wcaploan", 'shorttermloan', LAD 
	 * 
	 * person.account.accountType== savings
	 * then 
	 * AvailProducts=='personalloan' 
	 * 
	 * person.creditrating.name == creditrating.agencyname
	 * and
	 * person.score > 700
	 * then
	 * LoanApplication.eligibility=eligible
	 * 
	 * 
	 * person.account.status == OPEN|GOOD_CONDITION
	 * then
	 * LoanApplication.status = submitted
	 * 
	 * person.account.numTxns > 10
	 * and
	 * avg.Txn.value == 100
	 * then
	 * LoanApplication.status = submitted
	 * 
	 * 
	 * LoanApplication.amount < 100000
	 * bank.name=xyz
	 * branch.name=uvw
	 * account.branch=branch
	 * then
	 * LoanApplication.status = submitted
	 * 
	 * LoanApplication.amount > 100000
	 * bank.name=xyz
	 * branch.name=uvw
	 * account.branch=branch
	 * then
	 * LoanApplication.status = 'refer to main branch'
	 * 
	 * 

	 * Person 
	 * Bank
	 * Account
	 * LoanProduct
	 * LoanAppln
	 * 
	 * Person
	 * CreditRating
	 * Bank
	 * LoanApplication
	 * 
	 * 
	 * LoanApplication
	 * Bank
	 * Branch
	 * Account
	 * Person
	 * 
	 * 
	 * Person
	 * Account
	 * 
	 * Person
	 * Account
	 * Txn
	 * LoanApplication
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	private void insertFacts(WorkingMemory wm) {

		Populator populator = new PopulatorBuilder().build();
		
		Person person = populator.populateBean(Person.class);
				
		Address address1 = new Address("100 ravine st", "", "Delhi", "Delhi", "000001");

		Address address2 = new Address("21 Jose st", "", "Bogota", "Colombia", "996654");

		Address address3 = new Address("36 chowringee lane", "", "Kolkata", "Kolkata", "121417");

		Address address4 = new Address("200 park place ave", "", "Mumbai", "Mumbai", "260601");

		Address address5 = new Address("96 Ave della rosa", "", "Valencia", "Espania", "37MH04");

		Address address6 = new Address("10 Falfel st", "", "Beirut", "Lebanon", "ACH216");

		Address address7 = new Address("176 Wilshire blvd", "", "Los Angeles", "California", "92614");

		Address address8 = new Address("11 Market st", "", "San Francisco", "California", "98762");

		Address address9 = new Address("200 Mass Ave", "", "DC", "DC", "12129");

		
		
		Address address21 = new Address("505 king st", "", "London", "London", "LCH21A");

		Address address22 = new Address("221 Baker st", "", "London", "London", "MNRA6T");
		
		Address address23 = new Address("121 Park st", "", "London", "London", "ARN29D");

		Address address24 = new Address("55 Moore st", "", "London", "London", "ICM34P");

		Address address25 = new Address("221 Bond st", "", "London", "London", "DRS55A");
		
		Address address26 = new Address("61A Post st", "", "London", "London", "UDR33E");

		Address address27 = new Address("11 Lexington ave", "", "New York", "New York", "11010");
		
		Address address28 = new Address("55th st Amsterdam ave", "", "New York", "New York", "11024");
		Address address29 = new Address("31 Broad st", "", "New York", "New York", "110104");
		Address address30 = new Address("12th and Broadway st", "", "New York", "New York", "110005");
		Address address31 = new Address("33rd & Fifth ave", "", "New York", "New York", "110001");

		Address address32 = new Address("117 Avery st", "", "London", "London", "CS213E");
		Address address33 = new Address("12 Flower st", "", "London", "London", "KM2E3H");
		
		Address address34 = new Address("15 Puck st", "", "London", "London", "RMH21E");
		Address address35 = new Address("155 Gustav st", "", "London", "London", "ORN12L");
		Address address36 = new Address("3144 Feather st", "", "London", "London", "M321E");
		Address address37 = new Address("10 Downing st", "", "London", "London", "RMS2E7");
		Address address38 = new Address("321 Poplar ave", "", "London", "London", "CMH21S");
		Address address39 = new Address("44 Pike st", "", "London", "London", "ESW28T");
		
		Person ajib = new Person("Ajib", address1);

		Person amal = new Person("Amal", address2);
		
		Person rahej = new Person("Rahej", address3);

		Person sabah = new Person("Sabah", address4);

		Person latif = new Person("Latif", address5);

		Person mojaf = new Person("Mojaf", address6);

		Person demir = new Person("Demir", address7);

		Person tozab = new Person("Tozab", address8);

		Person wabin = new Person("Wabin", address9);

		
		
		Person muran = new Person("Muran", address21);

		Person adnan = new Person("Adnan", address22);
		
		Person wadir = new Person("Wadir", address23);

		Person jisha = new Person("Jisha", address24);

		Person laksa = new Person("Laksa", address25);

		Person remo = new Person("Remo", address26);

		Person rathi = new Person("Rathi", address27);

		Person ulban = new Person("Ulban", address28);

		Person ronak = new Person("Ronak", address29);

		Person romak = new Person("Romak", address30);

		Person baskar = new Person("Baskar", address31);

		Person vastav = new Person("Vastav", address32);
		
		Person nadir = new Person("Nadir", address33);

		Person sikha = new Person("Sikha", address34);

		Person mannu = new Person("Mannu", address35);

		Person poski = new Person("Poski", address36);

		Person posat = new Person("Posat", address37);

		Person rampal = new Person("Rampal", address38);

		Person erwan = new Person("Erwan", address39);
		
		Person rakun = new Person("rakun", address24);
		
		
		Bank icici = new Bank("ICICI Bank");
		icici.setMainBranchCode("ICIC0061227");

		Branch iciciAdyar = new Branch("ICICI Bank", "ICIC0000128", "Adyar");

		Branch iciciCenot = new Branch("ICICI Bank", "ICIC0061227", "Cenotaph Rd");

		Branch iciciNung = new Branch("ICICI Bank", "ICIC0000552", "Nungambakkam");

		Branch iciciPalki = new Branch("ICICI Bank", "ICIC0000617", "Palavakkam");

		Branch iciciVals = new Branch("ICICI Bank", "ICIC0000912", "Valsaravakkam");
		
		Bank hdfc = new Bank("HDFC Bank");
		hdfc.setMainBranchCode("RAPU54");
		
		Branch hdfcRapu = new Branch("HDFC Bank", "HDFC0000148", "R.A.Puram");

		Branch hdfcAdyar = new Branch("HDFC Bank", "HDFC0000121", "Adyar");

		Branch hdfcAnna = new Branch("HDFC Bank", "HDFC0000255", "Anna Nagar");
		
		Branch hdfcMyla = new Branch("HDFC Bank", "HDFC0000095", "Mylapore");

		Bank yesb = new Bank("Yes Bank");
		yesb.setMainBranchCode("YESRA1");

		Branch yesbRapu = new Branch("Yes Bank", "YESB0000657", "R.A.Puram");
		Branch yesbNung = new Branch("Yes Bank", "YESB0000742", "Nungambakkam");
		
		Bank canara = new Bank("Canara Bank");
		canara.setMainBranchCode("CANSB2");

		Branch canabi = new Branch("Canara Bank", "CANB0000112", "R.A.Puram");
				
		Bank sbin = new Bank("Stet Bank");
		sbin.setMainBranchCode("SBIN04");
		
		Branch sbiSilu = new Branch("State Bank of India", "SBIN0002255", "Siliguri");

		Branch sbiRai = new Branch("State Bank of India", "SBIN0003121", "Raigarg");

		Branch sbiJam = new Branch("State Bank of India", "SBIN0002255", "Jamnagar");
		
		Branch sbiSalem = new Branch("State Bank of India", "SBIN0001095", "Salem");
		
		Branch sbiJai = new Branch("State Bank of India", "SBIN0001199", "Jaipur");

		Branch sbiKarol = new Branch("State Bank of India", "SBIN0006154", "Karol Bah");

		Branch sbiSaket = new Branch("State Bank of India", "SBIN0002137", "Saket");
		
		Branch sbiConn = new Branch("State Bank of India", "SBIN0008608", "Connaught place");

		Branch sbiVirudu = new Branch("State Bank of India", "SBIN0001122", "Virudunagar");

		Branch sbiNellai = new Branch("State Bank of India", "SBIN0001298", "Nellai");

		Branch sbiGuwa = new Branch("State Bank of India", "SBIN0002134", "Guwahati");
		
		Branch sbiMarol = new Branch("State Bank of India", "SBIN0008675", "Marol");
		
		Branch sbiColaba = new Branch("State Bank of India", "SBIN0009120", "Colaba");

		Branch sbiChembur = new Branch("State Bank of India", "SBIN0000008", "Chembur");

		Branch sbiThane = new Branch("State Bank of India", "SBIN0000035", "Thane");
		
		Branch sbiLok = new Branch("State Bank of India", "SBIN0002608", "Lokandwala");
		
		Bank kotak = new Bank("Kotak Bank");
		kotak.setMainBranchCode("KOTA96");
		
		Bank idbi = new Bank("IDBI Bank");
		idbi.setMainBranchCode("IDBI44");
		
		Bank banbar = new Bank("Bank of Baroda");
		banbar.setMainBranchCode("BANB11");
		
		Bank banind = new Bank("Bank of India");
		banind.setMainBranchCode("BANI25");
		
		Bank axis = new Bank("Axis Bank");
		axis.setMainBranchCode("AXIS98");
		
			
		Bank bank = populator.populateBean(Bank.class);

		Branch branch = populator.populateBean(Branch.class);
				 
		Account account = populator.populateBean(Account.class);


		Account acct1 = new Account("ICICI Bank", "ICIC0000552", "21657", ajib.name, 100.24);

		Account acct2 = new Account("ICICI Bank", "ICIC0000128", "86508", mojaf.name, 900.44);
		Account acct3 = new Account("ICICI Bank", "ICIC0000617", "91622", rahej.name, 2000.28);
		Account acct4 = new Account("ICICI Bank", "ICIC0000912", "12947", latif.name, 141.36);
		
		Account acct5 = new Account("HDFC Bank", "HDFC0000148", "13799", tozab.name, 299.13);
		Account acct6 = new Account("HDFC Bank", "HDFC0000148", "28652", demir.name, 1299.37);
		Account acct7 = new Account("HDFC Bank", "HDFC0000255", "98854", wabin.name, 3299.17);
		Account acct8 = new Account("HDFC Bank", "HDFC0000121", "65217", amal.name, 562.28);
		Account acct9 = new Account("HDFC Bank", "HDFC0000095", "11206", "", 447.30);


		Account acct10 = new Account("Yes Bank", "YESB0000657", "20028", "latif", 448.12);
		Account acct51 = new Account("Yes Bank", "YESB0000742", "20028", "mojab", 448.12);
		
		
		Account acct11 = new Account("Canara Bank", "CANB0000112", "25754", "tozab", 1447.65);		
		
		Account acct12 = new Account("State Bank of India", "SBIN0008608", "1125754", ronak.name, 144.65);
		Account acct13 = new Account("State Bank of India", "SBIN0008608", "2679817", poski.name, 2144.60);
		Account acct14 = new Account("State Bank of India", "SBIN0002134", "5124376", posat.name, 1211.15);
		Account acct15 = new Account("State Bank of India", "SBIN0002134", "6659100", mannu.name, 345.45);
		Account acct16 = new Account("State Bank of India", "SBIN0002608", "1701251", ulban.name, 1187.12);
		Account acct17 = new Account("State Bank of India", "SBIN0002608", "1002086", laksa.name, 1026.33);
		Account acct18 = new Account("State Bank of India", "SBIN0002608", "3012507", demir.name, 3155.47);
		Account acct19 = new Account("State Bank of India", "SBIN0001095", "6542109", ajib.name, 2618.05);
		Account acct20 = new Account("State Bank of India", "SBIN0001199", "9010221", rathi.name, 912.34);
		Account acct21 = new Account("State Bank of India", "SBIN0001199", "1012012", erwan.name, 8764.29);
		
		LoanProduct loanProduct  = populator.populateBean(LoanProduct.class);
		
		LoanProduct lp1 = new LoanProduct("Business Loan", "Business", 10000000);
		LoanProduct lp2 = new LoanProduct("Working Capital Loan", "Business", 5000000);
		LoanProduct lp3 = new LoanProduct("Tide Loan", "Business", 3500000);
		LoanProduct lp4 = new LoanProduct("Personal Loan", "Personal", 500000);
		LoanProduct lp5 = new LoanProduct("Education Loan", "Personal", 2000000);
		LoanProduct lp6 = new LoanProduct("Vehicle Loan", "Personal", 3000000);
		LoanProduct lp77 = new LoanProduct("Home Loan", "Personal", 100000000);
				
		LoanApplication la8  = populator.populateBean(LoanApplication.class);
		
		
		LoanApplication la12 = new LoanApplication("Business Loan", "ICICI Bank", iciciAdyar.getBankName(), amal.name, "", 10000000);
		LoanApplication la13 = new LoanApplication("Business Loan", "ICICI Bank", iciciAdyar.getBankName(), laksa.name, "", 10000000);
		LoanApplication la14 = new LoanApplication("Business Loan", "HDFC Bank",  hdfcRapu.getBankName(), nadir.name, "", 10000000);
		LoanApplication la15 = new LoanApplication("Business Loan", "HDFC Bank",  hdfcAdyar.getBankName(), rathi.name, "", 10000000);
		LoanApplication la16 = new LoanApplication("Business Loan", "Yes Bank", iciciAdyar.getBankName(), demir.name, "", 10000000);
		LoanApplication la17 = new LoanApplication("Business Loan", "Canara Bank", iciciAdyar.getBankName(), baskar.name, "", 10000000);
		LoanApplication la18 = new LoanApplication("Business Loan", "Kotak Bank", iciciAdyar.getBankName(), wabin.name, "", 10000000);

		LoanApplication la52 = new LoanApplication("Business Loan", "State Bank of India", sbiSilu.getBankName(), jisha.name, "", 10000000);
		LoanApplication la53 = new LoanApplication("Business Loan", "State Bank of India", sbiSalem.getBankName(), adnan.name, "", 10000000);
		LoanApplication la54 = new LoanApplication("Business Loan", "State Bank of India", sbiVirudu.getBankName(), romak.name, "", 10000000);
		LoanApplication la55 = new LoanApplication("Business Loan", "State Bank of India", sbiColaba.getBankName(), sabah.name, "", 10000000);

		LoanApplication la22 = new LoanApplication("Personal Loan", "ICICI Bank", iciciAdyar.getBankName(), poski.name, "", 10000000);
		LoanApplication la23 = new LoanApplication("Personal Loan", "ICICI Bank", iciciAdyar.getBankName(), rahej.name, "", 10000000);
		LoanApplication la24 = new LoanApplication("Personal Loan", "ICICI Bank", iciciAdyar.getBankName(), latif.name, "", 10000000);
		LoanApplication la25 = new LoanApplication("Personal Loan", "ICICI Bank", iciciAdyar.getBankName(), rahej.name, "", 10000000);
		LoanApplication la26 = new LoanApplication("Personal Loan", "HDFC Bank",  hdfcAdyar.getBankName(), muran.name, "", 10000000);
		LoanApplication la27 = new LoanApplication("Personal Loan", "HDFC Bank",  hdfcAnna.getBankName(), ulban.name, "", 10000000);
		LoanApplication la28 = new LoanApplication("Personal Loan", "HDFC Bank",  hdfcAdyar.getBankName(), muran.name, "", 10000000);
		LoanApplication la29 = new LoanApplication("Personal Loan", "HDFC Bank",  hdfcAnna.getBankName(), wabin.name, "", 10000000);
		LoanApplication la30 = new LoanApplication("Personal Loan", "HDFC Bank",  hdfcAdyar.getBankName(), muran.name, "", 10000000);
		LoanApplication la31 = new LoanApplication("Personal Loan", "HDFC Bank",  hdfcAnna.getBankName(), ulban.name, "", 10000000);
		LoanApplication la32 = new LoanApplication("Personal Loan", "Yes Bank", iciciAdyar.getBankName(), sikha.name, "", 10000000);
		LoanApplication la33 = new LoanApplication("Personal Loan", "Canara Bank", iciciAdyar.getBankName(), vastav.name, "", 10000000);
		LoanApplication la34 = new LoanApplication("Personal Loan", "Canara Bank", iciciAdyar.getBankName(), rampal.name, "", 10000000);
		LoanApplication la35 = new LoanApplication("Personal Loan", "Kotak Bank", iciciAdyar.getBankName(), erwan.name, "", 10000000);
		
		LoanApplication la72 = new LoanApplication("Business Loan", "State Bank of India", sbiSalem.getBankName(), wadir.name, "", 10000000);
		LoanApplication la73 = new LoanApplication("Business Loan", "State Bank of India", sbiJai.getBankName(), adnan.name, "", 10000000);
		LoanApplication la74 = new LoanApplication("Business Loan", "State Bank of India", sbiSaket.getBankName(), baskar.name, "", 10000000);
		LoanApplication la75 = new LoanApplication("Business Loan", "State Bank of India", sbiKarol.getBankName(), rampal.name, "", 10000000);
		LoanApplication la76 = new LoanApplication("Business Loan", "State Bank of India", sbiConn.getBankName(), rathi.name, "", 10000000);
		LoanApplication la77 = new LoanApplication("Business Loan", "State Bank of India", sbiGuwa.getBankName(), erwan.name, "", 10000000);
		LoanApplication la78 = new LoanApplication("Business Loan", "State Bank of India", sbiSalem.getBankName(), mojaf.name, "", 10000000);
		LoanApplication la79 = new LoanApplication("Business Loan", "State Bank of India", sbiChembur.getBankName(), wabin.name, "", 10000000);
		LoanApplication la80 = new LoanApplication("Business Loan", "State Bank of India", sbiColaba.getBankName(), latif.name, "", 10000000);
		LoanApplication la81 = new LoanApplication("Business Loan", "State Bank of India", sbiThane.getBankName(), sikha.name, "", 10000000);
		LoanApplication la82 = new LoanApplication("Business Loan", "State Bank of India", sbiLok.getBankName(), poski.name, "", 10000000);
		LoanApplication la83 = new LoanApplication("Business Loan", "State Bank of India", sbiConn.getBankName(), muran.name, "", 10000000);
		
		
		TxnCriteria txncrit = populator.populateBean(TxnCriteria.class);
		
		//insert facts		
		//via wm
		icici.setProducts(Arrays.asList( new String [] {
				"Business Loan",
				"Personal Loan",				
		}));

		hdfc.setProducts(Arrays.asList( new String [] {
				"Business Loan",
				"Personal Loan",				
		}));

		wm.insert(amal); wm.insert(address6);
		
		wm.insert(mojaf); wm.insert(address21);

		wm.insert(latif); wm.insert(address26);
		
		wm.insert(wabin); wm.insert(address22);

		wm.insert(rakun); 

		wm.insert(icici);			wm.insert(hdfc);
		
		wm.insert(iciciAdyar);      wm.insert(iciciNung);
			
		wm.insert(hdfcAnna);    	wm.insert(hdfcRapu);

		//loanprods
		
		wm.insert(lp1);			
		wm.insert(lp2);			
		wm.insert(lp3);			
		wm.insert(lp4);			
		wm.insert(lp5);			
		wm.insert(lp6);
		
		//loanapps

		wm.insert(acct2);
		wm.insert(acct7);
		wm.insert(acct8); 
		wm.insert(acct10); 
		wm.insert(la12);
		wm.insert(la18);
		wm.insert(la24);
		wm.insert(la29);
		wm.insert(la78);
		wm.insert(la80);
		
		
	}

	public void buildNetwork3() throws Exception {

		Topology rete = sess.getWorkingMemory().getRete();
		
	//	Topology.setLayout(new TreeTableLayout());
		
		// rule 1
		AlphaNodeComparator p1 = new AlphaNodeComparator(rete,
				Person.class, new FieldNameComparator(COMPARATOR.EXISTS));

		AlphaNodeComparator a1 = new AlphaNodeComparator(rete,
				Account.class, new FieldNameComparator());
		
		JoinNodeComparator pacjoin = new JoinNodeComparator(rete,
				p1, a1, new FieldNameComparator("name", "custName"));

		//create customer object in working memory. we will later remove when 
		// customer becomes preapproved
		// create action consequence increment customer count by 1

		AlphaNodeComparator p2 = new AlphaNodeComparator(rete,
				Bank.class, new FieldNameComparator());
		
		JoinNodeComparator bck = new JoinNodeComparator(rete,
				p2, pacjoin, new FieldNameComparator("bankName", "name"));
				
		AlphaNodeComparator lp3 = new AlphaNodeComparator(rete,
				LoanProduct.class, new FieldNameComparator());

		JoinNodeComparator blip2 = new JoinNodeComparator(rete,
				p2, lp3, new FieldListValueComparator("products", "name"));

		AlphaNodeComparator la8i = new AlphaNodeComparator(rete,
				LoanApplication.class, new FieldNameComparator());
		
		JoinNodeComparator pla4= new JoinNodeComparator(rete,
				bck, la8i, new FieldNameComparator("name", "custName"));

		// create action consequence increment loan applicant count by 1

		//rule 2	
		AlphaNodeComparator ps11 = new AlphaNodeComparator(rete,
				Person.class, new FieldValueComparator( "score", 700, COMPARATOR.GT));
		
		//create action preapprovedCustomer(Person)
		
		JoinNodeComparator psc26= new JoinNodeComparator(rete,
				pla4, ps11, new FieldNameComparator("custName", "name"));

		// remove customer from customer pool
		// decrement customer count by 1

		
		//rule 3
		JoinNodeComparator tcx1 = new JoinNodeComparator(rete,
				TxnCriteria.class, psc26, 
						new FieldCriteriaComparator(
								new  Object [] { "name", "custName", "accNum", "acctNum" }));

		JoinNodeComparator pstx1 = new JoinNodeComparator(rete,
				pla4, tcx1, new FieldNameComparator("acnum", "accNum"));

		
		JoinNodeComparator pmct45 = new JoinNodeComparator(rete,
				psc26, pstx1);
		
		//rule4 not
		AlphaNodeComparator cc2 = new AlphaNodeComparator(rete,
				CrimCriteria.class, new FieldNameComparator());

		JoinNodeComparator lacc8 = new JoinNodeComparator(rete, 
				pla4, cc2, new DoesNotExistsComparator(cc2.getRealNode(), "loapp.custName", "name") );
		

		//rule5
		JoinNodeComparator lala5 = new JoinNodeComparator(rete, 
				pla4, null, new FieldValueComparator("amount", 10000000, COMPARATOR.GT));

		JoinNodeComparator lpdc4 = new JoinNodeComparator(rete,
				lala5, p2, new FieldNameComparator("branchCode", "mainBranchCode"));
		

		JoinNodeComparator ymct6 = new JoinNodeComparator(rete,
				lacc8, lpdc4);


		JoinNodeComparator rxbts7 = new JoinNodeComparator(rete,
				pmct45, ymct6);
		
		
		RTMNode rtmn1 = new RTMNode("Loan Eligibility ");
				
		rxbts7.addTupleSink(rtmn1);
		
		rete.build(rtmn1);
		
	}
	
	public void buildNetwork4() {			
		
		// a dsl for rule building		
		try {
			ClientReteBuilder.builder("n275")
				.newRule("r100")
					.newAlphaNode(Person.class)
						.fieldAlias("name", "${person.firstName} + ${person.lastName}")
						.filter("name", "lena", COMPARATOR.GT)
					//		.withComparator()					
					.newAlphaNode(Account.class)
					.join(new FieldNameComparator("custName", "name"))
					.newAlphaNode(Bank.class)
					.joinOther(new FieldNameComparator("bank.name", "bank.code", "account.bankCode"))
					.query("ValidCustomer", Person.class, Account.class, Bank.class)
					.buildComposite()					
					.newRule("r215")
					.fact("final-reminder")
					.fact("verifyDate")
					.inlinefact("deadline", "30-07-2021")
					.inlinefact("verifyDate", "12-06-2021")
					.join(new FieldNameComparator("bank.name", "appln.bankName"))
					.joinselect(new FieldNameComparator("bank.products.productId", "appln.loadProductCode"))
					.join(new FieldNameComparator("appln.custName", "person.name"))
					.where()
					.date( "appln.applyDate", "-3d", "30 July 2019")
					.where()
					.date( "verifyDate", "+10d", "deadline || lastVerifyDate")
					.where("")
					.query("hasAppliedForLoan", Person.class, LoanApplication.class)					.newAlphaNode(LoanApplication.class)
					.buildComposite()					
					.newRule("r3778")
					.fact("criteriaMinCreditrScore", 600)
					.joinval(new FieldListValueComparator("appln.loanProductId", "LP01764A", COMPARATOR.EQUALS)) //personal loan
					.fetch("credit-score", "CREDIT_SCORE_API")
						.withCredentials("PAN", "AX@&A356")
						.withCriteria("MOBILE", "7878929902")
					.where("${credit-score} >= $criteriaMinCreditScore")
					.query("hasGoodCreditScore", Person.class)
					.buildComposite()
					.newRule("r3927")
					.fetch("policeRecord", "POLICE_RECORD_API")
						.withCredentials("PAN", "AX@&A356")
						.withCriteria("MOBILE", "7878929902")
						.withCriteria("ADDRESS", "{person.address}")
					.not()
					.join(new FieldNameComparator("${policeRecord.name}", "${person.name})"))
					.query("hasNoCriminalRecord", Person.class)
					.buildComposite()
					.newRule("r456", true)
					/*
					.query("meetsTxnCriteria", Person.class)				
					.consolidate("{account.statement}", new Duration(3), Duration.Unit.MONTHS)
					. group()
						.where("acct.balance > 10000")
						.or()
						.range(Duration.TODAY, "-1m", "3")
						.where("acct.balance > 10000")
						.acceptAll(true)
						.acceptAny(true)
						.acceptMax(new FieldValueComparator("${account.balance}", 100000, COMPARATOR.GT)  )
						.acceptMedian(new FieldValueComparator("${account.balance}", 100000, COMPARATOR.GT))
						.acceptMin(new FieldValueComparator("${account.balance}", 100000, COMPARATOR.GT))
						.acceptAvg(new FieldValueComparator("${account.balance}", 100000, COMPARATOR.GT))
						.acceptCount(new FieldValueComparator("${account.balance}", 100000, COMPARATOR.GT))
						.or()
						.range(Duration.TODAY, "-1m", "3")
						.where("acct.txn > 100000")
						.or()
						.where("acct.txn > 100000")
					.endGroup()
					.buildComposite()
					.newRule("r2176")
					.query("SalaryCriteria", Person.class)
					.range(Duration.TODAY, "-1m", "3")
					.sort()
					.max()
					.median()
					.min()
					.avg()
					.count()
					.where("{acct.salary.high} > 100000")
					.endGroup()
					.buildComposite()
					*/
					.newRule("m7657")
					.query("LoanEligible", Person.class)
					. group()
					.validate("ValidCustomer", new Object [] { "?person", "?account", "?bank"})
					.validate("hasAppliedForLoan", "?loanApplication")
					.validate("hasGoodCreditScore", "?creditScore")
					.validate(("hasPoliceRecord"))
					.validate("meetsTxnCriteria")
					.validate("salaryCriteria")
					.endGroup()
					.acceptAll(true)
					.buildComposite()
				.build();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	

	
}
