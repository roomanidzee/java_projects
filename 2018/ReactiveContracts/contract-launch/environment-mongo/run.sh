#!/bin/bash
set -m
 
mongodb_cmd="mongod --storageEngine $STORAGE_ENGINE"

echo "Первоначальная команда запуска MongoDB: $mongodb_cmd .."

cmd="$mongodb_cmd --httpinterface --rest --master"

echo "Дополняем команду доступом через REST: $cmd ..."

if [ "$AUTH" == "yes" ]; then
	echo "Добавляем возможность авторизации.."
    cmd="$cmd --auth"
fi
 
if [ "$JOURNALING" == "no" ]; then
	echo "Отключаем журналирование.."
    cmd="$cmd --nojournal"
fi
 
if [ "$OPLOG_SIZE" != "" ]; then
	echo "Увеличиваем размер логов.."
    cmd="$cmd --oplogSize $OPLOG_SIZE"
fi
 
$cmd &
 
if [ ! -f /data/db/.configured_mongodb ]; then
    /apply_mongo_config.sh
fi
 
fg