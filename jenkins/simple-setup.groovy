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
        upstreamBuild()
      }
    }    

    shell('ant -file cd.xml decompress-artifact-remotely')
    shell('ant -file cd.xml apply-live-password')
    shell('ant -file cd.xml migrate-database')
    shell('ant -file cd.xml switch-symlinks')
  }
}