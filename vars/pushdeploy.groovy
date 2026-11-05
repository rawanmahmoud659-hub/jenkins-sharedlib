
def call(String imageName, String newTag, String deploymentFile) {
    withCredentials([
        usernamePassword(credentialsId: 'github-https', usernameVariable: 'USER', passwordVariable: 'TOKEN')
    ]) {
        sh """
            sed -i 's|image: ${imageName}:.*|image: ${imageName}:${newTag}|' ${deploymentFile}

            git config user.name "rawanmahmoud659-hub"
            git config user.email "rawan.mahmoud659@gmail.com"

            git add ${deploymentFile}
            git commit -m "Update image to ${newTag}"

            set +x
            git push https://\$USER:\$TOKEN@github.com/rawanmahmoud659-hub/ivolve-lab.git HEAD:main
        """
    }
}

