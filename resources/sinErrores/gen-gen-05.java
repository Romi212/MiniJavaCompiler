///hola&exitosamente

class Padre<Y>{
    Y x;


}

class Generic<X> extends Padre<X>{

    public void setX(X x){
        this.x = x;
    }



}


class Init{
    static void main()
    {
        var g = new Generic<String>();
        g.setX("hola");
        var a = g.x;
        System.printSln(g.x);
        System.printSln(a);

        var s = "chau";
        System.printSln(s);
        s = a;
        System.printSln(s);
    }
}