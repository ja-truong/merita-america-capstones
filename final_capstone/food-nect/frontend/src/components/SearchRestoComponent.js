import React, { Component } from "react";
import { Card, CardImg, CardImgOverlay,
    CardTitle, CardBody, CardText, Breadcrumb, BreadcrumbItem, Input, Badge } from 'reactstrap';
import { Container, Button, Label, Col, Row } from 'reactstrap';
import { Control, Form, Errors, LocalForm } from 'react-redux-form';
import { Link } from 'react-router-dom';

const validSearchString = (val) => /^[A-Z0-9]/i.test(val);

export default class SearchResto extends Component {

constructor(props) {
    super(props);
    this.state = {
        restaurants: this.props.restaurants,
        randomJson: null,
        isLoaded: false,
        resto: null
    }
    this.handleSubmit = this.handleSubmit.bind(this);
}

handleSubmit(values) {

    console.log("handle sub enter")
    console.log("values.searchField: " + values.searchField)
    console.log("validSearchString(values.searchField): " + validSearchString(values.searchField))
    console.log(new Date())

    if(validSearchString(values.searchField)) {

        fetch(`http://localhost:8081/foodnect/restaurants/search?completeAddress=${values.searchField}`,
            {mode: 'no-cors'})
                    .then((res) => res.json())
                    .then((json) => {
                        this.setState({
                            randomJson: json,
                            isLoaded: true
                        });
                    })
    } else {
        alert("Invalid search string entered!");
    }
}

render() {

    return (
        <Container>

           <LocalForm onSubmit={(values) => this.handleSubmit(values)}>
               <Row>
                    <Col>
                        <Label>Search by zip code or city:</Label>
                    </Col>
                    <Col>
                        <Control.text model='.searchField' name='searchField'
                            className='form-control'>
                        </Control.text>
                    </Col>
                    <Col>

                       <Button className="mr-2" type="submit" color="primary">Search</Button>
                       {console.log("from file: " + this.state.randomJson)}
                    </Col>
               </Row>
           </LocalForm>


        </Container>
    );
}
}