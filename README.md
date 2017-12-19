# Import-PokemonGo-Data

I have used whale cluster to implement the Home Work. Follow the below instructions to get the output 

Step1- Create a directory named HW2. Go into this directory

Step2- Create a file named build.xml and a directory named parsePokemon that will have your source code with .java extension
build.xml file should have the contents present in build.xml
parsePokemon directory should have the following files

-DataOSM.txt  
-HoustonPokemonGO.txt  
-OSMP.java    
-Pokemon.java  
-SamplePoke.txt  
-testrun

Write the below command in command line to get the OpenStreetMap data.
	wget http://overpass-api.de/api/map?bbox=-95.4437,29.7238,-95.2707,29.8263

rename the downloaded file with below command
	mv map?bbox=-95.8365,29.5543,-94.9493,30.0358 DataOSM.txt
 
Write the below command in command line to get the OpenStreetMap data.
	wget http://i2c.cs.uh.edu/class/fall2016-cloud/tiki-download_wiki_attachment.php?attId=172&download=y
rename the downloaded file with below command
	mv Houston_Pokémon_GO_Map_Pokéstops_and_Gyms.kml HoustonPokemonGO.txt

Also go into the HoustonPokemonGO.txt file and make one change ie
change Pokéstops to Pokestops and save the file.

Step3- We need to compile the java source code. Go back one directory where the build.xml is present and write the below command
cloudc09@whale:~/HW2> ant build

You will find the output as below:

Buildfile: /home2/cosc6376/cloudc09/HW2/build.xml

init:

compile:
    [javac] Compiling 2 source files to /home2/cosc6376/cloudc09/HW2/build/classes
    [javac] Note: Some input files use or override a deprecated API.
    [javac] Note: Recompile with -Xlint:deprecation for details.

build:
      [jar] Building jar: /home2/cosc6376/cloudc09/HW2/parsePokemon/Pokemon.jar

BUILD SUCCESSFUL
Total time: 3 seconds


Step4- Go in the parsePokemon directory to check whether the Pokemon.jar file is created.

Step5- Create a testrun file in the parsePokemon directory it should have the content of testrun.txt

Step6- Make this file executable by using the below command

cloudc09@whale:~/HW2/parsePokemon> chmod +x testrun

Step7- Execute the below Statement

cloudc09@whale:~/HW2/parsePokemon> ./testrun

Step8- Sit back and wait for the until the tables are created and rows inserted by the job.
Two tables are created "PokemonDD09" and "OpenStreetMapDD09". OSM data may take around 1.5 hrs of time to execute. Please bare till it executes completely.

Step9- Type "hbase shell" in command line as shown below:

cloudc09@whale:~> hbase shell
SLF4J: Class path contains multiple SLF4J bindings.
SLF4J: Found binding in [jar:file:/opt/hbase/1.1.5/lib/slf4j-log4j12-1.7.5.jar!/                                                                                                             org/slf4j/impl/StaticLoggerBinder.class]
SLF4J: Found binding in [jar:file:/opt/hadoop/2.7.2/share/hadoop/common/lib/slf4                                                                                                             j-log4j12-1.7.10.jar!/org/slf4j/impl/StaticLoggerBinder.class]
SLF4J: See http://www.slf4j.org/codes.html#multiple_bindings for an explanation.
SLF4J: Actual binding is of type [org.slf4j.impl.Log4jLoggerFactory]
HBase Shell; enter 'help<RETURN>' for list of supported commands.
Type "exit<RETURN>" to leave the HBase Shell
Version 1.1.5, r239b80456118175b340b2e562a5568b5c744252e, Sun May  8 20:29:26 PD                                                                                                             T 2016

hbase(main):001:0> 

Step10- Then type scan 'tablename' as below:

hbase(main):131:0> scan 'PokemonDD09'

repeat this for next table

Step11- You ll see the output as in the word document. 

Step12- Type command "hive"




Query 1

Select * from OpenStreetMapDD09 where Amenity='restaurant'

calculate the distance between the latitude and longitude we got from above points with the given university of houston co-ordinates. The ones with above 5 miles are not the restaurants that we want.

Query 2

Select Count(Type) from PokemonDD09 where Type='Pokestop' and Placemark like '%university%'