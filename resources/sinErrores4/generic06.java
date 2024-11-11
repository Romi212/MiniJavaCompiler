///[SinErrores]

class A<T,X>{
    T a;
    T f() {}
    void h(T p1) {}
}

class B{
    int f() {}
    A<String,B> m1(){}
    void h(int p1) {
        if(m1().f() == "hola"){
            m1().h("hola");
        }
    }
}
class Init{
    static void main()
    { }
}
