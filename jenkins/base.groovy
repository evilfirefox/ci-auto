job('auto-refuel-master') {
  scm {
    git {
      remote {
        github('evilfirefox/refuel2')
        refspec('*/master')
      }
    }
  }
  triggers {
    githubPullRequest {
      cron('H/2 * * * *')
    }
  }
}