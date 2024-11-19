///[SinErrores]
class Generic<X>{
    public X attr;
}

class Hija extends Generic<String> {
    public String getX() {
        return "hia";
    }

    public void m() {
        this.attr = getX();
    }
}

class Main {
    public static void main() {}
}