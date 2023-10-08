pipeline {
  // We don't care where this pipeline runs
  agent any
  options {
    timestamps ()
    skipDefaultCheckout()
  }
  stages {
    stage("Pre-Parallel Stage") {
      steps {
        echo "This is Pre-Parallel Stage"
      }
    }
    stage("Matrix Stages") {
      matrix {
        axes {
          axis {
            name "LABEL"
            values "WIN", "OSX", "LINUX"
          }
          axis {
            name "NODE_VERSION"
            values "12.22.12", "14.21.3", "16.20.2", "18.18.0"
          }
        }
        agent {
          label "${LABEL}"
        }
        stages {
          stage("Build") {
            steps {
              echo "Do Build for OS: ${LABEL} - Node Version: ${NODE_VERSION}"
            }
          }
          stage("Report") {
            steps {
              echo "Done"
            }
          }
        }
        post {
          always {
            echo "Build completed for OS: ${LABEL} - Node Version: ${NODE_VERSION}"
          }
        }
      }
    }
  }
  post {
    always {
      echo "All Done!"
    }
  }
}
