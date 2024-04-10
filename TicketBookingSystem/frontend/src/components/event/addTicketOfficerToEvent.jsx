import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Table, Button, Modal } from 'react-bootstrap';

function AddTicketOfficerToEvent(props) {
    const event_id = props.event_id;
    const event_status = props.event_status;
    const event_name = props.event_name;
    const token = localStorage.getItem('token');
    const config = {
        headers: { Authorization: `Bearer ${token}` }
    };
    const [userData, setUserData] = useState(null);
    const [AssignedTicketingOfficers, setAssignedTicketingOfficers] = useState([]);
    const [AllTicketingOfficers, setAllTicketingOfficers] = useState([]);
    const [show, setShow] = useState(false);
    const [show2, setShow2] = useState(false); // State for the second modal
    const [resultsMessage, setResultsMessage] = useState('');
    const handleShow = () => setShow(true);
    const handleClose = () => setShow(false);
    const handleClose2 = () => setShow2(false); // Close the second modal

    useEffect(() => {
        
        const getUserData = async (token) => {
            let api_endpoint_url = 'http://localhost:8080/user/getLoggedInUser';
            
            const bodyParameters = {};
            try {
                const response = await axios.post(api_endpoint_url, bodyParameters, config);
                setUserData(response.data);
            } catch (error) {
                console.error('Error occurred:', error);
            }
        };

        // Get ticket officers who are already assigned to the event
        const getTicketingOfficers = async (event_id) => {
            let api_endpoint_url = 'http://localhost:8080/event/viewTicketingOfficerByEventId';
            
            const bodyParameters = {
                eventId: event_id
            };
        
            try {
                const response = await axios.post(api_endpoint_url, bodyParameters, config);
                const officers = response.data.ticketingOfficers || [];
                
                // Fetch name for each officer asynchronously
                const updatedOfficers = await Promise.all(officers.map(async (officer) => {
                    let api_endpoint_url_user = 'http://localhost:8080/user/get';
                    const userBodyParameters = {
                        id: officer.userId
                    };
                    const userResponse = await axios.post(api_endpoint_url_user, userBodyParameters, config);
                    return {
                        ...officer,
                        name: userResponse.data.name
                    };
                }));
                
                setAssignedTicketingOfficers(updatedOfficers);
                // console.log('Ticketing officers:', updatedOfficers);
            } catch (error) {
                console.error('Error occurred:', error);
            }
        };

        const getAllTicketingOfficers = async () => {
            let api_endpoint_url = 'http://localhost:8080/user/viewAllTicketingOfficer';
            const bodyParameters = {};
            try {
                const response = await axios.post(api_endpoint_url, bodyParameters, config);
                setAllTicketingOfficers(response.data.ticketingOfficers);
                // console.log('All ticketing officers:', response.data);
            } catch (error) {
                console.error('Error occurred:', error);
            }
        };

        if (token) {
            getUserData(token);
            getTicketingOfficers(event_id);
            getAllTicketingOfficers();
        }
        
    }, []);

    const addTicketingOfficerToEvent = async (event_id, user_id) => {
        let api_endpoint_url = 'http://localhost:8080/user/setTicketOfficer';

        const bodyParameters = {
            userId: user_id,
            eventId: event_id
        };

        try {
            const response = await axios.post(api_endpoint_url, bodyParameters, config);
            // Show the second modal with the success message
            setShow2(true);
            setResultsMessage(response.data.message);
            console.log('Response:', response.data);
        } catch (error) {
            console.error('Error occurred:', error);
        }
    };

    const handleAddOfficer = async (event) => {
        event.preventDefault();
        // Get the selected ticketing officer ID from the dropdown
        const selectedOfficerId = document.getElementById('ticketingOfficerSelect').value;
    
        // Find the selected officer from AllTicketingOfficers
        const selectedOfficer = AllTicketingOfficers.find(officer => officer.id === parseInt(selectedOfficerId));
        if (selectedOfficer) {
            try {
                // Call addTicketingOfficerToEvent to add the officer to the event
                await addTicketingOfficerToEvent(event_id, parseInt(selectedOfficerId));
    
                // Add the selected officer's ID along with other officer details
                setAssignedTicketingOfficers(prevOfficers => [...prevOfficers, { ...selectedOfficer, userId: parseInt(selectedOfficerId) }]);
            } catch (error) {
                console.error('Error occurred while adding officer:', error);
            }
        }
    };    

    return (
        <>
            {userData && userData.role === 'Event_Manager' && (
                <Button className='w-100' style={{height: '60px'}} variant="warning" onClick={handleShow} disabled={event_status !== 'Active'}>
                    <i className="fas fa-plus me-1 text-gray fw-normal"></i> Ticketing Officer
                </Button>
            )}

            <Modal show={show} onHide={handleClose}>
                <Modal.Header closeButton>
                    <Modal.Title>Ticketing Officers</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <div className="bg-light p-2 text-center mb-2">
                        <span className=''>Event</span>
                        <h3 className="font-weight-bold text-uppercase">{event_name}</h3>
                    </div>
                    <p className='mt-5'>Ticketing Officer in charge of this event: </p>
                    {AssignedTicketingOfficers.length > 0 ? (
                        <Table striped bordered hover>
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Name</th>
                                </tr>
                            </thead>
                            <tbody>
                                {AssignedTicketingOfficers.map(officer => (
                                    <tr key={officer.userId}>
                                        <td>{officer.userId}</td>
                                        <td>{officer.name}</td>
                                    </tr>
                                ))}
                            </tbody>
                        </Table>
                    ) : (
                        <p className="text-center mt-4 text-danger">No officer is assigned to this event yet.</p>
                    )}

                    <div className="mb-3">
                        <select id="ticketingOfficerSelect" className="form-select">
                            <option value="">
                                    {AssignedTicketingOfficers.length === AllTicketingOfficers.length || AllTicketingOfficers.length === 0
                                        ? "All ticket officers have been allocated to the event"
                                        : "Select Ticketing Officer"}
                            </option>
                            
                            {AllTicketingOfficers.filter(officer => !AssignedTicketingOfficers.some(assigned => assigned.userId === officer.id)).map(officer => (
                                <option key={officer.id} value={officer.id}>{officer.name}</option>
                            ))}
                        </select>
                    </div>
                    <Button variant="primary" onClick={handleAddOfficer} disabled={AssignedTicketingOfficers.length === AllTicketingOfficers.length || AllTicketingOfficers.length === 0} className="w-100">
                        Add
                    </Button>

                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={handleClose}>
                        Close
                    </Button>
                </Modal.Footer>
            </Modal>

            <Modal show={show2} onHide={handleClose2}>
                <Modal.Header closeButton>
                    <Modal.Title>Update Ticket Result</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <p>{resultsMessage}</p>
                </Modal.Body>
            </Modal>

        </>
    );
}

export default AddTicketOfficerToEvent;
