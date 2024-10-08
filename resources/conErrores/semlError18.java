///[Error:B|6]
//
class A {
    public char att;
    public A(int a){}
    public A(B att, C att2){}
    private int met1(int att, char a2){}
    private char met2(int a){}

}

class C extends A{
    private int met1(int att, char a2){}
    private A attMal;

}

class Init{
    static void main()
    { }
}