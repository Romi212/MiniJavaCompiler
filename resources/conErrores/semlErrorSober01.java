///[Error:met1|20]
// Prueba constructores

class A {
    public A(){}
    public int att;
}
class B extends A {

   public B(int w){}
    public char att;
}

class C extends B{
    public int met1(int a){}
    public int met1(char a, char b){}
}

class D extends C {
    public int met1(char a){}
    public boolean att;
}




class Init{
    static void main()
    { }
}



