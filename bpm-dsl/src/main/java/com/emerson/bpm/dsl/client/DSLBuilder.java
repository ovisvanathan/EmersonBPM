package com.emerson.bpm.dsl.client;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import javax.inject.Inject;

import com.emerson.bpm.api.Topology;
import com.emerson.bpm.api.UtilsServiceProvider;
import com.emerson.bpm.dsl.Record;
import com.emerson.bpm.dsl.record.BankRecord;
import com.emerson.bpm.dsl.record.CustomerRecord;
import com.emerson.bpm.dsl.record.LoanRecord;
import com.emerson.bpm.dsl.util.APICall;
import com.emerson.bpm.dsl.util.BindingFunction;
import com.emerson.bpm.dsl.util.DSLPredicate;
import com.emerson.bpm.dsl.util.NetworkBuilder2;
import com.emerson.bpm.dsl.util.QuerySpec;
import com.emerson.bpm.functor.Predicate2;
import com.emerson.bpm.model.Person;
import com.emerson.bpm.nodes.match.el.util.ELUtils;
import com.emerson.bpm.util.ServiceFactory;
import com.picasso.paddle.inject.BeanGetters;

public class DSLBuilder implements NetworkBuilder2 {
	
	String id;

	Map recordsMap = new HashMap();

	QuerySpec currQuerySpec;
	
	APICall currApiCall;
	
	@Inject
	BeanGetters beanGetters;

	enum DSL_CONTEXT {
		QUERY_BEGIN,
		QUERY_END
	};
	
	DSL_CONTEXT DSLCONTEXT;
	
	private Map factsMap;

	private List<QuerySpec> querySpecs = new ArrayList();

	private Map availableAPIs = new HashMap();

	UtilsServiceProvider EmersonUtils = (UtilsServiceProvider) 
			ServiceFactory.getUtilsProvider();

	public DSLBuilder(String id) {
		this.id = id;
		
		populateAPIs();
	}

	private void populateAPIs() {
				
		//load from db		
		availableAPIs.put("credit-score", "http://www.crispin.com/creditscoreapi/endpoint");
		availableAPIs.put("police-record", "http://www.tn.police.gov/records/police-verification/api");

	}

	public static NetworkBuilder2 builder(String id) {

		return new DSLBuilder(id);
	}

	
	@Override
	public NetworkBuilder2 bind(String key, Record dslrec) {
			
		System.out.println("grtx name = " + dslrec.getRequestType().getName());
		
		if(!recordsMap.containsKey(key))
			recordsMap.put(key, dslrec);

	 	return this;
	}

	@Override
	public NetworkBuilder2 fact(String key, Object val) {
					
		if(!factsMap.containsKey(key))
			factsMap.put(key, val);

	 	return this;
	}

	public NetworkBuilder2 newQuery() {

		DSLCONTEXT = DSL_CONTEXT.QUERY_BEGIN;

		if(currQuerySpec != null) {
			querySpecs .add(currQuerySpec);
		
		}
		
		currQuerySpec = new QuerySpec();
		return this;
	}

	BiFunction<String, String, String> evalBindingFunc = (a, b) -> {
		
		if(a.equals("count")) {
			
		} else if(a.equals("avg")) {
			
		}  else if(a.equals("min")) {
			
		}  else if(a.equals("max")) {
			
		}  else if(a.equals("median")) {
			
		}  
		
		return null;
	};
	
	public NetworkBuilder2 bindFunc(String var, String rec) throws Exception {
		
		int opos = rec.indexOf("(");
		
		if(opos == -1)
			throw new Exception("invalid function should be of the form func_name(param)");

		
		String funcName = rec.substring(0, opos);

		String param = rec.substring(opos+1, rec.indexOf(")"));

		currQuerySpec.getBindingFunctions().put(var, new BindingFunction(funcName, param));
		
		return this;
				
	}
	
	public NetworkBuilder2 bindVar(String var, String rec) {
	
		try {
				//		currSpec.bindingsVars.put(var, beanGetters.get(rec, key));
		
				int ipos = rec.indexOf(".");
				
				if(ipos == -1)
					throw new Exception("Incorrect binding variable format. Must be objectName.fieldName or $objectAlias.fieldName");
				
				String varName = rec.substring(0, ipos);
		
				String varField = rec.substring(ipos+1);
		
					Record record = null;
					if(varName.startsWith("$")) {
						
						if(!recordsMap.containsKey(varName.substring(1)))
							throw new Exception("No record found for binding variable " + varName);
						
					} else {
					
						Collection<Record> values = recordsMap.values();
					
						
						for(Record record1 : values) {
						
							if(varName.equalsIgnoreCase(record1.getClass().getSimpleName())) {
									record = record1;				
									break;
							}
						}
						
					}
					
					if(record == null)
						throw new Exception("No record found for binding variable " + varName);

			//		currQuerySpec.getBindingVariables().put(var,  beanGetters.get(record, varField));
		
					Method getter = ELUtils.findGetMethod(record, varField);
					
					//dont use fieldVal which is valueToCheck
					Object fieldVal1 = null;
					try {
						fieldVal1 = getter.invoke(record, null);
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

		}  catch (Exception e) {
			
		}
		
		return this;		
	}

	public NetworkBuilder2 makeRule(String pstr) {		
		makeRule("QSP_PRECIATE_" + querySpecs.size(), new DSLPredicate(pstr));
		return this;				
	}

	public NetworkBuilder2 makeRule(String predicateName, String pstr) {		
		makeRule(predicateName, new DSLPredicate(pstr));
		return this;				
	}

	public void makeRule(String predicateName, Predicate2 p) {
		currQuerySpec.getPredicates().put(predicateName, p);
	}

	public NetworkBuilder2 fetchRule(String predicateName, String apiName) throws Exception {		

		
		if(!availableAPIs.containsKey(apiName))
			throw new Exception("API not found " + apiName);
		
		APICall currApiCall = new APICall();
				
		return this;				
	}

	public NetworkBuilder2 withCredentials(String key, String val) {
	
		currApiCall.getCredentials().put(key, val);
		
		return this;				
	}

	@Override
	public NetworkBuilder2 withCriteria(Function<Object, Object> criteriaLambda) {

		currApiCall.getCriteria().add(criteriaLambda);		
		return null;
	}

	@Override
	public NetworkBuilder2 verify(String expr) {
		currApiCall.setExprCriteria(expr);		
		return null;
	}


	public NetworkBuilder2 stop() {
		
		currQuerySpec.getApiCalls().add(currApiCall);		
		return this;				
	}

	public NetworkBuilder2 query(String queryName, Class... expr) {		
		currQuerySpec.getPredicates().put(queryName, new DSLPredicate(queryName, expr));
		return this;						
	}

	
	public Topology build() {		
			
		Map<String, Object> bvarsGlobal = new HashMap();
	 	
		for(QuerySpec qspec : querySpecs) {
			
			Map<String, Object> bvars = qspec.getBindingVariables();
			
			bvarsGlobal.putAll(bvars);

			Map<String, BindingFunction> bindFuncs = null;
			if(qspec.getBindingFunctions().size() > 0) {
				bindFuncs = qspec.getBindingFunctions();			
			}

			List<APICall> apiCalls = null;
			if(qspec.getApiCalls().size() > 0) {
				apiCalls = qspec.getApiCalls();			
			}
			
			
			Map<String, Predicate2> predicatesMap = qspec.getPredicates();

			assert(predicatesMap.size() == 1);
			
			Map.Entry [] entries = (Map.Entry []) predicatesMap.entrySet().toArray(new Map.Entry [] {});
			
			
			Map.Entry entry = entries[0];
			
			String predName = (String) entry.getKey();
			
			String predicate = (String) entry.getValue();
			
			
		//	DSLUtils.predicateToReteNetwork(predicate);

		}
		
	 	
	 	
	 	
		//	.fact("criteriaMinCreditrScore", 600)
	//	.then()	
	//	.fetch("credit-score", "CREDIT_SCORE_API")
	//		.withCredentials("PAN", "p1")
	//		.withCriteria("MOBILE", "p2")
	//	.where("${credit-score} == $criteriaMinCreditScore")	
	//	.buildComposite();
	 	
	 	return null;
	}
	
	
	public static void main(String [] args) {
		
		DSLBuilder dx = new DSLBuilder("IMS92");
		
		CustomerRecord cr = new CustomerRecord("cino", "nico", "T914", "217-226-2545", "xx@px.df");
		BankRecord br = new BankRecord("MSK271982654", "ABC Bank", "Mumbai", "ABC72600015");
		LoanRecord lks = new LoanRecord("MSK271982654", "ABC Bank", "Mumbai", "ABC72600015");

		dx.bind("cx2", cr)		
			.bind("bd4", br)
				.bind("lks9", lks)
					.build();
		

		  try {
			
			  
			dx
			.fact("c1", "lena skywalker")
			.fact("d1", "12 08 2021")
			.fact("criteriaMinCreditScore", 600)
			.bindVar("p1", "$c4.custId")
			.bindVar("a2", "$c4.acctNumber")
			.bindVar("p2", "$a3.accountNumber")	 	
			.makeRule("ValidCustomer", "$a2 == $p2")
			.bindVar("n1", "$c4.firstName")
			.bindVar("n2", "$c4.lastName")
			.bindVar("n3", "$n1  \" \" + $n2")
			.makeRule("$n3 == $c1")
			.bindVar("x1", "$a3.bankName2")	// account.bankName
			.bindVar("x2", "$b2.bankName")	// bank.bankName
			.makeRule("$x1 == $x2")
			.bindVar("k2", "$L1.customerId")	
			.makeRule("$p1 == $k2")
			.bindVar("m1", "$L1.productId")	
			.bindVar("m2", "$b2.products")
			.makeRule("$m1 in $m2")
			.bindVar("d1", "$L1.applicationDate")
			.makeRule("$d1 before $d2")
			.makeRule("$NOW $d1+10d")		
			
			.bindVar("r1", "$c4.phoneNumber")
			.bindVar("r2", "$c4.tax_id")
			.bindVar("r3", "$c4.email_id")
			.fetchRule("credit-score", "CREDIT_SCORE_API")
				.withCredentials("PAN", "$r2")
				.withCredentials("mobile", "$r1")
				.withCriteria((a) -> ("a >= \"$criteriaMinCreditScore\""))
			  	.stop()
			.fetchRule("police-record", "POLICE_RECORD_API")
				.withCredentials("name", "$n3")
				.withCredentials("PAN", "$r2")
				.withCredentials("mobile", "$r1")
				.verify("${criminal_record} == nil")	
				.stop()
			.bindFunc("ct1", "count($a3.txn_id)")	
			.makeRule("$ct1 >= 25")
			.query("LoanEligible", Person.class)
			.build();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		 	
	 	

	}



}
