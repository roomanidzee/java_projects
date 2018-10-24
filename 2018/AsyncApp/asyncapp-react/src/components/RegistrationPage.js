import React from "react";

import Form from 'muicss/lib/react/form';
import Input from 'muicss/lib/react/input';
import Container from 'muicss/lib/react/container';
import Button from 'muicss/lib/react/button';
import NavigationBar from "./NavigationBar";
import WebSocketsService from "../services/WebSocketsService";
import RESTAPIService from "../services/RESTAPIService";

export default class RegistrationPage extends React.Component{

    constructor(props){

        super(props);

        this.state = {
            username: '',
            password: '',
            imageURL: ''
        };

        this.handleChangeUsername = this.handleChangeUsername.bind(this);
        this.handleChangePassword = this.handleChangePassword.bind(this);
        this.handleChangeImageURL = this.handleChangeImageURL.bind(this);
        this.handleFormSubmit = this.handleFormSubmit.bind(this);

    }

    handleChangeUsername(event){
        this.setState({username: event.target.value});
    }

    handleChangePassword(event){
        this.setState({password: event.target.value});
    }

    handleChangeImageURL(event){
        this.setState({imageURL: event.target.value});
    }

    handleFormSubmit(){

        const websocketService = new WebSocketsService();
        const restAPIService = new RESTAPIService();

        restAPIService.register(this.state.username, this.state.password)
                      .catch(error => {
                         console.error("Произошла ошибка: " + error);
                         throw error;
                      });

        restAPIService.giveCatToUser(this.state.username, this.state.imageURL)
                      .catch(error => {
                         console.error("Произошла ошибка: " + error);
                         throw error;
                      });

        const result = websocketService.giveCatToUser(this.state.username);

        if(!result){
            throw new Error("Не был обработан запрос с помощью WebSockets");
        }

        alert("Вы зарегистрированы! У вас вот такой котёнок: " + this.state.imageURL);

    }

    render(){

        return (

            <Container fluid = {true}>

                <NavigationBar/>

                <Container fluid = {true}>

                    <h3 className="mui--text-center mui--align-middle">Страница регистрации</h3>

                    <Form onSubmit={this.handleFormSubmit}>
                        <legend className="mui--text-center mui--align-middle">Форма регистрации</legend>
                        <Input type="text"
                               value = {this.state.username}
                               onChange = {this.handleChangeUsername}
                               placeholder = "Имя пользователя"/>
                        <Input type="password"
                               value = {this.state.password}
                               onChange = {this.handleChangePassword}
                               placeholder = "Пароль пользователя"/>
                        <Input type="password"
                               value = {this.state.password}
                               onChange = {this.handleChangeImageURL}
                               placeholder = "URL с картинкой кота"/>
                        <Button type="submit" className="mui--text-center mui--align-middle"
                                color="primary">Зарегистрироваться</Button>
                    </Form>

                </Container>

            </Container>

        );

    }

}