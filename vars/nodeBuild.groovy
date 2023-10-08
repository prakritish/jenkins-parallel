def call(String nodeVersion, String label) {
    def parallelStage = {
        node("${label}") {
            stage("Node: ${nodeVersion}, OS: ${label}") {
                stage("Build") {
                    echo "Building on OS: ${label} with Node Version: ${nodeVersion}"
                    sleep(Math.abs(new Random().nextInt() % 600) + 1)
                }
                stage("Report") {
                    echo "Build completed!"
                }
            }
        }
    }
    return parallelStage
}