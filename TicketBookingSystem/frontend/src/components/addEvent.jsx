import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Form, Modal } from 'react-bootstrap';

function AddEvent() {
    const token = sessionStorage.getItem('token');
    const config = {
        headers: { Authorization: `Bearer ${token}` }
    };
    const [userData, setUserData] = useState(null);

    useEffect(() => {
        
        console.log('Bearer ' + token);

        const getUserData = async (token) => {
            let api_endpoint_url = 'http://localhost:8080/user/getLoggedInUser';
            
            const bodyParameters = {
                key: "value"
            };
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

    const [show, setShow] = useState(false);
    const [formData, setFormData] = useState({
        event_name: '',
        venue: '',
        description: '',
        date_time: '',
        event_type: '',
        number_of_ticket: '',
        ticket_price: ''
    });

    const handleClose = () => setShow(false);
    const handleShow = () => setShow(true);

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setFormData({
            ...formData,
            [name]: value,
        });
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        console.log('Form data submitted:', formData);
        // Add your authentication logic here, e.g., send data to an API
        send_onsubmit(formData.event_name, formData.venue, formData.description, formData.date_time, formData.event_type, formData.status, formData.number_of_ticket, formData.ticket_price)
    };

    const send_onsubmit = (event_name, venue, description, date_time, event_type, number_of_ticket, ticket_price) => {
        let api_endpoint_url = 'http://localhost:8080/event/createEvent';
        let data = {
            eventName: event_name,
            venue: venue,
            description: description,
            dateTime: date_time,
            event_type: event_type,
            eventManagerName: userData.user.name
        }
        console.log(data);

        axios.post(api_endpoint_url, data, config)
            .then(function (response) {
                var data = response.data;
                var status = response.request.status;
                console.log(data);  
            })
            .catch(function (error) {
                console.error('Error occurred:', error);
                // Handle error here
            });
    }

    return(
        <>

            <li className="nav-item"><a className="nav-link" href="#!" onClick={handleShow}> <i className="fas fa-plus me-1 text-gray fw-normal"></i>Add Event</a></li>
            <Modal show={show} onHide={handleClose}>
                <Modal.Header closeButton>
                    <Modal.Title>Add Event</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Form onSubmit={handleSubmit}>
                        <Form.Group className="mb-3" controlId="formBasicEventName">
                            <Form.Label>Event Name</Form.Label>
                            <Form.Control type="text" placeholder="Enter event name" name="event_name" onChange={handleInputChange} required />
                        </Form.Group>
                        <Form.Group className="mb-3" controlId="formBasicVenue">
                            <Form.Label>Venue</Form.Label>
                            <Form.Control type="text" placeholder="Enter venue" name="venue" onChange={handleInputChange} required />
                        </Form.Group>
                        <Form.Group className="mb-3" controlId="formBasicDescription">
                            <Form.Label>Description</Form.Label>
                            <Form.Control type="text" placeholder="Enter description" name="description" onChange={handleInputChange} required />
                        </Form.Group>
                        <Form.Group className="mb-3" controlId="formBasicDateTime">
                            <Form.Label>Date and Time</Form.Label>
                            <Form.Control type="datetime-local" placeholder="Enter date and time" name="date_time" onChange={handleInputChange} required />
                        </Form.Group>
                        <Form.Group className="mb-3" controlId="formBasicEventType">
                            <Form.Label>Event Type</Form.Label>
                            <Form.Control type="text" placeholder="Enter event type" name="event_type" onChange={handleInputChange} required />
                        </Form.Group>
                        <button type="submit" className="btn btn-primary">Submit</button>
                    </Form>
                </Modal.Body>
            </Modal>

        </>
    )
    
}

export default AddEvent;
