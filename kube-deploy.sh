#!/bin/sh

kubectl apply -f eureka-server/kube-deployment.yml
kubectl apply -f config-server/kube-deployment.yml
kubectl apply -f hello-service/kube-deployment.yml
kubectl apply -f hello-sub-service/kube-deployment.yml
kubectl apply -f gateway/kube-deployment.yml
kubectl apply -f zuul/kube-deployment.yml

kubectl delete -f gateway/kube-deployment.yml
kubectl delete -f zuul/kube-deployment.yml
kubectl delete -f hello-service/kube-deployment.yml
kubectl delete -f hello-sub-service/kube-deployment.yml
kubectl delete -f eureka-server/kube-deployment.yml
kubectl delete -f config-server/kube-deployment.yml