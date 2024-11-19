///hola&exitosamente


class Generic<X>{
    X x;

}


class Init{
    static void main()
    {
        var g = new Generic<String>();
        g.x = "hola";
        System.printSln(g.x);
    }
}