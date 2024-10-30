//[Error:m1|17]

class A {

     private void m1(){

     }

     void m2(){}
    
}    


class B{
    A att;
    void m2(int a){
        att.m1();
    }
}

class Init{
    static void main()
    { }
}


