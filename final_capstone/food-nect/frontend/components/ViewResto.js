import React, { Component } from "react";
import { Card, CardImg, CardImgOverlay,
    CardTitle, CardBody, CardText, Breadcrumb, BreadcrumbItem, Input, Badge } from 'reactstrap';
import { Container, Button, Label, Col, Row } from 'reactstrap';
import { Control, Form, Errors, LocalForm } from 'react-redux-form';

// import { Link } from 'react-router-dom';
// import { Control, Errors } from 'react-redux-form';

function RenderResto({resto, index}) {
   
    if (resto !== null) {
        console.log("renderresto")
        console.log("resto inside render funct: " + JSON.stringify(resto))
        return (
            <div className="carddiv" >
                <Col sm="7">
                <Card key={index} body color="warning" inverse>
                    <CardImg className="cardImg" width="100%" src={resto.image} alt={resto.name} />
                    <hr />
                    <CardBody className="text-center">
                        <CardTitle className="cardTitle" tag="h5">{resto.name}</CardTitle>

                        {resto.hrsoperation !== "" ?
                            <Badge color="success" pill>Open Now</Badge>
                        :   <Badge color="danger" pill>Closed</Badge>}

                        <CardText>Restaurant Type:  {resto.type}</CardText>
                        <CardText>Complete Address: {resto.address}</CardText>
                        <CardText>Hours of Operation: {resto.hrsoperation}</CardText>
                
                        {resto.telnum !== "" ? 
                            <>
                                <CardText>Contact #: {resto.telnum}</CardText> 
                                <Button color="primary">Call To Order</Button>
                            </>
                        : ""}
                    </CardBody>
                </Card>
                </Col>
            </div>
        );
    }
    else {
        return (
            <div></div>
        );
    }
}

export default class ViewResto extends Component {

    constructor(props) {
        super(props);

        this.state = {
            resto: this.props.restaurants
        }
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleSubmit(values) {
        console.log("enter filter function")
        const filteredResto = this.props.restaurants.filter(resto => {return resto.type === values.restoType} )
        console.log(filteredResto)
        this.setState({resto: filteredResto});
    }
    
    render() {

        const restaurant = this.state.resto.map((resto, index) => {
            return (
                <RenderResto resto={resto} index={index}/>
            );
        });

        return(
            <Container className="container">
                    <LocalForm onSubmit={(values) => this.handleSubmit(values)}>
                        
                        <Row>
                        <Col>
                        <Label>Filter by Restaurant Type:</Label>
                        </Col>
                        <Col>
                            <Control.select model='.restoType' name='restoType'
                                        className='form-control'>
                                <option>Select Restaurant Type</option>            
                                <option>Carinderia</option>
                                <option>Fine Dining</option>
                                <option>Fast Food</option>
                                
                            </Control.select>
                            </Col>
                            <Col>
                            <Button type="submit" color="primary">Filter</Button>
                            </Col>
                            </Row>
                        
                    </LocalForm>

                     {restaurant}
                     {console.log(restaurant)}
             </Container>
            //</Col>
            //     </Row>
            // </div>
        );
    }
}