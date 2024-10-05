///[Error:B|11]
// Prueba constructores

class A {
    public A(){}
    public int att;
}
class B extends A {

    public B(int w){}
    public B(char v){}
    public char att;
}

class C extends B{
    public int met1(int a){}
    public int met1(char a, char b){}
}

class D extends C {

    public boolean att;
}




class Init{
    static void main()
    { }
}




