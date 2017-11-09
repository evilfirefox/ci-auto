folder("${CIA_PROJECT_NAME}")

job("${CIA_PROJECT_NAME}/${CIA_PROJECT_NAME}-build") {
  parameters {
    gitParam('BLD_BRANCH') {
      type("BRANCH_TAG")
      defaultValue("master")
    }
  }
  scm {
    git {
      remote {
        github("${CIA_REPO_ALIAS}")
        credentials("${CIA_REPO_CREDENTIALS}")
      }
      branch('${BLD_BRANCH}')
    }
  }
  triggers {
    scm('H/2 * * * *')
    upstream(
      threshold: 'SUCCESS'
        upstreamProjects: '${CIA_PROJECT_NAME}/${CIA_PROJECT_NAME}-test'
    )
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

job("${CIA_PROJECT_NAME}/${CIA_PROJECT_NAME}-test") {
  steps
  {
    
  } 
}

job("${CIA_PROJECT_NAME}/${CIA_PROJECT_NAME}-deploy") {
}