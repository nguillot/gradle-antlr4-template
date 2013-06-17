A Gradle build template for ANTLR4 projects.  Just type "gradlew
build" to compile the grammar files and begin using in your src/main
and src/test source roots.

This work was inspired by

http://fenrock.wordpress.com/2012/05/17/gradle-and-antlr-3-x/

Remove the 

	apply from: file('gradle/idea.gradle')

if you are not using IntelliJ.  This is an IDE nice to have that
adds the generated sources to the IDE source roots.

The g4 grammar and t.* sample input files were taken from Terence
Parr's source code for the book The Definitive ANTLR4 Reference.

