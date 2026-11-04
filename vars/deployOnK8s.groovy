def call(String imageName, String newTag, String deploymentFile, String server) {
    withCredentials([
        string(credentialsId: 'kubetoken', variable: 'TOKEN')
    ]) {
        sh """
            sed -i 's|image: ${imageName}:.*|image: ${imageName}:${newTag}|' ${deploymentFile}

            kubectl apply -f ${deploymentFile} --server ${server} --token ${TOKEN} --insecure-skip-tls-verify

            DEPLOY_NAME=\$(awk '/^metadata:/{f=1;next} f&&/^  name:/{print \$2;exit}' ${deploymentFile})
            NAMESPACE=\$(awk '/^metadata:/{f=1;next} f&&/^  namespace:/{print \$2;exit}' ${deploymentFile})

            kubectl rollout status deployment/\${DEPLOY_NAME} -n \${NAMESPACE} --timeout=120s --server ${server} --token ${TOKEN} --insecure-skip-tls-verify
        """
    }
}
