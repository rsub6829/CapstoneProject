   def configuration = [vaultUrl: 'http://my-very-other-vault-url.com',
                         vaultCredentialId: '',vaultNamespace:'cx-devops',
                         engineVersion: 2]
   def secrets = [
	   [path: '/test/data/shop/ss_web_e2e_tests/healenium', engineVersion: 2, secretValues: [
		   [envVar: 'HEALENIUM_DB_PASSWORD', vaultKey: 'HEALENIUM_DB_PASSWORD']]]]
   
pipeline {
	options {
    buildDiscarder(logRotator(numToKeepStr: '50', daysToKeepStr: '30'))
    ansiColor('xterm')
  }
  agent any
  tools {
    nodejs "NodeJS14.9.0"
    jdk "open-jdk-11"
  }
	stages {
	  stage('Checkout') {
		  git(
				  branch: 'master',
				  changelog: false,
				  credentialsId: 'githubSyscoCorporation-svc_bender_000',
				  poll: false,
				  url: 'https://github.com/rsub6829/CapstoneProject.git')
	  }
	  stage('Start container') {
		steps {
		script{
		  sh 'docker-compose --env-file default.env up -d --wait'
		  sh 'docker compose ps'
		  }
		}
	  }
	
		  stage('Run Tests') {
			  env.JAVA_HOME = "${tool 'openjdk-17'}"
			  env.PATH = "${env.JAVA_HOME}/bin:${env.PATH}"
			  timeout(120){
				  wrap([$class: 'Xvfb', additionalOptions: '', assignedLabels: '', autoDisplayName: false, debug: false, displayNameOffset: 100, installationName: 'xvfb', parallelBuild: false, screen: '1920x1080x16', timeout: 0]) {
					  dir('e2e') {
						  sh(script: "echo `java -version`")
						  sh(script: "echo `google-chrome --version`")
  
						  sh(script: "pkill -9 -f \"selenium-server-standalone\" || true")
  
						  withCredentials([string(credentialsId: 'VAULT_GITHUB_TOKEN', variable: 'VAULT_GITHUB_TOKEN')]) {
							  sh(script: """mvn clean test -Dtest.layer=gui -Dtest.suite=testng -Dgithub.token=${VAULT_GITHUB_TOKEN}""")
                        }
                    }
                }
            }
        }
  }
  post {
    always {
      sh 'docker compose down --remove-orphans -v'
      sh 'docker compose ps'
    }
  }
}