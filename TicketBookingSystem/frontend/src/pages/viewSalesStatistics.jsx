import React, { useState, useEffect } from 'react';
import Navbar from '../components/navbar';
import { Form, Toast } from 'react-bootstrap';
import axios from 'axios';
import { useLocation } from 'react-router-dom';

function ViewSalesStatistics() {
    const token = localStorage.getItem('token');
    const [showToast, setShowToast] = useState(false);
    const [toastMessage, setToastMessage] = useState('');
    const [apiResponse, setApiResponse] = useState(null);
    const [eventNames, setEventNames] = useState([]);

    const [formData, setFormData] = useState({
        managerId: '',
        eventName: '',
    });

    useEffect(() => {
        const fetchEventNames = async () => {
            try {
                const response = await axios.get('http://localhost:8080/event/viewAllEvents', {
                    headers: {
                        Authorization: `Bearer ${token}`
                    }
                });
                setEventNames(response.data.map(event => event.eventName));
            } catch (error) {
                console.error('Error:', error);
                setToastMessage(error.message);
                setShowToast(true);
            }
        };
    
        fetchEventNames();
    }, []);

    const handleChange = (event) => {
        const { name, value } = event.target;
        setFormData(prevFormData => ({
            ...prevFormData,
            [name]: value
        }));
    };

    const handleSubmit = async (event) => {
        event.preventDefault();
        try {
            const response = await axios({
                method: 'post',
                url: 'http://localhost:8080/event/viewSalesStatistics',
                headers: {
                    Authorization: `Bearer ${token}`,
                    'Content-Type': 'text/plain'
                },
                data: {
                    'managerId': formData.managerId,
                    'eventName': formData.eventName,
                },
            });
            setApiResponse(response.data);
            console.log('API Response:', response.data);
        } catch (error) {
            // Handle error
            setToastMessage(error.message);
            setShowToast(true);
            console.error('Error:', error);
            console.log(1)
        }
    };

    const exportToCSV = async () => {
        try {
            const response = await axios({
                method: 'post',
                url: 'http://localhost:8080/event/getCSV',
                headers: {
                    Authorization: `Bearer ${token}`,
                },
                data: {
                    'managerId': formData.managerId,
                    'eventName': formData.eventName,
                },
                responseType: 'blob', // Important
            });
    
            // Create a blob from the API response
            const url = window.URL.createObjectURL(new Blob([response.data]));
    
            // Create a link and trigger the download
            const link = document.createElement('a');
            link.href = url;
            link.setAttribute('download', 'data.csv'); // or any other extension
            document.body.appendChild(link);
            link.click();
        } catch (error) {
            console.error('Error:', error);
            setToastMessage(error.message);
            setShowToast(true);
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
                                    <label htmlFor="managerId" className="form-label">Manager ID</label>
                                    <input type="text" className="form-control" id="managerId" name="managerId" value={formData.managerId} onChange={handleChange} placeholder="Enter user ID" />
                                </div>
                                {/* <div className="mb-3">
                                    <label htmlFor="eventName" className="form-label">Event Name</label>
                                    <input type="text" className="form-control" id="eventName" name="eventName" value={formData.eventName} onChange={handleChange} placeholder="Enter event ID" />
                                </div> */}
                                <div className="mb-3">
                                    <label htmlFor="eventNameDropdown" className="form-label">Event Name</label>
                                    <select className="form-control" id="eventNameDropdown" name="eventName" value={formData.eventName} onChange={handleChange}>
                                        <option value="">Select event</option>
                                        {eventNames && eventNames.map((eventName, index) => (
                                            <option key={index} value={eventName}>{eventName}</option>
                                        ))}
                                    </select>
                                </div>
                                <button type="submit" className="btn btn-dark">Show event statistics</button>
                            </Form>
                        </div>
                    </div>
                </section>
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
                            <button onClick={exportToCSV} className="btn btn-dark">Export to CSV</button>
                        </div>
                    }
                </div>
            </div>
        </>
    );
}

export default ViewSalesStatistics;
