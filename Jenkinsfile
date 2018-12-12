pipeline {
    agent {
        label {
            label ""
            customWorkspace "/var/lib/jenkins/workspace/io_klient_pipeline"
        }
    }
    stages {
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
            sh 'touch io_klient_pipeline@2/app/build/test-results/testReleaseUnitTest/*.xml'
            junit '**/test-results/testReleaseUnitTest/*.xml'
        }
    }
}