///0&1&1&exitosamente
class C {
    int x;
    public C() {
        x = 0;
    }
    int xCollateral() {
        x = x + 1;
        return x;
    }
    int getX() {
        return x;
    }
}

class Main {
    static void main() {
        var c = new C();

        var x1 = c.getX();
        var x2 = c.xCollateral();
        var x3 = c.getX();

        debugPrint(x1);
        debugPrint(x2);
        debugPrint(x3);
    }
}