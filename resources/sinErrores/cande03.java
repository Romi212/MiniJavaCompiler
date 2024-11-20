///exitosamente
class A {
    static int m1() {
        return 1;
    }
}

class Main {
    static void main() {
        var a = new A();

        A.m1();
    }
}