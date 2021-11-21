# Team Beryllium Android Style Guide
------
v1.0
Style guide from CMPT276 styleguide found at https://opencoursehub.cs.sfu.ca/bfraser/grav-cms/cmpt276/course-info/styleguide
Android Naming Conventions and Class content ordering overridden and replaced with content taken from https://github.com/ribot/android-guidelines/blob/master/project_and_code_guidelines.md

## Use Meaningful Names

- All identifiers must have meaningful, human-readable, English names. Avoid cryptic abbreviations such as dspl(), cntStd(), or stdRegYYYYMMDD. Instead use:
```
void display();
int countStudents();
Date dateStudentRegistered;
```

Exception 1: loop variables may be i, j or k. However, prefer the for-each loop when possible.
```
for (int i = 0; i < 10; i++) {
 ...
}
```

Exception 2: variables with very limited scope (<20 lines) may be shortened if the purpose of the variable is clear.
```
void swapCars(Person person1, Person person2) 
{
 Car tmp = person1.getCar();
 person1.setCar(person2.getCar());
 person2.setCar(tmp);
}
```

## Naming Conventions
- Constants must be all upper case, with multiple words separated by '_':
`final int DAYS_PER_WEEK = 7;`
- Functions use CamelCase. Functions should be named in terms of an action:
`double calculateTax();
boolean verifyInput();`
- Class names must start with an uppercase letter, and use CamelCase. Classes should be named in terms of a singular noun:
`class Student;
class VeryLargeCar;`
- Constant should have the most restrictive scope possible. 
  Ex. if it is used in only one class, then define the constant as private to that class. If a constant is needed in multiple classes, make it public.
- Generally favour local variables over "more global" variables such as class fields. In almost all languages, global variables are terrible!
- Do not use prefixes for variables: don't encode the type (like iSomeNumber, or strName), do not prefix member variables of a class have with m_.
- Boolean variables should be named so that they make sense in an if statement:

```
if (isOpen) {
 ...
}
while (!isEndOfFile && hasMoreData()) {
 ...
}
```
 
- Use named constants instead of literal numbers (magic numbers). It is often acceptable to use 0 and 1; however, it must be clear what they mean:
```
// OK:
int i = 0;
i = i + 1;`

// Bad: What are 0 and 1 for?!?
someFunction(x, 0, 1);
```

### Android Naming Conventions
- keys for SharedPreferences, Bundles, and Intents must be static final 
- keys are prefixed with the following
	- `SharedPreferences` -> `PREF_`
	- `Bundle` -> `BUNDLE_`
	- `Fragment Agument` -> `ARGUMENT_`
	- `Intent Extra` -> `EXTRA_`
	- `Intent Action` -> `ACTION_`

- Android resources IDs are written in lowercase_underscore (snake case)
- IDs are prefixed with the name of their element 
	- `Textview` -> `text_`
	- `ImageView` -> `image_`
	- `EditText` -> `field_`
	- `Button` -> `button_`
	- `ListView` -> `list_`
	
- Strings are prefixed with the element they are associated with (see previous)
- String IDs should describe where the string belongs and should not be the same name as its value.
  

## Indentation and Braces {...}
- Tab size is 4; indentation size is 4. Use tabs to indent code.
```
if (j < 10) {
→ → counter = getStudentCount(lowIndex,
→ → → → highIndex);
→ → if (x == 0) {
→ → → → if (y != 0) {
→ → → → → → x = y;→ → → → // Insightful comment here
→ → → → }
→ → }
}
```

- Opening brace is at the end of the enclosing statement; closing brace is on its own line, lined up with the start of the opening enclosing statement.
- Statements inside the block are indented one tab.

```
for (int i = 0; i < 10; i++) {
→ → ...
}
while (i > 0) {
→ → ...
}
do {
→ → ...
} while (x > 1);
if (y > 500) {
→ → ...
} else if (y == 0) {
→ → ...
} else {
→ → ...
}
```

- Exception: if statements with multi-line conditions have the starting brace aligned on the left to make it easier to spot the block in the if statement.

```
if (someBigBooleanExpression
→ → && !someOtherExpression)
{
→ → ...
}
```

- All if statements and loops should include braces around their statements, even if there is only one statement in the body:

```
if (a < 1) {
 a = 1;
} else {
 a *= 2;
}
while (count > 0) {
 count--;
}
```

## Statements and Spacing
- Declare each variable in its own definition, rather than together (int i, j).
```
int *p1;
int p2;
Each statement should be on its own line:
// Good:
i = j + k;
l = m * 2;
```

- All binary (2 argument) operators (arithmetic, bitwise and assignment) must be surrounded by one space.
- Avoid terneary operations.
- Commas must have one space after them and none before. Unary operators (!, *, &, - (ex: -1), + (ex: +1), ++, --) have no additional space on either side of the operator.

```
i = 2 + (j * 2) + -1 + k++;
if (i == 0 || j < 0 || !k) {
 arr[i] = i;
}
myObj.someFunction(i, j + 1);
```

- Add extra brackets in complex expressions, even if operator precedence will do what you want. The extra brackets increase readability and reduce errors.

```
if ((!isReady && isBooting)
 || (x > 10)
 || (y == 0 && z < (x + 1)))
{
 ...
}
```

- However, it is often better to simplify complex expressions by breaking them into multiple sub-expressions that are easier to understand and maintain:
```
boolean isFinishedBooting = (isReady || !isBooting);
boolean hasTimedOut = (x > 10);
boolean isOldFirmware = (y == 0 && z < (x + 1));
if (!isFinishedBooting
 || hasTimedOut
 || isOldFirmware) 
{
 ... 
}
```

## Classes
 - use the following order:
1.  Constants
2.  Fields
3.  Constructors
4.  Override methods and callbacks (public or private)
5.  Public methods
6.  Private methods
7.  Inner classes or interfaces


- If your class is extending an **Android component** such as an Activity or a Fragment, it is a good practice to order the override methods so that they **match the component's lifecycle**. 
  Ex. if you have an Activity that implements `onCreate()`, `onDestroy()`, `onPause()` and `onResume()`, then the correct order is:
1. onCreate
2. onResume
3. onPause
4. onDestroy


## Comments

- Comments which are on one line should use the // style. Comments which are or a couple lines may use either the //, or /* ... */ style.
- Comments which are many lines long should use /* ... */.
- Each class must have a descriptive comment before it describing the general purpose of the class. These comments should be in the JavaDoc format.  
Recommended format is shown below:
```
/**
 * Student class models the information about a 
 * university student. Data includes student number, 
 * name, and address. It supports reading in from a 
 * file, and writing out to a file.
 */
class Student {
 ...
}
```

- Comments should almost always be the line before what they describe, and be placed at the same level of indentation as the code. 
- Only very short comments may appear inline with the code:

```
// Display final confirmation message box.
callSomeFunction(
 0,→ → → → → → → // Parent.
 "My App", → → → // Title
 "test");→ → → → // Message
```
## Comments vs Functions

- Your code should not need many comments. Generally, before writing a comment, consider how you can refactor your code to remove the need to "freshen" it up with a comment.

When you do write a comment, it must describe why something is done, not what it does:
```
// Cast to char to avoid runtime error for 

// international characters when running on Windows 

if (isAlpha((char)someLetter)) {

 ...

}
```

## Other

- either post-increment or pre-increment may be used on its own:
`i++;
++j;`

- Avoid using goto. When clear, design your loops to not require the use of break or continue.

- All switch statements should include a default label. If the default case seems impossible, place an assert false; in it. 
- Comment any intentional fall-throughs in switch statements:

```
switch(buttonChoice) {
case YES:
 // Fall through
case OK:
 System.out.println("It's all good.");
 break;
case CANCEL:
 System.out.println("It's over!");
 break;
default:
 assert false;
}
```

- Use plenty of assertions. Any time you can use an assertion to check that some condition is true which "must" be true, you can catch a bug early in development. 
- It is especially useful to verify function pre-conditions for input arguments or the object's state. 
  	Note that you must give the JVM the -ea argument (enable assertions) for it to correctly give an error message when an assertion fails.

- Never let an assert have a side effect such as assert i++ > 1;. This may do what you expect during debugging, but when you build a release version, the asserts are removed. 


