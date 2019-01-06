cd /Users/anuragnarayan/eclipse-workspace/SocialNetworks/
echo "Running SocialNetworkGraphTest..."
java -cp \
.:/Users/anuragnarayan/Downloads/software_downloads/Eclipse.app/Contents/Eclipse/plugins/org.junit_4.12.0.v201504281640/junit.jar\
:/Users/anuragnarayan/.vscode/extensions/redhat.java-0.36.0/server/plugins/org.hamcrest.core_1.3.0.v20180420-1519.jar\
:/Users/anuragnarayan/eclipse-workspace/SocialNetworks/bin\
:/Users/anuragnarayan/Downloads/apache-log4j-2.11.1-bin/log4j-api-2.11.1.jar\
:/Users/anuragnarayan/Downloads/apache-log4j-2.11.1-bin/log4j-core-2.11.1.jar\
:/Applications/Eclipse.app/Contents/Eclipse/plugins/org.junit.jupiter.api_5.0.0.v20170910-2246.jar \
org.junit.runner.JUnitCore test.SocialNetworkGraphTest

echo "Running CapGraphTest..."
java -cp \
.:/Users/anuragnarayan/Downloads/software_downloads/Eclipse.app/Contents/Eclipse/plugins/org.junit_4.12.0.v201504281640/junit.jar\
:/Users/anuragnarayan/.vscode/extensions/redhat.java-0.36.0/server/plugins/org.hamcrest.core_1.3.0.v20180420-1519.jar\
:/Users/anuragnarayan/eclipse-workspace/SocialNetworks/bin\
:/Users/anuragnarayan/Downloads/apache-log4j-2.11.1-bin/log4j-api-2.11.1.jar\
:/Users/anuragnarayan/Downloads/apache-log4j-2.11.1-bin/log4j-core-2.11.1.jar\
:/Applications/Eclipse.app/Contents/Eclipse/plugins/org.junit.jupiter.api_5.0.0.v20170910-2246.jar \
org.junit.runner.JUnitCore test.CapGraphTest
