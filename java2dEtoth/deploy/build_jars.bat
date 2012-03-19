del ethos.jar
del ethos_assets.jar
del goldenT.jar
del GTGE_add_ons.jar
cd ..\bin\
"C:\Program Files (x86)\Java\jdk1.6.0_30\bin\jar" cfm ethos.jar ..\deploy\Manifest.txt *
cd ..
"C:\Program Files (x86)\Java\jdk1.6.0_30\bin\jar" cf ethos_assets.jar font img sound xml
move ethos_assets.jar deploy/ethos_assets.jar
move bin\ethos.jar deploy/ethos.jar
cd ..\gtge\bin\
"C:\Program Files (x86)\Java\jdk1.6.0_30\bin\jar" cfm goldenT.jar ..\..\java2dEtoth\deploy\Manifest_gtge.txt *
move goldenT.jar ..\..\java2dEtoth\deploy\goldenT.jar
cd ..\..\java2dEtoth\deploy\
cd ..\..\gtge_addons\bin\
"C:\Program Files (x86)\Java\jdk1.6.0_30\bin\jar" cfm GTGE_add_ons.jar ..\..\java2dEtoth\deploy\Manifest_gtge.txt *
move GTGE_add_ons.jar ..\..\java2dEtoth\deploy\GTGE_add_ons.jar
cd ..\..\java2dEtoth\deploy\