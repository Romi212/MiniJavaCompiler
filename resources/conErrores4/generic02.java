///[Error:A|12]

class A<T>{
    T a;
    T f() {}
    void h(T p1) {}
}

class B{
    int f() {}
    void h(int p1) {
        A<String,B> a = new A();
        if(a.f() == "hola"){
            a.h("hola");
        }
        A<B> b = new A();
        if(b.f() == "hola"){
            b.h("hola");
        }
    }
}
class Init{
    static void main()
    { }
}
