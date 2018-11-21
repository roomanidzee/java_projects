#!/bin/bash
 
# Администратор
MONGODB_ADMIN_USER=${MONGODB_ADMIN_USER}
MONGODB_ADMIN_PASS=${MONGODB_ADMIN_PASS}
 
# Обычный пользователь БД
MONGODB_APPLICATION_DATABASE=${MONGODB_APPLICATION_DATABASE}
MONGODB_APPLICATION_USER=${MONGODB_APPLICATION_USER}
MONGODB_APPLICATION_PASS=${MONGODB_APPLICATION_PASS}
 
# Ожидаем запуск MongoDB
RET=1
while [[ RET -ne 0 ]]; do
    echo "=> Ждём подтверждения старта сервиса MongoDB..."
    sleep 5
    mongo admin --eval "help" >/dev/null 2>&1
    RET=$?
done
 
# Создаём администратора
echo "=> Создаём пользователя с ролью admin в MongoDB"
mongo admin --eval "db.createUser({user: '$MONGODB_ADMIN_USER', pwd: '$MONGODB_ADMIN_PASS', roles:[{role:'root',db:'admin'}]});"
 
sleep 3

if [ "$MONGODB_APPLICATION_DATABASE" != "admin" ]; then
    echo "=> Создаём базу данных ${MONGODB_APPLICATION_DATABASE} для пользователя ${MONGODB_APPLICATION_USER} с паролем ${MONGODB_APPLICATION_PASS}"
    mongo admin -u $MONGODB_ADMIN_USER -p $MONGODB_ADMIN_PASS << EOF
use $MONGODB_APPLICATION_DATABASE
db.createUser({user: '$MONGODB_APPLICATION_USER', pwd: '$MONGODB_APPLICATION_PASS', roles:[{role:'dbOwner', db:'$MONGODB_APPLICATION_DATABASE'}]})
EOF
fi
 
sleep 1

echo "=> Готово!"
touch /data/db/.configured_mongodb
 
echo "========================================================================"
echo "Можете подключиться к серверу MongoDB как админ:"
echo ""
echo "    mongo admin -u $MONGODB_ADMIN_USER -p $MONGODB_ADMIN_PASS --host <host> --port <port>"
echo ""
echo "Или как пользователь: "
echo ""
echo "    mongo $MONGODB_APPLICATION_DATABASE -u $MONGODB_ADMIN_USER -p $MONGODB_ADMIN_PASS --host <host> --port <port>"
echo ""
echo "========================================================================"