FROM tarantool/tarantool:2
MAINTAINER Andrey Romanov <steampart@gmail.com>

EXPOSE 3301

COPY contract-app.lua /opt/tarantool
CMD ["tarantool", "/opt/tarantool/contract-app.lua"]