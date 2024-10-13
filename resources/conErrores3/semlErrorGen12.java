///[Error:C|12]
//
class A<T,X> {
    public char att;
    private int met1(int att, char a2){}
    private char met2(int a){}
    T atributo;

}

class B{
    A<B,C> atributo;

}


class Init{
    static void main()
    { }
}
