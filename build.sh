#!/bin/bash

# install openjdk & gcc

apt-get update -y
apt-get install build-essential software-properties-common -y
apt-get install -y wget
apt-get install openjdk-8-jdk -y
apt-get clean

# install maven

wget --no-verbose -O /tmp/apache-maven-3.5.0-bin.tar.gz http://www-us.apache.org/dist/maven/maven-3/3.5.0/binaries/apache-maven-3.5.0-bin.tar.gz
echo "35c39251d2af99b6624d40d801f6ff02 /tmp/apache-maven-3.5.0-bin.tar.gz" | md5sum -c
tar xzf /tmp/apache-maven-3.5.0-bin.tar.gz -C /opt/
ln -s /opt/apache-maven-3.5.0 /opt/maven
ln -s /opt/maven/bin/mvn /usr/local/bin
rm -f /tmp/apache-maven-3.5.0-bin.tar.gz

mvn clean

mvn package -DskipTests

javah -jni -force -classpath target/classes -d src/main/c io.webfolder.otmpfile.TempFile

mkdir -p src/main/resources/META-INF

gcc -fPIC -shared src/main/c/io_webfolder_otmpfile_TempFile.c -I$JAVA_HOME/include -I$JAVA_HOME/include/linux -o src/main/resources/META-INF/libotmpfile.so

strip src/main/resources/META-INF/libotmpfile.so

mvn test

mvn package
