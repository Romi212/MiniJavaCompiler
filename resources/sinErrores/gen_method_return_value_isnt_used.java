///exitosamente
class A {
    static int m1() {
        return 1;
    }

    char m2() {
        return 'c';
    }
}

class B {
    A a;
    public B() {
        a = new A();
    }
}

class Main {
    static void main() {
        var b = new B();
        var a = new A();

        //A.m1();
        a.m1();
        a.m2();

        //b.a.m1();
        //b.a.m2();
    }
}