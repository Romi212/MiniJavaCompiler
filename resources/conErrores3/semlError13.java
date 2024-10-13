///[Error:met2|12]
//
class A {
    public char att;
    private int met1(int att, char a2){}
    private char met2(int a){}

}

class C extends A{
    private int met1(int att, char a2){}
    private char met2(char a){}

}

class Init{
    static void main()
    { }
}
