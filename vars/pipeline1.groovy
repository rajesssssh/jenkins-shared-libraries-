def check_out() {
    echo 'Checking out code...'
    checkout scm
}
def setup_java() {
    echo 'Setting up Java 17...'
    sh 'sudo apt update'
    sh 'sudo apt install -y openjdk-17-jdk'
}
def setup_maven() {
    echo 'Setting up Maven...'
    sh 'sudo apt install -y maven'
}
def setup_build() {
    echo 'Building project with Maven...'
    sh 'mvn clean package'
}
def upload_artifact(String artifactPath) {
    echo 'Uploading artifact...'
    archiveArtifacts artifacts: artifactPath, allowEmptyArchive: true
}
def run_application() {
    echo 'Running Spring Boot application...'
    sh 'nohup mvn spring-boot:run &'
    sleep(time: 15, unit: 'SECONDS')

    def publicIp = sh(script: "curl -s https://checkip.amazonaws.com", returnStdout: true).trim()
    echo "The application is running and accessible at: http://${publicIp}:8080"
}
def validate_app() {
    echo 'Validating that the app is running...'
    def response = sh(script: 'curl --write-out "%{http_code}" --silent --output /dev/null http://localhost:8080', returnStdout: true).trim()
    if (response == "200") {
        echo 'The app is running successfully!'
    } else {
        echo "The app failed to start. HTTP response code: ${response}"
        error("The app did not start correctly!")
    }	
def keep_app() {
    echo 'Waiting for 2 minutes...'
    sleep(time: 2, unit: 'MINUTES')  // Wait for 5 minutes
}
def stop_app() {
    echo 'Gracefully stopping the Spring Boot application...'
    sh 'mvn spring-boot:stop'
}
def clean_app() {
    echo 'Cleaning up...'
    sh 'pkill -f "mvn spring-boot:run" || true'
}
}
