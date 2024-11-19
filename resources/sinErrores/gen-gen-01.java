///hola&10&10&exitosamente


class Generic<X>{
    X x;

}

class Base{
    int x;
}


class Init{
    static void main()
    {
        var g = new Generic<String>();
        g.x = "hola";
        System.printSln(g.x);

        var z = new Base();
        z.x = 10;
        System.printIln(z.x);
        var d = new Generic<Base>();
        d.x = z;
        System.printIln(d.x.x);
    }
}