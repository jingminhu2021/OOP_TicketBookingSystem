import React, { useState, useEffect } from 'react';
import Navbar from '../components/navbar';
import { Form, Toast } from 'react-bootstrap';
import axios from 'axios';
import { useLocation } from 'react-router-dom';

function VerifyTicket() {
    const token = localStorage.getItem('token');
    const [showToast, setShowToast] = useState(false);
    const [toastMessage, setToastMessage] = useState('');
    const location = useLocation();

    const [formData, setFormData] = useState({
        userId: '',
        eventId: '',
        ticketId: '',
        ticketOfficerId: '',
        ticketTypeId: ''
    });

    // Decrypt the encrypted params
    const CryptoJS = require("crypto-js");
    function decrypt(encryptedText) {
        try {
            const ENCRYPTION_KEY = process.env.REACT_APP_SECRET_KEY;

            // Decode the URL-encoded ciphertext
            const decodedEncryptedText = decodeURIComponent(encryptedText);

            const encryptedBytes = CryptoJS.enc.Base64.parse(decodedEncryptedText);
            const cipherParams = CryptoJS.lib.CipherParams.create({
                ciphertext: encryptedBytes
            });
    
            const decryptedBytes = CryptoJS.AES.decrypt(
                cipherParams,
                CryptoJS.enc.Utf8.parse(ENCRYPTION_KEY),
                {
                    mode: CryptoJS.mode.ECB,
                    padding: CryptoJS.pad.Pkcs7
                }
            );
    
            return decryptedBytes.toString(CryptoJS.enc.Utf8);
        } catch (error) {
            console.error("Error decrypting:", error.message);
            return null;
        }
    }

    // Parse query parameters from URL
    const searchParams = new URLSearchParams(location.search);
    const encryptedParams = searchParams.toString() || '';
    const encryptedText = encryptedParams;
    // Decrypt the encrypted url parameters
    const decryptedText = decrypt(encryptedText);

    useEffect(() => {
        if (decryptedText) {
            const params = new URLSearchParams("?"+decryptedText);
            setFormData({
                userId: params.get('userId') || '',
                eventId: params.get('eventId') || '',
                ticketId: params.get('ticketId') || '',
                ticketOfficerId: params.get('ticketOfficerId') || '',
                ticketTypeId: params.get('ticketTypeId') || ''
            });
        }
    }, [decryptedText]);

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
