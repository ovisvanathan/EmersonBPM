package com.emerson.bpm.rule.parser.sax;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import org.antlr.runtime.CommonToken;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.MismatchedTokenException;
import org.antlr.runtime.MissingTokenException;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.Token;
import org.antlr.runtime.TokenStream;
import org.antlr.runtime.UnwantedTokenException;
import org.drools.compiler.lang.DroolsEditorType;
import org.drools.compiler.lang.DroolsMismatchedTokenException;
import org.drools.compiler.lang.DroolsSentence;
import org.drools.compiler.lang.DroolsSoftKeywords;
import org.drools.compiler.lang.Location;
import org.drools.compiler.lang.api.AccumulateDescrBuilder;
import org.drools.compiler.lang.api.AnnotatedDescrBuilder;
import org.drools.compiler.lang.api.AnnotationDescrBuilder;
import org.drools.compiler.lang.api.AttributeDescrBuilder;
import org.drools.compiler.lang.api.AttributeSupportBuilder;
import org.drools.compiler.lang.api.BehaviorDescrBuilder;
import org.drools.compiler.lang.api.CEDescrBuilder;
import org.drools.compiler.lang.api.CollectDescrBuilder;
import org.drools.compiler.lang.api.ConditionalBranchDescrBuilder;
import org.drools.compiler.lang.api.DescrBuilder;
import org.drools.compiler.lang.api.EvalDescrBuilder;
import org.drools.compiler.lang.api.ForallDescrBuilder;
import org.drools.compiler.lang.api.NamedConsequenceDescrBuilder;
import org.drools.compiler.lang.api.PackageDescrBuilder;
import org.drools.compiler.lang.api.PatternContainerDescrBuilder;
import org.drools.compiler.lang.api.PatternDescrBuilder;
import org.drools.compiler.lang.api.RuleDescrBuilder;
import org.drools.compiler.lang.descr.AndDescr;
import org.drools.compiler.lang.descr.AttributeDescr;
import org.drools.compiler.lang.descr.BaseDescr;
import org.drools.compiler.lang.descr.ConditionalElementDescr;
import org.drools.compiler.lang.descr.ExistsDescr;
import org.drools.compiler.lang.descr.NotDescr;
import org.drools.compiler.lang.descr.OrDescr;
import org.drools.compiler.lang.descr.RuleDescr;
import org.drools.core.util.StringUtils;

import com.emerson.bpm.rule.parser.DRL6Expressions;
import com.emerson.bpm.rule.parser.DRL6Lexer;
import com.emerson.bpm.rule.parser.DRL6Parser;
import com.emerson.bpm.rule.parser.IDRLParser;

public class DRL6EventParser extends DRL6Parser implements IDRLParser {
	
	StatementHandler handler;
    
    SAXEventsTreeSet ruleset = new SAXEventsTreeSet();
    
	private final DRL6Expressions exprParser0;

	public DRL6EventParser(CommonTokenStream stream) {
		super(stream);
		handler = new StatementHandler();
        this.exprParser0 = new DRL6Expressions(input, state, helper);
	}
	
    public RuleDescr rule(PackageDescrBuilder pkg) throws RecognitionException {

        RuleDescrBuilder rule = null;
        try {
            rule = helper.start(pkg,
                    RuleDescrBuilder.class,
                    null);
                        
            // 'rule'
            match(input,
                    DRL6Lexer.ID,
                    DroolsSoftKeywords.RULE,
                    null,
                    DroolsEditorType.KEYWORD);
            
       
            String name = stringId();
            
            while (input.LA(1) == DRL6Lexer.AT) {
                // annotation*
                annotation(rule);
                if (state.failed)
                    return null;
            }

            attributes(rule);
            
            ruleset.add("rule", rule);
            
            if (helper.validateIdentifierKey(DroolsSoftKeywords.WHEN)) {
                lhs(rule);
            } else {
                // creates an empty LHS
                rule.lhs();
            }
            
            rhs(rule);

            match(input,
                    DRL6Lexer.ID,
                    DroolsSoftKeywords.END,
                    null,
                    DroolsEditorType.KEYWORD);
            if (state.failed)
                return null;
            
        } catch (RecognitionException re) {
            reportError(re);
        } finally {
            helper.end(RuleDescrBuilder.class,
                    rule);
        }
        return (rule != null) ? rule.getDescr() : null;
    	
    }
    
    String stringId() throws RecognitionException {
        if (input.LA(1) == DRL6Lexer.ID) {
            Token id = match(input,
                    DRL6Lexer.ID,
                    null,
                    null,
                    DroolsEditorType.IDENTIFIER);
            if (state.failed)
                return null;
            return id.getText();
        } else if (input.LA(1) == DRL6Lexer.STRING) {
            Token id = match(input,
                    DRL6Lexer.STRING,
                    null,
                    null,
                    DroolsEditorType.IDENTIFIER);
            if (state.failed)
                return null;
            return StringUtils.unescapeJava(safeStripStringDelimiters(id.getText()));
        } else {
            throw new MismatchedTokenException(DRL6Lexer.ID,
                    input);
        }
    }
    
    /**
     * attributes := (ATTRIBUTES COLON?)? [ attribute ( COMMA? attribute )* ]
     * @param rule
     * @throws org.antlr.runtime.RecognitionException
     */
    void attributes( RuleDescrBuilder rule ) throws RecognitionException {
        if (helper.validateIdentifierKey(DroolsSoftKeywords.ATTRIBUTES)) {
            match(input,
                    DRL6Lexer.ID,
                    DroolsSoftKeywords.ATTRIBUTES,
                    null,
                    DroolsEditorType.IDENTIFIER);
            if (state.failed)
                return;

            if (input.LA(1) == DRL6Lexer.COLON) {
                match(input,
                        DRL6Lexer.COLON,
                        null,
                        null,
                        DroolsEditorType.SYMBOL);
                if (state.failed)
                    return;
            }
        }

        if (helper.validateAttribute(1)) {
            attribute(rule);
            if (state.failed)
                return;

            while (input.LA(1) == DRL6Lexer.COMMA || helper.validateAttribute(1)) {
                if (input.LA(1) == DRL6Lexer.COMMA) {
                    match(input,
                            DRL6Lexer.COMMA,
                            null,
                            null,
                            DroolsEditorType.SYMBOL);
                    if (state.failed)
                        return;
                }
                attribute(rule);
                if (state.failed)
                    return;
            }
        }
    }
    
    /** 
     *  Match current input symbol against ttype and optionally
     *  check the text of the token against text.  Attempt
     *  single token insertion or deletion error recovery.  If
     *  that fails, throw MismatchedTokenException.
     */
    Token match( TokenStream input,
                 int ttype,
                 String text,
                 int[] follow,
                 DroolsEditorType etype ) throws RecognitionException {
        Token matchedSymbol = null;
        matchedSymbol = input.LT(1);
        if (input.LA(1) == ttype && (text == null || text.equals(matchedSymbol.getText()))) {
            input.consume();
            state.errorRecovery = false;
            state.failed = false;
            helper.emit(matchedSymbol,
                    etype);
            return matchedSymbol;
        }
        if (state.backtracking > 0) {
            state.failed = true;
            return matchedSymbol;
        }
        matchedSymbol = recoverFromMismatchedToken(input,
                ttype,
                text,
                follow);
        helper.emit(matchedSymbol,
                etype);
        return matchedSymbol;
    }

    /** Attempt to recover from a single missing or extra token.
    *
    *  EXTRA TOKEN
    *
    *  LA(1) is not what we are looking for.  If LA(2) has the right token,
    *  however, then assume LA(1) is some extra spurious token.  Delete it
    *  and LA(2) as if we were doing a normal match(), which advances the
    *  input.
    *
    *  MISSING TOKEN
    *
    *  If current token is consistent with what could come after
    *  ttype then it is ok to "insert" the missing token, else throw
    *  exception For example, Input "i=(3;" is clearly missing the
    *  ')'.  When the parser returns from the nested call to expr, it
    *  will have call chain:
    *
    *    stat -> expr -> atom
    *
    *  and it will be trying to match the ')' at this point in the
    *  derivation:
    *
    *       => ID '=' '(' INT ')' ('+' atom)* ';'
    *                          ^
    *  match() will see that ';' doesn't match ')' and report a
    *  mismatched token error.  To recover, it sees that LA(1)==';'
    *  is in the set of tokens that can follow the ')' token
    *  reference in rule atom.  It can assume that you forgot the ')'.
    */
    protected Token recoverFromMismatchedToken(TokenStream input,
            int ttype,
            String text,
            int[] follow)
            throws RecognitionException {
        RecognitionException e = null;
        // if next token is what we are looking for then "delete" this token
        if (mismatchIsUnwantedToken(input,
                ttype,
                text)) {
            e = new UnwantedTokenException(ttype,
                    input);
            input.consume(); // simply delete extra token
            reportError(e); // report after consuming so AW sees the token in the exception
            // we want to return the token we're actually matching
            Token matchedSymbol = input.LT(1);
            input.consume(); // move past ttype token as if all were ok
            return matchedSymbol;
        }
        // can't recover with single token deletion, try insertion
        if (mismatchIsMissingToken(input,
                follow)) {
            e = new MissingTokenException(ttype,
                    input,
                    null);
            reportError(e); // report after inserting so AW sees the token in the exception
            return null;
        }
        // even that didn't work; must throw the exception
        if (text != null) {
            e = new DroolsMismatchedTokenException(ttype,
                    text,
                    input);
        } else {
            e = new MismatchedTokenException(ttype,
                    input);
        }
        throw e;
    }

    public boolean mismatchIsUnwantedToken(TokenStream input,
            int ttype,
            String text) {
        return (input.LA(2) == ttype && (text == null || text.equals(input.LT(2).getText())));
    }

    public boolean mismatchIsMissingToken(TokenStream input,
            int[] follow) {
        if (follow == null) {
            // we have no information about the follow; we can only consume
            // a single token and hope for the best
            return false;
        }
        // TODO: implement this error recovery strategy
        return false;
    }

    private String safeStripDelimiters(String value,
            String left,
            String right) {
        if (value != null) {
            value = value.trim();
            if (value.length() >= left.length() + right.length() &&
                    value.startsWith(left) && value.endsWith(right)) {
                value = value.substring(left.length(),
                        value.length() - right.length());
            }
        }
        return value;
    }

    private String safeStripStringDelimiters(String value) {
        if (value != null) {
            value = value.trim();
            if (value.length() >= 2 && value.startsWith("\"") && value.endsWith("\"")) {
                value = value.substring(1,
                        value.length() - 1);
            }
        }
        return value;
    }

    
    /**
     * annotation := fullAnnotation | AT ID chunk_(_)?
     */
    void annotation( AnnotatedDescrBuilder<?> adb ) {
        AnnotationDescrBuilder<?> annotation = null;

        try {
            if (speculateFullAnnotation()) {
                boolean buildState = exprParser0.isBuildDescr();
                exprParser0.setBuildDescr(true);
                exprParser0.fullAnnotation(adb);
                exprParser0.setBuildDescr(buildState);
            } else {

                // '@'
                Token at = match(input,
                        DRL6Lexer.AT,
                        null,
                        null,
                        DroolsEditorType.SYMBOL);
                if (state.failed)
                    return;

                // identifier
                String fqn = qualifiedIdentifier();
                if (state.failed)
                    return;

                if (state.backtracking == 0) {
                    annotation = adb.newAnnotation(fqn);
                    helper.setStart(annotation,
                            at);
                }

                try {
                    if (input.LA(1) == DRL6Lexer.LEFT_PAREN) {
                        String value = chunk(DRL6Lexer.LEFT_PAREN,
                                DRL6Lexer.RIGHT_PAREN,
                                -1).trim();
                        if (state.failed)
                            return;
                        if (state.backtracking == 0) {
                            if (annotation != null) {
                                annotation.value(value);
                            } else {
                                throw new RecognitionException();
                            }
                        }
                    }
                } finally {
                    if (state.backtracking == 0) {
                        helper.setEnd(annotation);
                    }
                }
            }

        } catch (RecognitionException re) {
            reportError(re);
        }
    }

    /**
     * Invokes the expression parser, trying to parse the annotation
     * as a full java-style annotation
     *
     * @return true if the sequence of tokens will match the
     *         elementValuePairs() syntax. false otherwise.
     */
    private boolean speculateFullAnnotation() {
        state.backtracking++;
        int start = input.mark();
        try {
            exprParser0.fullAnnotation(null);
        } catch (RecognitionException re) {
            System.err.println("impossible: " + re);
            re.printStackTrace();
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed = false;
        return success;

    }

    
    /**
     * attribute :=
     *       salience
     *   |   enabled
     *   |   ( NO-LOOP
     *       | AUTO-FOCUS
     *       | LOCK-ON-ACTIVE
     *       | REFRACT
     *       | DIRECT
     *       ) BOOLEAN?
     *   |   ( AGENDA-GROUP
     *       | ACTIVATION-GROUP
     *       | RULEFLOW-GROUP
     *       | DATE-EFFECTIVE
     *       | DATE-EXPIRES
     *       | DIALECT
     *       ) STRING
     *   |   CALENDARS STRING (COMMA STRING)*
     *   |   TIMER ( DECIMAL | chunk_(_) )
     *   |   DURATION ( DECIMAL | chunk_(_) )
     *
     * The above syntax is not quite how this is parsed, because the soft keyword
     * is determined by look-ahead and passed on to one of the x-Attribute methods
     * (booleanAttribute, stringAttribute, stringListAttribute, intOrChunkAttribute)
     * which will actually gobble the tokens.
     *
     * @return
     */
    public AttributeDescr attribute(AttributeSupportBuilder<?> as) {
        AttributeDescr attribute = null;
        try {
            if (state.backtracking == 0 && input.LA(1) != DRL6Lexer.EOF) {
                helper.emit(Location.LOCATION_RULE_HEADER_KEYWORD);
            }
            if (helper.validateIdentifierKey(DroolsSoftKeywords.SALIENCE)) {
                attribute = salience(as);
            } else if (helper.validateIdentifierKey(DroolsSoftKeywords.ENABLED)) {
                attribute = enabled(as);
            } else if (helper.validateIdentifierKey(DroolsSoftKeywords.NO) &&
                    helper.validateLT(2,
                            "-") &&
                    helper.validateLT(3,
                            DroolsSoftKeywords.LOOP)) {
                attribute = booleanAttribute(as,
                        new String[]{DroolsSoftKeywords.NO, "-", DroolsSoftKeywords.LOOP});
            } else if (helper.validateIdentifierKey(DroolsSoftKeywords.AUTO) &&
                    helper.validateLT(2,
                            "-") &&
                    helper.validateLT(3,
                            DroolsSoftKeywords.FOCUS)) {
                attribute = booleanAttribute(as,
                        new String[]{DroolsSoftKeywords.AUTO, "-", DroolsSoftKeywords.FOCUS});
            } else if (helper.validateIdentifierKey(DroolsSoftKeywords.LOCK) &&
                    helper.validateLT(2,
                            "-") &&
                    helper.validateLT(3,
                            DroolsSoftKeywords.ON) &&
                    helper.validateLT(4,
                            "-") &&
                    helper.validateLT(5,
                            DroolsSoftKeywords.ACTIVE)) {
                attribute = booleanAttribute(as,
                        new String[]{DroolsSoftKeywords.LOCK, "-", DroolsSoftKeywords.ON, "-", DroolsSoftKeywords.ACTIVE});
            } else if (helper.validateIdentifierKey(DroolsSoftKeywords.REFRACT)) {
                attribute = booleanAttribute(as,
                        new String[]{DroolsSoftKeywords.REFRACT});
            } else if (helper.validateIdentifierKey(DroolsSoftKeywords.DIRECT)) {
                attribute = booleanAttribute(as,
                        new String[]{DroolsSoftKeywords.DIRECT});
            } else if (helper.validateIdentifierKey(DroolsSoftKeywords.AGENDA) &&
                    helper.validateLT(2,
                            "-") &&
                    helper.validateLT(3,
                            DroolsSoftKeywords.GROUP)) {
                attribute = stringAttribute(as,
                        new String[]{DroolsSoftKeywords.AGENDA, "-", DroolsSoftKeywords.GROUP});
            } else if (helper.validateIdentifierKey(DroolsSoftKeywords.ACTIVATION) &&
                    helper.validateLT(2,
                            "-") &&
                    helper.validateLT(3,
                            DroolsSoftKeywords.GROUP)) {
                attribute = stringAttribute(as,
                        new String[]{DroolsSoftKeywords.ACTIVATION, "-", DroolsSoftKeywords.GROUP});
            } else if (helper.validateIdentifierKey(DroolsSoftKeywords.RULEFLOW) &&
                    helper.validateLT(2,
                            "-") &&
                    helper.validateLT(3,
                            DroolsSoftKeywords.GROUP)) {
                attribute = stringAttribute(as,
                        new String[]{DroolsSoftKeywords.RULEFLOW, "-", DroolsSoftKeywords.GROUP});
            } else if (helper.validateIdentifierKey(DroolsSoftKeywords.DATE) &&
                    helper.validateLT(2,
                            "-") &&
                    helper.validateLT(3,
                            DroolsSoftKeywords.EFFECTIVE)) {
                attribute = stringAttribute(as,
                        new String[]{DroolsSoftKeywords.DATE, "-", DroolsSoftKeywords.EFFECTIVE});
                if (attribute != null) {
                    attribute.setType(AttributeDescr.Type.DATE);
                } else {
                    throw new RecognitionException();
                }
            } else if (helper.validateIdentifierKey(DroolsSoftKeywords.DATE) &&
                    helper.validateLT(2,
                            "-") &&
                    helper.validateLT(3,
                            DroolsSoftKeywords.EXPIRES)) {
                attribute = stringAttribute(as,
                        new String[]{DroolsSoftKeywords.DATE, "-", DroolsSoftKeywords.EXPIRES});
                if (attribute != null) {
                    attribute.setType(AttributeDescr.Type.DATE);
                } else {
                    throw new RecognitionException();
                }
            } else if (helper.validateIdentifierKey(DroolsSoftKeywords.DIALECT)) {
                attribute = stringAttribute(as,
                        new String[]{DroolsSoftKeywords.DIALECT});
            } else if (helper.validateIdentifierKey(DroolsSoftKeywords.CALENDARS)) {
                attribute = stringListAttribute(as,
                        new String[]{DroolsSoftKeywords.CALENDARS});
            } else if (helper.validateIdentifierKey(DroolsSoftKeywords.TIMER)) {
                attribute = intOrChunkAttribute(as,
                        new String[]{DroolsSoftKeywords.TIMER});
            } else if (helper.validateIdentifierKey(DroolsSoftKeywords.DURATION)) {
                attribute = intOrChunkAttribute(as,
                        new String[]{DroolsSoftKeywords.DURATION});
            }
            if (state.backtracking == 0) {
                helper.emit(Location.LOCATION_RULE_HEADER);
            }
        } catch (RecognitionException re) {
            reportError(re);
        }
        return attribute;
    }

    /**
     * salience := SALIENCE conditionalExpression
     * @throws org.antlr.runtime.RecognitionException
     */
    private AttributeDescr salience(AttributeSupportBuilder<?> as) throws RecognitionException {
        AttributeDescrBuilder<?> attribute = null;
        try {
            // 'salience'
            match(input,
                    DRL6Lexer.ID,
                    DroolsSoftKeywords.SALIENCE,
                    null,
                    DroolsEditorType.KEYWORD);
            if (state.failed)
                return null;
            if (state.backtracking == 0) {
                attribute = helper.start((DescrBuilder<?, ?>) as,
                        AttributeDescrBuilder.class,
                        DroolsSoftKeywords.SALIENCE);
            }

            boolean hasParen = input.LA(1) == DRL6Lexer.LEFT_PAREN;
            int first = input.index();
            if (hasParen) {
                match(input,
                        DRL6Lexer.LEFT_PAREN,
                        null,
                        null,
                        DroolsEditorType.SYMBOL);
                if (state.failed)
                    return null;
            }

            String value = conditionalExpression();
            if (state.failed)
                return null;

            if (hasParen) {
                match(input,
                        DRL6Lexer.RIGHT_PAREN,
                        null,
                        null,
                        DroolsEditorType.SYMBOL);
                if (state.failed)
                    return null;
            }
            if (state.backtracking == 0) {
                if (attribute != null) {
                    if (hasParen) {
                        value = input.toString(first,
                                               input.LT(-1).getTokenIndex());
                    }
                    attribute.value(value);
                    attribute.type(AttributeDescr.Type.EXPRESSION);
                }
            }

        } finally {
            if (attribute != null) {
                helper.end(AttributeDescrBuilder.class,
                        attribute);
            }
        }
        return attribute != null ? attribute.getDescr() : null;
    }

    /**
     * enabled := ENABLED conditionalExpression
     * @throws org.antlr.runtime.RecognitionException
     */
    private AttributeDescr enabled(AttributeSupportBuilder<?> as) throws RecognitionException {
        AttributeDescrBuilder<?> attribute = null;
        try {
            // 'enabled'
            match(input,
                    DRL6Lexer.ID,
                    DroolsSoftKeywords.ENABLED,
                    null,
                    DroolsEditorType.KEYWORD);
            if (state.failed)
                return null;
            if (state.backtracking == 0) {
                attribute = helper.start((DescrBuilder<?, ?>) as,
                        AttributeDescrBuilder.class,
                        DroolsSoftKeywords.ENABLED);
            }

            boolean hasParen = input.LA(1) == DRL6Lexer.LEFT_PAREN;
            int first = input.index();
            if (hasParen) {
                match(input,
                        DRL6Lexer.LEFT_PAREN,
                        null,
                        null,
                        DroolsEditorType.SYMBOL);
                if (state.failed)
                    return null;
            }

            String value = conditionalExpression();
            if (state.failed)
                return null;

            if (hasParen) {
                match(input,
                        DRL6Lexer.RIGHT_PAREN,
                        null,
                        null,
                        DroolsEditorType.SYMBOL);
                if (state.failed)
                    return null;
            }
            if (state.backtracking == 0) {
                if (attribute != null) {
                    if (hasParen) {
                        value = input.toString(first,
                                               input.LT(-1).getTokenIndex());
                    }
                    attribute.value(value);
                    attribute.type(AttributeDescr.Type.EXPRESSION);
                }
            }

        } finally {
            if (attribute != null) {
                helper.end(AttributeDescrBuilder.class,
                        attribute);
            }
        }
        return attribute != null ? attribute.getDescr() : null;
    }

    /**
     * booleanAttribute := attributeKey (BOOLEAN)?
     * @param key
     * @throws org.antlr.runtime.RecognitionException
     */
    private AttributeDescr booleanAttribute(AttributeSupportBuilder<?> as,
            String[] key) throws RecognitionException {
        AttributeDescrBuilder<?> attribute = null;
        try {
            StringBuilder builder = new StringBuilder();
            for (String k : key) {
                if ("-".equals(k)) {
                    match(input,
                            DRL6Lexer.MINUS,
                            k,
                            null,
                            DroolsEditorType.KEYWORD); // part of the keyword
                    if (state.failed)
                        return null;
                } else {
                    match(input,
                            DRL6Lexer.ID,
                            k,
                            null,
                            DroolsEditorType.KEYWORD);
                    if (state.failed)
                        return null;
                }
                builder.append(k);
            }
            if (state.backtracking == 0) {
                attribute = helper.start((DescrBuilder<?, ?>) as,
                        AttributeDescrBuilder.class,
                        builder.toString());
            }

            String value = "true";
            if (input.LA(1) == DRL6Lexer.BOOL) {
                Token bool = match(input,
                        DRL6Lexer.BOOL,
                        null,
                        null,
                        DroolsEditorType.KEYWORD);
                if (state.failed)
                    return null;
                value = bool.getText();
            }
            if (state.backtracking == 0) {
                if (attribute != null) {
                    attribute.value(value);
                    attribute.type(AttributeDescr.Type.BOOLEAN);
                }
            }
        } finally {
            if (attribute != null) {
                helper.end(AttributeDescrBuilder.class,
                        attribute);
            }
        }
        return attribute != null ? attribute.getDescr() : null;
    }

    /**
     * stringAttribute := attributeKey STRING
     * @param key
     * @throws org.antlr.runtime.RecognitionException
     */
    private AttributeDescr stringAttribute(AttributeSupportBuilder<?> as,
            String[] key) throws RecognitionException {
        AttributeDescrBuilder<?> attribute = null;
        try {
            StringBuilder builder = new StringBuilder();
            for (String k : key) {
                if ("-".equals(k)) {
                    match(input,
                            DRL6Lexer.MINUS,
                            k,
                            null,
                            DroolsEditorType.KEYWORD); // part of the keyword
                    if (state.failed)
                        return null;
                } else {
                    match(input,
                            DRL6Lexer.ID,
                            k,
                            null,
                            DroolsEditorType.KEYWORD);
                    if (state.failed)
                        return null;
                }
                builder.append(k);
            }
            if (state.backtracking == 0) {
                attribute = helper.start((DescrBuilder<?, ?>) as,
                        AttributeDescrBuilder.class,
                        builder.toString());
            }

            Token value = match(input,
                    DRL6Lexer.STRING,
                    null,
                    null,
                    DroolsEditorType.STRING_CONST);
            if (state.failed)
                return null;
            if (state.backtracking == 0) {
                if (attribute != null) {
                    attribute.value(StringUtils.unescapeJava(safeStripStringDelimiters(value.getText())));
                    attribute.type(AttributeDescr.Type.STRING);
                }
            }
        } finally {
            if (attribute != null) {
                helper.end(AttributeDescrBuilder.class,
                        attribute);
            }
        }
        return attribute != null ? attribute.getDescr() : null;
    }

    /**
     * stringListAttribute := attributeKey STRING (COMMA STRING)*
     * @param key
     * @throws org.antlr.runtime.RecognitionException
     */
    private AttributeDescr stringListAttribute(AttributeSupportBuilder<?> as,
            String[] key) throws RecognitionException {
        AttributeDescrBuilder<?> attribute = null;
        try {
            StringBuilder builder = new StringBuilder();
            for (String k : key) {
                if ("-".equals(k)) {
                    match(input,
                            DRL6Lexer.MINUS,
                            k,
                            null,
                            DroolsEditorType.KEYWORD); // part of the keyword
                    if (state.failed)
                        return null;
                } else {
                    match(input,
                            DRL6Lexer.ID,
                            k,
                            null,
                            DroolsEditorType.KEYWORD);
                    if (state.failed)
                        return null;
                }
                builder.append(k);
            }
            if (state.backtracking == 0) {
                attribute = helper.start((DescrBuilder<?, ?>) as,
                        AttributeDescrBuilder.class,
                        builder.toString());
            }

            builder = new StringBuilder();
            builder.append("[ ");
            Token value = match(input,
                    DRL6Lexer.STRING,
                    null,
                    null,
                    DroolsEditorType.STRING_CONST);
            if (state.failed)
                return null;
            builder.append(value.getText());

            while (input.LA(1) == DRL6Lexer.COMMA) {
                match(input,
                        DRL6Lexer.COMMA,
                        null,
                        null,
                        DroolsEditorType.SYMBOL);
                if (state.failed)
                    return null;
                builder.append(", ");
                value = match(input,
                        DRL6Lexer.STRING,
                        null,
                        null,
                        DroolsEditorType.STRING_CONST);
                if (state.failed)
                    return null;
                builder.append(value.getText());
            }
            builder.append(" ]");
            if (state.backtracking == 0) {
                if (attribute != null) {
                    attribute.value(builder.toString());
                    attribute.type(AttributeDescr.Type.LIST);
                }
            }
        } finally {
            if (attribute != null) {
                helper.end(AttributeDescrBuilder.class,
                        attribute);
            }
        }
        return attribute != null ? attribute.getDescr() : null;
    }

    /**
     * intOrChunkAttribute := attributeKey ( DECIMAL | chunk_(_) )
     * @param key
     * @throws org.antlr.runtime.RecognitionException
     */
    private AttributeDescr intOrChunkAttribute(AttributeSupportBuilder<?> as,
            String[] key) throws RecognitionException {
        AttributeDescrBuilder<?> attribute = null;
        try {
            StringBuilder builder = new StringBuilder();
            for (String k : key) {
                if ("-".equals(k)) {
                    match(input,
                            DRL6Lexer.MINUS,
                            k,
                            null,
                            DroolsEditorType.KEYWORD); // part of the keyword
                    if (state.failed)
                        return null;
                } else {
                    match(input,
                            DRL6Lexer.ID,
                            k,
                            null,
                            DroolsEditorType.KEYWORD);
                    if (state.failed)
                        return null;
                }
                builder.append(k);
            }
            if (state.backtracking == 0) {
                attribute = helper.start((DescrBuilder<?, ?>) as,
                        AttributeDescrBuilder.class,
                        builder.toString());
            }

            if (input.LA(1) == DRL6Lexer.LEFT_PAREN) {
                String value = chunk(DRL6Lexer.LEFT_PAREN,
                        DRL6Lexer.RIGHT_PAREN,
                        -1);
                if (state.failed)
                    return null;
                if (state.backtracking == 0) {
                    if (attribute != null) {
                        attribute.value(safeStripDelimiters(value,
                                                            "(",
                                                            ")"));
                        attribute.type(AttributeDescr.Type.EXPRESSION);
                    }
                }
            } else {
                String value = "";
                if (input.LA(1) == DRL6Lexer.PLUS) {
                    Token sign = match(input,
                            DRL6Lexer.PLUS,
                            null,
                            null,
                            DroolsEditorType.NUMERIC_CONST);
                    if (state.failed)
                        return null;
                    value += sign.getText();
                } else if (input.LA(1) == DRL6Lexer.MINUS) {
                    Token sign = match(input,
                            DRL6Lexer.MINUS,
                            null,
                            null,
                            DroolsEditorType.NUMERIC_CONST);
                    if (state.failed)
                        return null;
                    value += sign.getText();
                }
                Token nbr = match(input,
                        DRL6Lexer.DECIMAL,
                        null,
                        null,
                        DroolsEditorType.NUMERIC_CONST);
                if (state.failed)
                    return null;
                value += nbr.getText();
                if (state.backtracking == 0) {
                    if (attribute != null) {
                        attribute.value(value);
                        attribute.type(AttributeDescr.Type.NUMBER);
                    }
                }
            }
        } finally {
            if (attribute != null) {
                helper.end(AttributeDescrBuilder.class,
                        attribute);
            }
        }
        return attribute != null ? attribute.getDescr() : null;
    }

      
    
    /**
     * lhs := WHEN COLON? lhsExpression
     * @param rule
     * @throws org.antlr.runtime.RecognitionException
     */
    void lhs( RuleDescrBuilder rule ) throws RecognitionException {
        match(input,
                DRL6Lexer.ID,
                DroolsSoftKeywords.WHEN,
                null,
                DroolsEditorType.KEYWORD);
        if (state.failed)
            return;

        if (input.LA(1) == DRL6Lexer.COLON) {
            match(input,
                    DRL6Lexer.COLON,
                    null,
                    null,
                    DroolsEditorType.SYMBOL);
            if (state.failed)
                return;
        }

        lhsExpression(rule != null ? rule.lhs() : null);
    }

    /**
     * lhsExpression := lhsOr*
     *
     * @param lhs
     * @throws org.antlr.runtime.RecognitionException
     */
    private void lhsExpression(CEDescrBuilder<?, AndDescr> lhs) throws RecognitionException {
        helper.start(lhs,
                CEDescrBuilder.class,
                null);
        
        ruleset.add("lhs", lhs);
        
        if (state.backtracking == 0) {
            helper.emit(Location.LOCATION_LHS_BEGIN_OF_CONDITION);
        }
        try {
            while (input.LA(1) != DRL6Lexer.EOF &&
                    !helper.validateIdentifierKey(DroolsSoftKeywords.THEN) &&
                    !helper.validateIdentifierKey(DroolsSoftKeywords.END)) {
                if (state.backtracking == 0) {
                    helper.emit(Location.LOCATION_LHS_BEGIN_OF_CONDITION);
                }
                lhsOr(lhs,
                        true);

                if (lhs.getDescr() != null && lhs.getDescr() instanceof ConditionalElementDescr) {
                    ConditionalElementDescr root = (ConditionalElementDescr) lhs.getDescr();
                    BaseDescr[] descrs = root.getDescrs().toArray(new BaseDescr[root.getDescrs().size()]);
                    root.getDescrs().clear();
                    for (int i = 0; i < descrs.length; i++) {
                        root.addOrMerge(descrs[i]);
                    }
                }
                if (state.failed)
                    return;
            }
        } finally {
            helper.end(CEDescrBuilder.class,
                    lhs);
        }
    }

    /**
     * lhsOr := LEFT_PAREN OR lhsAnd+ RIGHT_PAREN
     *        | lhsAnd (OR lhsAnd)*
     *
     * @param ce
     * @param allowOr
     * @throws org.antlr.runtime.RecognitionException
     */
    private BaseDescr lhsOr(final CEDescrBuilder<?, ?> ce,
            boolean allowOr) throws RecognitionException {
        BaseDescr result = null;
        if (allowOr && input.LA(1) == DRL6Lexer.LEFT_PAREN && helper.validateLT(2,
                DroolsSoftKeywords.OR)) {
            // prefixed OR
            CEDescrBuilder<?, OrDescr> or = null;
            if (state.backtracking == 0) {
                or = ce.or();
                result = or.getDescr();
                helper.start(or,
                        CEDescrBuilder.class,
                        null);
            }

            ruleset.add("lhsOR", or);

            try {
                match(input,
                        DRL6Lexer.LEFT_PAREN,
                        null,
                        null,
                        DroolsEditorType.SYMBOL);
                if (state.failed)
                    return null;

                match(input,
                        DRL6Lexer.ID,
                        DroolsSoftKeywords.OR,
                        null,
                        DroolsEditorType.KEYWORD);
                if (state.failed)
                    return null;

                while (input.LA(1) == DRL6Lexer.AT) {
                    // annotation*
                    annotation(or);
                    if (state.failed)
                        return null;
                }

                if (state.backtracking == 0) {
                    helper.emit(Location.LOCATION_LHS_BEGIN_OF_CONDITION_AND_OR);
                }
                while (input.LA(1) != DRL6Lexer.RIGHT_PAREN) {
                    lhsAnd(or,
                            allowOr);
                    if (state.failed)
                        return null;
                }

                match(input,
                        DRL6Lexer.RIGHT_PAREN,
                        null,
                        null,
                        DroolsEditorType.SYMBOL);
                if (state.failed)
                    return null;
            } finally {
                if (state.backtracking == 0) {
                    helper.end(CEDescrBuilder.class,
                            or);
                }
            }
        } else {
            // infix OR

            // create an OR anyway, as if it is not an OR we remove it later
            CEDescrBuilder<?, OrDescr> or = null;
            if (state.backtracking == 0) {
                or = ce.or();
                result = or.getDescr();
                helper.start(or,
                        CEDescrBuilder.class,
                        null);
            }
            ruleset.add("lhsOR", or);

            try {
                lhsAnd(or,
                        allowOr);

                
                if (state.failed)
                    return null;

                if (allowOr &&
                        (helper.validateIdentifierKey(DroolsSoftKeywords.OR)
                        ||
                        input.LA(1) == DRL6Lexer.DOUBLE_PIPE)) {
                    while (helper.validateIdentifierKey(DroolsSoftKeywords.OR) ||
                            input.LA(1) == DRL6Lexer.DOUBLE_PIPE) {
                        if (input.LA(1) == DRL6Lexer.DOUBLE_PIPE) {
                            match(input,
                                    DRL6Lexer.DOUBLE_PIPE,
                                    null,
                                    null,
                                    DroolsEditorType.SYMBOL);

                        } else {
                            match(input,
                                    DRL6Lexer.ID,
                                    DroolsSoftKeywords.OR,
                                    null,
                                    DroolsEditorType.KEYWORD);
                        }
                
                        if (state.failed)
                            return null;

                        while (input.LA(1) == DRL6Lexer.AT) {
                            // annotation*
                            annotation(or);
                            if (state.failed)
                                return null;
                        }

                        if (state.backtracking == 0) {
                            helper.emit(Location.LOCATION_LHS_BEGIN_OF_CONDITION_AND_OR);
                        }

                        lhsAnd(or,
                                allowOr);
                        if (state.failed)
                            return null;
                    }
                } else if (allowOr) {
                    if (state.backtracking == 0) {
                        // if no OR present, then remove it and add children to parent
                        ((ConditionalElementDescr) ce.getDescr()).getDescrs().remove(or.getDescr());
                        for (BaseDescr base : or.getDescr().getDescrs()) {
                            ((ConditionalElementDescr) ce.getDescr()).addDescr(base);
                        }
                        result = ce.getDescr();
                    }
                }
            } finally {
                if (state.backtracking == 0) {
                    helper.end(CEDescrBuilder.class,
                            or);
                }
            }
        }
        return result;
    }

    /**
     * lhsAnd := LEFT_PAREN AND lhsUnary+ RIGHT_PAREN
     *         | lhsUnary (AND lhsUnary)*
     *
     * @param ce
     * @throws org.antlr.runtime.RecognitionException
     */
    private BaseDescr lhsAnd(final CEDescrBuilder<?, ?> ce,
            boolean allowOr) throws RecognitionException {
        BaseDescr result = null;
        if (input.LA(1) == DRL6Lexer.LEFT_PAREN && helper.validateLT(2,
                DroolsSoftKeywords.AND)) {
            // prefixed AND
            CEDescrBuilder<?, AndDescr> and = null;
            if (state.backtracking == 0) {
                and = ce.and();
                result = ce.getDescr();
                helper.start(and,
                        CEDescrBuilder.class,
                        null);
            }
            
            ruleset.add("lhsAND", and);

            try {
                match(input,
                        DRL6Lexer.LEFT_PAREN,
                        null,
                        null,
                        DroolsEditorType.SYMBOL);
                if (state.failed)
                    return null;

                match(input,
                        DRL6Lexer.ID,
                        DroolsSoftKeywords.AND,
                        null,
                        DroolsEditorType.KEYWORD);
                if (state.failed)
                    return null;

                while (input.LA(1) == DRL6Lexer.AT) {
                    // annotation*
                    annotation(and);
                    if (state.failed)
                        return null;
                }

                if (state.backtracking == 0) {
                    helper.emit(Location.LOCATION_LHS_BEGIN_OF_CONDITION_AND_OR);
                }
                while (input.LA(1) != DRL6Lexer.RIGHT_PAREN) {
                    lhsUnary(and,
                            allowOr);
                    if (state.failed)
                        return null;
                }

                match(input,
                        DRL6Lexer.RIGHT_PAREN,
                        null,
                        null,
                        DroolsEditorType.SYMBOL);
                if (state.failed)
                    return null;
            } finally {
                if (state.backtracking == 0) {
                    helper.end(CEDescrBuilder.class,
                            and);
                }
            }
        } else {
            // infix AND

            // create an AND anyway, since if it is not an AND we remove it later
            CEDescrBuilder<?, AndDescr> and = null;
            if (state.backtracking == 0) {
                and = ce.and();
                result = and.getDescr();
                helper.start(and,
                        CEDescrBuilder.class,
                        null);

                ruleset.add("lhsAND", and);

            }
            try {
                lhsUnary(and,
                        allowOr);
                if (state.failed)
                    return null;

                if (helper.validateIdentifierKey(DroolsSoftKeywords.AND) ||
                        input.LA(1) == DRL6Lexer.DOUBLE_AMPER) {
                    while (helper.validateIdentifierKey(DroolsSoftKeywords.AND) ||
                            input.LA(1) == DRL6Lexer.DOUBLE_AMPER) {
                        if (input.LA(1) == DRL6Lexer.DOUBLE_AMPER) {
                            match(input,
                                    DRL6Lexer.DOUBLE_AMPER,
                                    null,
                                    null,
                                    DroolsEditorType.SYMBOL);
                        } else {
                            match(input,
                                    DRL6Lexer.ID,
                                    DroolsSoftKeywords.AND,
                                    null,
                                    DroolsEditorType.KEYWORD);
                        }
                        if (state.failed)
                            return null;

                        while (input.LA(1) == DRL6Lexer.AT) {
                            // annotation*
                            annotation(and);
                            if (state.failed)
                                return null;
                        }

                        if (state.backtracking == 0) {
                            helper.emit(Location.LOCATION_LHS_BEGIN_OF_CONDITION_AND_OR);
                        }
                        lhsUnary(and,
                                allowOr);
                        if (state.failed)
                            return null;
                    }
                } else {
                    if (state.backtracking == 0 && and.getDescr().getDescrs().size() < 2) {
                        // if no AND present, then remove it and add children to parent
                        ((ConditionalElementDescr) ce.getDescr()).getDescrs().remove(and.getDescr());
                        for (BaseDescr base : and.getDescr().getDescrs()) {
                            ((ConditionalElementDescr) ce.getDescr()).addDescr(base);
                        }
                        result = ce.getDescr();
                    }
                }
            } finally {
                if (state.backtracking == 0) {
                    helper.end(CEDescrBuilder.class,
                            and);
                }
            }
        }
        return result;
    }

    /**
     * lhsUnary :=
     *           ( lhsExists namedConsequence?
     *           | lhsNot namedConsequence?
     *           | lhsEval consequenceInvocation*
     *           | lhsForall
     *           | lhsAccumulate
     *           | LEFT_PAREN lhsOr RIGHT_PAREN namedConsequence?
     *           | lhsPatternBind consequenceInvocation*
     *           )
     *           SEMICOLON?
     *
     * @param ce
     * @return
     */
    private BaseDescr lhsUnary(final CEDescrBuilder<?, ?> ce,
            boolean allowOr) throws RecognitionException {
        BaseDescr result = null;
        if (helper.validateIdentifierKey(DroolsSoftKeywords.EXISTS)) {
            result = lhsExists(ce,
                    allowOr);
           
            ruleset.add("lhsEXISTS", ce);

            if (helper.validateIdentifierKey(DroolsSoftKeywords.DO)) {
                namedConsequence(ce, null);

                ruleset.add("lhsNAMED_CONSEQ", ce);

            }
        } else if (helper.validateIdentifierKey(DroolsSoftKeywords.NOT)) {
            result = lhsNot(ce,
                    allowOr);

            ruleset.add("lhsNOT", ce);

            if (helper.validateIdentifierKey(DroolsSoftKeywords.DO)) {
                namedConsequence(ce, null);

                ruleset.add("lhsNAMED_CONSEQ", ce);

            }
        } else if (helper.validateIdentifierKey(DroolsSoftKeywords.EVAL)) {
            result = lhsEval(ce);
            for (BaseDescr i = consequenceInvocation(ce); i != null; i = consequenceInvocation(ce))
                ;
        } else if (helper.validateIdentifierKey(DroolsSoftKeywords.FORALL)) {
            result = lhsForall(ce);
        } else if (helper.validateIdentifierKey(DroolsSoftKeywords.ACCUMULATE) || helper.validateIdentifierKey(DroolsSoftKeywords.ACC)) {
            result = lhsAccumulate(ce);
        } else if (input.LA(1) == DRL6Lexer.LEFT_PAREN) {
            // the order here is very important: this if branch must come before the lhsPatternBind below
            result = lhsParen(ce,
                    allowOr);
            if (helper.validateIdentifierKey(DroolsSoftKeywords.DO)) {
                namedConsequence(ce, null);
            }
        } else if (input.LA(1) == DRL6Lexer.ID || input.LA(1) == DRL6Lexer.QUESTION || input.LA( 1) == DRL6Lexer.DIV) {
            result = lhsPatternBind(ce,
                                    allowOr);
            for (BaseDescr i = consequenceInvocation(ce); i != null; i = consequenceInvocation(ce))
                ;
        } else {
            failMismatchedTokenException();
        }
        if (input.LA(1) == DRL6Lexer.SEMICOLON) {
            match(input,
                    DRL6Lexer.SEMICOLON,
                    null,
                    null,
                    DroolsEditorType.SYMBOL);
            if (state.failed)
                return null;
        }

        return result;
    }

    /**
     * consequenceInvocation := conditionalBranch | namedConsequence
     *
     * @param ce
     * @return
     */
    private BaseDescr consequenceInvocation(CEDescrBuilder<?, ?> ce) throws RecognitionException {
        BaseDescr result = null;
        if (helper.validateIdentifierKey(DroolsSoftKeywords.IF)) {
            result = conditionalBranch(ce, null);
        } else if (helper.validateIdentifierKey(DroolsSoftKeywords.DO)) {
            result = namedConsequence(ce, null);
        }
        return result;
    }

    /**
     * conditionalBranch := IF LEFT_PAREN conditionalExpression RIGHT_PAREN
     *                      ( namedConsequence | breakingNamedConsequence )
     *                      ( ELSE ( namedConsequence | breakingNamedConsequence | conditionalBranch ) )?
     */
    private BaseDescr conditionalBranch(CEDescrBuilder<?, ?> ce, ConditionalBranchDescrBuilder<?> conditionalBranch) throws RecognitionException {
        if (conditionalBranch == null) {
            conditionalBranch = helper.start((DescrBuilder<?, ?>) ce,
                    ConditionalBranchDescrBuilder.class,
                    null);
        }

        try {
            match(input,
                    DRL6Lexer.ID,
                    DroolsSoftKeywords.IF,
                    null,
                    DroolsEditorType.KEYWORD);
            if (state.failed)
                return null;

            EvalDescrBuilder<?> eval = conditionalBranch.condition();
            if (!parseEvalExpression(eval))
                return null;

            if (helper.validateIdentifierKey(DroolsSoftKeywords.DO)) {
                if (namedConsequence(null, conditionalBranch.consequence()) == null)
                    return null;
            } else if (helper.validateIdentifierKey(DroolsSoftKeywords.BREAK)) {
                if (breakingNamedConsequence(null, conditionalBranch.consequence()) == null)
                    return null;
            } else {
                return null;
            }

            if (helper.validateIdentifierKey(DroolsSoftKeywords.ELSE)) {
                match(input,
                        DRL6Lexer.ID,
                        DroolsSoftKeywords.ELSE,
                        null,
                        DroolsEditorType.KEYWORD);
                if (state.failed)
                    return null;

                ConditionalBranchDescrBuilder<?> elseBranch = conditionalBranch.otherwise();
                if (helper.validateIdentifierKey(DroolsSoftKeywords.DO)) {
                    if (namedConsequence(null, elseBranch.consequence()) == null)
                        return null;
                } else if (helper.validateIdentifierKey(DroolsSoftKeywords.BREAK)) {
                    if (breakingNamedConsequence(null, elseBranch.consequence()) == null)
                        return null;
                } else if (helper.validateIdentifierKey(DroolsSoftKeywords.IF)) {
                    if (conditionalBranch(null, elseBranch) == null)
                        return null;
                } else {
                    return null;
                }
            }
        } finally {
            helper.end(ConditionalBranchDescrBuilder.class,
                    conditionalBranch);
        }
        return conditionalBranch.getDescr();
    }

    /**
     * namedConsequence := DO LEFT_SQUARE ID RIGHT_SQUARE BREAK?
     */
    private BaseDescr namedConsequence(CEDescrBuilder<?, ?> ce, NamedConsequenceDescrBuilder<?> namedConsequence) throws RecognitionException {
        if (namedConsequence == null) {
            namedConsequence = helper.start((DescrBuilder<?, ?>) ce,
                    NamedConsequenceDescrBuilder.class,
                    null);
        }

        try {
            match(input,
                    DRL6Lexer.ID,
                    DroolsSoftKeywords.DO,
                    null,
                    DroolsEditorType.KEYWORD);
            if (state.failed)
                return null;

            match(input,
                    DRL6Lexer.LEFT_SQUARE,
                    null,
                    null,
                    DroolsEditorType.SYMBOL);
            if (state.failed)
                return null;

            Token label = match(input,
                    DRL6Lexer.ID,
                    null,
                    null,
                    DroolsEditorType.SYMBOL);
            if (state.failed)
                return null;

            namedConsequence.name(label.getText());

            match(input,
                    DRL6Lexer.RIGHT_SQUARE,
                    null,
                    null,
                    DroolsEditorType.SYMBOL);
            if (state.failed)
                return null;
        } finally {
            helper.end(NamedConsequenceDescrBuilder.class,
                    namedConsequence);
        }
        return namedConsequence.getDescr();
    }

    /**
     * breakingNamedConsequence := BREAK LEFT_SQUARE ID RIGHT_SQUARE
     */
    private BaseDescr breakingNamedConsequence(CEDescrBuilder<?, ?> ce, NamedConsequenceDescrBuilder<?> namedConsequence) throws RecognitionException {
        if (namedConsequence == null) {
            namedConsequence = helper.start((DescrBuilder<?, ?>) ce,
                    NamedConsequenceDescrBuilder.class,
                    null);
        }

        try {
            match(input,
                    DRL6Lexer.ID,
                    DroolsSoftKeywords.BREAK,
                    null,
                    DroolsEditorType.KEYWORD);
            if (state.failed)
                return null;

            match(input,
                    DRL6Lexer.LEFT_SQUARE,
                    null,
                    null,
                    DroolsEditorType.SYMBOL);
            if (state.failed)
                return null;

            Token label = match(input,
                    DRL6Lexer.ID,
                    null,
                    null,
                    DroolsEditorType.SYMBOL);
            if (state.failed)
                return null;

            namedConsequence.name(label.getText());
            namedConsequence.breaking(true);

            match(input,
                    DRL6Lexer.RIGHT_SQUARE,
                    null,
                    null,
                    DroolsEditorType.SYMBOL);
            if (state.failed)
                return null;

        } finally {
            helper.end(NamedConsequenceDescrBuilder.class,
                    namedConsequence);
        }
        return namedConsequence.getDescr();
    }

    /**
  
    
    /**
     * lhsExists := EXISTS
     *           ( (LEFT_PAREN (or_key|and_key))=> lhsOr  // prevents '((' for prefixed and/or
     *           | LEFT_PAREN lhsOr RIGHT_PAREN
     *           | lhsPatternBind
     *           )
     *
     * @param ce
     * @return
     * @throws org.antlr.runtime.RecognitionException
     */
    protected BaseDescr lhsExists(CEDescrBuilder<?, ?> ce,
            boolean allowOr) throws RecognitionException {
        CEDescrBuilder<?, ExistsDescr> exists = null;

        if (state.backtracking == 0) {
            exists = ce.exists();
            helper.start(exists,
                    CEDescrBuilder.class,
                    null);
        }
        try {
            match(input,
                    DRL6Lexer.ID,
                    DroolsSoftKeywords.EXISTS,
                    null,
                    DroolsEditorType.KEYWORD);
            if (state.failed)
                return null;

            if (state.backtracking == 0) {
                helper.emit(Location.LOCATION_LHS_BEGIN_OF_CONDITION_EXISTS);
            }
            if (input.LA(1) == DRL6Lexer.LEFT_PAREN) {
                boolean prefixed = helper.validateLT(2,
                        DroolsSoftKeywords.AND) || helper.validateLT(2,
                        DroolsSoftKeywords.OR);

                if (!prefixed) {
                    match(input,
                            DRL6Lexer.LEFT_PAREN,
                            null,
                            null,
                            DroolsEditorType.SYMBOL);
                    if (state.failed)
                        return null;
                }

                lhsOr(exists,
                        allowOr);
                if (state.failed)
                    return null;

                if (!prefixed) {
                    match(input,
                            DRL6Lexer.RIGHT_PAREN,
                            null,
                            null,
                            DroolsEditorType.SYMBOL);
                    if (state.failed)
                        return null;
                }
            } else {

                lhsPatternBind(exists,
                        true);
                if (state.failed)
                    return null;
            }

        } finally {
            if (state.backtracking == 0) {
                helper.end(CEDescrBuilder.class,
                        exists);
            }
        }
        return exists != null ? exists.getDescr() : null;
    }
    
    /**
     * lhsNot := NOT
     *           ( (LEFT_PAREN (or_key|and_key))=> lhsOr  // prevents '((' for prefixed and/or
     *           | LEFT_PAREN lhsOr RIGHT_PAREN
     *           | lhsPatternBind
     *           )
     *
     * @param ce
     * @return
     * @throws org.antlr.runtime.RecognitionException
     */
    protected BaseDescr lhsNot(CEDescrBuilder<?, ?> ce,
            boolean allowOr) throws RecognitionException {
        CEDescrBuilder<?, NotDescr> not = null;

        if (state.backtracking == 0) {
            not = ce.not();
            helper.start(not,
                    CEDescrBuilder.class,
                    null);
        }

        try {
            match(input,
                    DRL6Lexer.ID,
                    DroolsSoftKeywords.NOT,
                    null,
                    DroolsEditorType.KEYWORD);
            if (state.failed)
                return null;

            if (state.backtracking == 0) {
                helper.emit(Location.LOCATION_LHS_BEGIN_OF_CONDITION_NOT);
            }
            if (input.LA(1) == DRL6Lexer.LEFT_PAREN) {
                boolean prefixed = helper.validateLT(2,
                        DroolsSoftKeywords.AND) || helper.validateLT(2,
                        DroolsSoftKeywords.OR);

                if (!prefixed) {
                    match(input,
                            DRL6Lexer.LEFT_PAREN,
                            null,
                            null,
                            DroolsEditorType.SYMBOL);
                    if (state.failed)
                        return null;
                }
                if (state.backtracking == 0 && input.LA(1) != DRL6Lexer.EOF) {
                    helper.emit(Location.LOCATION_LHS_BEGIN_OF_CONDITION);
                }

                lhsOr(not,
                        allowOr);
                if (state.failed)
                    return null;

                if (!prefixed) {
                    match(input,
                            DRL6Lexer.RIGHT_PAREN,
                            null,
                            null,
                            DroolsEditorType.SYMBOL);
                    if (state.failed)
                        return null;
                }
            } else if (input.LA(1) != DRL6Lexer.EOF) {

                lhsPatternBind(not,
                        true);
                if (state.failed)
                    return null;
            }

        } finally {
            if (state.backtracking == 0) {
                helper.end(CEDescrBuilder.class,
                        not);
            }
        }
        return not != null ? not.getDescr() : null;
    }
    
    /**
     * lhsForall := FORALL LEFT_PAREN lhsPatternBind+ RIGHT_PAREN
     *
     * @param ce
     * @return
     * @throws org.antlr.runtime.RecognitionException
     */
    protected BaseDescr lhsForall(CEDescrBuilder<?, ?> ce) throws RecognitionException {
        ForallDescrBuilder<?> forall = helper.start(ce,
                ForallDescrBuilder.class,
                null);

    	try {
            match(input,
                    DRL6Lexer.ID,
                    DroolsSoftKeywords.FORALL,
                    null,
                    DroolsEditorType.KEYWORD);
            if (state.failed)
                return null;

            match(input,
                    DRL6Lexer.LEFT_PAREN,
                    null,
                    null,
                    DroolsEditorType.SYMBOL);
            if (state.failed)
                return null;

            do {
                lhsPatternBind(forall,
                        false);
                if (state.failed)
                    return null;

                if (input.LA(1) == DRL6Lexer.COMMA) {
                    match(input,
                            DRL6Lexer.COMMA,
                            null,
                            null,
                            DroolsEditorType.SYMBOL);
                    if (state.failed)
                        return null;
                }
            } while (input.LA(1) != DRL6Lexer.EOF && input.LA(1) != DRL6Lexer.RIGHT_PAREN);

            match(input,
                    DRL6Lexer.RIGHT_PAREN,
                    null,
                    null,
                    DroolsEditorType.SYMBOL);
            if (state.failed)
                return null;
        } finally {
            helper.end(ForallDescrBuilder.class,
                    forall);
        }

        return forall != null ? forall.getDescr() : null;
    }
    
    /**
     * lhsEval := EVAL LEFT_PAREN conditionalExpression RIGHT_PAREN
     *
     * @param ce
     * @return
     * @throws org.antlr.runtime.RecognitionException
     */
    private BaseDescr lhsEval(CEDescrBuilder<?, ?> ce) throws RecognitionException {
        EvalDescrBuilder<?> eval = null;

        try {
            eval = helper.start(ce,
                    EvalDescrBuilder.class,
                    null);

            match(input,
                    DRL6Lexer.ID,
                    DroolsSoftKeywords.EVAL,
                    null,
                    DroolsEditorType.KEYWORD);
            if (state.failed)
                return null;

            if (!parseEvalExpression(eval))
                return null;

        } catch (RecognitionException e) {
            throw e;
        } finally {
            helper.end(EvalDescrBuilder.class,
                    eval);
        }

        return eval != null ? eval.getDescr() : null;
    }

    private boolean parseEvalExpression(EvalDescrBuilder<?> eval) throws RecognitionException {
        match(input,
                DRL6Lexer.LEFT_PAREN,
                null,
                null,
                DroolsEditorType.SYMBOL);
        if (state.failed)
            return false;

        if (state.backtracking == 0) {
            helper.emit(Location.LOCATION_LHS_INSIDE_EVAL);
        }

        int idx = input.index();
        final String expr;
        try {
            expr = conditionalExpression();
        } catch (RecognitionException e) {
            final Token tempToken = helper.getLastTokenOnList(helper.getEditorInterface().getLast().getContent());
            if (tempToken != null) {
                for (int i = tempToken.getTokenIndex() + 1; i < input.size(); i++) {
                    final Token token = input.get(i);
                    if (token.getType() == DRL6Lexer.EOF) {
                        break;
                    }
                    helper.emit(token, DroolsEditorType.CODE_CHUNK);
                }
            }

            throw e;
        }

        if (state.backtracking == 0) {
            eval.constraint(expr);
        }

        match(input,
                DRL6Lexer.RIGHT_PAREN,
                null,
                null,
                DroolsEditorType.SYMBOL);
        if (state.failed)
            return false;

        helper.emit(Location.LOCATION_LHS_BEGIN_OF_CONDITION);
        return true;
    }

    /**
     * lhsParen := LEFT_PAREN lhsOr RIGHT_PAREN
     *
     * @param ce
     * @return
     * @throws org.antlr.runtime.RecognitionException
     */
    private BaseDescr lhsParen(CEDescrBuilder<?, ?> ce,
            boolean allowOr) throws RecognitionException {
        match(input,
                DRL6Lexer.LEFT_PAREN,
                null,
                null,
                DroolsEditorType.SYMBOL);
        if (state.failed)
            return null;

        if (state.backtracking == 0 && input.LA(1) != DRL6Lexer.EOF) {
            helper.emit(Location.LOCATION_LHS_BEGIN_OF_CONDITION);
        }
        BaseDescr descr = lhsOr(ce,
                allowOr);
        if (state.failed)
            return null;

        match(input,
                DRL6Lexer.RIGHT_PAREN,
                null,
                null,
                DroolsEditorType.SYMBOL);
        if (state.failed)
            return null;

        return descr;
    }

    /**
     * lhsPatternBind := label?
     *                ( LEFT_PAREN lhsPattern (OR lhsPattern)* RIGHT_PAREN
     *                | lhsPattern )
     *
     * @param ce
     * @return
     * @throws org.antlr.runtime.RecognitionException
     */
    @SuppressWarnings("unchecked")
    private BaseDescr lhsPatternBind(PatternContainerDescrBuilder<?, ?> ce,
            final boolean allowOr) throws RecognitionException {
        PatternDescrBuilder<?> pattern = null;
        CEDescrBuilder<?, OrDescr> or = null;
        BaseDescr result = null;

        Token first = input.LT(1);
        pattern = helper.start((DescrBuilder<?, ?>) ce,
                PatternDescrBuilder.class,
                null);
        if (pattern != null) {
            result = pattern.getDescr();
        }

        String label = null;
        boolean isUnification = false;
        if (input.LA(1) == DRL6Lexer.ID && input.LA(2) == DRL6Lexer.COLON && !helper.validateCEKeyword(1)) {
            label = label(DroolsEditorType.IDENTIFIER_PATTERN);
            if (state.failed)
                return null;
        } else if (input.LA(1) == DRL6Lexer.ID && input.LA(2) == DRL6Lexer.UNIFY && !helper.validateCEKeyword(1)) {
            label = unif(DroolsEditorType.IDENTIFIER_PATTERN);
            if (state.failed)
                return null;
            isUnification = true;
        }

        if (input.LA(1) == DRL6Lexer.LEFT_PAREN) {
            try {
                match(input,
                        DRL6Lexer.LEFT_PAREN,
                        null,
                        null,
                        DroolsEditorType.SYMBOL);
                if (state.failed)
                    return null;

                if (helper.validateCEKeyword(1)) {
                    failMismatchedTokenException();
                    return null; // in case it is backtracking
                }

                lhsPattern(pattern,
                        label,
                        isUnification);
                if (state.failed)
                    return null;

                if (allowOr && helper.validateIdentifierKey(DroolsSoftKeywords.OR) && ce instanceof CEDescrBuilder) {
                    if (state.backtracking == 0) {
                        // this is necessary because of the crappy bind with multi-pattern OR syntax
                        or = ((CEDescrBuilder<DescrBuilder<?, ?>, OrDescr>) ce).or();
                        result = or.getDescr();

                        helper.end(PatternDescrBuilder.class,
                                pattern);
                        helper.start(or,
                                CEDescrBuilder.class,
                                null);
                        // adjust real or starting token:
                        helper.setStart(or,
                                first);

                        // remove original pattern from the parent CE child list:
                        ((ConditionalElementDescr) ce.getDescr()).getDescrs().remove(pattern.getDescr());
                        // add pattern to the OR instead
                        or.getDescr().addDescr(pattern.getDescr());
                    }

                    while (helper.validateIdentifierKey(DroolsSoftKeywords.OR)) {
                        match(input,
                                DRL6Lexer.ID,
                                DroolsSoftKeywords.OR,
                                null,
                                DroolsEditorType.KEYWORD);
                        if (state.failed)
                            return null;

                        pattern = helper.start(or,
                                PatternDescrBuilder.class,
                                null);
                        // new pattern, same binding
                        lhsPattern(pattern,
                                label,
                                isUnification);
                        if (state.failed)
                            return null;

                        helper.end(PatternDescrBuilder.class,
                                pattern);
                    }
                }

                match(input,
                        DRL6Lexer.RIGHT_PAREN,
                        null,
                        null,
                        DroolsEditorType.SYMBOL);
                if (state.failed)
                    return null;

            } finally {
                if (or != null) {
                    helper.end(CEDescrBuilder.class,
                            or);
                } else {
                    helper.end(PatternDescrBuilder.class,
                            pattern);
                }
            }

        } else {
            try {
                lhsPattern(pattern,
                        label,
                        isUnification);
                if (state.failed)
                    return null;

            } finally {
                helper.end(PatternDescrBuilder.class,
                        pattern);
            }
        }

        return result;
    }

    /**
     * lhsAccumulate := (ACCUMULATE|ACC) LEFT_PAREN lhsAnd (COMMA|SEMICOLON)
     *                      accumulateFunctionBinding (COMMA accumulateFunctionBinding)*
     *                      (SEMICOLON constraints)?
     *                  RIGHT_PAREN SEMICOLON?
     *
     * @param ce
     * @return
     * @throws org.antlr.runtime.RecognitionException
     */
    private BaseDescr lhsAccumulate(PatternContainerDescrBuilder<?, ?> ce) throws RecognitionException {
        PatternDescrBuilder<?> pattern = null;
        BaseDescr result = null;

        pattern = helper.start((DescrBuilder<?, ?>) ce,
                PatternDescrBuilder.class,
                null);
        if (pattern != null) {
            result = pattern.getDescr();
        }

        try {
            if (state.backtracking == 0) {
                pattern.type("Object");
                pattern.isQuery(false);
                // might have to add the implicit bindings as well
            }

            AccumulateDescrBuilder<?> accumulate = helper.start(pattern,
                    AccumulateDescrBuilder.class,
                    null);
            try {
                if (helper.validateIdentifierKey(DroolsSoftKeywords.ACCUMULATE)) {
                    match(input,
                            DRL6Lexer.ID,
                            DroolsSoftKeywords.ACCUMULATE,
                            null,
                            DroolsEditorType.KEYWORD);
                } else {
                    // might be using the short mnemonic
                    match(input,
                            DRL6Lexer.ID,
                            DroolsSoftKeywords.ACC,
                            null,
                            DroolsEditorType.KEYWORD);
                }
                if (state.failed)
                    return null;

                if (state.backtracking == 0 && input.LA(1) != DRL6Lexer.EOF) {
                    helper.emit(Location.LOCATION_LHS_FROM_ACCUMULATE);
                }
                match(input,
                        DRL6Lexer.LEFT_PAREN,
                        null,
                        null,
                        DroolsEditorType.SYMBOL);
                if (state.failed)
                    return null;

                CEDescrBuilder<?, AndDescr> source = accumulate.source();
                try {
                    helper.start(source,
                            CEDescrBuilder.class,
                            null);
                    lhsAnd(source,
                            true);
                    if (state.failed)
                        return null;

                    if (source.getDescr() != null && source.getDescr() instanceof ConditionalElementDescr) {
                        ConditionalElementDescr root = (ConditionalElementDescr) source.getDescr();
                        BaseDescr[] descrs = root.getDescrs().toArray(new BaseDescr[root.getDescrs().size()]);
                        root.getDescrs().clear();
                        for (int i = 0; i < descrs.length; i++) {
                            root.addOrMerge(descrs[i]);
                        }
                    }
                } finally {
                    helper.end(CEDescrBuilder.class,
                            source);
                }

                if (input.LA(1) == DRL6Lexer.COMMA) {
                    match(input,
                            DRL6Lexer.COMMA,
                            null,
                            null,
                            DroolsEditorType.SYMBOL);
                    if (state.failed)
                        return null;
                } else if (input.LA(-1) != DRL6Lexer.SEMICOLON) {
                    // lhsUnary will consume an optional SEMICOLON, so we need to check if it was consumed already
                    // or if we must fail consuming it now
                    match(input,
                            DRL6Lexer.SEMICOLON,
                            null,
                            null,
                            DroolsEditorType.SYMBOL);
                    if (state.failed)
                        return null;
                }

                // accumulate functions
                accumulateFunctionBinding(accumulate);
                if (state.failed)
                    return null;

                while (input.LA(1) == DRL6Lexer.COMMA) {
                    match(input,
                            DRL6Lexer.COMMA,
                            null,
                            null,
                            DroolsEditorType.SYMBOL);
                    if (state.failed)
                        return null;

                    accumulateFunctionBinding(accumulate);
                    if (state.failed)
                        return null;
                }

                if (input.LA(1) == DRL6Lexer.SEMICOLON) {
                    match(input,
                            DRL6Lexer.SEMICOLON,
                            null,
                            null,
                            DroolsEditorType.SYMBOL);
                    if (state.failed)
                        return null;

                    constraints(pattern);
                }
                match(input,
                        DRL6Lexer.RIGHT_PAREN,
                        null,
                        null,
                        DroolsEditorType.SYMBOL);
                if (state.failed)
                    return null;
            } finally {
                helper.end(AccumulateDescrBuilder.class,
                        accumulate);
                if (state.backtracking == 0 && input.LA(1) != DRL6Lexer.EOF) {
                    helper.emit(Location.LOCATION_LHS_BEGIN_OF_CONDITION);
                }
            }
        } finally {
            helper.end(PatternDescrBuilder.class,
                    pattern);
        }

        if (input.LA(1) == DRL6Lexer.SEMICOLON) {
            match(input,
                    DRL6Lexer.SEMICOLON,
                    null,
                    null,
                    DroolsEditorType.SYMBOL);
            if (state.failed)
                return null;
        }
        return result;
    }

    private void failMismatchedTokenException() throws DroolsMismatchedTokenException {
        if (state.backtracking > 0) {
            state.failed = true;
        } else {
            DroolsMismatchedTokenException mte = new DroolsMismatchedTokenException(input.LA(1),
                    input.LT(1).getText(),
                    input);
            input.consume();
            throw mte;
        }
    }

    void failMissingTokenException() throws MissingTokenException {
        if (state.backtracking > 0) {
            state.failed = true;
        } else {
            throw new MissingTokenException(DRL6Lexer.STRING,
                    input,
                    null);
        }
    }

    /**
     * lhsPattern := xpathPrimary |
     *               ( QUESTION? qualifiedIdentifier
     *                 LEFT_PAREN positionalConstraints? constraints? RIGHT_PAREN
     *                 (OVER patternFilter)? (FROM patternSource)? )
     *
     * @param pattern
     * @param label
     * @param isUnification
     * @throws org.antlr.runtime.RecognitionException
     */
    void lhsPattern( PatternDescrBuilder<?> pattern,
                     String label,
                     boolean isUnification ) throws RecognitionException {

        if (input.LA(1) == DRL6Lexer.DIV) {
            int first = input.index();
            exprParser0.xpathPrimary();
            if (state.failed) return;
            int last = input.LT(-1).getTokenIndex();
            String expr = toExpression("", first, last);
            pattern.constraint(expr);
            if ( label != null ) {
                pattern.id(label, isUnification);
            }
            return;
        }

        boolean query = false;
        if (input.LA(1) == DRL6Lexer.QUESTION) {
            match(input,
                    DRL6Lexer.QUESTION,
                    null,
                    null,
                    DroolsEditorType.SYMBOL);
            if (state.failed)
                return;
            query = true;
        }

        String type = this.qualifiedIdentifier();
        if (state.failed)
            return;

        if (state.backtracking == 0) {
            pattern.type(type);
            pattern.isQuery(query);
            if (label != null) {
                pattern.id(label, isUnification);
            }
        }

        match(input,
                DRL6Lexer.LEFT_PAREN,
                null,
                null,
                DroolsEditorType.SYMBOL);
        if (state.failed)
            return;

        if (input.LA(1) != DRL6Lexer.RIGHT_PAREN && speculatePositionalConstraints()) {
            positionalConstraints(pattern);
        }

        if (input.LA(1) != DRL6Lexer.RIGHT_PAREN) {
            constraints(pattern);
        }

        match(input,
                DRL6Lexer.RIGHT_PAREN,
                null,
                null,
                DroolsEditorType.SYMBOL);
        if (state.failed)
            return;

        while (input.LA(1) == DRL6Lexer.AT) {
            // annotation*
            annotation(pattern);
            if (state.failed)
                return;
        }

        if (helper.validateIdentifierKey(DroolsSoftKeywords.OVER)) {
            //           || input.LA( 1 ) == DRL6Lexer.PIPE ) {
            patternFilter(pattern);
        }

        if (helper.validateIdentifierKey(DroolsSoftKeywords.FROM)) {
            patternSource(pattern);
        }

        if (state.backtracking == 0) {
            helper.emit(Location.LOCATION_LHS_BEGIN_OF_CONDITION);
        }
    }

    /**
     * accumulateFunctionBinding := label accumulateFunction
     * @param accumulate
     * @throws org.antlr.runtime.RecognitionException
     */
    private void accumulateFunctionBinding( AccumulateDescrBuilder<?> accumulate ) throws RecognitionException {
        String label = null;
        boolean unif = false;
        if (input.LA(2) == DRL6Lexer.COLON) {
            label = label(DroolsEditorType.IDENTIFIER_VARIABLE);
        } else if (input.LA(2) == DRL6Lexer.UNIFY) {
            label = unif(DroolsEditorType.IDENTIFIER_VARIABLE);
            unif = true;
        }
        accumulateFunction( accumulate,
                            unif,
                            label );
    }

    /**
     * accumulateFunction := label? ID parameters
     * @param accumulate
     * @throws org.antlr.runtime.RecognitionException
     */
    private void accumulateFunction(AccumulateDescrBuilder<?> accumulate,
                                    boolean unif,
                                    String label) throws RecognitionException {
        Token function = match(input,
                DRL6Lexer.ID,
                null,
                null,
                DroolsEditorType.KEYWORD);
        if (state.failed)
            return;

        List<String> parameters = parameters();
        if (state.failed)
            return;

        if (state.backtracking == 0) {
            accumulate.function(function.getText(),
                    label,
                    unif,
                    parameters != null ? parameters.toArray(new String[]{}) : new String[]{});
        }
    }

    /**
     * parameters := LEFT_PAREN (conditionalExpression (COMMA conditionalExpression)* )? RIGHT_PAREN
     *
     * @return
     * @throws org.antlr.runtime.RecognitionException
     */
    private List<String> parameters() throws RecognitionException {
        match(input,
                DRL6Lexer.LEFT_PAREN,
                null,
                null,
                DroolsEditorType.SYMBOL);
        if (state.failed)
            return null;

        List<String> parameters = new ArrayList<String>();
        if (input.LA(1) != DRL6Lexer.EOF && input.LA(1) != DRL6Lexer.RIGHT_PAREN) {
            String param = conditionalExpression();
            if (state.failed)
                return null;
            parameters.add(param);

            while (input.LA(1) == DRL6Lexer.COMMA) {
                match(input,
                        DRL6Lexer.COMMA,
                        null,
                        null,
                        DroolsEditorType.SYMBOL);
                if (state.failed)
                    return null;

                param = conditionalExpression();
                if (state.failed)
                    return null;
                parameters.add(param);
            }
        }

        match(input,
                DRL6Lexer.RIGHT_PAREN,
                null,
                null,
                DroolsEditorType.SYMBOL);
        if (state.failed)
            return null;
        return parameters;
    }

    /**
     * label := ID COLON
     * @return
     * @throws org.antlr.runtime.RecognitionException
     */
    String label( DroolsEditorType edType ) throws RecognitionException {
        Token label = match(input,
                DRL6Lexer.ID,
                null,
                null,
                edType);
        if (state.failed)
            return null;

        match(input,
                DRL6Lexer.COLON,
                null,
                null,
                DroolsEditorType.SYMBOL);
        if (state.failed)
            return null;

        return label.getText();
    }

    /**
     * unif := ID UNIFY
     * @return
     * @throws org.antlr.runtime.RecognitionException
     */
    private String unif(DroolsEditorType edType) throws RecognitionException {
        Token label = match(input,
                DRL6Lexer.ID,
                null,
                null,
                edType);
        if (state.failed)
            return null;

        match(input,
                DRL6Lexer.UNIFY,
                null,
                null,
                DroolsEditorType.SYMBOL);
        if (state.failed)
            return null;

        return label.getText();
    }

    
    private boolean speculatePositionalConstraints() {
        state.backtracking++;
        int start = input.mark();
        try {
            positionalConstraints(null); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: " + re);
            re.printStackTrace();
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed = false;
        return success;
    }

    /**
     * positionalConstraints := constraint (COMMA constraint)* SEMICOLON
     * @param pattern
     * @throws org.antlr.runtime.RecognitionException
     */
    private void positionalConstraints(PatternDescrBuilder<?> pattern) throws RecognitionException {
        constraint(pattern,
                true,
                "");
        if (state.failed)
            return;

        while (input.LA(1) == DRL6Lexer.COMMA) {
            match(input,
                    DRL6Lexer.COMMA,
                    null,
                    null,
                    DroolsEditorType.SYMBOL);
            if (state.failed)
                return;

            constraint(pattern,
                    true,
                    "");
            if (state.failed)
                return;
        }

        match(input,
                DRL6Lexer.SEMICOLON,
                null,
                null,
                DroolsEditorType.SYMBOL);
        if (state.failed)
            return;
    }

    /**
     * constraints := constraint (COMMA constraint)*
     * @param pattern
     * @throws org.antlr.runtime.RecognitionException
     */
    private void constraints(PatternDescrBuilder<?> pattern) throws RecognitionException {
        constraints(pattern, "");
    }

    private void constraints(PatternDescrBuilder<?> pattern, String prefix) throws RecognitionException {
        constraint(pattern,
                false,
                prefix);
        if (state.failed)
            return;

        while (input.LA(1) == DRL6Lexer.COMMA) {
            match(input,
                    DRL6Lexer.COMMA,
                    null,
                    null,
                    DroolsEditorType.SYMBOL);

            if (state.failed)
                return;

            constraint(pattern,
                    false,
                    prefix);
            if (state.failed)
                return;
        }
    }

    /**
     * constraint := nestedConstraint | conditionalOrExpression
     * @param pattern
     * @throws org.antlr.runtime.RecognitionException
     */
    private void constraint(PatternDescrBuilder<?> pattern,
            boolean positional,
            String prefix) throws RecognitionException {
        if (speculateNestedConstraint()) {
            nestedConstraint(pattern, prefix);
            return;
        }

        if (state.backtracking == 0) {
            helper.emit(Location.LOCATION_LHS_INSIDE_CONDITION_START);
        }

        int first = input.index();
        exprParser0.getHelper().setHasOperator(false); // resetting
        try {
            exprParser0.conditionalOrExpression();
        } finally {
            if (state.backtracking == 0) {
                if (input.LA(1) == DRL6Lexer.ID && input.LA(2) == DRL6Lexer.EOF) {
                    helper.emit(Location.LOCATION_LHS_INSIDE_CONDITION_ARGUMENT);
                } else if (input.LA(1) != DRL6Lexer.EOF) {
                    helper.emit(Location.LOCATION_LHS_INSIDE_CONDITION_END);
                } else if (!lastTokenWasWhiteSpace()) {
                    int location = getCurrentLocation();
                    if (location == Location.LOCATION_LHS_INSIDE_CONDITION_END) {
                        helper.emit(Location.LOCATION_LHS_INSIDE_CONDITION_ARGUMENT);
                    } else if (input.get(input.index()).getType() != DRL6Lexer.EOF) {
                        helper.emit(Location.LOCATION_LHS_INSIDE_CONDITION_START);
                    }
                } else if (getCurrentLocation() == Location.LOCATION_LHS_INSIDE_CONDITION_START &&
                        !exprParser0.getHelper().getHasOperator() &&
                        lastTokenWasWhiteSpace() &&
                        input.LA(1) == DRL6Lexer.EOF &&
                        input.LA(-1) == DRL6Lexer.ID) {
                    helper.emit(Location.LOCATION_LHS_INSIDE_CONDITION_OPERATOR);
                }
            }
        }

        if (state.failed)
            return;

        if (state.backtracking == 0 && input.index() > first) {
            // expression consumed something
            int last = input.LT(-1).getTokenIndex();
            String expr = toExpression(prefix, first, last);
            pattern.constraint(expr,
                    positional);
            BaseDescr constrDescr = pattern.getDescr().getDescrs().get(pattern.getDescr().getDescrs().size() - 1);
            constrDescr.setLocation(input.get(first).getLine(),
                    input.get(first).getCharPositionInLine());
            constrDescr.setEndLocation(input.get(last).getLine(),
                    input.get(last).getCharPositionInLine());
            constrDescr.setStartCharacter(((CommonToken) input.get(first)).getStartIndex());
            constrDescr.setEndCharacter(((CommonToken) input.get(last)).getStopIndex());
        }
    }

    private String toExpression(String prefix, int first, int last) {
        String expr = input.toString(first, last);
        if (prefix.length() == 0) {
            return expr;
        }
        StringBuilder sb = new StringBuilder();
        toOrExpression(sb, prefix, expr);
        return sb.toString();
    }

    private void toOrExpression(StringBuilder sb, String prefix, String expr) {
        int start = 0;
        int end = expr.indexOf("||");
        do {
            if (start > 0) {
                sb.append(" || ");
            }
            toAndExpression(sb, prefix, end > 0 ? expr.substring(start, end) : expr.substring(start));
            start = end + 2;
            end = expr.indexOf("||", start);
        } while (start > 1);
    }

    private void toAndExpression(StringBuilder sb, String prefix, String expr) {
        int start = 0;
        int end = expr.indexOf("&&");
        do {
            if (start > 0) {
                sb.append(" && ");
            }
            sb.append(toExpression(prefix, end > 0 ? expr.substring(start, end) : expr.substring(start)));
            start = end + 2;
            end = expr.indexOf("&&", start);
        } while (start > 1);
    }

    private String toExpression(String prefix, String expr) {
        expr = expr.trim();
        int colonPos = expr.indexOf(":");
        return colonPos < 0 ? prefix + expr : expr.substring(0, colonPos + 1) + " " + prefix + expr.substring(colonPos + 1).trim();
    }

    private boolean speculateNestedConstraint() throws RecognitionException {
        return getNestedConstraintPrefixLenght() > 0;
    }

    /**
     * nestedConstraint := ( ID ( DOT | HASH ) )* ID DOT LEFT_PAREN constraints RIGHT_PAREN
     * @param pattern
     * @throws org.antlr.runtime.RecognitionException
     */
    private void nestedConstraint(PatternDescrBuilder<?> pattern, String prefix) throws RecognitionException {
        int prefixLenght = getNestedConstraintPrefixLenght();

        int prefixStart = input.index();
        prefix += input.toString(prefixStart, prefixStart + prefixLenght - 2);
        for (int i = 0; i < prefixLenght; i++) {
            input.consume();
        }

        constraints(pattern, prefix);
        match(input,
                DRL6Lexer.RIGHT_PAREN,
                null,
                null,
                DroolsEditorType.SYMBOL);
    }

    private int getNestedConstraintPrefixLenght() {
        int cursor = 0;
        int lastToken = input.LA(++cursor);
        while (true) {
            int nextToken = input.LA(++cursor);
            switch (lastToken) {
                case DRL6Lexer.ID:
                    if (nextToken != DRL6Lexer.DOT && nextToken != DRL6Lexer.NULL_SAFE_DOT && nextToken != DRL6Lexer.HASH) {
                        return -1;
                    }
                    break;
                case DRL6Lexer.DOT:
                case DRL6Lexer.NULL_SAFE_DOT:
                    if (nextToken == DRL6Lexer.LEFT_PAREN) {
                        return cursor;
                    }
                case DRL6Lexer.HASH:
                    if (nextToken != DRL6Lexer.ID) {
                        return -1;
                    }
                    break;
                default:
                    return -1;
            }
            lastToken = nextToken;
        }
    }

    private boolean lastTokenWasWhiteSpace() {
        int index = input.index();
        while (index >= 0) {
            int type = input.get(index).getType();
            switch (type) {
                case DRL6Lexer.EOF:
                    index--;
                    break;
                case DRL6Lexer.WS:
                    return true;
                default:
                    return false;
            }
        }
        return false;
    }

    
    private int getCurrentLocation() {
        LinkedList<DroolsSentence> ei = helper.getEditorInterface();
        LinkedList<?> content = ei.getLast().getContent();
        // the following call is efficient as it points to the tail of the list
        ListIterator<?> listIterator = content.listIterator(content.size());
        while (listIterator.hasPrevious()) {
            Object previous = listIterator.previous();
            if (previous instanceof Integer) {
                return ((Integer) previous).intValue();
            }
        }
        return Location.LOCATION_UNKNOWN;
    }


    /**
     * patternFilter :=   OVER filterDef
     * DISALLOWED:        | ( PIPE filterDef )+
     *
     * @param pattern
     * @throws org.antlr.runtime.RecognitionException
     */
    private void patternFilter(PatternDescrBuilder<?> pattern) throws RecognitionException {
        //        if ( input.LA( 1 ) == DRL6Lexer.PIPE ) {
        //            while ( input.LA( 1 ) == DRL6Lexer.PIPE ) {
        //                match( input,
        //                       DRL6Lexer.PIPE,
        //                       null,
        //                       null,
        //                       DroolsEditorType.SYMBOL );
        //                if ( state.failed ) return;
        //
        //                filterDef( pattern );
        //                if ( state.failed ) return;
        //            }
        //        } else {
        match(input,
                DRL6Lexer.ID,
                DroolsSoftKeywords.OVER,
                null,
                DroolsEditorType.KEYWORD);
        if (state.failed)
            return;

        filterDef(pattern);
        if (state.failed)
            return;
        //        }
    }

    /**
     * filterDef := label ID LEFT_PAREN parameters RIGHT_PAREN
     * @param pattern
     * @throws org.antlr.runtime.RecognitionException
     */
    private void filterDef(PatternDescrBuilder<?> pattern) throws RecognitionException {
        BehaviorDescrBuilder<?> behavior = helper.start(pattern,
                BehaviorDescrBuilder.class,
                null);
        try {
            String bName = label(DroolsEditorType.IDENTIFIER_PATTERN);
            if (state.failed)
                return;

            Token subtype = match(input,
                    DRL6Lexer.ID,
                    null,
                    null,
                    DroolsEditorType.IDENTIFIER_PATTERN);
            if (state.failed)
                return;

            if (state.backtracking == 0) {
                behavior.type(bName,
                        subtype.getText());
            }

            List<String> parameters = parameters();
            if (state.failed)
                return;

            if (state.backtracking == 0) {
                behavior.parameters(parameters);
            }
        } finally {
            helper.end(BehaviorDescrBuilder.class,
                    behavior);
        }
    }

    
    /**
     * patternSource := FROM
     *                ( fromAccumulate
     *                | fromCollect
     *                | fromEntryPoint
     *                | fromWindow
     *                | fromExpression )
     * @param pattern
     * @throws org.antlr.runtime.RecognitionException
     */
    private void patternSource(PatternDescrBuilder<?> pattern) throws RecognitionException {
        match(input,
                DRL6Lexer.ID,
                DroolsSoftKeywords.FROM,
                null,
                DroolsEditorType.KEYWORD);
        if (state.failed)
            return;

        if (state.backtracking == 0) {
            helper.emit(Location.LOCATION_LHS_FROM);
        }

        if (helper.validateIdentifierKey(DroolsSoftKeywords.ACCUMULATE) || helper.validateIdentifierKey(DroolsSoftKeywords.ACC)) {
            fromAccumulate(pattern);
        } else if (helper.validateIdentifierKey(DroolsSoftKeywords.COLLECT)) {
            fromCollect(pattern);
        } else if (helper.validateIdentifierKey(DroolsSoftKeywords.ENTRY) &&
                helper.validateLT(2,
                        "-") &&
                helper.validateLT(3,
                        DroolsSoftKeywords.POINT)) {
            fromEntryPoint(pattern);
            if (state.failed)
                return;
        } else if (helper.validateIdentifierKey(DroolsSoftKeywords.WINDOW)) {
            fromWindow(pattern);
        } else {
            fromExpression(pattern);
            if (!lastTokenWasWhiteSpace() && input.LA(1) == DRL6Lexer.EOF) {
                helper.emit(Location.LOCATION_LHS_FROM);
                throw new RecognitionException();
            }
            if (state.failed)
                return;
        }
        if (input.LA(1) == DRL6Lexer.SEMICOLON) {
            match(input,
                    DRL6Lexer.SEMICOLON,
                    null,
                    null,
                    DroolsEditorType.SYMBOL);
            if (state.failed)
                return;
        }
    }

    /**
     * fromExpression := conditionalOrExpression
     *
     * @param pattern
     * @throws org.antlr.runtime.RecognitionException
     */
    private void fromExpression(PatternDescrBuilder<?> pattern) throws RecognitionException {
        String expr = conditionalOrExpression();
        if (state.failed)
            return;

        if (state.backtracking == 0) {
            pattern.from().expression(expr);
            if (input.LA(1) != DRL6Lexer.EOF) {
                helper.emit(Location.LOCATION_LHS_BEGIN_OF_CONDITION);
            }
        }
    }

    /**
     * fromEntryPoint := ENTRY-POINT stringId
     *
     * @param pattern
     * @throws org.antlr.runtime.RecognitionException
     */
    private void fromEntryPoint(PatternDescrBuilder<?> pattern) throws RecognitionException {
        String ep = "";

        match(input,
                DRL6Lexer.ID,
                DroolsSoftKeywords.ENTRY,
                null,
                DroolsEditorType.KEYWORD);
        if (state.failed)
            return;

        match(input,
                DRL6Lexer.MINUS,
                null,
                null,
                DroolsEditorType.KEYWORD);
        if (state.failed)
            return;

        match(input,
                DRL6Lexer.ID,
                DroolsSoftKeywords.POINT,
                null,
                DroolsEditorType.KEYWORD);
        if (state.failed)
            return;

        ep = stringId();

        if (state.backtracking == 0) {
            pattern.from().entryPoint(ep);
            if (input.LA(1) != DRL6Lexer.EOF) {
                helper.emit(Location.LOCATION_LHS_BEGIN_OF_CONDITION);
            }
        }
    }

    /**
     * fromWindow := WINDOW ID
     *
     * @param pattern
     * @throws org.antlr.runtime.RecognitionException
     */
    private void fromWindow(PatternDescrBuilder<?> pattern) throws RecognitionException {
        String window = "";

        match(input,
                DRL6Lexer.ID,
                DroolsSoftKeywords.WINDOW,
                null,
                DroolsEditorType.KEYWORD);
        if (state.failed)
            return;

        Token id = match(input,
                DRL6Lexer.ID,
                null,
                null,
                DroolsEditorType.IDENTIFIER);
        if (state.failed)
            return;
        window = id.getText();

        if (state.backtracking == 0) {
            pattern.from().window(window);
            if (input.LA(1) != DRL6Lexer.EOF) {
                helper.emit(Location.LOCATION_LHS_BEGIN_OF_CONDITION);
            }
        }
    }

    /**
     * fromCollect := COLLECT LEFT_PAREN lhsPatternBind RIGHT_PAREN
     *
     * @param pattern
     * @throws org.antlr.runtime.RecognitionException
     */
    private void fromCollect(PatternDescrBuilder<?> pattern) throws RecognitionException {
        CollectDescrBuilder<?> collect = helper.start(pattern,
                CollectDescrBuilder.class,
                null);
        try {
            match(input,
                    DRL6Lexer.ID,
                    DroolsSoftKeywords.COLLECT,
                    null,
                    DroolsEditorType.KEYWORD);
            if (state.failed)
                return;
            if (state.backtracking == 0 && input.LA(1) != DRL6Lexer.EOF) {
                helper.emit(Location.LOCATION_LHS_FROM_COLLECT);
            }

            match(input,
                    DRL6Lexer.LEFT_PAREN,
                    null,
                    null,
                    DroolsEditorType.SYMBOL);
            if (state.failed)
                return;

            lhsPatternBind(collect,
                    false);
            if (state.failed)
                return;

            match(input,
                    DRL6Lexer.RIGHT_PAREN,
                    null,
                    null,
                    DroolsEditorType.SYMBOL);
            if (state.failed)
                return;
        } finally {
            helper.end(CollectDescrBuilder.class,
                    collect);
            if (state.backtracking == 0 && input.LA(1) != DRL6Lexer.EOF) {
                helper.emit(Location.LOCATION_LHS_BEGIN_OF_CONDITION);
            }
        }
    }

    /**
     * fromAccumulate := ACCUMULATE LEFT_PAREN lhsAnd (COMMA|SEMICOLON)
     *                   ( INIT chunk_(_) COMMA ACTION chunk_(_) COMMA
     *                     ( REVERSE chunk_(_) COMMA)? RESULT chunk_(_)
     *                   | accumulateFunction
     *                   ) RIGHT_PAREN
     *
     * @param pattern
     * @throws org.antlr.runtime.RecognitionException
     */
    private void fromAccumulate(PatternDescrBuilder<?> pattern) throws RecognitionException {
        AccumulateDescrBuilder<?> accumulate = helper.start(pattern,
                AccumulateDescrBuilder.class,
                null);
        try {
            if (helper.validateIdentifierKey(DroolsSoftKeywords.ACCUMULATE)) {
                match(input,
                        DRL6Lexer.ID,
                        DroolsSoftKeywords.ACCUMULATE,
                        null,
                        DroolsEditorType.KEYWORD);
            } else {
                // might be using the short mnemonic
                match(input,
                        DRL6Lexer.ID,
                        DroolsSoftKeywords.ACC,
                        null,
                        DroolsEditorType.KEYWORD);
            }
            if (state.failed)
                return;

            if (state.backtracking == 0 && input.LA(1) != DRL6Lexer.EOF) {
                helper.emit(Location.LOCATION_LHS_FROM_ACCUMULATE);
            }
            match(input,
                    DRL6Lexer.LEFT_PAREN,
                    null,
                    null,
                    DroolsEditorType.SYMBOL);
            if (state.failed)
                return;

            CEDescrBuilder<?, AndDescr> source = accumulate.source();
            try {
                helper.start(source,
                        CEDescrBuilder.class,
                        null);
                lhsAnd(source,
                        false);
                if (state.failed)
                    return;

                if (source.getDescr() != null && source.getDescr() instanceof ConditionalElementDescr) {
                    ConditionalElementDescr root = (ConditionalElementDescr) source.getDescr();
                    BaseDescr[] descrs = root.getDescrs().toArray(new BaseDescr[root.getDescrs().size()]);
                    root.getDescrs().clear();
                    for (int i = 0; i < descrs.length; i++) {
                        root.addOrMerge(descrs[i]);
                    }
                }
            } finally {
                helper.end(CEDescrBuilder.class,
                        source);
            }

            if (input.LA(1) == DRL6Lexer.COMMA) {
                match(input,
                        DRL6Lexer.COMMA,
                        null,
                        null,
                        DroolsEditorType.SYMBOL);
                if (state.failed)
                    return;
            } else if (input.LA(-1) != DRL6Lexer.SEMICOLON) {
                match(input,
                      DRL6Lexer.SEMICOLON,
                      null,
                      null,
                      DroolsEditorType.SYMBOL);
                if (state.failed)
                    return;
            }

            if (helper.validateIdentifierKey(DroolsSoftKeywords.INIT)) {
                // custom code, inline accumulate

                // initBlock
                match(input,
                        DRL6Lexer.ID,
                        DroolsSoftKeywords.INIT,
                        null,
                        DroolsEditorType.KEYWORD);
                if (state.failed)
                    return;
                if (state.backtracking == 0 && input.LA(1) != DRL6Lexer.EOF) {
                    helper.emit(Location.LOCATION_LHS_FROM_ACCUMULATE_INIT);
                }

                String init = chunk(DRL6Lexer.LEFT_PAREN,
                        DRL6Lexer.RIGHT_PAREN,
                        Location.LOCATION_LHS_FROM_ACCUMULATE_INIT_INSIDE);
                if (state.failed)
                    return;
                if (state.backtracking == 0)
                    accumulate.init(init);

                if (input.LA(1) == DRL6Lexer.COMMA) {
                    match(input,
                            DRL6Lexer.COMMA,
                            null,
                            null,
                            DroolsEditorType.SYMBOL);
                    if (state.failed)
                        return;
                }

                // actionBlock
                match(input,
                        DRL6Lexer.ID,
                        DroolsSoftKeywords.ACTION,
                        null,
                        DroolsEditorType.KEYWORD);
                if (state.failed)
                    return;
                if (state.backtracking == 0 && input.LA(1) != DRL6Lexer.EOF) {
                    helper.emit(Location.LOCATION_LHS_FROM_ACCUMULATE_ACTION);
                }

                String action = chunk(DRL6Lexer.LEFT_PAREN,
                        DRL6Lexer.RIGHT_PAREN,
                        Location.LOCATION_LHS_FROM_ACCUMULATE_ACTION_INSIDE);
                if (state.failed)
                    return;
                if (state.backtracking == 0)
                    accumulate.action(action);

                if (input.LA(1) == DRL6Lexer.COMMA) {
                    match(input,
                            DRL6Lexer.COMMA,
                            null,
                            null,
                            DroolsEditorType.SYMBOL);
                    if (state.failed)
                        return;
                }

                // reverseBlock
                if (helper.validateIdentifierKey(DroolsSoftKeywords.REVERSE)) {
                    match(input,
                            DRL6Lexer.ID,
                            DroolsSoftKeywords.REVERSE,
                            null,
                            DroolsEditorType.KEYWORD);
                    if (state.failed)
                        return;
                    if (state.backtracking == 0 && input.LA(1) != DRL6Lexer.EOF) {
                        helper.emit(Location.LOCATION_LHS_FROM_ACCUMULATE_REVERSE);
                    }

                    String reverse = chunk(DRL6Lexer.LEFT_PAREN,
                            DRL6Lexer.RIGHT_PAREN,
                            Location.LOCATION_LHS_FROM_ACCUMULATE_REVERSE_INSIDE);
                    if (state.failed)
                        return;
                    if (state.backtracking == 0)
                        accumulate.reverse(reverse);

                    if (input.LA(1) == DRL6Lexer.COMMA) {
                        match(input,
                                DRL6Lexer.COMMA,
                                null,
                                null,
                                DroolsEditorType.SYMBOL);
                        if (state.failed)
                            return;
                    }
                }

                // resultBlock
                match(input,
                        DRL6Lexer.ID,
                        DroolsSoftKeywords.RESULT,
                        null,
                        DroolsEditorType.KEYWORD);
                if (state.failed)
                    return;

                if (state.backtracking == 0 && input.LA(1) != DRL6Lexer.EOF) {
                    helper.emit(Location.LOCATION_LHS_FROM_ACCUMULATE_RESULT);
                }

                String result = chunk(DRL6Lexer.LEFT_PAREN,
                        DRL6Lexer.RIGHT_PAREN,
                        Location.LOCATION_LHS_FROM_ACCUMULATE_RESULT_INSIDE);
                if (state.failed)
                    return;
                if (state.backtracking == 0)
                    accumulate.result(result);
            } else {
                // accumulate functions
                accumulateFunction(accumulate,
                                   false,
                                   null);
                if (state.failed)
                    return;
            }

            match(input,
                    DRL6Lexer.RIGHT_PAREN,
                    null,
                    null,
                    DroolsEditorType.SYMBOL);
            if (state.failed)
                return;
        } finally {
            helper.end(AccumulateDescrBuilder.class,
                    accumulate);
            if (state.backtracking == 0 && input.LA(1) != DRL6Lexer.EOF) {
                helper.emit(Location.LOCATION_LHS_BEGIN_OF_CONDITION);
            }
        }
    }
    
    //RHS
    
    /**
     * rhs := defaultConsequence namedConsequence* (~END)*
     * @param rule
     */
    void rhs( RuleDescrBuilder rule ) {
        defaultConsequence(rule);
        while (input.LA(1) != DRL6Lexer.EOF && helper.validateIdentifierKey(DroolsSoftKeywords.THEN)) {
            namedConsequence(rule);
        }
    }

    	

}
