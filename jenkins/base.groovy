job('auto-refuel-master') {
  scm {
    git {
      remote {
        github('evilfirefox/refuel2')
        credentials('c6951f44-060d-4c1e-8f26-cfbf3e404d86')
      }
      branch('*/master')
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