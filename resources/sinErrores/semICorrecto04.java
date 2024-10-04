///[SinErrores]
// Prueba atributos y herencia
class A  {
    static int x;
    int meth(){
        int month = 5;
        switch (month) {
            case 1:
            case 2:
            case 12:{

                break;}
            case 3:
            case 4:
            case 5:{

                break;}
            case 6:
            case 7:
            case 8:{

                break;}
            case 9:
            case 10:
            case 11:


            default:{
                int x;
            }
            case 11:{

                break;}
        }
    }
}
class B extends A {
   A z;
   G<A> y = new G<>();
}

abstract class Abstract{
    int x;
    abstract void meth();
}

class Imp extends Abstract{
    void meth(){
        x = 5;
    }
}


class G<T> {
    T x;

    void meth(T x){
        this.x = x;
    }
}
class Init{
    static void main()
    { }
}




