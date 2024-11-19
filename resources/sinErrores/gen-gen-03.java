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
        System.printSln(g.getX());
    }
}