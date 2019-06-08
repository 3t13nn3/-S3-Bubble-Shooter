JAVAC=javac
Exe = BubbleShooter
sources = $(wildcard *.java)
classes = $(sources:.java=.class)

all: $(Exe)

$(Exe): $(classes)

%.class: %.java
	$(JAVAC) $<

jar:    
	$(JAVAC) $(sources)
	@echo "Class-Path: ." >> MANIFEST.MF
	@echo "Main-Class: Game" >> MANIFEST.MF
	jar -cvmf MANIFEST.MF $(Exe).jar $(classes)
	rm -f MANIFEST.MF
	rm -f *.class

clean:
	rm -f *.class
	rm -f $(Exe).jar