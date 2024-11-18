///1&12&30&2&9&6&1&true&false&exitosamente
class Main {
    static void main() {
        numeric();
        looksWeird();
        bool();
    }

    static void numeric() {
        debugPrint(1 + 2 / 3 * 7);          //1
        System.println();
        debugPrint(3*(3+1));                //12
        System.println();
        debugPrint(25 - -5);                //30
        System.println();
        debugPrint(3 / 2 + 1);              //2
        System.println();
        debugPrint(2 * 2 / 3 * 3 + 6);      //7
        System.println();
        debugPrint((2*2)/(3*3)+6);          //6
    }

    static void looksWeird() {
        debugPrint(((((((((((1)))))))))));  //1
    }

    static void bool() {
        var s = new Object();

        var b = (3 > 2) && ((true) || (s == new Object()));
        System.printBln(b); //true

        b = (s == new Otra()) && (3 >= 3) || false;
        System.printBln(b); //false
    }
}

class Otra {}