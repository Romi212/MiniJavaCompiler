///[SinErrores]
class Generic{
    public int attr;
}

class Hija extends Generic {
    public int getX() {
        return 24;
    }

    public void m() {
        this.attr = getX();
    }
}

class Main {
    public static void main() {}
}