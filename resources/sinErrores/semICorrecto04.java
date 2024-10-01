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
                System.out.println("Winter");
                break;}
            case 3:
            case 4:
            case 5:{
                System.out.println("Spring");
                break;}
            case 6:
            case 7:
            case 8:{
                System.out.println("Summer");
                break;}
            case 9:
            case 10:
            case 11:{
                System.out.println("Autumn");
                break;}
            default:{}
            case 11:{
                System.out.println("Autumn");
                break;}
        }
    }
}
class B extends A {
   A z;
}

class C<T>{
    T x;
    C<T> y;
    C<T> meth(){
        return y;
    }
}

class Init{
    static void main()
    { }
}




