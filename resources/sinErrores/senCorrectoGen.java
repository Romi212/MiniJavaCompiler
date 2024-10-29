///[SinErrores]

class A<T>{
    T a;
    T f() {}
    void h(T p1) {}
}
class B extends A<Base>{
    Base f() {}
    void h(Base p1) {}
}
class Init{
    static void main()
    { }
}
