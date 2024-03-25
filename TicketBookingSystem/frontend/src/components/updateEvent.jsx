import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Form, Modal, Alert, Button, Toast } from 'react-bootstrap';

function UpdateEvent(id) {
    const token = localStorage.getItem('token');

    const [show, setShow] = useState(false);
    const [formData, setFormData] = useState({
        event_id: 0,
        event_name: '',
        venue: '',
        description: '',
        date_time: '',
        event_type: '',
        image: null
    });

    const config = {
        headers: { Authorization: `Bearer ${token}` }
    };



    const getEventData = () => {
        let api_endpoint_url = 'http://localhost:8080/event/viewEvent?id=' + id;
        
        try {
            axios.get(api_endpoint_url, config)
                .then(function (response) {
                    var data = response.data;
                    var event = data.event;
                    console.log(data);
                    
                    var date;
                    try{
                        date = new Date(event.dateTime[0], event.dateTime[1] - 1, event.dateTime[2], event.dateTime[3], event.dateTime[4]);
                    }catch(e){
                        date =  new Date(event.dateTime[0], event.dateTime[1] - 1, event.dateTime[2], event.dateTime[3], event.dateTime[4], event.dateTime[5], event.dateTime[6]);
                    }
                    date.setHours(date.getHours() + 8);
                    setFormData({
                        event_id: event.id,
                        event_name: event.eventName,
                        venue: event.venue,
                        description: event.description,
                        date_time: date.toISOString().slice(0, 16),
                        event_type: event.eventType,
                        image: event.image
                    });
                })
                .catch(function (error) {
                    console.error('Error occurred:', error);
                });
        } catch (error) {
            console.error('Error occurred:', error);
        }
    };
    

    const [userData, setUserData] = useState(null);
    const [showAlert, setShowAlert] = useState(false);

    const [showToast, setShowToast] = useState(false);
    const [toastMessage, setToastMessage] = useState('');

    useEffect(() => {
        
        const getUserData = async (token) => {
            let api_endpoint_url = 'http://localhost:8080/user/getLoggedInUser';
            
            const bodyParameters = {
                key: id
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

    const handleClose = () => setShow(false);
    const handleShow = () => {
        setShow(true);
        getEventData();
    };

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
        send_onsubmit(formData.event_id,formData.event_name, formData.venue, formData.description, formData.date_time, formData.event_type, formData.image);
    };

    const send_onsubmit = (event_id, event_name, venue, description, date_time, event_type, image) => {
        let api_endpoint_url = 'http://localhost:8080/event/updateEvent';
        let data = {
            id: event_id,
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
                setShowToast(true);
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
        <Toast onClose={() => setShowToast(false)} style={{zIndex:9999}} show={showToast} delay={3000} autohide>
                <Toast.Header>
                    <strong className="me-auto">Notification</strong>
                </Toast.Header>
                <Toast.Body>{toastMessage}</Toast.Body>
        </Toast>
        {userData && userData.role === 'Event_Manager' &&(
            <Button variant="outline-primary" onClick={handleShow}>
                <i className="fas fa-plus me-1 text-gray fw-normal"></i>Update Event
            </Button>
        )}
            

            <Modal show={show} onHide={handleClose}>
                <Modal.Header closeButton>
                    <Modal.Title>Update Event</Modal.Title>
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
                            <Form.Control type="text" placeholder="Enter event name" name="event_name" value={formData.event_name || ''} onChange={handleInputChange} required />
                        </Form.Group>
                        <Form.Group className="mb-3" controlId="formBasicVenue">
                            <Form.Label>Venue</Form.Label>
                            <Form.Control type="text" placeholder="Enter venue" name="venue" value={formData.venue || ''} onChange={handleInputChange} required />
                        </Form.Group>
                        <Form.Group className="mb-3" controlId="formBasicDescription">
                            <Form.Label>Description</Form.Label>
                            <Form.Control type="text" placeholder="Enter description" name="description" value={formData.description || ''} onChange={handleInputChange} required />
                        </Form.Group>
                        <Form.Group className="mb-3" controlId="formBasicDateTime">
                            <Form.Label>Date and Time</Form.Label>
                            <Form.Control type="datetime-local" placeholder="Enter date and time" name="date_time" value={formData.date_time || ''} onChange={handleInputChange} required />
                        </Form.Group>
                        <Form.Group className="mb-3" controlId="formBasicEventType">
                            <Form.Label>Event Type</Form.Label>
                            <Form.Control type="text" placeholder="Enter event type" name="event_type" value={formData.event_type || ''} onChange={handleInputChange} required />
                        </Form.Group>
                        <button type="submit" className="btn btn-primary">Submit</button>
                    </Form>
                </Modal.Body>
            </Modal>

        </>
    )
    
}

export default UpdateEvent;
