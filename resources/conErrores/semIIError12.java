///[Error:p1|8]
// Nombre de variable local o parametro repetido x - ln: 17
class A {
    private int a1;
    
     void m1(int p1)
    {
         var p1 = 3;
         p1 = 4;
         m1(p1);


       
    }
    
 
  
}




class Init{
    static void main()
    { }
}


