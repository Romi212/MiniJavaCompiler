///[Error:G|10]
class G<E> {
    public boolean b() {
        return true;
    }
}

class A {
    public void m() {
        boolean g = new G<>().b();
    }
}

class Main {
    public static void main() {}
}
