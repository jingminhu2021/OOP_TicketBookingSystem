import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Table, Button, Modal } from 'react-bootstrap';
import UpdateTicketType from './updateTicketType';
import CreateTicketType from './createTicketType';

function ViewTicketOptionsForManager(props) {
    const event_id = props.event_id;
    const event_status = props.event_status;
    const token = localStorage.getItem('token');
    const config = {
        headers: { Authorization: `Bearer ${token}` }
    };
    const [userData, setUserData] = useState(null);
    const [ticketOptions, setTicketOptions] = useState([]);
    const [show, setShow] = useState(false);
    const handleShow = () => setShow(true);
    const handleClose = () => setShow(false);

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

        const getTicketOptions = async (event_id) => {
            let api_endpoint_url = 'http://localhost:8080/ticketType/viewTicketTypes';
            
            const bodyParameters = {
                event_id: event_id
            };

            axios.post(api_endpoint_url, bodyParameters, config)
            .then((response) => {
                setTicketOptions(prevResults => [...prevResults, ...response.data]);
            })
            .catch((error) => {
                console.error('Error occurred:', error);
            });
            
        }

        if (token) {
            getUserData(token);
            getTicketOptions(event_id);
            
        }
        
    }, []); // Empty dependency array ensures this effect runs only once

    

    return(
        <>
        {userData && userData.role === 'Event_Manager' &&(
        <Button className='w-100' style={{height: '60px'}} variant="primary" onClick={handleShow} disabled={event_status !== 'Active'}>
            <i className="fas fa-plus me-1 text-gray fw-normal"></i> Edit
        </Button>
        )}
        <Modal show={show} onHide={handleClose}>
            <Modal.Header closeButton>
                <Modal.Title>Ticket Details</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                {CreateTicketType(event_id)}
                <Table>
                    <thead>
                        <tr>
                            <th>Ticket Type</th>
                            <th>Event Price</th>
                            <th>Number of Tickets left</th>
                            <th>Cancellation Fee Percentage</th>
                            <th></th>
                        </tr>
                    </thead>
                    <tbody>

                        {ticketOptions.map((ticketOption, index) => {
                            // console.log(ticketOption)
                            return(
                                <tr key={index}>
                                    <td>{ticketOption.eventCat}</td>
                                    <td>{ticketOption.eventPrice}</td>
                                    <td>{ticketOption.numberOfTix}</td>
                                    <td>{ticketOption.cancellationFeePercentage}</td>
                                    <td>
                                        <UpdateTicketType ticketId={ticketOption.ticketTypeId} />
                                        {/* <Button variant="danger" onClick={() => {}}>Delete</Button> */}
                                    </td>
                                </tr>
                            )
                        })}
                    </tbody>
                </Table>
            </Modal.Body>
        </Modal>
            
            
        </>
    )
}
export default ViewTicketOptionsForManager;