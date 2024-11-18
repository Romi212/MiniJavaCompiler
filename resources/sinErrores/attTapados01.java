///a&exitosamente


class B extends A{
    char x;

    public B(char a){
        x = a;
        System.printCln(x);
        setX();
    }

    public void printX(){
        System.printCln(x);
    }

}
class A {
    int x;

    public A(int y) {

    }

    public void setX(){
        x = 89;
        System.printIln(x);
    }
}

class Init{
    static void main() {
        var b = new B('a');
        b.printX();
    }
}