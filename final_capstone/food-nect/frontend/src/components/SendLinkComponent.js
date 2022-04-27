import React, { Component } from 'react';
import {Button, Row, Col, Label } from 'reactstrap';
import { Control, LocalForm, Errors } from 'react-redux-form';

import styles from './SendLinkComponent.css';

const required = (val) => val && val.length;
const maxLength = (len) => (val) => !(val) || (val.length <= len);
const minLength = (len) => (val) => val && (val.length >= len);

/**
 * 
 * @param {*} props are:
 *  @array 'rows' is how many guests there are
 *  @function 'remove' lets you remove a row from the inside
 *  @function 'handleSubmit'
 * 
 */

function RenderGuestLinks(props) {
    
    let guestLinks = props.rows.map( (row, index) => {
        return (
            <div className='row m-2' key={index}>
                
                <Button className='text-light bg-danger col-1' onClick={() => props.remove(index)}>
                    X
                </Button>
                
                <LocalForm onSubmit={(values) => props.handleSubmit(values, index)} className='container col-11'>
                    <Row className="form-group row">
                        
                        <Col md={2}>
                            <Label htmlFor="name" >Guest #{index+1}</Label>
                        </Col>
                        <Col md={2}>
                            <Control.text model=".name" id="name" name="name"
                                placeholder="Name"
                                className="form-control"
                                validators={{
                                    required, minLength: minLength(3), maxLength: maxLength(15)
                                }}
                                    />
                            <Errors
                                className="text-danger"
                                model=".name"
                                show="touched"
                                messages={{
                                    required: 'Required ',
                                    minLength: 'Must be greater than 2 characters',
                                    maxLength: 'Must be 15 characters or less'
                                }}
                                />
                        </Col>

                        <Col md={2}>
                            <Control.text model=".username" id="username" name="username"
                                placeholder="Username (optional)"
                                className="form-control"
                                />
                        </Col>

                        <Col md={3}>
                            <Control.text model=".email" id="email" name="email"
                                placeholder="Email (optional)"
                                className="form-control"
                                />
                        </Col>

                        <Col md={2}>
                            <Button type="submit" color="primary">
                                Create Link
                            </Button>
                        </Col>
                    </Row>
                </LocalForm>
            </div>
        )
    })
    //console.log(guestLinks)
    return(
        <>
            {guestLinks}     
        </>
    )
}

function RenderDecideDate(props) {
    return(
        <form>
            <label>Decide by: 
                <input 
                    type="date"
                    onChange={(e) => props.setDecideDate(e.target.value)}
                />
            </label>

        </form>
    )
}

/**
 * props: 
 *  @property {boolean} 'trigger' makes the popup display or not
 *  @property {function} 'setTrigger' lets SendLink edit 'trigger' from the inside
 *  @property {string} 'baseUrl' need this so we know where to send the data
 *  @example
 *      it will POST this to server
 * 
 *      const newLink = {
            name: value.name,
            username: value.username,
            email: value.email,
            decideDate: decideDate,
            id: index,
            restaurants: restaurants
        }
    @property {array} 'restaurants' needs an array of resturant ID's
 */

export default class SendLink extends Component {
    constructor(props) {
        super(props);

        this.state = {
            inputRows:[Date.now],
            decideDate: null
        };
    }
////////////////////////////////////////////////////////////
/////////////////for <RenderGuestLinks>
////////////////////////////////////////////////////////////
    addGuestRow = () => {
        this.setState({
            inputRows:[...this.state.inputRows, Date.now],
            decideDate: this.state.decideDate
        });
    }

    removeGuestRow = (index) => {
        let temp = this.state.inputRows;
        temp.splice(index,1);
        this.setState({
            inputRows: temp,
            decideDate: this.state.decideDate
        })
    }

    resetGuest = () => {
        this.setState({
            inputRows:[""],
            decideDate: this.state.decideDate
        });
    }
////////////////////////////////////////////////////////////
/////////////////for <RenderDateSelect>
////////////////////////////////////////////////////////////
    setDecideDate = (date) => {
        console.log(this.state.inputRows);
        this.setState({
            inputRows:this.state.inputRows,
            decideDate: date
        });
    }

////////////////////////////////////////////////////////////
/////////////////general functions
////////////////////////////////////////////////////////////
    handleClose = () => {
        this.props.setTrigger(false)
        this.resetGuest();
    }

    handleSubmit = (value, guestIndex) => {
        console.log(`Current State is:  name ${value.name} username ${value.username} name ${value.email}`);
        alert('Current State is: ' + JSON.stringify(value) + '\n' 
            + 'date is: ' + this.state.decideDate + '\n' 
            + 'index is ' + guestIndex);
        //event.preventDefault();
        this.postLink(value, this.state.decideDate, guestIndex, this.props.baseUrl, this.props.restaurants);

    }

    postLink = (value, decideDate, index, baseUrl, restaurants) => {
        const newLink = {
            name: value.name,
            username: value.username,
            email: value.email,
            decideDate: decideDate,
            id: index,
            restaurants: restaurants
        }

        return fetch(baseUrl, {
            method: "POST",
            body: JSON.stringify(newLink),
            headers: {
              "Content-Type": "application/json"
            },
            credentials: "same-origin"
        })
        .then(response => {
            if (response.ok) {
              return response;
            } else {
              var error = new Error('Error ' + response.status + ': ' + response.statusText);
              error.response = response;
              throw error;
            }
          },
          error => {
                throw error;
          })
        .then(response => response.json())
        .catch(error =>  { console.log('POST links', error.message); alert('Your invite could not be sent\nError: '+error.message); });

    }

    render() {
        //console.log(this.state);
        // console.log("commentDialog props:")
        // console.log(this.props);
        return (this.props.trigger) ? (
            <div className="popup">
                <div className="popup-inner container">
                    <div>
                        <Button className='close-btn' onClick={this.handleClose}>close</Button>
                    </div>
                    <div className='row'>
                        <h3 className='col-8 offset-4'>
                            <RenderDecideDate setDecideDate={this.setDecideDate}/>
                        </h3>
                        <RenderGuestLinks className='col-12' rows={this.state.inputRows} remove={this.removeGuestRow} handleSubmit={this.handleSubmit}/>
                        <Button className='col-12' onClick={this.addGuestRow} >add guest</Button>
                        <p className='col-12 col-md-5'>
                            Instructions
                        </p>
                        <p className='col-12 col-md-5'>
                            Instructions    
                        </p>
                    </div>
                    
                </div>
            </div>
            
        ) : "";
    }
}