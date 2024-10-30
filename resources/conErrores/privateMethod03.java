//[Error:att1|17]

class A {
     private int att1;
     private void m1(){

     }

     void m2(){}
    
}    


class B{
    A att;
    void m2(int a){
        att.att1 = 3;
    }
}

class Init{
    static void main()
    { }
}


