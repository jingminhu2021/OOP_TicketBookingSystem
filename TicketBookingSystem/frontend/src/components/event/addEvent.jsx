import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Form, Modal, Alert, Button } from 'react-bootstrap';

function AddEvent() {
    // const token = sessionStorage.getItem('token');
    const token = localStorage.getItem('token');
    const config = {
        headers: { Authorization: `Bearer ${token}` }
    };
    const [userData, setUserData] = useState(null);
    const [showAlert, setShowAlert] = useState(false);
    const [toastMessage, setToastMessage] = useState('');

    useEffect(() => {
        
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
    const [show2, setShow2] = useState(false);
    const [formData, setFormData] = useState({
        event_name: '',
        venue: '',
        description: '',
        date_time: '',
        event_type: '',
        image: null
    });

    const handleClose = () => setShow(false);
    const handleShow = () => setShow(true);

    const handleClose2 = () => setShow2(false);
    const handleShow2 = () => setShow2(true);

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setFormData({
            ...formData,
            [name]: value,
        });
    };

    const handleImageChange = (e) => {
        const file = e.target.files[0];
    
        // Check if file is an image
        if (file && file.type.startsWith('image/')) {
            const reader = new FileReader();
    
            reader.onloadend = () => {
                setFormData({
                    ...formData,
                    image: reader.result
                });
            };
    
            reader.readAsDataURL(file);
            setShowAlert(false);
        } else {
            setShowAlert(true);
        }
    };


    const handleSubmit = (e) => {
        e.preventDefault();
        console.log('Form data submitted:', formData);
        // Add your authentication logic here, e.g., send data to an API
        send_onsubmit(formData.event_name, formData.venue, formData.description, formData.date_time, formData.event_type, formData.image);
    };

    const send_onsubmit = (event_name, venue, description, date_time, event_type, image) => {
        let api_endpoint_url = 'http://localhost:8080/event/createEvent';
        let data = {
            eventName: event_name,
            venue: venue,
            description: description,
            dateTime: date_time,
            eventType: event_type,
            eventManagerName: userData.user.name,
            image: image
        }
        console.log(data);

        axios.post(api_endpoint_url, data, config)
            .then(function (response) {
                var data = response.data;
                
                setToastMessage(data.message);
                setShow2(true);
                if (data.status === true) {
                    handleClose();
                }
                formData.image = null;
            })
            .catch(function (error) {
                console.error('Error occurred:', error);
                // Handle error here
            });
    }

    return(
        <>

        {userData && userData.role === 'Event_Manager' &&(
            <Button variant="outline-primary w-100" onClick={handleShow}>
                <i className="fas fa-plus me-1 text-gray fw-normal"></i>Add New Event
            </Button>
        )}
            

            <Modal show={show} onHide={handleClose}>
                <Modal.Header closeButton>
                    <Modal.Title>Add New Event</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Form onSubmit={handleSubmit}>
                        {showAlert && <Alert variant="danger">File is not an image.</Alert>}
                        <Form.Group className="mb-3" controlId="formBasicImage">
                            
                            <Form.Label>Image</Form.Label>
                            <div style={{ display: 'flex', justifyContent: 'center' }}>
                                {formData.image && <img src={formData.image} alt="Preview" style={{width: '200px', height: '200px'}} />}
                            </div>
                            <br></br>
                            <Form.Control type="file" name="image" onChange={handleImageChange} />
                        </Form.Group>
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

            <Modal show={show2} onHide={handleClose2}>
                <Modal.Header closeButton>
                    <Modal.Title>Notification</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    {toastMessage}
                </Modal.Body>
            </Modal>

        </>
    )
    
}

export default AddEvent;
