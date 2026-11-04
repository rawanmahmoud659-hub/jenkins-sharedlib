
def call(String imageName, String imageTag) {
    def image = "${imageName}:${imageTag}"

    try {
        stage('Build Image') {
            sh "docker build -t ${image} ."
        }

        stage('Scan Image') {
            sh "trivy image --exit-code 1 --severity HIGH,CRITICAL ${image}"
        }

        stage('Push Image') {
            withCredentials([
                usernamePassword(
                    credentialsId: 'docker',
                    usernameVariable: 'DOCKER_USER',
                    passwordVariable: 'DOCKER_PASS'
                )
            ]) {
                sh '''
                    echo "$DOCKER_PASS" |
                    docker login -u "$DOCKER_USER" --password-stdin
                '''
                sh "docker push ${image}"
            }
        }
    } finally {
        stage('Delete Local Image') {
            sh "docker image rm ${image} || true"
        }
    }
}

