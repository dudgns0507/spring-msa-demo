pipeline {
    agent any

    environment {
        AUTH = "auth-server"
        EUREKA = "eureka-server"
        CONFIG = "config-server"
        GATEWAY = "gateway-server"
        GITHUB_URL = "https://github.com/dudgns0507/spring-msa-demo"
    }

    parameters {
        string(name : "branch", defaultValue : "main", description : "")
    }

    stages {
		stage("Git") {
			steps {
			    git(
			        branch: "${params.branch}",
			        credentialsId: "a7c0186a-cf9a-4e15-b6c6-b0c796925c4b",
			        url: "${GITHUB_URL}"
		        )
			}

			post {
                success {
                    sh "echo \"Successfully Cloned Repository\""
                }
                failure {
                    sh "echo \"Fail Cloned Repository\""
                }
            }
		}

        stage("Build") {
            steps {
                sh "chmod +x ./gradlew"
                sh  "./gradlew clean build"
            }
            post {
                success {
                    echo "gradle build success"
                }

                failure {
                    echo "gradle build failed"
                }
            }
        }

        stage("SSH Transfer") {
            steps([$class: "BapSshPromotionPublisherPlugin"]) {
                sshPublisher(
                    continueOnError: false, failOnError: true,
                    publishers: [
                        sshPublisherDesc(
                            configName: "Home Server",
                            verbose: true,
                            transfers: [
                                sshTransfer(
                                    sourceFiles: "*/build/libs/*.jar",
                                    removePrefix: "*/build/libs",
                                    remoteDirectory: "/cropo",
                                    execCommand: ""
                                )
                            ]
                        )
                    ]
                )
            }
        }

        stage("Launch") {
            steps {
                sh "sudo sh /root/cropo/deploy-config.sh"
                sh "sudo sh /root/cropo/deploy-eureka.sh"
                sh "sudo sh /root/cropo/deploy-gateway.sh"
                sh "sudo sh /root/cropo/deploy-auth.sh"
            }
        }
    }
}