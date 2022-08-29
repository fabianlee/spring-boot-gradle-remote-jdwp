## spring-boot-gradle-remote-jdwp

Simple Spring Boot web application that exposes a REST api, used to show how gradle built project 
can be remotely debugged via JDWP from Eclipse IDE with breakpoints and tracing into 3rd 
party source libraries.

Full blog walk-through at: https://fabianlee.org/2022/08/29/gradle-interactive-jdwp-debugging-of-bootrun-gradle-task-in-eclipse-ide/

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

git clone https://github.com/fabianlee/spring-boot-gradle-remote-jdwp.git
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

# HEAD detached is expected
git branch
```

### From Eclipse IDE, start remote debugging session

#### import our web app so that we can place breakpoints, start remote debugging 
File > Import > Existing Gradle Project
* Project Root Directory=the $BASEDIR output earlier  
* Press "Finish"

#### make 3rd party source available for step-through
File > Open Projects from File System
* import source=/tmp/java-faker-src
* Press "Finish"
  
#### create breakpoint where user fake name is picked, so we can step-into 3rd party code
Open src/main/java/org/fabianlee/springbootgradleremotejdwp/ClassroomController.java
* Find method where student is added:
   String theNewName = faker.name().firstName();
* Right-click on left hand column and select "Toggle Breakpoint" at that line.

#### create Remote Debugging session attaching to JDWP agent
Eclipse Main Menu > Run > Debug Configurations
* Right-click "Remote Java Application", select "New Configuration"

* name=spring-boot-gradle-remote-jdwp
* Press "Browse" button by project and select our imported project = springbootgradleremotejwdp
* conection type=standard socket, host=localhost, port=5005

* Select "source" tab
* Press "Add" and then use "File System Directory" type
* use "/tmp/java-faker-src"
* Press "OK"
* Press "OK"
  
* Press "Debug" to start remote debugging session

### From Eclipse IDE, prepare for step-through

* In top-right of browser, make sure you selected "debug" view (not java)
* Select Run>Resume if the app stops immediately

Then from your browser open http://localhost:8080 to get started
* F6 = step over
* F8 = resume
* F5 = step into to go deeper




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