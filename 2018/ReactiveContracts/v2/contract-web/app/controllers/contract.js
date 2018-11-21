const amqp = require('amqplib/callback_api');

exports.indexPage = (req, resp) => resp.render('index_page', {title: 'Время возвращать долги'});

exports.contractSend = (req, resp) => {

    const formBody = req.body;

    const queue = process.env.AMQP_QUEUE;

    amqp.connect(process.env.AMQP_ADDRESS, (error, connection) =>{

        if(error){
            console.error("Произошла ошибка: " + error);
        }

        connection.createChannel((error, channel) =>{

            channel.assertQueue(queue);

            for(let i = 0; i < req.body.contract_duration; i++){
                channel.sendToQueue(queue, Buffer.from((JSON.stringify(formBody))));
            }

        });

    });

    resp.sendStatus(200);

};