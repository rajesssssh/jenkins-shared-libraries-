def call1() {
    echo 'Checking out code...'
    checkout scm
}
def call2() {
    echo 'Setting up Java 17...'
    sh 'sudo apt update'
    sh 'sudo apt install -y openjdk-17-jdk'
}
def call3() {
    echo 'Setting up Maven...'
    sh 'sudo apt install -y maven'
}
def call4() {
    echo 'Building project with Maven...'
    sh 'mvn clean package'
}
def call5(String artifactPath) {
    echo 'Uploading artifact...'
    archiveArtifacts artifacts: artifactPath, allowEmptyArchive: true
}   
def call6() {
    echo 'Running Spring Boot application...'
    sh 'nohup mvn spring-boot:run &'
    sleep(time: 15, unit: 'SECONDS')

    def publicIp = sh(script: "curl -s https://checkip.amazonaws.com", returnStdout: true).trim()
    echo "The application is running and accessible at: http://${publicIp}:8080"
}
def call7() {
    echo 'Validating that the app is running...'
    def response = sh(script: 'curl --write-out "%{http_code}" --silent --output /dev/null http://localhost:8080', returnStdout: true).trim()
    if (response == "200") {
        echo 'The app is running successfully!'
    } else {
        echo "The app failed to start. HTTP response code: ${response}"
        error("The app did not start correctly!")
    }
}    
def call8() {
    echo 'Waiting for 2 minutes...'
    sleep(time: 2, unit: 'MINUTES')  // Wait for 5 minutes
}
def call9() {
    echo 'Gracefully stopping the Spring Boot application...'
    sh 'mvn spring-boot:stop'
}
def call10() {
    echo 'Cleaning up...'
    sh 'pkill -f "mvn spring-boot:run" || true'
}    
