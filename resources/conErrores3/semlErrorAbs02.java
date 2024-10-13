///[Error:private|7]
//
abstract class A {
    public char att;
    private int met1(int att, char a2){}
    private char met2(int a){}
    private abstract char absMet();

}

class C extends A{
    private int met1(int att, char a2){}
    private char absMet(){}

}

class Init{
    static void main()
    { }
}
