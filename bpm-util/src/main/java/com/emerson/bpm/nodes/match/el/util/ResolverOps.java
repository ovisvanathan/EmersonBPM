package com.emerson.bpm.nodes.match.el.util;

import java.util.Stack;
import java.util.function.BiFunction;
import java.util.function.Function;

import com.picasso.paddle.tasks.pipeline.function.ParamFunction;

public class ResolverOps {

			private static  final int EQUALS = 0;
			private static  final int NOT_EQUALS = 1;
			private static  final int GT = 2;
			private static  final int LT = 3;
			private static  final int GTE = 4;
			private static  final int LTE = 5;
			private static  final int PLUS = 6;
			private static  final int MINUS = 7;
			private static  final int MULT = 8;
			private static  final int DIV = 9;
			private static  final int MODULO = 10;
			private static  final int NEGATE = 25;
					
			private static  final int BITWISE_AND = 11;
			private static  final int BITWISE_OR = 12;
			private static  final int BITWISE_XOR = 13;
			private static  final int LOGICAL_AND = 14;
			private static  final int LOGICAL_OR = 15;
			private static  final int LOGICAL_NOT = 16;
			private static  final int INCR = 17;
			private static  final int DECR = 18;
			private static  final int LEFT_SHIFT = 19;
			private static  final int RIGHT_SHIFT = 20;
			private static  final int ZERO_FILL_RIGHT_SHIFT = 21;
			private static  final int STRING_EQUALS = 22;
			private static  final int STRING_CASE_IGNORE_EQUALS = 23;
			private static  final int EQUALS_CLASS_ASSIGN = 24;
			private static  final int EQUALS_TO_INDEX = 25;

			private static  final int POW_INDEX = 26;
			private static  final int SQRT_INDEX = 27;
			private static  final int LOG_INDEX = 28;
			private static  final int LON_INDEX = 29;

			private static  final int SIN_INDEX = 29;
			private static  final int COS_INDEX = 29;
			private static  final int TAN_INDEX = 29;
			private static  final int SIN_INV_INDEX = 29;
			private static  final int COS_INV_INDEX = 29;
			private static  final int TAN_INV_INDEX = 29;

			private static  final int EXP_INDEX = 29;
			private static  final int MAX_INDEX = 29;
			private static  final int MIN_INDEX = 29;
			private static  final int ABS_INDEX = 29;

			private static  final int ROUND_INDEX = 29;
			private static  final int CBRT_INDEX = 29;
			private static  final int CEIL_INDEX = 29;
			private static  final int FLOOR_INDEX = 29;

	static String [] operators = {
		"==",
		"!=",
		">",
		"<",
		">=",
		"<=",
		"+",
		"-",
		"*",
		"/",
		"%",
		"&",
		"|",
		"^^",
		"&&",
		"||",
		"!",
		"++",
		"--",
		"<<",
		">>",
		">>>",
		"===",
		"<>",
		"!<>",
		"::=",			// the final equalsTo to begin calculating ::=
		"^",			 // x pow y
        "sqrt()",
        "log()",
        "ln()",
        "sin()",
        "cos()",
        "tan()",
        "asin()",
        "acos()",
        "atan()",
        "exp()",
        "max()",
        "min()",
        "abs()",
        "round()",
        "cbrt()",
        "ceil()",
        "floor()"
	};
	
	static BiFunction<Integer, Integer, Boolean> opEquals = (a, b) -> a == b;
	static BiFunction<Integer, Integer, Boolean> opNotEquals = (a, b) -> a != b;
	static BiFunction<Integer, Integer, Boolean> opGT = (a, b) -> a > b;
	static BiFunction<Integer, Integer, Boolean> opLT = (a, b) -> a < b;
	static BiFunction<Integer, Integer, Boolean> opGTE = (a, b) -> a >= b;
	static BiFunction<Integer, Integer, Boolean> opLTE = (a, b) -> a <= b;
	static BiFunction<Integer, Integer, Integer>  opPlus = (a, b) -> a + b;
	static BiFunction<Integer, Integer, Integer>  opMinus = (a, b) -> a - b;
	static BiFunction<Integer, Integer, Integer> opMult = (a, b) -> a * b;
	static BiFunction<Integer, Integer, Integer> opDiv = (a, b) -> a / b;
	static BiFunction<Integer, Integer, Integer> opMod = (a, b) -> a % b;
	
	static BiFunction<Integer, Integer, Integer> opBitwiseAnd = (a, b) -> a & b;
	static BiFunction<Integer, Integer, Integer> opBitwiseOr = (a, b) -> a | b;
	static BiFunction<Integer, Integer, Integer> opBitwiseXor = (a, b) -> a ^ b;
	static BiFunction<Boolean, Boolean, Boolean> opLogicalAnd = (a, b) -> a && b;
	static BiFunction<Boolean, Boolean, Boolean> opLogicalOr = (a, b) -> a || b;
	static Function<Boolean, Boolean> opLogicalNot = (a) -> !a;

	static Function<Integer, Integer> opIncr = (a) -> a++;
	static Function<Integer, Integer> opDecr = (a) -> a--;
	static BiFunction<Integer, Integer, Integer> opLeftShift = (a, b) -> a << b;
	static BiFunction<Integer, Integer, Integer> opRightShift = (a, b) -> a >> b;
	static BiFunction<Integer, Integer, Integer> opRightShiftZF = (a, b) -> a >>> b;
	
	static BiFunction<String, String, Boolean> opEqualsStr = (a, b) -> a.equals(b);
	static BiFunction<String, String, Boolean> opEqualsStrIgnoreCase = (a, b) -> a.equalsIgnoreCase(b);
	static BiFunction<Object, Object, Boolean> opEqualsClassAssign = (a, b) -> a.getClass().isAssignableFrom(b.getClass());
	static BiFunction<Integer, Integer, Integer> opNegate = (a, b) -> a * -1;

	static BiFunction<Double, Double, Double> opPower = (a, b) -> Math.pow(a, b);
	static Function<Double, Double> opSqrt = (a) -> Math.sqrt(a);
	static Function<Double, Double> opLog = (a) -> Math.log(a);
	static Function<Double, Double> opLon = (a) -> Math.log(a)/ Math.log(2);
	static Function<Double, Double> opSin = (a) -> Math.sin(a);
	static Function<Double, Double> opCos = (a) -> Math.cos(a);
	static Function<Double, Double> opTan = (a) -> Math.tan(a);
	static Function<Double, Double> opAsin = (a) -> Math.asin(a);
	static Function<Double, Double> opAcos = (a) -> Math.acos(a);
	static Function<Double, Double> opAtan = (a) -> Math.atan(a);
	static Function<Double, Double> opExp = (a) -> Math.exp(a);
	static BiFunction<Double, Double, Double> opMax = (a, b) -> Math.max(a, b);
	static BiFunction<Double, Double, Double> opMin = (a, b) -> Math.min(a, b);
	static Function<Double, Double> opAbs = (a) -> Math.abs(a);
	static Function<Double, Long> opRound = (a) -> Math.round(a);
	static BiFunction<Double, Double, Double> opRoundoff = (a, b) -> Math.round(a)/b;
	static BiFunction<Double, Double, Double> opRound100 = (a, b) -> Math.round(a*100)/b;

	static BiFunction<Double, Double, Double> opCbrt = (a, b) -> Math.cbrt(a);
	static BiFunction<Double, Double, Double> opCeil = (a, b) -> Math.ceil(a);
	static BiFunction<Double, Double, Double> opFloor = (a, b) -> Math.floor(a);

	
	
	static ParamFunction [] operatorFunctions = {
			new ParamFunction(opEquals), 
			new ParamFunction(opNotEquals), 
			new ParamFunction(opGT), 
			new ParamFunction(opLT), 
			new ParamFunction(opGTE), 
			new ParamFunction(opLTE), 
			new ParamFunction(opPlus), 
			new ParamFunction(opMinus), 
			new ParamFunction(opMult), 
			new ParamFunction(opDiv), 
			new ParamFunction(opMod), 
				
			new ParamFunction(opBitwiseAnd), 
			new ParamFunction(opBitwiseOr), 
			new ParamFunction(opBitwiseXor), 
			new ParamFunction(opLogicalAnd), 
			new ParamFunction(opLogicalOr), 
			new ParamFunction(opLogicalNot), 

			new ParamFunction(opIncr), 
			new ParamFunction(opDecr), 
			new ParamFunction(opLeftShift), 
			new ParamFunction(opRightShift), 
			new ParamFunction(opRightShiftZF), 
				
			new ParamFunction(opEqualsStr), 
			new ParamFunction(opEqualsStrIgnoreCase), 
			new ParamFunction(opEqualsClassAssign), 
			new ParamFunction(opNegate)
	};
	
	public interface StackOperand { 
		
			public String getTypeStr();

			public Object getValue();
		
	}
	
	static Stack<StackOperand> stack;

	public static class StringOperand implements StackOperand { 
		String e;
		public StringOperand(String e) {
			this.e = e;
		}

		public String getTypeStr() { return "String"; }

		@Override
		public Object getValue() {
			// TODO Auto-generated method stub
			return this.e;
		}
		
	}

	public static class OperatorOperand implements StackOperand {
		Object e;
		public OperatorOperand(Object e) {
				this.e = e;
		}

		public String getTypeStr() { return "Operator"; }

		@Override
		public Integer getValue() {
			// TODO Auto-generated method stub
			return null;
		}

	}

	public static class NumberOperand implements StackOperand { 
		public String getTypeStr() { return "Number"; }

		@Override
		public Integer getValue() {
			// TODO Auto-generated method stub
			return null;
		}
			
	}

	public static class IntegerOperand implements StackOperand {

		int num; 
		public IntegerOperand(int a) {
			this.num = a;
		}
		
		@Override
		public String getTypeStr() {
			return "Integer";
		}

		@Override
		public Object getValue() {
			return this.num;
		}
			
	}

	public static class FloatOperand implements StackOperand { 
		Object d;
			public FloatOperand(Object d) {
					this.d = d;
			}

			public String getTypeStr() { return "Float"; }

			@Override
			public Object getValue() {
				return null;
			}
	}

	public static class BooleanOperand implements StackOperand { 
		boolean x;
		
		public BooleanOperand(boolean x) {
			this.x = x;
		}
		
			public String getTypeStr() { return "Boolean"; }

			@Override
			public Object getValue() {
				return this.x;
			}

	}

	public static class VariableOperand extends StringOperand { 

		public VariableOperand(String e) {
				super(e);
		}

		public String getTypeStr() { return "Variable"; }
		
	}


	public ResolverOps(Stack operands) {
			this.stack = operands;
	}

	public static boolean expectOperator(Object item) {


		if(item instanceof StringOperand) {
			StringOperand spn = (StringOperand) item;

			String ops = (String) spn.getValue();
			
			int index = getOperator(ops);
			
			if(index != -1) {
				
				ParamFunction pfunc = operatorFunctions[index];
				
				if(pfunc.getOrder() == 2) {	// bifunction
					
					if(stack.size() >= 3) {
						
						StackOperand sop1 = (StackOperand) stack.pop();
						StackOperand sop2 = (StackOperand) stack.pop();
						StackOperand sop3 = (StackOperand) stack.pop();
					
						
						if(sop1.getTypeStr().equals("Integer")) {
						
							Integer param1 = (Integer) sop1.getValue();
							Integer param2 = (Integer) sop3.getValue();
							
							pfunc.getStepFunc2().apply(param1, param2);
							
						}
					}
					
				} else  {			// function
					
					
					
				}
				
				stack.push(new OperatorOperand(item));
				
					return true;								
			}

				
		}
		
		return false;
	}

	public static int getOperator(String ops) {

		for(int i=0;i<operators.length;i++)
			if(ops.equals(operators[i]))
				return i;

		return -1;
	}

	public static boolean expectGroup(Object item) {
	
		if(item instanceof StringOperand) {
			StringOperand spn = (StringOperand) item;

			String str4 = (String) spn.getValue();
		
			if(str4.equals( "(" ) )				
				stack.push(new StringOperand("START_GROUP"));
			else if(str4.equals( ")" ) )				
				stack.push(new StringOperand("END_GROUP"));
			else
				return false;
			
		}

		return true;	
	}
	
	
}
