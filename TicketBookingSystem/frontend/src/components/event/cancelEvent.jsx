import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Modal, Button, Toast } from 'react-bootstrap';

function CancelEvent(props){
    const id = props.event_id;
    const event_status = props.event_status;
    const token = localStorage.getItem('token');
    const config = {
        headers: { Authorization: `Bearer ${token}` }
    };
    const [userData, setUserData] = useState(null);
    const [show, setShow] = useState(false);
    const [show2, setShow2] = useState(false);
    const [message, setMessage] = useState('');
    const [title, setTitle] = useState('');
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
        let api_endpoint_url = 'http://localhost:8080/event/cancelEventByManager';
        const bodyParameters = {
            event_id: id
        };
        try {
            const response = await axios.post(api_endpoint_url, bodyParameters, config);
            
            handleClose();
            setTitle(response.data.status ? 'Event cancelled' : 'Error');
            setMessage(response.data.message);
            handleShow2();
        } catch (error) {
            console.error('Error occurred:', error);
        }
    }

    return(
        <>
        {userData && userData.role === 'Event_Manager' &&(
            <Button className='w-100' style={{height: '60px'}} variant="danger" onClick={handleShow} disabled={event_status !== 'Active'}>
                <i className="fas fa-plus me-1 text-gray fw-normal"></i>Cancel Event
            </Button>
        )}
        <Modal show={show} onHide={handleClose}>
            <Modal.Header closeButton>
                <Modal.Title>Cancel Event</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <p>Are you sure you want to cancel this event?</p>
                <p>Note: All purchase tickets will be refunded.</p>
            </Modal.Body>
            <Modal.Footer>
                <Button variant="secondary" onClick={handleClose}>
                    Close
                </Button>
                <Button variant="danger" onClick={handleCancel}>
                    Cancel Event
                </Button>
            </Modal.Footer>
        </Modal>
        
        <Modal show={show2} onHide={handleClose2}>
            <Modal.Header closeButton>
                <Modal.Title>{title}</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <p>{message}</p>
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

export default CancelEvent;