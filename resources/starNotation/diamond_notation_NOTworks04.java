///[Error:G|22]
class G<E> {

    public int m() {
        return 0;
    }
}


class A {

    public static void m(int x) {

    }

}



class Main {
    public static void main() {
        A.m(new G<>().m());
    }
}