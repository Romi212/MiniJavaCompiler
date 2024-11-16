///2&1&3&exitosamente

class A{
    int x;
    
   
      static void mc(int p1, int p2, int p3){
        debugPrint(p2);
        debugPrint(p1);
        debugPrint(p3);

      }


}


class Init{
    static void main()
    { 
        //var a = new A();
        A.mc(1,2,3);
        
    }
}


