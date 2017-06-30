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
      admin('devastator')
      cron('H/2 * * * *')
    }
  }
}