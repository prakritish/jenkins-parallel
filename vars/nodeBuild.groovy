def call(String nodeVersion, String label) {
    def parallelStage = {
        stage("Node: ${nodeVersion}, OS: ${label}") {
            agent {
                label "${label}"
            }
            steps {
                echo "Building on OS: ${label} with Node Version: ${nodeVersion}"
            }
            post {
                always {
                    echo "Job Completed"
                }
            }
        }
    }
    return parallelStage
}