///[Error:a1|30]
// Nombre de variable local o parametro repetido x - ln: 17
class A {
    private int a1;
    
     void m1(int p1)
    {
         var x = 1; 
       
        {
            {
                var y = 2;
            }
            var y = 3;

           }

       
    }
    
 
  
}

class B{
    A x;

    void m1()
    {
       int ent = x.a1;
    }

}




class Init{
    static void main()
    { }
}


