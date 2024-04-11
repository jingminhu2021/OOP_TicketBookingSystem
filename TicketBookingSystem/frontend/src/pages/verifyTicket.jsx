import React, { useState, useEffect } from 'react';
import Navbar from '../components/navbar';
import { Form, Button, Modal } from 'react-bootstrap';
import axios from 'axios';
import { useLocation } from 'react-router-dom';

function VerifyTicket() {
    const token = localStorage.getItem('token');
    const config = {
        headers: { Authorization: `Bearer ${token}` }
    };

    const [userData, setUserData] = useState(null);
    const [show2, setShow2] = useState(false);
    const [message, setMessage] = useState('');
    
    const location = useLocation();
    const [customerDetails, setCustomerDetails] = useState({});
    const [ticketDetails, setTicketDetails] = useState({});
    const [ticketType, setTicketType] = useState({});
    const [eventDetails, setEventDetails] = useState({});

    const handleClose2 = () => {
        setShow2(false);
    }

    const [formData, setFormData] = useState({
        userId: '',
        eventId: '',
        ticketId: '',
        ticketOfficerId: '',
        ticketTypeId: ''
    });
    
    const getUserData = async () => {
        let api_endpoint_url = 'http://localhost:8080/user/getLoggedInUser';
        const bodyParameters = {
            key: "value"
        };
        try {
            const response = await axios.post(api_endpoint_url, bodyParameters, config);
            setUserData(response.data);
            // console.log('User Data:', response.data.user);
            setFormData(prevFormData => ({
                ...prevFormData,
                ticketOfficerId: response.data.user.id
            }));
            
        } catch (error) {
            console.error('Error occurred:', error);
        }
    };

    const getTicketDetails = async (userId, eventId, ticketId, ticketTypeId) => {
        let api_endpoint_url = 'http://localhost:8080/ticketType/getTicketDetails';
        const bodyParameters = {
            userId: userId,
            eventId: eventId,
            ticketId: ticketId,
            ticketTypeId: ticketTypeId
        };
        try {
            const response = await axios.post(api_endpoint_url, bodyParameters, config);
            // console.log('Response:', response.data);
            setTicketDetails(response.data);
            if (response.data.status){
                return true;
            }
            else{
                return false;
            }
            
        } catch (error) {
            console.error('Error:', error);
        }
    };

    const getCustomerDetails = async (userId) => {
        let api_endpoint_url = 'http://localhost:8080/user/get';
        const bodyParameters = {
            id: userId
        };
        try {
            const response = await axios.post(api_endpoint_url, bodyParameters, config);
            setCustomerDetails(response.data);
            // console.log('Response:', response.data);
        } catch (error) {
            console.error('Error:', error);
        }
    };

    const getTicketType = async (ticketTypeId) => {
        ticketTypeId = parseInt(ticketTypeId);
        let api_endpoint_url = 'http://localhost:8080/ticketType/viewSingleTicketType';
        const bodyParameters = {
            ticketTypeId: ticketTypeId
        };
        try {
            const response = await axios.post(api_endpoint_url, bodyParameters, config);
            setTicketType(response.data);
            // console.log('Response:', response.data);
        } catch (error) {
            console.error('Error:', error);
        }
    };

    const getEventDetails = async (eventId) => {
        eventId = parseInt(eventId);
        let api_endpoint_url = `http://localhost:8080/event/viewEvent?id=${eventId}`;
      
        try {
            const response = await axios.get(api_endpoint_url, config);
            setEventDetails(response.data.event);
            // console.log('Response:', response.data.event);
        } catch (error) {
            console.error('Error:', error);
        }
      };

      function formatDate(dateTimeArray) {
        const [year, month, day] = dateTimeArray.slice(0, 3);
        return `${year}-${month.toString().padStart(2, '0')}-${day.toString().padStart(2, '0')}`;
    }
    
    function formatTime(dateTimeArray) {
        const [hour, minute] = dateTimeArray.slice(3, 5);
        if(hour>12){
            return `${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')} pm`;
        }else{
            return `${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')} am`;
        }
    }

    useEffect(() => {
   
        const fetchData = async () => {
            
            try {
                // Check if login
                if (token) {    
                    await getUserData(token);

                    // Parse query parameters from URL
                    const searchParams = new URLSearchParams(location.search);
                    const encryptedParams = searchParams.toString() || '';
                    // console.log('Encrypted Parameters:', encryptedParams);
        
                    // Decrypt the encrypted url parameters
                    const response = await axios.post(
                        'http://localhost:8080/transaction/decryptParams',
                        { encryptedText: encryptedParams },
                        {
                            headers: {
                                Authorization: `Bearer ${token}`
                            }
                        }
                    );
                    const decryptedParams = response.data;
                    const params = new URLSearchParams("?"+decryptedParams);

                    // Update form data with decrypted parameters
                    console.log('Decrypted Parameters:', params.toString());
                    setFormData({
                        ...formData,
                        userId: params.get('userId'),
                        eventId: params.get('eventId'),
                        ticketId: params.get('ticketId'),
                        ticketTypeId: params.get('ticketTypeId')
                    });
        
                    // check if contains query parameters
                    if (window.location.search !== '') {
                        await getCustomerDetails(params.get('userId'));
                        await getTicketDetails(params.get('userId'), params.get('eventId'), params.get('ticketId'),params.get('ticketTypeId'));
                        await getTicketType(params.get('ticketTypeId'));
                        await getEventDetails(params.get('eventId'));
                    }

                }

            } catch (error) {
                console.error('Error:', error);
            }
        };

        fetchData();
    }, [location.search, token]); // Include token in dependency array

    const handleChange = (event) => {
        const { name, value } = event.target;
        // console.log(`Input ${name} changed to: ${value}`);
        setFormData({
            ...formData,
            [name]: value
        });

        let user_id = formData.userId;
        let event_id = formData.eventId;
        let ticket_id = formData.ticketId;
        let ticket_type_id = formData.ticketTypeId;

        if (name === 'userId'){
            user_id = value;
        }
        else if (name === 'eventId'){
            event_id = value;
        }
        else if (name === 'ticketId'){
            ticket_id = value;
        }
        else if (name === 'ticketTypeId'){
            ticket_type_id = value;
            
        }
        // Call the get functions to retrieve the details when form data all filled up
        if (user_id && event_id && ticket_id && ticket_type_id){
            getTicketDetails(user_id, event_id, ticket_id, ticket_type_id)
            .then((res) => {
                if (res){
                    getCustomerDetails(user_id);
                    getTicketType(ticket_type_id);
                    getEventDetails(event_id);
                    console.log('Valid ticket');
                } else {
                    // clear the details if ticket is invalid
                    setCustomerDetails({});
                    setTicketType({});
                    setEventDetails({});
                    console.log('Invalid ticket');
                }
            }) .catch((error) => {
                console.error('Error:', error);
            });
        }
    };

    const handleSubmit = async (event) => {
        event.preventDefault();
        try {    
            const updatedFormData = {
                ...formData,
                ticketOfficerId: userData.user.id
            };
            const response = await axios.post(
                'http://localhost:8080/ticketType/verifyTicket', 
                updatedFormData, // Use updatedFormData here
                {
                    headers: {
                        Authorization: `Bearer ${token}`
                    }
                }
            );
            setMessage(response.data.message);
            setShow2(true);
            console.log('API Response:', response.data);
        } catch (error) {
            // Handle error
            setMessage(error.message);
            setShow2(true);
            console.error('Error:', error);
        }
    };
    

    return (
        <>
            <Navbar />

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

                {userData && (userData.role === 'Ticketing_Officer') ? (

                <section className="py-5">
                <div className="container p-0">
                    <div className="row">
                        <div className="col-lg-7">
                            <div className="container border p-4">
                                <h5 className="mb-4">User Information</h5>
                                <Form onSubmit={handleSubmit}>
                                    <div className="mb-3">
                                        <label htmlFor="userId" className="form-label">User ID</label>
                                        <input type="text" className="form-control" id="userId" name="userId" value={formData.userId} onChange={handleChange} placeholder="Enter user ID" required />
                                    </div>
                                    <div className="mb-3">
                                        <label htmlFor="eventId" className="form-label">Event ID</label>
                                        <input type="text" className="form-control" id="eventId" name="eventId" value={formData.eventId} onChange={handleChange} placeholder="Enter event ID" required />
                                    </div>
                                    <div className="mb-3">
                                        <label htmlFor="ticketId" className="form-label">Ticket ID</label>
                                        <input type="text" className="form-control" id="ticketId" name="ticketId" value={formData.ticketId} onChange={handleChange} placeholder="Enter ticket ID" required />
                                    </div>
                                    <div className="mb-3">
                                        <label htmlFor="ticketTypeId" className="form-label">Ticket Type ID</label>
                                        <input type="text" className="form-control" id="ticketTypeId" name="ticketTypeId" value={formData.ticketTypeId} onChange={handleChange} placeholder="Enter ticket type ID" required />
                                    </div>
                                        
                                    <Button type="submit" variant="info" className="w-100 p-2 rounded">Verify Ticket</Button>

                                </Form>
                            </div>
                        </div>
                        {/* Display ticket details if all form data is filled up */}
                        {ticketDetails.status && formData.userId !== '' && formData.eventId !== '' && formData.ticketId !== '' && formData.ticketTypeId !== '' ? (
                            <div className="col-lg-5">
                                <div className="container border p-4 rounded">
                                    <h5 className="mb-4 text-center">Ticket Details</h5>
                                    <table className="table table-striped">
                                        <tbody>
                                            <tr>
                                                <td><strong>Ticket Booked by:</strong></td>
                                                <td>{customerDetails.name}</td>
                                            </tr>
                                            <tr>
                                                <td><strong>Event:</strong></td>
                                                <td>{eventDetails.eventName}</td>
                                            </tr>
                                            <tr>
                                                <td><strong>Venue:</strong></td>
                                                <td>{eventDetails.eventType}</td>
                                            </tr>
                                            <tr>
                                                <td><strong>Ticket Category:</strong></td>
                                                <td>{ticketType.eventCat}</td>
                                            </tr>
                                            <tr>
                                                <td><strong>Date of Event:</strong></td>
                                                <td>
                                                    {eventDetails.dateTime ? 
                                                        `${formatDate(eventDetails.dateTime)}, ${formatTime(eventDetails.dateTime)}` : 
                                                        'N/A'
                                                    }
                                                </td>
                                            </tr>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        ) : null}
                    </div>
                </div>
                </section>

                ) : (
                    <p><br/>Please log in as a Ticketing Officer to verify ticket.</p>
                )}

            </div>

            <Modal show={show2} onHide={handleClose2}>
                <Modal.Header closeButton>
                    <Modal.Title>Notification</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <p>{message}</p>
                </Modal.Body>
            </Modal>
        </>
    );
}

export default VerifyTicket;
