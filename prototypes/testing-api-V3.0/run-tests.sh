cd ../project-under-test

mvn test

# time-consuming stuff:
# Convert .xml reports into .html report, but without the CSS or images
#mvn surefire-report:report-only
# Put the CSS and images where they need to be without the rest of the
#mvn site -DgenerateReports=false

# Not really necessary
#mv ./project-under-test/target/site ./target/projectUnderTest-site

cat ./target/surefire-reports/*.txt > ../target/log.txt

