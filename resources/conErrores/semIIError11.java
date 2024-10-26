///[Error:m1|15]
// Nombre de variable local o parametro repetido x - ln: 17
class A {
    private int a1;
    
     void m1(int p1)
    {
         var w1 = 3;
         w1 = 4;
         m1(w1);

    }

    static int m2(){
         m1(3);
         return 3;
    }
    
 
  
}






class Init{
    static void main()
    { }
}


