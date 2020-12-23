# Customize light-codegen for generating Gradle/Maven project.

## How to build light-codegen.
git clone https://github.com/networknt/light-codegen.git

cd light-codegen/

mvn clean install -DskipTests

## How to generate project.
For Maven

java -jar path/to/**codegen-cli.jar** -f openapi -o light-example-4j/rest/openapi/petstore-maven -m model-config/rest/openapi/petstore/1.0.0/openapi.yaml -c model-config/rest/openapi/petstore/1.0.0/config.json -b maven

For Gradle

java -jar path/to/**codegen-cli.jar** -f openapi -o light-example-4j/rest/openapi/petstore-maven -m model-config/rest/openapi/petstore/1.0.0/openapi.yaml -c model-config/rest/openapi/petstore/1.0.0/config.json -b gradle

-f : Framework

-o : Project Output directory

-m : Model directory

-c : Config directory

-b : Build tool (maven/gradle)

## Build and run project

For Maven:
**mvn clean install -Prelease**

For Gradle:
**./gradlew clean build**

Run **java -jar path/to/<file_name>.jar**