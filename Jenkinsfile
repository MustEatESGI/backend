pipeline {
    agent any
    tools {
        maven 'maven-3.8.4'
        jdk 'openjdk-11'
    }
    stages {
        stage('SCM') {
            steps {
                script {
                    checkout scm
                }
            }
        }
        stage('SonarQube Analysis') {
            when {
                not {
                    changeRequest()
                }
            }
            steps {
                script {
                    def mvn = tool 'maven-3.8.4';
                    withSonarQubeEnv() {
                        sh "${mvn}/bin/mvn clean verify sonar:sonar -Dsonar.projectKey=MustEatESGI_backend -Dsonar.branch.name=${env.BRANCH_NAME}"
                    }
                }
            }
        }
        stage('SonarQube PR Analysis') {
            when {
                changeRequest()
            }
            steps {
                script {
                    def mvn = tool 'maven-3.8.4';
                    withSonarQubeEnv() {
                        sh "${mvn}/bin/mvn clean verify sonar:sonar -Dsonar.projectKey=MustEatESGI_backend\
                                                -Dsonar.pullrequest.key=${env.CHANGE_ID} \
                                                -Dsonar.pullrequest.base=${env.CHANGE_TARGET} \
                                                -Dsonar.pullrequest.branch=${env.CHANGE_BRANCH}"
                    }
                }
            }
        }
        stage('Unit test') {
            steps {
                script {
                    def mvn = tool 'maven-3.8.4';
                    withSonarQubeEnv() {
                        sh "${mvn}/bin/mvn clean test"
                    }
                }
            }
        }
        stage('Package jar') {
            when {
                branch 'develop'
            }
            steps {
                script {
                    def mvn = tool 'maven-3.8.4';
                    withSonarQubeEnv() {
                        sh "${mvn}/bin/mvn clean package"
                    }
                }
            }
        }
        stage('Deploy to dev environment') {
            when {
                branch 'develop'
            }
            steps   {
                withCredentials([usernamePassword(credentialsId: 'JEE_LPHN_SERVER', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]){
                    nodejs(nodeJSInstallationName: 'nodejs'){
                        sh('sshpass -p ${PASSWORD} scp -P 22 -o StrictHostKeyChecking=no -o UserKnownHostsFile=/dev/null target/backend-0.0.1-SNAPSHOT.jar ${USERNAME}@192.168.1.78:/root/.')
                    }
                }
            }
        }
        stage('Cleaning project') {
            steps {
                script {
                    def mvn = tool 'maven-3.8.4';
                    withSonarQubeEnv() {
                        sh "${mvn}/bin/mvn clean"
                    }
                }
            }
        }
    }

}