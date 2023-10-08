@Library("parallel-jobs") _
String[] versions = ["12.22.12", "14.21.3", "16.20.2", "18.18.0"]
String[] osLabel = ["WIN", "OSX", "LINUX"]

pipeline {
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
    stage("Parallel Stages") {
      steps {
        script {
          def targets = [:]
          for (String node : versions) {
            for (String os : osLabel) {
              targets["${os}-${node}"] = nodeBuild(node, os)
            }
          }
          parallel targets
        }
      }
    }
  }
}