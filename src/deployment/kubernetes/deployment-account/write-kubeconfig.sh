#!/bin/bash -e

curdir=$(dirname "$0")

# Usage ./write-kubeconfig.sh ( namespace ) ( service account name )

user=github-deployment

tmpcert=$(mktemp)
KUBE_DEPLOY_SECRET_NAME=$(kubectl get sa $user -o jsonpath='{.secrets[0].name}')
KUBE_API_EP=$(kubectl get ep -o jsonpath='{.items[0].subsets[0].addresses[0].ip}')
KUBE_API_TOKEN=$(kubectl get secret "$KUBE_DEPLOY_SECRET_NAME" -o jsonpath='{.data.token}' | base64 --decode)
KUBE_API_CA=$(kubectl get secret "$KUBE_DEPLOY_SECRET_NAME" -o jsonpath='{.data.ca\.crt}' | base64 --decode)
echo "$KUBE_API_CA" > "$tmpcert"

export KUBECONFIG=$curdir/kubeconfig.secret.yaml
rm -f $KUBECONFIG
kubectl config set-cluster k8s --server="https://$KUBE_API_EP" --certificate-authority="$tmpcert" --embed-certs=true
kubectl config set-credentials $user --token="$KUBE_API_TOKEN"
kubectl config set-context k8s --cluster k8s --user $user
kubectl config use-context k8s

rm "$tmpcert"
unset KUBECONFIG
