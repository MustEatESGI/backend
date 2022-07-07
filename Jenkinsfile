pipeline {
    agent any
    tools {
        maven 'maven-3.8.4'
        jdk 'openjdk-11'
        dockerTool 'docker-agent'
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
                    withCredentials([file(credentialsId: 'ESGI_MUSTEAT_APPLICATION_PROD', variable: 'FILE')]) {
                        writeFile file: 'application.properties', text: readFile(FILE)
                        def mvn = tool 'maven-3.8.4';
                        withSonarQubeEnv() {
                            sh "${mvn}/bin/mvn clean verify sonar:sonar -Dsonar.projectKey=MustEatESGI_backend -Dsonar.branch.name=${env.BRANCH_NAME} -DskipTests"
                        }
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
                    withCredentials([file(credentialsId: 'ESGI_MUSTEAT_APPLICATION_PROD', variable: 'FILE')]) {
                        writeFile file: 'application.properties', text: readFile(FILE)
                        def mvn = tool 'maven-3.8.4';
                        withSonarQubeEnv() {
                            sh "${mvn}/bin/mvn clean verify sonar:sonar -Dsonar.projectKey=MustEatESGI_backend\
                                                    -Dsonar.pullrequest.key=${env.CHANGE_ID} \
                                                    -Dsonar.pullrequest.base=${env.CHANGE_TARGET} \
                                                    -Dsonar.pullrequest.branch=${env.CHANGE_BRANCH} -DskipTests"
                        }
                    }
                }
            }
        }
        stage('Unit test') {
            steps {
                script {
                    withCredentials([file(credentialsId: 'ESGI_MUSTEAT_APPLICATION_PROD', variable: 'FILE')]) {
                        def mvn = tool 'maven-3.8.4';
                        sh "${mvn}/bin/mvn clean test -Dspring.config.location=${FILE}"
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
                    sh "${mvn}/bin/mvn clean package -DskipTests -Dmaven.test.skip=true"
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
        stage('Build Dockerfile and push to gcloud') {
            when {
                branch 'main'
            }
            steps {
                script {
                    withCredentials([file(credentialsId: 'ESGI_MUSTEAT_APPLICATION_PROD', variable: 'FILE'), file(credentialsId: 'ESGI_MUSTEAT_GCLOUD_KEY', variable: 'AUTH')]) {
                        def docker = tool 'docker-agent';
                        writeFile file: 'application.properties', text: readFile(FILE)
                        sh "gcloud auth activate-service-account jenkins@tough-valve-353020.iam.gserviceaccount.com	--key-file=${AUTH}";
                        sh "gcloud auth configure-docker europe-west9-docker.pkg.dev";
                        sh "${docker}/bin/docker build --no-cache . -t europe-west9-docker.pkg.dev/tough-valve-353020/musteat/backend:latest";
                        sh "${docker}/bin/docker push europe-west9-docker.pkg.dev/tough-valve-353020/musteat/backend:latest";
                    }

                }
            }
        }
        stage('Delete docker local image') {
            when {
                branch 'main'
            }
            steps {
                script {
                    withCredentials([file(credentialsId: 'ESGI_MUSTEAT_APPLICATION_PROD', variable: 'FILE'), file(credentialsId: 'ESGI_MUSTEAT_GCLOUD_KEY', variable: 'AUTH')]) {
                        def docker = tool 'docker-agent';
                        sh "${docker}/bin/docker image rm europe-west9-docker.pkg.dev/tough-valve-353020/musteat/backend:latest"
                    }

                }
            }
        }
        stage('Cleaning project') {
            steps {
                script {
                    def mvn = tool 'maven-3.8.4';
                    def docker = tool 'docker-agent';
                    sh "${mvn}/bin/mvn clean"
                }
            }
        }
    }

}
