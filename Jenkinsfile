pipeline {
    agent {
		node {
			label 'master'
		}
	}
    tools{
        maven 'apache-maven'
        jdk 'JDK 11'
    }
    options {
        skipStagesAfterUnstable()
    }

    stages {
        stage('Docker cleanup') {
            steps{
				sh '''
				docker rmi $(docker images -f 'dangling=true' -q) || true
				docker rmi $(docker images | sed 1,2d | awk '{print $3}') || true
				'''
            }
        }

		stage('Docker Build') {
			steps {
				sh 'docker build -t alexstraszacontainerregistry.azurecr.io/s6-auth:kube${BUILD_NUMBER} ./authentication'
				sh 'docker build -t alexstraszacontainerregistry.azurecr.io/s6-auction:kube${BUILD_NUMBER} ./auctioning'
				sh 'docker build -t alexstraszacontainerregistry.azurecr.io/s6-currency:kube${BUILD_NUMBER} ./currency'
				sh 'docker build -t alexstraszacontainerregistry.azurecr.io/s6-inventory:kube${BUILD_NUMBER} ./inventory'
				sh 'docker build -t alexstraszacontainerregistry.azurecr.io/s6-webapi:kube${BUILD_NUMBER} ./web-api'
				sh 'docker build -t alexstraszacontainerregistry.azurecr.io/s6-gateway:kube${BUILD_NUMBER} ./gateway'
			}
		}

		stage('Docker publish') {
			steps {
			withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId:'acr-credentials', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD']]){
				    sh 'docker login alexstraszacontainerregistry.azurecr.io -u $USERNAME -p $PASSWORD'
				    sh 'docker push alexstraszacontainerregistry.azurecr.io/s6-auth:kube${BUILD_NUMBER}'
				    sh 'docker push alexstraszacontainerregistry.azurecr.io/s6-auction:kube${BUILD_NUMBER}'
				    sh 'docker push alexstraszacontainerregistry.azurecr.io/s6-currency:kube${BUILD_NUMBER}'
				    sh 'docker push alexstraszacontainerregistry.azurecr.io/s6-inventory:kube${BUILD_NUMBER}'
				    sh 'docker push alexstraszacontainerregistry.azurecr.io/s6-webapi:kube${BUILD_NUMBER}'
				    sh 'docker push alexstraszacontainerregistry.azurecr.io/s6-gateway:kube${BUILD_NUMBER}'
				}
			}
		}
		stage('kubetcl set') {
        			steps {
        				sh 'kubectl set image deployment/s6-auth s6-auth=alexstraszacontainerregistry.azurecr.io/s6-auth:kube${BUILD_NUMBER} --kubeconfig /home/alexstrasza/.kube/config'
        				sh 'kubectl set image deployment/s6-auction s6-auction=alexstraszacontainerregistry.azurecr.io/s6-auction:kube${BUILD_NUMBER} --kubeconfig /home/alexstrasza/.kube/config'
        				sh 'kubectl set image deployment/s6-currency s6-currency=alexstraszacontainerregistry.azurecr.io/s6-currency:kube${BUILD_NUMBER} --kubeconfig /home/alexstrasza/.kube/config'
        				sh 'kubectl set image deployment/s6-inventory s6-inventory=alexstraszacontainerregistry.azurecr.io/s6-inventory:kube${BUILD_NUMBER} --kubeconfig /home/alexstrasza/.kube/config'
        				sh 'kubectl set image deployment/s6-webapi s6-webapi=alexstraszacontainerregistry.azurecr.io/s6-webapi:kube${BUILD_NUMBER} --kubeconfig /home/alexstrasza/.kube/config'
        				sh 'kubectl set image deployment/s6-gateway s6-gateway=alexstraszacontainerregistry.azurecr.io/s6-gateway:kube${BUILD_NUMBER} --kubeconfig /home/alexstrasza/.kube/config'
        			}
        		}
    }
}