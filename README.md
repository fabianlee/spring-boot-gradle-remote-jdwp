## spring-boot-gradle-remote-jdwp

Simple Spring Boot web application that exposes REST api, used to show how gradle built project 
can be remotely debugged via JDWP from Eclipse IDE with breakpoints and tracing into 3rd 
party source libraries.

Full blog walk-through at: 

The web application listens on port 8080 and exposes the following:

* GET /api/class/user - list of all students in class
* POST /api/class/user - creates a new student in the the class
* DELETE /api/class/user - deletes a user in the class
* index.html - test harness page that allows you to invoke each of the REST API endpoints above

### Running project with gradle and JDWP debugging enabled


```
# need OpenJDK 17+
javac --version
java --version

git clone 
cd spring-boot-gradle-remote-jdwp
BASEDIR=$(realpath .)
echo "When you import the existing gradle project from Eclipse later, use this folder: $BASEDIR"

# start on port 8080, JDWP debugging enabled on port 5005
./gradlew bootRun --debug-jvm

```

### Get copy of 3rd party source for debug step-into

```
# get correct tagged version (matching our build.gradle)
cd /tmp 
git clone https://github.com/DiUS/java-faker.git java-faker-src
cd java-faker-src
git checkout javafaker-0.12

# it will say HEAD detached, which is normal
git branch

```

### From Eclipse IDE, start remote debugging session

# make 3rd party source available for step-through
File > Open Projects from File System
  import source=/tmp/java-faker-src
  Press "Finish"
  
# import our web app so that we can place breakpoints, start remote debugging 
File > Import > Existing Gradle Project
  Project Root Directory=the $BASEDIR output earlier  
  Press "Finish"
  
# create breakpoint on deleted students
Open src/main/java/org/fabianlee/springbootgradleremotejdwp/ClassroomController.java
Find method where students are deleted:
        log.debug("deleteStudent");  
Right-click on left hand column and select "Toggle Breakpoint" at that line.

# create breakpoint where user fake name is picked, so we can step-into 3rd party code
In same ClassroomController.java,
Find method where student is added:
        String theNewName = faker.name().firstName();
Right-click on left hand column and select "Toggle Breakpoint" at that line.


Eclipse Main Menu > Run > Debug Configurations
Right-click "Remote Java Application", select "New Configuration"
   
  








### Project initially created using start.spring.io

```
id=spring-boot-gradle-remote-jdwp
artifact_id="${id//-}"
SpringAppClassName=SpringMain
version="0.0.1-SNAPSHOT"
groupId="org.fabianlee"

curl https://start.spring.io/starter.tgz \
    -d type=gradle-project \
    -d dependencies=web,devtools \
    -d javaVersion=17 \
    -d bootVersion=2.7.3 \
    -d groupId=$groupId \
    -d artifactId=$artifact_id \
    -d name=$SpringAppClassName \
    -d baseDir=$id \
    -d version=$version \
    -o ${artifact_id}.tgz

tar xf ${artifact_id}.tgz -C ../.
rm ${artifact_id}.tgz

```