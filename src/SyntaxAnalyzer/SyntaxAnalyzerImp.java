package SyntaxAnalyzer;

import LexicalAnalyzer.LexicalAnalyzer;
import SymbolTable.Attributes.AttributeDeclaration;
import SymbolTable.ClassDeclaration;
import SymbolTable.MethodDeclaration;
import SymbolTable.MemberDeclaration;
import SymbolTable.SymbolTable;
import SymbolTable.Types.MemberObjectType;
import utils.Exceptions.CompilerException;
import utils.Exceptions.LexicalErrorException;
import utils.Exceptions.SyntaxErrorException;
import utils.Token;

import java.lang.reflect.Member;
import java.util.ArrayList;

public class SyntaxAnalyzerImp implements SyntaxAnalyzer {

    private LexicalAnalyzer lexicalAnalyzer;
    private Token currentToken;

    private ArrayList<String> syntaxErrors = new ArrayList<>();

    public SyntaxAnalyzerImp(LexicalAnalyzer lexicalAnalyzer){
        this.lexicalAnalyzer = lexicalAnalyzer;
    }

    @Override
    public String analyzeSintax() throws CompilerException{
        getNewToken();
        try{
            initial();
        }
        catch (SyntaxErrorException e){
            syntaxErrors.add(e.getMessage()+"\n"+e.getLongMessage());
        }
        String SyntaxOutput = "";
        if(syntaxErrors.size() > 0){
            for (String error: syntaxErrors){
                SyntaxOutput += error + "\n";
            }
        }else{
            SyntaxOutput = "[SinErrores]";
        }

        return SyntaxOutput;
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
        newClass.makeAbstract();
        SymbolTable.addClass(newClass);
        match("pm_brace_open");
        abstractMemberList();
        match("pm_brace_close");
    }

    private ClassDeclaration classSignature() throws CompilerException {
        match("rw_class");
        ClassDeclaration newClass = new ClassDeclaration(className());
        Token parent = parents();
        newClass.setParent(parent);
        return newClass;
    }



    private Token className() throws CompilerException {
        Token newClass = currentToken;
        match("id_class");
        generic();
        //TODO VER GENERIC
        return newClass;
    }

    private void generic() throws CompilerException {
        if(currentToken.getToken().equals("op_less")){
            match("op_less");
            optionalTypes();
            match("op_greater");
        }
        else if(Follows.itFollows("Generic", currentToken.getToken())){
            //TODO Check follows
        }else{
            throw new SyntaxErrorException( currentToken, "reserved word extends or ( or {");
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

    private void pTypesList() throws CompilerException {
        if(currentToken.getToken().equals("pm_comma")){
            match("pm_comma");
            match("id_class");
            pTypesList();
        }
        else if(Follows.itFollows("PTypesList", currentToken.getToken())){
            //Only one parametric Type
        }else{
            throw new SyntaxErrorException( currentToken, "another parametric Type or >");
        }
    }
    private Token parents() throws CompilerException{
        Token parentClass;
        if(currentToken.getToken().equals("rw_extends")){
            match("rw_extends");
            parentClass = className();
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
            try{
                visibleMember();
            } catch (SyntaxErrorException e){
                syntaxErrors.add(e.getMessage()+"\n"+e.getLongMessage());
                recover(new ArrayList<>(){{add("pm_brace_open"); add("pm_semicolon");}});
                if(currentToken.getToken().equals("pm_brace_open")){
                    block();
                }else if(currentToken.getToken().equals("pm_semicolon")){
                    match("pm_semicolon");
                }

            }
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
            try{
                abstractVisibleMember();
            } catch (SyntaxErrorException e){
                syntaxErrors.add(e.getMessage()+"\n"+e.getLongMessage());
                recover(new ArrayList<>(){{add("pm_brace_open"); add("pm_semicolon");}});
                if(currentToken.getToken().equals("pm_brace_open")){
                    block();
                }else if(currentToken.getToken().equals("pm_semicolon")){
                    match("pm_semicolon");
                }

            }
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
            abstractMethod();
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
            Token type = memberType();
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
            Token type = nonObjectType();
            MemberDeclaration member = declaration(type);
            member.setVisibility(visibility);
        }
        else {
            throw new SyntaxErrorException( currentToken, "attribute or non abstract method declaration");
        }
    }

    private void abstractMethod() throws CompilerException {
        match("rw_abstract");
        memberType();
        match("id_met_var");
        formalArgs(new MethodDeclaration(new Token("id_met_var", "rw_void", 0), new MemberObjectType(new Token("id_met_var", "rw_void", 0))));
        match("pm_semicolon");
    }

    private MemberDeclaration declaration(Token type) throws CompilerException {
        Token name = currentToken;
        match("id_met_var");
        return body(name, type);
    }

    private MemberDeclaration possibleCtor(Token className) throws CompilerException {
        if(currentToken.getToken().equals("pm_par_open")){
            return constructor(className);
        }
        else if(currentToken.getToken().equals("id_met_var")|| currentToken.getToken().equals("op_less")) {
            generic();
            return declaration(className);
        }
        else{
            throw new SyntaxErrorException(currentToken, "constructor or method declaration");
        }
    }

    private MemberDeclaration body(Token name, Token type) throws CompilerException {
        if(currentToken.getToken().equals("pm_par_open")){
            MethodDeclaration newMethod = SymbolTable.addMethod(name, type);
            formalArgs(newMethod);
            block();
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
        //TODO cambiar si hay mas de un constructor
        MethodDeclaration newMethod = SymbolTable.addConstructor(name);
        formalArgs(newMethod);
        block();
        return newMethod;
    }

    private Token nonObjectType() throws CompilerException {
        Token toReturn = currentToken;
        if(currentToken.getToken().equals("rw_void")){
            match("rw_void");
        }
        else if(Firsts.isFirst("PrimitiveType", currentToken.getToken())){
            toReturn = primitiveType();
        }
        else {
            throw new SyntaxErrorException(currentToken, "primitive type or void");
        }
        return toReturn;
    }
    private Token memberType() throws   CompilerException {
        Token toReturn;
        if(Firsts.isFirst("Type", currentToken.getToken())){
            toReturn = type();
        }
        else if( currentToken.getToken().equals("rw_void")){
            toReturn = currentToken;
            match("rw_void");

        }else{
            throw new SyntaxErrorException( currentToken, "type or void");
        }
        return toReturn;
    }

    private Token type() throws CompilerException {
        Token toReturn;
        if(Firsts.isFirst("PrimitiveType", currentToken.getToken())){
            toReturn = primitiveType();
        }
        else if( currentToken.getToken().equals("id_class")){
            toReturn = className();
        }else{
            throw new SyntaxErrorException(currentToken, "primitive type or class name");
        }
        return toReturn;
    }

    private Token primitiveType() throws CompilerException {
        Token toReturn = currentToken;
        if(currentToken.getToken().equals("rw_int")){
            match("rw_int");
        }
        else if(currentToken.getToken().equals("rw_char")){
            match("rw_char");
        }
        else if(currentToken.getToken().equals("rw_boolean")){
            match("rw_boolean");
        } else{
            throw new SyntaxErrorException(currentToken, "int, char or boolean");
        }
        return toReturn;

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
        Token argType = type();
        Token argName = currentToken;
        match("id_met_var");
        method.addParameter(argName, argType);
    }

    private void block() throws CompilerException {
        match("pm_brace_open");
        statementsList();
        match("pm_brace_close");
    }

    private void statementsList() throws CompilerException {
        if(Firsts.isFirst("Statement", currentToken.getToken())){
            statement();
            statementsList();
        }
        else if(currentToken.getToken().equals("pm_brace_close")){
            //No more statements
        }
        else{
            throw new SyntaxErrorException(currentToken, "statement or }");
        }
    }

    private void statement() throws CompilerException {
        if(currentToken.getToken().equals("pm_semicolon")){
            match("pm_semicolon");
        }
        else if(currentToken.getToken().equals("id_class")){
            match("id_class");
            try {
                object();
            } catch (SyntaxErrorException e) {
                syntaxErrors.add(e.getMessage()+"\n"+e.getLongMessage());
                recover("pm_semicolon");
            }
            match("pm_semicolon");
        }
        else if(Firsts.isFirst("NonStaticExp", currentToken.getToken())){

            try{
                nonStaticExp();
            } catch (SyntaxErrorException e){
                syntaxErrors.add(e.getMessage()+"\n"+e.getLongMessage());
                recover("pm_semicolon");
            }
            match("pm_semicolon");
        }
        else if(Firsts.isFirst("LocalType", currentToken.getToken())){
            try{
                primitiveVar();
            } catch (SyntaxErrorException e){
                syntaxErrors.add(e.getMessage()+"\n"+e.getLongMessage());
                recover("pm_semicolon");
            }
            match("pm_semicolon");
        }
        else if(Firsts.isFirst("Return", currentToken.getToken())){
            try{
                returnT();
            } catch (SyntaxErrorException e){
                syntaxErrors.add(e.getMessage()+"\n"+e.getLongMessage());
                recover("pm_semicolon");
            }
            match("pm_semicolon");
        }
        else if(Firsts.isFirst("Break", currentToken.getToken())){
            try{
                breakT();
            } catch (SyntaxErrorException e){
                syntaxErrors.add(e.getMessage()+"\n"+e.getLongMessage());
                recover("pm_semicolon");
            }
            match("pm_semicolon");
        }
        else if(Firsts.isFirst("If", currentToken.getToken())){
            try {
                ifT();
            } catch (SyntaxErrorException e){
                syntaxErrors.add(e.getMessage()+"\n"+e.getLongMessage());
                recover("pm_brace_close");
                match("pm_brace_close");
                if(currentToken.getToken().equals("rw_else")){
                    recover("pm_brace_close");
                    match("pm_brace_close");
                }
            }
        }
        else if(Firsts.isFirst("While", currentToken.getToken())){
            try{
                whileT();
            }catch (SyntaxErrorException e){
                syntaxErrors.add(e.getMessage()+"\n"+e.getLongMessage());
                recover("pm_brace_close");
                match("pm_brace_close");
            }
        }
        else if(Firsts.isFirst("Switch", currentToken.getToken())){
            try{
                switchT();
            }catch (SyntaxErrorException e){
                syntaxErrors.add(e.getMessage()+"\n"+e.getLongMessage());
                recover("pm_brace_close");
                match("pm_brace_close");
            }
        }
        else if(Firsts.isFirst("For", currentToken.getToken())){

            try {
                forT();
            } catch (SyntaxErrorException e){
                syntaxErrors.add(e.getMessage()+"\n"+e.getLongMessage());
                recover("pm_brace_close");
                match("pm_brace_close");
            }
        }

        else if (currentToken.getToken().equals("pm_brace_open")){
            try{
                block();
            } catch (SyntaxErrorException e){
                syntaxErrors.add(e.getMessage()+"\n"+e.getLongMessage());
                recover("pm_brace_close");
                match("pm_brace_close");
            }
        }
        else{
            throw new SyntaxErrorException(currentToken, "valid statement");
        }

    }

    //VOY POR ACA

    private void object() throws CompilerException {
        if (currentToken.getToken().equals("pm_period")) {
            staticCall();
            possibleExp();
        }
        else if(currentToken.getToken().equals("op_less")|| currentToken.getToken().equals("id_met_var")){
            generic();
            localVar();
        }
        else{
            throw new SyntaxErrorException(currentToken, "static call or local object declaration");
        }
    }
    private void localVar() throws CompilerException {
        match("id_met_var");
        chainedVar();
        initialize();
    }

    private void staticCall() throws CompilerException {
        match("pm_period");
        match("id_met_var");
        actualArgs();
        chainedOp();
        possibleOp();
    }


    private void chainedVar() throws CompilerException {
        if(currentToken.getToken().equals("pm_comma")){
            match("pm_comma");
            match("id_met_var");
            chainedVar();
        }
        else if(Follows.itFollows("ChainedVar", currentToken.getToken())){
            //No more vars declared here
        }else{
            throw new SyntaxErrorException(currentToken, "another var, initialization or ;");
        }
    }

    private void primitiveVar() throws CompilerException {
        localType();
        localVar();
    }

    private void localType() throws CompilerException {
        if(Firsts.isFirst("PrimitiveType", currentToken.getToken())){
            primitiveType();
        }
        else if(currentToken.getToken().equals("rw_var")){
            match("rw_var");
        }
        else{
            throw new SyntaxErrorException(currentToken, "local var type");
        }
    }

    private void nonStaticExp() throws CompilerException {
        nSComplexExpression();
        possibleExp();
    }

    private void nSComplexExpression() throws CompilerException {
        basicExpression();
        possibleOp();
    }
    private void returnT() throws CompilerException {
        match("rw_return");
        expressionOp();
    }

    private void breakT() throws CompilerException {
        match("rw_break");
    }

    private void expressionOp() throws CompilerException {
        if(Firsts.isFirst("Expression", currentToken.getToken())){
            try{
                expression();
            } catch (SyntaxErrorException e){
                syntaxErrors.add(e.getMessage()+"\n"+e.getLongMessage());
                recover("pm_semicolon");
            }
        }
        else if (currentToken.getToken().equals("pm_semicolon")){
            //No expression after return
        }else{
            throw new SyntaxErrorException(currentToken, "expression or ;");
        }
    }

    private void ifT() throws CompilerException {
        match("rw_if");
        match("pm_par_open");
        try{
            expression();
        } catch (SyntaxErrorException e){
            syntaxErrors.add(e.getMessage()+"\n"+e.getLongMessage());
            recover("pm_par_close");
        }
        match("pm_par_close");
        statement();
        possibleElse();
    }
    private void possibleElse() throws CompilerException {
        if(currentToken.getToken().equals("rw_else")){
            match("rw_else");
            statement();
        }
        else if(Firsts.isFirst("Statement", currentToken.getToken())|| currentToken.getToken().equals("pm_brace_close")){
            //No else clause
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
            //TODO CHANGE PARA QUIE SEA LL1 O ACLARAR
            try{
                nonStaticExp();
            } catch (SyntaxErrorException e){
                syntaxErrors.add(e.getMessage()+"\n"+e.getLongMessage());
                recover("pm_semicolon");
            }
            forRest();
        }
        else {
            throw new SyntaxErrorException(currentToken, "first expression of for, or iterator variable declaration");
        }
    }

    private void forSecondPart() throws CompilerException {
        if(currentToken.getToken().equals("pm_colon")){
            match("pm_colon");
            try{
                expression();
            } catch (SyntaxErrorException e){
                syntaxErrors.add(e.getMessage()+"\n"+e.getLongMessage());
                recover("pm_par_close");
            }

        }
        else if (currentToken.getToken().equals("pm_semicolon") || currentToken.getToken().equals("assign")){
            initialize();
            forRest();
        }
        else{
            throw new SyntaxErrorException(currentToken, "second part of for");
        }
    }

    private void initialize() throws CompilerException {
        if(currentToken.getToken().equals("assign")){
            match("assign");
            try{
                complexExpression();
                //Recuperar el valor de exprecion y guardarlo en la tabla de simbolos currentClas.initialize(name, value);
            } catch (SyntaxErrorException e){
                syntaxErrors.add(e.getMessage()+"\n"+e.getLongMessage());
                recover("pm_semicolon");
            }
        }
        else if (currentToken.getToken().equals("pm_semicolon")){
            //Not initialized
        }
        else{
            throw new SyntaxErrorException(currentToken, "var initialization or ;");
        }
    }
    private void forRest() throws CompilerException {

        match("pm_semicolon");
        try{
            expression();
        } catch (SyntaxErrorException e){
            syntaxErrors.add(e.getMessage()+"\n"+e.getLongMessage());
            recover("pm_semicolon");
        }
        match("pm_semicolon");
        try{
            expression();
        } catch (SyntaxErrorException e){
            syntaxErrors.add(e.getMessage()+"\n"+e.getLongMessage());
            recover("pm_par_close");
        }

    }

    private void whileT() throws CompilerException {
        match("rw_while");
        match("pm_par_open");
        try{
            expression();
        } catch (SyntaxErrorException e){
            syntaxErrors.add(e.getMessage()+"\n"+e.getLongMessage());
            recover("pm_par_close");
        }
        match("pm_par_close");
        statement();
    }

    private void switchT() throws CompilerException {
        match("rw_switch");
        match("pm_par_open");
        try{
            expression();
        } catch (SyntaxErrorException e){
            syntaxErrors.add(e.getMessage()+"\n"+e.getLongMessage());
            recover("pm_par_close");
        }
        match("pm_par_close");
        match("pm_brace_open");
        switchStateList();
        match("pm_brace_close");
    }

    private void switchStateList() throws CompilerException {
        if(Firsts.isFirst("SwitchStatement", currentToken.getToken())){
            switchStatement();
            switchStateList();
        }
        else if(currentToken.getToken().equals("pm_brace_close")){
            //No more switch statements
        }
        else{
            throw new SyntaxErrorException(currentToken, "switch statement or }");
        }
    }

    private void switchStatement() throws CompilerException {
        if(currentToken.getToken().equals("rw_case")){
            match("rw_case");
            primitiveLiteral();
            match("pm_colon");
            statementOp();
        }
        else if(currentToken.getToken().equals("rw_default")){
            match("rw_default");
            match("pm_colon");
            statement();
        }else{
            throw new SyntaxErrorException(currentToken, "case or default");
        }
    }

    private void statementOp() throws CompilerException {
        if(Firsts.isFirst("Statement", currentToken.getToken())){
            statement();
        }
        else if(currentToken.getToken().equals("rw_case")|| currentToken.getToken().equals("rw_default")|| currentToken.getToken().equals("pm_brace_close")){
            //No statement here
        }else{
            throw new SyntaxErrorException(currentToken, "valid statement or case or default");
        }
    }


    private void expression() throws CompilerException {
        complexExpression();
        possibleExp();
    }

    private void possibleExp() throws CompilerException {
        if(Firsts.isFirst("AssignmentOp", currentToken.getToken())){
            assignmentOp();
            complexExpression();
        }
        else if(Follows.itFollows("PossibleExp", currentToken.getToken())){
            //Expression ended
        }else{
            throw new SyntaxErrorException(currentToken, "assignment operator or ; or )");
        }
    }

    private void assignmentOp() throws CompilerException {
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
    }

    private void complexExpression() throws CompilerException {
        if(currentToken.getToken().equals("id_class")){
            match("id_class");
            staticCall();
        }
        else if(Firsts.isFirst("BasicExpression", currentToken.getToken())){
            basicExpression();
            possibleOp();
        }
        else{
            throw new SyntaxErrorException(currentToken, "basic expression");
        }
    }

    private void possibleOp() throws CompilerException {
        if(Firsts.isFirst("BinaryOp", currentToken.getToken())){
            binaryOp();
            complexExpression();
        }
        else if(Follows.itFollows("PossibleExp", currentToken.getToken()) || Firsts.isFirst("AssignmentOp", currentToken.getToken())){
            //End of expression
        }
        else{
            throw new SyntaxErrorException(currentToken, "binary operation, assignment or end of expression");
        }
    }

    private void binaryOp() throws CompilerException {
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
            match("op_mult");
        }
        else if(currentToken.getToken().equals("op_div")){
            match("op_div");
        }
        else if ( currentToken.getToken().equals("op_mod")){
            match("op_mod");
        }
        else{
            throw new SyntaxErrorException(currentToken, "binary operator");
        }

    }

    private void basicExpression() throws CompilerException {
        if(Firsts.isFirst("UnaryOp", currentToken.getToken())){
            unaryOp();
            operand();
        }
        else if(Firsts.isFirst("Operand", currentToken.getToken())){
            operand();
        }
        else{
            throw new SyntaxErrorException(currentToken, "unary operator or operand");
        }
    }

    private void unaryOp() throws CompilerException {
        if(currentToken.getToken().equals("op_add")){
            match("op_add");
        }
        else if(currentToken.getToken().equals("op_sub")){
            match("op_sub");
        }
        else if(currentToken.getToken().equals("op_not")){
            match("op_not");
        }
        else{
            throw new SyntaxErrorException(currentToken, "unary operator");
        }
    }

    private void operand() throws CompilerException {
        if(Firsts.isFirst("Literal", currentToken.getToken())){
            literal();
        }
        else if(Firsts.isFirst("Access", currentToken.getToken())){
            access();
        }
        else{
            throw new SyntaxErrorException(currentToken, "literal or access");
        }
    }

    private void literal() throws CompilerException {

       if(Firsts.isFirst("PrimitiveLiteral", currentToken.getToken())){
            primitiveLiteral();
        }
        else if(currentToken.getToken().equals("rw_null")|| currentToken.getToken().equals("lit_string")){
            objectLiteral();
        }
        else {
            throw new SyntaxErrorException(currentToken, "primitive or object literal");
       }
    }

    private void primitiveLiteral() throws CompilerException {
        if(currentToken.getToken().equals("rw_true")){
            match("rw_true");
        }
        else if(currentToken.getToken().equals("rw_false")){
            match("rw_false");
        }
        else if(currentToken.getToken().equals("lit_int")){
            match("lit_int");
        }
        else if(currentToken.getToken().equals("lit_char")){
            match("lit_char");
        }
        else{
            throw new SyntaxErrorException(currentToken,"primitive literal");
        }
    }

    private void objectLiteral() throws CompilerException {
        if(currentToken.getToken().equals("rw_null")){
            match("rw_null");

        }
        else if(currentToken.getToken().equals("lit_string")){
            match("lit_string");
        }
        else{
            throw new SyntaxErrorException(currentToken,"object literal");
        }

    }

    private void access() throws CompilerException {
        primary();
        chainedOp();
    }

    private void primary() throws CompilerException {
        if(Firsts.isFirst("AccessThis", currentToken.getToken())){
            accessThis();
        }
        else if(Firsts.isFirst("AccessConstructor", currentToken.getToken())){
            accessConstructor();
        }
        else if(Firsts.isFirst("PExpression", currentToken.getToken())){
            pExpression();
        }
        else if(currentToken.getToken().equals("id_met_var")){
            accessVarMethod();
        }
        else{
            throw new SyntaxErrorException(currentToken, "access to a value");
        }
    }

    private void accessThis() throws CompilerException {
        match("rw_this");
    }

    private void accessConstructor() throws CompilerException {
        match("rw_new");
        className();
        actualArgs();
    }


    private void pExpression() throws CompilerException {
        match("pm_par_open");
        try{
            expression();
        } catch (SyntaxErrorException e){
            syntaxErrors.add(e.getMessage()+"\n"+e.getLongMessage());
            recover("pm_par_close");
        }
        match("pm_par_close");
    }

    private void accessVarMethod() throws CompilerException {
        match("id_met_var");
        possibleArgs();
    }

    private void possibleArgs() throws CompilerException {
        if(Firsts.isFirst("ActualArgs", currentToken.getToken())){
            actualArgs();
        }
        else if(Follows.itFollows("ChainedOp", currentToken.getToken())){
            //No method call just variable
        } else{
            throw new SyntaxErrorException(currentToken, "actual arguments or end of access");
        }
    }

    private void actualArgs() throws CompilerException {
        match("pm_par_open");
        expressionList();
        match("pm_par_close");
    }

    private void expressionList() throws CompilerException {
        if(Firsts.isFirst("Expression", currentToken.getToken())){
            try{
                expression();
            } catch (SyntaxErrorException e){
                syntaxErrors.add(e.getMessage()+"\n"+e.getLongMessage());
                recover(new ArrayList<>(){{add("pm_par_close");add("pm_comma");}});
            }
            optionalExpList();
        }
        else if(currentToken.getToken().equals("pm_par_close")){
            //No actual parameters
        }else{
            throw new SyntaxErrorException(currentToken, "actual argument or )");
        }
    }

    private void optionalExpList() throws CompilerException {
        if(currentToken.getToken().equals("pm_comma")){
            match("pm_comma");
            try{
                expression();
            } catch (SyntaxErrorException e){
                syntaxErrors.add(e.getMessage()+"\n"+e.getLongMessage());
                recover(new ArrayList<>(){{add("pm_par_close");add("pm_comma");}});
            }
            optionalExpList();
        }
        else if(currentToken.getToken().equals("pm_par_close")){
            //No more actual parameters
        }else{
            throw new SyntaxErrorException(currentToken, "another actual argument or )");
        }
    }

    private void chainedOp() throws CompilerException {
        if(currentToken.getToken().equals("pm_period")){
            match("pm_period");
            match("id_met_var");
            possibleArgs();
            chainedOp();
        }
        else if(Follows.itFollows("ChainedOp", currentToken.getToken())){
            //No more chained operations
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

    private boolean recover(String terminal) throws LexicalErrorException {
        while (!currentToken.getToken().equals(terminal) && !currentToken.getToken().equals("EOF")){
            try {
                getNewToken();
            } catch (LexicalErrorException e) {
                syntaxErrors.add(e.getMessage()+"\n"+e.getLongMessage());
            }
        }
        return !currentToken.getToken().equals("EOF");
    }

    private boolean recover(ArrayList<String> terminals) throws LexicalErrorException {
        while (!terminals.contains(currentToken.getToken()) && !currentToken.getToken().equals("EOF")){
            try {
                getNewToken();
            } catch (LexicalErrorException e) {
                syntaxErrors.add(e.getMessage()+"\n"+e.getLongMessage());
            }
        }
        return !currentToken.getToken().equals("EOF");
    }

}
