///[Error:E|6]
class G<E> {
    public boolean b() {
        return true;
    }
    public static E m1() {

        return null;
    }

}

class A {
    public void m() {

    }
}

class Main {
    public static void main() {
        int i = G.m1();
    }
}
