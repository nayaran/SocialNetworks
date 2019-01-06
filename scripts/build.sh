echo "Accumulating all java files in sources.txt..."
find /Users/anuragnarayan/eclipse-workspace/SocialNetworks/ -name "*.java" | grep -vi grader > /Users/anuragnarayan/eclipse-workspace/SocialNetworks/scripts/sources.txt

echo "Compiling all java files listed in sources.txt to bin/"
javac -cp \
.:/Applications/Eclipse.app/Contents/Eclipse/plugins/*\
:/Users/anuragnarayan/Downloads/apache-log4j-2.11.1-bin/*\
:/Applications/Eclipse.app/Contents/Eclipse/plugins/org.junit_4.12.0.v201504281640/junit.jar \
 -d /Users/anuragnarayan/eclipse-workspace/SocialNetworks/bin @/Users/anuragnarayan/eclipse-workspace/SocialNetworks/scripts/sources.txt
