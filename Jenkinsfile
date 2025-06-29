// // pipeline {
// //     agent {
// //         docker {
// //             image 'markhobson/maven-chrome:latest'
// //             args '-v /tmp:/tmp'
// //         }
// //     }
// //     environment {
// //         BASE_URL = 'http://184.73.146.183/:5000'
// //     }
// //     stages {
// //         stage('Deploy App') {
// //             steps {
// //                 sh '''
// //                 cd /home/ubuntu/dvd-rental-app
// //                 docker-compose down
// //                 docker-compose up -d --build
// //                 '''
// //             }
// //         }
// //         stage('Checkout') {
// //             steps {
// //                 git url: 'https://github.com/MUHAMMAD-ABDULLHA/dvd-rental-tests.git', branch: 'main', credentialsId: 'github-credentials'
// //             }
// //         }
// //         stage('Test') {
// //             steps {
// //                 sh 'mvn clean test'
// //             }
// //         }
// //     }
// //     post {
// //         always {
// //             emailext (
// //                 subject: "Jenkins Build #${env.BUILD_NUMBER} - ${currentBuild.result}",
// //                 body: "Test results for build #${env.BUILD_NUMBER}. See attached logs.",
// //                 to: "${env.CHANGE_AUTHOR_EMAIL}",
// //                 attachLog: true,
// //                 attachmentsPattern: 'target/surefire-reports/*.xml'
// //             )
// //             archiveArtifacts artifacts: 'target/surefire-reports/*.xml', allowEmptyArchive: true
// //         }
// //     }
// // }
// pipeline {
//     agent {
//         docker {
//             image 'markhobson/maven-chrome:latest'
//             args '-v /tmp:/tmp'
//         }
//     }
//     environment {
//         BASE_URL = 'http://184.73.146.183/:5000'
//     }
//     stages {
//         stage('Build') {
//             steps {
//                 git url: 'https://github.com/MUHAMMAD-ABDULLHA/dvd-rental-tests.git', branch: 'main'
//                 sh 'mvn clean compile'
//             }
//         }
//         stage('Test') {
//             steps {
//                 sh 'mvn test'
//             }
//         }
//         stage('Deploy') {
//             steps {
//                 sh '''
//                 cd /home/ubuntu/dvd-rental-app
//                 docker-compose down
//                 docker-compose up -d --build
//                 '''
//             }
//         }
//     }
//     post {
//         always {
//             archiveArtifacts artifacts: 'target/surefire-reports/*.xml', allowEmptyArchive: true
//         }
//     }
// }
pipeline {
    agent any

    stages {
        stage('Clean Old Containers') {
            steps {
                sh '''
                docker rm -f dvd-tests || true
                '''
            }
        }

        stage('Clone Test Repo') {
            steps {
                git branch: 'main', url: 'https://github.com/MUHAMMAD-ABDULLHA/dvd-rental-tests.git'
            }
        }

        stage('Run Maven Tests Against EC2 App') {
            steps {
                dir('dvd-rental-tests') {
                    sh '''
                    docker run --rm \
                      -v /home/jenkins/.m2:/home/jenkins/.m2 \
                      -v $(pwd):/usr/src/app \
                      -w /usr/src/app \
                      markhobson/maven-chrome:latest \
                      mvn test
                    '''
                }
            }
        }
    }

    post {
        always {
            echo 'Cleaning up containers after pipeline...'
            sh '''
            docker rm -f dvd-tests || true
            '''
        }
    }
}