import React from "react";
import Container from "muicss/lib/react/container";
import Button from 'muicss/lib/react/button';
import NavigationBar from "./NavigationBar";
import RESTAPIService from "../services/RESTAPIService";

export default class CatSearchPage extends React.Component{

    constructor(props){

        super(props);

        this.state = {
            imageID: '',
            imageURL: '',
            showCat: false
        };

        this.getImage = this.getImage.bind(this);

    }

    getImage(){

        const restAPIService = new RESTAPIService();

        restAPIService.getCatPicture()
                      .then(responseJSON => {

                          this.setState({
                              imageURL: responseJSON.url,
                              showCat: true
                          });

                      })
                      .catch(error => {
                          console.error("Произошла ошибка: " + error);
                          throw error;
                      })
    }

    saveImage(){

        const restAPIService = new RESTAPIService();

        restAPIService.saveCatToDB(this.state.imageID, this.state.imageURL)
                      .then(responseJSON => {

                          alert("Картинка с URL: " + this.state.imageURL + " сохранена в систему. " +
                              "Скопируйте себе этот URL");

                      })
                      .catch(error => {
                          console.error("Произошла ошибка: " + error);
                          throw error;
                      });

    }

    render(){

        const imgTag = <img alt={"Картиночка"} src={this.state.imageURL}/>;

        return(

            <Container fluid = {true}>

                <NavigationBar/>

                <Container fluid = {true}>

                    <h3 className="mui--text-center mui--align-middle">Получение котика</h3>

                    <Button className="mui--text-center mui--align-middle"
                            color="primary"
                            onClick={this.getImage}>
                        Получить картинку котика
                    </Button>

                    <Button className="mui--text-center mui--align-middle"
                            color="primary"
                            onClick={this.saveImage}>
                        Сохранить картинку котика
                    </Button>

                    {this.state.showCat ? imgTag: null}

                </Container>

            </Container>

        );

    }

}