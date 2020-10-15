package testowe;

// Outer class should be public and have the same name as the file (without .java and preferably in camel case)
// Every java app must contain at least one class definition
// Meanings:
//   -public - method can be called from anywhere (even outside the program)
//   -private - prevents outsite entities from accessing this resource, it might be accessible via e.g. public
//              getter functions
//   -static - it exists and can be run by itself (without creating an object)
//   -void - it returns no value
public class Hello {
    // It accepts an array of strings passed as arguments, it's typically called args
    public static void main(String[] args) {
        // System is a class from java.lang package
        System.out.println("testowe.Hello, world!");
    }
}

