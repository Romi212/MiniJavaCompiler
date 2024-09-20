package SyntaxAnalyzer;

import LexicalAnalyzer.LexicalAnalyzer;
import utils.LexicalErrorException;
import utils.SyntaxErrorException;
import utils.Token;

public class SyntaxAnalyzerImp implements SyntaxAnalyzer {

    private LexicalAnalyzer lexicalAnalyzer;
    private Token currentToken;

    public SyntaxAnalyzerImp(LexicalAnalyzer lexicalAnalyzer){
        this.lexicalAnalyzer = lexicalAnalyzer;
    }

    @Override
    public String analyzeSintax() throws LexicalErrorException, SyntaxErrorException{
        getNewToken();
        initial();
        return "[SinErrores]";
    }

    private void getNewToken() throws LexicalErrorException {
        currentToken = lexicalAnalyzer.nextToken();
    }
    private void initial() throws LexicalErrorException, SyntaxErrorException {
        if(Firsts.isFirst("ClassList", currentToken.getToken())){
            classList();
        }
    }

    private void classList() throws SyntaxErrorException, LexicalErrorException {
        if (Firsts.isFirst("Class", currentToken.getToken())) {
            classT();
            classList();
        }
        else if( Follows.itFollows("ClassList", currentToken.getToken())){
            //TODO: poner que EOF sea ''
        }
        else{
            throw new SyntaxErrorException("EOF", currentToken, "a class or end of file");
        }

    }

    private void classT() throws SyntaxErrorException, LexicalErrorException {
        abstractT();
        match("rw_class");
        className();
        parents();
        match("pm_brace_open");
        memberList();
        match("pm_brace_close");
    }

    private void abstractT() throws LexicalErrorException, SyntaxErrorException {
        if(currentToken.getToken().equals("rw_abstract")){
            match("rw_abstract");
        }
        else if(Follows.itFollows("Abstract", currentToken.getToken())){
            //TODO Check follows
        }
        else{
            throw new SyntaxErrorException("rw_class", currentToken, "reserved word 'class'");
        }
    }

    private void className() throws LexicalErrorException, SyntaxErrorException {
        match("id_class");
        generic();
    }

    private void generic() throws LexicalErrorException, SyntaxErrorException {
        if(currentToken.getToken().equals("op_less")){
            match("op_less");
            match("id_class");
            pTypesList();
            match("op_greater");
        }
        else if(Follows.itFollows("Generic", currentToken.getToken())){
            //TODO Check follows
        }else{
            throw new SyntaxErrorException("generic", currentToken, "reserved word extends or ( or {");
        }
    }

    private void pTypesList() throws LexicalErrorException, SyntaxErrorException {
        if(currentToken.getToken().equals("pm_comma")){
            match("pm_comma");
            match("id_class");
            pTypesList();
        }
        else if(Follows.itFollows("PTypesList", currentToken.getToken())){
            //TODO Check follows
        }else{
            throw new SyntaxErrorException("pm_greater", currentToken, "another parametric Type or >");
        }
    }
    private void parents() throws LexicalErrorException, SyntaxErrorException{
        if(currentToken.getToken().equals("rw_extends")){
            match("rw_extends");
            className();
        }
        else if(Follows.itFollows("Parents", currentToken.getToken())){
            //TODO: check follows
        }else{
            throw new SyntaxErrorException("rw_brace_open", currentToken, "{");
        }
    }
    private void memberList() throws  LexicalErrorException, SyntaxErrorException {
        if(Firsts.isFirst("Member", currentToken.getToken())){
            visibleMember();
            memberList();
        }/*
        else if(Follows.itFollows("MemberList", currentToken.getToken())){
            //TODO Check follows
        }else{
            throw new SyntaxErrorException("rw_brace_close", currentToken, "end of class declaration }");
        }*/
    }

    private void visibleMember() throws LexicalErrorException, SyntaxErrorException {
        visibility();
        member();
    }

    private void visibility() throws LexicalErrorException, SyntaxErrorException {
        if(currentToken.getToken().equals("rw_public")){
            match("rw_public");
        }
        else if(currentToken.getToken().equals("rw_private")){
            match("rw_private");
        }
        //else if(Follows.itFollows("Visibility", currentToken.getToken())){
            //TODO Check follows
       // } else{
       //     throw new SyntaxErrorException("rw_static", currentToken, "attribute or method declaration");
       // }
    }
    private void member() throws LexicalErrorException, SyntaxErrorException {
        if(currentToken.getToken().equals("rw_abstract")){
            abstractMethod();
        }
        else if(currentToken.getToken().equals("rw_static")){
            match("rw_static");
            memberType();
            declaration();
        }
        else if( currentToken.getToken().equals("id_class")){
            match("id_class");
            possibleCtor();
        }
        else{
            nonObjectType();
            declaration();
        }
    }

    private void abstractMethod() throws LexicalErrorException, SyntaxErrorException {
        match("rw_abstract");
        memberType();
        match("id_met_var");
        formalArgs();
        match("pm_semicolon");
    }

    private void declaration() throws LexicalErrorException, SyntaxErrorException {
        match("id_met_var");
        body();
    }

    private void possibleCtor() throws LexicalErrorException, SyntaxErrorException {
        if(currentToken.getToken().equals("pm_par_open")){
            constructor();
        }
        else {
            declaration();
        }
    }

    private void body() throws LexicalErrorException, SyntaxErrorException {
        if(currentToken.getToken().equals("pm_par_open")){
            formalArgs();
            block();

        }
        else{
            initialize();
            match("pm_semicolon");
        }

    }

    private void constructor() throws LexicalErrorException, SyntaxErrorException{
        formalArgs();
        block();
    }

    private void nonObjectType() throws LexicalErrorException, SyntaxErrorException {
        if(currentToken.getToken().equals("rw_void")){
            match("rw_void");
        }
        else{
            primitiveType();
        }
    }
    private void memberType() throws   LexicalErrorException, SyntaxErrorException {
        if(Firsts.isFirst("Type", currentToken.getToken())){
            type();
        }
        else {
            match("rw_void");

        }
    }

    private void type() throws LexicalErrorException, SyntaxErrorException {
        if(Firsts.isFirst("PrimitiveType", currentToken.getToken())){
            primitiveType();
        }
        else{
            className();
        }
    }

    private void primitiveType() throws LexicalErrorException, SyntaxErrorException {
        if(currentToken.getToken().equals("rw_int")){
            match("rw_int");
        }
        else if(currentToken.getToken().equals("rw_char")){
            match("rw_char");
        }
        else{
            match("rw_boolean");
        }

    }


    private void formalArgs() throws LexicalErrorException, SyntaxErrorException {
        match("pm_par_open");
        formalArgsList();
        match("pm_par_close");
    }

    private void formalArgsList() throws LexicalErrorException, SyntaxErrorException {
        if(Firsts.isFirst("FormalArg",currentToken.getToken())){
            formalArg();
            formalArgsList();
        }
        else{
            //TODO Check follows
        }
    }

    private void formalArg() throws LexicalErrorException, SyntaxErrorException {
        type();
        match("id_met_var");
    }

    private void block() throws LexicalErrorException, SyntaxErrorException {
        match("pm_brace_open");
        statementsList();
        match("pm_brace_close");
    }

    private void statementsList() throws LexicalErrorException, SyntaxErrorException {
        if(Firsts.isFirst("Statement", currentToken.getToken())){
            statement();
            statementsList();
        }
        else{
            //TODO Check follows
        }
    }

    private void statement() throws LexicalErrorException, SyntaxErrorException {
        if(currentToken.getToken().equals("pm_semicolon")){
            match("pm_semicolon");
        }
        else if(currentToken.getToken().equals("id_class")){
            match("id_class");
            object();
        }
        else if(Firsts.isFirst("NonStaticExp", currentToken.getToken())){
            nonStaticExp();
            match("pm_semicolon");
        }
        else if(Firsts.isFirst("PrimitiveVar", currentToken.getToken())){
            primitiveVar();
            match("pm_semicolon");
        }
        else if(Firsts.isFirst("Return", currentToken.getToken())){
            returnT();
            match("pm_semicolon");
        }
        else if(Firsts.isFirst("Break", currentToken.getToken())){
            breakT();
            match("pm_semicolon");
        }
        else if(Firsts.isFirst("If", currentToken.getToken())){
            ifT();
        }
        else if(Firsts.isFirst("While", currentToken.getToken())){
            whileT();
        }
        else if(Firsts.isFirst("Switch", currentToken.getToken())){
            switchT();
        }
        else if(Firsts.isFirst("For", currentToken.getToken())){
            forT();
        }

        else {
            block();
        }

    }

    private void object() throws LexicalErrorException, SyntaxErrorException {
        if (currentToken.getToken().equals("pm_period")) {
            staticCall();
            possibleExp();
        }
        else {
            generic();
            localVar();
        }
    }
    private void localVar() throws LexicalErrorException, SyntaxErrorException {
        match("id_met_var");
        chainedVar();
        initialize();
    }

    private void staticCall() throws LexicalErrorException, SyntaxErrorException {
        match("pm_period");
        match("id_met_var");
        actualArgs();
        chainedOp();
        possibleOp();
    }


    private void chainedVar() throws LexicalErrorException, SyntaxErrorException {
        if(currentToken.getToken().equals("pm_comma")){
            match("pm_comma");
            match("id_met_var");
            chainedVar();
        }
        else{
            //TODO Check follows
        }
    }

    private void primitiveVar() throws LexicalErrorException, SyntaxErrorException {
        localType();
        localVar();
    }

    private void localType() throws LexicalErrorException, SyntaxErrorException {
        if(Firsts.isFirst("PrimitiveType", currentToken.getToken())){
            primitiveType();
        }
        else{
            match("rw_var");
        }
    }

    private void nonStaticExp() throws LexicalErrorException, SyntaxErrorException {
        nSComplexExpression();
        possibleExp();
    }

    private void nSComplexExpression() throws LexicalErrorException, SyntaxErrorException {
        basicExpression();
        possibleOp();
    }
    private void returnT() throws LexicalErrorException, SyntaxErrorException {
        match("rw_return");
        expressionOp();
    }

    private void breakT() throws LexicalErrorException, SyntaxErrorException {
        match("rw_break");
    }

    private void expressionOp() throws LexicalErrorException, SyntaxErrorException {
        if(Firsts.isFirst("Expression", currentToken.getToken())){
            expression();
        }
        else{
            //TODO Check follows
        }
    }

    private void ifT() throws LexicalErrorException, SyntaxErrorException {
        match("rw_if");
        match("pm_par_open");
        expression();
        match("pm_par_close");
        statement();
        possibleElse();
    }
    private void possibleElse() throws LexicalErrorException, SyntaxErrorException {
        if(currentToken.getToken().equals("rw_else")){
            match("rw_else");
            statement();
        }
        else{
            //TODO Check follows
        }
    }

    private void forT() throws LexicalErrorException, SyntaxErrorException {
        match("rw_for");
        match("pm_par_open");
        forExpression();
        match("pm_par_close");
        block();
    }

    private void forExpression() throws LexicalErrorException, SyntaxErrorException {
        if(Firsts.isFirst("Type", currentToken.getToken())){
            type();
            match("id_met_var");
            forSecondPart();
        }
        else{
            expression();
            forRest();
        }
    }

    private void forSecondPart() throws LexicalErrorException, SyntaxErrorException {
        if(currentToken.getToken().equals("pm_colon")){
            match("pm_colon");
            match("id_met_var");

        }
        else{
            initialize();
            forRest();
        }
    }

    private void initialize() throws LexicalErrorException, SyntaxErrorException {
        if(currentToken.getToken().equals("assign")){
            match("assign");
            complexExpression();
        }
        else {
            //TODO Check follows
        }
    }
    private void forRest() throws LexicalErrorException, SyntaxErrorException {
        //if(currentToken.getToken().equals("pm_semicolon")){
            match("pm_semicolon");
            expression();
        match("pm_semicolon");
        expression();
        //}
        //else{
            //TODO Check follows
       // }
    }

    private void whileT() throws LexicalErrorException, SyntaxErrorException {
        match("rw_while");
        match("pm_par_open");
        expression();
        match("pm_par_close");
        statement();
    }

    private void switchT() throws LexicalErrorException, SyntaxErrorException {
        match("rw_switch");
        match("pm_par_open");
        expression();
        match("pm_par_close");
        match("pm_brace_open");
        switchStateList();
        match("pm_brace_close");
    }

    private void switchStateList() throws LexicalErrorException, SyntaxErrorException {
        if(Firsts.isFirst("SwitchStatement", currentToken.getToken())){
            switchStatement();
            switchStateList();
        }
        else{
            //TODO Check follows
        }
    }

    private void switchStatement() throws LexicalErrorException, SyntaxErrorException {
        if(currentToken.getToken().equals("rw_case")){
            match("rw_case");
            primitiveLiteral();
            match("pm_colon");
            statementOp();
        }
        else{
            match("rw_default");
            match("pm_colon");
            statement();
        }
    }

    private void statementOp() throws LexicalErrorException, SyntaxErrorException {
        if(Firsts.isFirst("Statement", currentToken.getToken())){
            statement();
        }
        else{
            //TODO Check follows
        }
    }


    private void expression() throws LexicalErrorException, SyntaxErrorException {
        complexExpression();
        possibleExp();
    }

    private void possibleExp() throws LexicalErrorException, SyntaxErrorException {
        if(Firsts.isFirst("AssignmentOp", currentToken.getToken())){
            assignmentOp();
            complexExpression();
        }
        else{
            //TODO Check follows
        }
    }

    private void assignmentOp() throws LexicalErrorException, SyntaxErrorException {
        if(currentToken.getToken().equals("assign")){
            match("assign");
        }
        else if(currentToken.getToken().equals("assign_add")){
            match("assign_add");
        }
        else{
            match("assign_sub");
        }
    }

    private void complexExpression() throws LexicalErrorException, SyntaxErrorException {
        if(currentToken.getToken().equals("id_class")){
            match("id_class");
            staticCall();
        }
        else{
            basicExpression();
            possibleOp();
        }
    }

    private void possibleOp() throws LexicalErrorException, SyntaxErrorException {
        if(Firsts.isFirst("BinaryOp", currentToken.getToken())){
            binaryOp();
            complexExpression();
        }
        else{
            //TODO Check follows Y CHECKEAR GRAMTIA
        }
    }

    private void binaryOp() throws LexicalErrorException, SyntaxErrorException {
        if(currentToken.getToken().equals("op_and")){
            match("op_and");
        }
        else if(currentToken.getToken().equals("op_or")){
            match("op_or");
        }
        else if(currentToken.getToken().equals("op_equal")){
            match("op_equal");
        }
        else if(currentToken.getToken().equals("op_not_equal")){
            match("op_not_equal");
        }
        else if(currentToken.getToken().equals("op_less")){
            match("op_less");
        }
        else if(currentToken.getToken().equals("op_greater")){
            match("op_greater");
        }
        else if(currentToken.getToken().equals("op_less_equal")){
            match("op_less_equal");
        }
        else if(currentToken.getToken().equals("op_greater_equal")){
            match("op_greater_equal");
        }
        else if(currentToken.getToken().equals("op_add")){
            match("op_add");
        }
        else if(currentToken.getToken().equals("op_sub")){
            match("op_sub");
        }
        else if(currentToken.getToken().equals("op_mult")){
            match("op_mul");
        }
        else if(currentToken.getToken().equals("op_div")){
            match("op_div");
        }
        else{
            match("op_mod");
        }

    }

    private void basicExpression() throws LexicalErrorException, SyntaxErrorException {
        if(Firsts.isFirst("UnaryOp", currentToken.getToken())){
            unaryOp();
            operand();
        }
        else{
            operand();
        }
    }

    private void unaryOp() throws LexicalErrorException, SyntaxErrorException {
        if(currentToken.getToken().equals("op_add")){
            match("op_add");
        }
        else if(currentToken.getToken().equals("op_sub")){
            match("op_sub");
        }
        else{
            match("op_not");
        }
    }

    private void operand() throws LexicalErrorException, SyntaxErrorException {
        if(Firsts.isFirst("Literal", currentToken.getToken())){
            literal();
        }
        else{
            access();
        }
    }

    private void literal() throws LexicalErrorException, SyntaxErrorException {

       if(Firsts.isFirst("PrimitiveLiteral", currentToken.getToken())){
            primitiveLiteral();
        }
        else{
            objectLiteral();
        }
    }

    private void primitiveLiteral() throws LexicalErrorException, SyntaxErrorException {
        if(currentToken.getToken().equals("rw_true")){
            match("rw_true");
        }
        else if(currentToken.getToken().equals("rw_false")){
            match("rw_false");
        }
        else if(currentToken.getToken().equals("lit_int")){
            match("lit_int");
        }
        else{
            match("lit_char");
        }
    }

    private void objectLiteral() throws LexicalErrorException, SyntaxErrorException {
        if(currentToken.getToken().equals("rw_null")){
            match("rw_null");

        }
        else{
            match("lit_string");
        }

    }

    private void access() throws LexicalErrorException, SyntaxErrorException {
        primary();
        chainedOp();
    }

    private void primary() throws LexicalErrorException, SyntaxErrorException {
        if(Firsts.isFirst("AccessThis", currentToken.getToken())){
            accessThis();
        }
        else if(Firsts.isFirst("AccessConstructor", currentToken.getToken())){
            accessConstructor();
        }
        else if(Firsts.isFirst("PExpression", currentToken.getToken())){
            pExpression();
        }
        else{
            accessVarMethod();
        }
    }

    private void accessThis() throws LexicalErrorException, SyntaxErrorException {
        match("rw_this");
    }

    private void accessConstructor() throws LexicalErrorException, SyntaxErrorException {
        match("rw_new");
        className();
        actualArgs();
    }


    private void pExpression() throws LexicalErrorException, SyntaxErrorException {
        match("pm_par_open");
        expression();
        match("pm_par_close");
    }

    private void accessVarMethod() throws LexicalErrorException, SyntaxErrorException {
        match("id_met_var");
        possibleArgs();
    }

    private void possibleArgs() throws LexicalErrorException, SyntaxErrorException {
        if(Firsts.isFirst("ActualArgs", currentToken.getToken())){
            actualArgs();
        }
        else{
            //TODO Check follows
        }
    }

    private void actualArgs() throws LexicalErrorException, SyntaxErrorException {
        match("pm_par_open");
        expressionList();
        match("pm_par_close");
    }

    private void expressionList() throws LexicalErrorException, SyntaxErrorException {
        if(Firsts.isFirst("Expression", currentToken.getToken())){
            expression();
            match("pm_comma");
            expressionList();
        }
        else{
            //TODO Check follows
        }
    }

    private void chainedOp() throws LexicalErrorException, SyntaxErrorException {
        if(currentToken.getToken().equals("pm_period")){
            match("pm_period");
            match("id_met_var");
            possibleArgs();
            chainedOp();
        }
        else{
            //TODO Check follows
        }
    }


    private void match(String terminal) throws LexicalErrorException, SyntaxErrorException {
        if (currentToken.getToken().equals(terminal)) {
            getNewToken();
        }
        else{
            throw new SyntaxErrorException(terminal, currentToken, terminal);
        }
    }

}
