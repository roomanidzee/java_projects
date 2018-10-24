import React from "react";
import Container from 'muicss/lib/react/container';
import Button from 'muicss/lib/react/button';
import {Link} from "react-router-dom";
import NavigationBar from "./NavigationBar";

export default class HomePage extends React.Component{

    render(){

        return (

            <Container fluid = {true}>

                <NavigationBar/>

                <Container fluid = {true}>

                    <h3 className="mui--text-center mui--align-middle">Стартовая страница</h3>

                    <Link to="/cat_receive">
                        <Button className="mui--text-center mui--align-middle"
                                color="primary">Перейти к странице получения кота</Button>
                    </Link>

                </Container>

            </Container>

        );

    }

}
