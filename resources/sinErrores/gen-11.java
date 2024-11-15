///1&2&3&exitosamente

class A{
    int x;
    int y;
    int z;
    
   
      void setX(int a){
          x = a;
      }

        void setY(int a){
            y = a;
        }

         void setZ(int a){
              z = a;
         }

    void printAtt(){
        debugPrint(z);
        debugPrint(y);
        debugPrint(x);
    }

}


class Init{
    static void main()
    { 
        var a = new A();
        a.setX(3);
        a.setY(2);
        a.setZ(1);
        a.printAtt();
        
    }
}


