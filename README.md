# Logic in the *Middle*
Code for presentation on the logic in the middle pattern
This is a architectural/design pattern to refactor untestable classes into testable classes mainly using inheritance.
The point is to create an empty class that the untestable class inherits from. Then move all conditional logic to the abstract class in the middle, but not the untestable type. Leave those in the original top class. Eventually the top class will just hold untestable types and implement abstractions created be the logic classes
Anyone is welcome to read and learn the pattern (read each git branch in order), but this is not open source software. All rights reserved.
