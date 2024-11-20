///[Error:G|13]
class G<E> {

    public int m() {
        return 0;
    }
}


class A {

    public int m() {
        return new G<>().m();
    }

}



class Main {
    public static void main() {}
}