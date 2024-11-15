///2&3&1&exitosamente

class A{
    int x;
    
   
      void mc(){
        debugPrint(1);

      }

      void m2(){
          debugPrint(2);
      }

      void m3(){
          debugPrint(3);
      }
}


class Init{
    static void main()
    { 
        var a = new A();
        a.m2();
        a.mc();
        a.m3();
        
    }
}


