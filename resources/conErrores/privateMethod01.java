//[Error:m1|17]

class A {

     private void m1(){

        
     }

     void m2(){}
    
}    


class B extends A{
    void m2(int a){
        m1();
    }
}

class Init{
    static void main()
    { }
}


