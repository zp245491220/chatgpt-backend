docker login -u zp245491220@sina.com -p Zp19911024  registry.cn-hangzhou.aliyuncs.com

docker build   -t  gpt-service:0.0.1 .

@echo 3.start tag aigc images

docker  tag  gpt-service:0.0.1 registry.cn-hangzhou.aliyuncs.com/ali_centos/zd_hub:gpt-service

@echo 4.push image to registry

docker push  registry.cn-hangzhou.aliyuncs.com/ali_centos/zd_hub:gpt-service

@echo ================build success================