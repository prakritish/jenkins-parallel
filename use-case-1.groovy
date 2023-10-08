pipeline {
  agent any
  options {
    timestamps ()
  }
  stages {
    stage("Pre-Parallel Stage") {
      steps {
        echo "This is pre-parallel stage"
      }
    }
    stage("Parallel Tests") {
      parallel {
        stage("Windows Test") {
          agent {
            label "WIN"
          }
          steps {
            echo "Running on Windows"
          }
          post {
            always {
              echo "Job on Windows Completed"
            }
            failure {
              echo "Windows Job Failed"
            }
            success {
              echo "Windows Job Successful"
            }
          }
        }
        stage("Mac Test") {
          agent {
            label "OSX"
          }
          steps {
            echo "Running on Mac"
          }
          post {
            always {
              echo "Job on Mac Completed"
            }
            failure {
              echo "Mac Job Failed"
            }
            success {
              echo "Mac Job Successful"
            }
          }
        }
        stage("Linux Test") {
          agent {
            label "LINUX"
          }
          steps {
            echo "Running on Linux"
          }
          post {
            always {
              echo "Job on Linux Completed"
            }
            failure {
              echo "Linux Job Failed"
            }
            success {
              echo "Linux Job Successful"
            }
          }
        }
      }
    }
    stage("Post-Parallel Stage") {
      steps {
        echo "This is post-parallel stage"
      }
    }
  }
}

