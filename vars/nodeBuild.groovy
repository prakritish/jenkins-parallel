def call(String nodeVersion, String label) {
    def parallelStage = {
        node("${label}") {
            stage("Node: ${nodeVersion}, OS: ${label}") {
                stage("Build") {
                    echo "Building on OS: ${label} with Node Version: ${nodeVersion}"
                }
                stage("Report") {
                    echo "Build completed!"
                }
            }
        }
    }
    return parallelStage
}