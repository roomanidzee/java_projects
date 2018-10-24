import React from "react";

import Container from 'muicss/lib/react/container';
import Row from 'muicss/lib/react/row';
import Col from 'muicss/lib/react/col';

import NavigationBar from "./NavigationBar";
import Input from "muicss/lib/react/input";
import Button from "muicss/lib/react/button";
import Form from "muicss/lib/react/form";
import RESTAPIService from "../services/RESTAPIService";

export default class UsersPage extends React.Component{

    constructor(props){

        super(props);

        this.state = {
            usersList: [],
            username: '',
            password: '',
            token: '',
            showUsers: false,
            isLogged: false
        };

        this.handleChangeUsername = this.handleChangeUsername.bind(this);
        this.handleChangePassword = this.handleChangePassword.bind(this);

        this.handleUsers = this.handleUsers.bind(this);

    }

    handleChangeUsername(event){
        this.setState({username: event.target.value});
    }

    handleChangePassword(event){
        this.setState({password: event.target.value});
    }

    handleUsers(){

        const restAPIService = new RESTAPIService();

        restAPIService.login(this.state.username, this.state.password)
                      .then(responseJSON => {

                          this.setState({
                              token: responseJSON.authtoken.value,
                              isLogged: true,
                              showUsers: true
                          });

                      })
                      .catch(error => {
                          console.error("Произошла ошибка: " + error);
                          throw error;
                      });

        restAPIService.setToken(this.state.token);

        restAPIService.getAllUsers()
                      .then(responseJSON => this.setState({usersList: responseJSON}))
                      .catch(error => {
                          console.error("Произошла ошибка: " + error);
                          throw error;
                      });


    }

    render(){

        const formLogin =
            <Form onSubmit={this.handleFormSubmit}>
                <legend className="mui--text-center mui--align-middle">Форма входа</legend>
                <Input type="text"
                       value = {this.state.username}
                       onChange = {this.handleChangeUsername}
                       placeholder = "Имя пользователя"/>
                <Input type="password"
                       value = {this.state.password}
                       onChange = {this.handleChangePassword}
                       placeholder = "Пароль пользователя"/>
                <Button type="submit" className="mui--text-center mui--align-middle"
                       color="primary">Войти</Button>
            </Form>;

        const showUsersItem =
            <Container fluid={true}>

                {this.state.usersList.map(function(item, index){

                    return(
                        <Row>
                            <Col md="6" className="mui--text-center mui--align-middle">{item.username}</Col>
                            <Col md="6" className="mui--text-center mui--align-middle">{item.image_url}</Col>
                        </Row>
                    );

                })}

            </Container>;


        return (
            <Container fluid={true}>

                <NavigationBar/>

                <h3 className="mui--text-center mui--align-middle">Список пользователей</h3>

                {!this.state.isLogged ? formLogin : null}

                {this.state.showUsers ? showUsersItem : null}

            </Container>
        );

    }

}