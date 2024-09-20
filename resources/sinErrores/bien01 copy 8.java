///[SinErrores]

abstract class Clase1<P3>{

public abstract abMet();
public abstract abMet2(int a, char b);
private abstract abMetPriv();
private abstract abMetPriv2(Clase3 a);
abstract abMet5();
abstract abMet4(char a, int c, String s);

public static void metodoStatic(){}
public static void metodoStatic(int a, Clase4 c){}
private static int metodoStatic(int b, char s){}
private static Clase4 metidiStaticPriv(){}
static char metodoS(int a){}
static void metodoS2(){}

public Clase1(){}
public Clase1(int a, char b){}
public Clase1 metodo(){}
public Clase1 metodo2(int a){}
private Clase1(String ctor){}
private Clase4 metodoPriv(){}
Clase1(){}
Clase1 metodo(int v1){}

public void metodoStatic(){}
public  void metodoStatic(int a, Clase4 c){}
private  int metodoStatic(int b, char s){}
private  Clase4<String> metidiStaticPriv(){}
char metodoS(int a){}
void metodoS2(){}

void atributoVOid;
private int atributo;
public Clase1 atributo;
Clase4 atributo1;
public char charA;

static private int atributo;
static public Clase1 atributo;
static Clase4 atributo1;
static public char charA;

private int atributo = 3+5;
public Clase1 atributo = new Clase1<String>(3,'a');
Clase4 atributo1 = new Clase4();
public char charA = metodoS(atributo);


    
}

abstract class Clase2 extends Clase1<P>{
    
}

abstract class Clase3 extends Clase2{

}

class Clase4{

}

class Clase5 extends Clase4{
    
}