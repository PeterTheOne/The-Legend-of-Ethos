del ethos.jar
cd ..\bin\
"C:\Program Files (x86)\Java\jdk1.6.0_30\bin\jar" cfm ethos.jar ..\deploy\Manifest.txt *
cd ..
move bin\ethos.jar deploy\ethos.jar
cd deploy\