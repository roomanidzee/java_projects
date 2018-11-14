FROM mongo:3.4-jessie
MAINTAINER Andrey Romanov <steampart@gmail.com>

ENV AUTH yes
ENV STORAGE_ENGINE wiredTiger
ENV JOURNALING yes
 
ADD run.sh /run.sh
ADD apply_mongo_config.sh /apply_mongo_config.sh
 
CMD ["/run.sh"]