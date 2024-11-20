///[SinErrores]
class G<E> {

    public E x;
    public void setX(E e){
        x = e;
    }
}

class A {


    public void m(){
        System.printSln("A");
    }

}

class B extends A{
    public void m(){
        System.printSln("B");
    }

}

class Main {
    public static void main() {
        G<A> g = new G<>();
        g.setX(new B());
        g.x.m();

    }
}
