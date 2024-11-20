///[Error:E|7]
class G<E> {
    public boolean b() {
        return true;
    }
    public static int m1() {
        E e = null;
        return 1;
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
