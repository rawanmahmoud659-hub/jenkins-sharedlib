
def call() {
    stage('Unit Test') {
        sh 'mvn test'
    }

    stage('Build Application') {
        sh 'mvn package -DskipTests'
    }
}

