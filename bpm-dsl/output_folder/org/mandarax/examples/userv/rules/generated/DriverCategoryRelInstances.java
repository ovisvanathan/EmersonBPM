package org.mandarax.examples.userv.rules.generated;
 
import org.mandarax.examples.userv.domain.*;

import org.mandarax.rt.*;

/**
 * Interface for queries for relationship <strong>DriverCategory</strong>.
 * Generated by org.mandarax.compiler.impl.DefaultCompiler.
 * @version 15 Jun, 2021 11:03:26 AM 
 */
public class DriverCategoryRelInstances {
	// object references
	
	
	// fields representing annotations
	
	// rule: DE_DAC01: _driver.getIsMale() & (_driver.getAge())<25 -> DriverCategory(_driver,"young driver");
	private final static java.util.Properties _annotations_DE_DAC01 = new java.util.Properties();
	
	// rule: DE_DAC02: !(_driver.getIsMale()) & (_driver.getAge())<20 -> DriverCategory(_driver,"young driver");
	private final static java.util.Properties _annotations_DE_DAC02 = new java.util.Properties();
	
	// rule: DE_DAC04: (_driver.getAge())>70 -> DriverCategory(_driver,"senior driver");
	private final static java.util.Properties _annotations_DE_DAC04 = new java.util.Properties();
	
	// rule: DP_07: not DriverCategory(_driver,"young driver") & not DriverCategory(_driver,"senior driver") -> DriverCategory(_driver,"typical driver");
	private final static java.util.Properties _annotations_DP_07 = new java.util.Properties();
	
	
	// initialise annotations
	static {
		// relationship annotations for rule  DE_DAC01: _driver.getIsMale() & (_driver.getAge())<25 -> DriverCategory(_driver,"young driver");
		_annotations_DE_DAC01.put("category","Driver Eligibility Rule Set");
		_annotations_DE_DAC01.put("author","Jens Dietrich");
		_annotations_DE_DAC01.put("lastupdated","19\/11\/10");
		
		// rule annotations for rule  DE_DAC01: _driver.getIsMale() & (_driver.getAge())<25 -> DriverCategory(_driver,"young driver");
		_annotations_DE_DAC01.put("description","If the driver is male and is under the age of 25, then young driver");
		
		
	
		// relationship annotations for rule  DE_DAC02: !(_driver.getIsMale()) & (_driver.getAge())<20 -> DriverCategory(_driver,"young driver");
		_annotations_DE_DAC02.put("category","Driver Eligibility Rule Set");
		_annotations_DE_DAC02.put("author","Jens Dietrich");
		_annotations_DE_DAC02.put("lastupdated","19\/11\/10");
		
		// rule annotations for rule  DE_DAC02: !(_driver.getIsMale()) & (_driver.getAge())<20 -> DriverCategory(_driver,"young driver");
		_annotations_DE_DAC02.put("description","If the driver is female and is under the age of 20, then young driver");
		
		
	
		// relationship annotations for rule  DE_DAC04: (_driver.getAge())>70 -> DriverCategory(_driver,"senior driver");
		_annotations_DE_DAC04.put("category","Driver Eligibility Rule Set");
		_annotations_DE_DAC04.put("author","Jens Dietrich");
		_annotations_DE_DAC04.put("lastupdated","19\/11\/10");
		
		// rule annotations for rule  DE_DAC04: (_driver.getAge())>70 -> DriverCategory(_driver,"senior driver");
		_annotations_DE_DAC04.put("description","If the driver is over the age of 70, then senior driver");
		
		
	
		// relationship annotations for rule  DP_07: not DriverCategory(_driver,"young driver") & not DriverCategory(_driver,"senior driver") -> DriverCategory(_driver,"typical driver");
		_annotations_DP_07.put("category","Driver Eligibility Rule Set");
		_annotations_DP_07.put("author","Jens Dietrich");
		_annotations_DP_07.put("lastupdated","19\/11\/10");
		
		// rule annotations for rule  DP_07: not DriverCategory(_driver,"young driver") & not DriverCategory(_driver,"senior driver") -> DriverCategory(_driver,"typical driver");
		
		
	}
		
	

	// interface generated for queries	
	 
	public static ResultSet<DriverCategoryRel> getCategory (  Driver driver  ) {
		DerivationController _derivation = new DefaultDerivationController();
		return new ResultSet<DriverCategoryRel>(getCategory ( _derivation ,  driver ),_derivation);
	} 
	 
	public static ResultSet<DriverCategoryRel> hasCategory (  Driver driver ,  String category  ) {
		DerivationController _derivation = new DefaultDerivationController();
		return new ResultSet<DriverCategoryRel>(hasCategory ( _derivation ,  driver ,  category ),_derivation);
	} 
	
	
	// implementations - these methods are referenced by code generated from other rules in this package
	// and therefore kept static 
	 
	static ResourceIterator<DriverCategoryRel> getCategory ( final DerivationController _derivation ,  final Driver driver  ) {
		final int _derivationlevel = _derivation.size();
		return new IteratorChain<DriverCategoryRel>(4) {
			
			public ResourceIterator<DriverCategoryRel> getNextIterator(int pos) {
			switch (pos) {
				
                		case 0: {
                			// invoke DE_DAC01: _driver.getIsMale() & (_driver.getAge())<25 -> DriverCategory(_driver,"young driver");
                			return getCategory_0(_derivation.pop(_derivationlevel) ,  driver );
                		}
				
                		case 1: {
                			// invoke DE_DAC02: !(_driver.getIsMale()) & (_driver.getAge())<20 -> DriverCategory(_driver,"young driver");
                			return getCategory_1(_derivation.pop(_derivationlevel) ,  driver );
                		}
				
                		case 2: {
                			// invoke DE_DAC04: (_driver.getAge())>70 -> DriverCategory(_driver,"senior driver");
                			return getCategory_2(_derivation.pop(_derivationlevel) ,  driver );
                		}
				
                		case 3: {
                			// invoke DP_07: not DriverCategory(_driver,"young driver") & not DriverCategory(_driver,"senior driver") -> DriverCategory(_driver,"typical driver");
                			return getCategory_3(_derivation.pop(_derivationlevel) ,  driver );
                		}
				
				default: return EmptyIterator.DEFAULT;
			}}
		};
	} 
	 
	static ResourceIterator<DriverCategoryRel> hasCategory ( final DerivationController _derivation ,  final Driver driver ,  final String category  ) {
		final int _derivationlevel = _derivation.size();
		return new IteratorChain<DriverCategoryRel>(4) {
			
			public ResourceIterator<DriverCategoryRel> getNextIterator(int pos) {
			switch (pos) {
				
                		case 0: {
                			// invoke DE_DAC01: _driver.getIsMale() & (_driver.getAge())<25 -> DriverCategory(_driver,"young driver");
                			return hasCategory_0(_derivation.pop(_derivationlevel) ,  driver ,  category );
                		}
				
                		case 1: {
                			// invoke DE_DAC02: !(_driver.getIsMale()) & (_driver.getAge())<20 -> DriverCategory(_driver,"young driver");
                			return hasCategory_1(_derivation.pop(_derivationlevel) ,  driver ,  category );
                		}
				
                		case 2: {
                			// invoke DE_DAC04: (_driver.getAge())>70 -> DriverCategory(_driver,"senior driver");
                			return hasCategory_2(_derivation.pop(_derivationlevel) ,  driver ,  category );
                		}
				
                		case 3: {
                			// invoke DP_07: not DriverCategory(_driver,"young driver") & not DriverCategory(_driver,"senior driver") -> DriverCategory(_driver,"typical driver");
                			return hasCategory_3(_derivation.pop(_derivationlevel) ,  driver ,  category );
                		}
				
				default: return EmptyIterator.DEFAULT;
			}}
		};
	} 
	
	
	
	// private methods - each method represents the invocation of a single rule for a certain query
	// query: getCategory
	// rule: DE_DAC01: _driver.getIsMale() & (_driver.getAge())<25 -> DriverCategory(_driver,"young driver");
	private static ResourceIterator<DriverCategoryRel> getCategory_0 (final DerivationController _derivation ,  final Driver driver ) {
		
		
			_derivation.log("org.mandarax.examples.userv.rules.generated.DriverCategory.DE_DAC01", DerivationController.RULE, _annotations_DE_DAC01);		
				
	
		
		// utility class used to keep track of variables bindings
		// rule: DE_DAC01: _driver.getIsMale() & (_driver.getAge())<25 -> DriverCategory(_driver,"young driver");
		// prereqs: [_driver.getIsMale(), (_driver.getAge())<25]
		class _Bindings {
			private org.mandarax.examples.userv.domain.Driver _driver = driver;
		}
		final _Bindings _bindings = new _Bindings();
		ResourceIterator<?> _tmp = null;
		
		 
		
		
		
		 
		// check conditions	
		boolean _checkfailed = false;
		
		
		
		if (_checkfailed) {
			return EmptyIterator.DEFAULT;
		}
		
		

		 
		
		
		
		// apply prerequisite _driver.getIsMale()
		
		
		 // case 4
					if (!(_bindings._driver.isMale())) {return EmptyIterator.DEFAULT;} 
					
		 
		
		
		
		// apply prerequisite (_driver.getAge())<25
		
		
		 // case 4
					if (!((_bindings._driver.getAge())<25)) {return EmptyIterator.DEFAULT;} 
					
		
		
		// rule head
		
		return new SingletonIterator(new DriverCategoryRel(_bindings._driver,"young driver"));
        
		
		
		
	

		
	}
	// rule: DE_DAC02: !(_driver.getIsMale()) & (_driver.getAge())<20 -> DriverCategory(_driver,"young driver");
	private static ResourceIterator<DriverCategoryRel> getCategory_1 (final DerivationController _derivation ,  final Driver driver ) {
		
		
			_derivation.log("org.mandarax.examples.userv.rules.generated.DriverCategory.DE_DAC02", DerivationController.RULE, _annotations_DE_DAC02);		
				
	
		
		// utility class used to keep track of variables bindings
		// rule: DE_DAC02: !(_driver.getIsMale()) & (_driver.getAge())<20 -> DriverCategory(_driver,"young driver");
		// prereqs: [!(_driver.getIsMale()), (_driver.getAge())<20]
		class _Bindings {
			private org.mandarax.examples.userv.domain.Driver _driver = driver;
		}
		final _Bindings _bindings = new _Bindings();
		ResourceIterator<?> _tmp = null;
		
		 
		
		
		
		 
		// check conditions	
		boolean _checkfailed = false;
		
		
		
		if (_checkfailed) {
			return EmptyIterator.DEFAULT;
		}
		
		

		 
		
		
		
		// apply prerequisite !(_driver.getIsMale())
		
		
		 // case 4
					if (!(!(_bindings._driver.isMale()))) {return EmptyIterator.DEFAULT;} 
					
		 
		
		
		
		// apply prerequisite (_driver.getAge())<20
		
		
		 // case 4
					if (!((_bindings._driver.getAge())<20)) {return EmptyIterator.DEFAULT;} 
					
		
		
		// rule head
		
		return new SingletonIterator(new DriverCategoryRel(_bindings._driver,"young driver"));
        
		
		
		
	

		
	}
	// rule: DE_DAC04: (_driver.getAge())>70 -> DriverCategory(_driver,"senior driver");
	private static ResourceIterator<DriverCategoryRel> getCategory_2 (final DerivationController _derivation ,  final Driver driver ) {
		
		
			_derivation.log("org.mandarax.examples.userv.rules.generated.DriverCategory.DE_DAC04", DerivationController.RULE, _annotations_DE_DAC04);		
				
	
		
		// utility class used to keep track of variables bindings
		// rule: DE_DAC04: (_driver.getAge())>70 -> DriverCategory(_driver,"senior driver");
		// prereqs: [(_driver.getAge())>70]
		class _Bindings {
			private org.mandarax.examples.userv.domain.Driver _driver = driver;
		}
		final _Bindings _bindings = new _Bindings();
		ResourceIterator<?> _tmp = null;
		
		 
		
		
		
		 
		// check conditions	
		boolean _checkfailed = false;
		
		
		
		if (_checkfailed) {
			return EmptyIterator.DEFAULT;
		}
		
		

		 
		
		
		
		// apply prerequisite (_driver.getAge())>70
		
		
		 // case 4
					if (!((_bindings._driver.getAge())>70)) {return EmptyIterator.DEFAULT;} 
					
		
		
		// rule head
		
		return new SingletonIterator(new DriverCategoryRel(_bindings._driver,"senior driver"));
        
		
		
		
	

		
	}
	// rule: DP_07: not DriverCategory(_driver,"young driver") & not DriverCategory(_driver,"senior driver") -> DriverCategory(_driver,"typical driver");
	private static ResourceIterator<DriverCategoryRel> getCategory_3 (final DerivationController _derivation ,  final Driver driver ) {
		
		
			_derivation.log("org.mandarax.examples.userv.rules.generated.DriverCategory.DP_07", DerivationController.RULE, _annotations_DP_07);		
				
	
		
		// utility class used to keep track of variables bindings
		// rule: DP_07: not DriverCategory(_driver,"young driver") & not DriverCategory(_driver,"senior driver") -> DriverCategory(_driver,"typical driver");
		// prereqs: [not DriverCategory(_driver,"young driver"), not DriverCategory(_driver,"senior driver")]
		class _Bindings {
			private org.mandarax.examples.userv.domain.Driver _driver = driver;
		}
		final _Bindings _bindings = new _Bindings();
		ResourceIterator<?> _tmp = null;
		
		 
		
		
		
		 
		// check conditions	
		boolean _checkfailed = false;
		
		
		
		if (_checkfailed) {
			return EmptyIterator.DEFAULT;
		}
		
		

		 
		
		
		
		// apply prerequisite not DriverCategory(_driver,"young driver")
		
		
		 // case 4
					
					_tmp = DriverCategoryRelInstances.hasCategory(_derivation.push(),_bindings._driver,"young driver");
					
					if (_tmp.hasNext()) {
						_tmp.close();
						return EmptyIterator.DEFAULT;
					}
					
					
		 
		
		
		
		// apply prerequisite not DriverCategory(_driver,"senior driver")
		
		
		 // case 4
					
					_tmp = DriverCategoryRelInstances.hasCategory(_derivation.push(),_bindings._driver,"senior driver");
					
					if (_tmp.hasNext()) {
						_tmp.close();
						return EmptyIterator.DEFAULT;
					}
					
					
		
		
		// rule head
		
		return new SingletonIterator(new DriverCategoryRel(_bindings._driver,"typical driver"));
        
		
		
		
	

		
	}
	// query: hasCategory
	// rule: DE_DAC01: _driver.getIsMale() & (_driver.getAge())<25 -> DriverCategory(_driver,"young driver");
	private static ResourceIterator<DriverCategoryRel> hasCategory_0 (final DerivationController _derivation ,  final Driver driver ,  final String category ) {
		
		
			_derivation.log("org.mandarax.examples.userv.rules.generated.DriverCategory.DE_DAC01", DerivationController.RULE, _annotations_DE_DAC01);		
				
	
		
		// utility class used to keep track of variables bindings
		// rule: DE_DAC01: _driver.getIsMale() & (_driver.getAge())<25 -> DriverCategory(_driver,"young driver");
		// prereqs: [_driver.getIsMale(), (_driver.getAge())<25]
		class _Bindings {
			private org.mandarax.examples.userv.domain.Driver _driver = driver;
		}
		final _Bindings _bindings = new _Bindings();
		ResourceIterator<?> _tmp = null;
		
		 
		
		
		
		 
		// check conditions	
		boolean _checkfailed = false;
		
		
		
		_checkfailed = _checkfailed || !Equals.compare(category,"young driver"); 
		
		if (_checkfailed) {
			return EmptyIterator.DEFAULT;
		}
		
		

		 
		
		
		
		// apply prerequisite _driver.getIsMale()
		
		
		 // case 4
					if (!(_bindings._driver.isMale())) {return EmptyIterator.DEFAULT;} 
					
		 
		
		
		
		// apply prerequisite (_driver.getAge())<25
		
		
		 // case 4
					if (!((_bindings._driver.getAge())<25)) {return EmptyIterator.DEFAULT;} 
					
		
		
		// rule head
		
		return new SingletonIterator(new DriverCategoryRel(_bindings._driver,"young driver"));
        
		
		
		
	

		
	}
	// rule: DE_DAC02: !(_driver.getIsMale()) & (_driver.getAge())<20 -> DriverCategory(_driver,"young driver");
	private static ResourceIterator<DriverCategoryRel> hasCategory_1 (final DerivationController _derivation ,  final Driver driver ,  final String category ) {
		
		
			_derivation.log("org.mandarax.examples.userv.rules.generated.DriverCategory.DE_DAC02", DerivationController.RULE, _annotations_DE_DAC02);		
				
	
		
		// utility class used to keep track of variables bindings
		// rule: DE_DAC02: !(_driver.getIsMale()) & (_driver.getAge())<20 -> DriverCategory(_driver,"young driver");
		// prereqs: [!(_driver.getIsMale()), (_driver.getAge())<20]
		class _Bindings {
			private org.mandarax.examples.userv.domain.Driver _driver = driver;
		}
		final _Bindings _bindings = new _Bindings();
		ResourceIterator<?> _tmp = null;
		
		 
		
		
		
		 
		// check conditions	
		boolean _checkfailed = false;
		
		
		
		_checkfailed = _checkfailed || !Equals.compare(category,"young driver"); 
		
		if (_checkfailed) {
			return EmptyIterator.DEFAULT;
		}
		
		

		 
		
		
		
		// apply prerequisite !(_driver.getIsMale())
		
		
		 // case 4
					if (!(!(_bindings._driver.isMale()))) {return EmptyIterator.DEFAULT;} 
					
		 
		
		
		
		// apply prerequisite (_driver.getAge())<20
		
		
		 // case 4
					if (!((_bindings._driver.getAge())<20)) {return EmptyIterator.DEFAULT;} 
					
		
		
		// rule head
		
		return new SingletonIterator(new DriverCategoryRel(_bindings._driver,"young driver"));
        
		
		
		
	

		
	}
	// rule: DE_DAC04: (_driver.getAge())>70 -> DriverCategory(_driver,"senior driver");
	private static ResourceIterator<DriverCategoryRel> hasCategory_2 (final DerivationController _derivation ,  final Driver driver ,  final String category ) {
		
		
			_derivation.log("org.mandarax.examples.userv.rules.generated.DriverCategory.DE_DAC04", DerivationController.RULE, _annotations_DE_DAC04);		
				
	
		
		// utility class used to keep track of variables bindings
		// rule: DE_DAC04: (_driver.getAge())>70 -> DriverCategory(_driver,"senior driver");
		// prereqs: [(_driver.getAge())>70]
		class _Bindings {
			private org.mandarax.examples.userv.domain.Driver _driver = driver;
		}
		final _Bindings _bindings = new _Bindings();
		ResourceIterator<?> _tmp = null;
		
		 
		
		
		
		 
		// check conditions	
		boolean _checkfailed = false;
		
		
		
		_checkfailed = _checkfailed || !Equals.compare(category,"senior driver"); 
		
		if (_checkfailed) {
			return EmptyIterator.DEFAULT;
		}
		
		

		 
		
		
		
		// apply prerequisite (_driver.getAge())>70
		
		
		 // case 4
					if (!((_bindings._driver.getAge())>70)) {return EmptyIterator.DEFAULT;} 
					
		
		
		// rule head
		
		return new SingletonIterator(new DriverCategoryRel(_bindings._driver,"senior driver"));
        
		
		
		
	

		
	}
	// rule: DP_07: not DriverCategory(_driver,"young driver") & not DriverCategory(_driver,"senior driver") -> DriverCategory(_driver,"typical driver");
	private static ResourceIterator<DriverCategoryRel> hasCategory_3 (final DerivationController _derivation ,  final Driver driver ,  final String category ) {
		
		
			_derivation.log("org.mandarax.examples.userv.rules.generated.DriverCategory.DP_07", DerivationController.RULE, _annotations_DP_07);		
				
	
		
		// utility class used to keep track of variables bindings
		// rule: DP_07: not DriverCategory(_driver,"young driver") & not DriverCategory(_driver,"senior driver") -> DriverCategory(_driver,"typical driver");
		// prereqs: [not DriverCategory(_driver,"young driver"), not DriverCategory(_driver,"senior driver")]
		class _Bindings {
			private org.mandarax.examples.userv.domain.Driver _driver = driver;
		}
		final _Bindings _bindings = new _Bindings();
		ResourceIterator<?> _tmp = null;
		
		 
		
		
		
		 
		// check conditions	
		boolean _checkfailed = false;
		
		
		
		_checkfailed = _checkfailed || !Equals.compare(category,"typical driver"); 
		
		if (_checkfailed) {
			return EmptyIterator.DEFAULT;
		}
		
		

		 
		
		
		
		// apply prerequisite not DriverCategory(_driver,"young driver")
		
		
		 // case 4
					
					_tmp = DriverCategoryRelInstances.hasCategory(_derivation.push(),_bindings._driver,"young driver");
					
					if (_tmp.hasNext()) {
						_tmp.close();
						return EmptyIterator.DEFAULT;
					}
					
					
		 
		
		
		
		// apply prerequisite not DriverCategory(_driver,"senior driver")
		
		
		 // case 4
					
					_tmp = DriverCategoryRelInstances.hasCategory(_derivation.push(),_bindings._driver,"senior driver");
					
					if (_tmp.hasNext()) {
						_tmp.close();
						return EmptyIterator.DEFAULT;
					}
					
					
		
		
		// rule head
		
		return new SingletonIterator(new DriverCategoryRel(_bindings._driver,"typical driver"));
        
		
		
		
	

		
	}
	
	
	// methods representing aggregation functions
	
	
}


