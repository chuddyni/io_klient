pipeline {
    agent {
        label {
            label ""
            customWorkspace "/var/lib/jenkins/workspace/io_klient_pipeline"
        }
    }
    stages {
        stage ('Clean') {
            steps {
                sh 'chmod +x ./gradlew'
                sh './gradlew clean'
            }
        }

        stage ('Test') {
            steps {
                sh 'chmod +x ./gradlew'
                sh './gradlew test'
            }
        }

        stage ('Build') {
            steps {
                sh 'chmod +x ./gradlew'
                sh './gradlew build'
            }
        }

    }

    post {
        always {
            junit '**/test-results/testReleaseUnitTest/*.xml'
        }
    }
}