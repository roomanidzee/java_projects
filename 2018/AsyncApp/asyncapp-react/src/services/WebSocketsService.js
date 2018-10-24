import React from "react";
import SockJS from 'sockjs-client';
import Stomp from 'stompjs';

const config = require('config-yml');

export default class WebSocketsService extends React.Component{

    constructor(props){
        super(props);

        this.state = {
            microservicesBaseURL: config.microservices.base_url,
            websocketsBaseURL: config.microservices.rabbit_service.websockets.base_url
        }

    }

    giveCatToUser(username){

        const socket = new SockJS(this.state.microservicesBaseURL + this.state.websocketsBaseURL);
        const stompClient = Stomp.overWS(socket);

        stompClient.connect({}, (frame) => {

            console.log('Подключено: ' + frame);

            const responseString = config.microservices.rabbit_service.websockets.response_url + '/' + username + '/reply';

            stompClient.send(config.microservices.rabbit_service.websockets.request_url + '/' + username, {});

            stompClient.subscribe(responseString, (response) => {

                if(!response){
                    console.error("Неверный ответ: " + response);
                }

                return JSON.parse(response);

            });

        });

    }

}