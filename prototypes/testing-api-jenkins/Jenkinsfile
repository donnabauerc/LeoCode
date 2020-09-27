pipeline {
    agent {
        docker { image 'maven:3.6.3-adoptopenjdk-11' }
    }
    stages {
        stage('Test') {
            steps {
                sh 'java --version'
                sh 'mvn --version'
                sh 'cd prototypes/simulating-files/code-examples && mvn test'
            }
        }
    }
}
