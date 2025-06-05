pipeline {
    agent any
    
    environment {
        BASE_URL = 'https://reqres.in'
    }
    
    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/your-repo/automation-framework.git'
            }
        }
        
        stage('Build') {
            steps {
                sh 'mvn clean compile'
            }
        }
        
        stage('Test') {
            steps {
                sh 'mvn test'
            }
            post {
                always {
                    archiveArtifacts artifacts: 'test-output/**/*.html', fingerprint: true
                    junit 'test-output/**/*.xml'
                }
            }
        }
        
        stage('Docker Build') {
            steps {
                script {
                    docker.build("automation-framework:${env.BUILD_ID}")
                }
            }
        }
        
        stage('Docker Push') {
            steps {
                script {
                    docker.withRegistry('https://registry.hub.docker.com', 'docker-hub-credentials') {
                        docker.image("automation-framework:${env.BUILD_ID}").push()
                    }
                }
            }
        }
    }
    
    post {
        always {
            emailext attachLog: true,
                attachmentsPattern: 'test-output/**/*.html',
                body: '${DEFAULT_CONTENT}\n\n${BUILD_URL}',
                recipientProviders: [[$class: 'DevelopersRecipientProvider']],
                subject: 'Build Result: ${JOB_NAME} - ${BUILD_NUMBER}'
        }
    }
}