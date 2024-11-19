///[SinErrores]

class GenericClass<T> {

}


class GenericImplementation<T>  {
    T parametricTypeReturnMethod() {}
    void parametricTypeParameter(T e) {}
    GenericClass<T> parametizedClassReturnType() {}

}

class GenericDaughterClass<T> extends GenericClass<T> {
    //MIEMBROS DE TIPO "TIPO PARAMETRICO"
    T t;

    T getT() {
        return t;
    }

    void setT(T t) {
        this.t = t;
    }
    //-------------------------------------
    //MIEMBROS DE TIPO "TIPO PARAMETRIZADO CLASE"
    GenericClass<T> gc;

    GenericClass<T> getGC() {
        return gc;
    }

    void setGC(GenericClass<T> gc) {
        this.gc = gc;
    }
    //-------------------------------------
    //MIEMBROS DE TIPO "TIPO PARAMETRIZADO INTERFAZ"



}

class Main {
    public static void main() {}
}