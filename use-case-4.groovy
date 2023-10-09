pipeline {
  agent any
  options {
    timestamps()
    skipDefaultCheckout()
  }
  environment {
  // Setting these as Environment variables for the example.
  // Ideally these should come as parameter from Jenkins.
    NO_OF_THREADS = 5
    AGENT_LABEL = "LINUX"
    GITHUB_REPOS = """
      https://github.com/actions/heroku.git
      https://github.com/actions/github.git
      https://github.com/actions/labeler.git
      https://github.com/actions/toolkit.git
      https://github.com/actions/runner.git
      https://github.com/actions/setup-node.git
      https://github.com/actions/runner-images.git
      https://github.com/actions/setup-go.git
      https://github.com/actions/setup-dotnet.git
      https://github.com/actions/setup-python.git
      https://github.com/actions/upload-artifact.git
      https://github.com/actions/download-artifact.git
      https://github.com/actions/typescript-action.git
      https://github.com/actions/container-action.git
      https://github.com/actions/setup-ruby.git
      https://github.com/actions/setup-java.git
      https://github.com/actions/checkout.git
      https://github.com/actions/starter-workflows.git
      https://github.com/actions/container-toolkit-action.git
      https://github.com/actions/first-interaction.git
      https://github.com/actions/hello-world-javascript-action.git
      https://github.com/actions/stale.git
      https://github.com/actions/hello-world-docker-action.git
      https://github.com/actions/example-services.git
      https://github.com/actions/setup-elixir.git
      https://github.com/actions/github-script.git
      https://github.com/actions/javascript-action.git
      https://github.com/actions/create-release.git
      https://github.com/actions/upload-release-asset.git
      https://github.com/actions/setup-haskell.git
    """
  }
  stages {
    stage("Pre Parallel") {
      steps {
        echo "This is Pre Parallel Stage"
      }
    }
    stage("Parallel Stage") {
      steps {
        script {
          // We'll use this Array to store all the repositories provided.
          def repos = []
          // Removing any white spaces and splitting it
          def repoList = env.GITHUB_REPOS.strip().split('\n')
          for (String repo: repoList) {
            if (repo) {
              // Strip white spaces and add it to the Array.
              repos.add(repo.strip())
            }
          }
          def repoCount = repos.size()
          def threads = env.NO_OF_THREADS.toInteger()
          echo "No. of Parallel Scans allowed: ${NO_OF_THREADS}"
/*
          This is important to note. The `repos` is a single dimensional array
          We are using 'collate' to convert this into a two dimensinal array.
          Say,
            repos = ["a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k"]
          The statement
            repo_group = repos.collate 4
          would create a two dimensional array as
            repo_group = [
              ["a", "b", "c", "d"], 
              ["e", "f", "g", "h"],
              ["i", "j", "k"]
            ]
          Or in other words, split the original array into an array of smaller arrays.
*/
          def repo_group = repos.collate threads
          for (def row : repo_group) {
            def targets = [:]
            for (String repo : row) {
              // We pass the empty map targets by reference to generateTargets along with other
              // parameters. The function generateTargets populates the map 'targets'
              generateTargets(targets, repo, env.AGENT_LABEL)
            }
            // Execute parallel stages
            parallel targets
          }
        }
      }
    }
  }
}

def generateTargets(def targets, String repo, String label) {
  targets[repo] = {
    node(label) {
      stage('Scan') {
        try {
          echo "Use this stage to scan ${repo}"
        } catch (err) {
            echo err.toString()
            unstable("endorctl Scan failed for ${project}")
        }
      }
    }
  }
}