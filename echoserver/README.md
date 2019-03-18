```
cd echoserver
docker build . -t echoserver
docker tag echoserver  $(minishift openshift registry)/$(oc project -q)/echoserver
docker login -u developer -p `oc whoami -t` docker-registry-default.`minishift ip`.nip.io:443`
docker push $(minishift openshift registry)/$(oc project -q)/echoserver
oc create --template=echoserver.yml
oc new-app --template=echoserver
oc scale sts echoserver --replicas=3
```

* each even server will start with readiness probe being true
* each odd server will get failing readiness probe forever

```
oc rsh echoserver-0
curl -I echoserver-1:8080
curl -I echoserver-1.echoserver:8080
```


