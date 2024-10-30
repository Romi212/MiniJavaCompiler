package SyntaxAnalyzer;

import AST.BLockDeclaration;
import AST.Expressions.Access.*;
import AST.Expressions.AssignmentExp;
import AST.Expressions.BinaryOperations.*;
import AST.Expressions.ExpressionNode;
import AST.Expressions.Literals.*;
import AST.Expressions.UnaryOperations.NotOperation;
import AST.Expressions.UnaryOperations.SignExpression;
import AST.Expressions.UnaryOperations.UnaryExpression;
import AST.Statements.*;
import LexicalAnalyzer.LexicalAnalyzer;
import SymbolTable.Attributes.AttributeDeclaration;
import SymbolTable.Clases.ClassDeclaration;
import SymbolTable.MethodDeclaration;
import SymbolTable.MemberDeclaration;
import SymbolTable.SymbolTable;
import SymbolTable.Types.*;
import SymbolTable.ConstructorDeclaration;
import utils.Exceptions.CompilerException;
import utils.Exceptions.LexicalErrorException;
import utils.Exceptions.SyntaxErrorException;
import utils.Token;

import java.util.ArrayList;

public class SyntaxAnalyzerImp implements SyntaxAnalyzer {

    private LexicalAnalyzer lexicalAnalyzer;
    private Token currentToken;

    public SyntaxAnalyzerImp(LexicalAnalyzer lexicalAnalyzer){
        this.lexicalAnalyzer = lexicalAnalyzer;
    }

    @Override
    public String analyzeSintax() throws CompilerException{
        getNewToken();

        initial();

        if(SymbolTable.isCorrect(currentToken)) System.out.println("[SinErrores]");
        return "[SinErrores]";
    }

    private void getNewToken() throws LexicalErrorException {
        currentToken = lexicalAnalyzer.nextToken();
    }
    private void initial() throws CompilerException {
        if(Firsts.isFirst("ClassList", currentToken.getToken())){
            classList();
        }
        else if(currentToken.getToken().equals("EOF")){
            //Termino bien uwu
        }else{
            throw new SyntaxErrorException( currentToken, "a class or end of file");
        }
    }

    private void classList() throws CompilerException {
        if (Firsts.isFirst("Class", currentToken.getToken())) {
            classT();
            classList();
        }
        else if( Follows.itFollows("ClassList", currentToken.getToken())){
            //Termino de leer el archivo correctamente
        }
        else{
            throw new SyntaxErrorException( currentToken, "a class or end of file");
        }

    }

    private void classT() throws CompilerException{
        if(currentToken.getToken().equals("rw_class")){
            normalClass();
        }
        else if(currentToken.getToken().equals("rw_abstract")){
            abstractClass();
        }
        else{
            throw new SyntaxErrorException( currentToken, "reserved word 'class' or 'abstract'");
        }
    }

    private void normalClass() throws CompilerException {
        SymbolTable.addClass(classSignature());
        match("pm_brace_open");
        memberList();
        match("pm_brace_close");
    }

    private void abstractClass() throws CompilerException{
        match("rw_abstract");
        ClassDeclaration newClass = classSignature();
        newClass.setAbstract(true);
        SymbolTable.addClass(newClass);
        match("pm_brace_open");
        abstractMemberList();
        match("pm_brace_close");
    }

    private ClassDeclaration classSignature() throws CompilerException {
        match("rw_class");
        ClassDeclaration newClass = className();
        Token parent = parents(newClass);
        newClass.setParent(parent);
        return newClass;
    }

    private ClassDeclaration className() throws CompilerException {
        Token newClass = currentToken;
        match("id_class");
        ClassDeclaration newClassDeclaration = new ClassDeclaration(newClass);
        newClassDeclaration.addParametricTypes(generic());
        //TODO VER GENERIC
        return newClassDeclaration;
    }

    private ArrayList<Token> generic() throws CompilerException {
        ArrayList<Token> genericTypes = new ArrayList<>();
        if(currentToken.getToken().equals("op_less")){
            match("op_less");
            Token genericType = currentToken;
            match("id_class");
            genericTypes = pTypesList();
            genericTypes.add(genericType);
            match("op_greater");
        }
        else if(Follows.itFollows("Generic", currentToken.getToken())){

        }else{
            throw new SyntaxErrorException( currentToken, "reserved word extends or ( or {");
        }
        return genericTypes;
    }

    private ArrayList<Token> pTypesList() throws CompilerException {
        ArrayList<Token> pTypes = new ArrayList<>();
        if(currentToken.getToken().equals("pm_comma")){
            match("pm_comma");
            Token genericType = currentToken;
            match("id_class");
            pTypes = pTypesList();
            pTypes.add(genericType);
        }
        else if(Follows.itFollows("PTypesList", currentToken.getToken())){
            return new ArrayList<>();
        }else{
            throw new SyntaxErrorException( currentToken, "another parametric Type or >");
        }
        return pTypes;
    }
    private Token parents(ClassDeclaration child) throws CompilerException{
        Token parentClass;
        if(currentToken.getToken().equals("rw_extends")){
            match("rw_extends");
            ClassDeclaration parent = className();
            parentClass = parent.getName();
            SymbolTable.checkParent(parent, child);
        }
        else if(Follows.itFollows("Parents", currentToken.getToken())){
            parentClass = new Token("rw_object", "Object",0);
            //Doenst extends any class
        }else{
            throw new SyntaxErrorException( currentToken, "{");
        }
        return parentClass;
    }
    private void memberList() throws  CompilerException {
        if(Firsts.isFirst("VisibleMember", currentToken.getToken())){

            visibleMember();
            memberList();
        }
        else if(Follows.itFollows("MemberList", currentToken.getToken())){
            //No more members
        }else{
            throw new SyntaxErrorException(currentToken, "member or end of class declaration }");
        }
    }

    private void abstractMemberList() throws CompilerException {
        if(Firsts.isFirst("VisibleMember", currentToken.getToken()) || currentToken.getToken().equals("rw_abstract")){

            abstractVisibleMember();
            abstractMemberList();
        }
        else if(Follows.itFollows("MemberList", currentToken.getToken())){
            //No more members in abstract class
        }else{
            throw new SyntaxErrorException( currentToken, " member or end of class declaration }");
        }
    }

    private void visibleMember() throws CompilerException {
        Token visibility = visibility();
        member(visibility);
    }

    private void abstractVisibleMember() throws CompilerException {
        Token vis = visibility();
        abstractMember(vis);
    }

    private void abstractMember(Token visibility) throws CompilerException {
        if(currentToken.getToken().equals("rw_abstract")){
            abstractMethod(visibility);
        }
        else if(Firsts.isFirst("Member", currentToken.getToken())){
            member(visibility);
        }else{
            throw new SyntaxErrorException( currentToken, "abstract method or member declaration");
        }
    }
    private Token visibility() throws CompilerException {
        Token visibility;
        if(currentToken.getToken().equals("rw_public")){
            visibility = currentToken;
            match("rw_public");
        }
        else if(currentToken.getToken().equals("rw_private")){
            visibility = currentToken;
            match("rw_private");
        }
        else if(Firsts.isFirst("Member", currentToken.getToken()) || currentToken.getToken().equals("rw_abstract")){
            visibility = new Token("rw_public", "public", -1);
            //No visibility explicity declared
        } else{
            throw new SyntaxErrorException( currentToken, "attribute or method declaration");
        }
        return visibility;
    }
    private void member(Token visibility) throws CompilerException {
        if(currentToken.getToken().equals("rw_static")){
            match("rw_static");
            MemberType type = memberType();
            MemberDeclaration member = declaration(type);
            member.setVisibility(visibility);
            member.setStatic(true);
        }
        else if( currentToken.getToken().equals("id_class")){
            Token className = currentToken;
            match("id_class");
            MemberDeclaration member = possibleCtor(className);
            member.setVisibility(visibility);
        }
        else if(Firsts.isFirst("NonObjectType", currentToken.getToken())){
            MemberType type = nonObjectType();
            MemberDeclaration member = declaration(type);
            member.setVisibility(visibility);
        }
        else {
            throw new SyntaxErrorException( currentToken, "attribute or non abstract method declaration");
        }
    }

    private void abstractMethod(Token visibility) throws CompilerException {
        match("rw_abstract");
        MemberType type = memberType();
        Token name = currentToken;
        match("id_met_var");
        MethodDeclaration newMethod = new MethodDeclaration(name, type);
        newMethod.setAbstract(true);
        newMethod.setVisibility(visibility);
        formalArgs(newMethod);
        SymbolTable.addAbstractMethod(newMethod);
        match("pm_semicolon");
    }

    private MemberDeclaration declaration(MemberType type) throws CompilerException {
        Token name = currentToken;
        match("id_met_var");
        return body(name, type);
    }

    private MemberDeclaration possibleCtor(Token className) throws CompilerException {
        if(currentToken.getToken().equals("pm_par_open")){
            return constructor(className);
        }
        else if(currentToken.getToken().equals("id_met_var")|| currentToken.getToken().equals("op_less")) {
            ArrayList<Token> parameters = generic();
            MemberObjectType type = new MemberObjectType(className);
            type.addParametricTokens(parameters);
            return declaration(type);
        }
        else{
            throw new SyntaxErrorException(currentToken, "constructor or method declaration");
        }
    }

    private MemberDeclaration body(Token name, MemberType type) throws CompilerException {
        if(currentToken.getToken().equals("pm_par_open")){
            MethodDeclaration newMethod = new MethodDeclaration(name, type);
            formalArgs(newMethod);

            newMethod.addBlock(block());
            SymbolTable.addMethod(newMethod);
            return newMethod;
        }
        else if(currentToken.getToken().equals("pm_semicolon")|| currentToken.getToken().equals("assign")){
            AttributeDeclaration newAtt = SymbolTable.addAttribute(name, type);
            initialize();
            match("pm_semicolon");

            return newAtt;
        }
        else{
            throw new SyntaxErrorException( currentToken, "method declaration or attribute initialization");
        }



    }

    private MemberDeclaration constructor(Token name) throws CompilerException{
        ConstructorDeclaration newMethod = new ConstructorDeclaration(name);
        formalArgs(newMethod);
        SymbolTable.addConstructor(newMethod);
        BLockDeclaration block = block();
        newMethod.addBlock(block);
        return newMethod;
    }

    private MemberType nonObjectType() throws CompilerException {
        Token toReturn = currentToken;
        MemberType type;
        if(currentToken.getToken().equals("rw_void")){
            match("rw_void");
            return new VoidType(toReturn);
        }
        else if(Firsts.isFirst("PrimitiveType", currentToken.getToken())){
            type = primitiveType();
        }
        else {
            throw new SyntaxErrorException(currentToken, "primitive type or void");
        }
        return type;
    }
    private MemberType memberType() throws   CompilerException {
        MemberType toReturn;
        if(Firsts.isFirst("Type", currentToken.getToken())){
            toReturn = type();
        }
        else if( currentToken.getToken().equals("rw_void")){
            toReturn = new VoidType(currentToken);
            match("rw_void");

        }else{
            throw new SyntaxErrorException( currentToken, "type or void");
        }
        return toReturn;
    }

    private MemberType type() throws CompilerException {
        MemberType toReturn;
        if(Firsts.isFirst("PrimitiveType", currentToken.getToken())){
            toReturn = primitiveType();
        }
        else if( currentToken.getToken().equals("id_class")){
            ClassDeclaration className = className();
            MemberObjectType type = new MemberObjectType(className.getName());

            type.addParametricTypes(className.getParametricTypes());
            toReturn = type;
        }else{
            throw new SyntaxErrorException(currentToken, "primitive type or class name");
        }
        return toReturn;
    }

    private MemberType primitiveType() throws CompilerException {
        Token toReturn = currentToken;
        MemberType type;
        if(currentToken.getToken().equals("rw_int")){
            match("rw_int");
            type = new IntegerType(toReturn);
        }
        else if(currentToken.getToken().equals("rw_char")){
            match("rw_char");
            type = new CharacterType(toReturn);
        }
        else if(currentToken.getToken().equals("rw_boolean")){
            match("rw_boolean");
            type = new BooleanType(toReturn);
        } else{
            throw new SyntaxErrorException(currentToken, "int, char or boolean");
        }
        return type;

    }

    private void formalArgs(MethodDeclaration newMethod) throws CompilerException {
        match("pm_par_open");
        formalArgsList(newMethod);
        match("pm_par_close");
    }

    private void formalArgsList(MethodDeclaration method) throws CompilerException {
        if(Firsts.isFirst("FormalArg",currentToken.getToken())){
            formalArg(method);
            optionalArgsList(method);
        }
        else if(currentToken.getToken().equals("pm_par_close")){
            //No formal arguments
        }
        else{
            throw new SyntaxErrorException(currentToken, "formal argument or )");
        }

    }

    private void optionalArgsList(MethodDeclaration method) throws CompilerException {
        if(currentToken.getToken().equals("pm_comma")){
            match("pm_comma");
            formalArg(method);
            optionalArgsList(method);
        }
        else if(currentToken.getToken().equals("pm_par_close")){
            //No more formal args
        }
        else{
            throw new SyntaxErrorException(currentToken, "another formal argument or )");
        }
    }

    private void formalArg(MethodDeclaration method) throws CompilerException {
        MemberType argType = type();
        Token argName = currentToken;
        match("id_met_var");
        method.addParameter(argName, argType);
    }

    private BLockDeclaration block() throws CompilerException {
        BLockDeclaration block = new BLockDeclaration(currentToken);
        match("pm_brace_open");
        statementsList(block);
        match("pm_brace_close");
        return block;
    }

    private void statementsList(BLockDeclaration block) throws CompilerException {
        if(Firsts.isFirst("Statement", currentToken.getToken())){
            block.addStatement(statement());
            statementsList(block);
        }
        else if(currentToken.getToken().equals("pm_brace_close")){
            //No more statements
        }
        else{
            throw new SyntaxErrorException(currentToken, "statement or }");
        }
    }

    private StatementNode statement() throws CompilerException {

        StatementNode statementN = null;

        if(currentToken.getToken().equals("pm_semicolon")){
            statementN = new SemicolonNode(currentToken);
            match("pm_semicolon");
        }
        else if(currentToken.getToken().equals("id_class")){

            statementN = object();
            match("pm_semicolon");

        }
        else if(Firsts.isFirst("NonStaticExp", currentToken.getToken())){
            statementN = new ExpressionStatement(nonStaticExp());
            match("pm_semicolon");

        }
        else if(Firsts.isFirst("LocalType", currentToken.getToken())){
            statementN = primitiveVar();
            match("pm_semicolon");

        }
        else if(Firsts.isFirst("Return", currentToken.getToken())){
            statementN = returnT();
            match("pm_semicolon");
        }
        else if(Firsts.isFirst("Break", currentToken.getToken())){

            statementN = breakT();
            match("pm_semicolon");
        }
        else if(Firsts.isFirst("If", currentToken.getToken())){

            return ifT();

        }
        else if(Firsts.isFirst("While", currentToken.getToken())){

            return whileT();

        }
        else if(Firsts.isFirst("Switch", currentToken.getToken())){
            return switchT();
        }
        else if(Firsts.isFirst("For", currentToken.getToken())){
            forT();
        }

        else if (currentToken.getToken().equals("pm_brace_open")){
            return block();
        }
        else{
            throw new SyntaxErrorException(currentToken, "valid statement");
        }

        return statementN;

    }


    private StatementNode object() throws CompilerException {
        Token name = currentToken;
        match("id_class");
        if (currentToken.getToken().equals("pm_period")) {
            ChainedExpression staticClass = new ChainedExpression();
            staticClass.setFirst(new AccessStaticClass(name));
            ExpressionNode exp = staticCall(staticClass);
            exp.setName(name);
            BinaryExpression binexp = possibleOp();
            if(binexp != null){
                binexp.addLeft(exp);
                exp = binexp;
            }
            AssignmentExp ass = possibleExp();
            ExpressionStatement expression = new ExpressionStatement(null);
            if(ass != null) {
                ass.addAccess(exp);
                expression.setExpression(ass);
            }else{
                expression.setExpression(exp);
            }
            return expression;

        }
        else if(currentToken.getToken().equals("op_less")|| currentToken.getToken().equals("id_met_var")){
            ArrayList<Token> parametric = generic();
            DeclarationStatement object = localVar();
            MemberObjectType type = new MemberObjectType(name);
            type.setParametricInstance(parametric);
            object.setType(type);
            return object;
        }
        else{
            throw new SyntaxErrorException(currentToken, "static call or local object declaration");
        }
    }
    private DeclarationStatement localVar() throws CompilerException {
        Token var = currentToken;
        match("id_met_var");
        DeclarationStatement declarationStatement = chainedVar();
        declarationStatement.addVariable(var);
        ExpressionNode exp = initialize();
        declarationStatement.setExpression(exp);
        return declarationStatement;
    }

    private ExpressionNode staticCall(ChainedExpression staticClass) throws CompilerException {
        match("pm_period");
        Link chain = new Link();
        AccessMethod access = new AccessMethod();
        access.setName(currentToken);
        match("id_met_var");
        actualArgs(access);
        Link rest = chainedOp();
        chain.setLink(access);
        chain.setNext(rest);
        staticClass.addNext(chain);

        return staticClass;
    }


    private DeclarationStatement chainedVar() throws CompilerException {
        if(currentToken.getToken().equals("pm_comma")){
            match("pm_comma");
            Token var = currentToken;
            match("id_met_var");
            DeclarationStatement declarationStatement = chainedVar();
            declarationStatement.addVariable(var);
            return declarationStatement;
        }
        else if(Follows.itFollows("ChainedVar", currentToken.getToken())){
            //No more vars declared here
            return new DeclarationStatement(null);
        }else{
            throw new SyntaxErrorException(currentToken, "another var, initialization or ;");
        }
    }


    private StatementNode primitiveVar() throws CompilerException {
        if(Firsts.isFirst("PrimitiveType", currentToken.getToken())){
            MemberType t = primitiveType();
            DeclarationStatement a = localVar();
            a.setType(t);
            return a;
        }
        else if(currentToken.getToken().equals("rw_var")){
            match("rw_var");
            LocalVarDeclaration localVar = new LocalVarDeclaration(currentToken);
            match("id_met_var");
            ExpressionNode exp =initialize();
            localVar.setExpression(exp);
            return localVar;
        }
        else{
            throw new SyntaxErrorException(currentToken, "local var type");
        }
    }

    private ExpressionNode nonStaticExp() throws CompilerException {
        ExpressionNode expN = nSComplexExpression();
        AssignmentExp assign = possibleExp();
        if( assign != null){
            assign.addAccess(expN);
            return assign;
        }
        return expN;
    }

    private ExpressionNode nSComplexExpression() throws CompilerException {
        ExpressionNode exp = basicExpression();
        BinaryExpression operation  = possibleOp();
        if(operation!= null){
            operation.addLeft(exp);
            return operation;
        }else return exp;
    }
    private StatementNode returnT() throws CompilerException {
        Token name = currentToken;
        match("rw_return");
        return new ReturnNode(name, expressionOp());
    }

    private BreakStatement breakT() throws CompilerException {
        Token name = currentToken;
        match("rw_break");
        return new BreakStatement(name);
    }

    private ExpressionNode expressionOp() throws CompilerException {
        if(Firsts.isFirst("Expression", currentToken.getToken())){
            return expression();
        }
        else if (currentToken.getToken().equals("pm_semicolon")){
            //No expression after return
            return null;
        }else{
            throw new SyntaxErrorException(currentToken, "expression or ;");
        }
    }

    private IfNode ifT() throws CompilerException {
        Token name = currentToken;
        match("rw_if");
        match("pm_par_open");
        ExpressionNode condition = expression();
        match("pm_par_close");
        StatementNode thenStatement = statement();
        StatementNode elseStatement = possibleElse();
        return new IfNode(condition, thenStatement, elseStatement, name);
    }
    private StatementNode possibleElse() throws CompilerException {
        if(currentToken.getToken().equals("rw_else")){
            match("rw_else");
            return statement();
        }
        else if(Firsts.isFirst("Statement", currentToken.getToken())|| currentToken.getToken().equals("pm_brace_close")){
            //No else clause
            return null;
        }
        else{
            throw new SyntaxErrorException(currentToken, "else, valid statement, or }");
        }
    }

    private void forT() throws CompilerException {
        match("rw_for");
        match("pm_par_open");
        forExpression();
        match("pm_par_close");
        statement();
    }

    private void forExpression() throws CompilerException {
        if(Firsts.isFirst("Type", currentToken.getToken())){
            type();
            match("id_met_var");
            forSecondPart();
        }
        else if(Firsts.isFirst("BasicExpression", currentToken.getToken())){
            nonStaticExp();
            forRest();
        }
        else {
            throw new SyntaxErrorException(currentToken, "first expression of for, or iterator variable declaration");
        }
    }

    private void forSecondPart() throws CompilerException {
        if(currentToken.getToken().equals("pm_colon")){
            match("pm_colon");
            expression();
        }
        else if (currentToken.getToken().equals("pm_semicolon") || currentToken.getToken().equals("assign")){
            initialize();
            forRest();
        }
        else{
            throw new SyntaxErrorException(currentToken, "second part of for");
        }
    }

    private ExpressionNode initialize() throws CompilerException {
        if(currentToken.getToken().equals("assign")){
            match("assign");
            return  complexExpression();
        }
        else if (currentToken.getToken().equals("pm_semicolon")){
            //Not initialized
            return null;
        }
        else{
            throw new SyntaxErrorException(currentToken, "var initialization or ;");
        }
    }
    private void forRest() throws CompilerException {

        match("pm_semicolon");
        expression();
        match("pm_semicolon");

        expression();

    }

    private WhileNode whileT() throws CompilerException {
        Token name = currentToken;
        match("rw_while");
        match("pm_par_open");
        ExpressionNode condition = expression();

        match("pm_par_close");
        StatementNode body = statement();
        return new WhileNode(condition, body, name);
    }

    private SwitchNode switchT() throws CompilerException {
        match("rw_switch");
        match("pm_par_open");
        ExpressionNode exp = expression();

        match("pm_par_close");
        match("pm_brace_open");
        SwitchNode node = switchStateList();
        match("pm_brace_close");
        node.setExpression(exp);
        return node;

    }

    private SwitchNode switchStateList() throws CompilerException {
        if(Firsts.isFirst("SwitchStatement", currentToken.getToken())){
            CaseNode c = switchStatement();
            SwitchNode s = switchStateList();
            s.addCase(c);
            return s;
        }
        else if(currentToken.getToken().equals("pm_brace_close")){
            //No more switch statements
            return new SwitchNode(currentToken);
        }
        else{
            throw new SyntaxErrorException(currentToken, "switch statement or }");
        }
    }

    private CaseNode switchStatement() throws CompilerException {
        LiteralValue option = null;
        StatementNode statement;
        Token name = currentToken;
        if(currentToken.getToken().equals("rw_case")){
            match("rw_case");
            option = primitiveLiteral();
            name = option.getName();
            match("pm_colon");
            statement = statementOp();
        }
        else if(currentToken.getToken().equals("rw_default")){
            match("rw_default");
            match("pm_colon");
            statement = statement();
        }else{
            throw new SyntaxErrorException(currentToken, "case or default");
        }
        return new CaseNode(name, option, statement);
    }

    private StatementNode statementOp() throws CompilerException {
        if(Firsts.isFirst("Statement", currentToken.getToken())){
            return statement();
        }
        else if(currentToken.getToken().equals("rw_case")|| currentToken.getToken().equals("rw_default")|| currentToken.getToken().equals("pm_brace_close")){
            //No statement here
            return null;
        }else{
            throw new SyntaxErrorException(currentToken, "valid statement or case or default");
        }
    }


    private ExpressionNode expression() throws CompilerException {
        ExpressionNode expN = complexExpression();
        AssignmentExp assign = possibleExp();
        if( assign != null){
            assign.addAccess(expN);
            return assign;
        }
        return expN;
    }

    private AssignmentExp possibleExp() throws CompilerException {
        if(Firsts.isFirst("AssignmentOp", currentToken.getToken())){
            Token op = assignmentOp();
            AssignmentExp assign = new AssignmentExp(op);
            assign.addExpression(complexExpression());
            return assign;
        }
        else if(Follows.itFollows("PossibleExp", currentToken.getToken())){
            //Expression ended
            return null;
        }else{
            throw new SyntaxErrorException(currentToken, "assignment operator or ; or )");
        }
    }

    private Token assignmentOp() throws CompilerException {
        Token toReturn = currentToken;
        if(currentToken.getToken().equals("assign")){
            match("assign");
        }
        else if(currentToken.getToken().equals("assign_add")){
            match("assign_add");
        }
        else if(currentToken.getToken().equals("assign_sub")){
            match("assign_sub");
        }
        else{
            throw new SyntaxErrorException(currentToken, "assignment operator");
        }
        return toReturn;
    }

    private ExpressionNode complexExpression() throws CompilerException {
        if(currentToken.getToken().equals("id_class")){
            Token name = currentToken;
            match("id_class");
            ChainedExpression staticClass = new ChainedExpression();
            staticClass.setFirst(new AccessStaticClass(name));
            ExpressionNode staticCall = staticCall(staticClass);
            BinaryExpression operation = possibleOp();
            if(operation != null) {
                operation.addLeft(staticCall);
                return operation;
            }
            return staticCall;

        }
        else if(Firsts.isFirst("BasicExpression", currentToken.getToken())){
            ExpressionNode operand = basicExpression();
            BinaryExpression operation = possibleOp();
            if(operation != null){
                operation.addLeft(operand);
                return operation;
            }else return operand;
        }
        else{
            throw new SyntaxErrorException(currentToken, "basic expression");
        }
    }

    private BinaryExpression possibleOp() throws CompilerException {
        if(Firsts.isFirst("BinaryOp", currentToken.getToken())){
            BinaryExpression operation = binaryOp();
            ExpressionNode right = complexExpression();

            operation.addRight(right);
            return operation;
        }
        else if(Follows.itFollows("PossibleExp", currentToken.getToken()) || Firsts.isFirst("AssignmentOp", currentToken.getToken())){
            //End of expression
            return null;
        }
        else{
            throw new SyntaxErrorException(currentToken, "binary operation, assignment or end of expression");
        }
    }

    private BinaryExpression binaryOp() throws CompilerException {
        Token toReturn = currentToken;
        BinaryExpression exp ;
        if(currentToken.getToken().equals("op_and")){
            match("op_and");
            exp = new LogicalOperation(toReturn);
        }
        else if(currentToken.getToken().equals("op_or")){
            match("op_or");
            exp = new LogicalOperation(toReturn);
        }
        else if(currentToken.getToken().equals("op_equal")){
            match("op_equal");
            exp = new EqualsOperation(toReturn);
        }
        else if(currentToken.getToken().equals("op_not_equal")){
            match("op_not_equal");
            exp = new EqualsOperation(toReturn);
        }
        else if(currentToken.getToken().equals("op_less")){
            match("op_less");
            exp = new CompareOperation(toReturn);
        }
        else if(currentToken.getToken().equals("op_greater")){
            match("op_greater");
            exp = new CompareOperation(toReturn);
        }
        else if(currentToken.getToken().equals("op_less_equal")){
            match("op_less_equal");
            exp = new CompareOperation(toReturn);
        }
        else if(currentToken.getToken().equals("op_greater_equal")){
            match("op_greater_equal");
            exp = new CompareOperation(toReturn);
        }
        else if(currentToken.getToken().equals("op_add")){
            match("op_add");
            exp = new NumberOperation(toReturn);
        }
        else if(currentToken.getToken().equals("op_sub")){
            match("op_sub");
            exp = new NumberOperation(toReturn);
        }
        else if(currentToken.getToken().equals("op_mult")){
            match("op_mult");
            exp = new NumberOperation(toReturn);
        }
        else if(currentToken.getToken().equals("op_div")){
            match("op_div");
            exp = new NumberOperation(toReturn);
        }
        else if ( currentToken.getToken().equals("op_mod")){
            match("op_mod");
            exp = new NumberOperation(toReturn);
        }
        else{
            throw new SyntaxErrorException(currentToken, "binary operator");
        }
        return exp;

    }

    private ExpressionNode basicExpression() throws CompilerException {
        if(Firsts.isFirst("UnaryOp", currentToken.getToken())){
            UnaryExpression op = unaryOp();
            ExpressionNode exp = operand();
            op.addExpression(exp);
            return op;
        }
        else if(Firsts.isFirst("Operand", currentToken.getToken())){
            return nsOperand();
        }
        else{
            throw new SyntaxErrorException(currentToken, "unary operator or operand");
        }
    }

    private UnaryExpression unaryOp() throws CompilerException {
        Token toReturn = currentToken;
        UnaryExpression exp;
        if(currentToken.getToken().equals("op_add")){
            match("op_add");
            exp = new SignExpression(toReturn);
        }
        else if(currentToken.getToken().equals("op_sub")){
            match("op_sub");
            exp = new SignExpression(toReturn);
        }
        else if(currentToken.getToken().equals("op_not")){
            match("op_not");
            exp = new NotOperation(toReturn);
        }
        else{
            throw new SyntaxErrorException(currentToken, "unary operator");
        }
        return exp;
    }

    private ExpressionNode operand() throws CompilerException{
        if(currentToken.getToken().equals("id_class")){
            ChainedExpression staticClass = new ChainedExpression();
            staticClass.setFirst(new AccessStaticClass(currentToken));
            match("id_class");
            return staticCall(staticClass);
        }
        else {
            return nsOperand();
        }
    }

    private ExpressionNode nsOperand() throws CompilerException {
        if(Firsts.isFirst("Literal", currentToken.getToken())){
            return literal();
        }
        else if(Firsts.isFirst("Access", currentToken.getToken())){
            return access();
        }
        else{
            throw new SyntaxErrorException(currentToken, "literal or access");
        }
    }

    private LiteralValue literal() throws CompilerException {

       if(Firsts.isFirst("PrimitiveLiteral", currentToken.getToken())){
            return primitiveLiteral();
        }
        else if(currentToken.getToken().equals("rw_null")|| currentToken.getToken().equals("lit_string")){
            return objectLiteral();
        }
        else {
            throw new SyntaxErrorException(currentToken, "primitive or object literal");
       }
    }

    private LiteralValue primitiveLiteral() throws CompilerException {
        LiteralValue lit;
        Token literalV = currentToken;
        if(currentToken.getToken().equals("rw_true")){
            match("rw_true");
            lit = new LiteralBool(literalV);
        }
        else if(currentToken.getToken().equals("rw_false")){
            match("rw_false");
            lit = new LiteralBool(literalV);
        }
        else if(currentToken.getToken().equals("lit_int")){
            match("lit_int");
            lit = new LiteralInt(literalV);
        }
        else if(currentToken.getToken().equals("lit_char")){
            match("lit_char");
            lit = new LiteralChar(literalV);
        }
        else{
            throw new SyntaxErrorException(currentToken,"primitive literal");
        }
        return lit;
    }

    private LiteralValue objectLiteral() throws CompilerException {
        LiteralValue lit;
        Token literalV = currentToken;
        if(currentToken.getToken().equals("rw_null")){
            match("rw_null");
            lit = new LiteralNull(literalV);

        }
        else if(currentToken.getToken().equals("lit_string")){
            match("lit_string");
            lit = new LiteralString(literalV);
        }
        else{
            throw new SyntaxErrorException(currentToken,"object literal");
        }
        return lit;

    }

    private ExpressionNode access() throws CompilerException {
        AccessMember exp = primary();
        ChainedExpression chain = new ChainedExpression();
        chain.setFirst(exp);
        Link rest = chainedOp();
        chain.addNext(rest);
        return chain;
    }

    private AccessMember primary() throws CompilerException {
        if(Firsts.isFirst("AccessThis", currentToken.getToken())){
            return accessThis();
        }
        else if(Firsts.isFirst("AccessConstructor", currentToken.getToken())){
            return accessConstructor();
        }
        else if(Firsts.isFirst("PExpression", currentToken.getToken())){
            return pExpression();
        }
        else if(currentToken.getToken().equals("id_met_var")){
            return accessVarMethod();
        }
        else{
            throw new SyntaxErrorException(currentToken, "access to a value");
        }
    }

    private AccessThis accessThis() throws CompilerException {
        AccessThis access = new AccessThis(currentToken);
        match("rw_this");
        return access;
    }

    private AccessNewObject accessConstructor() throws CompilerException {
        match("rw_new");
        Token className = currentToken;
        match("id_class");
        optionalGeneric();
        MemberObjectType type = new MemberObjectType(className);
        AccessNewObject access = new AccessNewObject(className, type);
        actualArgs(access);
        return access;
    }

    private void optionalGeneric() throws CompilerException {
        if(currentToken.getToken().equals("op_less")){
            match("op_less");
            optionalTypes();
            match("op_greater");
        }
        else if(currentToken.getToken().equals("pm_par_open")){
            //No generic
        }else{
            throw new SyntaxErrorException(currentToken, "generic or (");
        }
    }
    private void optionalTypes() throws CompilerException {

        if(currentToken.getToken().equals("id_class")){
            match("id_class");
            pTypesList();
        }else if(currentToken.getToken().equals("op_greater")) {
            //No parametric Types inside <>
        }else{
            throw new SyntaxErrorException( currentToken, "parametric type or >");
        }

    }

    private AccessPE pExpression() throws CompilerException {
        match("pm_par_open");
        ExpressionNode exp = expression();
        match("pm_par_close");
        return new AccessPE(exp);
    }

    private AccessMember accessVarMethod() throws CompilerException {
        Token name = currentToken;
        match("id_met_var");
        AccessMember access = possibleArgs();
        access.setName(name);
        return access;
    }

    private AccessMember possibleArgs() throws CompilerException {
        if(Firsts.isFirst("ActualArgs", currentToken.getToken())){
            AccessMember access = new AccessMethod();
            actualArgs(access);
            return access;
        }
        else if(Follows.itFollows("ChainedOp", currentToken.getToken())){
            //No method call just variable
            return new AccessVar();
        } else{
            throw new SyntaxErrorException(currentToken, "actual arguments or end of access");
        }
    }

    private void actualArgs(AccessExpression access) throws CompilerException {
        match("pm_par_open");
        expressionList(access);
        match("pm_par_close");
    }

    private void expressionList(AccessExpression acc) throws CompilerException {
        if(Firsts.isFirst("Expression", currentToken.getToken())){
            ExpressionNode exp = expression();
            acc.addParameter(exp);
            optionalExpList(acc);
        }
        else if(currentToken.getToken().equals("pm_par_close")){
            //No actual parameters
        }else{
            throw new SyntaxErrorException(currentToken, "actual argument or )");
        }
    }

    private void optionalExpList(AccessExpression acc) throws CompilerException {
        if(currentToken.getToken().equals("pm_comma")){
            match("pm_comma");
            acc.addParameter(expression());
            optionalExpList(acc);
        }
        else if(currentToken.getToken().equals("pm_par_close")){
            //No more actual parameters
        }else{
            throw new SyntaxErrorException(currentToken, "another actual argument or )");
        }
    }

    private Link chainedOp() throws CompilerException {
        if(currentToken.getToken().equals("pm_period")){
            match("pm_period");
            Token name = currentToken;

            match("id_met_var");
            AccessMember access = possibleArgs();
            access.setName(name);
            Link next = chainedOp();
            Link chain = new Link();
            chain.setLink(access);
            chain.setNext(next);
            return chain;
        }
        else if(Follows.itFollows("ChainedOp", currentToken.getToken())){
            //No more chained operations
            return  null;
        }else{
            throw new SyntaxErrorException(currentToken, "chained operation or end of access");
        }
    }


    private void match(String terminal) throws CompilerException {
        if (currentToken.getToken().equals(terminal)) {
            getNewToken();
        }
        else{
            throw new SyntaxErrorException( currentToken, terminal);
        }
    }



}
