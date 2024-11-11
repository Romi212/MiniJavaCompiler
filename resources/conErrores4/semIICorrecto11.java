///[Error:A|23]
// Prueba un lado izquierdo con dos encadenamiento y atributos heredados

abstract class A {
    abstract void m1();
}

class B extends A{
    int w;
    int a2;

    void m1() {
        if(a2 > 5){
            w += 5;
        }
    }
}


class Init{
    static void main()
    {
        A ob = new A();
        ob.m1();
    }
}


