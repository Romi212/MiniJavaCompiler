///hola&exitosamente


class Generic<X>{
    X x;

    public void setX(X x){
        this.x = x;
    }
    public X getX(){
        return x;
    }

}

class Init{
    static void main()
    {
        var g = new Generic<String>();
        g.setX("hola");
        System.printSln(g.x);
        //g.x = "hola";
        System.printSln(g.getX());
        var s = "chau";
        System.printSln(s);
        s = g.getX();
        System.printSln(s);

    }
}