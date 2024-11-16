///5&3&exitosamente

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
        var x = 5;
        debugPrint(x);
        debugPrint(this.x);
        this.setY(8);
    }

}


class Init{
    static void main()
    { 
        var a = new A();
        a.setX(3);
        a.printAtt();
        
    }
}


