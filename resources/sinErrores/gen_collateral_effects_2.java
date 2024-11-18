///true&exitosamente
class C {
    int x;
    public C() {
        x = 0;
    }
    int xCollateral() {
        x = x + 1;
        return x;
    }
}

class Main {
    static void main() {
        var c = new C();

        var x1 = true || (c.xCollateral() < 1);

        System.printBln(x1);
    }
}