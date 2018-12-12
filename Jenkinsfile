pipeline {
    agent {
        label {
            label ""
            customWorkspace "/var/lib/jenkins/workspace"
        }
    }
    stages {
        stage ('Test') {
            steps {
                sh 'chmod +x ./io_klient_pipeline/gradlew'
                sh './gradlew test'
            }
        }

        stage ('Build') {
            steps {
                sh 'chmod +x ./io_klient_pipeline/gradlew'
                sh './gradlew build'
            }
        }
    }
}