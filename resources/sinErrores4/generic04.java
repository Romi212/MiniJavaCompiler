///[SinErrores]

class A<T,X>{
    T a;
    T f() {}
    void h(T p1) {}
}

class B{
    int f() {}
    A<String,B> a;
    void h(int p1) {
        a = new A<String,B>();
        if(a.f() == "hola"){
            a.h("hola");
        }
    }
}
class Init{
    static void main()
    { }
}
