///[SinErrores]
// Prueba un lado izquierdo con dos encadenamiento y atributos heredados

abstract class A {
    abstract void m1();
}

class B extends A{
    int w;
    int a2;

    void m1() {
       for(a2 = 0; a2 < 10; a2 = a2 + 1)
       {
           w += 1;
       }
    }
}


class Init{
    static void main()
    {
        A ob = new B();
        ob.m1();
    }
}


