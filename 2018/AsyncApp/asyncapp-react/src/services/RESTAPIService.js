import React from "react";

const config = require('config-yml');

export default class RESTAPIService extends React.Component{

    constructor(props){

        super(props);

        this.state = {
            apiURL: config.microservices.base_url
        };

    }

    setToken(token){
        this.setState({authToken: token});
    }

    getAllUsers(){

        const requestHeaders = {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'x-access-token': this.token
        };

        return fetch(this.state.apiURL + config.microservices.db_service.get_users, {

            method: 'POST',
            headers: requestHeaders

        })
        .then((response) => {
            return response.json();
        })
        .catch(error => {
            console.log("error occurred: " + error.message);
            throw error;
        });

    }

    register(username, password){

        const jsonBody = {
            "username": username,
            "password": password
        };

        const requestHeaders = {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        };

        return fetch(this.state.apiURL + config.microservices.rabbit_service.registration, {

            method: 'POST',
            headers: requestHeaders,
            body: jsonBody

        })
        .then((response) => {
            return response.json();
        })
        .catch(error => {
            console.log("error occurred: " + error.message);
            throw error;
        });

    }

    login(username, password){

        const jsonBody = {
            "username": username,
            "password": password
        };

        const requestHeaders = {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        };

        return fetch(this.state.apiURL + config.microservices.db_service.login, {

            method: 'POST',
            headers: requestHeaders,
            body: jsonBody

        })
        .then((response) => {
            return response.json();
        })
        .catch(error => {
            console.log("error occurred: " + error.message);
            throw error;
        });

    }

    getCatPicture(){

        const requestHeaders = {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        };

        return fetch(this.state.apiURL + config.microservices.cat_service.search_cat, {

            method: 'GET',
            headers: requestHeaders

        })
        .then((response) => {
            return response.json();
        })
        .catch(error => {
            console.log("error occurred: " + error.message);
            throw error;
        });

    }

    saveCatToDB(id, url){

        const jsonBody = {
            "id": id,
            "url": url
        };

        const requestHeaders = {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        };

        return fetch(this.state.apiURL + config.microservices.db_service.add_cat, {

            method: 'POST',
            headers: requestHeaders,
            body: jsonBody

        })
        .then((response) => {
            return response.json();
        })
        .catch(error => {
            console.log("error occurred: " + error.message);
            throw error;
        });

    }

    giveCatToUser(username, pictureURL){

        const jsonBody = {
            "username": username,
            "image_url": pictureURL
        };

        const requestHeaders = {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        };

        return fetch(this.state.apiURL + config.microservices.rabbit_service_user_with_cat, {

            method: 'POST',
            headers: requestHeaders,
            body: jsonBody

        })
        .then((response) => {
            return response.json();
        })
        .catch(error => {
            console.log("error occurred: " + error.message);
            throw error;
        });

    }

}