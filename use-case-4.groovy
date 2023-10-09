pipeline {
  agent any
  options {
    timestamps()
    skipDefaultCheckout()
  }
  environment {
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
          String[] repos = []
          def repoList = env.GITHUB_REPOS.strip().split('\n')
          for (String repo: repoList) {
            if (repo) {
              repos.add(repo.strip())
            }
          }
          def repoCount = repos.size()
          def threads = env.NO_OF_THREADS.toInteger()
          echo "No. of Parallel Scans allowed: ${NO_OF_THREADS}"
          def repo_group = repos.collate threads
          for (def row : repo_group) {
            def targets = [:]
            for (String repo : row) {
              generateTargets(targets, repo, env.AGENT_LABEL)
            }
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