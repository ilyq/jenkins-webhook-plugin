# jenkins webhook

# create project
```
mvn archetype:generate -B -DarchetypeGroupId=io.jenkins.archetypes -DarchetypeArtifactId=hello-world-plugin -DhostOnJenkinsGitHub=true -DarchetypeVersion=1.12 -DartifactId=jenkins-webhook-plugin
```

# version
```
java: openjdk 11.0.17 2022-10-18 LTS
maven: Apache Maven 3.8.6 (84538c9988a25aec085021c365c560670ad80f63)
```

# build
```
 mvn install && mvn hpi:hpi -e
```
