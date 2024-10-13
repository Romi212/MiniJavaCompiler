///[Error:A|12]
//
class A<T,X> {
    public char att;
    private int met1(int att, char a2){}
    private char met2(int a){}
    T atributo;
    private int met3(T p1){}

}

class B extends A<Integer>{
    public char att;
    private int met1(int att, char a2){}
    private char met2(int a){}
    int atributo;
    private int met3(int p1){}

}


class Init{
    static void main()
    { }
}
