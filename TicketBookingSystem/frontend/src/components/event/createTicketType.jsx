import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Modal, Button, Form, InputGroup } from 'react-bootstrap';

function CreateTicketType(id){
    const token = localStorage.getItem('token');
    const config = {
        headers: { Authorization: `Bearer ${token}` }
    };
    const [userData, setUserData] = useState(null);
    const [show, setShow] = useState(false);
    const handleClose = () => setShow(false);
    const handleShow = () => setShow(true);
    const [formData, setFormData] = useState([{ category: '', price: '', number_of_ticket: '', cancellation_fee: ''}]);


    const handleAddClick = () => {
        setFormData([...formData, { category: '', price: '', number_of_ticket: '', cancellation_fee: ''}]);
    };

    const handleDeleteClick = index => {
        const list = [...formData];
        list.splice(index, 1);
        setFormData(list);
    };
    

    useEffect(() => {
        
        const getUserData = async (token) => {
            let api_endpoint_url = 'http://localhost:8080/user/getLoggedInUser';
            
            const bodyParameters = {};
            try {
                const response = await axios.post(api_endpoint_url, bodyParameters, config);
                setUserData(response.data);
            } catch (error) {
                console.error('Error occurred:', error);
            }
        };

        if (token) {
            getUserData(token);
        }
        
    }, []); // Empty dependency array ensures this effect runs only once

    const handleInputChange = (e, index) => {
        const { name, value } = e.target;
        const list = [...formData];
    
        // Check if value is within range for 'cancellation_fee' field
        if (name === 'cancellation_fee') {
            if (value < 0) {
                list[index][name] = 0;
            } else if (value > 100) {
                list[index][name] = 100;
            }  else {
                list[index][name] = value;
            }
            
        } else {
            list[index][name] = value;
        }
    
        setFormData(list);
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        if (formData.length === 0) {
            console.log('No data to submit');
            return;
        }
        console.log('Form data submitted:', formData);
        // Add your authentication logic here, e.g., send data to an API
        // send_onsubmit(formData.email, formData.password)
    };

    const send_onsubmit = (email, password) => {
        // let api_endpoint_url = 'http://localhost:8080/login';
        // let formData = new FormData();
        // formData.append('email', email);
        // formData.append('password', password);
    
        // axios.post(api_endpoint_url, formData)
        //     .then(function (response) {
        //         var data = response.data;
        //         var status = response.request.status;
        //         console.log(data);

        //         if (status !== 200) {
        //             console.log(response);
        //             console.log("here");

        //         } else {
        //             console.log(response);
        //             localStorage.setItem('token', data.accessToken);
        //             // sessionStorage.setItem('token', data.accessToken);
        //             window.location.reload(false);
        //         }
        //     })
        //     .catch(function (error) {
        //         console.error('Error occurred:', error);
        //         // Handle error here
        //     });
    }

    return(
        <>
        {userData && userData.role === 'Event_Manager' &&(
            <Button variant="danger" onClick={handleShow}>
                <i className="fas fa-plus me-1 text-gray fw-normal"></i>Create Ticket Type
            </Button>
        )}
        <Modal show={show} onHide={handleClose}>
            <Modal.Header>
                <Modal.Title>Create Ticket Type</Modal.Title>
            </Modal.Header>

            <Modal.Body>
                <Form onSubmit={handleSubmit}>
                    {formData.map((item, index) => (

                        <Form.Group className="mb-3" controlId={`ticketType${index}`} key={index}>
                            <h5>Add new Ticket {index+1}:</h5>
                            <Form.Label>Event Category:</Form.Label>
                            <Form.Control type="text" placeholder="Enter category name" name="category" value={item.category} onChange={e => handleInputChange(e, index)} required/>

                            <Form.Label>Event Price:</Form.Label>
                            <Form.Control type="number" placeholder="Enter price" name="price" value={item.price} onChange={e => handleInputChange(e, index)} required/>
                        
                            <Form.Label>Number of ticket:</Form.Label>
                            <Form.Control type="number" placeholder="Enter number of ticket" name="number_of_ticket" value={item.number_of_ticket} onChange={e => handleInputChange(e, index)} required/>
                            
                            <Form.Label> Cancellation fee (Percentage)</Form.Label>

                            <InputGroup>
                                <Form.Control type="number" min="0" max="100" placeholder="Enter cancellation fee" name="cancellation_fee" value={item.cancellation_fee} onChange={e => handleInputChange(e, index)} required/>
                                <InputGroup.Text id="basic-addon1">%</InputGroup.Text>
                            </InputGroup>

                            <br></br>
                            <Button variant="danger" onClick={() => handleDeleteClick(index)}>Delete</Button>
                            <hr/>
                        </Form.Group>
                    ))}
                    <div className="d-flex justify-content-between">
                        <Button onClick={handleAddClick}>Add new ticket type</Button>
                        <Button variant="primary" type="submit">
                            Create
                        </Button>
                    </div>
  
                </Form>
            </Modal.Body>
        </Modal>
        
        </>
    )
}

export default CreateTicketType;