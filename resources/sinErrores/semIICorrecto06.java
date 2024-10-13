// Prueba un lado izquierdo con dos encadenamiento y atributos heredados

class A {
    B a1;
    int a2;

    void m1() {
        a1.a3.a2 = 4;
        switch(a1.a3.a2){
            case 1:
                a1.a3.a2 = 5;
            case 2:
                a1.a3.a2 = 2;
            case 3:
                a1.a3.a2 = 1;
            default:
                a1.a3.a2 = 3;
        }

        if(a2 >= 3 + 7 + - 2){
            a1.a3.a2 = 2;
        }else{
            a1.a3.a2 = 1;
        }

        var cat = 2;
        int e1,e2,e3 = 3;
        while(cat < 5){
            a1.a3.a2 = 3;
            cat = cat + 1;
        }

    }
    
    
    
} 

class B extends A{
    A a3;
    
     void m1(B p1)     
    {
        a1.a3.a2 = 4;
        
    }
}


class Init{
    static void main()
    { }
}


