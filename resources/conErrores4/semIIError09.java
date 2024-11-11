///[Error:w1|11]
// Nombre de variable local o parametro repetido x - ln: 17
class A {
    private int a1;
    
     void m1(int p1)
    {
         var w1 = 3;
         w1 = 4;
         m1(w1);
         w1;

       
    }
    
 
  
}

class B{
    A x;

    void m1()
    {
       ;
    }

}




class Init{
    static void main()
    { }
}


