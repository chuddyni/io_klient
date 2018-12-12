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
            sh 'cd io_klient_pipeline@2/app/build/test-results/testReleaseUnitTest'
            sh 'touch *.xml'
            sh 'cd /var/lib/jenkins/workspace/io_klient_pipeline'
            junit '**/test-results/testReleaseUnitTest/*.xml'
        }
    }
}