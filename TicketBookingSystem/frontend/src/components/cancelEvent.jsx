import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Modal, Button, Toast } from 'react-bootstrap';

function cancelEvent(id){
    const token = localStorage.getItem('token');
    const config = {
        headers: { Authorization: `Bearer ${token}` }
    };
    const [show, setShow] = useState(false);
    const [show2, setShow2] = useState(false);

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
    const handleShow = () => setShow(true);
    const handleClose2 = () => setShow2(false);
    const handleShow2 = () => setShow2(true);

    const handleCancel = async () => {
        let api_endpoint_url = 'http://localhost:8080/event/cancelEvent';
        const bodyParameters = {
            event_id: id
        };
        try {
            const response = await axios.post(api_endpoint_url, bodyParameters, config);
            console.log(response.data);
            handleShow2();
        } catch (error) {
            console.error('Error occurred:', error);
        }
    }

    return(
        <>
        {userData && userData.role === 'Event_Manager' &&(
            <Button variant="outline-primary" onClick={handleShow}>
                <i className="fas fa-plus me-1 text-gray fw-normal"></i>Cancel Event
            </Button>
        )}
        <Modal show={show} onHide={handleClose}>
            <Modal.Header closeButton>
                <Modal.Title>Cancel Event</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <p>Are you sure you want to cancel this event? <br> Note: All purchase tickets will be refunded.</br></p>
            </Modal.Body>
            <Modal.Footer>
                <Button variant="secondary" onClick={handleClose}>
                    Close
                </Button>
                <Button variant="primary" onClick={handleCancel}>
                    Cancel Event
                </Button>
            </Modal.Footer>
        </Modal>
        
        <Modal show={show2} onHide={handleClose2}>
            <Modal.Header closeButton>
                <Modal.Title>Event Cancelled</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <p>Event has been cancelled successfully.</p>
            </Modal.Body>
            <Modal.Footer>
                <Button variant="primary" onClick={handleClose2}>
                    Close
                </Button>
            </Modal.Footer>
        </Modal>
        </>
    )
}

export default cancelEvent;