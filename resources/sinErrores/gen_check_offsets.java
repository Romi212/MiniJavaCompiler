///1&2&3&exitosamente
class A {
    static int atributo;

    static void main() {
        m(10);
    }

    static void m(int parametro) {
        var local = 1;
        parametro = 2;
        atributo = 3;

        debugPrint(local);
        debugPrint(parametro);
        debugPrint(atributo);
    }
}