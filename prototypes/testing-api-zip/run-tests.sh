PROJECT_NAME=$(ls ../project-under-test)

cd ../project-under-test/$PROJECT_NAME
# Run tests and generate .xml reports
mvn test > ./log.txt

# Convert .xml reports into .html report, but without the CSS or images
mvn surefire-report:report-only >> ./log.txt

# Put the CSS and images where they need to be without the rest of the
# time-consuming stuff
mvn site -DgenerateReports=false >> ./log.txt

cd ..

mv ./$PROJECT_NAME/target/site ../target/
mv ./$PROJECT_NAME/log.txt ../target/

rm -rf ./$PROJECT_NAME
