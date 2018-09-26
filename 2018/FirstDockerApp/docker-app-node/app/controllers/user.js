const amqp = require('amqplib/callback_api');

exports.indexPage = (req, resp) => resp.render('index_page', {title: 'Регистрация'});

exports.registerUser = (req, resp) => {

    const formBody = req.body;

    const queue = process.env.AMQP_QUEUE;

    amqp.connect(process.env.AMQP_ADDRESS, (error, connection) =>{

         if(error){
            console.error("Произошла ошибка: " + error);
            resp.sendStatus(400);
         }
      
         connection.createChannel((error, channel) =>{

             channel.assertQueue(queue);
             channel.sendToQueue(queue, new Buffer(JSON.stringify(formBody)));

         });

    });

    resp.sendStatus(200);

};