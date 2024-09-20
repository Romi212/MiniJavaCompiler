///[SinErrores]

class Ejemplo{

    int metodo(){
        for(int i = 0; i<5; i+=1){
            x = x + 1;
        }

        for(x = 7; x<10; x+=1){
            x = x + 1;
        }

        for(x+1; x - 2; x==3){
            x = x + 1;
        }

        for(int i : arreglo){
            x = x + 1;
            for(int j = 0; j<5; j+=1){
                x = x + 1;
            }
        }
    }
}