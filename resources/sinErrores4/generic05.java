///[SinErrores]

class A<T,X>{
    T a;
    T f() {}
    void h(T p1) {}
}

class B{
    int f() {}
    A<String,B> a;
    void h(A<String,B> p1) {
        a = new A<String,B>();
        if(p1.f() == "hola"){
            p1.h("hola");
        }
    }
}
class Init{
    static void main()
    { }
}
