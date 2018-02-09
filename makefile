JFLAGS = -g

JC = javac

RM = rm

JVM = java

FILE=

.SUFFIXES: .java .class

.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
        World.java \
        Tim.java \
        Driver.java 

MAIN = Driver
default: classes
classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class *~

run: Driver.java
	$(JVM) $(MAIN)

default: Driver.java
	@java Driver