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
  }
  steps {
    shell('composer install')
    shell('rm -rf var/cache/* var/logs/*')
    shell('ant -file cd.xml compress-artifact')
  }
  publishers
  {
    archiveArtifacts('**/*.caf, cd.xml')
    downstream("${CIA_PROJECT_NAME}/${CIA_PROJECT_NAME}-test", 'SUCCESS')
  }
}

job("${CIA_PROJECT_NAME}/${CIA_PROJECT_NAME}-test") {
  steps
  {
    copyArtifacts("${CIA_PROJECT_NAME}/${CIA_PROJECT_NAME}-build") {      
      buildSelector {
        upstreamBuild()
      }
    }
    shell('ant -file cd.xml decompress-artifact')
    shell('chmod +x bin/*')
    shell('./bin/phpunit --coverage-clover coverage.xml')
  }
  publishers {
     cloverPHP('coverage.xml') {
       publishHtmlReport('reports') {
         disableArchiving()
       }
       healthyMethodCoverage(90)
       healthyStatementCoverage(80)
       unhealthyMethodCoverage(60)
       unhealthyStatementCoverage(50)
       unstableMethodCoverage(50)
       unstableStatementCoverage(40)
     }
  }
}

job("${CIA_PROJECT_NAME}/${CIA_PROJECT_NAME}-deploy") {
}