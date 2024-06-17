pipeline {
    agent any

    environment {
        DOCKER_IMAGE = 'sodamaple/api_ws_2'
        DOCKER_CREDENTIALS_ID = 'docker-hub-credentials'
    }

    stages {
        stage('Clean') {
            steps {
                bat 'mvn clean'
            }
        }
        stage('Test and Compile') {
            steps {
                bat 'mvn test'
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }
	stage('Build Docker Image') {
            steps {
                script {
                    docker.build(DOCKER_IMAGE)
                }
            }
        }
        stage('Push Docker Image') {
            steps {
                script {
		    docker.withRegistry('https://registry.hub.docker.com/sodamaple', "$DOCKER_CREDENTIALS_ID") {
                         docker.image(DOCKER_IMAGE).push()
                    }
                }
            }
        }
    }
}