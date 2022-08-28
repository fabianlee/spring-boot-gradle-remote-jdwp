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

#unzip $id.zip
#cd $id
#chmod +x ./gradlew
