///5&3&exitosamente

class B{
    int a;

    void setA(int b){
        a = b;
        debugPrint(a);
    }

}

class A{
    int x;
    int y;
    B b;
    
   
      void setX(int a){
          x = a;
          b = new B();
      }

        void setY(int a){
            y = a;
        }

    void printAtt(){
        b  = new B();
        b.setA(3);
    }

}


class Init{
    static void main()
    { 
        var a = new A();
        a.setX(3);

        a.b.setA(3);
        debugPrint(a.b.a);
        
    }
}


