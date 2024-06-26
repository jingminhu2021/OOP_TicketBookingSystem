import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Modal, Button, Toast } from 'react-bootstrap';

function CancelBooking(props){
    const ticket_id = props.ticket_id;

    const token = localStorage.getItem('token');
    const config = {
        headers: { Authorization: `Bearer ${token}` }
    };
    const [userData, setUserData] = useState(null);
    const [show, setShow] = useState(false);
    const [show2, setShow2] = useState(false);
    const [message, setMessage] = useState('');
    const [title, setTitle] = useState('');
    const [ticketDetails, setTicketDetails] = useState(null);
    const [isDisabled, setIsDisabled] = useState(false);

    useEffect(() => {
        
        const getUserData = async (token) => {
            let api_endpoint_url = 'http://localhost:8080/user/getLoggedInUser';
            
            try {
                const response = await axios.post(api_endpoint_url, "", config);
                setUserData(response.data);
            } catch (error) {
                console.error('Error occurred:', error);
            }
        };

        const getTicketDetails = async () => {

            let api_endpoint_url = 'http://localhost:8080/transaction/getTicketDetails';
            const bodyParameters = {
                ticket_id: ticket_id
            };

            try {
                const response = await axios.post(api_endpoint_url, bodyParameters, config);
                setTicketDetails(response.data);
                // console.log(response.data);
            } catch (error) {
                console.error('Error occurred:', error);
            }
        };

        if (token) {
            getUserData(token);
            getTicketDetails();
        }
        
    }, []); // Empty dependency array ensures this effect runs only once



    const handleClose = () => setShow(false);
    const handleShow = () => setShow(true);
    const handleClose2 = () => {
        setShow2(false);
        window.location.reload();
    }
    const handleShow2 = () => setShow2(true);

    const handleCancel = async () => {
        let api_endpoint_url = 'http://localhost:8080/transaction/cancellation';
        const bodyParameters = {
            ticket_id: ticket_id
        };
        try {
            const response = await axios.post(api_endpoint_url, bodyParameters, config);
                
            handleClose();
            setTitle(response.data.status ? 'Booking cancelled' : 'Cancellation failed');
            setMessage(response.data.message);
            handleShow2();
            setIsDisabled(true);

        } catch (error) {
            console.error('Error occurred:', error);
        }
    }

    return(
        <>
        {userData && userData.role === 'Customer' && ticketDetails ? (
            ticketDetails.status === 'active' ? (
                <Button variant="danger" onClick={handleShow} disabled={isDisabled}>
                    <i className="fas fa-plus me-1 text-gray fw-normal"></i>Cancel Booking
                </Button>
            ) : (
                <Button variant="danger" onClick={handleShow} disabled>
                    <i className="fas fa-plus me-1 text-gray fw-normal"></i>Cancel Booking
                </Button>
            )
        ) : null}
        <Modal show={show} onHide={handleClose}>
            <Modal.Header closeButton>
                <Modal.Title>Cancel Booking</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <p>Are you sure you want to cancel this booking?</p>
                <p>Note: Booking can only be cancel 48 hours before the event.</p>
            </Modal.Body>
            <Modal.Footer>
                <Button variant="secondary" onClick={handleClose}>
                    Close
                </Button>
                <Button variant="danger" onClick={handleCancel}>
                    Cancel Booking
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

export default CancelBooking;