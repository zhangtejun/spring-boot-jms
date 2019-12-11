pipeline {
   agent any
   tools {
      maven 'maven3.6.3'
   }
   stages {
      stage('Build') {
         steps {
            echo 'Build starting ....'
            sh 'mvn clean package'
            sh 'printenv'
         }
      }
   }
}
