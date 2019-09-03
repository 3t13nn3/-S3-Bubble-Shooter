javac ..\*.java
echo Main-Class: Game >..\MANIFEST.MF
jar -cvmf ..\MANIFEST.MF ..\BubbleShooter.jar ..\*.class
del ..\MANIFEST.MF *.class