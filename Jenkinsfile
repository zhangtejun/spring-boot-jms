pipeline {
   agent any

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
