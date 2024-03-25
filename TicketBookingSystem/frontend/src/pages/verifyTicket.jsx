import React, { useState, useEffect} from 'react';
import Navbar from '../components/navbar';
import { BrowserRouter, useLocation } from 'react-router-dom'
import axios from 'axios';
import { Form, Modal, Alert, Button, Toast } from 'react-bootstrap';

function VerifyTicket() {
    const token = sessionStorage.getItem('token');
    // const token = localStorage.getItem('token');
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
                console.log(response.data.role)
            } catch (error) {
                console.error('Error occurred:', error);
            }
        };
        if (token) {
            getUserData(token);
        }
    }, []); // Empty dependency array ensures this effect runs only once

    const [showToast, setShowToast] = useState(false);
    const [toastMessage, setToastMessage] = useState('');
    const location = useLocation();

    // Parse query parameters from URL
    const searchParams = new URLSearchParams(location.search);
    const userId = searchParams.get('userId') || '';
    const eventId = searchParams.get('eventId') || '';
    const ticketId = searchParams.get('ticketId') || '';
    const ticketOfficerId = searchParams.get('ticketOfficerId') || '';
    const ticketTypeId = searchParams.get('ticketTypeId') || '';

    const [formData, setFormData] = useState({
        userId: userId,
        eventId: eventId,
        ticketId: ticketId,
        ticketOfficerId: ticketOfficerId,
        ticketTypeId: ticketTypeId
    });

    const handleChange = (event) => {
        const { name, value } = event.target;
        setFormData({
            ...formData,
            [name]: value
        });
    };

    const handleSubmit = async (event) => {
        event.preventDefault();
        try {
            const response = await axios.get('http://localhost:8080/user/verifyTicket', {
                params: formData,
                headers: {
                    Authorization: `Bearer ${token}`
                }
            });
            setToastMessage(response.data.message);
            setShowToast(true);
            console.log('API Response:', response.data);
        } catch (error) {
            // Handle error
            setToastMessage(error.message);
            setShowToast(true);
            console.error('Error:', error);
        }
    };

    return (
        <>
            <Navbar />

            <Toast onClose={() => setShowToast(false)} style={{ position: 'fixed', left: '20px', transform: 'translateY(-50%)', zIndex: 9999 }} show={showToast} delay={3000} autohide>
                <Toast.Header>
                    <strong className="me-auto">Notification</strong>
                </Toast.Header>
                <Toast.Body>{toastMessage}</Toast.Body>
            </Toast>

            <div className="container">
                <section className="py-5 bg-light">
                    <div className="container">
                        <div className="row px-4 px-lg-5 py-lg-4 align-items-center">
                            <div className="col-lg-6">
                                <h1 className="h2 text-uppercase mb-0">Verify Ticket</h1>
                            </div>
                            <div className="col-lg-6 text-lg-end">
                                <nav aria-label="breadcrumb">
                                    <ol className="breadcrumb justify-content-lg-end mb-0 px-0 bg-light">
                                        <li className="breadcrumb-item"><a className="text-dark" href="/">Home</a></li>
                                        <li className="breadcrumb-item active">Verify Ticket</li>
                                    </ol>
                                </nav>
                            </div>
                        </div>
                    </div>
                </section>
                <section className="py-5">
                    <div className="row">
                        <div className="col">
                            <Form onSubmit={handleSubmit}>
                                <div className="mb-3">
                                    <label htmlFor="userId" className="form-label">User ID</label>
                                    <input type="text" className="form-control" id="userId" name="userId" value={formData.userId} onChange={handleChange} placeholder="Enter user ID" />
                                </div>
                                <div className="mb-3">
                                    <label htmlFor="eventId" className="form-label">Event ID</label>
                                    <input type="text" className="form-control" id="eventId" name="eventId" value={formData.eventId} onChange={handleChange} placeholder="Enter event ID" />
                                </div>
                                <div className="mb-3">
                                    <label htmlFor="ticketId" className="form-label">Ticket ID</label>
                                    <input type="text" className="form-control" id="ticketId" name="ticketId" value={formData.ticketId} onChange={handleChange} placeholder="Enter ticket ID" />
                                </div>
                                <div className="mb-3">
                                    <label htmlFor="ticketOfficerId" className="form-label">Ticket Officer ID</label>
                                    <input type="text" className="form-control" id="ticketOfficerId" name="ticketOfficerId" value={formData.ticketOfficerId} onChange={handleChange} placeholder="Enter ticket officer ID" />
                                </div>
                                <div className="mb-3">
                                    <label htmlFor="ticketTypeId" className="form-label">Ticket Type ID</label>
                                    <input type="text" className="form-control" id="ticketTypeId" name="ticketTypeId" value={formData.ticketTypeId} onChange={handleChange} placeholder="Enter ticket type ID" />
                                </div>
                                <button type="submit" className="btn btn-dark">Verify Ticket</button>
                            </Form>
                        </div>
                    </div>
                </section>
            </div>
        </>
    );
}

export default VerifyTicket;
