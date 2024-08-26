///[SinErrores]
public class ExampleProgram {
    public static void main(String args) {
        int number = 10;
        char character = 'a';
        String str = "Hello, World!";
        boolean flag = true;

        if (number > 5) {
            str = "Number is greater than 5";
        } else {
            str = "Number is not greater than 5";
        }
         /* This is a
           multi-line comment */
        switch (character) {
            case 'a':
                str = "Character is 'a'";
                break;
            case 'b':
                str = "Character is 'b'";
                break;

        }
    }
}