@Library("parallel-jobs") _

// Emulating user input or input from external sources below
// Creating a string array named versions to denote the list of NodeJs versions to use
String[] versions = ["12.22.12", "14.21.3", "16.20.2", "18.18.0"]
// Creating a string array name osLabel to denote the different OS's we are going to use
String[] osLabel = ["WIN", "OSX", "LINUX"]
// The total numer of parallel jobs would versions.size() * osLabel.size()
// In this example total number of parallel jobs = 4 * 3 = 12

def targets = [:]
for (String node : versions) {
  for (String os : osLabel) {
    // Populating the map 'targets'
    // nodeBuild(String, String) is coming from shared library 'vars/nodeBuild.groovy'
    targets["${os}-${node}"] = nodeBuild(node, os)
  }
}


pipeline {
  // We don't care where this pipeline runs
  agent any
  options {
    timestamps ()
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
}