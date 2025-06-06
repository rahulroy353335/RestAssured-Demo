pipeline {
    agent {
        label 'java21-agent' // Replace with your Jenkins agent label
    }

    parameters {
        choice(
            name: 'BRANCH',
            choices: ['main', 'gitactionsemail', 'gitactions'],
            description: 'Select branch to test'
        )
    }
    environment {
        // Lombok configuration for Java 21
        MAVEN_OPTS = """
            --add-opens=jdk.compiler/com.sun.tools.javac.code=ALL-UNNAMED
            --add-opens=jdk.compiler/com.sun.tools.javac.comp=ALL-UNNAMED
            --add-opens=jdk.compiler/com.sun.tools.javac.file=ALL-UNNAMED
            --add-opens=jdk.compiler/com.sun.tools.javac.main=ALL-UNNAMED
            --add-opens=jdk.compiler/com.sun.tools.javac.model=ALL-UNNAMED
            --add-opens=jdk.compiler/com.sun.tools.javac.parser=ALL-UNNAMED
            --add-opens=jdk.compiler/com.sun.tools.javac.processing=ALL-UNNAMED
            --add-opens=jdk.compiler/com.sun.tools.javac.tree=ALL-UNNAMED
            --add-opens=jdk.compiler/com.sun.tools.javac.util=ALL-UNNAMED
            -Djava.awt.headless=true
        """
        REPO_URL = 'https://github.com/rahulroy353335/RestAssured-Demo.git' // Update this
    }

    stages {
        stage('Checkout') {
            steps {
                checkout([
                    $class: 'GitSCM',
                    branches: [[name: "${params.BRANCH}"]],
                    userRemoteConfigs: [[url: "${env.REPO_URL}"]]
                ])                
                
            }
        }

        stage('Build & Test') {
            steps {
                withEnv(["JAVA_HOME=${tool 'jdk-21'}", "PATH+JDK=${tool 'jdk-21'}/bin"]) {
                    sh """
                    mvn -B clean test \
                        -Denvironment=${env.ENVIRONMENT} \
                        -DargLine=\"${MAVEN_OPTS}\"
                    """
                }
            }

        stage('Reports') {
            steps {
                sh 'mkdir -p test-output/extent-reports' // Ensure directory exists
            }
            post {
                always {
                    archiveArtifacts artifacts: 'test-output/extent-reports/**/*', allowEmptyArchive: true
                    archiveArtifacts artifacts: 'target/surefire-reports/**/*', allowEmptyArchive: true
                }
            }
        }
    }
    
    post {
        always {
            script {
                // Generate summary report
                def summary = """
                📋 **Build Summary**
                - Result: ${currentBuild.currentResult}
                - Branch: ${params.BRANCH}

                - Duration: ${currentBuild.durationString}
                - Report: ${env.BUILD_URL}testReport/
                - Artifacts: ${env.BUILD_URL}artifact/
                """
                echo summary

                // Store summary for notifications
                currentBuild.description = summary
            }
        }
    }
}