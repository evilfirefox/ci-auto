folder("${CIA_PROJECT_NAME}")

job("${CIA_PROJECT_NAME}/${CIA_PROJECT_NAME}-build-master") {
  scm {
    git {
      remote {
        github("${CIA_REPO_ALIAS}")
        credentials("${CIA_REPO_CREDENTIALS}")
      }
      branch('master')
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

job("${CIA_PROJECT_NAME}/${CIA_PROJECT_NAME}-deploy-master") {
  steps
  {
    copyArtifacts("${CIA_PROJECT_NAME}/${CIA_PROJECT_NAME}-build-master") {      
      buildSelector {
        latestSuccessful(true)
      }
    }    

    publishers {
        publishOverSsh {
            server('vlkr2-dev-web7') {
                label('vlkr2-dev-web7')
                transferSet {
                    sourceFiles('**/*.*')
                    remoteDirectory("${CIA_PROJECT_NAME}" + '/${BUILD_DISPLAY_NAME}')
                    
                }
            }
        }
    }
  }
}