///[Error:==|17]

class A<T>{
    T a;
    T f() {}
    void h(T p1) {}
}

class B{
    int f() {}
    void h(int p1) {
        A<String> a = new A<String>();
        if(a.f() == "hola"){
            a.h("hola");
        }
        A<B> b = new A<B>();
        if(b.f() == "hola"){
            b.h("hola");
        }
    }
}
class Init{
    static void main()
    { }
}
