import React, { useState, useEffect } from 'react';
import Navbar from '../components/navbar';
import { Toast } from 'react-bootstrap';
import axios from 'axios';
import { useLocation } from 'react-router-dom';

function ViewBookingHistory() {
    const token = localStorage.getItem('token');
    const [showToast, setShowToast] = useState(false);
    const [toastMessage, setToastMessage] = useState('');
    const [apiResponse, setApiResponse] = useState(null);

    useEffect(() => {
        const fetchBookingHistory = async () => {
            try {
                const response = await axios({
                    method: 'post',
                    url: 'http://localhost:8080/transaction/bookingHistory',
                    headers: {
                        Authorization: `Bearer ${token}`,
                    },
                });

                setApiResponse(response.data);
                console.log('API Response:', response.data);
            } catch (error) {
                console.error('Error:', error);
                setToastMessage(error.message);
                setShowToast(true);
            }
        };

        fetchBookingHistory();
    }, []);

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
                                <h1 className="h2 text-uppercase mb-0">Booking History</h1>
                            </div>
                            <div className="col-lg-6 text-lg-end">
                                <nav aria-label="breadcrumb">
                                    <ol className="breadcrumb justify-content-lg-end mb-0 px-0 bg-light">
                                        <li className="breadcrumb-item"><a className="text-dark" href="/">Home</a></li>
                                        <li className="breadcrumb-item active">Booking History</li>
                                    </ol>
                                </nav>
                            </div>
                        </div>
                    </div>
                </section>
                <section className="py-5">
                    <div className="row">
                        <div className="col">
                            <div className="mb-3">
                                <div className="api-response">
                                    {apiResponse &&
                                        <div>
                                            <table className="table">
                                                <thead>
                                                    <tr>
                                                        {Object.keys(apiResponse[0]).map((key) => (
                                                            <th key={key}>{key}</th>
                                                        ))}
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    {apiResponse.map((row, index) => (
                                                        <tr key={index}>
                                                            {Object.values(row).map((value, i) => (
                                                                <td key={i}>{value}</td>
                                                            ))}
                                                        </tr>
                                                    ))}
                                                </tbody>
                                            </table>
                                        </div>
                                    }
                                </div>
                            </div>
                        </div>
                    </div>
                </section>
            </div>
        </>
    );
}

export default ViewBookingHistory;
