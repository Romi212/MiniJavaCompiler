///[SinErrores]

class A<T,X>{
    T a;
    T f() {}
    void h(T p1) {}
}

class B{
    int f() {}
    void h(int p1) {
        var a = new A<String,B>();
        if(a.f() == "hola"){
            a.h("hola");
        }
    }
}
class Init{
    static void main()
    { }
}
