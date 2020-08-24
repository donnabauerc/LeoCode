cd ../../project-under-test
mvn test
cat ./target/surefire-reports/*.txt > testing-api-V6.0/target/log.txt
