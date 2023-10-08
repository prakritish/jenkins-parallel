def call(String nodeVersion, String label) {
    // The variable parallelStage will be used to store the code for one of the parallel job.
    // This is dynamically getting generated with the received inputs.
    def parallelStage = {
        // We specify the label of the Jenkins Agent where this particular job will be executed
        node("${label}") {
            stage("Node: ${nodeVersion}, OS: ${label}") {
                stage("Build") {
                    echo "Building on OS: ${label} with Node Version: ${nodeVersion}"
                    // Sleeping for random number of seconds to emulate a build job
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