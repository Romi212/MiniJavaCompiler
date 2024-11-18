///exitosamente
class Base{}
class Gen1<T>{
    T a;
    T dar(){ return a; } }
class Prueba extends Gen1<Base> {
    Base atr;

    void m1() {
        atr = a;
    }
}
    class Init{
    static void main()
    {


    }
}