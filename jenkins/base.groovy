folder("${CIA_PROJECT_NAME}")

job("${CIA_PROJECT_NAME}/${CIA_PROJECT_NAME}-build") {
  parameters {
    stringParam('BLD_BRANCH', 'master', 'branch name to build')
  }
  scm {
    git {
      remote {
        github("${CIA_REPO_ALIAS}")
        credentials("${CIA_REPO_CREDENTIALS}")
      }
      branch('*/${BLD_BRANCH}')
    }
  }
  triggers {
    scm('H/2 * * * *')
  }
  steps {
    shell('composer install')
    shell('rm -rf var/cache/* var/logs/*')
    shell('ant -file cd.xml compress-artifact')
  }
  publishers
  {
     archiveArtifacts('**/*.caf, cd.xml')
  }
}

job("${CIA_PROJECT_NAME}/${CIA_PROJECT_NAME}-deploy") {  
}