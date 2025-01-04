@Library('my-shared-library@main') _ 

pipeline {
    agent { label 'slave2' }

    environment {
        JAVA_HOME = '/usr/lib/jvm/java-17-openjdk-amd64'
        MAVEN_HOME = '/usr/share/maven'
        PATH = "${JAVA_HOME}/bin:${MAVEN_HOME}/bin:${env.PATH}"
    }

    stages {
        stage('Checkout Code') {
            steps {
                pipeline1.check_out()
            }
        }

        stage('Set up Java 1') {
            steps {
                pipeline1.setup_java()
            }
        }

        stage('Set up Maven') {
            steps {
                pipeline1.setup_maven()
            }
        }

        stage('Build with Maven') {
            steps {
                pipeline1.setup_build()
            }
        }

        stage('Upload Artifact') {
            steps {
                echo 'Uploading artifact...'
                pipeline1.upload_artifact(String artifactPath)
            }
        }

        stage('Run Application') {
            steps {
                pipeline1.run_application()
            }
        }

        stage('Validate App is Running') {
            steps {
                pipeline1.validate_app()
            }
        }

        stage('Keeping application up for 2 mins') {
            steps {
                pipeline1.keep_app()
            }
        }

        stage('Gracefully Stop Spring Boot App') {
            steps {
                pipeline1.stop_app()
            }
        }
    }
}

    
		
    
