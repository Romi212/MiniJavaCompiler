///94&exitosamente

class A {
	int a;

	void m1() {
		this.a = 94;
	}
	int get() {
		return a;
	}
}
class Init{
    static void main()
    { 
	    var a = new A();
	    a.m1();
	    debugPrint(a.get());
    }
}


