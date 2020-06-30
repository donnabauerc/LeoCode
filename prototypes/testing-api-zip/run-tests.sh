PROJECT_NAME=$(ls ../project-under-test)

cd ../project-under-test/code-examples
# Run tests and generate .xml reports
mvn test

# Convert .xml reports into .html report, but without the CSS or images
mvn surefire-report:report-only

# Put the CSS and images where they need to be without the rest of the
# time-consuming stuff
mvn site -DgenerateReports=false

cd ..

mv ./$PROJECT_NAME/target/site ../target/

cat ./$PROJECT_NAME/target/surefire-reports/*.txt > ../target/log.txt

rm -rf ./$PROJECT_NAME
